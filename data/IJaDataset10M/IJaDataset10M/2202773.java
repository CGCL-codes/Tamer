package co.edu.unal.ungrid.image.dicom.scpecg;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import co.edu.unal.ungrid.image.dicom.util.JTreeWithAdditionalKeyStrokeActions;

/**
 * <p>
 * The
 * {@link co.edu.unal.ungrid.image.dicom.scpecg.SCPTreeBrowser SCPTreeBrowser}
 * class implements a Swing graphical user interface to browse the contents of
 * an {@link co.edu.unal.ungrid.image.dicom.scpecg.SCPTree SCPTree}.
 * </p>
 * 
 * 
 */
public class SCPTreeBrowser {

    /**
	 * @uml.property name="tree"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    private JTree tree;

    /**
	 * @uml.property name="treeModel"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    private SCPTree treeModel;

    /**
	 * <p>
	 * Build and display a graphical user interface view of an SCP-ECG instance.
	 * </p>
	 * 
	 * <p>
	 * Implicitly builds a tree from the SCP-ECG instance.
	 * </p>
	 * 
	 * @param scpecg
	 *            tan SCP-ECG instance
	 * @param treeBrowserScrollPane
	 *            the scrolling pane in which the tree view of the attributes
	 *            will be rendered
	 * @exception Exception
	 */
    public SCPTreeBrowser(SCPECG scpecg, JScrollPane treeBrowserScrollPane) throws Exception {
        treeModel = new SCPTree(scpecg);
        tree = new JTreeWithAdditionalKeyStrokeActions(treeModel);
        treeBrowserScrollPane.setViewportView(tree);
    }
}
