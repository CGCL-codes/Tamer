package com.mu.jacob.core.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import org.apache.log4j.Logger;
import com.google.inject.Inject;
import com.mu.jacob.core.builder.AbstractBuilder;
import com.mu.jacob.core.builder.IBuilder;
import com.mu.jacob.core.builder.IModelContext;
import com.mu.jacob.core.model.IModel;
import com.mu.jacob.core.model.Model;
import com.sun.tools.javac.jvm.Target;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.DefaultFileManager;

/**
 * Compiler processor for Jacob
 * @author Adam Smyczek
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class JacobProcessor extends AbstractProcessor {

    private int rounds = 0;

    private List<IBuilder> builders = null;

    @Inject
    private Config config;

    @Inject
    private IModel model;

    /**
	 * Main Jacob process method
	 */
    public void process(List<IBuilder> builders) {
        this.builders = builders;
        compile(getOptions());
    }

    /**
	 * Process called from Java compiler
	 */
    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnv) {
        if (rounds == 0) {
            ((Model) model).initEnvironment(processingEnv, roundEnv);
            try {
                for (IBuilder builder : builders) {
                    if (builder instanceof AbstractBuilder) {
                        for (Object context : ((AbstractBuilder) builder).getContextMap().values()) {
                            if (context instanceof IModelContext) {
                                ((IModelContext) context).initContext(model);
                            }
                        }
                    }
                    builder.initRenderer();
                    builder.build(model);
                }
            } catch (Exception e) {
                logger.error("Processor exception!", e);
            }
        }
        rounds++;
        return true;
    }

    /**
	 * Processor compile function, initializes and runs Java compiler.
	 * @param options
	 */
    private void compile(String... options) {
        com.sun.tools.javac.main.Main compiler = new com.sun.tools.javac.main.Main("jacob");
        Context context = new Context();
        DefaultFileManager.preRegister(context);
        int status = compiler.compile(options, context, com.sun.tools.javac.util.List.<JavaFileObject>nil(), Arrays.<Processor>asList(new Processor[] { this }));
        if (status != 0) {
            logger.error("Error processing model!");
        }
    }

    /**
	 * @return compiler options
	 */
    private String[] getOptions() {
        List<String> params = new ArrayList<String>();
        params.add("-target");
        params.add(Target.JDK1_6.name);
        params.add("-proc:only");
        if (config.getClassPath() != null) {
            params.add("-classpath");
            params.add(config.getClassPath());
        }
        for (String fileName : getAbsolutePathModelFiles()) {
            params.add(fileName);
        }
        return params.toArray(new String[params.size()]);
    }

    /**
	 * @return absolute paths for all model classes
	 */
    public List<String> getAbsolutePathModelFiles() {
        List<String> result = new ArrayList<String>();
        for (ModelSet modelSet : config.getModelSets()) {
            String path = modelSet.getDir().getPath() + File.separator;
            for (String fileName : modelSet.getFileNames()) {
                result.add(path + fileName);
            }
        }
        return result;
    }

    private static Logger logger = Logger.getLogger(JacobProcessor.class);
}
