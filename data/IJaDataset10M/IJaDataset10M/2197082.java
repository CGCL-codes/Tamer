package com.loribel.commons.module.selector;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.loribel.commons.abstraction.GB_BooleanSelector;
import com.loribel.commons.abstraction.GB_DateSelector;
import com.loribel.commons.abstraction.GB_EnumIntSelector;
import com.loribel.commons.abstraction.GB_EnumStringSelector;
import com.loribel.commons.abstraction.GB_NumberSelector;
import com.loribel.commons.abstraction.GB_Selector;
import com.loribel.commons.abstraction.GB_StringSelector;
import com.loribel.commons.exception.GB_VisitorException;
import com.loribel.commons.module.selector.convertor.GB_BooleanSelectorStringConvertor;
import com.loribel.commons.module.selector.convertor.GB_DateSelectorStringConvertor;
import com.loribel.commons.module.selector.convertor.GB_EnumIntSelectorStringConvertor;
import com.loribel.commons.module.selector.convertor.GB_EnumStringSelectorStringConvertor;
import com.loribel.commons.module.selector.convertor.GB_NumberSelectorStringConvertor;
import com.loribel.commons.module.selector.convertor.GB_StringSelectorStringConvertor;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_NumberTools;
import com.loribel.commons.util.Log;
import com.loribel.commons.util.convertor.GB_StringConvertors;

/**
 * Tools to use GB_Selector.
 *
 * @author Gregory Borelli
 */
public final class GB_SelectorTools {

    /** 
     * <ul>
     *   <li>key [Class]: the type (String, Number, Integer, int, ...)</li>
     *   <li>value [Class]: type of Selector (interface)</li>
     * </ul>
     */
    private static Map selectorByType;

    /**
     * Retourne le type de selector pour un type donn�.
     * Retourne toujours un type interface.
     */
    public static Class getSelectorType(Class a_type) {
        if (selectorByType == null) {
            initSelectorByType();
        }
        return (Class) selectorByType.get(a_type);
    }

    public static synchronized void initAll() {
        initStringConvertors();
        initSelectorByType();
    }

    private static void initSelectorByType() {
        if (selectorByType != null) {
            return;
        }
        selectorByType = new HashMap();
        selectorByType.put(String.class, GB_StringSelector.class);
        selectorByType.put(Date.class, GB_DateSelector.class);
        selectorByType.put(Number.class, GB_NumberSelector.class);
        Class[] l_types = GB_NumberTools.getTypes();
        int len = CTools.getSize(l_types);
        for (int i = 0; i < len; i++) {
            Class l_type = l_types[i];
            selectorByType.put(l_type, GB_NumberSelector.class);
        }
    }

    private static void initStringConvertors() {
        GB_StringConvertors l_convertors = GB_StringConvertors.getInstance();
        l_convertors.addConvertor(GB_BooleanSelector.class, new GB_BooleanSelectorStringConvertor());
        l_convertors.addConvertor(GB_DateSelector.class, new GB_DateSelectorStringConvertor());
        l_convertors.addConvertor(GB_StringSelector.class, new GB_StringSelectorStringConvertor());
        l_convertors.addConvertor(GB_NumberSelector.class, new GB_NumberSelectorStringConvertor());
        l_convertors.addConvertor(GB_EnumStringSelector.class, new GB_EnumStringSelectorStringConvertor());
        l_convertors.addConvertor(GB_EnumIntSelector.class, new GB_EnumIntSelectorStringConvertor());
    }

    /**
     * Returns true if type is a subclass of GB_Selector.
     */
    public static boolean isSelector(Class a_type) {
        if (a_type == null) {
            return false;
        }
        if (GB_Selector.class.isAssignableFrom(a_type)) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the value match with selector.
     */
    public static boolean match(GB_Selector a_selector, Object a_value) {
        GB_SelectorVisitorMatch l_matchVisitor = new GB_SelectorVisitorMatch(a_value);
        Boolean r;
        try {
            r = (Boolean) a_selector.accept(l_matchVisitor);
        } catch (GB_VisitorException ex) {
            Log.logWarning(GB_SelectorTools.class, ex);
            return false;
        }
        return r.booleanValue();
    }

    private GB_SelectorTools() {
    }
}
