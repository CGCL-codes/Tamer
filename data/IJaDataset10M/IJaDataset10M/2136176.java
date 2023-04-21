package fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.analysis;

public class CollectionTypeIdentifierCSTypeNameReferenceResolver implements fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.IOclReferenceResolver<fr.inria.papyrus.uml4tst.emftext.ocl.CollectionTypeIdentifierCS, pivotmodel.Type> {

    private fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.analysis.OclDefaultResolverDelegate<fr.inria.papyrus.uml4tst.emftext.ocl.CollectionTypeIdentifierCS, pivotmodel.Type> delegate = new fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.analysis.OclDefaultResolverDelegate<fr.inria.papyrus.uml4tst.emftext.ocl.CollectionTypeIdentifierCS, pivotmodel.Type>();

    public void resolve(String identifier, fr.inria.papyrus.uml4tst.emftext.ocl.CollectionTypeIdentifierCS container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.IOclReferenceResolveResult<pivotmodel.Type> result) {
        delegate.resolve(identifier, container, reference, position, resolveFuzzy, result);
    }

    public String deResolve(pivotmodel.Type element, fr.inria.papyrus.uml4tst.emftext.ocl.CollectionTypeIdentifierCS container, org.eclipse.emf.ecore.EReference reference) {
        return delegate.deResolve(element, container, reference);
    }

    public void setOptions(java.util.Map<?, ?> options) {
    }
}
