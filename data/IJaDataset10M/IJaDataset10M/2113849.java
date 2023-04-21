package org.enml.documents.tests;

import java.io.File;
import java.io.IOException;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.enml.documents.Certification;
import org.enml.documents.DocumentsFactory;
import org.enml.documents.DocumentsPackage;

/**
 * <!-- begin-user-doc -->
 * A sample utility for the '<em><b>documents</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class DocumentsExample {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static final String copyright = "enml.org (C) 2007";

    /**
	 * <!-- begin-user-doc -->
	 * Load all the argument file paths or URIs as instances of the model.
	 * <!-- end-user-doc -->
	 * @param args the file paths or URIs.
	 * @generated
	 */
    public static void main(String[] args) {
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
        resourceSet.getPackageRegistry().put(DocumentsPackage.eNS_URI, DocumentsPackage.eINSTANCE);
        if (args.length == 0) {
            System.out.println("Enter a list of file paths or URIs that have content like this:");
            try {
                Resource resource = resourceSet.createResource(URI.createURI("http:///My.documents"));
                Certification root = DocumentsFactory.eINSTANCE.createCertification();
                resource.getContents().add(root);
                resource.save(System.out, null);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            for (int i = 0; i < args.length; ++i) {
                File file = new File(args[i]);
                URI uri = file.isFile() ? URI.createFileURI(file.getAbsolutePath()) : URI.createURI(args[0]);
                try {
                    Resource resource = resourceSet.getResource(uri, true);
                    System.out.println("Loaded " + uri);
                    for (EObject eObject : resource.getContents()) {
                        Diagnostic diagnostic = Diagnostician.INSTANCE.validate(eObject);
                        if (diagnostic.getSeverity() != Diagnostic.OK) {
                            printDiagnostic(diagnostic, "");
                        }
                    }
                } catch (RuntimeException exception) {
                    System.out.println("Problem loading " + uri);
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * Prints diagnostics with indentation.
	 * <!-- end-user-doc -->
	 * @param diagnostic the diagnostic to print.
	 * @param indent the indentation for printing.
	 * @generated
	 */
    protected static void printDiagnostic(Diagnostic diagnostic, String indent) {
        System.out.print(indent);
        System.out.println(diagnostic.getMessage());
        for (Diagnostic child : diagnostic.getChildren()) {
            printDiagnostic(child, indent + "  ");
        }
    }
}
