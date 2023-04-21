package org.semtinel.core.importer.fiz;

import java.awt.Component;
import java.awt.Dialog;
import java.text.MessageFormat;
import java.util.Map;
import javax.swing.JComponent;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;
import org.semtinel.core.util.ProgressListener;

public final class FIZImportWizardAction extends CallableSystemAction {

    private WizardDescriptor.Panel[] panels;

    public static final String FILENAME = "filename";

    public void performAction() {
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels());
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("Import FIZ");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            final ProgressHandle p = ProgressHandleFactory.createHandle("Importing FIZ");
            Map<String, Object> props = wizardDescriptor.getProperties();
            final String fizFile = (String) props.get(FILENAME);
            Runnable run = new Runnable() {

                public void run() {
                    FIZImporter fi = new FIZImporter(fizFile);
                    fi.addProgressListener(new ProgressListener() {

                        public void progress(String message, int progress) {
                            p.progress(message);
                        }

                        public void finish() {
                            p.finish();
                        }

                        public void start(int max) {
                            p.start();
                        }
                    });
                    fi.process();
                }
            };
            Thread thread = new Thread(run);
            thread.start();
        }
    }

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[] { new FIZImportWizardPanel1() };
            String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                steps[i] = c.getName();
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }

    public String getName() {
        return "Import FIZ";
    }

    @Override
    public String iconResource() {
        return null;
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
