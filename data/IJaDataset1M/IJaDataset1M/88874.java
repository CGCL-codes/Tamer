package org.netbeans.cubeon.ui.repository;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import org.netbeans.cubeon.tasks.core.spi.RepositorysViewRefreshable;
import org.netbeans.cubeon.tasks.spi.repository.TaskRepository;
import org.netbeans.cubeon.tasks.spi.repository.TaskRepositoryType;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

public final class NewRepositoryWizardAction extends AbstractAction {

    private WizardDescriptor.Panel<WizardObject>[] panels;

    public NewRepositoryWizardAction() {
        putValue(NAME, NbBundle.getMessage(NewRepositoryWizardAction.class, "LBL_Repo_New"));
    }

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[] { new ChooseRepositoryWizard(), new RepositorySettingsWizard() };
            String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                steps[i] = c.getName();
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.FALSE);
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.FALSE);
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.FALSE);
                }
            }
        }
        return panels;
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    public void actionPerformed(ActionEvent e) {
        final WizardObject wizardObject = new WizardObject();
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels(), wizardObject);
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        if (wizardDescriptor.getValue() == WizardDescriptor.FINISH_OPTION) {
            TaskRepositoryType type = wizardObject.getType();
            assert type != null;
            TaskRepository repository = wizardObject.getRepository();
            assert repository != null;
            type.persistRepository(repository);
            Collection<? extends RepositorysViewRefreshable> refreshables = Lookup.getDefault().lookupAll(RepositorysViewRefreshable.class);
            for (RepositorysViewRefreshable rvr : refreshables) {
                rvr.refreshContent();
            }
        }
    }

    static class WizardObject {

        private TaskRepositoryType type;

        private TaskRepository repository;

        public WizardObject() {
        }

        public WizardObject(TaskRepositoryType type, TaskRepository repository) {
            this.type = type;
            this.repository = repository;
        }

        public TaskRepository getRepository() {
            return repository;
        }

        public void setRepository(TaskRepository repository) {
            this.repository = repository;
        }

        public TaskRepositoryType getType() {
            return type;
        }

        public void setType(TaskRepositoryType type) {
            this.type = type;
        }
    }
}
