package com.loribel.commons.module.debug.gui.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import com.loribel.commons.abstraction.GB_Debugable;
import com.loribel.commons.module.debug.GB_DebugableTools;

/**
 * Tree Node to represent Debugable Objects.
 *
 * @author Gregory Borelli
 */
public final class GB_DebuggableTNTools {

    public static DefaultMutableTreeNode newTreeNode(Object a_item) {
        if (a_item == null) {
            return new DefaultMutableTreeNode("null");
        } else if (a_item instanceof GB_Debugable) {
            return new GB_DebuggableTN((GB_Debugable) a_item);
        } else {
            Object l_debug = GB_DebugableTools.newDebugableLight(null, a_item);
            if (l_debug instanceof GB_Debugable) {
                return new GB_DebuggableTN((GB_Debugable) l_debug);
            } else {
                return new DefaultMutableTreeNode(l_debug);
            }
        }
    }
}
