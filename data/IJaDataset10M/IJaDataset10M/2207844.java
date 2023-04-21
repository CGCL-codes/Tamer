package com.pentagaia.eclipse.sgs.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Panel to select new property pairs
 * 
 * @author mepeisen
 */
public class PropertyCreatePairPanel extends Composite {

    /** label for strKey */
    private Label labelKey = null;

    /** ey name */
    private Text strKey = null;

    /** label for strValue */
    private Label labelValue = null;

    /** Value name */
    private Text strValue = null;

    /**
     * Constructor
     * 
     * @param parent
     * @param style
     */
    public PropertyCreatePairPanel(final Composite parent, final int style) {
        super(parent, style);
        this.initialize();
    }

    /**
     * Initialize and create the ui
     */
    private void initialize() {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        this.labelKey = new Label(this, SWT.NONE);
        this.labelKey.setText(MessagesOld.getString("PropertyCreatePairPanel.LabelKey"));
        final GridData gridData = new GridData();
        gridData.widthHint = 200;
        this.strKey = new Text(this, SWT.BORDER);
        this.strKey.setLayoutData(gridData);
        this.labelValue = new Label(this, SWT.NONE);
        this.labelValue.setText(MessagesOld.getString("PropertyCreatePairPanel.Labelalue"));
        this.strValue = new Text(this, SWT.BORDER);
        this.strValue.setLayoutData(gridData);
        this.setLayout(gridLayout);
    }

    /**
     * returns the key
     * @return key name
     */
    public String getKey() {
        return this.strKey.getText();
    }

    /**
     * returns value
     * @return value name
     */
    public String getValue() {
        return this.strValue.getText();
    }
}
