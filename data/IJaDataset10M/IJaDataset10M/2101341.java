package com.loribel.commons.util.impl;

import javax.swing.Icon;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.business.GB_BOLabelIconTools;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * Implementation for couple value (type GB_SimpleBusinessObject).
 * 
 * @author Gregory Borelli
 */
public class GB_CoupleValuesBOImpl extends GB_CoupleValuesImpl {

    public GB_CoupleValuesBOImpl() {
        super();
    }

    public GB_CoupleValuesBOImpl(Object a_value1, Object a_value2) {
        super(a_value1, a_value2);
    }

    protected GB_LabelIcon buildLabelIcon() {
        Object l_value1 = getValue1();
        Object l_value2 = getValue2();
        Object l_value = l_value1;
        GB_LabelIcon retour = GB_BOLabelIconTools.newLabelIcon((GB_SimpleBusinessObject) l_value);
        if (retour == null) {
            l_value = l_value2;
            retour = GB_BOLabelIconTools.newLabelIconShort((GB_SimpleBusinessObject) l_value);
        }
        Icon l_decoreIcon = null;
        if (l_value1 == null) {
            if (l_value2 != null) {
                l_decoreIcon = GB_IconTools.get(AA.ICON.X16D_DELETE);
            }
        } else if (l_value2 == null) {
            l_decoreIcon = GB_IconTools.get(AA.ICON.X16D_NEW);
        }
        if (l_decoreIcon != null) {
            GB_LabelIconTools.updateWithDecoratedIcon(retour, l_decoreIcon);
        }
        return retour;
    }
}
