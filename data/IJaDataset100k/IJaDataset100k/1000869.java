package org.robcash.commons.swing.wizard.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.event.EventListenerList;
import org.robcash.commons.event.StateChangeEvent;
import org.robcash.commons.event.StateChangeListener;
import org.robcash.commons.swing.wizard.WizardPanel;
import org.robcash.commons.swing.wizard.event.WizardModelChangeListener;
import org.robcash.commons.swing.wizard.event.WizardModelEvent;
import org.robcash.commons.util.DefaultState;
import org.robcash.commons.util.DefaultStateMachine;
import org.robcash.commons.util.DefaultStateTransition;
import org.robcash.commons.util.State;

/**
 *
 * @author Rob
 */
public class DefaultWizardModel implements WizardModel, StateChangeListener {

    public static final String PROP_CURRENTPANEL = "currentPanel";

    private static final String PREV_PANEL_STATE_NAME = "_prevPanel";

    protected Map<String, WizardPanel> panels;

    private DefaultStateMachine<WizardPanel> stateMachine;

    private EventListenerList eventListeners;

    public DefaultWizardModel() {
        this.panels = new HashMap<String, WizardPanel>();
        this.stateMachine = new DefaultStateMachine<WizardPanel>();
        this.eventListeners = new EventListenerList();
        stateMachine.addStateChangeListener(this);
    }

    /**
     * Add a wizard panel to the model
     * @param panel Panel to add
     */
    public void add(WizardPanel panel) {
        stateMachine.addState(new DefaultState<WizardPanel>(panel.getId(), panel));
        panels.put(panel.getId(), panel);
    }

    /**
     * Add a wizard panel to the model
     * @param panel Panel to add
     * @param initial Indicates that this panel is the initial panel to display
     */
    public void add(WizardPanel panel, boolean initial) {
        stateMachine.addState(new DefaultState<WizardPanel>(panel.getId(), panel), initial);
        panels.put(panel.getId(), panel);
    }

    /**
     * Add a transition between two panels
     * @param startPanel Panel on which the transition starts
     * @param endPanel Panel on which the transition ends
     * @param transitionName Name of transition
     */
    public void add(WizardPanel startPanel, WizardPanel endPanel, String transitionName) {
        State<WizardPanel> startState = new DefaultState<WizardPanel>(startPanel.getId(), startPanel);
        State<WizardPanel> endState = new DefaultState<WizardPanel>(endPanel.getId(), endPanel);
        stateMachine.addTransition(new DefaultStateTransition<WizardPanel>(transitionName, startState, endState));
        stateMachine.addTransition(new DefaultStateTransition<WizardPanel>(PREV_PANEL_STATE_NAME, endState, startState));
    }

    /**
     * Get the current panel
     * @return Current wizrd panel
     */
    public WizardPanel getCurrentPanel() {
        return stateMachine.getCurrentState().getValue();
    }

    /**
     * Indicates if there was a panel displayed before the current one
     * @return <tt>true</tt> if there was a panel displayed before the current
     * one
     */
    public boolean hasPreviousPanel() {
        return stateMachine.canTransitionTo(PREV_PANEL_STATE_NAME);
    }

    /**
     * Go forward to the next panel, as identified by panelId
     * @param panelId Unique identifier of next wizard panel
     * @throws IllegalArgumentException Thrown if the specified panelId does not
     * correspond to a known panel or is not valid as the next panel
     */
    public void goToNextPanel(String panelId) throws IllegalArgumentException {
        try {
            stateMachine.transitionToNewState(panelId);
        } catch (NullPointerException npe) {
            throw new IllegalArgumentException(npe);
        } catch (IllegalStateException ise) {
            throw new IllegalArgumentException(ise);
        }
    }

    /**
     * Go back to the previous panel
     */
    public void goToPreviousPanel() {
        try {
            stateMachine.transitionToNewState(PREV_PANEL_STATE_NAME);
        } catch (NullPointerException npe) {
            throw new IllegalArgumentException(npe);
        } catch (IllegalStateException ise) {
            throw new IllegalArgumentException(ise);
        }
    }

    /**
     * Get a wizard panel by its unique identifier
     * @param id Wizard panel identifier
     * @return Wizard panel
     */
    public WizardPanel getPanel(String id) {
        return panels.get(id);
    }

    /**
     * Remove a wizard panel
     * @param panel Panel to remove
     * @return <tt>true</tt> if the panel was removed
     */
    public boolean remove(WizardPanel panel) {
        if (panels.containsKey(panel.getId()) && panels.get(panel.getId()).equals(panel)) {
            panels.remove(panel.getId());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Reset the model to its original state
     */
    public void reset() {
        stateMachine.reset();
    }

    /**
     * Returns an iterator over the states in this state machine
     * @return an <tt>Iterator</tt> over the states in this state machine
     */
    public Iterator<WizardPanel> iterator() {
        return new WizardPanelIterator();
    }

    /**
     * Receive notification that a state change is about to occur
     * @param evt State change event
     */
    public void stateChanging(StateChangeEvent evt) {
        fireWizardModelChangeListenerModelIsChanging(new WizardModelEvent(this));
    }

    /**
     * Receive notification that a state change has taken place
     * @param evt State change event
     */
    public void stateChanged(StateChangeEvent evt) {
        fireWizardModelChangeListenerModelChanged(new WizardModelEvent(this));
    }

    /**
     * Add state change listener
     * @param listener State change listener
     */
    public void addWizardModelChangeListener(WizardModelChangeListener listener) {
        eventListeners.add(WizardModelChangeListener.class, listener);
    }

    /**
     * Add state change listener
     * @param listener State change listener
     */
    public void removeWizardModelChangeListener(WizardModelChangeListener listener) {
        eventListeners.remove(WizardModelChangeListener.class, listener);
    }

    /**
     * Fire an event to state change listeners
     * @param event Event to fire
     */
    protected void fireWizardModelChangeListenerModelIsChanging(WizardModelEvent event) {
        WizardModelChangeListener[] listeners = eventListeners.getListeners(WizardModelChangeListener.class);
        for (int index = 0; index < listeners.length; index++) {
            listeners[index].modelIsChanging(event);
        }
    }

    /**
     * Fire an event to state change listeners
     * @param event Event to fire
     */
    protected void fireWizardModelChangeListenerModelChanged(WizardModelEvent event) {
        WizardModelChangeListener[] listeners = eventListeners.getListeners(WizardModelChangeListener.class);
        for (int index = 0; index < listeners.length; index++) {
            listeners[index].modelChanged(event);
        }
    }

    /**
     * Fire an event to state change listeners
     * @param event Event to fire
     */
    protected void fireWizardModelChangeListenerWizardPanelAdded(WizardModelEvent event) {
        WizardModelChangeListener[] listeners = eventListeners.getListeners(WizardModelChangeListener.class);
        for (int index = 0; index < listeners.length; index++) {
            listeners[index].wizardPanelAdded(event);
        }
    }

    /**
     * Fire an event to state change listeners
     * @param event Event to fire
     */
    protected void fireWizardModelChangeListenerWizardPanelRemoved(WizardModelEvent event) {
        WizardModelChangeListener[] listeners = eventListeners.getListeners(WizardModelChangeListener.class);
        for (int index = 0; index < listeners.length; index++) {
            listeners[index].wizardPanelRemoved(event);
        }
    }

    /**
     * Iterator class
     */
    private class WizardPanelIterator implements Iterator<WizardPanel> {

        private Iterator<State<WizardPanel>> backingIterator;

        public WizardPanelIterator() {
            backingIterator = stateMachine.iterator();
        }

        public boolean hasNext() {
            return backingIterator.hasNext();
        }

        public WizardPanel next() {
            State<WizardPanel> nextState = backingIterator.next();
            return nextState.getValue();
        }

        public void remove() {
            throw new UnsupportedOperationException("Removal of wizard panels not permitted via iterator");
        }
    }
}
