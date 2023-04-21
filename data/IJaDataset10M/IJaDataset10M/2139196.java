package com.loribel.commons.business.impl.node;

import java.util.List;
import javax.swing.Icon;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.loribel.commons.LNG;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.business.GB_BOPropertyTools;
import com.loribel.commons.business.GB_BOTools;
import com.loribel.commons.business.abstraction.GB_BOMetaData;
import com.loribel.commons.business.abstraction.GB_BOProperty;
import com.loribel.commons.business.impl.bo.GB_BOMetaDataBO;
import com.loribel.commons.business.impl.node.generated.GB_BOMetaDataNodeGen;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_ArrayTools;
import com.loribel.commons.util.GB_IndexOwnerTools;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * Class GB_BOMetaDataNode.
 */
public class GB_BOMetaDataNode extends GB_BOMetaDataNodeGen implements GB_BOMetaData {

    private String description;

    private Icon icon;

    private String label;

    private GB_BOProperty[] properties;

    public GB_BOMetaDataNode(Node a_node) {
        super(a_node);
    }

    public GB_BOProperty[] getBOProperties() {
        if (properties == null) {
            List l_list = CTools.toList(getBoPropertyArray());
            GB_BOExtendsNode l_extendsNode = getBoExtends();
            if (l_extendsNode != null) {
                String l_boName = l_extendsNode.getName();
                String[] l_propertyNames;
                String[] l_namesToExclude = l_extendsNode.getPropertiesToExclude();
                if (l_namesToExclude != null) {
                    l_propertyNames = GB_BOTools.getPropertyNames(l_boName);
                    l_propertyNames = (String[]) GB_ArrayTools.removeAll(l_propertyNames, l_namesToExclude, new String[0]);
                } else {
                    l_propertyNames = l_extendsNode.getPropertiesToInclude();
                }
                int len = CTools.getSize(l_propertyNames);
                for (int i = 0; i < len; i++) {
                    String l_propertyName = l_propertyNames[i];
                    GB_BOProperty l_property = GB_BOPropertyTools.getProperty(l_boName, l_propertyName);
                    l_list.add(l_property);
                }
            }
            properties = (GB_BOProperty[]) l_list.toArray(new GB_BOProperty[l_list.size()]);
            int len = CTools.getSize(properties);
            for (int i = 0; i < len; i++) {
                GB_BOProperty l_property = properties[i];
                l_property = GB_BOPropertyTools.updateProperty(l_property);
                properties[i] = l_property;
            }
            GB_IndexOwnerTools.sortByIndex(properties);
        }
        return properties;
    }

    public Element getConfigNode() {
        return getNode();
    }

    public String getDescription() {
        if (description == null) {
            description = getDescription(AA.NAME_DEFAULT, LNG.DEFAULT, true);
        }
        return description;
    }

    public String getDescription(String a_name, String a_lng, boolean a_useDefault) {
        GB_BOStringMNode l_str = getDescriptionM(a_name);
        if (l_str == null) {
            return null;
        }
        return l_str.getString(a_lng, a_useDefault);
    }

    public Icon getIcon() {
        if (icon == null) {
            icon = getIcon(AA.NAME_DEFAULT);
        }
        return icon;
    }

    public String getLabel() {
        if (label == null) {
            label = getLabel(AA.NAME_DEFAULT, LNG.DEFAULT, true);
        }
        return label;
    }

    public String getLabel(String a_name, String a_lng, boolean a_useDefault) {
        GB_BOStringMNode l_str = getLabelM(a_name);
        if (l_str == null) {
            return null;
        }
        return l_str.getString(a_lng, a_useDefault);
    }

    public GB_LabelIcon getLabelIcon() {
        String l_label = getLabel();
        String l_desc = getDescription();
        Icon l_icon = getIcon();
        return GB_LabelIconTools.newLabelIcon(l_label, l_icon, l_desc);
    }

    public String getLabelInfo() {
        return GB_BOMetaDataBO.BO_NAME + " [" + getId() + "]";
    }
}
