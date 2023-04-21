package com.loribel.commons.module.selector;

import java.util.ArrayList;
import java.util.List;
import com.loribel.commons.abstraction.GB_NumberSelector;
import com.loribel.commons.abstraction.GB_NumberSelectorSet;
import com.loribel.commons.abstraction.GB_SelectorTestCase;
import com.loribel.commons.module.selector.impl.GB_NumberSelectorImpl;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.impl.GB_SelectorTestCaseImpl;

/**
 * Tools.
 *
 * @author Gregory Borelli
 */
public final class GB_NumberSelectorTestTools {

    private static GB_SelectorTestCase getInverse(GB_SelectorTestCaseImpl a_selectorTestCase) {
        GB_NumberSelectorSet l_selector = GB_NumberSelectorTools.getInverse((GB_NumberSelector) a_selectorTestCase.getSelector());
        GB_SelectorTestCaseImpl retour = new GB_SelectorTestCaseImpl(l_selector);
        retour.addValues(a_selectorTestCase.getValidValues(), false);
        retour.addValues(a_selectorTestCase.getInvalidValues(), true);
        return retour;
    }

    private static List getInverses(List a_selectorTestCases) {
        List retour = new ArrayList();
        int len = CTools.getSize(a_selectorTestCases);
        for (int i = 0; i < len; i++) {
            GB_SelectorTestCaseImpl l_item = (GB_SelectorTestCaseImpl) a_selectorTestCases.get(i);
            GB_SelectorTestCase l_newItem = getInverse(l_item);
            retour.add(l_newItem);
        }
        return retour;
    }

    public static GB_SelectorTestCase[] getTestCases(boolean a_useInverse) {
        List retour = new ArrayList();
        GB_SelectorTestCaseImpl l_selectorTestCase;
        GB_NumberSelectorSet l_selector;
        l_selector = new GB_NumberSelectorImpl(GB_NumberSelector.TYPE.EQUALS, 10);
        l_selectorTestCase = new GB_SelectorTestCaseImpl(l_selector);
        retour.add(l_selectorTestCase);
        l_selectorTestCase.addValue(new Double(10), true);
        l_selectorTestCase.addValue(new Float(10), true);
        l_selectorTestCase.addValue(new Integer(10), true);
        l_selectorTestCase.addValue(new Long(10), true);
        l_selectorTestCase.addValue(new Double(12), false);
        l_selectorTestCase.addValue(new Float(158), false);
        l_selectorTestCase.addValue(new Integer(7), false);
        l_selectorTestCase.addValue(new Long(-5), false);
        if (a_useInverse) {
            List l_inverseList = getInverses(retour);
            retour.addAll(l_inverseList);
        }
        return (GB_SelectorTestCase[]) retour.toArray(new GB_SelectorTestCase[retour.size()]);
    }

    private GB_NumberSelectorTestTools() {
    }
}
