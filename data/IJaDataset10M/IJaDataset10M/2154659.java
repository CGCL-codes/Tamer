package net.sourceforge.olympos.oaw.extend;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.openarchitectureware.type.MetaModel;
import org.openarchitectureware.uml2.UML2MetaModel;
import org.openarchitectureware.uml2.profile.ProfileMetaModel;
import org.openarchitectureware.workflow.WorkflowRunner;
import org.openarchitectureware.workflow.monitor.NullProgressMonitor;
import org.openarchitectureware.xtend.XtendFacade;

/**
 * This is the main generator class
 * 
 * @author ingo herwig <ingo@wemove.com>
 */
public class Generator {

    /**
	 * Keywords used to mark elements
	 */
    private static final String GENERATOR_ADDED = "generatorAdded";

    private static final String GENERATOR_INHERITED = "generatorInherited";

    private static final String PK_VALUE = "PK";

    private static final String FK_VALUE = "FK";

    private static Map<Property, org.eclipse.uml2.uml.Class> fkRegistry = new Hashtable<Property, org.eclipse.uml2.uml.Class>();

    /**
	 * Application main
	 * 
	 * @param arguments
	 */
    @SuppressWarnings("unchecked")
    public static void main(String[] arguments) {
        if (arguments.length == 0) printUsage(); else {
            String wfFile = arguments[0];
            try {
                Map properties = new HashMap();
                Map slotContents = new HashMap();
                try {
                    String genClass = '/' + Generator.class.getName().replace('.', '/') + ".class";
                    URL genClassURL = Generator.class.getResource(genClass);
                    String prefix = "";
                    if ("jar" == genClassURL.getProtocol()) {
                        prefix = genClassURL.toExternalForm();
                        prefix = prefix.substring(0, prefix.lastIndexOf("!/") + 2);
                    }
                    JarURLConnection conn = (JarURLConnection) new URL(prefix).openConnection();
                    JarFile jarfile = conn.getJarFile();
                    Manifest manifest = jarfile.getManifest();
                    if (manifest != null) {
                        String version = "";
                        Attributes attrs = (Attributes) manifest.getMainAttributes();
                        for (Iterator it = attrs.keySet().iterator(); it.hasNext(); ) {
                            Attributes.Name attrName = (Attributes.Name) it.next();
                            if ("ChronosGenerator-Version" == attrName.toString()) version = attrs.getValue(attrName);
                        }
                        properties.put("generatorVersion", version);
                        Logger.info("Running ChronosGenerator " + version + " ...");
                    }
                } catch (Exception e) {
                }
                if (arguments.length > 1) {
                    for (int i = 1; i < arguments.length; i++) {
                        String[] kvp = arguments[i].split("=");
                        String property = kvp[0].substring(1);
                        String value = kvp[1];
                        properties.put(property, value);
                    }
                }
                new WorkflowRunner().run(wfFile, new NullProgressMonitor(), properties, slotContents);
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(5);
            }
        }
    }

    /**
	 * Print the usage message
	 */
    public static void printUsage() {
        System.out.println("Generator Usage: ");
        System.out.println("java -jar ChronosGenerator.jar workflow.oaw");
    }

    /**
	 * Mark a Element as added by the generator.
	 * 
	 * @param e
	 */
    public static void setGeneratorAdded(Element e) {
        e.addKeyword(GENERATOR_ADDED);
    }

    /**
	 * Check if an Element as added by the generator.
	 * 
	 * @param e
	 * @return Boolean
	 */
    public static boolean isGeneratorAdded(Element e) {
        return e.hasKeyword(GENERATOR_ADDED);
    }

    /**
	 * Mark a Element as inherited by the generator.
	 * 
	 * @param e
	 */
    public static void setGeneratorInherited(Element e) {
        e.addKeyword(GENERATOR_INHERITED);
    }

    /**
	 * Check if an Element as inherited by the generator.
	 * 
	 * @param e
	 * @return Boolean
	 */
    public static boolean isGeneratorInherited(Element e) {
        return e.hasKeyword(GENERATOR_INHERITED);
    }

    /**
	 * Mark a Property as a primary key value.
	 * 
	 * @param p
	 */
    public static void setPKValue(Property p) {
        p.addKeyword(PK_VALUE);
    }

    /**
	 * Check if a Property as a primary key value.
	 * 
	 * @param p
	 * @return Boolean
	 */
    public static boolean isPKValue(Property p) {
        return p.hasKeyword(PK_VALUE);
    }

    /**
	 * Mark a Property as a foreign key value.
	 * 
	 * @param p
	 * @param clazz
	 *            The referenced class
	 */
    public static void setFKValue(Property p, org.eclipse.uml2.uml.Class clazz) {
        p.addKeyword(FK_VALUE);
        fkRegistry.put(p, clazz);
    }

    /**
	 * Check if a Property as a foreign key value.
	 * 
	 * @param p
	 * @return Boolean
	 */
    public static boolean isFKValue(Property p) {
        return p.hasKeyword(FK_VALUE);
    }

    /**
	 * Get the referenced class for a foreign key value.
	 * 
	 * @param p
	 * @return org.eclipse.uml2.uml.Class
	 */
    public static org.eclipse.uml2.uml.Class getFKClass(Property p) {
        if (p.hasKeyword(FK_VALUE)) {
            return fkRegistry.get(p);
        }
        return null;
    }

    /**
	 * Create a XtendFacade to call methods from extension files.
	 * 
	 * @param xTendFile
	 *            The full qualified template name without extension. e.g.
	 *            "templates::extensions::naming"
	 * @param usedProfiles
	 *            An array of Profile instances that are used in the extension
	 *            (uml2 meta model is added by default)
	 * @return An instance of a XtendFacade
	 */
    public static XtendFacade createXtendFacade(String xTendFile, Profile[] usedProfiles) {
        XtendFacade f = XtendFacade.create(xTendFile);
        MetaModel uml2MM = new UML2MetaModel();
        f.registerMetaModel(uml2MM);
        for (int i = 0; i < usedProfiles.length; i++) {
            MetaModel mm = new ProfileMetaModel(usedProfiles[i]);
            f.registerMetaModel(mm);
        }
        return f;
    }
}
