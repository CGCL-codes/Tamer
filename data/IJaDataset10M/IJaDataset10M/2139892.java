package eulergui.inputs;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EMOFResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.uml2.uml.UMLPackage;

/** XMI (eCore) To N3 Converter:
 * just converts XMI "triples" into RDF triples, package namespaces into RDF prefixes;
 * there is no intelligence at all here (like transforming UML primitive types into XSD, etc);
 * the intelligence is in rules downstream.
 *
 * Caution: when the XMI has several values
 * for one object and the same structuralFeature,
 * it become duplicated triples, that are eliminated by CWM .
 * */
public class XMIToN3Converter implements N3Converter {

    private static int indent;

    private final Set<EObject> already_traversed = new HashSet<EObject>();

    private static boolean doSaveEcoreContentAsEMOF;

    private boolean debug;

    private URI uri;

    private final Map<String, String> prefixTOURIMap = new HashMap<String, String>();

    private PrintStream printStream = System.out;

    public static void main(String[] args) throws IOException {
        final String ecoreFileName = args[0];
        final XMIToN3Converter app = new XMIToN3Converter();
        final EList<EObject> contents = app.loadEcoreFile(ecoreFileName);
        final URI uri = URI.createFileURI(ecoreFileName);
        app.translateToN3(contents, uri);
        if (doSaveEcoreContentAsEMOF) {
            app.saveEcoreContentAsEMOF(contents, ecoreFileName + ".emof");
        }
    }

    /**
	 * @see eulergui.inputs.N3Converter#loadURIAndTranslateToN3(org.eclipse.emf.common.util.URI, java.io.OutputStream)
	 */
    @Override
    public void loadURIAndTranslateToN3(java.net.URI uri, OutputStream outputStream) {
        final URI uri_emf = URI.createURI(uri.toString());
        loadEcoreURIAndTranslateToN3(uri_emf, outputStream);
    }

    @Override
    public void loadURLAndTranslateToN3(URL url, OutputStream outputStream) {
        throw new RuntimeException("Not implemented yet");
    }

    /** load eCore URI and translate To N3 */
    public void loadEcoreURIAndTranslateToN3(URI uri, OutputStream outputStream) {
        this.printStream = new PrintStream(outputStream);
        this.uri = uri;
        final EList<EObject> contents = loadECoreURI(uri);
        translateToN3(contents, uri);
        try {
            outputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void translateToN3(EList<EObject> contents, URI uri2) {
        this.uri = uri2;
        print("@prefix : <" + uri + "#> .");
        traverse(contents);
    }

    private void traverse(EList<EObject> contents) {
        for (final EObject object : contents) {
            traverse(object);
        }
    }

    private void traverse(EObject object) {
        if (already_traversed.contains(object)) {
            return;
        } else {
            already_traversed.add(object);
        }
        indent++;
        final EClass clazz = object.eClass();
        if (debug) {
            print("# id " + object.hashCode() + ", object.eClass() " + clazz);
        }
        String prefix;
        prefix = processXMLprefix(clazz);
        print(":id" + object.hashCode() + " a " + prefix + ":" + clazz.getName() + " .");
        final EList<EStructuralFeature> sf = clazz.getEAllStructuralFeatures();
        for (final EStructuralFeature structuralFeature : sf) {
            if (debug) {
                print("# structuralFeature " + structuralFeature.getName());
            }
            final Object value = object.eGet(structuralFeature);
            if (value != null) {
                if (debug) {
                    print("# structuralFeature " + structuralFeature.getName() + " : class = " + value.getClass());
                }
                if (structuralFeature instanceof EAttribute) {
                    final EAttribute eattribute = (EAttribute) structuralFeature;
                    if (structuralFeature.getDefaultValue() != value) {
                        prefix = processXMLprefix(eattribute.getEType());
                        String type = "";
                        if (!"String".equals(eattribute.getEType().getName())) {
                            type = "^^" + prefix + ":" + eattribute.getEType().getName();
                        }
                        print(":id" + object.hashCode() + " " + prefix + ":" + structuralFeature.getName() + " \"\"\"" + value + "\"\"\"" + type + " .");
                    }
                } else {
                    final EReference ereference = (EReference) structuralFeature;
                    prefix = processXMLprefix(ereference.getEType());
                    if (value instanceof List<?>) {
                        final List<?> list = (List<?>) value;
                        for (final Object object2 : list) {
                            if (object2 instanceof EObject) {
                                final EObject eobject = (EObject) object2;
                                if (structuralFeature.getDefaultValue() != value) {
                                    print(":id" + object.hashCode() + " " + prefix + ":" + structuralFeature.getName() + " :id" + eobject.hashCode() + " .");
                                }
                                traverse(eobject);
                            }
                        }
                    } else {
                        if (value instanceof EObject) {
                            if (structuralFeature.getDefaultValue() != value) {
                                print(":id" + object.hashCode() + " " + prefix + ":" + structuralFeature.getName() + " :id" + value.hashCode() + " .");
                            }
                            final EObject eo = (EObject) value;
                            traverse(eo);
                        } else {
                            print("#>>>>>>>>>> NOT A BasicEList and NOT a EObject");
                        }
                    }
                }
            }
        }
        indent--;
    }

    private String processXMLprefix(EClassifier clazz) {
        final String nsURI = clazz.getEPackage().getNsURI();
        final String prefix = clazz.getEPackage().getNsPrefix();
        final String uriMapped = prefixTOURIMap.get(prefix);
        if (uriMapped == null) {
            prefixTOURIMap.put(prefix, nsURI);
            print("@prefix " + prefix + ": <" + nsURI + "#> .");
        } else {
            if (!uriMapped.equals(nsURI)) {
                throw new RuntimeException("XMIToN3Converter.processXMLprefix:" + "redefine prefix not implemented");
            }
        }
        return prefix;
    }

    private void print(String s) {
        final char[] data = new char[indent];
        for (int i = 0; i < data.length; i++) {
            data[i] = ' ';
        }
        final String indentation = String.valueOf(data);
        printStream.println(indentation + s);
    }

    /** load eCore File
	 * cf "Saving and loading resources" in
	 * http://help.eclipse.org/help33/index.jsp?topic=/org.eclipse.emf.doc/references/overview/EMF.html
	 *  */
    public EList<EObject> loadEcoreFile(String ecoreFileName) {
        final URI uri = URI.createFileURI(ecoreFileName);
        final EList<EObject> contents = loadECoreURI(uri);
        return contents;
    }

    /**
	 * @param ecoreFileName
	 * @return
	 */
    public EList<EObject> loadECoreURI(URI uri) {
        final ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
        resourceSet.getPackageRegistry().put("http://www.eclipse.org/uml2/3.0.0/UML", UMLPackage.eINSTANCE);
        resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
        final Map<String, Object> extensionToFactoryMap = resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
        extensionToFactoryMap.put("ecore", new EcoreResourceFactoryImpl());
        extensionToFactoryMap.put("uml", new EcoreResourceFactoryImpl());
        extensionToFactoryMap.put("xmi", new EcoreResourceFactoryImpl());
        extensionToFactoryMap.put("mof", new EcoreResourceFactoryImpl());
        extensionToFactoryMap.put("emof", new EcoreResourceFactoryImpl());
        extensionToFactoryMap.put("cmof", new EcoreResourceFactoryImpl());
        EList<EObject> contents = null;
        try {
            final Resource resource = resourceSet.getResource(uri, true);
            print("# Loaded " + uri);
            if (doSaveEcoreContentAsEMOF) {
                resource.save(System.out, null);
            }
            contents = resource.getContents();
        } catch (final Exception exception) {
            print("Problem loading " + uri);
            exception.printStackTrace();
        }
        return contents;
    }

    public void saveEcoreContentAsEMOF(EList<EObject> contents, String outputFile) {
        final ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new EMOFResourceFactoryImpl());
        final URI uri = URI.createURI(outputFile);
        try {
            final Resource resource = resourceSet.createResource(uri);
            resource.getContents().addAll(contents);
            print("Created " + uri);
            resource.save(null);
            print("Saved " + uri);
        } catch (final Exception exception) {
            print("Problem processing " + uri);
            exception.printStackTrace();
        }
    }
}
