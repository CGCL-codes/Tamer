package com.cep.jqemu.ui.model;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author cbourque
 */
public class DiskImageModel extends DefaultComboBoxModel {

    public static final String[] FORMAT_VALUES = new String[] { "", "raw", "qcow", "cow", "vmdk", "cloop" };

    public DiskImageModel() {
        super(FORMAT_VALUES);
    }
}
