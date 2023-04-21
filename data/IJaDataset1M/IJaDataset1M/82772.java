package org.tagnetic.tagneto;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javax.xml.bind.JAXBException;
import junit.framework.TestCase;
import org.tagnetic.core.configuration.ConfigurationLoader;
import org.tagnetic.core.exception.TagneticException;
import org.tagnetic.core.framework.TagContext;
import org.tagnetic.core.framework.TagContextFactory;
import org.tagnetic.core.jaxb.tagneticconfig.Tagneticconfig;
import org.tagnetic.core.transformer.Transformer;

public class TransformerTest2 extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TransformerTest2.class);
    }

    public void testTransformer() throws JAXBException, TagneticException, InvocationTargetException, IllegalAccessException {
        ArrayList fileList = new ArrayList();
        fileList.add("../config/Master.xml");
        fileList.add("TagneticConfigTest2.xml");
        ConfigurationLoader configLoader = new ConfigurationLoader(fileList);
        Tagneticconfig config = configLoader.load();
        TagContextFactory.setConfig(config);
        TagContext tagContext = TagContextFactory.createTagContext();
        Transformer transformer = new Transformer(tagContext);
        transformer.transform();
    }
}
