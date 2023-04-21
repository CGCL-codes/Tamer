package uk.ac.imperial.ma.metric.nav;

import uk.ac.imperial.ma.metric.util.Task;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import java.util.Enumeration;
import uk.ac.imperial.ma.metric.gui.NavigationPanel;

public class NavigationTreeViewSetter implements Task {

    private NavigationTreeNode ntnRoot;

    private NavigationTreeView ntv;

    private NavigationPanel navigationPanel;

    private boolean isAlive;

    private boolean cancel;

    private int value;

    private int min;

    private int max;

    private String name;

    private String status;

    private JTree jtreeSubject;

    private boolean subjectTreeSetup;

    private JTree jtreeSyllabusPrerequisites;

    private boolean syllabusPrerequisitesTreeSetup;

    private JTree jtreeSyllabusEdexcel;

    private boolean syllabusEdexcelTreeSetup;

    private JTree jtreeSyllabusOCR;

    private boolean syllabusOCRTreeSetup;

    public NavigationTreeViewSetter(NavigationTreeNode ntnRoot, NavigationPanel navigationPanel) {
        this.ntnRoot = ntnRoot;
        this.navigationPanel = navigationPanel;
        ntv = NavigationTreeView.DEFAULT;
        subjectTreeSetup = false;
        syllabusPrerequisitesTreeSetup = false;
        syllabusEdexcelTreeSetup = false;
        syllabusOCRTreeSetup = false;
    }

    public void setNavigationTreeView(NavigationTreeView ntv) {
        this.ntv = ntv;
        run();
        JTree jtree = new JTree(ntnRoot);
        navigationPanel.setNavigationTree(jtree);
        navigationPanel.revalidate();
    }

    public void run() {
        isAlive = true;
        cancel = false;
        value = 0;
        min = 0;
        max = 3;
        name = "Generating navigation tree.";
        status = "Recursing full tree.";
        Thread.yield();
        recurse(ntnRoot);
        value = 1;
        status = "Second pass.";
        Thread.yield();
        recurse2(ntnRoot);
        value = 2;
        status = "Finishing up.";
        Thread.yield();
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void cancel() {
        cancel = true;
    }

    public boolean getCanceled() {
        return cancel;
    }

    public int getValue() {
        return value;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    private void recurse(NavigationTreeNode ntn) {
        if (ntv.contains(ntn)) {
            ntn.setVisible(true);
        } else {
            ntn.setVisible(false);
        }
        Enumeration enumeration = ntn.getChildrenVector().elements();
        while (enumeration.hasMoreElements()) {
            recurse((NavigationTreeNode) enumeration.nextElement());
        }
    }

    private void recurse2(NavigationTreeNode ntn) {
        ntn.setVisibleChildrenVector();
        Enumeration enumeration = ntn.getChildrenVector().elements();
        while (enumeration.hasMoreElements()) {
            recurse2((NavigationTreeNode) enumeration.nextElement());
        }
    }
}
