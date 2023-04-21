package org.emftext.language.OCL.resource.OCL.mopp;

public class OCLSyntaxCoverageInformationProvider {

    public org.eclipse.emf.ecore.EClass[] getClassesWithSyntax() {
        return new org.eclipse.emf.ecore.EClass[] { org.emftext.language.OCL.OCLPackage.eINSTANCE.getSimpleName(), org.emftext.language.OCL.OCLPackage.eINSTANCE.getExp(), org.emftext.language.OCL.OCLPackage.eINSTANCE.getTypePathNameSimple(), org.emftext.language.OCL.OCLPackage.eINSTANCE.getVariableDeclarationWithInit(), org.emftext.language.OCL.OCLPackage.eINSTANCE.getLetExp(), org.emftext.language.OCL.OCLPackage.eINSTANCE.getIfExp() };
    }

    public org.eclipse.emf.ecore.EClass[] getStartSymbols() {
        return new org.eclipse.emf.ecore.EClass[] { org.emftext.language.OCL.OCLPackage.eINSTANCE.getExp() };
    }
}
