package com.bluebrim.gui.client;

import javax.swing.*;
import com.bluebrim.base.shared.*;

/**
 * @author G�ran St�ck
 */
public class CoNumberUserInterfaceBuilder extends CoUserInterfaceBuilder {

    /**
     * @param userInterface
     */
    public CoNumberUserInterfaceBuilder(CoUserInterface userInterface) {
        super(userInterface);
    }

    /**
     */
    public CoTextFieldAdaptor createNumberFieldAdaptor(CoValueModel aValueModel, JTextField aTextField) {
        return createNumberFieldAdaptor(aValueModel, aTextField, CoNumberConverter.INTEGER);
    }

    /**
     */
    public CoTextFieldAdaptor createNumberFieldAdaptor(CoValueModel aValueModel, JTextField aTextField, int formatPatternIndex) {
        aTextField.setHorizontalAlignment(JTextField.RIGHT);
        return doCreateTextFieldAdaptor(CoNumberConverter.newNumberConverter(aValueModel, formatPatternIndex), aTextField);
    }

    /**
     */
    public CoTextFieldAdaptor createNumberFieldAdaptor(CoValueModel aValueModel, JTextField aTextField, int formatPatternIndex, CoConvertibleUnitSet us) {
        aTextField.setHorizontalAlignment(JTextField.RIGHT);
        return doCreateTextFieldAdaptor(CoNumberConverter.newNumberConverter(aValueModel, us, formatPatternIndex), aTextField);
    }
}
