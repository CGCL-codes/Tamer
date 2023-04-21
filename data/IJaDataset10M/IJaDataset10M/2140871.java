package com.android.globalsearch;

import junit.framework.TestCase;
import junit.framework.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.content.ComponentName;
import android.test.suitebuilder.annotation.SmallTest;
import com.google.android.collect.Lists;

/**
 * Contains tests for logic in {@link SessionManager}
 *
 * TODO: refactor out hard coded 'promotableWhenInsufficientRankingInfo' list in session manager
 * to make these tests less brittle.
 */
@SmallTest
public class SessionManagerTest extends TestCase {

    static final ComponentName WEB = new ComponentName("com.example", "com.example.WEB");

    static final ComponentName B = new ComponentName("com.android.contacts", "com.example.B");

    static final ComponentName C = new ComponentName("com.android.contacts", "com.example.C");

    static final ComponentName D = new ComponentName("com.android.contacts", "com.example.D");

    static final ComponentName E = new ComponentName("com.android.contacts", "com.example.E");

    static final ComponentName F = new ComponentName("com.android.contacts", "com.example.F");

    private SimpleSourceLookup mSourceLookup;

    private ArrayList<SuggestionSource> mAllSuggestionSources;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mAllSuggestionSources = Lists.newArrayList(makeSource(B), makeSource(C), makeSource(D), makeSource(E), makeSource(F));
        mSourceLookup = new SimpleSourceLookup(mAllSuggestionSources, makeSource(WEB));
    }

    private SuggestionSource makeSource(ComponentName componentName) {
        return new TestSuggestionSource.Builder().setComponent(componentName).create();
    }

    public void testOrderSources_onlyIncludeEnabled() {
        SessionManager.Sources sources1 = SessionManager.orderSources(Lists.newArrayList(makeSource(B)), mSourceLookup, Lists.newArrayList(C, D, WEB), 3);
        assertContentsInOrder("should only include enabled source, even if the ranking includes more.", sources1.mPromotableSources, makeSource(WEB), makeSource(B));
        SessionManager.Sources sources2 = SessionManager.orderSources(Lists.newArrayList(makeSource(B)), mSourceLookup, Lists.newArrayList(C, B, WEB), 3);
        assertContentsInOrder("should only include enabled source, even if the ranking includes more.", sources2.mPromotableSources, makeSource(WEB), makeSource(B));
    }

    public void testOrderSources_webAlwaysFirst() {
        SessionManager.Sources sources = SessionManager.orderSources(mAllSuggestionSources, mSourceLookup, Lists.newArrayList(C, D, WEB), 3);
        assertContentsInOrder("web source should be first even if its stats are worse.", sources.mPromotableSources, makeSource(WEB), makeSource(C), makeSource(D), makeSource(B), makeSource(E), makeSource(F));
    }

    public void testOrderSources_unRankedAfterPromoted() {
        SessionManager.Sources sources = SessionManager.orderSources(mAllSuggestionSources, mSourceLookup, Lists.newArrayList(C, D, WEB, B), 3);
        assertContentsInOrder("unranked sources should be ordered after the ranked sources in the promoted " + "slots.", sources.mPromotableSources, makeSource(WEB), makeSource(C), makeSource(D), makeSource(E), makeSource(F), makeSource(B));
    }

    static void assertContentsInOrder(Iterable<?> actual, Object... expected) {
        assertContentsInOrder(null, actual, expected);
    }

    /**
     * an implementation of {@link android.test.MoreAsserts#assertContentsInOrder}
     * that isn't busted.  a bug has been filed about that, but for now this works.
     */
    static void assertContentsInOrder(String message, Iterable<?> actual, Object... expected) {
        ArrayList actualList = new ArrayList();
        for (Object o : actual) {
            actualList.add(o);
        }
        Assert.assertEquals(message, Arrays.asList(expected), actualList);
    }
}
