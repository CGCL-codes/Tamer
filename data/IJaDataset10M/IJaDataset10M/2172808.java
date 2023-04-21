package fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.analysis;

public class OperationCallBaseExpCSOperationNameReferenceResolver implements fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.IOclReferenceResolver<fr.inria.papyrus.uml4tst.emftext.ocl.OperationCallBaseExpCS, pivotmodel.Operation> {

    private fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.analysis.OclDefaultResolverDelegate<fr.inria.papyrus.uml4tst.emftext.ocl.OperationCallBaseExpCS, pivotmodel.Operation> delegate = new fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.analysis.OclDefaultResolverDelegate<fr.inria.papyrus.uml4tst.emftext.ocl.OperationCallBaseExpCS, pivotmodel.Operation>();

    public void resolve(String identifier, fr.inria.papyrus.uml4tst.emftext.ocl.OperationCallBaseExpCS container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.IOclReferenceResolveResult<pivotmodel.Operation> result) {
        delegate.resolve(identifier, container, reference, position, resolveFuzzy, result);
    }

    public String deResolve(pivotmodel.Operation element, fr.inria.papyrus.uml4tst.emftext.ocl.OperationCallBaseExpCS container, org.eclipse.emf.ecore.EReference reference) {
        return delegate.deResolve(element, container, reference);
    }

    public void setOptions(java.util.Map<?, ?> options) {
    }
}
