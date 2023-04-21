package net.sf.refactorit.ui.options;

/**
 * Describes category of options.
 * Can obtain and preserve option value by it key
 *
 * @author Vladislav Vislogubov
 */
public interface CustomOptionsTab extends OptionsTab {

    Object getValue(String key);

    void setValue(String key, Object value);

    /**
   * Checks wherther we can set this value for this key
   */
    boolean isValid(String key, Object value);
}
