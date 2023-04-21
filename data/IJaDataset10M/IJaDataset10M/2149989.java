package com.loribel.commons.gui.bo.metamodel.form.line;

import javax.swing.JLabel;
import com.loribel.commons.abstraction.GB_Refreshable;
import com.loribel.commons.business.impl.bo.GB_BOPropertyBO;
import com.loribel.commons.swing.GB_PanelCols;

/**
 * Property Line
 *
 * @author Gr�gory Borelli
 */
public class GB_BOPropertyLineOther extends GB_BOPropertyLineAbstract {

    public GB_BOPropertyLineOther(GB_BOPropertyBO a_property, GB_Refreshable a_refreshable) {
        super(a_property, a_refreshable);
    }

    protected void appendMainComponent(GB_PanelCols a_panel) {
        String l_labelStr = "Visuel non g�r� en mode template";
        JLabel l_label = new JLabel(l_labelStr);
        a_panel.addColFill(l_label);
    }
}
