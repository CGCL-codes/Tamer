package org.clico.swing.controller;

import javax.swing.JButton;

public interface FormActions extends Actions {

    void clearButton(JButton clear);

    void saveButton(JButton save);

    void cancelButton(JButton cancel);
}
