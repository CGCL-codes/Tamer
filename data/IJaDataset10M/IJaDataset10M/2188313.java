package com.loribel.commons.business.metamodel.visitor;

import java.util.ArrayList;
import java.util.List;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.business.impl.bo.GB_BOMetaDataBO;
import com.loribel.commons.business.impl.bo.GB_BOPropertyBO;
import com.loribel.commons.business.impl.bo.generated.GB_BOVisitor;
import com.loribel.commons.business.metamodel.GB_BOMetaModel;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.Info;

/**
 * Tools.
 *
 * @author Gregory Borelli
 */
public final class GB_BOVisitorTools {

    public static Object[] visitList(GB_SimpleBusinessObject[] a_children, GB_BOVisitor a_visitor) {
        if (a_visitor == null) {
            return null;
        }
        int len = CTools.getSize(a_children);
        int l_infoId = Info.newId(len);
        Object[] retour = new Object[len];
        for (int i = 0; i < len; i++) {
            Info.setInfoList(l_infoId, i);
            GB_SimpleBusinessObject l_bo = a_children[i];
            if ((l_bo != null) && (l_bo instanceof GB_BOMetaModel)) {
                GB_BOMetaModel l_boMetaModel = (GB_BOMetaModel) l_bo;
                retour[i] = l_boMetaModel.accept(a_visitor);
            }
        }
        Info.end(l_infoId);
        return retour;
    }

    public static Object[] visitListWithTree(GB_SimpleBusinessObject[] a_bos, GB_BOVisitor a_visitor, boolean a_visitChildBefore) {
        if (a_visitor == null) {
            return null;
        }
        GB_BOVisitor l_visitor = new GB_BOTreeVisitor(a_visitor, a_visitChildBefore);
        int len = CTools.getSize(a_bos);
        int l_infoId = Info.newId(len);
        Object[] retour = new Object[len];
        for (int i = 0; i < len; i++) {
            Info.setInfoList(l_infoId, i);
            GB_SimpleBusinessObject l_bo = a_bos[i];
            if ((l_bo != null) && (l_bo instanceof GB_BOMetaModel)) {
                GB_BOMetaModel l_boMetaModel = (GB_BOMetaModel) l_bo;
                retour[i] = l_boMetaModel.accept(l_visitor);
            }
        }
        Info.end(l_infoId);
        return retour;
    }

    public static Object[] visitProperties(GB_BOMetaDataBO[] a_bos, GB_BOVisitor a_visitor) {
        List retour = new ArrayList();
        int len = CTools.getSize(a_bos);
        for (int i = 0; i < len; i++) {
            GB_BOMetaDataBO l_boMetaData = a_bos[i];
            GB_BOPropertyBO[] l_properties = l_boMetaData.getBoPropertyArray();
            int len2 = CTools.getSize(l_properties);
            for (int j = 0; j < len2; j++) {
                GB_BOPropertyBO l_property = l_properties[j];
                retour.add(l_property.accept(a_visitor));
            }
        }
        return retour.toArray();
    }

    public static Object visitWithTree(GB_BOMetaModel a_bo, GB_BOVisitor a_visitor, boolean a_visitChildBefore) {
        if (a_bo == null) {
            return null;
        }
        if (a_visitor == null) {
            return null;
        }
        GB_BOTreeVisitor l_visitor = new GB_BOTreeVisitor(a_visitor, a_visitChildBefore);
        return a_bo.accept(l_visitor);
    }
}
