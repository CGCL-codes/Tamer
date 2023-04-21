package fr.univartois.cril.xtext.scoping;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;

public class AlsImportedNamespaceAwareLocalScopeProvider extends ImportedNamespaceAwareLocalScopeProvider {

    @Override
    public IScope getScope(EObject context, EReference reference) {
        IScope scope = super.getScope(context, reference);
        return scope;
    }
}
