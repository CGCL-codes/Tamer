package org.apache.harmony.awt.state;

/**
 * State of Checkbox component
 */
public interface CheckboxState extends TextState {

    boolean isChecked();

    boolean isInGroup();

    boolean isPressed();
}
