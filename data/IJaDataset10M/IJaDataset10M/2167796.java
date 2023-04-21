package net.sf.sail.emf.sailuserdata.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.sf.sail.emf.sailuserdata.EAnnotationBundle;
import net.sf.sail.emf.sailuserdata.SailuserdataPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Demonstrates how the AnnotationBundle string xml can be loaded into an
 * EMF object and provides static method that can be used by outside
 * classes
 * 
 * @author hirokiterashima
 */
public class AnnotationBundleLoader extends DefaultHandler {

    /**
	 * Loads AnnotationBundle in a XML-String format into an EAnnotationBundle object
	 * 
	 * @param annotationBundleString AnnotationBundle represented in xml string 
	 * @return EAnnotationBundle AnnotationBundle object instantiated using the values that are
	 *     stored in the provided annotationBundleString
	 */
    @SuppressWarnings("unchecked")
    public static EAnnotationBundle loadAnnotationBundle(String annotationBundleString) {
        URI emfURI = URI.createURI(annotationBundleString);
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
        @SuppressWarnings("unused") SailuserdataPackage sudPackage = SailuserdataPackage.eINSTANCE;
        Resource annotationBundleResource = (Resource) resourceSet.createResource(emfURI);
        try {
            InputStream is = new ByteArrayInputStream(annotationBundleString.getBytes());
            annotationBundleResource.load(is, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        EAnnotationBundle annotationBundle = (EAnnotationBundle) annotationBundleResource.getContents().get(0);
        return annotationBundle;
    }

    /**
	 * There are currently two ways to instantiate the EAnnotationBundle object:
	 * 1) calling AnnotationBundleLoader.loadPortfolio(String) static method. This can be useful
	 *     when you already have the xmlstring
	 * 2) from a file which contains a xmlstring.
	 */
    public static void main(String[] args) {
        String annotationBundleString = "<?xml version=\"1.0\" encoding=\"ASCII\"?>" + "<sailuserdata:EAnnotationBundle xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:sailuserdata=\"sailuserdata\">" + "<annotationGroups annotationSource=\"http://sail.sf.net/annotations/test\">" + "<annotations entityUUID=\"dddddddd-6004-0002-0000-000000000000\" entityName=\"undefined6\" contentType=\"text/plain\" contents=\"Test rim annotation for rim with name undefined6\"/>" + "<annotations entityUUID=\"dddddddd-6004-0003-0000-000000000000\" entityName=\"undefined7\" contentType=\"text/plain\" contents=\"Test rim annotation for rim with name undefined7\"/>" + "</annotationGroups></sailuserdata:EAnnotationBundle>";
        System.out.println("annotationBundleString:");
        System.out.println(annotationBundleString);
        EAnnotationBundle annotationBundle = AnnotationBundleLoader.loadAnnotationBundle(annotationBundleString);
        System.out.println("annotationbundle object:");
        System.out.println(annotationBundle);
    }
}
