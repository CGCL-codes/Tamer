package com.tscribble.bitleech.ui.dialogs.options;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.tscribble.bitleech.core.config.ConfigManager;

public class GeneralView extends Composite implements IOptionView {

    private Text text;

    private ConfigManager conf = ConfigManager.getInst();

    public GeneralView(Composite parent) {
        super(parent, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 2;
        gridLayout.marginWidth = 2;
        gridLayout.marginHeight = 2;
        gridLayout.horizontalSpacing = 2;
        setLayout(gridLayout);
        final Group downloadsGroup = new Group(this, SWT.NONE);
        downloadsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        downloadsGroup.setText("Downloads");
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 3;
        downloadsGroup.setLayout(gridLayout_1);
        final Label lbSave = new Label(downloadsGroup, SWT.NONE);
        lbSave.setLayoutData(new GridData());
        lbSave.setText("Save To:");
        text = new Text(downloadsGroup, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        text.setText(conf.getDownloadDir());
        final Button selectButton = new Button(downloadsGroup, SWT.NONE);
        selectButton.setLayoutData(new GridData());
        selectButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(getShell());
                dialog.setMessage("Select Save Directory");
                dialog.setText("Directory Browser");
                String st = dialog.open();
                text.setText((st != null) ? st : text.getText());
            }
        });
        selectButton.setText("Browse");
        final Button allwaysAskMeButton = new Button(downloadsGroup, SWT.CHECK);
        allwaysAskMeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        allwaysAskMeButton.setText("Allways ask me where to save files");
        new Label(downloadsGroup, SWT.NONE);
        new Label(downloadsGroup, SWT.NONE);
    }

    public void createControl() {
    }

    public Composite getControl() {
        return this;
    }

    public String getTitle() {
        return "General";
    }

    public void save() {
        conf.setDownloadDir(text.getText());
    }
}
