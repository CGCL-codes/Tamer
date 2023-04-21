package jmetric.ui;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import jmetric.model.JPackageMetrics;
import jmetric.model.JProjectMetrics;

public class ProjectTreeNode extends DefaultMutableTreeNode {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4680200326279473622L;

    private boolean hasLoaded;

    public ProjectTreeNode(JProjectMetrics o) {
        super(o);
    }

    public boolean isLeaf() {
        return false;
    }

    public int getChildCount() {
        if (!hasLoaded) loadChildren();
        return super.getChildCount();
    }

    private void loadChildren() {
        JProjectMetrics theProject = (JProjectMetrics) getUserObject();
        loadPackages(theProject);
        hasLoaded = true;
    }

    private void loadPackages(JProjectMetrics from) {
        JPackageMetrics tmpJPackageM, tmpJPackageCompare;
        Vector<JPackageMetrics> thePackages = from.getPackages();
        for (int i = 0; i < (thePackages.size() - 1); i++) {
            tmpJPackageM = (JPackageMetrics) thePackages.elementAt(i);
            for (int j = i + 1; j < thePackages.size(); j++) {
                tmpJPackageCompare = (JPackageMetrics) thePackages.elementAt(j);
                if (tmpJPackageM.getName().compareTo(tmpJPackageCompare.getName()) > 0) {
                    thePackages.setElementAt(tmpJPackageCompare, i);
                    thePackages.setElementAt(tmpJPackageM, j);
                    tmpJPackageM = tmpJPackageCompare;
                }
            }
        }
        Enumeration<JPackageMetrics> e = thePackages.elements();
        int count = 0;
        while (e.hasMoreElements()) {
            tmpJPackageM = (JPackageMetrics) e.nextElement();
            insert(new PackageTreeNode(tmpJPackageM), count);
            count++;
        }
    }

    public void refreshProject() {
        reload();
    }

    public void reload() {
        hasLoaded = false;
        if (super.children != null) {
            removeAllChildren();
        }
        loadChildren();
    }
}
