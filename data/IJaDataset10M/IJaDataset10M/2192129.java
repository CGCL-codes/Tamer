package com.saugstation.sbev;

import java.util.EventObject;

/**
 * @author ms
 * 
 * This class provides a group of ValidatorContainer. It is valid, if all
 * added ValidatorContainers are valid. It recives InputChangedEvents, if one 
 * ValidatorContainer recieves an InputChangedEvent
 *
 */
public abstract class ValidatorGroup {

    /**
   * adds a ValidatorContainer
   * 
   * @param AbstractValidatorContainer
   */
    public abstract void addValidatorContainer(ValidatorContainer validatorContainer);

    /**
   * validates all ValidatorContainer
   * 
   * @param allValidatorContainers if set true, on validation all ValidatorContainer will be validated, if set false validation stops after the first invalid ValidatorContainer
   * @param allValidators if set true, on validation all validators will be run, if set false validation stops after the first invalid validator
   * @return true, if all added ValidatorContainer are valid 
   * @throws ValidatorException in exceptional case during validation
   */
    public abstract boolean isValid(boolean allValidatorContainers, boolean allValidators) throws ValidatorException;

    /**
   * validates all ValidatorContainer and returns
   * validationFailsMessages if validation fails and validationFailsMessage are present
   * 
   * @param showAllContainers if true all containers will be validated
   * @param showAllValidators if true all validators of a container will be invoked  
   * @return validationFailsMessages if present and validation fails
   * @throws ValidatorException in exceptional case during validation
   */
    public abstract String[] getValidationFailsMessages(boolean showAllContainers, boolean showAllValidators) throws ValidatorException;

    /**
   * Adds the specified input change listener to receive input changed events from the
   * ValidatorContainers
   * @param l the input change listener to be added
   */
    public abstract void addInputChangeListener(InputChangeListener l);

    /**
   * Adds the specified Activateable object, which will be enabled if validation
   * succeed and will be disabled if validation fails
   * 
   * @param ed the Validatable object to be added
   */
    public abstract void addActivatable(Activatable ed);

    /**
   * Adds the specified MassegDisplay object, which will recieve
   * validationFailsMessages if validation fails
   * 
   * @param ed the MessageDisplay object to be added
   * @param showAllContainers if true all containers will be validated
   * @param showAllValidators if true all validators of a container will be invoked  
   */
    public abstract void addMessageDisplay(MessageDisplay messageDisplay, boolean showAllContainers, boolean showAllValidators);

    /**
   *
   * @return true if input of validator group changed, false else
   */
    public abstract boolean isInputChanged();

    /** 
   * Recieves input changed event  
   * set Activatable to valid/not valid
   */
    public abstract void inputChanged(EventObject eo);
}
