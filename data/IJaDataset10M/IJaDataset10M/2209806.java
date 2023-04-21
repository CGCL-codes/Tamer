package com.loribel.commons.gui.bo.tree;

import java.util.List;
import javax.swing.Icon;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_LabelIconSet;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.abstraction.swing.GB_ViewManagerOwner;
import com.loribel.commons.business.GB_BOMetaDataTools;
import com.loribel.commons.business.GB_BOPropertyListTools;
import com.loribel.commons.business.GB_BOTools;
import com.loribel.commons.business.abstraction.GB_BOProperty;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.gui.bo.vm.GB_BOTabbedDemoVM;
import com.loribel.commons.swing.tree.GB_TreeNodeWithChildren;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * TreeNode to represent a businessObject.
 * <ul>
 * <li>This node use meta data of BusinessObject to the label and icon of this node
 * <li>ViewManagerOwer (see GB_DemoBOVM for details)
 * <li>children = the values of property complex (flagBusinessObject == true)
 * </ul>
 */
class GB_BOTNDemo extends GB_TreeNodeWithChildren implements GB_ViewManagerOwner {

    /**
     * The businessObject associated to this node.
     */
    private GB_SimpleBusinessObject businessObject;

    /**
     * The viewManager associated to this node.
     */
    private GB_ViewManager viewManager;

    /**
     * Default constructor.
     */
    public GB_BOTNDemo(GB_SimpleBusinessObject a_businessObject) {
        super(a_businessObject);
        businessObject = a_businessObject;
        GB_BOProperty[] l_properties = GB_BOTools.getProperties(a_businessObject.getBOName());
        boolean l_allowsChildren = GB_BOPropertyListTools.containsComplexProperty(l_properties);
        this.setAllowsChildren(l_allowsChildren);
    }

    /**
     * Get the businessObject associated to this node.
     */
    public GB_SimpleBusinessObject getBusinessObject() {
        return businessObject;
    }

    /**
     * Get the viewManager associated to this node. Build the viewManager on the first call with an
     * instance of {@link GB_DemoBOVM}.
     */
    public GB_ViewManager getViewManager() {
        if (viewManager == null) {
            viewManager = new GB_BOTabbedDemoVM(businessObject, false);
        }
        return viewManager;
    }

    public boolean buildChildren2(boolean a_flagDeep) {
        List l = GB_BOTools.getAllCouplesOfPropertyValue(businessObject);
        if (l == null) {
            return false;
        }
        int len = l.size();
        GB_BOProperty l_property;
        Object l_value;
        for (int i = 0; i < len; i = i + 2) {
            l_property = (GB_BOProperty) l.get(i);
            l_value = l.get(i + 1);
            if (l_property.isBusinessObject()) {
                if (l_value != null) {
                    this.add(new GB_BOTNDemo((GB_SimpleBusinessObject) l_value));
                }
            }
        }
        return true;
    }

    public GB_LabelIcon getLabelIcon() {
        GB_LabelIcon l_labelIcon = GB_BOMetaDataTools.getLabelIcon(businessObject.getBOName());
        String l_label = l_labelIcon.getLabel();
        Icon l_icon = l_labelIcon.getIcon();
        GB_LabelIconSet retour = GB_LabelIconTools.newLabelIcon(l_label, l_icon);
        retour.setDescription(businessObject.toString());
        return retour;
    }
}
