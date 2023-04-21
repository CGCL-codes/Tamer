package org.apache.ibatis.builder;

import java.io.InputStream;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

public class XmlMapperBuilderTest {

    @Test
    public void shouldSuccessfullyLoadXMLMapperFile() throws Exception {
        Configuration configuration = new Configuration();
        String resource = "org/apache/ibatis/builder/AuthorMapper.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        XMLMapperBuilder builder = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
        builder.parse();
    }
}
