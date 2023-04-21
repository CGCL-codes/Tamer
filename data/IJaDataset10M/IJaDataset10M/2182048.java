package com.floreysoft.jmte;

import static org.junit.Assert.*;
import java.net.URI;
import java.util.Arrays;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;
import org.junit.Test;
import com.floreysoft.jmte.template.Template;

public class CompiledEngineTest extends AbstractEngineTest {

    protected Engine newEngine() {
        Engine engine = new Engine();
        engine.setUseCompilation(true);
        return engine;
    }

    @Test
    public void compiledClassLoaders() throws Exception {
        String templateSource = "${address}";
        Engine engine1 = new Engine();
        engine1.setUseCompilation(true);
        Engine engine2 = new Engine();
        engine2.setUseCompilation(true);
        Template template1 = engine1.getTemplate(templateSource);
        Template template2 = engine2.getTemplate(templateSource);
        assertEquals(template1.getClass().getName(), template2.getClass().getName());
        assertNotSame(template1.getClass(), template2.getClass());
        String transformed1 = template1.transform(DEFAULT_MODEL, DEFAULT_LOCALE);
        String transformed2 = template2.transform(DEFAULT_MODEL, DEFAULT_LOCALE);
        assertEquals(transformed1, transformed2);
    }
}
