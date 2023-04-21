package com.loribel.commons.business.impl.old;

import java.util.Collection;
import java.util.Iterator;
import javax.swing.Icon;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.business.abstraction.GB_BOMetaData;
import com.loribel.commons.business.abstraction.GB_BOProperty;
import com.loribel.commons.business.impl.GB_BONodeAbstract;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.xml.GB_ElementTools;

abstract class GB_BOMetaDataNode2 extends GB_BONodeAbstract implements GB_BOMetaData {

    /**
     * Property names of this Business Object.
     */
    public final class NODE_NAME {

        public static final String BO_PROPERTY = "boProperty";

        public static final String DESCRIPTION = "descriptionM";

        public static final String FLAG_NOT_USE = "flagNotUse";

        public static final String FLAG_PERSISTENT = "flagPersistent";

        public static final String FLAG_READ_ONLY = "flagReadOnly";

        public static final String FLAG_VISIBLE = "flagVisible";

        public static final String HELP_FILENAME = "helpFilename";

        public static final String ICON = "icon";

        public static final String ID = "id";

        public static final String LABEL = "labelM";
    }

    private GB_BOProperty[] boProperties;

    private String description;

    private boolean flagNotUse;

    private boolean flagPersistent;

    private boolean flagReadOnly;

    private boolean flagVisible;

    private String helpFilename;

    private Icon icon;

    private String id;

    private String label;

    public GB_BOMetaDataNode2(Node a_node) {
        this(a_node, null);
    }

    public GB_BOMetaDataNode2(Node a_node, String a_lng) {
        super(a_node, a_lng);
        init();
    }

    /**
     *
     */
    public GB_BOProperty[] getBOProperties() {
        if (boProperties == null) {
            Collection l_nodePropertyList = GB_ElementTools.getChildElementsByName(node, NODE_NAME.BO_PROPERTY, 1);
            if (l_nodePropertyList == null) {
                return null;
            }
            int len = CTools.getSize(l_nodePropertyList);
            boProperties = new GB_BOProperty[len];
            GB_BOProperty l_property = null;
            Node l_nodeProperty = null;
            int l_index = 0;
            Iterator it = l_nodePropertyList.iterator();
            while (it.hasNext()) {
                l_nodeProperty = (Node) it.next();
                l_property = new GB_BOPropertyNode2(l_nodeProperty);
                boProperties[l_index] = l_property;
                l_index++;
            }
        }
        return boProperties;
    }

    public Element getConfigNode() {
        return getNode();
    }

    public String getDescription() {
        if (description == null) {
            Node l_node = GB_ElementTools.getFirstChildElement(getNode(), NODE_NAME.DESCRIPTION, 1);
            description = getI18n(l_node);
            if (description == null) {
                description = "";
            }
        }
        return description;
    }

    public String getDescription(String a_name) {
        return description;
    }

    public String getDescription(String a_name, String a_lng) {
        return null;
    }

    public final String getHelpFilename() {
        return helpFilename;
    }

    public final Icon getIcon() {
        return icon;
    }

    public Icon getIcon(String a_name) {
        return icon;
    }

    public final String getId() {
        return id;
    }

    public String getLabel() {
        if (label == null) {
            Node l_node = GB_ElementTools.getFirstChildElement(getNode(), NODE_NAME.LABEL, 1);
            label = getI18n(l_node);
            if (label == null) {
                label = "";
            }
        }
        return label;
    }

    public String getLabel(String a_name) {
        return label;
    }

    public String getLabel(String a_name, String a_lng) {
        return null;
    }

    public GB_LabelIcon getLabelIcon() {
        String l_label = getLabel();
        String l_description = getDescription();
        Icon l_icon = getIcon();
        return GB_LabelIconTools.newLabelIcon(l_label, l_icon, l_description);
    }

    private void init() {
        this.initId();
        this.initVisible();
        this.initReadOnly();
        this.initPersistent();
        this.initHelpFilename();
        this.initNotUse();
        this.initIcon();
    }

    protected void initHelpFilename() {
        helpFilename = (String) getNodeValue(NODE_NAME.HELP_FILENAME, String.class);
    }

    protected void initIcon() {
        icon = (Icon) getNodeValue(NODE_NAME.ICON, Icon.class);
    }

    protected void initId() {
        id = getAttributeValue(NODE_NAME.ID);
    }

    protected void initNotUse() {
        String l_valueStr = node.getAttribute(NODE_NAME.FLAG_NOT_USE);
        if ((l_valueStr == null) || (l_valueStr.length() == 0)) {
            flagNotUse = false;
        } else {
            flagNotUse = (l_valueStr.equals("true"));
        }
    }

    protected void initPersistent() {
        String l_valueStr = node.getAttribute(NODE_NAME.FLAG_PERSISTENT);
        if ((l_valueStr == null) || (l_valueStr.length() == 0)) {
            flagPersistent = true;
        } else {
            flagPersistent = (l_valueStr.equals("true"));
        }
    }

    protected void initReadOnly() {
        String l_valueStr = node.getAttribute(NODE_NAME.FLAG_READ_ONLY);
        if ((l_valueStr == null) || (l_valueStr.length() == 0)) {
            flagReadOnly = false;
        } else {
            flagReadOnly = (l_valueStr.equals("true"));
        }
    }

    protected void initVisible() {
        String l_valueStr = node.getAttribute(NODE_NAME.FLAG_VISIBLE);
        if ((l_valueStr == null) || (l_valueStr.length() == 0)) {
            flagVisible = true;
        } else {
            flagVisible = (l_valueStr.equals("true"));
        }
    }

    public final boolean isNotUse() {
        return flagNotUse;
    }

    public final boolean isPersistent() {
        return flagPersistent;
    }

    public final boolean isReadOnly() {
        return flagReadOnly;
    }

    public final boolean isVisible() {
        return flagVisible;
    }

    public void setDescription(String a_description) {
        description = a_description;
    }

    public void setHelpFilename(String a_helpFilename) {
        helpFilename = a_helpFilename;
    }

    public void setIcon(Icon a_icon) {
        icon = a_icon;
    }

    public void setId(String a_id) {
        id = a_id;
    }

    public void setLabel(String a_label) {
        label = a_label;
    }

    public void setNotUse(boolean a_flagNotUse) {
        flagNotUse = a_flagNotUse;
    }

    public void setPersistent(boolean a_flagPersistent) {
        flagPersistent = a_flagPersistent;
    }

    public void setReadOnly(boolean a_flagReadOnly) {
        flagReadOnly = a_flagReadOnly;
    }

    public void setVisible(boolean a_flagVisible) {
        flagVisible = a_flagVisible;
    }
}
