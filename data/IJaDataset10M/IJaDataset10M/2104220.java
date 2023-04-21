package org.emftext.language.parametercheck.resource.pcheck;

public interface IPcheckTextDiagnostic extends org.eclipse.emf.ecore.resource.Resource.Diagnostic {

    public int getCharStart();

    public int getCharEnd();

    public int getColumn();

    public int getLine();

    public org.emftext.language.parametercheck.resource.pcheck.IPcheckProblem getProblem();

    public boolean wasCausedBy(org.eclipse.emf.ecore.EObject element);
}
