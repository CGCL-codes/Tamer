package org.nightlabs.annotation.facade.apt;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import org.nightlabs.annotation.facade.Facade;
import org.nightlabs.annotation.facade.FacadeMethodRule;
import org.nightlabs.annotation.facade.FacadeRemoveParameter;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;

/**
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public class FacadeGenerator extends AbstractFacadeGenerator {

    public FacadeGenerator(ClassDeclaration classDeclaration, AnnotationProcessorEnvironment env) {
        super(classDeclaration, env);
    }

    @Override
    protected String getTargetPackageName(ClassDeclaration classDeclaration) {
        Facade annotation = getFacadeAnnotation(classDeclaration);
        if (!annotation.targetPackage().isEmpty()) return annotation.targetPackage();
        return super.getTargetPackageName(classDeclaration);
    }

    @Override
    protected String getTargetSimpleClassName(ClassDeclaration classDeclaration) {
        Facade annotation = getFacadeAnnotation(classDeclaration);
        if (!annotation.targetName().isEmpty()) return annotation.targetName();
        return super.getTargetSimpleClassName(classDeclaration);
    }

    @Override
    protected String getRulesFileValue(ClassDeclaration classDeclaration) {
        return getFacadeAnnotation(classDeclaration).rulesFile();
    }

    private Facade getFacadeAnnotation(ClassDeclaration classDeclaration) {
        Facade facadeAnnotation = classDeclaration.getAnnotation(Facade.class);
        if (facadeAnnotation == null) throw new IllegalStateException("No annotation found: " + Facade.class.getName());
        return facadeAnnotation;
    }

    protected String getFacadeClassRule(ClassDeclaration classDeclaration, String key) {
        return getRule(classDeclaration, RuleType.CLASS, getFacadeAnnotation(classDeclaration).id(), key);
    }

    protected String getFacadeMethodRule(ClassDeclaration classDeclaration, MethodDeclaration methodDeclaration, String key) {
        FacadeMethodRule methodAnnotation = methodDeclaration.getAnnotation(FacadeMethodRule.class);
        if (methodAnnotation == null) return "";
        String id = methodAnnotation.id();
        return getRule(classDeclaration, RuleType.METHOD, id, key);
    }

    @Override
    protected void writeClassAnnotations(ClassDeclaration classDeclaration, PrintWriter w) {
        super.writeClassAnnotations(classDeclaration, w);
        w.println(getFacadeClassRule(classDeclaration, "additionalAnnotations"));
    }

    @Override
    protected void writeClassPreCode(ClassDeclaration classDeclaration, PrintWriter w) {
        super.writeClassPreCode(classDeclaration, w);
        w.println(getFacadeClassRule(classDeclaration, "preCode"));
    }

    @Override
    protected void writeClassPostCode(ClassDeclaration classDeclaration, PrintWriter w) {
        super.writeClassPostCode(classDeclaration, w);
        w.println(getFacadeClassRule(classDeclaration, "postCode"));
    }

    @Override
    protected String getMethodParameterString(ClassDeclaration classDeclaration, MethodDeclaration methodDeclaration, Collection<ParameterDeclaration> parametersToRemove) {
        if (parametersToRemove == null) parametersToRemove = new HashSet<ParameterDeclaration>();
        Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
        for (ParameterDeclaration parameterDeclaration : parameters) {
            FacadeRemoveParameter rpa = parameterDeclaration.getAnnotation(FacadeRemoveParameter.class);
            if (rpa != null) parametersToRemove.add(parameterDeclaration);
        }
        String parameterString = super.getMethodParameterString(classDeclaration, methodDeclaration, parametersToRemove);
        String additionalParameters = getFacadeMethodRule(classDeclaration, methodDeclaration, "additionalParameters");
        if (!additionalParameters.isEmpty()) {
            if (!parameterString.isEmpty()) parameterString += ", ";
            parameterString += additionalParameters;
        }
        return parameterString;
    }

    @Override
    public void writeMethodPreCode(ClassDeclaration classDeclaration, MethodDeclaration methodDeclaration, PrintWriter w) {
        super.writeMethodPreCode(classDeclaration, methodDeclaration, w);
        w.println(getFacadeMethodRule(classDeclaration, methodDeclaration, "preCode"));
    }

    @Override
    public void writeMethodPostCode(ClassDeclaration classDeclaration, MethodDeclaration methodDeclaration, PrintWriter w) {
        w.println(getFacadeMethodRule(classDeclaration, methodDeclaration, "postCode"));
        super.writeMethodPostCode(classDeclaration, methodDeclaration, w);
    }
}
