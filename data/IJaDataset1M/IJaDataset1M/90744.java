package com.loribel.commons.business.impl.bo;

import javax.swing.Icon;
import org.w3c.dom.Element;
import com.loribel.commons.LNG;
import com.loribel.commons.abstraction.GB_IdOwner;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_TypeOwnerSet;
import com.loribel.commons.business.abstraction.GB_BOEnumItem;
import com.loribel.commons.business.abstraction.GB_BOEnumValues;
import com.loribel.commons.business.abstraction.GB_BOProperty;
import com.loribel.commons.business.impl.bo.generated.GB_BOPropertyBOGen;
import com.loribel.commons.business.metamodel.GB_BOLabelsDescMOwner;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.GB_MapTools;
import com.loribel.commons.util.GB_NameOwnerTools;

/**
 * Class GB_BOPropertyBO.
 */
public class GB_BOPropertyBO extends GB_BOPropertyBOGen implements GB_BOProperty, GB_TypeOwnerSet, GB_IdOwner, GB_BOLabelsDescMOwner {

    private GB_BOProperty propertyOrigin;

    public GB_BOPropertyBO() {
        super();
    }

    public GB_BOPropertyBO(String a_name) {
        super();
        this.setName(a_name);
    }

    protected String appendToString() {
        GB_BOMetaDataBO l_boParent = getBoMetaDataParent();
        String l_id = "";
        if (l_boParent != null) {
            l_id = l_boParent.getId() + ".";
        }
        l_id += getName();
        String retour = super.appendToString() + ", id=" + l_id;
        return retour;
    }

    public void clearExtensionByName(String a_name) {
        GB_BOExtensionBO[] l_extensions = getExtensionsByName(a_name);
        int len = CTools.getSize(l_extensions);
        for (int i = 0; i < len; i++) {
            GB_BOExtensionBO l_extension = l_extensions[i];
            removeExtension(l_extension);
        }
    }

    public GB_BOEnumItemBO[] getBoEnumItemArray() {
        GB_BOEnumValuesBO l_enumValues = getBoEnumValues();
        if (l_enumValues == null) {
            return new GB_BOEnumItemBO[0];
        }
        return l_enumValues.getBoEnumItemArray();
    }

    public GB_BOMetaDataBO getBoMetaDataParent() {
        return (GB_BOMetaDataBO) getBoParent();
    }

    public Element getConfigNode() {
        return null;
    }

    public String getDescription() {
        return getDescription(AA.NAME_DEFAULT, LNG.DEFAULT, true);
    }

    public String getDescription(String a_name, String a_lng, boolean a_useDefault) {
        GB_BOStringMBO l_str = getDescriptionM(a_name);
        if (l_str == null) {
            return null;
        }
        return l_str.getString(a_lng, a_useDefault);
    }

    public final String[] getDEscriptionMNames() {
        return GB_MapTools.getStringKeys(getDescriptionMMap(), true, true);
    }

    public int getEnumItemCount() {
        GB_BOEnumValuesBO l_enumValues = getBoEnumValues();
        if (l_enumValues == null) {
            return 0;
        }
        return l_enumValues.getBoEnumItemCount();
    }

    public GB_BOEnumItem[] getEnumItems() {
        GB_BOEnumValuesBO l_enumValues = getBoEnumValues();
        if (l_enumValues == null) {
            return null;
        }
        return l_enumValues.getBoEnumItemArray();
    }

    public GB_BOEnumValues getEnumValues() {
        return getBoEnumValues();
    }

    public GB_BOExtensionBO getExtensionByName(String a_name) {
        GB_BOExtensionBO[] l_extensions = getExtensionArray();
        return (GB_BOExtensionBO) GB_NameOwnerTools.getFirstWithName(l_extensions, a_name);
    }

    public GB_BOExtensionBO getExtensionByNameNotNull(String a_name) {
        GB_BOExtensionBO retour = getExtensionByName(a_name);
        if (retour == null) {
            retour = new GB_BOExtensionBO(a_name);
            this.addExtension(retour);
        }
        return retour;
    }

    public GB_BOExtensionBO[] getExtensionsByName(String a_name) {
        GB_BOExtensionBO[] l_extensions = getExtensionArray();
        return (GB_BOExtensionBO[]) GB_NameOwnerTools.getFilterByName(l_extensions, a_name, new GB_BOExtensionBO[0]);
    }

    public GB_BOExtensionBO getFirstExtensionByName(String a_name) {
        return (GB_BOExtensionBO) GB_NameOwnerTools.getFirstWithName(getExtensionArray(), a_name);
    }

    public Icon getIcon() {
        return getIcon(AA.NAME_DEFAULT);
    }

    public String getId() {
        return getName();
    }

    public String getLabel() {
        return getLabel(AA.NAME_DEFAULT, LNG.DEFAULT, true);
    }

    public String getLabel(String a_name, String a_lng, boolean a_useDefault) {
        return GB_BOLabelsMOwnerTools.getLabel(this, a_name, a_lng, a_useDefault);
    }

    public GB_LabelIcon getLabelIcon() {
        String l_label = getLabel();
        String l_description = getDescription();
        Icon l_icon = getIcon();
        return GB_LabelIconTools.newLabelIcon(l_label, l_icon, l_description);
    }

    public String getLabelInfo() {
        return GB_BOPropertyBO.BO_NAME + " [" + getName() + "]";
    }

    public final String[] getLabelMNames() {
        return GB_MapTools.getStringKeys(getLabelMMap(), true, true);
    }

    public GB_BOProperty getPropertyBase() {
        return propertyOrigin;
    }

    public String getPropertyId() {
        GB_BOMetaDataBO l_parent = getBoMetaDataParent();
        String retour = "";
        if (l_parent != null) {
            retour = l_parent.getId() + ".";
        }
        retour += this.getName();
        return retour;
    }

    public boolean isStringM() {
        return GB_BOStringMBO.BO_NAME.equals(getBoType());
    }

    public void setDescription(String a_name, String a_lng, String a_value) {
        GB_BOStringMBO l_str = getDescriptionM(a_name);
        if (l_str == null) {
            l_str = new GB_BOStringMBO();
            putDescriptionM(a_name, l_str);
        }
        l_str.setPropertyValue(a_lng, a_value);
    }

    public void setLabel(String a_name, String a_lng, String a_value) {
        GB_BOLabelsMOwnerTools.setLabel(this, a_name, a_lng, a_value);
    }

    public void setPropertyOrigin(GB_BOProperty a_propertyOrigin) {
        propertyOrigin = a_propertyOrigin;
    }

    public void sortEnumItemsByIndex() {
        GB_BOEnumValuesBO l_enumValues = getBoEnumValues();
        if (l_enumValues == null) {
            return;
        }
        l_enumValues.sortEnumItemsByIndex();
    }
}
