package org.neurpheus.nlp.morphology.baseimpl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import org.neurpheus.nlp.morphology.tagset.GrammaticalCategory;
import org.neurpheus.nlp.morphology.tagset.GrammaticalProperties;
import org.neurpheus.nlp.morphology.tagset.GrammaticalTag;

/**
 *
 * @author jstrychowski
 */
public class TagsetImplTest extends TestCase {

    public TagsetImplTest(String testName) {
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

    /**
     * Test of getGrammaticalCategories method, of class TagsetImpl.
     */
    public void testGetSetGrammaticalCategories() {
        System.out.println("getGrammaticalCategories");
        TagsetImpl tagset = new TagsetImpl();
        assertTrue(tagset.getGrammaticalCategories().size() == 0);
        ArrayList categories = new ArrayList();
        GrammaticalCategory gc1 = createGrammaticalCategory("1");
        GrammaticalCategory gc2 = createGrammaticalCategory("2");
        GrammaticalCategory gc3 = createGrammaticalCategory("3");
        categories.add(gc1);
        categories.add(gc2);
        categories.add(gc3);
        tagset.setGrammaticalCategories(categories);
        Collection result = tagset.getGrammaticalCategories();
        assertTrue(result.size() == 3);
        assertTrue(result.contains(gc1));
        assertTrue(result.contains(gc2));
        assertTrue(result.contains(gc3));
        GrammaticalCategory gc4 = createGrammaticalCategory("4");
        categories.add(gc4);
        tagset.setGrammaticalCategories(categories);
        result = tagset.getGrammaticalCategories();
        assertTrue(result.size() == 4);
        assertTrue(result.contains(gc1));
        assertTrue(result.contains(gc2));
        assertTrue(result.contains(gc3));
        assertTrue(result.contains(gc4));
        categories.remove(gc2);
        categories.remove(gc3);
        tagset.setGrammaticalCategories(categories);
        result = tagset.getGrammaticalCategories();
        assertTrue(result.size() == 2);
        assertTrue(result.contains(gc1));
        assertTrue(result.contains(gc4));
        try {
            tagset.setGrammaticalCategories(null);
            fail("The NullPointerException should be thrown.");
        } catch (NullPointerException e) {
        }
    }

    /**
     * Test of getGrammaticalCategory method, of class TagsetImpl.
     */
    public void testGetGrammaticalCategory() {
        System.out.println("getGrammaticalCategory");
        TagsetImpl tagset = new TagsetImpl();
        ArrayList categories = new ArrayList();
        GrammaticalCategory gc1 = createGrammaticalCategory("1");
        GrammaticalCategory gc2 = createGrammaticalCategory("2");
        GrammaticalCategory gc3 = createGrammaticalCategory("3");
        categories.add(gc1);
        categories.add(gc2);
        categories.add(gc3);
        tagset.setGrammaticalCategories(categories);
        assertEquals(gc1, tagset.getGrammaticalCategory("category_1"));
        assertEquals(gc2, tagset.getGrammaticalCategory("category_2"));
        assertEquals(gc3, tagset.getGrammaticalCategory("category_3"));
        try {
            tagset.getGrammaticalCategory(null);
            fail("The NullPointerException should be thrown.");
        } catch (NullPointerException e) {
        }
        try {
            tagset.getGrammaticalCategory("");
            fail("The IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test of addGrammaticalCategory method, of class TagsetImpl.
     */
    public void testAddGrammaticalCategory() {
        System.out.println("addGrammaticalCategory");
        TagsetImpl tagset = new TagsetImpl();
        assertTrue(tagset.getGrammaticalCategories().size() == 0);
        GrammaticalCategory gc1 = createGrammaticalCategory("1");
        GrammaticalCategory gc2 = createGrammaticalCategory("2");
        GrammaticalCategory gc3 = createGrammaticalCategory("3");
        tagset.addGrammaticalCategory(gc1);
        Collection result = tagset.getGrammaticalCategories();
        assertTrue(result.size() == 1);
        assertTrue(result.contains(gc1));
        tagset.addGrammaticalCategory(gc2);
        result = tagset.getGrammaticalCategories();
        assertTrue(result.size() == 2);
        assertTrue(result.contains(gc1));
        assertTrue(result.contains(gc2));
        tagset.addGrammaticalCategory(gc3);
        result = tagset.getGrammaticalCategories();
        assertTrue(result.size() == 3);
        assertTrue(result.contains(gc1));
        assertTrue(result.contains(gc2));
        assertTrue(result.contains(gc3));
        try {
            tagset.addGrammaticalCategory(null);
            fail("The NullPointerException should be thrown.");
        } catch (NullPointerException e) {
        }
    }

    /**
     * Test of getTags method, of class TagsetImpl.
     */
    public void testGetTags() {
        System.out.println("getTags");
        TagsetImpl tagset = new TagsetImpl();
        ArrayList categories = new ArrayList();
        GrammaticalCategory gc1 = createGrammaticalCategory("1");
        GrammaticalCategory gc2 = createGrammaticalCategory("2");
        GrammaticalCategory gc3 = createGrammaticalCategory("3");
        categories.add(gc1);
        categories.add(gc2);
        tagset.setGrammaticalCategories(categories);
        Collection tags = tagset.getTags();
        assertEquals(6, tags.size());
        assertTrue(tags.contains(createGrammaticalTag("1.1")));
        assertTrue(tags.contains(createGrammaticalTag("1.2")));
        assertTrue(tags.contains(createGrammaticalTag("1.3")));
        assertTrue(tags.contains(createGrammaticalTag("2.1")));
        assertTrue(tags.contains(createGrammaticalTag("2.2")));
        assertTrue(tags.contains(createGrammaticalTag("2.3")));
        assertFalse(tags.contains(createGrammaticalTag("3.1")));
        assertFalse(tags.contains(createGrammaticalTag("3.2")));
        assertFalse(tags.contains(createGrammaticalTag("3.3")));
        tagset.addGrammaticalCategory(gc3);
        tags = tagset.getTags();
        assertEquals(9, tags.size());
        assertTrue(tags.contains(createGrammaticalTag("1.1")));
        assertTrue(tags.contains(createGrammaticalTag("1.2")));
        assertTrue(tags.contains(createGrammaticalTag("1.3")));
        assertTrue(tags.contains(createGrammaticalTag("2.1")));
        assertTrue(tags.contains(createGrammaticalTag("2.2")));
        assertTrue(tags.contains(createGrammaticalTag("2.3")));
        assertTrue(tags.contains(createGrammaticalTag("3.1")));
        assertTrue(tags.contains(createGrammaticalTag("3.2")));
        assertTrue(tags.contains(createGrammaticalTag("3.3")));
    }

    /**
     * Test of getTag method, of class TagsetImpl.
     */
    public void testGetTag() {
        System.out.println("getTag");
        TagsetImpl tagset = new TagsetImpl();
        ArrayList categories = new ArrayList();
        GrammaticalCategory gc1 = createGrammaticalCategory("1");
        GrammaticalCategory gc2 = createGrammaticalCategory("2");
        GrammaticalCategory gc3 = createGrammaticalCategory("3");
        categories.add(gc1);
        categories.add(gc2);
        tagset.setGrammaticalCategories(categories);
        assertEquals(createGrammaticalTag("1.1"), tagset.getTag("tag_1.1"));
        assertEquals(createGrammaticalTag("1.2"), tagset.getTag("tag_1.2"));
        assertEquals(createGrammaticalTag("1.3"), tagset.getTag("tag_1.3"));
        assertEquals(createGrammaticalTag("2.1"), tagset.getTag("tag_2.1"));
        assertEquals(createGrammaticalTag("2.2"), tagset.getTag("tag_2.2"));
        assertEquals(createGrammaticalTag("2.3"), tagset.getTag("tag_2.3"));
        assertNull(tagset.getTag("tag_3.1"));
        assertNull(tagset.getTag("tag_3.2"));
        assertNull(tagset.getTag("tag_3.3"));
        tagset.addGrammaticalCategory(gc3);
        assertEquals(createGrammaticalTag("1.1"), tagset.getTag("tag_1.1"));
        assertEquals(createGrammaticalTag("1.2"), tagset.getTag("tag_1.2"));
        assertEquals(createGrammaticalTag("1.3"), tagset.getTag("tag_1.3"));
        assertEquals(createGrammaticalTag("2.1"), tagset.getTag("tag_2.1"));
        assertEquals(createGrammaticalTag("2.2"), tagset.getTag("tag_2.2"));
        assertEquals(createGrammaticalTag("2.3"), tagset.getTag("tag_2.3"));
        assertEquals(createGrammaticalTag("3.1"), tagset.getTag("tag_3.1"));
        assertEquals(createGrammaticalTag("3.2"), tagset.getTag("tag_3.2"));
        assertEquals(createGrammaticalTag("3.3"), tagset.getTag("tag_3.3"));
        try {
            tagset.getTag(null);
            fail("The NullPointerException should be thrown.");
        } catch (NullPointerException e) {
        }
        try {
            tagset.getTag("");
            fail("The IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test of readFromXML method, of class TagsetImpl.
     */
    public void testXML() throws Exception {
        System.out.println("readFromXML, writeToXML");
        TagsetImpl tagset = new TagsetImpl();
        ArrayList categories = new ArrayList();
        GrammaticalCategory gc1 = createGrammaticalCategory("1");
        GrammaticalCategory gc2 = createGrammaticalCategory("2");
        GrammaticalCategory gc3 = createGrammaticalCategory("3");
        categories.add(gc1);
        categories.add(gc2);
        categories.add(gc3);
        tagset.setGrammaticalCategories(categories);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tagset.writeAsXML(baos);
        byte[] bytes = baos.toByteArray();
        baos.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        tagset = new TagsetImpl();
        tagset.readFromXML(bais);
        bais.close();
        Collection result = tagset.getGrammaticalCategories();
        assertTrue(result.size() == 3);
        assertTrue(result.contains(gc1));
        assertTrue(result.contains(gc2));
        assertTrue(result.contains(gc3));
        assertEquals(gc1, tagset.getGrammaticalCategory("category_1"));
        assertEquals(gc2, tagset.getGrammaticalCategory("category_2"));
        assertEquals(gc3, tagset.getGrammaticalCategory("category_3"));
        assertEquals(createGrammaticalTag("1.1"), tagset.getTag("tag_1.1"));
        assertEquals(createGrammaticalTag("1.2"), tagset.getTag("tag_1.2"));
        assertEquals(createGrammaticalTag("1.3"), tagset.getTag("tag_1.3"));
        assertEquals(createGrammaticalTag("2.1"), tagset.getTag("tag_2.1"));
        assertEquals(createGrammaticalTag("2.2"), tagset.getTag("tag_2.2"));
        assertEquals(createGrammaticalTag("2.3"), tagset.getTag("tag_2.3"));
        assertEquals(createGrammaticalTag("3.1"), tagset.getTag("tag_3.1"));
        assertEquals(createGrammaticalTag("3.2"), tagset.getTag("tag_3.2"));
        assertEquals(createGrammaticalTag("3.3"), tagset.getTag("tag_3.3"));
    }

    public void testGetGrammaticalProperties() throws Exception {
        System.out.println("getGrammaticalProperties");
        TagsetImpl tagset = new TagsetImpl();
        ArrayList categories = new ArrayList();
        GrammaticalCategory gc1 = createGrammaticalCategory("1");
        GrammaticalCategory gc2 = createGrammaticalCategory("2");
        GrammaticalCategory gc3 = createGrammaticalCategory("3");
        categories.add(gc1);
        categories.add(gc2);
        categories.add(gc3);
        tagset.setGrammaticalCategories(categories);
        GrammaticalProperties gp1a = tagset.getGrammaticalProperties("tag_1.1:tag_2.1");
        gp1a.hasTag("tag_1.1");
        gp1a.hasTag("tag_2.1");
        GrammaticalProperties gp2 = tagset.getGrammaticalProperties("tag_3.1:tag_3.2:tag_3.3");
        gp2.hasTag("tag_1.1");
        gp2.hasTag("tag_3.2");
        GrammaticalProperties gp1b = tagset.getGrammaticalProperties("tag_1.1:tag_2.1");
        assertTrue(gp1a == gp1b);
    }

    public void testPolishTagset() throws Exception {
        TagsetImpl tagset = new TagsetImpl();
        String path = "N:\\data\\polish.tags.xml";
        InputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        tagset.readFromXML(in);
        in.close();
    }
}
