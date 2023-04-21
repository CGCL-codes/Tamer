package com.simpledata.bc.uicomponents.conception.wizards;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import com.simpledata.bc.datamodel.*;
import com.simpledata.bc.uicomponents.*;

/**
 * Defines the step panel for choosing new tarif's mapping
 */
public class TarifMappingSelectionStep extends StepPanel implements TreeSelectionListener {

    private Tarification tarification = null;

    private TarificationExplorerTree tet = null;

    private BCNode currentNode = null;

    public TarifMappingSelectionStep(NewTarifWizard ntw, Tarification t) {
        super(ntw);
        this.tarification = t;
        initComponents();
    }

    private void initComponents() {
        this.tet = new TarificationExplorerTree(this.tarification.getTreeBase(), false, false, null);
        this.tet.addTreeSelectionListener(this);
        this.tet.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    /**
	 * @see com.simpledata.bc.uicomponents.conception.wizards.StepPanel#doCancel()
	 */
    public void doCancel() {
    }

    public void doBack() {
    }

    /**
	 * @see com.simpledata.bc.uicomponents.conception.wizards.StepPanel#doNext()
	 */
    public void doNext() {
        this.owner().setUserObject("node", this.currentNode);
    }

    public void doFinish() {
    }

    public JComponent getDisplay() {
        return this.tet;
    }

    public String getStepTitle() {
        return new String("STEP 1. Select tarif position");
    }

    public String getStepDescription() {
        return new String("Select the node you wish the tarif to be linked on, and press Next");
    }

    public void doButtonEnabling() {
        this.owner().setCancel(true);
        this.owner().setBack(false);
        if (this.currentNode == null) {
            this.owner().setNext(false);
        } else {
            this.owner().setNext(true);
        }
        this.owner().setFinish(false);
    }

    public void refreshState() {
        this.currentNode = (BCNode) this.owner().getUserObject("node");
        if (this.currentNode != null) {
            TarificationExplorerNode tn = tet.getTEN(this.currentNode);
            if (tn != null) this.tet.selectNode(tn);
        }
    }

    public void valueChanged(TreeSelectionEvent event) {
        TreePath tp = event.getNewLeadSelectionPath();
        if (tp != null) {
            setCurrentNode((TarificationExplorerNode) tp.getLastPathComponent());
        } else {
            setCurrentNode(null);
        }
    }

    /** change the currently selected node **/
    private void setCurrentNode(TarificationExplorerNode ten) {
        if (ten != null) {
            this.currentNode = ten.getBCNode();
        } else {
            this.currentNode = null;
        }
        this.owner().setNext(this.currentNode != null);
    }
}
