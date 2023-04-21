package com.loribel.commons.swing.tools;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import com.loribel.commons.GB_FwkGuiInitializer;
import com.loribel.commons.util.GB_DebugTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.impl.GB_LabelIconImpl;

/**
 * Demo GB_GuiOrderTools.
 * 
 * @author Gregory Borelli
 */
public class GB_GuiOrderToolsDemo {

    public static void main(String[] args) {
        GB_FwkGuiInitializer.initAll();
        List l_items = new ArrayList();
        int len = 10;
        for (int i = 0; i < len; i++) {
            Object l_item = new GB_LabelIconImpl("Test" + i);
            l_items.add(l_item);
        }
        Dimension l_dim = new Dimension(500, 400);
        GB_GuiOrderTools.showOrderDialog(null, l_items, l_dim, "Trier une liste");
        List l_itemsStr = GB_LabelIconTools.getLabels(l_items);
        GB_DebugTools.debugCollection(GB_GuiOrderToolsDemo.class, "Liste tri�e", l_itemsStr);
    }
}
