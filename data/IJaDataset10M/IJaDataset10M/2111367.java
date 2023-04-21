package com.hexapixel.widgets.collapsiblebuttons;

import org.eclipse.swt.events.MouseEvent;

public interface IButtonListener {

    public void buttonClicked(CustomButton button, MouseEvent e);

    public void buttonEnter(CustomButton button, MouseEvent e);

    public void buttonExit(CustomButton button, MouseEvent e);

    public void buttonHover(CustomButton button, MouseEvent e);
}
