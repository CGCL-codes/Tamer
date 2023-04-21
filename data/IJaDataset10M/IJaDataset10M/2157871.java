package com.loribel.commons.module.selector;

import java.util.ArrayList;
import java.util.List;
import com.loribel.commons.abstraction.GB_SelectorTestCase;
import com.loribel.commons.abstraction.GB_StringSelector;
import com.loribel.commons.abstraction.GB_StringSelectorSet;
import com.loribel.commons.module.selector.impl.GB_StringSelectorImpl;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.impl.GB_SelectorTestCaseImpl;

/**
 * Tools.
 *
 * @author Gregory Borelli
 */
public final class GB_StringSelectorTestTools {

    private static List getInverses(List a_selectorTestCases) {
        List retour = new ArrayList();
        int len = CTools.getSize(a_selectorTestCases);
        for (int i = 0; i < len; i++) {
            GB_SelectorTestCaseImpl l_item = (GB_SelectorTestCaseImpl) a_selectorTestCases.get(i);
            GB_StringSelectorSet l_selector = GB_StringSelectorTools.getInverse((GB_StringSelector) l_item.getSelector());
            GB_SelectorTestCaseImpl l_newItem = new GB_SelectorTestCaseImpl(l_selector);
            l_newItem.addValues(l_item.getValidValues(), false);
            l_newItem.addValues(l_item.getInvalidValues(), true);
            retour.add(l_newItem);
        }
        return retour;
    }

    public static GB_SelectorTestCase[] getTestCases(boolean a_useInverse) {
        List retour = new ArrayList();
        GB_SelectorTestCaseImpl l_selectorTestCase;
        GB_StringSelectorSet l_selector;
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.CONTAINS, "abc", true);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", true);
        l_selectorTestCase.addValue("ABC", true);
        l_selectorTestCase.addValue("abc toto", true);
        l_selectorTestCase.addValue("toto abc", true);
        l_selectorTestCase.addValue("toto abccc", true);
        l_selectorTestCase.addValue("totoaBctoto", true);
        l_selectorTestCase.addValue("totoAbAbtoto", false);
        l_selectorTestCase.addValue("", false);
        l_selectorTestCase.addValue(null, false);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.CONTAINS, "abc", false);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", true);
        l_selectorTestCase.addValue("ABC", false);
        l_selectorTestCase.addValue("abc toto", true);
        l_selectorTestCase.addValue("toto abc", true);
        l_selectorTestCase.addValue("toto abccc", true);
        l_selectorTestCase.addValue("totoaBctoto", false);
        l_selectorTestCase.addValue("totoAbAbtoto", false);
        l_selectorTestCase.addValue("", false);
        l_selectorTestCase.addValue(null, false);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.NOT_CONTAINS, "abc", true);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", false);
        l_selectorTestCase.addValue("ABC", false);
        l_selectorTestCase.addValue("abc toto", false);
        l_selectorTestCase.addValue("toto abc", false);
        l_selectorTestCase.addValue("toto abccc", false);
        l_selectorTestCase.addValue("totoaBctoto", false);
        l_selectorTestCase.addValue("totoAbAbtoto", true);
        l_selectorTestCase.addValue("", true);
        l_selectorTestCase.addValue(null, true);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.NOT_CONTAINS, "abc", false);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", false);
        l_selectorTestCase.addValue("ABC", true);
        l_selectorTestCase.addValue("abc toto", false);
        l_selectorTestCase.addValue("toto abc", false);
        l_selectorTestCase.addValue("toto abccc", false);
        l_selectorTestCase.addValue("totoaBctoto", true);
        l_selectorTestCase.addValue("totoAbAbtoto", true);
        l_selectorTestCase.addValue("", true);
        l_selectorTestCase.addValue(null, true);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.STARTS, "abc", true);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", true);
        l_selectorTestCase.addValue("ABC", true);
        l_selectorTestCase.addValue("abc toto", true);
        l_selectorTestCase.addValue("toto abc", false);
        l_selectorTestCase.addValue("toto abccc", false);
        l_selectorTestCase.addValue("totoaBctoto", false);
        l_selectorTestCase.addValue("totoAbAbtoto", false);
        l_selectorTestCase.addValue("", false);
        l_selectorTestCase.addValue(null, false);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.STARTS, "abc", false);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", true);
        l_selectorTestCase.addValue("ABC", false);
        l_selectorTestCase.addValue("abc toto", true);
        l_selectorTestCase.addValue("toto abc", false);
        l_selectorTestCase.addValue("toto abccc", false);
        l_selectorTestCase.addValue("totoaBctoto", false);
        l_selectorTestCase.addValue("totoAbAbtoto", false);
        l_selectorTestCase.addValue("", false);
        l_selectorTestCase.addValue(null, false);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.ENDS, "abc", true);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", true);
        l_selectorTestCase.addValue("ABC", true);
        l_selectorTestCase.addValue("abc toto", false);
        l_selectorTestCase.addValue("toto abc", true);
        l_selectorTestCase.addValue("toto abccc", false);
        l_selectorTestCase.addValue("totoaBctoto", false);
        l_selectorTestCase.addValue("totoAbAbtoto", false);
        l_selectorTestCase.addValue("", false);
        l_selectorTestCase.addValue(null, false);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.ENDS, "abc", false);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", true);
        l_selectorTestCase.addValue("ABC", false);
        l_selectorTestCase.addValue("abc toto", false);
        l_selectorTestCase.addValue("toto abc", true);
        l_selectorTestCase.addValue("toto abccc", false);
        l_selectorTestCase.addValue("totoaBctoto", false);
        l_selectorTestCase.addValue("totoAbAbtoto", false);
        l_selectorTestCase.addValue("", false);
        l_selectorTestCase.addValue(null, false);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.EMPTY);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", false);
        l_selectorTestCase.addValue("ABC", false);
        l_selectorTestCase.addValue("abc toto", false);
        l_selectorTestCase.addValue("toto abc", false);
        l_selectorTestCase.addValue("toto abccc", false);
        l_selectorTestCase.addValue("totoaBctoto", false);
        l_selectorTestCase.addValue("totoAbAbtoto", false);
        l_selectorTestCase.addValue("", true);
        l_selectorTestCase.addValue(null, true);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.NOT_EMPTY);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", true);
        l_selectorTestCase.addValue("ABC", true);
        l_selectorTestCase.addValue("abc toto", true);
        l_selectorTestCase.addValue("toto abc", true);
        l_selectorTestCase.addValue("toto abccc", true);
        l_selectorTestCase.addValue("totoaBctoto", true);
        l_selectorTestCase.addValue("totoAbAbtoto", true);
        l_selectorTestCase.addValue("", false);
        l_selectorTestCase.addValue(null, false);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.WILDCARDS, "ab?", true);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", true);
        l_selectorTestCase.addValue("ABC", true);
        l_selectorTestCase.addValue("abc toto", false);
        l_selectorTestCase.addValue("toto abc", false);
        l_selectorTestCase.addValue("toto abccc", false);
        l_selectorTestCase.addValue("totoaBctoto", false);
        l_selectorTestCase.addValue("totoAbAbtoto", false);
        l_selectorTestCase.addValue("", false);
        l_selectorTestCase.addValue(null, false);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.WILDCARDS, "ab?", false);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", true);
        l_selectorTestCase.addValue("ABC", false);
        l_selectorTestCase.addValue("abc toto", false);
        l_selectorTestCase.addValue("toto abc", false);
        l_selectorTestCase.addValue("toto abccc", false);
        l_selectorTestCase.addValue("totoaBctoto", false);
        l_selectorTestCase.addValue("totoAbAbtoto", false);
        l_selectorTestCase.addValue("", false);
        l_selectorTestCase.addValue(null, false);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.WILDCARDS, "ab*c", true);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("abc", true);
        l_selectorTestCase.addValue("ABC", true);
        l_selectorTestCase.addValue("abc totoc", true);
        l_selectorTestCase.addValue("toto abc", false);
        l_selectorTestCase.addValue("abtoto abccc", true);
        l_selectorTestCase.addValue("totoaBctoto", false);
        l_selectorTestCase.addValue("totoAbAbtoto", false);
        l_selectorTestCase.addValue("", false);
        l_selectorTestCase.addValue(null, false);
        l_selector = new GB_StringSelectorImpl(GB_StringSelector.TYPE.CONTAINS, "���", true);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue("���", true);
        l_selectorTestCase.addValue("���", true);
        l_selectorTestCase.addValue("abc ���oc", true);
        l_selectorTestCase.addValue("toto ���", true);
        l_selectorTestCase.addValue("abtoto ���cc", true);
        l_selectorTestCase.addValue("toto���toto", true);
        l_selectorTestCase.addValue("toto��btoto", false);
        l_selectorTestCase.addValue("", false);
        l_selectorTestCase.addValue(null, false);
        if (a_useInverse) {
            List l_inverseList = getInverses(retour);
            retour.addAll(l_inverseList);
        }
        return (GB_SelectorTestCase[]) retour.toArray(new GB_SelectorTestCase[retour.size()]);
    }

    private GB_StringSelectorTestTools() {
    }
}
