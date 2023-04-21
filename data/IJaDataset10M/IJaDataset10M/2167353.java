package com.alveole.studio.web.managers;

import com.alveole.interfaces.ProjectContext;
import com.alveole.studio.web.data.NodeContainer;
import com.alveole.studio.web.data.NodeLink;
import com.alveole.studio.web.data.NodePackage;
import com.alveole.studio.web.data.ProjectChangeListener;
import com.alveole.studio.web.packager.dialogs.DialogListener;
import com.alveole.studio.web.packager.dialogs.EditInterpackageLinkSettings;

/**
 * This class handles package to package links.
 * 
 * @author sylvain
 *
 */
public class PackageToAnyLinksManager extends BasicLinkManager {

    /**
	 * This plguin ID.
	 */
    public static final String ExtId = "com.alveole.studio.links.PackageToAny";

    /**
	 * Check if can link.
	 * @param context The project context.
	 * @param node1 The origin node.
	 * @param node2 The target node.
	 */
    public boolean canLink(ProjectContext context, NodeContainer node1, NodeContainer node2) {
        if (node1 instanceof NodePackage || node2 instanceof NodePackage) return true;
        return false;
    }

    /**
	 * Create a copy of this link.
	 */
    public NodeLink copyLink(ProjectContext context, NodeLink original, NodeContainer node1, NodeContainer node2) {
        NodeLink ret = new NodeLink(ExtId, original.getLabel(), node1, node2);
        ret.setArrowNode1(original.isArrowNode1());
        ret.setArrowNode2(original.isArrowNode2());
        context.getProject().addLink(ret);
        return ret;
    }

    /**
	 * A data-model associated to a View Editor.
	 * 
	 * @author sylvain
	 *
	 */
    public class CreateLinkListener implements DialogListener {

        /**
		 * The nodes to link.
		 */
        NodeContainer node1, node2;

        /**
		 * The project context.
		 */
        ProjectContext context;

        public void cancelClicked(Object source) {
        }

        public void okClicked(Object source) {
            EditInterpackageLinkSettings cpd = (EditInterpackageLinkSettings) source;
            NodeLink ret = new NodeLink(ExtId, cpd.strLabel, node1, node2);
            ret.setArrowNode1(cpd.boolLeftArrow);
            ret.setArrowNode2(cpd.boolRightArrow);
            context.getProject().addLink(ret);
        }
    }

    /**
	 * A data-model associated to a View Editor.
	 * @author sylvain
	 *
	 */
    public class EditLinkListener implements DialogListener {

        /**
		 * The link to edit.
		 */
        NodeLink link;

        /**
		 * The project context.
		 */
        ProjectContext context;

        public void cancelClicked(Object source) {
        }

        public void okClicked(Object source) {
            EditInterpackageLinkSettings cpd = (EditInterpackageLinkSettings) source;
            link.setLabel(cpd.strLabel);
            link.setArrowNode1(cpd.boolLeftArrow);
            link.setArrowNode2(cpd.boolRightArrow);
            context.getProject().fireChangeEvent(ProjectChangeListener.EventType.LinkChanged, link);
        }
    }

    /**
	 * Create a link.
	 * @param context the project context.
	 * @param node1 The origin node.
	 * @param node2 The target node.
	 */
    public void createLink(ProjectContext context, NodeContainer node1, NodeContainer node2) {
        EditInterpackageLinkSettings cpd = new EditInterpackageLinkSettings();
        CreateLinkListener cll = new CreateLinkListener();
        cll.node1 = node1;
        cll.node2 = node2;
        cll.context = context;
        cpd.setDialogListener(cll);
        cpd.openView(context);
    }

    /**
	 * Edit a link.
	 * @param context The project context.
	 * @param link The link to edit.
	 * @param shortView The edition mode.
	 */
    public void editLink(ProjectContext context, NodeLink link, boolean shortView) {
        EditInterpackageLinkSettings cpd = new EditInterpackageLinkSettings();
        EditLinkListener cll = new EditLinkListener();
        cll.link = link;
        cll.context = context;
        cpd.boolLeftArrow = link.arrowNode1();
        cpd.boolRightArrow = link.arrowNode2();
        cpd.strLabel = link.getLabel();
        cpd.setDialogListener(cll);
        cpd.openView(context);
        context.setFocus();
    }
}
