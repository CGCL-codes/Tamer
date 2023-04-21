package com.w20e.socrates.rendering;

/**
 * The SelectionControl can be used as a base class for those types that
 * actually provide a vocabulary of options to choose from.
 * 
 * @author dokter
 * 
 */
public abstract class SelectionControl extends ControlImpl {

    /**
     * Create new SelectionControl
     * @param newId
     */
    public SelectionControl(String newId) {
        super(newId);
    }

    /**
     * Hold options.
     */
    private OptionList options = new OptionList();

    /**
     * Add an option to the item.
     * 
     * @param opt
     *            option to add.
     */
    public final void addOption(final Option opt) {
        this.options.put(opt.getValue(), opt);
    }

    /**
     * Get all options for this item.
     * 
     * @return options.
     */
    public final OptionList getOptions() {
        return this.options;
    }

    /**
     * Set options for this item.
     */
    public final void setOptions(OptionList newOptions) {
        this.options = newOptions;
    }

    /**
     * Implement vocabulary like behaviour for the optionlist.
     * 
     * @param value
     *            value to get label for.
     * @return String (nice) presentation for this option.
     */
    public final String getOptionLabel(String value) {
        return this.options.get(value).getLabel();
    }
}
