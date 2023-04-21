package com.unitt.buildtools.modeldata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.unitt.modeldata.ModelDataBean;

public class ModelDataBeanFactory implements AnnotationProcessorFactory {

    protected String sourceDir;

    public ModelDataBeanFactory(String aSourceDir) {
        sourceDir = aSourceDir;
    }

    public ModelDataBeanFactory() {
    }

    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> aDeclarations, AnnotationProcessorEnvironment aEnvironment) {
        AnnotationProcessor result;
        if (aDeclarations.isEmpty()) {
            result = AnnotationProcessors.NO_OP;
        } else {
            result = new ModelDataBeanProcessor(aEnvironment);
        }
        return result;
    }

    public Collection<String> supportedAnnotationTypes() {
        Collection<String> supported = new ArrayList<String>();
        supported.add(ModelDataBean.class.getName());
        return supported;
    }

    public Collection<String> supportedOptions() {
        List<String> options = new ArrayList<String>();
        options.add("-s");
        options.add("-Aoutput");
        return options;
    }
}
