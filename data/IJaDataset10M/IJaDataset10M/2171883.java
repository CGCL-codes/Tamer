package com.peterhi.client.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import com.peterhi.client.ui.constants.Images;
import com.peterhi.client.ui.constants.Strings;

/**
 * About dialog.
 * 
 * @author YUN TAO
 * 
 */
public class AboutPanel extends Composite {

    /**
     * Show the about dialog, with the specified parent.
     * 
     * @param parent
     *            The parent {@link Shell}.
     * @return The {@link AboutPanel} instance created for further processing.
     */
    public static AboutPanel show(Shell parent) {
        Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText(Strings.about_title);
        shell.setImage(Images.peterhi16);
        shell.setLayout(new FillLayout());
        AboutPanel ret = new AboutPanel(shell, SWT.NONE);
        shell.pack();
        Util.center(shell, parent);
        shell.open();
        return ret;
    }

    private Label image;

    private Label text;

    public AboutPanel(Composite parent, int style) {
        super(parent, style);
        GridLayout lay = new GridLayout();
        lay.marginWidth = 5;
        lay.marginHeight = 5;
        lay.horizontalSpacing = 5;
        lay.verticalSpacing = 5;
        lay.numColumns = 2;
        setLayout(lay);
        image = new Label(this, SWT.WRAP);
        image.setImage(Images.peterhi200);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        data.widthHint = -1;
        data.heightHint = -1;
        image.setLayoutData(data);
        text = new Label(this, SWT.WRAP);
        text.setText(Strings.about_text);
        data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        data.widthHint = 280;
        data.heightHint = -1;
        text.setLayoutData(data);
    }
}
