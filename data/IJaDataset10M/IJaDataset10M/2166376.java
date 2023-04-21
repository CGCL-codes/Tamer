package org.newsml.toolkit.dom.unittests;

import junit.framework.TestCase;
import org.newsml.toolkit.SubjectCodeItem;

public class SubjectCodeItemTest extends TestCase {

    public SubjectCodeItemTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        sample = SampleFactory.createNewsML().getNewsItem(0).getRootNewsComponent().getDescriptiveMetadata().getSubjectCode(1).getItem(1);
        sample2 = SampleFactory.createNewsML().getNewsItem(0).getRootNewsComponent().getDescriptiveMetadata().getSubjectCode(1).getItem(3);
        sample3 = SampleFactory.createNewsML().getNewsItem(0).getRootNewsComponent().getDescriptiveMetadata().getSubjectCode(1).getItem(5);
    }

    public void testSubject() {
        assertNotNull(sample.getSubject());
        assertNull(sample.getSubjectMatter());
        assertNull(sample.getSubjectDetail());
    }

    public void testSubjectMatter() {
        assertNull(sample2.getSubject());
        assertNotNull(sample2.getSubjectMatter());
        assertNull(sample2.getSubjectDetail());
    }

    public void testSubjectDetail() {
        assertNull(sample3.getSubject());
        assertNull(sample3.getSubjectMatter());
        assertNotNull(sample3.getSubjectDetail());
    }

    public void testQualifier() {
        assertEquals(sample.getSubjectQualifierCount(), 2);
        assertEquals(sample.getSubjectQualifier().length, 2);
        assertNotNull(sample.getSubjectQualifier(0));
        assertNotNull(sample.getSubjectQualifier(1));
        assertNull(sample.getSubjectQualifier(2));
        assertEquals(sample.getSubjectQualifier(0).getName().toString(), "qualifier.a");
        assertEquals(sample.getSubjectQualifier(1).getName().toString(), "qualifier.b");
    }

    private SubjectCodeItem sample, sample2, sample3;
}
