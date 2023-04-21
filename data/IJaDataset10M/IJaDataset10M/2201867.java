package org.gudy.azureus2.ui.swt.config.wizard;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.gudy.azureus2.core3.internat.MessageText;
import org.gudy.azureus2.ui.swt.Messages;
import org.gudy.azureus2.ui.swt.wizard.AbstractWizardPanel;
import org.gudy.azureus2.ui.swt.wizard.IWizardPanel;

/**
 * @author Olivier
 * 
 */
public class FilePanel extends AbstractWizardPanel {

    public FilePanel(ConfigureWizard wizard, IWizardPanel previous) {
        super(wizard, previous);
    }

    public void show() {
        wizard.setTitle(MessageText.getString("configureWizard.file.title"));
        Composite rootPanel = wizard.getPanel();
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        rootPanel.setLayout(layout);
        Composite panel = new Composite(rootPanel, SWT.NULL);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        panel.setLayoutData(gridData);
        layout = new GridLayout();
        layout.numColumns = 3;
        panel.setLayout(layout);
        {
            Label label = new Label(panel, SWT.WRAP);
            gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.horizontalSpan = 3;
            label.setLayoutData(gridData);
            Messages.setLanguageText(label, "configureWizard.file.message3");
            label = new Label(panel, SWT.NULL);
            label.setLayoutData(new GridData());
            Messages.setLanguageText(label, "configureWizard.file.path");
            final Text textPath = new Text(panel, SWT.BORDER);
            gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.widthHint = 100;
            textPath.setLayoutData(gridData);
            textPath.setText(((ConfigureWizard) wizard).getDataPath());
            Button browse = new Button(panel, SWT.PUSH);
            Messages.setLanguageText(browse, "configureWizard.file.browse");
            browse.setLayoutData(new GridData());
            browse.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event arg0) {
                    DirectoryDialog dd = new DirectoryDialog(wizard.getWizardWindow());
                    dd.setFilterPath(textPath.getText());
                    String path = dd.open();
                    if (path != null) {
                        textPath.setText(path);
                    }
                }
            });
            textPath.addListener(SWT.Modify, new Listener() {

                public void handleEvent(Event event) {
                    String path = textPath.getText();
                    ((ConfigureWizard) wizard).setDataPath(path);
                    try {
                        File f = new File(path);
                        if (f.exists() && f.isDirectory()) {
                            wizard.setErrorMessage("");
                            wizard.setFinishEnabled(true);
                        } else {
                            wizard.setErrorMessage(MessageText.getString("configureWizard.file.invalidPath"));
                            wizard.setFinishEnabled(false);
                        }
                    } catch (Exception e) {
                        wizard.setErrorMessage(MessageText.getString("configureWizard.file.invalidPath"));
                        wizard.setFinishEnabled(false);
                    }
                }
            });
        }
        {
            Label label = new Label(panel, SWT.WRAP);
            gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.horizontalSpan = 3;
            label.setLayoutData(gridData);
            Messages.setLanguageText(label, "configureWizard.file.message1");
            label = new Label(panel, SWT.NULL);
            label.setLayoutData(new GridData());
            Messages.setLanguageText(label, "configureWizard.file.path");
            final Text textPath = new Text(panel, SWT.BORDER);
            gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.widthHint = 100;
            textPath.setLayoutData(gridData);
            textPath.setText(((ConfigureWizard) wizard).torrentPath);
            Button browse = new Button(panel, SWT.PUSH);
            Messages.setLanguageText(browse, "configureWizard.file.browse");
            browse.setLayoutData(new GridData());
            browse.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event arg0) {
                    DirectoryDialog dd = new DirectoryDialog(wizard.getWizardWindow());
                    dd.setFilterPath(textPath.getText());
                    String path = dd.open();
                    if (path != null) {
                        textPath.setText(path);
                    }
                }
            });
            textPath.addListener(SWT.Modify, new Listener() {

                public void handleEvent(Event event) {
                    String path = textPath.getText();
                    ((ConfigureWizard) wizard).torrentPath = path;
                    try {
                        File f = new File(path);
                        if (f.exists() && f.isDirectory()) {
                            wizard.setErrorMessage("");
                            wizard.setFinishEnabled(true);
                        } else {
                            wizard.setErrorMessage(MessageText.getString("configureWizard.file.invalidPath"));
                            wizard.setFinishEnabled(false);
                        }
                    } catch (Exception e) {
                        wizard.setErrorMessage(MessageText.getString("configureWizard.file.invalidPath"));
                        wizard.setFinishEnabled(false);
                    }
                }
            });
            textPath.setText(((ConfigureWizard) wizard).torrentPath);
        }
    }

    public IWizardPanel getFinishPanel() {
        return new FinishPanel(((ConfigureWizard) wizard), this);
    }
}
