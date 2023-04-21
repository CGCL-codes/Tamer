package net.sf.gridarta.model.archetypechooser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The view of the archetype chooser.
 * @author Andreas Kirschbaum
 */
public class ArchetypeChooserModel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ArchetypeChooserPanel ArchetypeChooserPanels}. The panels are
     * ordered by panel name.
     */
    @NotNull
    private final List<ArchetypeChooserPanel<G, A, R>> panels = new ArrayList<ArchetypeChooserPanel<G, A, R>>();

    /**
     * The selected {@link ArchetypeChooserPanel}. It must be part of {@link
     * #panels}. Set to <code>null</code> only if no panels do exist.
     */
    @Nullable
    private ArchetypeChooserPanel<G, A, R> selectedPanel = null;

    /**
     * The default direction for game objects created from archetypes. Set to
     * <code>null</code> for default direction.
     */
    @Nullable
    private Integer direction = null;

    /**
     * The registered listeners.
     */
    @NotNull
    private final EventListenerList2<ArchetypeChooserModelListener<G, A, R>> listeners = new EventListenerList2<ArchetypeChooserModelListener<G, A, R>>(ArchetypeChooserModelListener.class);

    /**
     * The {@link ArchetypeChooserPanelListener} attached to {@link
     * #selectedPanel}.
     */
    @NotNull
    private final transient ArchetypeChooserPanelListener<G, A, R> archetypeChooserPanelListener = new ArchetypeChooserPanelListener<G, A, R>() {

        @Override
        public void selectedFolderChanged(@NotNull final ArchetypeChooserFolder<G, A, R> selectedFolder) {
            fireSelectedFolderChanged(selectedFolder);
        }

        @Override
        public void selectedArchetypeChanged(@Nullable final R selectedArchetype) {
            fireSelectedArchetypeChanged(selectedArchetype);
        }
    };

    /**
     * Adds a listener to be notified of changes.
     * @param listener the listener
     */
    public void addArchetypeChooserModelListener(@NotNull final ArchetypeChooserModelListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener to be notified of changes.
     * @param listener the listener
     */
    public void removeArchetypeChooserModelListener(@NotNull final ArchetypeChooserModelListener<G, A, R> listener) {
        listeners.remove(listener);
    }

    /**
     * Returns the {@link ArchetypeChooserPanel ArchetypeChooserPanels}.
     * @return the panels ordered by panel name
     */
    @NotNull
    public Iterable<ArchetypeChooserPanel<G, A, R>> getPanels() {
        return Collections.unmodifiableList(panels);
    }

    /**
     * Adds an {@link Archetype} to this model.
     * @param panel the panel name to add to
     * @param folder the folder name to add to
     * @param archetype the archetype to add
     */
    public void addArchetype(@NotNull final String panel, @NotNull final String folder, @NotNull final R archetype) {
        getPanel(panel).addArchetype(folder, archetype);
    }

    /**
     * Returns an {@link ArchetypeChooserPanel} by panel name. The panel is
     * created if it does not yet exist.
     * @param panelName the panel name
     * @return the folder
     */
    @NotNull
    public ArchetypeChooserPanel<G, A, R> getPanel(@NotNull final String panelName) {
        for (final ArchetypeChooserPanel<G, A, R> panel : panels) {
            if (panel.getName().equals(panelName)) {
                return panel;
            }
        }
        final ArchetypeChooserPanel<G, A, R> panel = new ArchetypeChooserPanel<G, A, R>(panelName);
        panels.add(panel);
        if (selectedPanel == null) {
            setSelectedPanel(panel);
        }
        return panel;
    }

    /**
     * Returns the selected {@link ArchetypeChooserPanel}.
     * @return the selected panel
     */
    @Nullable
    public ArchetypeChooserPanel<G, A, R> getSelectedPanel() {
        return selectedPanel;
    }

    /**
     * Sets the selected {@link ArchetypeChooserPanel}.
     * @param selectedPanel the selected panel
     */
    @SuppressWarnings("NullableProblems")
    public void setSelectedPanel(@NotNull final ArchetypeChooserPanel<G, A, R> selectedPanel) {
        if (!panels.contains(selectedPanel)) {
            throw new IllegalArgumentException("selected panel " + selectedPanel.getName() + " is not part of the model");
        }
        if (this.selectedPanel == selectedPanel) {
            return;
        }
        if (this.selectedPanel != null) {
            this.selectedPanel.removeArchetypeChooserPanelListener(archetypeChooserPanelListener);
        }
        this.selectedPanel = selectedPanel;
        this.selectedPanel.addArchetypeChooserPanelListener(archetypeChooserPanelListener);
        for (final ArchetypeChooserModelListener<G, A, R> listener : listeners.getListeners()) {
            listener.selectedPanelChanged(selectedPanel);
        }
        final ArchetypeChooserFolder<G, A, R> selectedFolder = selectedPanel.getSelectedFolder();
        fireSelectedFolderChanged(selectedFolder);
        fireSelectedArchetypeChanged(selectedFolder.getSelectedArchetype());
    }

    /**
     * Returns the default direction for game objects created from archetypes.
     * @return the direction or <code>null</code> for default
     */
    @Nullable
    public Integer getDirection() {
        return direction;
    }

    /**
     * Sets the default direction for game objects created from archetypes.
     * @param direction the direction or <code>null</code> for default
     */
    public void setDirection(@Nullable final Integer direction) {
        if (this.direction == null) {
            if (direction == null) {
                return;
            }
        } else {
            if (this.direction.equals(direction)) {
                return;
            }
        }
        this.direction = direction;
        for (final ArchetypeChooserModelListener<G, A, R> listener : listeners.getListeners()) {
            listener.directionChanged(direction);
        }
    }

    /**
     * Notifies all registered {@link ArchetypeChooserModelListener
     * ArchetypeChooserModelListeners} that the selected folder has changed.
     * @param selectedFolder the new selected folder
     */
    private void fireSelectedFolderChanged(@NotNull final ArchetypeChooserFolder<G, A, R> selectedFolder) {
        for (final ArchetypeChooserModelListener<G, A, R> listener : listeners.getListeners()) {
            listener.selectedFolderChanged(selectedFolder);
        }
    }

    /**
     * Notifies all registered {@link ArchetypeChooserPanelListener
     * ArchetypeChooserPanelListeners} that the selected archetype has changed.
     * @param selectedArchetype the new selected archetype or <code>null</code>
     * if none is selected
     */
    private void fireSelectedArchetypeChanged(@Nullable final R selectedArchetype) {
        for (final ArchetypeChooserModelListener<G, A, R> listener : listeners.getListeners()) {
            listener.selectedArchetypeChanged(selectedArchetype);
        }
    }
}
