package net.sf.echobinding.datacontrol;

/**
 * 
 * @deprecated
 */
public interface Form {

    /**
	 * inititialzes all form widget with the model's values
	 *
	 */
    void loadForm();

    /**
	 * pulls the input from the widgets into the model
	 *
	 */
    void saveForm();

    /**
	 * validates the input of all form widgets
	 * 
	 */
    void validateForm();

    boolean isValid();
}
