package org.tigr.microarray.mev.cgh.CGHAlgorithms.NumberOfAlterations;

import java.util.Collection;
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;
import org.tigr.microarray.mev.cgh.CGHDataObj.AlterationRegion;
import org.tigr.microarray.mev.cgh.CGHGuiObj.AlgorithmResultsViewers.NumberOfAlterationsViewers.AlterationParametersViewer;
import org.tigr.microarray.mev.cgh.CGHGuiObj.AlgorithmResultsViewers.NumberOfAlterationsViewers.NumberOfAlterationsDataModel;
import org.tigr.microarray.mev.cgh.CGHGuiObj.AlgorithmResultsViewers.NumberOfAlterationsViewers.NumberOfAlterationsViewer;
import org.tigr.microarray.mev.cluster.algorithm.AlgorithmException;
import org.tigr.microarray.mev.cluster.gui.ICGHCloneValueMenu;
import org.tigr.microarray.mev.cluster.gui.IClusterGUI;
import org.tigr.microarray.mev.cluster.gui.IData;
import org.tigr.microarray.mev.cluster.gui.IFramework;
import org.tigr.microarray.mev.cluster.gui.IViewer;
import org.tigr.microarray.mev.cluster.gui.LeafInfo;

/**
 *
 * @author  Adam Margolin
 * @author Raktim Sinha
 */
public abstract class NumberOfAlterationsCalculator implements IClusterGUI {

    public IFramework framework;

    public IData data;

    public String nodeName = "";

    /** Creates a new instance of NumberOfAlterationsCalculator */
    public NumberOfAlterationsCalculator() {
    }

    public abstract DefaultMutableTreeNode execute(IFramework framework) throws AlgorithmException;

    /**
     * NOT USED
     * Replaced by one below - DefaultMutableTreeNode createResultsTree(Collection results)
     * @param results
     * @return
     */
    public DefaultMutableTreeNode createResultsTree2(Collection results) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(nodeName);
        root.add(new DefaultMutableTreeNode(new LeafInfo("Parameters", createParametersViewer())));
        root.add(new DefaultMutableTreeNode(new LeafInfo("Results", createResultsViewer(results))));
        return root;
    }

    /**
     * New method to be consistent with Mev Look & Feel
     * @param results
     * @return
     */
    public DefaultMutableTreeNode createResultsTree(Collection results) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(nodeName);
        root.add(new DefaultMutableTreeNode(new LeafInfo("Results", createResultsViewer(results))));
        addGeneralInfo(root);
        return root;
    }

    /**
     * Raktim 4/27
     * Added for State Saving to capture the File used for LoadGeneList
     * @param results
     * @param file
     * @return
     */
    public DefaultMutableTreeNode createResultsTree(Collection results, String file) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(nodeName);
        root.add(new DefaultMutableTreeNode(new LeafInfo("Results", createResultsViewer(results))));
        addGeneralInfo(root);
        return root;
    }

    public DefaultMutableTreeNode createResultsTree(Collection results, boolean addInfo) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(nodeName);
        root.add(new DefaultMutableTreeNode(new LeafInfo("Results", createResultsViewer(results))));
        if (addInfo) addGeneralInfo(root);
        return root;
    }

    private IViewer createResultsViewer(Collection results) {
        AlterationRegion[] alterationRegions = new AlterationRegion[results.size()];
        Iterator it = results.iterator();
        int i = 0;
        while (it.hasNext()) {
            AlterationRegion alterationRegion = (AlterationRegion) it.next();
            alterationRegions[i++] = alterationRegion;
        }
        NumberOfAlterationsDataModel dataModel = new NumberOfAlterationsDataModel();
        dataModel.setAlterationRegions(alterationRegions);
        NumberOfAlterationsViewer viewer = new NumberOfAlterationsViewer();
        viewer.setData(framework.getData());
        viewer.setDataModel(dataModel);
        return viewer;
    }

    public IViewer createParametersViewer() {
        return new AlterationParametersViewer(framework);
    }

    /**
     * Adds node with general iformation.
     */
    private void addGeneralInfo(DefaultMutableTreeNode root) {
        ICGHCloneValueMenu menu = framework.getCghCloneValueMenu();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("General Information");
        node.add(new DefaultMutableTreeNode("Amplification Threshold: " + menu.getAmpThresh()));
        node.add(new DefaultMutableTreeNode("Deletion Threshold: " + menu.getDelThresh()));
        node.add(new DefaultMutableTreeNode("Amplification 2 Copy Threshold: " + menu.getAmpThresh2Copy()));
        node.add(new DefaultMutableTreeNode("Deletion 2 Copy Threshold: " + menu.getDelThresh2Copy()));
        root.add(node);
    }

    /**
     * Getter for property fcd.
     * @return Value of property fcd.
     * Not used in New Struct

    public org.abramson.microarray.cgh.CGHFcdObj.CGHMultipleArrayDataFcd getFcd() {
        return fcd;
    }
    */
    public IData getData() {
        return this.data;
    }

    /**
     * Setter for property fcd.
     * @param fcd New value of property fcd.
     * Not used in New Struct

    public void setFcd(org.abramson.microarray.cgh.CGHFcdObj.CGHMultipleArrayDataFcd fcd) {
        this.fcd = fcd;
    }
    */
    public void setData(IData dat) {
        this.data = dat;
    }

    /** Getter for property framework.
     * @return Value of property framework.
     */
    public IFramework getFramework() {
        return framework;
    }

    /** Setter for property framework.
     * @param framework New value of property framework.
     */
    public void setFramework(IFramework framework) {
        this.framework = framework;
    }
}
