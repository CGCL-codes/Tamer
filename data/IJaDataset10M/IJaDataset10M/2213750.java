package org.tranche.annotation.annotation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.tranche.annotation.ALexTestCase;
import org.tranche.annotation.category.Category;
import org.tranche.annotation.category.CategoryCache;
import org.tranche.annotation.category.CategoryUtil;
import org.tranche.annotation.field.FieldUtil;
import org.tranche.annotation.fieldvalue.FieldValue;
import org.tranche.annotation.fieldvalue.FieldValueCache;
import org.tranche.annotation.fieldvalue.FieldValueUtil;
import org.tranche.annotation.standard.Standard;
import org.tranche.annotation.standard.StandardCache;
import org.tranche.annotation.standard.StandardUtil;
import org.tranche.commons.DebugUtil;

/**
 *
 * @author James "Augie" Hill - augman85@gmail.com
 */
public class CategoryInstanceTest extends ALexTestCase {

    @Override()
    protected void setUp() throws Exception {
        super.setUp();
        DebugUtil.setDebug(CategoryInstance.class, true);
    }

    @Override()
    protected void tearDown() throws Exception {
        super.tearDown();
        DebugUtil.setDebug(CategoryInstance.class, false);
    }

    public void verifyEquality(CategoryInstance ci, int annotationID, int categoryID, int categoryInstance, int parentCategoryInstance, Map<Integer, List<FieldValue>> map) throws Exception {
        if (ci == null) {
            throw new Exception("Category Instance is null.");
        }
        assertEquals(annotationID, ci.getAnnotationID());
        assertEquals(annotationID, ci.getAnnotation().getPrimaryKey());
        assertEquals(categoryID, ci.getCategoryID());
        assertEquals(categoryID, ci.getCategory().getPrimaryKey());
        assertEquals(categoryInstance, ci.getPrimaryKey());
        assertEquals(parentCategoryInstance, ci.getParentPrimaryKey());
        assertEquals(map, ci.getFieldValuesMap());
    }

    public void testCreate() throws Exception {
        printTitle("CategoryInstanceTest.testCreate()");
        Standard standard = StandardCache.get(StandardUtil.create("name", "description", "version"));
        Category category = CategoryCache.get(CategoryUtil.create(standard, "cat name", "cat desc", "cat conc summ", 0, false));
        int fieldID = FieldUtil.create(category, "field name", "field description", "cde", 0, false, false, false, "int");
        standard.finalizeStandard();
        Annotation annotation = AnnotationCache.get(AnnotationUtil.create(standard, "name", "hash", true));
        int categoryInstance = 0;
        int parentCategoryInstance = CategoryInstance.DEFAULT_PARENT_CATEGORY_INSTANCE_ID;
        Map<Integer, List<FieldValue>> map = new HashMap<Integer, List<FieldValue>>();
        CategoryInstance ci = new CategoryInstance(annotation.getPrimaryKey(), category.getPrimaryKey(), categoryInstance, parentCategoryInstance, map);
        verifyEquality(ci, annotation.getPrimaryKey(), category.getPrimaryKey(), categoryInstance, CategoryInstance.DEFAULT_PARENT_CATEGORY_INSTANCE_ID, map);
    }

    public void testCreateSubCategoryInstances() throws Exception {
        printTitle("CategoryInstanceTest.testCreateSubCategoryInstances()");
        Standard standard = StandardCache.get(StandardUtil.create("name", "description", "version"));
        Category category = CategoryCache.get(CategoryUtil.create(standard, "cat name", "cat desc", "cat conc summ", 0, false));
        Category subcategory = CategoryCache.get(CategoryUtil.create(standard, "cat name", "cat desc", "cat conc summ", 0, false));
        category.addSubCategory(subcategory);
        int fieldID = FieldUtil.create(category, "field name", "field description", "cde", 0, false, false, false, "int");
        standard.finalizeStandard();
        Annotation annotation = AnnotationCache.get(AnnotationUtil.create(standard, "name", "hash", true));
        CategoryInstance ci = new CategoryInstance(annotation.getPrimaryKey(), category.getPrimaryKey(), 0, CategoryInstance.DEFAULT_PARENT_CATEGORY_INSTANCE_ID, new HashMap<Integer, List<FieldValue>>());
        CategoryInstance subci = new CategoryInstance(annotation.getPrimaryKey(), subcategory.getPrimaryKey(), 0, 0, new HashMap<Integer, List<FieldValue>>());
        assertEquals(1, ci.getInstances().size());
        assertEquals(0, ci.getInstances(subcategory.getPrimaryKey()).size());
        assertEquals(0, ci.getInstances().get(subcategory.getPrimaryKey()).size());
        ci.addInstance(subci);
        assertEquals(1, ci.getInstances().size());
        assertEquals(subci, ci.getInstance(subcategory.getPrimaryKey(), 0));
        assertEquals(1, ci.getInstances(subcategory.getPrimaryKey()).size());
        assertEquals(1, ci.getInstances().get(subcategory.getPrimaryKey()).size());
        ci.removeInstance(subci);
        assertEquals(1, ci.getInstances().size());
        assertEquals(0, ci.getInstances(subcategory.getPrimaryKey()).size());
        assertEquals(0, ci.getInstances().get(subcategory.getPrimaryKey()).size());
        assertEquals(null, ci.getInstances(-1));
        assertEquals(null, ci.getInstance(subcategory.getPrimaryKey(), -1));
    }

    public void testScore() throws Exception {
        printTitle("CategoryInstanceTest.testScore()");
        Standard standard = StandardCache.get(StandardUtil.create("name", "description", "version"));
        Category category = CategoryCache.get(CategoryUtil.create(standard, "cat name", "cat desc", "cat conc summ", 0, false));
        Category subcategory = CategoryCache.get(CategoryUtil.create(standard, "cat name", "cat desc", "cat conc summ", 0, false));
        category.addSubCategory(subcategory);
        int fieldID = FieldUtil.create(category, "field name", "field description", "cde", 0, false, false, false, "int");
        int fieldID2 = FieldUtil.create(subcategory, "field name", "field description", "cde", 0, false, false, false, "int");
        standard.finalizeStandard();
        Annotation annotation = AnnotationCache.get(AnnotationUtil.create(standard, "name", "hash", true));
        CategoryInstance ci = new CategoryInstance(annotation.getPrimaryKey(), category.getPrimaryKey(), 0, CategoryInstance.DEFAULT_PARENT_CATEGORY_INSTANCE_ID, new HashMap<Integer, List<FieldValue>>());
        CategoryInstance subci = new CategoryInstance(annotation.getPrimaryKey(), subcategory.getPrimaryKey(), 0, 0, new HashMap<Integer, List<FieldValue>>());
        ci.addInstance(subci);
        assertEquals(0.0, ci.getScore());
        assertEquals(0.0, ci.getScore(subcategory.getPrimaryKey()));
        assertEquals(0.0, ci.getScore(-1));
        FieldValue fieldValue = FieldValueCache.get(FieldValueUtil.create(annotation, fieldID2, "1", 0, 0));
        Map<Integer, List<FieldValue>> map = new HashMap<Integer, List<FieldValue>>();
        List<FieldValue> list = new LinkedList<FieldValue>();
        list.add(fieldValue);
        map.put(fieldID2, list);
        subci.setFieldValuesMap(map);
        assertEquals(1.0, ci.getScore(subcategory.getPrimaryKey()));
        assertEquals(0.5, ci.getScore());
    }

    public void testSetFieldValueMap() throws Exception {
        printTitle("CategoryInstanceTest.testSetFieldValueMap()");
        Standard standard = StandardCache.get(StandardUtil.create("name", "description", "version"));
        Category category = CategoryCache.get(CategoryUtil.create(standard, "cat name", "cat desc", "cat conc summ", 0, false));
        int fieldID = FieldUtil.create(category, "field name", "field description", "cde", 0, false, false, false, "int");
        standard.finalizeStandard();
        Annotation annotation = AnnotationCache.get(AnnotationUtil.create(standard, "name", "hash", true));
        int categoryInstance = 0;
        int parentCategoryInstance = CategoryInstance.DEFAULT_PARENT_CATEGORY_INSTANCE_ID;
        Map<Integer, List<FieldValue>> map = new HashMap<Integer, List<FieldValue>>();
        CategoryInstance ci = new CategoryInstance(annotation.getPrimaryKey(), category.getPrimaryKey(), categoryInstance, parentCategoryInstance, map);
        map = new HashMap<Integer, List<FieldValue>>();
        ci.setFieldValuesMap(map);
        verifyEquality(ci, annotation.getPrimaryKey(), category.getPrimaryKey(), categoryInstance, parentCategoryInstance, map);
        boolean exceptionThrown = false;
        try {
            ci.setFieldValuesMap(null);
        } catch (Exception e) {
            exceptionThrown = true;
        }
        if (!exceptionThrown) {
            fail("No exception thrown.");
        }
    }

    public void testCreateWithNull() throws Exception {
        printTitle("CategoryInstanceTest.testCreateWithNull()");
        boolean exceptionThrown = false;
        try {
            new CategoryInstance(0, 0, 0, 0, null);
        } catch (Exception e) {
            exceptionThrown = true;
        }
        if (!exceptionThrown) {
            fail("Exception not thrown.");
        }
    }
}
