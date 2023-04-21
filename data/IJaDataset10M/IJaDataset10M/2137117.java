package fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.mopp;

public class OclNewFileContentProvider {

    public fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.IOclMetaInformation getMetaInformation() {
        return new fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.mopp.OclMetaInformation();
    }

    public String getNewFileContent(String newFileName) {
        return getExampleContent(new org.eclipse.emf.ecore.EClass[] { fr.inria.papyrus.uml4tst.emftext.ocl.OclPackage.eINSTANCE.getPackageDeclarationWithNamespaceCS(), fr.inria.papyrus.uml4tst.emftext.ocl.OclPackage.eINSTANCE.getPackageDeclarationWithoutNamespaceCS() }, getMetaInformation().getClassesWithSyntax(), newFileName);
    }

    protected String getExampleContent(org.eclipse.emf.ecore.EClass[] startClasses, org.eclipse.emf.ecore.EClass[] allClassesWithSyntax, String newFileName) {
        String content = "";
        for (org.eclipse.emf.ecore.EClass next : startClasses) {
            content = getExampleContent(next, allClassesWithSyntax, newFileName);
            if (content.trim().length() > 0) {
                break;
            }
        }
        return content;
    }

    protected String getExampleContent(org.eclipse.emf.ecore.EClass eClass, org.eclipse.emf.ecore.EClass[] allClassesWithSyntax, String newFileName) {
        org.eclipse.emf.ecore.EObject root = new fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.util.OclMinimalModelHelper().getMinimalModel(eClass, allClassesWithSyntax, newFileName);
        if (root == null) {
            return "";
        }
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.IOclTextPrinter printer = getPrinter(buffer);
        try {
            printer.print(root);
        } catch (java.io.IOException e) {
            fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.mopp.OclPlugin.logError("Exception while generating example content.", e);
        }
        return buffer.toString();
    }

    public fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.IOclTextPrinter getPrinter(java.io.OutputStream outputStream) {
        return getMetaInformation().createPrinter(outputStream, new fr.inria.papyrus.uml4tst.emftext.ocl.resource.ocl.mopp.OclResource());
    }
}
