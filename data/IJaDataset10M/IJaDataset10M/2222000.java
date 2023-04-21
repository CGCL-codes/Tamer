package com.tensegrity.palobrowser.editors.dimensioneditor;

import java.util.List;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.palo.api.Element;
import com.tensegrity.palobrowser.editors.dimensioneditor.attr.IAttributeCubeWrapper;
import com.tensegrity.palobrowser.editors.dimensioneditor.attr.IAttributeDataWrapper;
import com.tensegrity.palobrowser.editors.dimensioneditor.attr.IAttributeFieldWrapper;
import com.tensegrity.palobrowser.editors.dimensioneditor.wrapper.ProxyElementWrapper;
import com.tensegrity.palobrowser.managedlist.ManagedList;

/**
 * <code>ElementWrapperLabelProvider</code>
 * 
 * @author Stepan Rutz
 * @author Andreas Ebbert
 * @version $Id$
 */
class ElementWrapperLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {

    private final DimensionEditor dimensionEditor;

    private final DimensionEditorElementTab elementTab;

    private final TableViewer tableViewer;

    ElementWrapperLabelProvider(DimensionEditor editor, DimensionEditorElementTab elementTab, TableViewer tableViewer) {
        this.dimensionEditor = editor;
        this.elementTab = elementTab;
        this.tableViewer = tableViewer;
    }

    public void dispose() {
        super.dispose();
    }

    public String getColumnText(Object obj, int index) {
        if (!(obj instanceof ProxyElementWrapper)) return obj.toString();
        ProxyElementWrapper element = (ProxyElementWrapper) obj;
        switch(index) {
            case 0:
                Object input = tableViewer.getInput();
                if (!(input instanceof ManagedList)) return "";
                final int indexInList = dimensionEditor.getElementTab().getElementIndexOfObject(obj);
                return Integer.toString(indexInList);
            case 1:
                return getElementNameConsideringAttributes(element);
            case 2:
                String elementTypeName = element.getTypeAsString();
                if (elementTypeName.equals(Element.ELEMENTTYPE_NUMERIC_STRING)) {
                    return DimensionEditorMessages.getString("DimensionEditorElem.NumericTypeName");
                } else if (elementTypeName.equals(Element.ELEMENTTYPE_STRING_STRING)) {
                    return DimensionEditorMessages.getString("DimensionEditorElem.StringTypeName");
                } else if (elementTypeName.equals(Element.ELEMENTTYPE_CONSOLIDATED_STRING)) {
                    return DimensionEditorMessages.getString("DimensionEditorElem.ConsolidatedTypeName");
                } else if (elementTypeName.equals(Element.ELEMENTTYPE_RULE_STRING)) {
                    return DimensionEditorMessages.getString("DimensionEditorElem.RuleTypeName");
                }
                return element.getTypeAsString();
        }
        return obj.toString();
    }

    public Image getColumnImage(Object obj, int index) {
        return null;
    }

    public Image getImage(Object obj) {
        return null;
    }

    public Color getBackground(Object element, int index) {
        return null;
    }

    public Color getForeground(Object element, int index) {
        switch(index) {
            case 0:
                return DimensionEditor.disabledBgColor;
        }
        return null;
    }

    private String getElementNameConsideringAttributes(ProxyElementWrapper elWr) {
        if (elementTab.getAttributeDimensionName() == null) {
            String name = elWr.getName();
            if (elWr.isDirty()) name += DimensionEditor.POSTFIX_UNSAVED;
            return name;
        } else {
            final IAttributeCubeWrapper acw = ((DimensionEditorAttributeTab) dimensionEditor.getAttributeTab()).attrCubeWrapper;
            final IAttributeFieldWrapper adw = acw.getAttrField(elementTab.getAttributeDimensionName());
            final int indexInList = dimensionEditor.getElementTab().getElementIndexOfObject(elWr);
            final List adew = adw.getDataWrappers();
            if (indexInList < adew.size()) return ((IAttributeDataWrapper) adew.get(indexInList)).getName();
            return "";
        }
    }
}
