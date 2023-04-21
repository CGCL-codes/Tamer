package tudresden.ocl20.pivot.language.ocl.resource.ocl.analysis;

import java.util.List;
import tudresden.ocl20.pivot.language.ocl.resource.ocl.IOclReferenceResolveHelper;
import tudresden.ocl20.pivot.language.ocl.resource.ocl.OclReferenceResolveHelperProvider;
import tudresden.ocl20.pivot.pivotmodel.Namespace;

public class TypePathNameNestedCSNamespaceReferenceResolver implements tudresden.ocl20.pivot.language.ocl.resource.ocl.IOclReferenceResolver<tudresden.ocl20.pivot.language.ocl.TypePathNameNestedCS, tudresden.ocl20.pivot.pivotmodel.Namespace> {

    private tudresden.ocl20.pivot.language.ocl.resource.ocl.analysis.OclDefaultResolverDelegate<tudresden.ocl20.pivot.language.ocl.TypePathNameNestedCS, tudresden.ocl20.pivot.pivotmodel.Namespace> delegate = new tudresden.ocl20.pivot.language.ocl.resource.ocl.analysis.OclDefaultResolverDelegate<tudresden.ocl20.pivot.language.ocl.TypePathNameNestedCS, tudresden.ocl20.pivot.pivotmodel.Namespace>();

    public void resolve(java.lang.String identifier, tudresden.ocl20.pivot.language.ocl.TypePathNameNestedCS container, org.eclipse.emf.ecore.EReference reference, int position, boolean resolveFuzzy, final tudresden.ocl20.pivot.language.ocl.resource.ocl.IOclReferenceResolveResult<tudresden.ocl20.pivot.pivotmodel.Namespace> result) {
        IOclReferenceResolveHelper rrHelper = OclReferenceResolveHelperProvider.getOclReferenceResolveHelper();
        if (rrHelper != null) {
            List<Namespace> namespaces = rrHelper.resolveNamespace(identifier, resolveFuzzy, container);
            for (Namespace namespace : namespaces) {
                if (!resolveFuzzy) result.addMapping(identifier, namespace); else result.addMapping(namespace.getName(), namespace);
            }
        }
    }

    public java.lang.String deResolve(tudresden.ocl20.pivot.pivotmodel.Namespace element, tudresden.ocl20.pivot.language.ocl.TypePathNameNestedCS container, org.eclipse.emf.ecore.EReference reference) {
        return delegate.deResolve(element, container, reference);
    }

    public void setOptions(java.util.Map<?, ?> options) {
    }
}
