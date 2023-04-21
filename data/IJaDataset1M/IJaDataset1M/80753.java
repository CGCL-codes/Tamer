package org.neurpheus.nlp.morphology.baseimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import junit.framework.TestCase;
import org.neurpheus.nlp.morphology.tagset.GrammaticalCategory;
import org.neurpheus.nlp.morphology.tagset.GrammaticalTag;
import org.neurpheus.nlp.morphology.tagset.Tagset;

/**
 *
 * @author jstrychowski
 */
public class GrammaticalPropertiesImplTest extends TestCase {

    public GrammaticalPropertiesImplTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private GrammaticalTag createGrammaticalTag(final String prefix) {
        GrammaticalTag tag = new GrammaticalTagImpl("tag_" + prefix);
        tag.setName("pl", "polish_name_of_tag_" + prefix);
        tag.setName("en", "english_name_of_tag_" + prefix);
        tag.setDescription("pl", "polish_description_of_tag_" + prefix);
        tag.setDescription("en", "english_description_of_tag_" + prefix);
        String[] examples = new String[] { "first_polish_example_of_tag_" + prefix, "second_polish_example_of_tag_" + prefix };
        tag.setExamples("pl", examples);
        examples = new String[] { "first_english_example_of_tag_" + prefix, "second_english_example_of_tag_" + prefix };
        tag.setExamples("en", examples);
        return tag;
    }

    private GrammaticalCategory createGrammaticalCategory(final String prefix) {
        GrammaticalCategory category = new GrammaticalCategoryImpl("category_" + prefix);
        category.setName("pl", "polish_name_of_category_" + prefix);
        category.setName("en", "english_name_of_category_" + prefix);
        category.setDescription("pl", "polish_description_of_category_" + prefix);
        category.setDescription("en", "english_description_of_category_" + prefix);
        category.addTag(createGrammaticalTag(prefix + ".1"));
        category.addTag(createGrammaticalTag(prefix + ".2"));
        category.addTag(createGrammaticalTag(prefix + ".3"));
        return category;
    }

    private Tagset createTagset() {
        TagsetImpl tagset = new TagsetImpl();
        ArrayList categories = new ArrayList();
        GrammaticalCategory gc1 = createGrammaticalCategory("1");
        GrammaticalCategory gc2 = createGrammaticalCategory("2");
        GrammaticalCategory gc3 = createGrammaticalCategory("3");
        categories.add(gc1);
        categories.add(gc2);
        categories.add(gc3);
        tagset.setGrammaticalCategories(categories);
        return tagset;
    }

    /**
     * Test of setTagsFromMark method, of class GrammaticalPropertiesImpl.
     */
    public void testSetTagsFromMark() {
        System.out.println("setTagsFromMark");
        GrammaticalPropertiesImpl instance = new GrammaticalPropertiesImpl();
        Tagset tagset = createTagset();
        instance.setTagset(tagset);
        String mark = "tag_1.1:tag_3.2:tag_2.1";
        instance.setMark(mark);
        Collection tags = instance.getTags();
        assertEquals(3, tags.size());
        assertTrue(tags.contains(tagset.getTag("tag_1.1")));
        assertTrue(tags.contains(tagset.getTag("tag_3.2")));
        assertTrue(tags.contains(tagset.getTag("tag_2.1")));
    }

    /**
     * Test of getMark method, of class GrammaticalPropertiesImpl.
     */
    public void testGetMark() {
        System.out.println("getMark");
        Tagset tagset = createTagset();
        String mark = "tag_1.1:tag_3.2:tag_2.1";
        GrammaticalPropertiesImpl instance = new GrammaticalPropertiesImpl(mark, tagset);
        assertEquals(mark, instance.getMark());
    }

    /**
     * Test of setMark method, of class GrammaticalPropertiesImpl.
     */
    public void testSetMark() {
        System.out.println("setMark");
        Tagset tagset = createTagset();
        String mark = "tag_1.1:tag_3.2:tag_2.1";
        GrammaticalPropertiesImpl instance = new GrammaticalPropertiesImpl(mark, tagset);
        assertEquals(mark, instance.getMark());
        mark = "tag_2.1:tag_2.2";
        instance.setMark(mark);
        Collection tags = instance.getTags();
        assertEquals(2, tags.size());
        assertTrue(tags.contains(tagset.getTag("tag_2.1")));
        assertTrue(tags.contains(tagset.getTag("tag_2.2")));
        try {
            instance.setMark(null);
            fail("The NullPointerException should be thrown.");
        } catch (NullPointerException e) {
        }
    }

    /**
     * Test of getTags method, of class GrammaticalPropertiesImpl.
     */
    public void testGetTags() {
        System.out.println("getTags");
        Tagset tagset = createTagset();
        String mark = "tag_1.1:tag_3.2:tag_2.1";
        GrammaticalPropertiesImpl instance = new GrammaticalPropertiesImpl(mark, tagset);
        Collection tags = instance.getTags();
        assertEquals(3, tags.size());
        assertTrue(tags.contains(tagset.getTag("tag_1.1")));
        assertTrue(tags.contains(tagset.getTag("tag_3.2")));
        assertTrue(tags.contains(tagset.getTag("tag_2.1")));
    }

    /**
     * Test of setTags method, of class GrammaticalPropertiesImpl.
     */
    public void testSetTags() {
        System.out.println("setTags");
        Tagset tagset = createTagset();
        String mark = "tag_1.1:tag_3.2:tag_2.1";
        GrammaticalPropertiesImpl instance = new GrammaticalPropertiesImpl(mark, tagset);
        ArrayList tags = new ArrayList();
        tags.add(tagset.getTag("tag_2.1"));
        tags.add(tagset.getTag("tag_3.1"));
        instance.setTags(tags);
        assertEquals("tag_2.1:tag_3.1", instance.getMark());
        assertTrue(instance.hasTag("tag_2.1"));
        assertTrue(instance.hasTag("tag_3.1"));
        assertEquals(2, instance.getTags().size());
        try {
            instance.setTags(null);
            fail("The NullPointerException should be thrown.");
        } catch (NullPointerException e) {
        }
    }

    /**
     * Test of hasTag method, of class GrammaticalPropertiesImpl.
     */
    public void testHasTag() {
        System.out.println("hasTag");
        Tagset tagset = createTagset();
        String mark = "tag_1.1:tag_3.2:tag_2.1";
        GrammaticalPropertiesImpl instance = new GrammaticalPropertiesImpl(mark, tagset);
        assertTrue(instance.hasTag("tag_1.1"));
        assertTrue(instance.hasTag("tag_3.2"));
        assertTrue(instance.hasTag("tag_2.1"));
        assertTrue(instance.hasTag(tagset.getTag("tag_1.1")));
        assertTrue(instance.hasTag(tagset.getTag("tag_3.2")));
        assertTrue(instance.hasTag(tagset.getTag("tag_2.1")));
    }

    /**
     * Test of getTagset method, of class GrammaticalPropertiesImpl.
     */
    public void testGetSetTagset() {
        System.out.println("getSetTagset");
        Tagset tagset = createTagset();
        Tagset tagset2 = createTagset();
        String mark = "tag_1.1:tag_3.2:tag_2.1";
        GrammaticalPropertiesImpl instance = new GrammaticalPropertiesImpl(mark, tagset);
        assertTrue(instance.getTagset() == tagset);
        instance.setTagset(tagset2);
        assertTrue(instance.getTagset() == tagset2);
        for (final Iterator it = instance.getTags().iterator(); it.hasNext(); ) {
            GrammaticalTag tag = (GrammaticalTag) it.next();
            assertTrue(tag == tagset2.getTag(tag.getSymbol()));
        }
    }
}
