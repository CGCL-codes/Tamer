package com.loribel.commons.gui.bo.table;

import java.util.List;
import javax.swing.JTable;
import com.loribel.commons.abstraction.GB_TableModelSimple;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.gui.abstraction.GB_VMFactory;
import com.loribel.commons.gui.bo.GB_BOGuiTools;

/**
 * Tools.
 *
 * @author Gregory Borelli
 */
public class GB_BOTableTools {

    public static boolean editRow(JTable a_table, int a_index, GB_VMFactory a_vmFactory) {
        int l_index = a_index;
        if (l_index == -1) {
            l_index = a_table.getSelectedRow();
        }
        if (l_index == -1) {
            return false;
        }
        GB_TableModelSimple l_model = (GB_TableModelSimple) a_table.getModel();
        List l_items = l_model.getItems();
        if (l_items != null) {
            GB_SimpleBusinessObject l_bo = (GB_SimpleBusinessObject) l_items.get(l_index);
            return GB_BOGuiTools.showEditBo(a_table, l_bo, a_vmFactory, null, null, null);
        } else {
            return false;
        }
    }
}
