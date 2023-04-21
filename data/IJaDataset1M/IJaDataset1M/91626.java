package com.memoire.vainstall.builder.gui;

import com.memoire.vainstall.builder.VAIProductModel;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 
 *
 * @see 
 *
 * @author Henrik Falk
 * @version $Id: RequiredTextField.java,v 1.1 2001/09/28 19:35:30 hfalk Exp $
 */
public class RequiredTextField extends JTextField implements DocumentListener {

    private VAIProductModel productModel;

    private String requirementName;

    private Border oldBorder;

    private Border greenBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.green);

    private Border redBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red);

    private boolean hasChanged = false;

    public RequiredTextField() {
        oldBorder = getBorder();
        setBorder(redBorder);
        getDocument().addDocumentListener(this);
    }

    /**
     * initialize the textfield
     */
    public void initialize(VAIProductModel model, String requirementName) {
        this.productModel = model;
        this.requirementName = requirementName;
    }

    public void changedUpdate(DocumentEvent e) {
    }

    public void insertUpdate(DocumentEvent e) {
        if (productModel == null) {
            return;
        }
        if (getText().length() > 0) {
            productModel.removeRequirement(requirementName);
            setBorder(greenBorder);
        } else {
            productModel.addRequirement(requirementName);
            setBorder(redBorder);
        }
        hasChanged = true;
    }

    public void removeUpdate(DocumentEvent e) {
        if (productModel == null) {
            return;
        }
        if (getText().length() > 0) {
            productModel.removeRequirement(requirementName);
            setBorder(greenBorder);
        } else {
            productModel.addRequirement(requirementName);
            setBorder(redBorder);
        }
        hasChanged = true;
    }

    public boolean hasChanged() {
        return hasChanged;
    }

    public void setChanged(boolean change) {
        hasChanged = change;
    }
}
