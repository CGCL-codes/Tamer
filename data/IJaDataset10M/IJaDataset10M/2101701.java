package ch.sahits.codegen.extensions;

import java.util.Iterator;
import java.util.Vector;

/**
 * This collection holds all the information retrieved by the
 * extension point 'ch.sahits.codegen.generator'
 * 
 * This class is package private and only its interface {@link IGeneratorCollection}
 * is exported. The {@link Generator} class functions as a factory for this object.
 * @author Andi Hotz
 * @since 2.1.0
 */
class GeneralGeneratorCollection extends GeneratorCollection implements IGeneralGeneratorCollection {

    /** List that holds all the generators */
    private Vector<IGeneralGeneratorExtension> list = new Vector<IGeneralGeneratorExtension>();

    /**
	 * @param bundleName
	 * @param fileExtension
	 * @param generatorClass
	 * @param display 
	 * @param withDB indicating if the database information in needed for the model
	 * @param inputFileExtension file extension of the input file
	 * @param withXML indicating if the input file is XML
	 * @param xmlParser XML parser, mandatory if withXML is true
	 * @param contextID help context ID may be null
	 */
    @SuppressWarnings("unchecked")
    public void addGeneratorClass(String bundleName, String fileExtension, Class generatorClass, String display, boolean withDB, String inputFileExtension, boolean withXML, Class<?> xmlParser, String contextID) {
        list.add(new ClassGenratorExtension(bundleName, fileExtension, generatorClass, display, withDB, inputFileExtension, withXML, xmlParser, contextID));
    }

    /**
	 * @param bundleName
	 * @param fileExtension
	 * @param jetemplateLocation
	 * @param display 
	 * @param withDB indicating if the database information in needed for the model
	 * @param inputFileExtension file extension of the input file
	 * @param withXML indicating if the input file is XML
	 * @param xmlParser XML parser, mandatory if withXML is true
	 * @param contextID help context ID may be null
	 */
    public void addJetemplate(String bundleName, String fileExtension, String jetemplateLocation, String display, boolean withDB, String inputFileExtension, boolean withXML, Class<?> xmlParser, String contextID) {
        list.add(new JetGeneratorExtension(bundleName, fileExtension, jetemplateLocation, display, withDB, inputFileExtension, withXML, xmlParser, contextID));
    }

    /**
	 * @see ch.sahits.codegen.extensions.IGeneratorCollection#iterator()
	 */
    @SuppressWarnings("unchecked")
    public Iterator iterator() {
        return list.iterator();
    }

    /**
	 * @see ch.sahits.codegen.extensions.IGeneratorCollection#getExtension(java.lang.String, java.lang.String)
	 */
    public IGeneralGeneratorExtension getExtension(String bundle, String generator) {
        for (Iterator<IGeneralGeneratorExtension> iterator = list.iterator(); iterator.hasNext(); ) {
            IGeneralGeneratorExtension ext = iterator.next();
            if (ext.getBundleName().equals(bundle)) {
                if (ext instanceof IJetGeneratorExtension) {
                    if (((IJetGeneratorExtension) ext).getJetLoacation().equals(generator)) {
                        return ext;
                    }
                } else if (ext instanceof IClassGeneratorExtension) {
                    if (((IClassGeneratorExtension) ext).getClassName().equals(generator)) {
                        return ext;
                    }
                } else {
                    throw new RuntimeException("A " + IGeneratorExtension.class.getName() + " must either be of instance " + IJetGeneratorExtension.class.getName() + " or " + IClassGeneratorExtension.class.getName());
                }
            }
        }
        return null;
    }
}
