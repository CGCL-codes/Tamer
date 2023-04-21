package de.aw.star.prototype;

import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.JavaFileObject;
import javax.annotation.processing.AbstractProcessor;
import groovy.util.Node;
import javax.tools.ToolProvider;
import de.aw.star.prototype.fetch.internal.CodeAnalyzerProcessor;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author: Armin Weisser
 * @date: 23.09.2009
 * @time: 15:55:16
 */
public abstract class AstGenerator {

    private static Logger LOGGER = Logger.getLogger(AstGenerator.class.getName());

    public Node createAST(String... sourceFileNames) {
        JavaCompiler compiler = getJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(sourceFileNames);
        CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits);
        List<AbstractProcessor> processors = new LinkedList<AbstractProcessor>();
        CodeAnalyzerProcessor processor = new CodeAnalyzerProcessor();
        processors.add(processor);
        task.setProcessors(processors);
        LOGGER.info("Performing the compilation task.");
        task.call();
        return processor.getAST();
    }

    public abstract JavaCompiler getJavaCompiler();

    protected JavaCompiler getStandardJavaCompiler() {
        return ToolProvider.getSystemJavaCompiler();
    }
}
