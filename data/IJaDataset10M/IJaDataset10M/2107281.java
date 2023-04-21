package uk.ac.imperial.ma.metric.exercises.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import uk.ac.imperial.ma.metric.computerese.impl1.MetricProcessingInstructionTranslatorImpl;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseDevelopmentStatus;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.xmlutils.ProcessingInstructionTranslatorException;

/**
 * Generates the <code>exercisesMetadata.xml</code> file.
 * 
 * @author <a href="mailto:mail@daniel.may.name">Daniel J. R. May</a>
 * @version 0.2, 4 November 2008
 */
public class BlackboardExerciseFilesGenerator {

    private static final String TITLE = "Exercises Metadata Generator";

    private static final String AUTHOR = "Daniel J. R. May (mail@daniel.may.name)";

    private static final String VERSION = "0.1, 27 November 2008";

    private static final String DESCRIPTION = "Generates the exercisesMetadata.xml file by inspecting all exercises class files.";

    public static final String METRIC_NAMESPACE_URI = "http://metric.ma.imperial.ac.uk/";

    public static final String ROOT_ELEMENT = "exercisesMetadata";

    private static final String EXERCISES_JAR_FILE_PATH = "/home/dan/.m2/repository/uk/ac/imperial/ma/metric/exercises/0.1-SNAPSHOT/exercises-0.1-SNAPSHOT.jar";

    private static final String METADATA_FILE = "src/main/exercisesMetadata.xml";

    private static Document doc;

    private static MetricProcessingInstructionTranslatorImpl mpit;

    /**
	 * Main method; it all starts from here!
	 * 
	 * @param args
	 *            these are ignored.
	 */
    public static void main(String[] args) {
        try {
            System.out.println(TITLE + "\n\nAuthor: " + AUTHOR + "\nVersion: " + VERSION + "\n\nDescription: " + DESCRIPTION + "\n");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            DOMImplementation domImpl = db.getDOMImplementation();
            doc = domImpl.createDocument(METRIC_NAMESPACE_URI, ROOT_ELEMENT, null);
            mpit = new MetricProcessingInstructionTranslatorImpl(doc);
            JarFile exercisesJar = new JarFile(EXERCISES_JAR_FILE_PATH);
            Enumeration<JarEntry> entries = exercisesJar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (isExerciseClassEntry(entryName)) {
                    String className = getExerciseClassName(entryName);
                    try {
                        Class exerciseClass = Class.forName(className);
                        processClass(exerciseClass);
                    } catch (ClassNotFoundException cnfe) {
                        System.err.println("Class not found " + cnfe.getMessage());
                    }
                }
            }
            File metadataFile = new File(METADATA_FILE);
            FileWriter metadataFileWriter = new FileWriter(metadataFile);
            DOMImplementationLS domLsImpl = (DOMImplementationLS) domImpl;
            LSOutput output = domLsImpl.createLSOutput();
            output.setCharacterStream(metadataFileWriter);
            LSSerializer serializer = domLsImpl.createLSSerializer();
            DOMConfiguration domConfig = serializer.getDomConfig();
            domConfig.setParameter("format-pretty-print", true);
            serializer.write(doc, output);
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
            System.exit(1);
        } catch (ParserConfigurationException pce) {
            System.err.println(pce.getMessage());
            pce.printStackTrace();
            System.exit(2);
        } catch (ProcessingInstructionTranslatorException pite) {
            System.err.println(pite.getMessage());
            pite.printStackTrace();
            System.exit(3);
        }
        System.out.println("\nFinished\n");
        System.exit(0);
    }

    /**
	 * Test whether this entry corresponds to a exercise class.
	 * 
	 * @param entryName
	 *            the entry name to test.
	 * @return <code>true</code> if it matches the scheme of an exercise class
	 *         name; <code>false</code> otherwise.
	 */
    private static boolean isExerciseClassEntry(String entryName) {
        if (entryName.endsWith(".class") && entryName.startsWith("uk/ac/imperial/ma/metric/exercises")) {
            System.out.println("Found jar entry " + entryName);
            return true;
        } else {
            System.out.println("Ignoring jar entry " + entryName);
            return false;
        }
    }

    /**
	 * Convert the jar entry name into a canonical class name.
	 * 
	 * @param entryName
	 *            the entry name to convert.
	 * @return a canonical class name.
	 */
    private static String getExerciseClassName(String entryName) {
        String classNameWithSlashes = entryName.substring(0, entryName.length() - 6);
        String className = classNameWithSlashes.replace('/', '.');
        return className;
    }

    /**
	 * Gather information about an exercise class.
	 * 
	 * @param exerciseClass
	 *            the exercise class to be processed.
	 * @throws ProcessingInstructionTranslatorException 
	 */
    private static void processClass(final Class<ExerciseInterface> exerciseClass) throws ProcessingInstructionTranslatorException {
        String className = exerciseClass.getName();
        System.out.println("Processing " + className);
        String title = "UNDEFINED";
        try {
            title = (String) exerciseClass.getField("TITLE").get(null);
        } catch (NoSuchFieldException nsfe) {
            title = "untitled";
            System.err.println("No TITLE field defined in " + exerciseClass);
        } catch (IllegalAccessException iae) {
            System.err.println(iae.getMessage());
        }
        String description = "UNDEFINED";
        try {
            description = (String) exerciseClass.getField("DESCRIPTION").get(null);
        } catch (NoSuchFieldException nsfe) {
            description = "undescribed";
            System.err.println("No DESCRIPTION field defined in " + exerciseClass);
        } catch (IllegalAccessException iae) {
            System.err.println(iae.getMessage());
        }
        String developmentStatus = "UNDEFINED";
        try {
            ExerciseDevelopmentStatus eds = (ExerciseDevelopmentStatus) exerciseClass.getField("STATUS").get(null);
            developmentStatus = eds.getClassAttrValue();
        } catch (NoSuchFieldException nsfe) {
            developmentStatus = "untested";
        } catch (IllegalAccessException iae) {
            System.err.println(iae.getMessage());
        }
        String type = "UNDEFINED";
        try {
            ExerciseInterface instance = (ExerciseInterface) exerciseClass.newInstance();
            short t = instance.getExerciseType();
            switch(t) {
                case ExerciseInterface.FREE_FORM_TYPE_1:
                    type = "FREE_FORM_TYPE_1";
                    break;
                case ExerciseInterface.GRAPHICAL_EXERCISE_TYPE_1:
                    type = "GRAPICAL_EXERCISE_TYPE_1";
                    break;
                case ExerciseInterface.MULTIPLE_CHOICE_GRAPHICAL_EXERCISE_TYPE_1:
                    type = "MULTIPLE_CHOICE_GRAPHICAL_EXERCISE_TYPE_1";
                    break;
                case ExerciseInterface.MULTIPLE_CHOICE_TYPE_1:
                    type = "MULTIPLE_CHOICE_TYPE_1";
                    break;
                default:
                    type = "UNKOWN_TYPE";
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException iae) {
            System.err.println(iae.getMessage());
        }
        Element exerciseElement = doc.createElementNS(METRIC_NAMESPACE_URI, "exercise");
        exerciseElement.setAttribute("javaclass", className);
        exerciseElement.setAttribute("status", developmentStatus);
        DocumentFragment titleDocFrag = mpit.translate(title, METRIC_NAMESPACE_URI, "title");
        exerciseElement.appendChild(titleDocFrag);
        DocumentFragment descriptionDocFrag = mpit.translate(description, METRIC_NAMESPACE_URI, "description");
        exerciseElement.appendChild(descriptionDocFrag);
        doc.getDocumentElement().appendChild(exerciseElement);
    }
}
