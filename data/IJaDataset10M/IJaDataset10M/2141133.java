package com.goodcodeisbeautiful.archtea.web.form;

import com.goodcodeisbeautiful.archtea.search.FoundItem;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FoundItem4XmlBeanTest extends TestCase {

    public static Test suite() {
        return new TestSuite(FoundItem4XmlBeanTest.class);
    }

    private static final String SAMPLE_TEXT = "\r\n\t" + (char) 0x1b;

    private static final String RESULT_TEXT = "\r\n\t?";

    FoundItem m_item;

    FoundItem4XmlBean m_bean;

    protected void setUp() throws Exception {
        super.setUp();
        m_item = new DummyFoundItem(SAMPLE_TEXT + "1", SAMPLE_TEXT + "2", SAMPLE_TEXT + "3", SAMPLE_TEXT + "4", SAMPLE_TEXT + "5", SAMPLE_TEXT + "6");
        m_bean = new FoundItem4XmlBean(m_item);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetIndexNumber() {
        assertEquals(RESULT_TEXT + "1", m_bean.getIndexNumber());
    }

    public void testGetTitle() {
        assertEquals(RESULT_TEXT + "2", m_bean.getTitle());
    }

    public void testGetSummary() {
        assertEquals(RESULT_TEXT + "3", m_bean.getSummary());
    }

    public void testGetUrl() {
        assertEquals(RESULT_TEXT + "4", m_bean.getUrl());
    }

    public void testGetSummarizedUrl() {
        assertEquals(RESULT_TEXT + "5", m_bean.getSummarizedUrl());
    }

    public void testGetSize() {
        assertEquals(RESULT_TEXT + "6", m_bean.getSize());
    }
}
