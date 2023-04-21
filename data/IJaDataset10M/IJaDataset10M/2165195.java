package com.alturatec.dienstreise.mvc.pkw;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jrcaf.mvc.annotations.Data;
import org.jrcaf.mvc.annotations.NLS;

public class PKWComposite extends Composite {

    public PKWComposite(Composite aParent) {
        super(aParent, SWT.NONE);
        initialize();
    }

    @NLS("entfernung")
    private Label entfernungLabel;

    @Data("verbindung.entfernung")
    private Text entfernungText;

    private void initialize() {
        setLayout(new GridLayout(2, false));
        entfernungLabel = new Label(this, SWT.NONE);
        entfernungText = new Text(this, SWT.BORDER);
        entfernungText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1));
    }
}
