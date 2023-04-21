package com.hifiremote.jp1;

/**
 * The Class ButtonAssignment.
 */
public class ButtonAssignment {

    /**
   * Instantiates a new button assignment.
   * 
   * @param button the button
   * @param function the function
   * @param shiftedFunction the shifted function
   */
    public ButtonAssignment(Button button, Function function, Function shiftedFunction) {
        this.button = button;
        this.function = function;
        this.shiftedFunction = function;
    }

    /**
   * Instantiates a new button assignment.
   * 
   * @param button the button
   */
    public ButtonAssignment(Button button) {
        this.button = button;
    }

    /**
   * Gets the button.
   * 
   * @return the button
   */
    public Button getButton() {
        return button;
    }

    /**
   * Sets the function.
   * 
   * @param function the function
   * 
   * @return the button assignment
   */
    public ButtonAssignment setFunction(Function function) {
        this.function = function;
        return this;
    }

    /**
   * Gets the function.
   * 
   * @return the function
   */
    public Function getFunction() {
        return function;
    }

    /**
   * Sets the shifted function.
   * 
   * @param function the function
   * 
   * @return the button assignment
   */
    public ButtonAssignment setShiftedFunction(Function function) {
        this.shiftedFunction = function;
        return this;
    }

    /**
   * Gets the shifted function.
   * 
   * @return the shifted function
   */
    public Function getShiftedFunction() {
        return shiftedFunction;
    }

    /** The button. */
    private Button button = null;

    /** The function. */
    private Function function = null;

    /** The shifted function. */
    private Function shiftedFunction = null;
}
