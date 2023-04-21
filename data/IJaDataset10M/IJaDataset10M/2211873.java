package org.eclipse.xtext.example.typesystem.fjfirst.validator;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.typesystem.runtime.AbstractTypeSystemDeclarativeValidator;
import org.eclipse.xtext.typesystem.runtime.TypingJudgmentEnvironment;
import org.eclipse.xtext.example.fj.FjPackage;
import org.eclipse.xtext.example.typesystem.fjfirst.FJFirstTypeSystemDefinition;

public class FJFirstTypeSystemValidator extends AbstractTypeSystemDeclarativeValidator {

    protected FJFirstTypeSystemDefinition typeSystem = new FJFirstTypeSystemDefinition();

    protected FjPackage basicPackage = FjPackage.eINSTANCE;

    @Override
    protected List<EPackage> getEPackages() {
        List<EPackage> result = new ArrayList<EPackage>();
        result.add(org.eclipse.xtext.example.fj.FjPackage.eINSTANCE);
        return result;
    }

    public static final String CHECK_TYPE_FAILED_METHOD = "CheckTypeFailedMethod";

    @Check
    public void checkMethodType(org.eclipse.xtext.example.fj.Method object) {
        generateErrors(typeSystem.tryToApply(typeEnvironmentFor(object), "|-", ":", typeSystem.createEClassifierType(basicPackage.getMethod()), typeSystem.createBasicType("String"), object), object, CHECK_TYPE_FAILED_METHOD);
    }

    protected TypingJudgmentEnvironment typeEnvironmentFor(org.eclipse.xtext.example.fj.Method object) {
        return getDefaultTypingJudgmentEnvironmentFor(object);
    }

    public static final String CHECK_TYPE_FAILED_FIELD = "CheckTypeFailedField";

    @Check
    public void checkFieldType(org.eclipse.xtext.example.fj.Field object) {
        generateErrors(typeSystem.tryToApply(typeEnvironmentFor(object), "|-", ":", typeSystem.createEClassifierType(basicPackage.getField()), typeSystem.createBasicType("String"), object), object, CHECK_TYPE_FAILED_FIELD);
    }

    protected TypingJudgmentEnvironment typeEnvironmentFor(org.eclipse.xtext.example.fj.Field object) {
        return getDefaultTypingJudgmentEnvironmentFor(object);
    }

    public static final String CHECK_TYPE_FAILED_CLASS = "CheckTypeFailedClass";

    @Check
    public void checkClassType(org.eclipse.xtext.example.fj.Class object) {
        generateErrors(typeSystem.tryToApply(typeEnvironmentFor(object), "|-", ":", typeSystem.createEClassifierType(basicPackage.getClass_()), typeSystem.createBasicType("String"), object), object, CHECK_TYPE_FAILED_CLASS);
    }

    protected TypingJudgmentEnvironment typeEnvironmentFor(org.eclipse.xtext.example.fj.Class object) {
        return getDefaultTypingJudgmentEnvironmentFor(object);
    }
}
