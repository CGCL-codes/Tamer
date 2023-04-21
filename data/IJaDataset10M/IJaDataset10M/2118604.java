package com.loribel.tools.xa.vm;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JComponent;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.loribel.commons.abstraction.GB_NodeOwner;
import com.loribel.commons.abstraction.GB_NodesSelector2;
import com.loribel.commons.abstraction.GB_Unregisterable;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.gui.GB_ErrorDialog;
import com.loribel.commons.gui.GB_VMTools;
import com.loribel.commons.gui.GB_ViewManagerAbstract;
import com.loribel.commons.swing.GB_Button;
import com.loribel.commons.swing.GB_ButtonFactory;
import com.loribel.commons.swing.GB_Panel;
import com.loribel.commons.swing.GB_PanelRows;
import com.loribel.commons.swing.GB_PanelZonesSplit;
import com.loribel.commons.xml.GB_NodeListTools;
import com.loribel.tools.xa.GB_XSUtils;
import com.loribel.tools.xml.template.GB_XmlGuiTemplate;
import com.loribel.tools.xml.template.def.GB_XmlGuiTemplate2;
import com.loribel.tools.xml.template.vm.GB_TreeFilterXmlVM;

/**
 * XS - Tools
 *
 * @author Gr�gory Borelli
 */
public class GB_XSToolsVM extends GB_ViewManagerAbstract {

    private GB_NodeOwner nodeOwner;

    private GB_XSToolsModel myModel;

    private GB_ViewManager vmXmlAction;

    private GB_Panel treePanel;

    private GB_ViewManager treeVM;

    protected GB_XSToolsVM() {
        super();
    }

    public GB_XSToolsVM(GB_XSToolsModel a_model, GB_NodeOwner a_nodeOwner) {
        super();
        nodeOwner = a_nodeOwner;
        myModel = a_model;
    }

    protected void setModel(GB_XSToolsModel a_model) {
        myModel = a_model;
    }

    protected void setNodeOwner(GB_NodeOwner a_nodeOwner) {
        nodeOwner = a_nodeOwner;
    }

    /**
     * buildView
     *
     * @return JComponent
     */
    protected JComponent buildView() {
        vmXmlAction = myModel.getViewManager();
        JComponent l_view = GB_VMTools.getViewSafe(vmXmlAction, true);
        return new MyView(l_view);
    }

    private GB_Panel buildTreePanel() {
        GB_Panel retour = new GB_Panel();
        retour.setLayout(new GridLayout(1, 1));
        retour.setPreferredSize(new Dimension(200, 200));
        return retour;
    }

    private void updateTreePanel(NodeList a_nodes) {
        if (treeVM != null) {
            treeVM.stop(true);
        }
        GB_XmlGuiTemplate l_template = new GB_XmlGuiTemplate2();
        treeVM = new GB_TreeFilterXmlVM(a_nodes, l_template);
        JComponent l_view = GB_VMTools.getViewSafe(treeVM, true);
        treePanel.removeAll();
        treePanel.add(l_view);
        treePanel.forceRepaint();
    }

    public boolean stop(boolean a_flagOk) {
        if (vmXmlAction != null) {
            if (!vmXmlAction.stop(a_flagOk)) {
                return false;
            }
        }
        return super.stop(a_flagOk);
    }

    private class MyView extends GB_PanelZonesSplit {

        MyView(JComponent a_stringActionView) {
            this.setZone1(buildFilterPanel(a_stringActionView), "Filtre");
            treePanel = buildTreePanel();
            this.setZone2(treePanel, "Resultat");
        }

        private JComponent buildFilterPanel(JComponent a_stringActionView) {
            GB_PanelRows retour = new GB_PanelRows();
            retour.addRowFill2(a_stringActionView);
            retour.addRowFill(buildButtonsBar());
            retour.setPreferredSize(new Dimension(200, 200));
            return retour;
        }

        protected JComponent buildButtonsBar() {
            Collection l = new ArrayList();
            l.add(new MyButtonApply());
            JComponent retour = GB_ButtonFactory.newButtonsPanel(l, true);
            return retour;
        }
    }

    private class MyButtonApply extends GB_Button implements ActionListener, GB_Unregisterable {

        MyButtonApply() {
            super(AA.BUTTON_APPLY);
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent a_event) {
            try {
                GB_NodesSelector2 l_selector = myModel.getNodesSelector();
                Node[] l_nodes = GB_XSUtils.getSelectedNodes(nodeOwner, l_selector);
                NodeList l_list = GB_NodeListTools.toNodeList(l_nodes);
                updateTreePanel(l_list);
            } catch (Exception e) {
                GB_ErrorDialog.showErrorMsg(view, e);
            }
        }

        public boolean unregister() {
            this.removeActionListener(this);
            return true;
        }
    }
}
