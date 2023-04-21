package com.tensegrity.palobrowser.editors.subseteditor.flat;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.palo.api.Attribute;
import org.palo.api.Element;
import org.palo.api.ElementNode;
import com.tensegrity.palobrowser.dbtree.DbTreeLabelProvider;

/**
 * <code></code>
 *
 * @author Stepan Rutz
 * @version $ID$
 */
public class FlatListLabelProvider extends LabelProvider implements ITableLabelProvider {

    Attribute alias;

    public FlatListLabelProvider(Attribute alias) {
        this.alias = alias;
    }

    public Attribute getAlias() {
        return alias;
    }

    public void setAlias(Attribute alias) {
        this.alias = alias;
    }

    public String getColumnText(Object obj, int index) {
        if (!(obj instanceof FlatListEntry)) return obj.toString();
        FlatListEntry entry = (FlatListEntry) obj;
        Element element = entry.getElement();
        if (alias != null) {
            Object attrVal = element.getAttributeValue(alias);
            return attrVal.toString();
        } else {
            return element.getName();
        }
    }

    public Image getColumnImage(Object obj, int index) {
        if (!(obj instanceof FlatListEntry)) return null;
        FlatListEntry entry = (FlatListEntry) obj;
        Element element = entry.getElement();
        if (index == 0) return DbTreeLabelProvider.getElementImage(element.getType());
        return null;
    }

    public Image getImage(Object obj) {
        return null;
    }
}
