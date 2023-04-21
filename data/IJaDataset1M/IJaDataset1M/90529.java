package com.triplea.rolap.plugins.acl;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import com.triplea.dao.Dimension;
import com.triplea.rolap.plugins.MutableList;

/**
 * @author kononyhin
 *
 */
public class DimensionElementAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    private JPluginACLDefaultController _controller = null;

    private int _actionType;

    public static final int ELEMENT_NEW = 1;

    public static final int ELEMENT_DELETE = 2;

    public DimensionElementAction(JPluginACLDefaultController controller, int actionType) {
        super();
        this._controller = controller;
        this._actionType = actionType;
        String text = "";
        switch(actionType) {
            case DimensionElementAction.ELEMENT_NEW:
                text = "New";
                break;
            case DimensionElementAction.ELEMENT_DELETE:
                text = "Delete";
                break;
        }
        this.putValue(Action.NAME, text);
    }

    public void actionPerformed(ActionEvent e) {
        Object eventSource = e.getSource();
        ObjectTreeNodeInfo nodeInfo = this._controller.getRuleTreeNodeInfo();
        Rule rule = null;
        if (null != nodeInfo) {
            rule = nodeInfo.getRule();
        }
        if (null == rule) {
            return;
        }
        Object invoker = ((JPopupMenu) ((JMenuItem) eventSource).getParent()).getInvoker();
        if (this._controller.getView().getArrayOfDimElementsList().contains(invoker)) {
            int index = this._controller.getView().getArrayOfDimElementsList().indexOf(invoker);
            MutableList dimensionList = this._controller.getView().getArrayOfDimElementsList().get(index);
            Dimension dimension = this._controller.getView().getArrayOfCubeDimensions().get(index);
            switch(this._actionType) {
                case DimensionElementAction.ELEMENT_NEW:
                    DimensionElementsDialog treeDialog = new DimensionElementsDialog(this._controller.getView().getFrame(), dimension);
                    treeDialog.setVisible(true);
                    if (treeDialog.isOkClicked()) {
                        ArrayList<String> elements = treeDialog.getSelectedElements();
                        for (int i = 0; i < elements.size(); i++) {
                            if (!dimensionList.getContents().contains(elements.get(i))) {
                                dimensionList.getContents().addElement(elements.get(i));
                            }
                        }
                        RuleDimension ruleDimension = rule.getRuleDimension(dimension.getName());
                        if (null != ruleDimension) {
                            ArrayList<String> dimElements = new ArrayList<String>();
                            for (int i = 0; i < dimensionList.getContents().size(); i++) {
                                dimElements.add(dimensionList.getContents().getElementAt(i));
                            }
                            ruleDimension.setElements(dimElements);
                            this._controller.rulesHasModifications(true);
                        }
                    }
                    break;
                case DimensionElementAction.ELEMENT_DELETE:
                    int[] indices = dimensionList.getContents().getSelectedIndices();
                    for (int i = indices.length - 1; i >= 0; i--) {
                        dimensionList.getContents().remove(i);
                    }
                    dimensionList.sort();
                    RuleDimension ruleDimension = rule.getRuleDimension(dimension.getName());
                    if (null != ruleDimension) {
                        ArrayList<String> dimElements = new ArrayList<String>();
                        for (int i = 0; i < dimensionList.getContents().size(); i++) {
                            dimElements.add(dimensionList.getContents().getElementAt(i));
                        }
                        ruleDimension.setElements(dimElements);
                        this._controller.rulesHasModifications(true);
                    }
                    break;
            }
        }
    }
}
