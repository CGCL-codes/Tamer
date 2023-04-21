package org.emftext.language.models.resource.model;

public interface IModelLocationMap {

    public void setLine(org.eclipse.emf.ecore.EObject element, int line);

    public int getLine(org.eclipse.emf.ecore.EObject element);

    public void setColumn(org.eclipse.emf.ecore.EObject element, int column);

    public int getColumn(org.eclipse.emf.ecore.EObject element);

    public void setCharStart(org.eclipse.emf.ecore.EObject element, int charStart);

    public int getCharStart(org.eclipse.emf.ecore.EObject element);

    public void setCharEnd(org.eclipse.emf.ecore.EObject element, int charEnd);

    public int getCharEnd(org.eclipse.emf.ecore.EObject element);

    public java.util.List<org.eclipse.emf.ecore.EObject> getElementsAt(int documentOffset);

    public java.util.List<org.eclipse.emf.ecore.EObject> getElementsBetween(int startOffset, int endOffset);
}
