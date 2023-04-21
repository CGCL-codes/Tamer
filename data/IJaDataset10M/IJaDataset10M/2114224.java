package com.farata.cleardatabuilder.validation.apt;

import java.util.Collection;
import org.eclipse.jdt.apt.core.util.EclipseMessager;
import clear.cdb.annotations.CX_GetMethod;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;

public class CX_GetMethodProcessor implements AnnotationProcessor {

    private AnnotationProcessorEnvironment _env;

    CX_GetMethodProcessor(AnnotationProcessorEnvironment _env) {
        this._env = _env;
    }

    @Override
    public void process() {
        EclipseMessager messager = (EclipseMessager) _env.getMessager();
        AnnotationTypeDeclaration annoDecl = (AnnotationTypeDeclaration) _env.getTypeDeclaration(CX_GetMethod.class.getName());
        Collection<Declaration> annotatedTypes = _env.getDeclarationsAnnotatedWith(annoDecl);
        for (Declaration decl : annotatedTypes) {
            Collection<AnnotationMirror> mirrors = decl.getAnnotationMirrors();
            for (AnnotationMirror mirror : mirrors) {
                if (!"CX_GetMethod".equals(mirror.getAnnotationType().getDeclaration().getSimpleName())) {
                    continue;
                }
                MethodDeclaration methodDeclaration = (MethodDeclaration) decl;
                CX_JPQLMethodProcessor.checkTransferInfo(mirror, methodDeclaration, false, messager);
            }
        }
    }
}
