package com.google.gdt.eclipse.designer.model.property.css;

import org.eclipse.wb.internal.css.semantics.SimpleSidedProperty;

/**
 * Property for {@link SimpleSidedProperty} with color values.
 * 
 * @author scheglov_ke
 * @coverage gwt.model.property
 */
final class StyleColorValueProperty extends StyleSimpleValueProperty {

    public StyleColorValueProperty(RuleAccessor accessor, String styleName, String valueObjectPath, String title) {
        super(accessor, styleName, valueObjectPath, title, ColorPropertyEditor.INSTANCE);
    }
}
