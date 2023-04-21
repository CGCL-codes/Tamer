package console;

import javax.swing.*;
import org.gjt.sp.jedit.gui.HistoryTextField;
import org.gjt.sp.jedit.jEdit;
import projectviewer.vpt.VPTProject;
import projectviewer.gui.OptionPaneBase;

/**
 * @author Damien Radtke
 * Projectviewer Console OptionPane
 * An option pane for configuring project commands
 */
public class ProjectCommandOptionPane extends OptionPaneBase {

    private VPTProject proj;

    private HistoryTextField compile;

    private HistoryTextField run;

    public ProjectCommandOptionPane(VPTProject proj) {
        super("pv.commands", "console");
        this.proj = proj;
    }

    protected void _init() {
        compile = new HistoryTextField("console.compile.project");
        run = new HistoryTextField("console.run.project");
        String _compile = proj.getProperty("console.compile");
        if (_compile != null) compile.setText(_compile);
        String _run = proj.getProperty("console.run");
        if (_run != null) run.setText(_run);
        addComponent(new JLabel(jEdit.getProperty("options.pv.commands.help")));
        addComponent(jEdit.getProperty("options.pv.commands.compile"), compile);
        addComponent(jEdit.getProperty("options.pv.commands.run"), run);
    }

    protected void _save() {
        String ccmd = compile.getText();
        if (!ccmd.equals("")) {
            proj.setProperty("console.compile", compile.getText());
            compile.getModel().addItem(ccmd);
        }
        String rcmd = run.getText();
        if (!rcmd.equals("")) {
            run.getModel().addItem(rcmd);
            proj.setProperty("console.run", rcmd);
        }
    }
}
