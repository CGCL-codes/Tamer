package com.loribel.commons.gui.bo.metamodel.tree.drag;

import javax.swing.Icon;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.business.abstraction.GB_BOProperty;
import com.loribel.commons.business.impl.bo.GB_BOMetaDataBO;
import com.loribel.commons.business.impl.bo.GB_BOPropertyBO;
import com.loribel.commons.swing.abstraction.GB_TreeNode;
import com.loribel.commons.swing.tree.GB_TreeNodeWithChildren;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * TN.
 *
 * @author Gregory Borelli
 */
public class GB_BOBOMetaDataDragTN extends GB_TreeNodeWithChildren {

    private GB_BOMetaDataBO bo;

    public GB_BOBOMetaDataDragTN(GB_BOMetaDataBO a_bo) {
        super(a_bo.getId());
        bo = a_bo;
    }

    public GB_LabelIcon getLabelIcon() {
        String l_label = bo.getId();
        Icon l_icon = GB_IconTools.get(AA.ICON.X16_FILE);
        String l_desc = null;
        return GB_LabelIconTools.newLabelIcon(l_label, l_icon, l_desc);
    }

    public boolean buildChildren2(boolean a_flagDeep) throws Exception {
        GB_BOProperty[] l_properties = bo.getBOProperties();
        int len = CTools.getSize(l_properties);
        for (int i = 0; i < len; i++) {
            GB_BOPropertyBO l_property = (GB_BOPropertyBO) l_properties[i];
            addChildProperty(l_property);
        }
        return true;
    }

    private void addChildProperty(GB_BOPropertyBO a_property) {
        GB_TreeNode l_node = new GB_BOBOPropertyDragTN(a_property);
        add(l_node);
    }
}
