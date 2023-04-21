package com.framedobjects.dashwell.utils.webservice;

/**
 * Defines a standard set of methods that a Visual Component has to implement.
 * Visual Components represent a Java type in a grapical user interface.
 * They can hold and modify the string representation of a Java type value.
 * For example the {@link WSint VCint} class represents a integer as a text label
 * with a text box.
 * 
 */
public interface IParamView {

    /**
   * Returns the text label of the component(parent: fieldName).
   * 
   * @return the text label of the component.
   */
    public String getLabel();

    /**
   * Returns the text label of the component(fieldName).
   * 
   * @return the text label of the component.
   */
    public String getSimpleLabel();

    /**
   * Sets the text label of the component. The text label is
   * to give more information about the value that has to be
   * represented. For example a Visual Component can represent
   * the temperature of your living room. So you can set a label
   * with the value <i>living room temperature</i>.
   * 
   * @param label the label text.
	 */
    public void setLabel(String label);

    /**
   * Returns all attached validators of this component.
   * 
   * @return all attached validators.
   */
    public IValidator[] getValidators();

    /**
   * Adds a validator to the components. A component can hold more that
   * one validator.
   * 
   * @param validator validator to attach.
   */
    public void addValidator(IValidator validator);

    /**
   * Removes the validator.
   * 
   * @param validator the validator that has to be removed.
   * @return removed validator instance.
   */
    public IValidator removeValidator(IValidator validator);

    /**
   * Returns all validators that failed after calling the {@link IParamView#validate() validate}
   * method.
   * 
   * @return array of failed validators.
   */
    public IValidator[] getFailedValidators();

    /**
   * Validates the component. Every component can hold zero or
   * more validators. A validator parses a string for some rules
   * and returns true or false. If one validator or more validator
   * failed, validation failed.
   * 
   * @return true if no validator failed.
   */
    public boolean validate();
}
