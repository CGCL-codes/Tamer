package org.emftext.language.parametercheck.resource.pcheck;

public interface IPcheckTokenResolver extends org.emftext.language.parametercheck.resource.pcheck.IPcheckConfigurable {

    public void resolve(String lexem, org.eclipse.emf.ecore.EStructuralFeature feature, org.emftext.language.parametercheck.resource.pcheck.IPcheckTokenResolveResult result);

    public String deResolve(java.lang.Object value, org.eclipse.emf.ecore.EStructuralFeature feature, org.eclipse.emf.ecore.EObject container);
}
