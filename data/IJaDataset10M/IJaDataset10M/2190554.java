package com.loribel.commons.business.convertor;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.loribel.commons.business.GB_BOMetaDataFactoryTools;
import com.loribel.commons.business.GB_BOXmlTools;
import com.loribel.commons.business.abstraction.GB_BOMetaData;
import com.loribel.commons.business.abstraction.GB_BOProperty;
import com.loribel.commons.business.impl.bo.GB_BOMetaDataBO;
import com.loribel.commons.business.impl.bo.GB_BOPropertyBO;
import com.loribel.commons.business.metamodel.GB_BOMetaModelTools;
import com.loribel.commons.util.CTools;

/**
 * Generate BO Short from non Short.
 */
public class GB_BO2BOShort {

    public GB_BO2BOShort() {
    }

    private void addPropertyShort(String a_boName, GB_BOMetaDataBO a_bo, GB_BOProperty a_property) {
        if (a_property.isPropertyMulti()) {
            if (a_property.getType() == String.class) {
                String l_name = a_property.getName();
                GB_BOPropertyBO l_property = new GB_BOPropertyBO(l_name + "List");
                l_property.setType(String.class);
                l_property.setPropertyIdLink(a_boName + "." + l_name);
                l_property.setSemantic(null);
                a_bo.addBoProperty(l_property);
            }
            return;
        }
        if (a_property.isPropertyMap()) {
            return;
        }
        if (a_property.isBusinessObject()) {
            return;
        }
        if (a_property.isLabel()) {
            return;
        }
        if (a_property.isCalculated()) {
            return;
        }
        if (a_property.isScrollable()) {
            return;
        }
        String l_name = a_property.getName();
        GB_BOPropertyBO l_property = new GB_BOPropertyBO(l_name);
        l_property.setRefId(a_boName + "." + l_name);
        a_bo.addBoProperty(l_property);
    }

    private Node metaDateToNode(GB_BOMetaDataBO a_metaData, Document a_doc) {
        return GB_BOXmlTools.toNode(a_metaData, a_doc);
    }

    public Node toNode(GB_BOMetaData a_bo, Document a_doc) {
        GB_BOMetaDataBO l_metaData = toShort(a_bo);
        return metaDateToNode(l_metaData, a_doc);
    }

    public Node toNode(String a_boName, Document a_doc) {
        GB_BOMetaDataBO l_metaData = toShort(a_boName);
        return metaDateToNode(l_metaData, a_doc);
    }

    public GB_BOMetaDataBO toShort(GB_BOMetaData a_bo) {
        String l_boName = a_bo.getId();
        String l_id = l_boName + "Short";
        GB_BOMetaDataBO retour = new GB_BOMetaDataBO(l_id);
        retour.setSemantic(GB_BOMetaData.SEMANTIC.SHORT);
        GB_BOProperty[] l_properties = a_bo.getBOProperties();
        int len = CTools.getSize(l_properties);
        for (int i = 0; i < len; i++) {
            GB_BOProperty l_property = l_properties[i];
            addPropertyShort(l_boName, retour, l_property);
        }
        GB_BOMetaModelTools.cleanMetaData(retour);
        return retour;
    }

    public GB_BOMetaDataBO toShort(String a_boName) {
        GB_BOMetaData l_metaData = GB_BOMetaDataFactoryTools.getFactory().getBOMetaData(a_boName);
        return toShort(l_metaData);
    }
}
