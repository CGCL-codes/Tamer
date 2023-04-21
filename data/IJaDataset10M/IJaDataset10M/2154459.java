package geovista.common.event;

import java.util.EventListener;

/**
 * This interface enables listening to senders of VariableSelectionEvents.
 * 
 * This interface also enables "fireVariableSelectionChanged" methods in classes
 * that generate and broadcast VariableSelectionEvents.
 * 
 */
public interface VariableSelectionListener extends EventListener {

    public void variableSelectionChanged(VariableSelectionEvent e);
}
