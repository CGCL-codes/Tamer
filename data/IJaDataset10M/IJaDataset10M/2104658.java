package com.farata.cleardatabuilder.validation.apt;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;

public class CX_ServiceProcessor implements AnnotationProcessor {

    private AnnotationProcessorEnvironment _env;

    CX_ServiceProcessor(AnnotationProcessorEnvironment _env) {
        this._env = _env;
    }

    @Override
    public void process() {
    }
}
