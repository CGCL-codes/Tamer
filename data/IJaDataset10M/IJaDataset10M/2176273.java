package com.bluebrim.swing.client;

import javax.swing.ButtonModel;

/**
 * Ett event som skickas fr�n en CoButtonGroup n�r en ny knapp
 * har selekterats.F�ngas upp av alla CoSelectedButtonListener.
 * 
 */
public class CoSelectedButtonEvent extends java.util.EventObject {

    ButtonModel selectedButton;

    /**
 * This method was created by a SmartGuide.
 */
    public CoSelectedButtonEvent(Object source) {
        super(source);
    }

    /**
 * This method was created by a SmartGuide.
 */
    public CoSelectedButtonEvent(Object source, ButtonModel selectedButton) {
        this(source);
        setSelectedButton(selectedButton);
    }

    /**
 * This method was created by a SmartGuide.
 */
    public ButtonModel getSelectedButton() {
        return selectedButton;
    }

    /**
 * This method was created by a SmartGuide.
 */
    public void setSelectedButton(ButtonModel selectedButton) {
        this.selectedButton = selectedButton;
    }
}
