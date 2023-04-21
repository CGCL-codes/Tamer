package astcentric.structure.vl;

import java.util.Set;

/**
 * Receiver of a set of strings which defines the allowed values of a
 * selection value validator.
 */
public interface SelectionSetReceiver extends ValueValidatorReceiver {

    /**
   * Receives the unmodifiable set of selectable string values.
   * 
   * @param set Can be assumed to never <code>null</code>.
   */
    public void receiveSelectionSet(Set<String> set);
}
