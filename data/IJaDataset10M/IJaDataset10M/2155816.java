package ch.bbv.dynamicproperties.objecteditor;

import ch.bbv.dynamicproperties.core.ComplexType;
import ch.bbv.dynamicproperties.core.ListType;
import ch.bbv.dynamicproperties.core.SimpleType;
import ch.bbv.explorer.GeneralExplorerTree;
import ch.bbv.explorer.IconFactory;
import ch.bbv.explorer.IconSelector;
import ch.bbv.explorer.PopupSelector;
import ch.bbv.explorer.SimpleIconSelector;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.UiNodeTypeFactory;

public class DynamicTypeTree extends GeneralExplorerTree {

    public DynamicTypeTree(UiNodeTypeFactory factory) {
        super(factory);
    }

    /**
     * {@inheritDoc}
     */
    protected IconSelector getIconSelector(IconFactory factory, Object clazz) {
        if (clazz instanceof TreeElement) {
            return null;
        }
        if (clazz instanceof SimpleType) {
            return new SimpleIconSelector("simpletype");
        }
        if (clazz instanceof ComplexType) {
            return new SimpleIconSelector("complextype");
        }
        if (clazz instanceof ListType) {
            return new SimpleIconSelector("listtype");
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    protected PopupSelector getPopupSelector(UiNodeType nodeType, Object clazz) {
        return new PropertyPopupSelector();
    }
}
