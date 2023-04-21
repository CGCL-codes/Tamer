package org.emftext.language.office.resource.office;

public interface IOfficeReferenceResolverSwitch extends org.emftext.language.office.resource.office.IOfficeConfigurable {

    public void resolveFuzzy(String identifier, org.eclipse.emf.ecore.EObject container, int position, org.emftext.language.office.resource.office.IOfficeReferenceResolveResult<org.eclipse.emf.ecore.EObject> result);
}
