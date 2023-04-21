package net.peelmeagrape.hibernate;

import org.dom4j.DocumentException;
import java.io.IOException;

public class FilterTest extends GenerateMappingsTestBase {

    public void testXmlGen() throws DocumentException, IOException {
        assertAnnotationsGenerateXml("<class name=\"net.peelmeagrape.hibernate.FilterTest$SomeEntity\">\n" + "<id name=\"Id\"/>" + "<filter condition=\":myFilterParam = MY_FILTERED_COLUMN\" name=\"filter\"/>" + "</class>" + "<filter-def name='filter'>" + "<filter-param name='myFilterParam' type='string'/>" + "</filter-def>", SomeEntity.class);
    }

    @FilterDef(name = "filter", params = { @FilterDef.Param(name = "myFilterParam", type = "string") })
    @H8Class(filters = @Filter(name = "filter", condition = ":myFilterParam = MY_FILTERED_COLUMN"))
    public static class SomeEntity {

        @Id
        private long Id;
    }
}
