package com.apelon.beans.dts.subset.builder.item;

import com.apelon.beans.dts.plugin.DTSAppManager;
import com.apelon.beans.dts.uielements.ColorContents;
import com.apelon.beans.dts.uielements.IconProvider;
import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.attribute.DTSPropertyType;
import com.apelon.dts.client.namespace.Namespace;
import com.apelon.dts.common.subset.expression.DefinedItem;
import com.apelon.dts.common.subset.expression.PropertyFilter;
import java.awt.*;

/**
 * Description
 * <p/>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Apelon, Inc.</p>
 *
 * @author Apelon Inc.
 * @version DTS 3.4.0
 * @since 3.4.0
 */
public class SEPropertyFilter extends SEAttributeFilter {

    public SEPropertyFilter() {
    }

    public SEPropertyFilter(DTSPropertyType propertyType, String value) {
        this(propertyType, value, -1);
    }

    public SEPropertyFilter(DTSPropertyType propertyType, String value, int namespaceId) {
        setFilter(new PropertyFilter());
        if (propertyType != null) {
            DefinedItem attr = new DefinedItem(propertyType.getId(), propertyType.getName(), propertyType.getNamespaceId());
            try {
                Namespace ns = DTSAppManager.getQuery().getNamespaceQuery().findNamespaceById(propertyType.getNamespaceId());
                attr.setNamespaceName(ns.getName());
            } catch (DTSException dte) {
                attr.setNamespaceName("");
            }
            filter.setAttribute(attr);
        }
        filter.setValue(value);
        if (value == null) {
            filter.setOperator(PropertyFilter.OPERATOR_NONE);
        } else {
            filter.setOperator(PropertyFilter.OPERATOR_MATCHING);
        }
        filter.setNamespaceId(namespaceId);
    }

    public String getModifierText() {
        return filter.getModifier() + " PROPERTY";
    }

    public String getAttributeText() {
        DefinedItem attribute = (DefinedItem) filter.getAttribute();
        if (attribute.getNamespaceId() == getNamespaceId()) {
            return attribute.getName();
        } else {
            return attribute.getName() + " [" + attribute.getNamespaceName() + "]";
        }
    }

    public int getIconIndex() {
        return IconProvider.PROPERTY_IDX;
    }

    public Color getDisplayTextColor() {
        return ColorContents.PROPERTIES_COLOR;
    }
}
