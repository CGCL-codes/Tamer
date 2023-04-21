package org.jactr.core.production.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import javolution.util.FastList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.chunk.ISymbolicChunk;
import org.jactr.core.chunktype.IChunkType;
import org.jactr.core.model.IModel;
import org.jactr.core.production.VariableBindings;
import org.jactr.core.production.condition.CannotMatchException;
import org.jactr.core.production.condition.match.ChunkTypeMatchFailure;
import org.jactr.core.production.condition.match.LogicMatchFailure;
import org.jactr.core.production.condition.match.SlotMatchFailure;
import org.jactr.core.production.condition.match.UnresolvedVariablesMatchFailure;
import org.jactr.core.slot.DefaultConditionalSlot;
import org.jactr.core.slot.DefaultLogicalSlot;
import org.jactr.core.slot.DefaultVariableConditionalSlot;
import org.jactr.core.slot.IConditionalSlot;
import org.jactr.core.slot.ILogicalSlot;
import org.jactr.core.slot.IMutableVariableNameSlot;
import org.jactr.core.slot.ISlot;
import org.jactr.core.slot.ISlotContainer;
import org.jactr.core.slot.IUniqueSlotContainer;
import org.jactr.core.slot.IVariableNameSlot;

/**
 * basic slot based request
 */
public class SlotBasedRequest implements IRequest, ISlotContainer {

    /**
   * Logger definition
   */
    private static final transient Log LOGGER = LogFactory.getLog(SlotBasedRequest.class);

    protected Collection<ISlot> _slots;

    protected Collection<ISlot> _unresolved;

    private boolean _locked = false;

    @SuppressWarnings("unchecked")
    public SlotBasedRequest() {
        this(Collections.EMPTY_LIST);
    }

    public SlotBasedRequest(Collection<? extends ISlot> slots) {
        _slots = new ArrayList<ISlot>(Math.max(slots.size(), 5));
        for (ISlot slot : slots) if (slot instanceof ILogicalSlot) _slots.add(new DefaultLogicalSlot(slot)); else _slots.add(new DefaultVariableConditionalSlot(slot));
    }

    protected boolean resolveSlot(ISlot slot, VariableBindings bindings, String slotContainerName, IUniqueSlotContainer container) throws CannotMatchException {
        if (slot instanceof IConditionalSlot) return resolveConditionalSlot((IConditionalSlot) slot, bindings, slotContainerName, container); else if (slot instanceof ILogicalSlot) return resolveLogicalSlot((ILogicalSlot) slot, bindings, slotContainerName, container);
        if (LOGGER.isWarnEnabled()) LOGGER.warn(String.format("A slot other than conditional or logical was attempted to resolve? %s", slot));
        return false;
    }

    /**
   * resolve a logical slot. We do this by recursing down and only checking on
   * the return result and exceptions based on the logical condition
   * 
   * @param slotToResolve
   * @param bindings
   * @param slotContainerName
   * @param container
   * @return
   * @throws CannotMatchException
   */
    protected boolean resolveLogicalSlot(ILogicalSlot slotToResolve, VariableBindings bindings, String slotContainerName, IUniqueSlotContainer container) throws CannotMatchException {
        FastList<ISlot> slots = FastList.newInstance();
        int op = slotToResolve.getOperator();
        try {
            slotToResolve.getSlots(slots);
            boolean anyIsResolved = false;
            CannotMatchException anyException = null;
            for (ISlot slot : slots) try {
                boolean resolved = resolveSlot(slot, bindings, slotContainerName, container);
                if (resolved) anyIsResolved = true;
                if (!resolved && op == ILogicalSlot.AND) return false;
            } catch (CannotMatchException cme) {
                anyException = cme;
                if (op == ILogicalSlot.AND) throw cme;
            }
            if (op == ILogicalSlot.OR) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("%s was%s resolved", slotToResolve, anyIsResolved ? "" : "nt"));
                return anyIsResolved;
            } else if (op == ILogicalSlot.AND) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("No exceptions or unresolved slots for %s", slotToResolve));
            } else if (op == ILogicalSlot.NOT) if (anyException == null) throw new CannotMatchException(new LogicMatchFailure(container, slotToResolve));
            return true;
        } finally {
            FastList.recycle(slots);
        }
    }

    /**
   * attempt to resolve a single (non logical) slot. this will attempt to
   * resolve variable names and values, in addition to extending the binding set
   * (if there is a container to match against). Impossible matching errors may
   * result in cannot match.
   * 
   * @param slotToResolve
   * @param model
   * @param bindings
   * @param slotContainer
   * @return true if it is fully resolved, false if it cannot be resolved at
   *         present
   */
    protected boolean resolveConditionalSlot(IConditionalSlot slotToResolve, VariableBindings bindings, String slotContainerName, IUniqueSlotContainer slotContainer) throws CannotMatchException {
        if (slotToResolve instanceof IMutableVariableNameSlot && ((IMutableVariableNameSlot) slotToResolve).isVariableName()) {
            String variableName = slotToResolve.getName().toLowerCase();
            if (bindings.isBound(variableName)) {
                Object obj = bindings.get(variableName);
                ((IMutableVariableNameSlot) slotToResolve).setName(obj.toString());
                if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("slot name %s resolved to %s", variableName, obj.toString()));
            } else {
                if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("slot name %s unresolved", variableName));
                return false;
            }
        }
        if (slotContainer == null) {
            if (slotToResolve.isVariableValue()) {
                String variableName = ((String) slotToResolve.getValue()).toLowerCase();
                if (bindings.isBound(variableName)) {
                    Object obj = bindings.get(variableName);
                    slotToResolve.setValue(obj);
                    if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("Resolved %s %s = %s", slotToResolve.getName(), variableName, obj));
                } else {
                    if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("%s %s unresolved ", slotToResolve.getName(), variableName));
                    return false;
                }
            }
        } else {
            ISlot valueSlot = null;
            try {
                valueSlot = slotContainer.getSlot(slotToResolve.getName());
                if (valueSlot == null) {
                    if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("%s does not contain slot %s", slotContainerName, slotToResolve.getName()));
                    throw new CannotMatchException(new SlotMatchFailure(slotContainer, slotToResolve));
                }
            } catch (CannotMatchException cme) {
                throw cme;
            } catch (Exception e) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("%s does not contain slot %s", slotContainerName, slotToResolve.getName()));
                throw new CannotMatchException(new SlotMatchFailure(slotContainer, slotToResolve));
            }
            boolean locallyBound = false;
            String variableName = null;
            if (slotToResolve.isVariableValue()) {
                variableName = ((String) slotToResolve.getValue()).toLowerCase();
                if (bindings.isBound(variableName)) {
                    Object value = bindings.get(variableName);
                    slotToResolve.setValue(value);
                } else if (slotToResolve.getCondition() == IConditionalSlot.EQUALS) if (slotToResolve.matchesCondition(valueSlot.getValue())) {
                    locallyBound = true;
                    bindings.bind(variableName, valueSlot.getValue(), valueSlot);
                    if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("Bound %s %s", variableName, valueSlot.getValue()));
                } else throw new CannotMatchException(new SlotMatchFailure(slotContainer, slotToResolve, valueSlot));
            }
            if (!slotToResolve.isVariableValue()) if (slotToResolve.getName().equalsIgnoreCase(":isa")) {
                if (!(slotContainer instanceof ISymbolicChunk)) throw new CannotMatchException(new SlotMatchFailure(slotContainer, slotToResolve, valueSlot));
                if (!(slotToResolve.getValue() instanceof IChunkType)) throw new CannotMatchException(new SlotMatchFailure(slotContainer, slotToResolve, valueSlot));
                ISymbolicChunk chunk = (ISymbolicChunk) slotContainer;
                IChunkType ct = (IChunkType) slotToResolve.getValue();
                if (!chunk.isA(ct)) throw new CannotMatchException(new ChunkTypeMatchFailure(ct, chunk.getParentChunk()));
            } else if (!slotToResolve.matchesCondition(valueSlot.getValue())) {
                if (locallyBound) bindings.unbind(variableName);
                throw new CannotMatchException(new SlotMatchFailure(slotContainer, slotToResolve, valueSlot));
            }
        }
        return true;
    }

    /**
   * attempt to resolve all the bindings, returning the number of unresolved.
   * This is the main entry call for the {@link IRequest} class. An alternative
   * entry call is available for those that want to bind against a specific slot
   * container
   * {@link #bind(IModel, String, IUniqueSlotContainer, VariableBindings, boolean)}
   */
    public int bind(IModel model, VariableBindings bindings, boolean iterativeCall) throws CannotMatchException {
        if (_unresolved == null) {
            _unresolved = FastList.newInstance();
            _unresolved.addAll(getConditionalAndLogicalSlots());
        }
        for (Iterator<ISlot> slots = _unresolved.iterator(); slots.hasNext(); ) {
            ISlot slot = slots.next();
            if (resolveSlot(slot, bindings, null, null)) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("Resolved %s", slot));
                slots.remove();
            }
        }
        if (_unresolved.size() > 0 && !iterativeCall) {
            Collection<IConditionalSlot> unresolved = new ArrayList<IConditionalSlot>(_unresolved.size());
            for (ISlot slot : _unresolved) if (slot instanceof IConditionalSlot) unresolved.add((IConditionalSlot) slot);
            throw new CannotMatchException(new UnresolvedVariablesMatchFailure(unresolved, bindings.getVariables(), null));
        }
        return _unresolved.size();
    }

    /**
   * bind the slot values in this request against those slots contained in the
   * container. This allows us to generally bind against anything that contains
   * a slot (chunk, chunktype, or buffer for queries)
   * 
   * @param model
   * @param container
   * @param bindings
   * @param iterativeCall
   * @return
   * @throws CannotMatchException
   */
    public int bind(IModel model, String containerName, IUniqueSlotContainer container, VariableBindings bindings, boolean iterativeCall) throws CannotMatchException {
        if (_unresolved == null) {
            _unresolved = FastList.newInstance();
            _unresolved.addAll(getConditionalAndLogicalSlots());
        }
        for (Iterator<ISlot> slots = _unresolved.iterator(); slots.hasNext(); ) {
            ISlot slot = slots.next();
            if (resolveSlot(slot, bindings, containerName, container)) {
                slots.remove();
                if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("Resolved %s", slot));
            }
        }
        if (_unresolved.size() > 0 && !iterativeCall) {
            Collection<IConditionalSlot> unresolved = new ArrayList<IConditionalSlot>(_unresolved.size());
            for (ISlot slot : _unresolved) if (slot instanceof IConditionalSlot) unresolved.add((IConditionalSlot) slot);
            throw new CannotMatchException(new UnresolvedVariablesMatchFailure(unresolved, bindings.getVariables(), null));
        }
        return _unresolved.size();
    }

    /**
   * attempt to resolve the slot values using a container
   * 
   * @param model
   * @param containerName
   * @param container
   * @param bindings
   * @param slots
   * @throws CannotMatchException
   */
    public void bindSlots(IModel model, String containerName, IUniqueSlotContainer container, VariableBindings bindings, Collection<ISlot> slots) throws CannotMatchException {
        for (ISlot slot : slots) resolveSlot(slot, bindings, containerName, container);
    }

    /**
   * bind and resolve as many slots in the collection as possible.
   * 
   * @throws CannotMatchException
   *           if there is a critical binding error
   */
    public void bindSlots(IModel model, VariableBindings bindings, Collection<ISlot> slots) throws CannotMatchException {
        for (ISlot slot : slots) resolveSlot(slot, bindings, null, null);
    }

    @Override
    public SlotBasedRequest clone() {
        return new SlotBasedRequest(_slots);
    }

    protected void setLocked(boolean locked) {
        if (_locked != locked) {
            _locked = locked;
            if (_locked) _slots = Collections.unmodifiableCollection(_slots); else _slots = new ArrayList<ISlot>(_slots);
        }
    }

    public void addSlot(ISlot slot) {
        if (_locked) throw new RuntimeException("Cannot modify a locked slot container");
        if (slot instanceof IVariableNameSlot) _slots.add(new DefaultVariableConditionalSlot(slot)); else if (slot instanceof ILogicalSlot) _slots.add(new DefaultLogicalSlot(slot)); else _slots.add(new DefaultConditionalSlot(slot));
    }

    public Collection<? extends IConditionalSlot> getConditionalSlots() {
        Collection<IConditionalSlot> slots = FastList.newInstance();
        for (ISlot slot : _slots) if (slot instanceof IConditionalSlot) slots.add((IConditionalSlot) slot);
        return slots;
    }

    public Collection<? extends ISlot> getConditionalAndLogicalSlots() {
        Collection<ISlot> slots = FastList.newInstance();
        for (ISlot slot : _slots) if (slot instanceof IConditionalSlot || slot instanceof ILogicalSlot) slots.add(slot);
        return slots;
    }

    public Collection<? extends ISlot> getSlots() {
        if (!_locked) return _slots;
        return Collections.unmodifiableCollection(_slots);
    }

    public void removeSlot(ISlot slot) {
        if (_locked) throw new RuntimeException("Cannot modify a locked slot container");
        _slots.remove(slot);
    }

    public Collection<ISlot> getSlots(Collection<ISlot> container) {
        if (container == null) if (_slots != null) container = new ArrayList<ISlot>(_slots.size() + 1); else container = new ArrayList<ISlot>();
        container.addAll(_slots);
        return container;
    }
}
