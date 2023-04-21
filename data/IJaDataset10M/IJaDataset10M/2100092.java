package skycastle.util.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import skycastle.util.listenable.collection.list.ListenableArrayList;
import skycastle.util.listenable.collection.set.ListenableLinkedSet;
import skycastle.util.listenable.map.ListenableLinkedMap;
import java.io.*;

/**
 * XML loading and saving related utilities.
 *
 * @author Hans H�ggstr�m
 */
public class XmlUtils {

    private static XStream theXStream = null;

    /**
     * Saves the specified object to the specified file.
     * Uses XStram for the xml generation.
     */
    public static void save(Object objectToSave, File fileToSaveOver) throws IOException {
        save(objectToSave, fileToSaveOver, getXStream());
    }

    /**
     * Saves the specified object to the specified file.
     * Uses XStram for the xml generation.
     *
     * @param xStream the XStream instance to use.  Useful if some custom aliasing or configuration needs to be done.
     */
    public static void save(Object objectToSave, File fileToSaveOver, XStream xStream) throws IOException {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(fileToSaveOver));
            xStream.toXML(objectToSave, bufferedWriter);
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }

    /**
     * Loads the specified xml file and returns it as an object.
     * Uses XStram for the xml unmarshalling.
     *
     * @param xmlFileToLoad the file to load and return as object
     *
     * @return the loaded object
     *
     * @throws IOException if there was problem in the loading or unmarshalling
     */
    public static Object load(File xmlFileToLoad) throws IOException {
        return load(xmlFileToLoad, getXStream());
    }

    /**
     * Loads the specified xml file and returns it as an object.
     * Uses XStram for the xml unmarshalling.
     *
     * @param xmlFileToLoad the file to load and return as object
     * @param xStream       the XStream instance to use.  Useful if some custom aliasing or configuration needs to be done.
     *
     * @return the loaded object
     *
     * @throws IOException if there was problem in the loading or unmarshalling
     */
    public static Object load(File xmlFileToLoad, XStream xStream) throws IOException {
        BufferedReader bufferedReader = null;
        Object loadedObject;
        try {
            bufferedReader = new BufferedReader(new FileReader(xmlFileToLoad));
            loadedObject = xStream.fromXML(bufferedReader);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return loadedObject;
    }

    /**
     * @return a singleton XStream instance.
     */
    public static XStream getXStream() {
        synchronized (XmlUtils.class) {
            if (theXStream == null) {
                theXStream = createFieldPrefixHandlingXStream();
            }
            return theXStream;
        }
    }

    /**
     * @return a customized XStream that handles fields starting with myFoo or _foo correctly, so that the
     *         xml contains only <foo> in the element.
     */
    public static XStream createFieldPrefixHandlingXStream() {
        final XStream xStream = new XStream() {

            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new FieldPrefixStrippingMapper(next);
            }
        };
        xStream.alias("ListenableArrayList", ListenableArrayList.class);
        xStream.alias("ListenableLinkedSet", ListenableLinkedSet.class);
        xStream.alias("ListenableLinkedMap", ListenableLinkedMap.class);
        return xStream;
    }

    /**
     * Creates a new XmlUtils.
     */
    private XmlUtils() {
    }
}
