package com.loribel.commons.swing.button;

import javax.swing.Icon;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_LabelIconOwner;
import com.loribel.commons.swing.GB_ButtonAction;
import com.loribel.commons.swing.tools.GB_ButtonTools;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * Tool button to open into default application.
 */
public class GB_TButtonListSelectAll extends GB_ButtonAction implements GB_LabelIconOwner {

    private ListModel listModel;

    private ListSelectionModel selectionModel;

    private static GB_LabelIcon labelIcon;

    public GB_TButtonListSelectAll(ListModel a_listModel, ListSelectionModel a_selectionModel) {
        super();
        listModel = a_listModel;
        selectionModel = a_selectionModel;
        GB_ButtonTools.decoreWithLabelIcon(this);
    }

    public void doAction() {
        int l_min = 0;
        int l_max = listModel.getSize() - 1;
        if (l_max == -1) {
            l_min = -1;
        }
        selectionModel.setSelectionInterval(l_min, l_max);
    }

    public GB_LabelIcon getLabelIcon() {
        if (labelIcon == null) {
            String l_label = AA.BUTTON_SELECT_ALL;
            String l_desc = AA.BUTTON_SELECT_ALL;
            Icon l_icon = GB_IconTools.get(AA.ICON.X16_LIST_SELECTION_ALL);
            labelIcon = GB_LabelIconTools.newLabelIcon(l_label, l_icon, l_desc);
        }
        return labelIcon;
    }
}
