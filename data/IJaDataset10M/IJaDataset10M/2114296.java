package org.t2framework.t2.contexts.impl;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import org.t2framework.commons.meta.MethodDesc;
import org.t2framework.commons.meta.impl.MethodDescImpl;
import org.t2framework.t2.annotation.core.ActionPath;
import org.t2framework.t2.annotation.core.Ajax;
import org.t2framework.t2.annotation.core.Default;
import org.t2framework.t2.contexts.internal.ActionInfo;
import org.t2framework.t2.contexts.internal.ActionInfoComparator;

public class ActionInfoComparatorTest extends TestCase {

    static Set<Class<? extends Annotation>> actionAnnotationSet = new HashSet<Class<? extends Annotation>>();

    static {
        actionAnnotationSet.add(Default.class);
        actionAnnotationSet.add(ActionPath.class);
        actionAnnotationSet.add(Ajax.class);
    }

    public void test1_normal() throws Exception {
        ActionInfoComparator comparator = new ActionInfoComparator();
        ActionInfo a1 = createActionInfo("hoge1_1");
        ActionInfo a2 = createActionInfo("hoge1_2");
        assertTrue(comparator.compare(a1, a2) == 0);
    }

    public void test2_oneHasVariable() throws Exception {
        ActionInfoComparator comparator = new ActionInfoComparator();
        ActionInfo a1 = createActionInfo("hoge2_1");
        ActionInfo a2 = createActionInfo("hoge2_2");
        int compare = comparator.compare(a1, a2);
        assertTrue(compare < 0);
    }

    public void test3_bothHasVariable() throws Exception {
        ActionInfoComparator comparator = new ActionInfoComparator();
        ActionInfo a1 = createActionInfo("hoge3_1");
        ActionInfo a2 = createActionInfo("hoge3_2");
        int compare = comparator.compare(a1, a2);
        assertTrue(0 < compare);
    }

    public void test4_bothHasVariableAndSameLength_soCheckAnnoationCounts() throws Exception {
        ActionInfoComparator comparator = new ActionInfoComparator();
        ActionInfo a1 = createActionInfo("hoge4_1");
        ActionInfo a2 = createActionInfo("hoge4_2");
        int compare = comparator.compare(a1, a2);
        assertTrue(0 < compare);
    }

    public void test5_removeVariablesThenCheckWithShorterOne() throws Exception {
        ActionInfoComparator comparator = new ActionInfoComparator();
        ActionInfo a1 = createActionInfo("hoge5_1");
        ActionInfo a2 = createActionInfo("hoge5_2");
        int compare = comparator.compare(a1, a2);
        assertTrue(0 < compare);
    }

    public void test6_removeVariablesThenCheckWithShorterOne() throws Exception {
        ActionInfoComparator comparator = new ActionInfoComparator();
        ActionInfo a1 = createActionInfo("hoge6_1");
        ActionInfo a2 = createActionInfo("hoge6_2");
        int compare = comparator.compare(a1, a2);
        assertTrue(0 < compare);
    }

    protected ActionInfo createActionInfo(String methodName) throws Exception {
        MethodDesc md = new MethodDescImpl(this.getClass().getDeclaredMethod(methodName));
        return new ActionInfo(md, actionAnnotationSet);
    }

    @Default
    @ActionPath
    public void hoge1_1() {
    }

    @Default
    @ActionPath
    public void hoge1_2() {
    }

    @Default
    @ActionPath("/hoge")
    public void hoge2_1() {
    }

    @Default
    @ActionPath("/{moge}")
    public void hoge2_2() {
    }

    @Default
    @ActionPath("/aaa{hoge}")
    public void hoge3_1() {
    }

    @Default
    @ActionPath("/bbb{moge}ccc")
    public void hoge3_2() {
    }

    @ActionPath("/aaa{hoge}")
    public void hoge4_1() {
    }

    @Ajax
    @ActionPath("/bbb{moge}")
    public void hoge4_2() {
    }

    @ActionPath("/aaa")
    public void hoge5_1() {
    }

    @Ajax
    @ActionPath("/bbb/{moge}")
    public void hoge5_2() {
    }

    @ActionPath("/aaa")
    public void hoge6_1() {
    }

    @Ajax
    @ActionPath("/bbb")
    public void hoge6_2() {
    }
}
