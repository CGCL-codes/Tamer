package com.loribel.commons.gui.bo.table;

import java.util.List;
import javax.swing.Icon;
import javax.swing.JTable;
import com.loribel.commons.abstraction.GB_TableModelSimple;
import com.loribel.commons.business.GB_BOIconTools;
import com.loribel.commons.business.abstraction.GB_BOProperty;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObjectSet;
import com.loribel.commons.gui.abstraction.GB_VMFactory;
import com.loribel.commons.gui.bo.GB_BOGuiTools;
import com.loribel.commons.swing.table.GB_TableButtonEdit;

/**
 * Button to decorate JTable with button edit.
 *
 * @author Gregory Borelli
 */
public class GB_BOPropertyTableButtonEdit extends GB_TableButtonEdit {

    protected GB_SimpleBusinessObjectSet bo;

    protected String propertyName;

    private GB_VMFactory vmFactory;

    public GB_BOPropertyTableButtonEdit(JTable a_table, GB_SimpleBusinessObject a_bo, String a_propertyName, GB_VMFactory a_vmFactory) {
        super(a_table);
        bo = (GB_SimpleBusinessObjectSet) a_bo;
        propertyName = a_propertyName;
        vmFactory = a_vmFactory;
        GB_BOProperty l_property = a_bo.getProperty(a_propertyName);
        String l_boType = l_property.getBoType();
        Icon l_icon = GB_BOIconTools.getIconEditForBO(l_boType);
        this.setIcon(l_icon);
    }

    public void doAction() throws Exception {
        int l_index = myTable.getSelectedRow();
        if (l_index == -1) {
            return;
        }
        GB_TableModelSimple l_model = (GB_TableModelSimple) myTable.getModel();
        List l_items = l_model.getItems();
        GB_SimpleBusinessObject l_bo = (GB_SimpleBusinessObject) l_items.get(l_index);
        if (editBO(l_bo)) {
            l_model.fireTableDataChanged();
        }
        doAfter();
    }

    /**
     * Overwrite this method to customize the edit
     */
    protected boolean editBO(GB_SimpleBusinessObject a_bo) throws Exception {
        return GB_BOGuiTools.showEditBo(myTable, a_bo, vmFactory, null, null, null);
    }

    /**
     * Overwrite this method to do action after edit.
     */
    protected void doAfter() throws Exception {
    }
}
