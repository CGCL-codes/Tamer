package com.loribel.commons.gui.bo.table;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_LabelIconOwner;
import com.loribel.commons.abstraction.GB_LabelIconSet;
import com.loribel.commons.abstraction.GB_ObjectFilter;
import com.loribel.commons.business.GB_BOSelectorTools;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.gui.bo.GB_BOGuiTools;
import com.loribel.commons.swing.GB_ButtonAction;
import com.loribel.commons.swing.table.GB_TableModelFilter;
import com.loribel.commons.swing.tools.GB_ButtonTools;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * Button to decorate GB_Table.
 *
 * @author Gregory Borelli
 */
public class GB_BOTableButtonFilter extends GB_ButtonAction implements GB_LabelIconOwner {

    private GB_LabelIcon labelIcon;

    private JTable table;

    private GB_SimpleBusinessObject boSearch;

    private GB_TableModelFilter myModel;

    private GB_ObjectFilter myFilter;

    public GB_BOTableButtonFilter(JTable a_table, GB_SimpleBusinessObject a_boSearch) {
        super();
        GB_ButtonTools.decoreWithLabelIcon(this);
        table = a_table;
        boSearch = a_boSearch;
        TableModel l_model = table.getModel();
        if (!(l_model instanceof GB_TableModelFilter)) {
            throw new IllegalArgumentException("Invalid table model, must be a GB_TableModelFilter");
        }
        myModel = (GB_TableModelFilter) table.getModel();
        myFilter = GB_BOSelectorTools.newFilter(boSearch);
    }

    public void doAction() {
        String l_title = AA.TITLE_FILTER;
        boolean r = GB_BOGuiTools.showEditBo(this, boSearch, l_title, null, null);
        if (!r) {
            return;
        }
        myModel.setFilter(myFilter);
        myModel.setFilterActive(true);
        myModel.updateItems(false);
        myModel.fireTableDataChanged();
    }

    public GB_LabelIcon getLabelIcon() {
        if (labelIcon == null) {
            Icon l_icon = GB_IconTools.get(AA.ICON.X16_FILTER);
            GB_LabelIconSet retour = GB_LabelIconTools.newLabelIcon(AA.BUTTON_FILTER_, l_icon);
            retour.setDescription(AA.BUTTON_FILTER_TABLE_DESC);
            labelIcon = retour;
        }
        return labelIcon;
    }
}
