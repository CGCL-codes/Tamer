package org.emftext.language.models.resource.model;

public interface IModelReferenceResolverSwitch extends org.emftext.language.models.resource.model.IModelConfigurable {

    public void resolveFuzzy(String identifier, org.eclipse.emf.ecore.EObject container, int position, org.emftext.language.models.resource.model.IModelReferenceResolveResult<org.eclipse.emf.ecore.EObject> result);
}
