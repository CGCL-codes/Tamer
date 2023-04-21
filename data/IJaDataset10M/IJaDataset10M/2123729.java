package com.cosmos.acacia.data.ui;

import java.io.Serializable;

/**
 *
 * @author Miro
 */
public class MenuBar extends Widget implements Serializable {

    public static final String ELEMENT_NAME = "menuBar";

    public MenuBar() {
        super(false, true);
    }

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }
}
