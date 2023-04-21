package com.google.gdt.eclipse.designer.gxt.model.property;

import org.eclipse.wb.internal.core.utils.reflect.ReflectionUtils;

/**
 * Wrapper for <code>Margins</code>.
 * 
 * @author scheglov_ke
 * @coverage ExtGWT.model.property
 */
public class Margins {

    public int top;

    public int left;

    public int bottom;

    public int right;

    public Margins() {
    }

    public Margins(Object margins) {
        top = ReflectionUtils.getFieldInt(margins, "top");
        left = ReflectionUtils.getFieldInt(margins, "left");
        bottom = ReflectionUtils.getFieldInt(margins, "bottom");
        right = ReflectionUtils.getFieldInt(margins, "right");
    }

    public static boolean isMargins(Object value) {
        return ReflectionUtils.isSuccessorOf(value, "com.extjs.gxt.ui.client.util.Margins");
    }
}
