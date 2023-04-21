package com.otom.bcel.deepmapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.List;
import java.util.Vector;
import org.junit.Test;
import com.otom.bcel.Mapper;
import com.otom.bcel.annotations.CollectionHint;
import com.otom.bcel.annotations.CollectionIndexHint;
import com.otom.bcel.annotations.CollectionInfo;
import com.otom.bcel.annotations.MapClassTo;
import com.otom.bcel.annotations.MapTo;
import com.otom.bcel.testobjects.InnerDestinationObject;

public class IndexHintsTest {

    @Test
    public void testSourceToDestination() {
        IndexHintsMappingSource source = new IndexHintsMappingSource();
        source.setName1("true");
        source.setName2("false");
        source.setInnerDestinationObject(new InnerDestinationObject());
        IndexHintsMappingDestination destination = new IndexHintsMappingDestination();
        Mapper<IndexHintsMappingSource, IndexHintsMappingDestination> mapper = new Mapper<IndexHintsMappingSource, IndexHintsMappingDestination>();
        mapper.map(source, destination);
        assertEquals(Boolean.TRUE, destination.getStringList().get(0));
        assertEquals(Boolean.FALSE, destination.getInnerDestinationObjectArr()[0].getSecondLevelObject().getNames().get(0));
    }

    @Test
    public void testDestinationToSource() {
        IndexHintsMappingDestination destination = new IndexHintsMappingDestination();
        IndexHintsMappingSource source = new IndexHintsMappingSource();
        source.setName1("true");
        source.setName2("false");
        source.setId(10);
        Mapper<IndexHintsMappingDestination, IndexHintsMappingSource> mapper = new Mapper<IndexHintsMappingDestination, IndexHintsMappingSource>();
        mapper.map(destination, source);
        assertNull(source.getName1());
        assertNull(source.getName2());
        assertEquals(0, source.getId());
    }

    @MapClassTo(value = IndexHintsMappingDestination.class, biDirectional = true)
    public class IndexHintsMappingSource {

        @MapTo(name = "stringList[0]", hints = @CollectionIndexHint(@CollectionInfo(type = Boolean.class, impl = Vector.class)))
        private String name1;

        @MapTo(name = "innerDestinationObjectArr[0].secondLevelObject.names[0]", hints = @CollectionIndexHint({ @CollectionInfo(type = Boolean.class, impl = Vector.class) }))
        private String name2;

        @MapTo(name = "innerDestinationObjectArr[0].innerInt")
        private String name3;

        @MapTo(name = "intArr[0]")
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName3() {
            return name3;
        }

        public void setName3(String name3) {
            this.name3 = name3;
        }

        private InnerDestinationObject innerDestinationObject;

        public InnerDestinationObject getInnerDestinationObject() {
            return innerDestinationObject;
        }

        public void setInnerDestinationObject(InnerDestinationObject innerDestinationObject) {
            this.innerDestinationObject = innerDestinationObject;
        }

        public String getName1() {
            return name1;
        }

        public void setName1(String name1) {
            this.name1 = name1;
        }

        public String getName2() {
            return name2;
        }

        public void setName2(String name2) {
            this.name2 = name2;
        }
    }

    public class IndexHintsMappingDestination {

        @CollectionHint(source = @CollectionInfo(type = Boolean.class, impl = Vector.class))
        private List stringList;

        private int[] intArr;

        public int[] getIntArr() {
            return intArr;
        }

        public void setIntArr(int[] intArr) {
            this.intArr = intArr;
        }

        private InnerDestinationObject[] innerDestinationObjectArr;

        public InnerDestinationObject[] getInnerDestinationObjectArr() {
            return innerDestinationObjectArr;
        }

        public void setInnerDestinationObjectArr(InnerDestinationObject[] innerDestinationObjectArr) {
            this.innerDestinationObjectArr = innerDestinationObjectArr;
        }

        public List getStringList() {
            return stringList;
        }

        public void setStringList(List stringList) {
            this.stringList = stringList;
        }
    }
}
