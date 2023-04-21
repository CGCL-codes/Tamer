package org.easy.framework.generate;

import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBException;
import junit.framework.TestCase;
import org.easy.framework.generate.impl.engine.FreemakerTemplateEngine;
import org.easy.framework.generate.impl.task.GenJavaTask;
import org.easy.framework.metadata.MetadataScanner;
import org.easy.framework.metadata.definition.Application;
import org.easy.framework.metadata.definition.Module;

public class GenerateTaskTest extends TestCase {

    private TemplateRenderingContext context;

    private TemplateEngine templateEngine;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initContext();
        initTemplateEngine();
    }

    public void testGenerateJava() throws Exception {
        Task genJavaTask = new GenJavaTask(context, templateEngine);
        genJavaTask.execute();
    }

    private synchronized void initContext() throws IOException, JAXBException {
        if (context == null) {
            MetadataScanner scanner = new MetadataScanner();
            Application application = scanner.loadApplication();
            List<Module> modules = scanner.loadModules();
            context = new TemplateRenderingContext();
            context.setApplication(application);
            context.setModules(modules);
        }
    }

    private synchronized void initTemplateEngine() {
        if (templateEngine == null) {
            templateEngine = new FreemakerTemplateEngine();
        }
    }
}
