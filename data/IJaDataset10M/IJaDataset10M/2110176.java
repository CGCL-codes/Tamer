package com.jklas.search.indexer.pipeline;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.jklas.search.SearchEngine;
import com.jklas.search.annotations.IndexReference;
import com.jklas.search.annotations.IndexSelector;
import com.jklas.search.annotations.Indexable;
import com.jklas.search.annotations.IndexableContainer;
import com.jklas.search.annotations.LangId;
import com.jklas.search.annotations.LangSelector;
import com.jklas.search.annotations.SearchCollection;
import com.jklas.search.annotations.SearchContained;
import com.jklas.search.annotations.SearchField;
import com.jklas.search.annotations.SearchId;
import com.jklas.search.annotations.TextProcessor;
import com.jklas.search.configuration.AnnotationConfigurationMapper;
import com.jklas.search.configuration.MappedFieldDescriptor;
import com.jklas.search.configuration.SearchMapping;
import com.jklas.search.engine.processor.DefaultTextProcessor;
import com.jklas.search.engine.processor.NullProcessor;
import com.jklas.search.engine.processor.OneTermTextProcessor;
import com.jklas.search.exception.SearchEngineException;
import com.jklas.search.exception.SearchEngineMappingException;
import com.jklas.search.index.IndexId;
import com.jklas.search.util.Utils.SingleAttributeEntity;

public class AdvancedMappingTest {

    @Before
    public void before() {
        SearchEngine.getInstance().reset();
    }

    @SuppressWarnings("unused")
    @Indexable(makeSubclassesIndexable = false)
    private class SuperWithNoCascade {

        @SearchId
        public int id;
    }

    private class ChildWithNoCascadeParent extends SuperWithNoCascade {
    }

    @Test
    public void NoCascadeImpliesNoMapping() throws SearchEngineMappingException {
        ChildWithNoCascadeParent child = new ChildWithNoCascadeParent();
        AnnotationConfigurationMapper.configureAndMap(child);
        Assert.assertNull(SearchEngine.getInstance().getConfiguration().getMapping(child));
    }

    @SuppressWarnings("unused")
    @Indexable(makeSubclassesIndexable = true, indexName = "SUPER")
    @TextProcessor(OneTermTextProcessor.class)
    @LangId(value = "SUPER_LANG")
    private class SuperWithCascade {

        @SearchId
        public int id;
    }

    private class ChildWithCascadeParent extends SuperWithCascade {
    }

    @Test
    public void CascadeImpliesMapping() throws SearchEngineMappingException {
        ChildWithCascadeParent child = new ChildWithCascadeParent();
        AnnotationConfigurationMapper.configureAndMap(child);
        Assert.assertNotNull(SearchEngine.getInstance().getConfiguration().getMapping(child));
    }

    @Test
    public void ChildInheritsId() throws SearchEngineMappingException {
        ChildWithCascadeParent child = new ChildWithCascadeParent();
        AnnotationConfigurationMapper.configureAndMap(child);
        Assert.assertNotNull(SearchEngine.getInstance().getConfiguration().getMapping(child).getIdField());
    }

    @Test
    public void ChildInheritsIndexName() throws SearchEngineMappingException, SearchEngineException {
        ChildWithCascadeParent child = new ChildWithCascadeParent();
        AnnotationConfigurationMapper.configureAndMap(child);
        Assert.assertEquals(new IndexId("SUPER"), SearchEngine.getInstance().getConfiguration().getMapping(child).getIndexSelector().selectIndex(child));
    }

    @Test
    public void ChildInheritsTextProcessor() throws SearchEngineMappingException {
        ChildWithCascadeParent child = new ChildWithCascadeParent();
        AnnotationConfigurationMapper.configureAndMap(child);
        Assert.assertEquals(OneTermTextProcessor.class, SearchEngine.getInstance().getConfiguration().getMapping(child).getTextProcessor().getClass());
    }

    @Test
    public void ChildInheritsLangId() throws SearchEngineMappingException {
        ChildWithCascadeParent child = new ChildWithCascadeParent();
        AnnotationConfigurationMapper.configureAndMap(child);
        Assert.assertEquals("SUPER_LANG", SearchEngine.getInstance().getConfiguration().getMapping(child).getLanguage().getIdentifier());
    }

    @SuppressWarnings("unused")
    @LangId(value = "CHILD_LANG")
    @TextProcessor(NullProcessor.class)
    private class ChildWithOverrides extends SuperWithCascade {

        @SearchId
        public String childId;

        @IndexSelector
        public String anIndexId = "CHILD";
    }

    @Test
    public void ChildOverridesValues() throws SearchEngineMappingException, SearchEngineException {
        ChildWithOverrides child = new ChildWithOverrides();
        AnnotationConfigurationMapper.configureAndMap(child);
        SearchMapping mapping = SearchEngine.getInstance().getConfiguration().getMapping(child);
        Assert.assertEquals(ChildWithOverrides.class, mapping.getIdField().getDeclaringClass());
        Assert.assertEquals(NullProcessor.class, mapping.getTextProcessor().getClass());
        Assert.assertEquals("CHILD_LANG", mapping.getLanguage().getIdentifier());
        Assert.assertEquals("CHILD", mapping.getIndexSelector().selectIndex(child).getIndexName());
    }

    @SuppressWarnings("unused")
    @Indexable(makeSubclassesIndexable = true, indexName = "GRANDGRAND")
    @TextProcessor(NullProcessor.class)
    private class GrandGrandfather {

        @SearchId
        int id;

        @SearchField
        public String gf = "From grandgrandfather";
    }

    @SuppressWarnings("unused")
    @Indexable(makeSubclassesIndexable = true, indexName = "GRAND")
    @TextProcessor(DefaultTextProcessor.class)
    private class Grandfather extends GrandGrandfather {

        @SearchId
        int id = 0;

        @SearchField
        public String g = "From grandfather";
    }

    @SuppressWarnings("unused")
    private class Father extends Grandfather {

        @SearchId
        int id = 0;

        @SearchField
        public String f = "From father";
    }

    @SuppressWarnings("unused")
    @TextProcessor(OneTermTextProcessor.class)
    @LangId("LANG_SON")
    private class Son extends Father {

        @LangSelector
        public static final String lang = "SON_LANG";

        @SearchField
        public String s = "From son";
    }

    @Test
    public void CascadesIgnoresClassesBeyondTheFirstIndexable() throws SearchEngineMappingException, SearchEngineException {
        Son son = new Son();
        AnnotationConfigurationMapper.configureAndMap(son);
        SearchMapping mapping = SearchEngine.getInstance().getConfiguration().getMapping(son);
        Assert.assertEquals("GRAND", mapping.getIndexSelector().selectIndex(son).getIndexName());
        Assert.assertEquals(Father.class, mapping.getIdField().getDeclaringClass());
    }

    @Test
    public void CascadesOverridesOnMultipleLevels() throws SearchEngineMappingException, SearchEngineException, IllegalArgumentException, IllegalAccessException {
        Son son = new Son();
        AnnotationConfigurationMapper.configureAndMap(son);
        SearchMapping mapping = SearchEngine.getInstance().getConfiguration().getMapping(son);
        Assert.assertEquals(OneTermTextProcessor.class, mapping.getTextProcessor().getClass());
        Assert.assertEquals(Son.class, mapping.getLanguageSelectorField().getDeclaringClass());
        Field langField = mapping.getLanguageSelectorField();
        langField.setAccessible(true);
        Assert.assertEquals("SON_LANG", langField.get(son));
        Assert.assertEquals("LANG_SON", mapping.getLanguage().getIdentifier());
    }

    @SuppressWarnings("unused")
    private class NotIndexableWithId {

        @SearchId
        public int id = 1000;
    }

    @SuppressWarnings("unused")
    @Indexable(climbingTarget = NotIndexableWithId.class)
    private class IndexableWithInheritedId extends NotIndexableWithId {

        public String attribute = "something";
    }

    @Test
    public void IdIsInherited() throws SearchEngineMappingException, SearchEngineException, IllegalArgumentException, IllegalAccessException {
        IndexableWithInheritedId son = new IndexableWithInheritedId();
        AnnotationConfigurationMapper.configureAndMap(son);
        SearchMapping mapping = SearchEngine.getInstance().getConfiguration().getMapping(son);
        Field idField = mapping.getIdField();
        idField.setAccessible(true);
        Assert.assertTrue(1000 == (Integer) idField.get(son));
    }

    @SuppressWarnings("unused")
    private class GrandfatherWithClimbing extends NotIndexableWithId {

        @SearchId
        public int id = 1000;
    }

    @SuppressWarnings("unused")
    @Indexable(makeSubclassesIndexable = true, indexName = "GRAND", climbingTarget = Object.class)
    private class FatherWithClimbing extends GrandfatherWithClimbing {

        @SearchField
        public String f = "From father";
    }

    @SuppressWarnings("unused")
    private class SonWithClimbing extends FatherWithClimbing {

        @SearchField
        public String s = "From son";
    }

    @Test
    public void CascadeCombinedWithClimbing() throws SearchEngineMappingException, SearchEngineException, IllegalArgumentException, IllegalAccessException {
        SonWithClimbing son = new SonWithClimbing();
        AnnotationConfigurationMapper.configureAndMap(son);
        SearchMapping mapping = SearchEngine.getInstance().getConfiguration().getMapping(son);
        Field idField = mapping.getIdField();
        idField.setAccessible(true);
        Assert.assertTrue(1000 == (Integer) idField.get(son));
    }

    @Test
    public void PropertiesAreInheritedFromSuperclasses() throws SearchEngineMappingException, SearchEngineException, IllegalArgumentException, IllegalAccessException {
        Son son = new Son();
        AnnotationConfigurationMapper.configureAndMap(son);
        SearchMapping mapping = SearchEngine.getInstance().getConfiguration().getMapping(son);
        Assert.assertEquals(OneTermTextProcessor.class, mapping.getTextProcessor().getClass());
        Assert.assertEquals(Son.class, mapping.getLanguageSelectorField().getDeclaringClass());
        Field langField = mapping.getLanguageSelectorField();
        langField.setAccessible(true);
        Assert.assertEquals("SON_LANG", langField.get(son));
        Assert.assertEquals("LANG_SON", mapping.getLanguage().getIdentifier());
    }

    @Test
    public void EntireHierarchyIsMapped() throws SearchEngineMappingException, SearchEngineException, IllegalArgumentException, IllegalAccessException {
        Son son = new Son();
        AnnotationConfigurationMapper.configureAndMap(son);
        SearchMapping mapping = SearchEngine.getInstance().getConfiguration().getMapping(son);
        Map<Field, MappedFieldDescriptor> mappedFields = mapping.getMappedFields();
        Assert.assertEquals(6, mappedFields.size());
        int searchFieldCount = 0;
        for (Iterator<MappedFieldDescriptor> iterator = mappedFields.values().iterator(); iterator.hasNext(); ) {
            MappedFieldDescriptor mfd = (MappedFieldDescriptor) iterator.next();
            if (mfd.isSearchField()) searchFieldCount++;
        }
        Assert.assertEquals(3, searchFieldCount);
    }

    @IndexableContainer
    public class EmptyContainter {
    }

    @Test
    public void EmptyContainerIsMappedWithZeroEntries() throws SearchEngineMappingException, SearchEngineException, IllegalArgumentException, IllegalAccessException {
        EmptyContainter ic = new EmptyContainter();
        AnnotationConfigurationMapper.configureAndMap(ic);
        SearchMapping mapping = SearchEngine.getInstance().getConfiguration().getMapping(ic);
        Assert.assertNotNull(mapping);
        Map<Field, MappedFieldDescriptor> mappedFields = mapping.getMappedFields();
        Assert.assertEquals(0, mappedFields.size());
    }

    @IndexableContainer
    public class DummyContainter {

        @SearchCollection(reference = IndexReference.BOTH)
        List<Object> objectList1;

        @SearchCollection(reference = IndexReference.SELF)
        List<Object> objectList2;
    }

    @Test
    public void SearchCollectionsAreMapped() throws SearchEngineMappingException, SearchEngineException, IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
        DummyContainter dc = new DummyContainter();
        AnnotationConfigurationMapper.configureAndMap(dc);
        SearchMapping mapping = SearchEngine.getInstance().getConfiguration().getMapping(dc);
        Assert.assertNotNull(mapping);
        Map<Field, MappedFieldDescriptor> mappedFields = mapping.getMappedFields();
        Assert.assertEquals(2, mappedFields.size());
        for (Entry<Field, MappedFieldDescriptor> entries : mappedFields.entrySet()) {
            if (entries.getKey().equals(DummyContainter.class.getDeclaredField("objectList1"))) Assert.assertEquals(IndexReference.BOTH, entries.getValue().getReferenceType()); else Assert.assertEquals(IndexReference.SELF, entries.getValue().getReferenceType());
        }
    }

    @Indexable
    @IndexableContainer
    public class DummyContainterAndIndexable {

        @SearchCollection(reference = IndexReference.BOTH)
        List<Object> objectList;

        @SearchField
        String a;

        @SearchId
        int id;
    }

    @Test
    public void IndexableContainerIsAllowedInPresenceOfIndexable() throws SearchEngineMappingException, SearchEngineException, IllegalArgumentException, IllegalAccessException {
        DummyContainterAndIndexable dc = new DummyContainterAndIndexable();
        AnnotationConfigurationMapper.configureAndMap(dc);
        SearchMapping mapping = SearchEngine.getInstance().getConfiguration().getMapping(dc);
        Assert.assertNotNull(mapping);
        Map<Field, MappedFieldDescriptor> mappedFields = mapping.getMappedFields();
        Assert.assertEquals(3, mappedFields.size());
        Assert.assertTrue(mapping.isIndexableContainer());
        Assert.assertTrue(mapping.isIndexable());
    }

    @Indexable
    @IndexableContainer
    public class DummyWithIndexableByMapping {

        @SearchId
        int id = 0;

        @SearchContained
        SingleAttributeEntity contained = new SingleAttributeEntity(1, "single");
    }

    @Test
    public void SearchContainedIsMappedOnParent() throws SearchEngineMappingException, SearchEngineException, IllegalArgumentException, IllegalAccessException {
        DummyWithIndexableByMapping outer = new DummyWithIndexableByMapping();
        AnnotationConfigurationMapper.configureAndMap(outer, false);
        SearchMapping mapping = SearchEngine.getInstance().getConfiguration().getMapping(outer);
        Assert.assertNotNull(mapping);
        Map<Field, MappedFieldDescriptor> mappedFields = mapping.getMappedFields();
        Assert.assertEquals(2, mappedFields.size());
        Assert.assertTrue(mapping.isIndexableContainer());
        Assert.assertTrue(mapping.isIndexable());
    }

    @Test
    public void SearchContainedIsMappedByItself() throws SearchEngineMappingException, SearchEngineException, IllegalArgumentException, IllegalAccessException {
        DummyWithIndexableByMapping outer = new DummyWithIndexableByMapping();
        AnnotationConfigurationMapper.configureAndMap(outer, true);
        SearchMapping mapping = SearchEngine.getInstance().getConfiguration().getMapping(SingleAttributeEntity.class);
        Assert.assertNotNull(mapping);
        Map<Field, MappedFieldDescriptor> mappedFields = mapping.getMappedFields();
        Assert.assertEquals(2, mappedFields.size());
        Assert.assertTrue(mapping.isIndexable());
    }
}
