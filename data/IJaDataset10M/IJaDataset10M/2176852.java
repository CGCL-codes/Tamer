package com.rapidminer.gui.properties;

import java.awt.GridBagConstraints;
import com.rapidminer.operator.Operator;
import com.rapidminer.parameter.ParameterTypeFile;

/**
 * A simple file cell editor for generic files. These can be used for all
 * parameter types which are not special.
 * 
 * @see AttributeFileCellEditor
 * @author Ingo Mierswa, Simon Fischer
 *          Exp $
 */
public class SimpleFileValueCellEditor extends FileValueCellEditor {

    private static final long serialVersionUID = 8800712397096177848L;

    public SimpleFileValueCellEditor(ParameterTypeFile type) {
        super(type);
        addButton(createFileChooserButton(), GridBagConstraints.REMAINDER);
    }

    /** Does nothing. */
    public void setOperator(Operator operator) {
    }
}
