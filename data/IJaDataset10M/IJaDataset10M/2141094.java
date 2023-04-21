package com.kitten.gui;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

public abstract class KittenAbstractPanel extends JPanel {

    protected abstract void makePanel();

    protected abstract void buttonActionListeners(ActionListener al);

    protected abstract void buttonKeyListeners(KeyListener kl);
}
