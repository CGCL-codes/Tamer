package org.nightlabs.annotation.wsfacade.apt;

import java.util.Collection;
import java.util.StringTokenizer;
import org.nightlabs.annotation.facade.apt.FacadeGenerator;
import org.nightlabs.annotation.wsfacade.WSFacade;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;

/**
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public class WSFacadeGenerator extends FacadeGenerator {

    public WSFacadeGenerator(ClassDeclaration classDeclaration, AnnotationProcessorEnvironment env) {
        super(classDeclaration, env);
    }

    @Override
    protected String getRulesFileValue(ClassDeclaration classDeclaration) {
        return getFacadeAnnotation(classDeclaration).rulesFile();
    }

    private WSFacade getFacadeAnnotation(ClassDeclaration classDeclaration) {
        WSFacade facadeAnnotation = classDeclaration.getAnnotation(WSFacade.class);
        if (facadeAnnotation == null) throw new IllegalStateException("No annotation found: " + WSFacade.class.getName());
        return facadeAnnotation;
    }

    @Override
    protected String getTargetPackageName(ClassDeclaration classDeclaration) {
        WSFacade annotation = getFacadeAnnotation(classDeclaration);
        if (!annotation.targetPackage().isEmpty()) return annotation.targetPackage();
        return super.getTargetPackageName(classDeclaration);
    }

    @Override
    protected String getTargetSimpleClassName(ClassDeclaration classDeclaration) {
        WSFacade annotation = getFacadeAnnotation(classDeclaration);
        if (!annotation.targetName().isEmpty()) return annotation.targetName();
        return super.getTargetSimpleClassName(classDeclaration);
    }

    @Override
    protected String getFacadeClassRule(ClassDeclaration classDeclaration, String key) {
        return getRule(classDeclaration, RuleType.CLASS, getFacadeAnnotation(classDeclaration).id(), key);
    }

    @Override
    protected String getMethodParameterString(ClassDeclaration classDeclaration, MethodDeclaration methodDeclaration, Collection<ParameterDeclaration> parametersToRemove) {
        String parameterString = super.getMethodParameterString(classDeclaration, methodDeclaration, parametersToRemove);
        StringTokenizer st = new StringTokenizer(parameterString, ",");
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String parameterPair = st.nextToken().trim();
            String[] split = parameterPair.split(" ");
            if (split.length < 2) throw new IllegalStateException("Invalid parameter pair: " + parameterPair);
            String parameterName = split[split.length - 1];
            if (sb.length() > 0) sb.append(", ");
            sb.append("@javax.jws.WebParam(name=\"" + parameterName + "\") ");
            sb.append(parameterPair);
        }
        return sb.toString();
    }
}
