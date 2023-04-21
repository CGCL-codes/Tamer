package com.technoetic.tornado.config.annotations;

import static com.technoetic.tornado.ColumnMapping.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.junit.Test;
import com.technoetic.tornado.ColumnMapping;
import com.technoetic.tornado.ObjectMapping;
import com.technoetic.tornado.PersistenceException;
import com.technoetic.tornado.plugin.EnumColumnConverter;

public class TestAnnotationProcessor {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldProcessEntityAndTableAnnotations() throws PersistenceException {
        AnnotationProcessor processor = new AnnotationProcessor();
        assertTrue(processor.isEntity(TestClass1.class));
        ObjectMapping mapping = processor.getObjectMapping(TestClass1.class);
        assertEquals(mapping.getObjectClass(), TestClass1.class);
        assertThat(mapping.getTableName(), is("TABLE"));
        ColumnMapping[] columns = mapping.getTableMapping();
        assertThat(columns.length, is(3));
        assertThat(mapping.getTableMapping(), hasItemInArray(new ColumnMapping("value", String.class, "db_value", Types.VARCHAR)));
        ColumnMapping column = new ColumnMapping("count", SINGLE_COLUMN_SUBOBJECT, "count", Types.OTHER);
        column.converter = new EnumColumnConverter(TestClass1.Count.class);
        assertThat(mapping.getTableMapping(), hasItemInArray(column));
        column = new ColumnMapping("testObject", SINGLE_COLUMN_SUBOBJECT, "testObject", Types.OTHER);
        column.converter = new TestClass2ColumnConverter(TestClass2.class);
        assertThat(mapping.getTableMapping(), hasItemInArray(column));
        assertThat(Arrays.asList(mapping.getIdentityColumns()), hasItems("count", "db_value"));
        assertThat(mapping.getIdentityExtractor(), notNullValue());
        assertThat(mapping.getInstanceFactory(), notNullValue());
    }

    @Entity
    @Table(name = "TABLE")
    public static class TestClass1 {

        public enum Count {

            ONE, TWO
        }

        ;

        @Id
        @Column(name = "db_value")
        public String getValue() {
            return "value";
        }

        public void setValue(String value) {
        }

        @Id
        public Count getCount() {
            return Count.ONE;
        }

        public void setCount(Count value) {
        }

        @Transient
        public int getNumber() {
            return 0;
        }

        public void setNumber(int n) {
        }

        @ColumnConverter(TestClass2ColumnConverter.class)
        public TestClass2 getTestObject() {
            return null;
        }

        public void setTestObject(TestClass2 date) {
        }
    }

    public static class TestClass2 {
    }

    public static class TestClass2ColumnConverter implements com.technoetic.tornado.plugin.ColumnConverter {

        public TestClass2ColumnConverter(Class<?> clazz) {
        }

        @Override
        public int getSqlColumnType() throws PersistenceException {
            return Types.VARCHAR;
        }

        @Override
        @SuppressWarnings("deprecation")
        public Object toJavaObject(Object sqlObject) throws PersistenceException {
            return new Date((String) sqlObject);
        }

        @Override
        public Object toSqlObject(Object javaObject) throws PersistenceException {
            return javaObject.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestClass2ColumnConverter;
        }
    }
}
