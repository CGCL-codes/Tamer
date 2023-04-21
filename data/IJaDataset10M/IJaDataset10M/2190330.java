package net.sf.traser.common;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import net.sf.traser.common.exceptions.NotInitializable;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

/**
 * Utility class that initializes an Initializable object of class specified in 
 * the <code>class</code> attribute of the root node of the provided XML tree.
 * @author Marcell Szathmari
 */
public final class InitializerUtility {

    /**
     * Attribute name holding class information about a component.
     */
    public static final QName CLASSNAME = new QName(TraserConstants.DEFAULT_NAMESPACE, "class");

    /**
     * This field is used to log messages.
     */
    private static final Logger LOG = Logger.getLogger(InitializerUtility.class.getName());

    /** Creates a new instance of InitializerUtility. */
    private InitializerUtility() {
        LOG.finest("Initializer Utility Instantiated");
    }

    /**
     * Returns an Initializable object from the XML tree located in 
     * <code>file</code>.
     * @param file the file containing the description
     * @return the Initializable object loaded
     * @throws net.sf.traser.common.exceptions.NotInitializable if the 
     * initilization was not successful
     * @throws java.io.FileNotFoundException if the file cannot be opened
     */
    public static Initializable getComponent(File file) throws NotInitializable, FileNotFoundException {
        return InitializerUtility.getComponent(new FileInputStream(file));
    }

    /**
     * Returns an Initializable object from the XML tree located in the file
     * named <code>fileName</code>.
     * @param fileName the name of the file containing the description
     * @return the Initializable object loaded
     * @throws net.sf.traser.common.exceptions.NotInitializable if the 
     * initilization was not successful
     * @throws java.io.FileNotFoundException if the file cannot be opened
     */
    public static Initializable getComponent(String fileName) throws NotInitializable, FileNotFoundException {
        return InitializerUtility.getComponent(new FileInputStream(fileName));
    }

    /**
     * Returns an Initializable object from the XML tree read from the stream
     * <code>stream</code>.
     * @param stream the stream to read the description from
     * @return the Initializable object loaded
     * @throws net.sf.traser.common.exceptions.NotInitializable if the 
     * initilization was not successful
     */
    public static Initializable getComponent(InputStream stream) throws NotInitializable {
        try {
            StAXOMBuilder bld = new StAXOMBuilder(stream);
            OMElement doc = bld.getDocumentElement();
            return InitializerUtility.getObject(doc);
        } catch (XMLStreamException ex) {
            throw new NotInitializable("Could not load definition XML!", ex);
        }
    }

    /**
     * Creates an instance of the class specified in the root element of the XML
     * tree.
     * @param config the description XML document
     * @return the Initializable object loaded
     * @throws net.sf.traser.common.exceptions.NotInitializable if the 
     * initilization was not successful
     */
    public static Initializable getObject(OMElement config) throws NotInitializable {
        String className = config.getAttributeValue(CLASSNAME);
        if (className == null) {
            throw new NotInitializable("No class attribute was specified!");
        }
        try {
            Class<?> cl = Class.forName(className);
            Constructor<?> cons = cl.getConstructor(new Class[] {});
            Initializable obj = (Initializable) cons.newInstance(new Object[] {});
            obj.init(config);
            return obj;
        } catch (InstantiationException ex) {
            throw new NotInitializable("Instantiation failed!", ex);
        } catch (IllegalAccessException ex) {
            throw new NotInitializable("Specified class has no public constructors!", ex);
        } catch (IllegalArgumentException ex) {
            throw new NotInitializable(ex);
        } catch (InvocationTargetException ex) {
            throw new NotInitializable(ex);
        } catch (ClassNotFoundException ex) {
            throw new NotInitializable("Specified class could not be loaded!", ex);
        } catch (NoSuchMethodException noSuchMethodException) {
            throw new NotInitializable("Specified class is not default constructible!", noSuchMethodException);
        }
    }
}
