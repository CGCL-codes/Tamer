package org.thechiselgroup.choosel.core.client.visualization.model.managed;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.thechiselgroup.choosel.core.client.util.ObjectUtils;
import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightList;
import org.thechiselgroup.choosel.core.client.util.event.EventHandlerPriority;
import org.thechiselgroup.choosel.core.client.util.event.PrioritizedEventHandler;
import org.thechiselgroup.choosel.core.client.visualization.model.Slot;
import org.thechiselgroup.choosel.core.client.visualization.model.SlotMappingChangedEvent;
import org.thechiselgroup.choosel.core.client.visualization.model.SlotMappingChangedHandler;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemContainerChangeEvent;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemContainerChangeEventHandler;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemResolutionErrorModel;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemValueResolver;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualizationModel;
import com.google.gwt.event.shared.HandlerManager;

public class DefaultManagedSlotMappingConfiguration implements ManagedSlotMappingConfiguration {

    private final class VisualMappingUpdaterForVisualItemChanges implements VisualItemContainerChangeEventHandler, PrioritizedEventHandler {

        @Override
        public EventHandlerPriority getPriority() {
            return EventHandlerPriority.FIRST;
        }

        @Override
        public void onVisualItemContainerChanged(VisualItemContainerChangeEvent event) {
            if (event == null || event.getDelta().isEmpty()) {
                return;
            }
            Map<Slot, ManagedSlotMappingState> newStates = updateVisualMappings();
            eventBus.fireEvent(new ManagedSlotMappingConfigurationChangedEvent(newStates, getVisualItems()));
        }
    }

    private VisualItemValueResolverFactoryProvider resolverProvider;

    private Map<Slot, ManagedSlotMapping> slotsToSlotMappings = new HashMap<Slot, ManagedSlotMapping>();

    private SlotMappingInitializer slotMappingInitializer;

    private VisualizationModel visualizationModel;

    private final VisualItemResolutionErrorModel errorModel;

    private HandlerManager eventBus;

    public DefaultManagedSlotMappingConfiguration(VisualItemValueResolverFactoryProvider resolverProvider, SlotMappingInitializer slotMappingInitializer, VisualizationModel visualizationModel, VisualItemResolutionErrorModel errorModel) {
        assert resolverProvider != null;
        assert slotMappingInitializer != null;
        assert visualizationModel != null;
        assert errorModel != null;
        this.resolverProvider = resolverProvider;
        this.slotMappingInitializer = slotMappingInitializer;
        this.visualizationModel = visualizationModel;
        this.errorModel = errorModel;
        this.eventBus = new HandlerManager(this);
        initSlotModels(visualizationModel.getSlots());
        visualizationModel.addHandler(new SlotMappingChangedHandler() {

            @Override
            public void onSlotMappingChanged(SlotMappingChangedEvent e) {
                assert e.getCurrentResolver() instanceof ManagedVisualItemValueResolver : "tried to set unmanaged resolver for " + e.getSlot();
                if (ObjectUtils.equals(e.getOldResolver(), e.getCurrentResolver())) {
                    return;
                }
                handleResolverChange(e.getSlot(), e.getOldResolver(), e.getCurrentResolver());
                LightweightList<Slot> slots = CollectionFactory.createLightweightList();
                slots.add(e.getSlot());
                eventBus.fireEvent(new ManagedSlotMappingConfigurationChangedEvent(generateSlotMappingStateSnapshot(slots, getVisualItems()), getVisualItems()));
            }
        });
        visualizationModel.getFullVisualItemContainer().addHandler(new VisualMappingUpdaterForVisualItemChanges());
    }

    @Override
    public void addManagedSlotMappingConfigurationChangedEventHandler(ManagedSlotMappingConfigurationChangedEventHandler handler) {
        assert handler != null;
        eventBus.addHandler(ManagedSlotMappingConfigurationChangedEvent.TYPE, handler);
    }

    private Map<Slot, ManagedSlotMappingState> generateSlotMappingStateSnapshot(LightweightCollection<Slot> slots, LightweightCollection<VisualItem> visualItems) {
        Map<Slot, ManagedSlotMappingState> oldStates = new HashMap<Slot, ManagedSlotMappingState>();
        for (Slot slot : slots) {
            ManagedSlotMapping managedSlotMapping = getManagedSlotMapping(slot);
            ManagedVisualItemValueResolver resolver;
            boolean isAllowable;
            boolean isConfigured;
            if (isConfigured = visualizationModel.isConfigured(slot)) {
                resolver = getCurrentResolver(slot);
                isAllowable = managedSlotMapping.isAllowableResolver(resolver, visualItems);
            } else {
                resolver = null;
                isAllowable = false;
            }
            oldStates.put(slot, new ManagedSlotMappingState(slot, resolver, isConfigured, isAllowable, managedSlotMapping.getAllowableResolverFactories()));
        }
        return oldStates;
    }

    @Override
    public ManagedVisualItemValueResolver getCurrentResolver(Slot slot) {
        assert slotsToSlotMappings.containsKey(slot) : slot;
        return slotsToSlotMappings.get(slot).getCurrentResolver();
    }

    @Override
    public ManagedSlotMapping getManagedSlotMapping(Slot slot) {
        return slotsToSlotMappings.get(slot);
    }

    @Override
    public LightweightList<ManagedSlotMapping> getManagedSlotMappings() {
        LightweightList<ManagedSlotMapping> managedMappings = CollectionFactory.createLightweightList();
        managedMappings.addAll(slotsToSlotMappings.values());
        return managedMappings;
    }

    @Override
    public Slot getSlotById(String slotId) {
        return visualizationModel.getSlotById(slotId);
    }

    @Override
    public Slot[] getSlots() {
        return visualizationModel.getSlots();
    }

    public LightweightList<Slot> getSlotsWithInvalidResolvers() {
        LightweightList<Slot> invalidSlots = CollectionFactory.createLightweightList();
        for (Entry<Slot, ManagedSlotMapping> entry : slotsToSlotMappings.entrySet()) {
            if (!slotHasInvalidResolver(entry.getKey())) {
                invalidSlots.add(entry.getKey());
            }
        }
        return invalidSlots;
    }

    public LightweightCollection<VisualItem> getVisualItems() {
        return visualizationModel.getFullVisualItemContainer().getVisualItems();
    }

    public void handleResolverChange(Slot slot, VisualItemValueResolver oldResolver, VisualItemValueResolver resolver) {
        assert visualizationModel.containsSlot(slot) : "slot " + slot + " is not a part of the " + "visualization model";
        slotsToSlotMappings.get(slot).currentResolverWasSet(oldResolver, resolver, getVisualItems());
    }

    private void initSlotModels(Slot[] slots) {
        for (Slot slot : slots) {
            slotsToSlotMappings.put(slot, new ManagedSlotMapping(slot, resolverProvider, visualizationModel, errorModel));
        }
    }

    @Override
    public boolean isConfigured(Slot slot) {
        return visualizationModel.isConfigured(slot);
    }

    /**
     * This method returns the new potential states of the slotMappings
     */
    private Map<Slot, ManagedSlotMappingState> resetMappingsFromInitializer(LightweightCollection<Slot> unresolvableSlots, LightweightCollection<VisualItem> visualItems) {
        assert !unresolvableSlots.isEmpty();
        Map<Slot, ManagedSlotMappingState> states = generateSlotMappingStateSnapshot(unresolvableSlots, visualItems);
        for (Entry<Slot, VisualItemValueResolver> entry : slotMappingInitializer.getResolvers(visualizationModel.getContentResourceSet(), states).entrySet()) {
            Slot slot = entry.getKey();
            VisualItemValueResolver resolver = entry.getValue();
            assert resolver != null;
            assert resolver instanceof ManagedVisualItemValueResolver : "resolver " + resolver + " is not managed";
            if (getManagedSlotMapping(slot).isAllowableResolver(resolver, visualItems)) {
                updateStatesWithNewResolvers(slot, (ManagedVisualItemValueResolver) resolver, states, visualItems);
                visualizationModel.setResolver(slot, resolver);
            } else {
            }
        }
        return states;
    }

    @Override
    public void setCurrentResolver(Slot slot, ManagedVisualItemValueResolver resolver) {
        slotsToSlotMappings.get(slot).setResolver(resolver);
    }

    public boolean slotHasInvalidResolver(Slot slot) {
        assert slotsToSlotMappings.containsKey(slot);
        return slotsToSlotMappings.get(slot).inValidState(getVisualItems());
    }

    /**
     * Call this method whenever the model changes (whenever the
     * {@link VisualItem}s change).
     */
    private void updateManagedMappings(LightweightCollection<VisualItem> visualItems) {
        for (ManagedSlotMapping managedMapping : slotsToSlotMappings.values()) {
            managedMapping.updateAllowableFactories(visualItems);
        }
    }

    /**
     * This method has the side effect that it alters the map to include the new
     * state of the mapping
     */
    private void updateStatesWithNewResolvers(Slot slot, ManagedVisualItemValueResolver resolver, Map<Slot, ManagedSlotMappingState> states, LightweightCollection<VisualItem> visualItems) {
        ManagedSlotMappingState newState = new ManagedSlotMappingState(slot, resolver, true, this.getManagedSlotMapping(slot).isAllowableResolver(resolver, visualItems), this.getManagedSlotMapping(slot).getAllowableResolverFactories());
        if (states.containsKey(slot)) {
            states.remove(slot);
        }
        states.put(slot, newState);
    }

    private Map<Slot, ManagedSlotMappingState> updateVisualMappings() {
        return updateVisualMappings(getVisualItems());
    }

    private Map<Slot, ManagedSlotMappingState> updateVisualMappings(LightweightCollection<VisualItem> visualItems) {
        updateManagedMappings(visualItems);
        LightweightCollection<Slot> slots = visualizationModel.getSlotsWithErrors();
        Map<Slot, ManagedSlotMappingState> newStates = new HashMap<Slot, ManagedSlotMappingState>();
        if (!slots.isEmpty()) {
            newStates.putAll(resetMappingsFromInitializer(slots, visualItems));
        }
        return newStates;
    }
}
