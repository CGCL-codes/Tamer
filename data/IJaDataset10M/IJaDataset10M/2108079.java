package net.sourceforge.iwii.db.dev.ui.artifacts.wizards;

import java.awt.Component;
import java.awt.Dialog;
import java.text.MessageFormat;
import javax.swing.JComponent;
import net.sourceforge.iwii.db.dev.bo.project.artifact.ProjectArtifactDataBO;
import net.sourceforge.iwii.db.dev.ui.main.ProjectExplorerTopComponent;
import net.sourceforge.iwii.db.dev.ui.model.artifacts.wizard.ArtifactAcceptWizardModel;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

public final class AcceptArtifactWizardAction extends CallableSystemAction {

    private WizardDescriptor.Panel[] panels;

    private ProjectArtifactDataBO artifactData;

    private ArtifactAcceptWizardModel model;

    public void performAction() {
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels());
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("Zatwierdzanie artefaktu");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            this.model.accept();
            ProjectExplorerTopComponent.findInstance().updateExplorerContent();
        }
        this.model = null;
        this.artifactData = null;
    }

    public void setArtifactData(ProjectArtifactDataBO artifactData) {
        this.artifactData = artifactData;
        this.model = null;
        this.panels = null;
    }

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            if (this.model == null) this.model = new ArtifactAcceptWizardModel();
            this.model.setArtifactData(artifactData);
            panels = new WizardDescriptor.Panel[] { new AcceptArtifactWizardPanel1(this.model) };
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
        return "Zatwierdź artefakt";
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
