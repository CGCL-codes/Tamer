package p4plugin.action;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenu;
import org.gjt.sp.jedit.jEdit;
import projectviewer.ProjectViewer;
import projectviewer.action.Action;
import projectviewer.vpt.VPTNode;
import p4plugin.config.P4Config;

/**
 *  The sub-menu to be shown in the project viewer context menu.
 *
 *  @author     Marcelo Vanzin
 *  @version    $Id: P4FileMenu.java 6457 2006-06-12 04:10:39Z vanza $
 *  @since      P4P 0.1
 */
public class P4FileMenu extends Action {

    private JMenu fileMenu;

    private List actions;

    public String getText() {
        return jEdit.getProperty("p4plugin.action.file-menu");
    }

    public JComponent getMenuItem() {
        if (fileMenu == null) {
            fileMenu = new JMenu(getText());
            addAction(new P4FileAction("edit", true));
            addAction(new P4FileAction("add", true));
            addAction(new P4FileAction("delete", true));
            addAction(new P4FileAction("revert", true));
            addAction(new P4FileAction("edit", false));
            addAction(new P4FileAction("add", false));
            addAction(new P4FileAction("delete", false));
            addAction(new P4FileAction("reopen", false, false));
            addAction(new P4FileInfoAction("fstat"));
            addAction(new P4FileInfoAction("filelog"));
            addAction(new P4Submit(true));
        }
        return fileMenu;
    }

    public void prepareForNode(VPTNode node) {
        P4Config cfg = P4Config.getProjectConfig(jEdit.getActiveView());
        getMenuItem().setVisible(cfg != null && node != null && node.isFile());
    }

    public void actionPerformed(ActionEvent ae) {
    }

    public void setViewer(ProjectViewer viewer) {
        if (actions != null) for (Iterator i = actions.iterator(); i.hasNext(); ) ((Action) i.next()).setViewer(viewer);
        super.setViewer(viewer);
    }

    private void addAction(Action a) {
        a.setViewer(viewer);
        if (actions == null) actions = new LinkedList();
        actions.add(a);
        fileMenu.add(a.getMenuItem());
    }
}
