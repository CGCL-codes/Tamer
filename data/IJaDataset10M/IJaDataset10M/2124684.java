package ru.ifmo.rain.astrans.interpreter;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import ru.ifmo.rain.astrans.CreatedEClass;
import ru.ifmo.rain.astrans.EClassifierReference;
import ru.ifmo.rain.astrans.ExistingEClass;
import ru.ifmo.rain.astrans.ExistingEDataType;
import ru.ifmo.rain.astrans.MappedEClass;
import ru.ifmo.rain.astrans.util.AstransSwitch;

class ReferenceResolver extends AstransSwitch {

    private final AstransInterpreterTrace trace;

    public ReferenceResolver(final AstransInterpreterTrace trace) {
        super();
        this.trace = trace;
    }

    @Override
    public EClass caseExistingEClass(ExistingEClass reference) {
        assert reference != null;
        return reference.getEClass();
    }

    @Override
    public EClass caseMappedEClass(MappedEClass reference) {
        assert reference != null;
        EClass result = trace.getMappedClass(reference.getProto());
        assert result != null;
        return result;
    }

    @Override
    public EClass caseCreatedEClass(CreatedEClass reference) {
        assert reference != null;
        EClass result = trace.getCreatedClass(reference.getCreate());
        assert result != null;
        return result;
    }

    @Override
    public EDataType caseExistingEDataType(ExistingEDataType reference) {
        return reference.getEDataType();
    }

    public EClassifier resolveEClassifierReference(EClassifierReference reference) {
        return (EClassifier) doSwitch(reference);
    }
}
