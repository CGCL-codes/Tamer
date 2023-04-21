/*
 * 
 *   Copyright (c) 2009 - 2011 Vektor software
 *   This code is licensed under both GNU General Public License and
 *   Vektor software commercial license. You may elect to use one or
 *   the other of these licenses.
 * 
 *   1. For the GPL license (GPL), you can modify and/or redistribute
 *   this code under the terms of GPL version 2, as published by Free
 *   software fondation.
 * 
 *   2. If you do not wish to be bound by the terms of GPL, you may
 *   purchase commercial license. Commercial license can be found
 *   on http://www.vektorsoft.com/license/acapulco-license.txt
 * 
 */
package com.vektorsoft.acapulco.tools.source.annotations;

import com.vektorsoft.acapulco.core.annotation.Extension;
import com.vektorsoft.acapulco.core.annotation.Listener;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * <p>
 *  Processor for {@link Listener} annotation type. This processor will generate a source file which is used for automatic
 * listener registration.
 * </p>
 *
 * @author Vladimir Djurovic
 */
@SupportedAnnotationTypes(value = "com.vektorsoft.acapulco.core.annotation.Listener")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ListenerAnnotationProcessor extends AbstractProcessor {
    
    /** Fully qualified name of generated class. */
    private static final String GENERATED_CLASS_NAME = "com.vektorsoft.acapulco.generated.DeclaredListeners";
    /** Path to FreeMarker template for source file generation. */
    private static final String TEMPLATE_FILE_PATH = "ftl/DeclaredListeners.ftl";

    /** Set containing names of annotated classes. */
    private Set<String> annotatedClasses;
    /** Messager for printing processing messages. */
    private Messager messager;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager = processingEnv.getMessager();
        annotatedClasses = new HashSet<>();
        for (TypeElement element : annotations) {

            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(element);
            String className = null;
            for (Element e : annotatedElements) {
                if (e instanceof TypeElement) {
                    className = ((TypeElement) e).getQualifiedName().toString();
                } else {
                    className = e.toString();
                }
                // do not add class if it also contains @Extension annotation
                if(e.getAnnotation(Extension.class) == null){
                    annotatedClasses.add(className);
                }
                
            }

        }


        if (!annotatedClasses.isEmpty()) {
            Filer filer = processingEnv.getFiler();
            try {
                // set up FreeMarker for processing
                Configuration config = new Configuration();
                config.setClassForTemplateLoading(this.getClass(), "/");
                config.setObjectWrapper(new DefaultObjectWrapper());
                // add annotated class to FTL map
                Map<String, Object> map = new HashMap<>();
                map.put("listeners", annotatedClasses);
                Template template = config.getTemplate(TEMPLATE_FILE_PATH);
                Writer writer = filer.createSourceFile(GENERATED_CLASS_NAME).openWriter();
                template.process(map, writer);
                writer.close();
            } catch (IOException | TemplateException ex) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Error occured: " + ex.getMessage());
                return true;
            }
        }

        return true;
    }
}
