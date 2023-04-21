package com.netflexitysolutions.commons.amazonws.sdb;

import java.io.IOException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.netflexitysolutions.amazonws.sdb.orm.internal.generator.OrmGenerator;
import freemarker.template.TemplateException;

/**
 * @author netflexity
 *
 */
public class CodeGeneratorTest {

    private static ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

    @Test
    public void testCodeGeneration() throws IOException, TemplateException {
        OrmGenerator generator = (OrmGenerator) context.getBean("generator");
        generator.generate();
    }
}
