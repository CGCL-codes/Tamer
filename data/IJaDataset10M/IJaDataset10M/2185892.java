package org.xnap.commons.gui.completion;

import javax.swing.MutableComboBoxModel;

/**
 * Defines the requirements for classes implementing a CompletionModel.
 * <p>
 * The completion model can be queried for possible completions of a given
 * <code>prefix</code>. There are two methods a CompletionModel has to
 * implement:
 * <ul>
 * <li>complete - Find all possible completions for a given prefix and add
 * them as a side effect to the MutableComboboxModel using {@link
 * #addElement(Object)}.</li>
 * <li>completeUniquePrefix - Find the largest common prefix of all possible
 * completions and return it.</li>
 * </ul>
 * 
 * The completion model interface inherits MutableComoboxModel so that
 * possible {@link CompletionMode CompletionModes} can use it as the model for
 * a comobobox.
 * 
 * @author Felix Berger
 */
public interface CompletionModel extends MutableComboBoxModel {

    /**
	 * Finds possible completions for a prefix.
	 *
	 * As a side effect the completions are added to the {@link
	 * MutableComboBoxModel}. Use {@link #getSize()} to get the number of
	 * completions found and retrieve them with {@link #getElementAt(int)}. 
	 *
	 * @param prefix the prefix being matched
	 * @return true, if completion was successful, i.e. the prefix matches 
	 * at least one item in the model
	 */
    boolean complete(String prefix);

    /**
	 * Returns largest common prefix of all possible completions for the given
	 * prefix.
	 *
	 * @param prefix the prefix being matched
	 * @return the largest common prefix of all possible matches, the prefix
	 * given if no longer prefix can be found
	 */
    String completeUniquePrefix(String prefix);
}
