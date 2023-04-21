package net.sourceforge.greenvine.generator.impl.java.entity.springhibernate;

import net.sourceforge.greenvine.generator.impl.tests.BaseGeneratorTest;
import net.sourceforge.greenvine.generator.template.Template;
import org.junit.Test;

public class SpringHibernateContextGeneratorTest extends BaseGeneratorTest {

    @Test
    public void testGenerate() throws Exception {
        String exportDirectory = "target/greenvine/src/main/resources";
        createExportDirectory(exportDirectory);
        Template template = this.factory.assembleTemplate("net/sourceforge/greenvine/generator/impl/java/entity/springhibernate/SpringHibernateContextTemplate.vm", exportDirectory);
        SpringHibernateContextGenerator generator = new SpringHibernateContextGenerator();
        this.executor.addGeneratorTask(generator, model, template);
    }
}
