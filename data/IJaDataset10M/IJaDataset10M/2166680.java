package org.freelords.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.freelords.util.io.loaders.ResourceReference;
import org.freelords.util.io.loaders.Resources;
import org.freelords.util.io.loaders.VirtualPath;
import org.freelords.xml.standard.XMLBooleanHandler;
import org.freelords.xml.standard.XMLCollectionHandler;
import org.freelords.xml.standard.XMLEnumHandler;
import org.freelords.xml.standard.XMLIntArrayHandler;
import org.freelords.xml.standard.XMLIntegerHandler;
import org.freelords.xml.standard.XMLMapHandler;
import org.freelords.xml.standard.XMLStringHandler;
import org.freelords.xml.standard.XMLVirtualPathHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class XMLHelper {

    /** A provider of the injector (at the construction time the injector is not yet known. So we can get the right instances */
    private Provider<Injector> injector;

    /** The path of the program resources */
    private Resources loader;

    /** Transforms xml to Document */
    private DocumentBuilder db;

    /** List of demarshallers for certain classes */
    private Map<Class<?>, XMLDemarshaller<?>> demarshall = new HashMap<Class<?>, XMLDemarshaller<?>>();

    /** List of marshallers for certain classes */
    private Map<Class<?>, XMLMarshaller<?>> marshall = new HashMap<Class<?>, XMLMarshaller<?>>();

    /** Cache of loaded items */
    private Cache cache = new Cache();

    /** Formats and writes xml */
    private Transformer trafo;

    /** Returns the injector */
    public Injector getInjector() {
        if (injector == null) {
            return null;
        }
        return injector.get();
    }

    /** Used by tests */
    public void enablePrettyOutput() {
        trafo.setOutputProperty(OutputKeys.INDENT, "yes");
        trafo.setOutputProperty(OutputKeys.METHOD, "xml");
    }

    /** Inits the cache with a certain (regex) pattern and cache size */
    public void cacheContents(Pattern pattern, int size) {
        cache.cache(pattern, size);
    }

    private Map<TypeVariable<?>, Type> typeMap = new HashMap<TypeVariable<?>, Type>();

    protected void mapTypeVariable(TypeVariable<?> typeVariable, Type actualType) {
        typeMap.put(typeVariable, actualType);
    }

    /** Creates an instance.
     *
     * @param loader    A Resources instance that can be used to resolve
     * filenames given as virtual paths into actual input/output streams.
     * See {@link org.freelords.util.io.loaders} for the details.
     */
    public XMLHelper(Resources loader, Provider<Injector> injectorProvider) throws ParserConfigurationException {
        this.loader = loader;
        this.injector = injectorProvider;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        db = dbf.newDocumentBuilder();
        addHandler(Integer.TYPE, new XMLIntegerHandler());
        addHandler(Integer.class, new XMLIntegerHandler());
        addHandler(String.class, new XMLStringHandler());
        addHandler(int[].class, new XMLIntArrayHandler());
        addHandler(Map.class, new XMLMapHandler());
        addHandler(EnumMap.class, new XMLMapHandler());
        addDemarshall(VirtualPath.class, new XMLVirtualPathHandler());
        addHandler(Set.class, new XMLCollectionHandler());
        addHandler(List.class, new XMLCollectionHandler());
        addHandler(Collection.class, new XMLCollectionHandler());
        addHandler(Boolean.class, new XMLBooleanHandler());
        addHandler(Boolean.TYPE, new XMLBooleanHandler());
        addHandler(Object.class, XMLAnnotatedHandlerFactory.XML_CONSTRUCTABLE_HANDLER);
        addHandler(Enum.class, new XMLEnumHandler());
        try {
            trafo = TransformerFactory.newInstance().newTransformer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Returns the Resources object of this XMLHelper instance */
    public Resources getResources() {
        return loader;
    }

    /** Add a new demarshalling object to the map.
     *
     * Extends the knowledge of the XMLHelper object about mapping between
     * classes and objects to demarshall the class.
     *
     * @param clazz   The class type for which the demarshaller should be used.
     * @param demarsh The demarshaller that should be used to convert XML into
     * an object of type clazz.
     */
    public <E> void addDemarshall(Class<E> clazz, XMLDemarshaller<? super E> demarsh) {
        demarshall.put(clazz, demarsh);
    }

    /** Add a new marshalling object to the map.
     *
     * Extends the knowledge of the XMLHelper object about mapping between
     * classes and objects to marshall the class.
     *
     * @param clazz   The class type for which the marshaller should be used.
     * @param marsh   The marshaller that should be used to convert a given
     * object into XML code.
     */
    public <E> void addMarshall(Class<E> clazz, XMLMarshaller<? super E> marsh) {
        marshall.put(clazz, marsh);
    }

    /** Adds a new handler (marshaller <i>and</i> demarshaller) to the map. */
    public <E> void addHandler(Class<E> clazz, XMLHandler<? super E> handler) {
        demarshall.put(clazz, handler);
        marshall.put(clazz, handler);
    }

    @SuppressWarnings("unchecked")
    public <E> E demarshallMandatory(VirtualPath currentFile, Class<E> clazz) {
        E e = (E) demarshall(currentFile, (Type) clazz);
        if (e == null) {
            throw new IllegalArgumentException(currentFile + " does not exist.");
        }
        return e;
    }

    /** */
    private static final Map<String, Object> noAddPar = Collections.emptyMap();

    /** A convenience method that will return an object of the desired type already cast
     * @param currentFile A VirtualPath that should hook up to a InputStream containing xml
     * @param clazz The clazz to demarshall into
     * @return an instance of the desired type.
     */
    @SuppressWarnings("unchecked")
    public <E> E demarshall(VirtualPath currentFile, Class<E> clazz, Map<String, Object> addPar) {
        return (E) demarshall(currentFile, (Type) clazz, addPar);
    }

    /** Variant with varargs, structure is 'identifier', object, '...'
     */
    @SuppressWarnings("unchecked")
    public <E> E demarshall(VirtualPath currentFile, Class<E> clazz, String id, Object... valueid) {
        if (valueid.length % 2 != 1) {
            throw new IllegalArgumentException("Length of additional parameter list must be even!");
        }
        Map<String, Object> addPar = new HashMap<String, Object>();
        addPar.put(id, valueid[0]);
        for (int i = 0; i < (valueid.length - 1) / 2; i++) {
            if (!(valueid[i * 2 + 1] instanceof String)) {
                throw new IllegalArgumentException("Identifier in parameter list is not a String!");
            }
            addPar.put((String) valueid[i * 2 + 1], valueid[i * 2 + 2]);
        }
        return (E) demarshall(currentFile, (Type) clazz, addPar);
    }

    /** Variant without everything
     */
    @SuppressWarnings("unchecked")
    public <E> E demarshall(VirtualPath currentFile, Class<E> clazz) {
        return (E) demarshall(currentFile, (Type) clazz, noAddPar);
    }

    /** Variant without everything
    */
    public Object demarshall(VirtualPath currentFile, Type exactType) {
        return demarshall(currentFile, exactType, noAddPar);
    }

    /** More generic form of loading some object from XML.
     *
     * @param currentFile A VirtualPath that should hook up to a InputStream containing xml
     * @param exactType   the exact type of the object we wish to load from the
     * stream.
     * @return an instance of the given type as an Object.
     * @throws RuntimeException on any errors.
     */
    @SuppressWarnings("unchecked")
    public Object demarshall(VirtualPath currentFile, Type exactType, Map<String, Object> addPar) {
        Object response = cache.get(currentFile);
        if (response != null) {
            return response;
        }
        try {
            Document doc;
            if (currentFile.getName().endsWith(".xml")) {
                ResourceReference rr = loader.getResource(currentFile);
                if (rr == null) {
                    return null;
                }
                InputStream is = rr.getInputStream();
                try {
                    doc = db.parse(is);
                } finally {
                    is.close();
                }
            } else {
                doc = null;
            }
            Object obj = demarshall(currentFile, doc == null ? null : doc.getFirstChild(), exactType, addPar);
            if (obj != null) {
                XMLDemarshaller<?> demarsh = getDemarshaller(exactType);
                return cache.put(currentFile, obj, demarsh instanceof CacheAware<?> ? (CacheAware) demarsh : null);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("When demarshalling " + currentFile, e);
        }
    }

    /** Converts a Type instance into a Class instance.
     *
     * Does proper casts, finds lists, maps etc.
     * @param exactType   The type to convert.
     */
    @SuppressWarnings("unchecked")
    protected Class getClassFromType(Type exactType) {
        Class clazz = null;
        if (exactType instanceof TypeVariable) {
            exactType = typeMap.get(exactType);
        }
        if (exactType instanceof Class) {
            clazz = (Class) exactType;
        } else if (exactType instanceof ParameterizedType) {
            clazz = (Class) ((ParameterizedType) exactType).getRawType();
        } else if (exactType instanceof GenericArrayType) {
            if (Integer.TYPE.equals(((GenericArrayType) exactType).getGenericComponentType())) {
                clazz = int[].class;
            }
        }
        return clazz;
    }

    /** Returns the proper demarshaller for a given type.
     *
     * If it does not know how to handle the type and if the type is no
     * array/list/map, it continues with the superclass until it does find
     * a proper demarshaller (the last thing is the
     * XMLAnnotatedDemarshallerFactory for the Object class).
     *
     * @param exactType  the type to get the demarshaller for.
     * @return a demarshaller to load this type from XML.
     */
    @SuppressWarnings("unchecked")
    public XMLDemarshaller getDemarshaller(Type exactType) {
        if (exactType instanceof TypeVariable<?>) {
            exactType = typeMap.get(exactType);
        }
        Class clazz = getClassFromType(exactType);
        if (clazz == null) {
            throw new IllegalArgumentException("Can not handle type of " + exactType);
        }
        while (true) {
            XMLDemarshaller demarsh = demarshall.get(clazz);
            if (demarsh != null) {
                return demarsh;
            }
            clazz = clazz.getSuperclass();
            if (clazz == null) {
                throw new IllegalArgumentException("There is no demarshaller for " + exactType);
            }
        }
    }

    /** Returns the proper marshaller for a given type.
     *
     * If it does not know how to handle the type and if the type is no
     * array/list/map, it continues with the superclass until it does find
     * a proper marshaller (the last one is the
     * XMLAnnotatedDemarshallerFactory for the Object class).
     *
     * @param exactType  the type to get the marshaller for.
     * @return a marshaller to load this type from XML.
     */
    @SuppressWarnings("unchecked")
    public XMLMarshaller getMarshaller(Type exactType) {
        Class clazz = getClassFromType(exactType);
        if (clazz == null) {
            throw new IllegalArgumentException("Can not handle type of " + exactType);
        }
        while (true) {
            XMLMarshaller marsh = marshall.get(clazz);
            if (marsh != null) {
                return marsh;
            }
            clazz = clazz.getSuperclass();
            if (clazz == null) {
                throw new IllegalArgumentException("There is no demarshaller for " + exactType);
            }
        }
    }

    /** Turns some XML into a valid object.
     *
     * @param currentFile   the XML file we treat
     * @param node          the node that encodes the current object
     * @param exactType     the exact type of the object to load
     */
    public Object demarshall(VirtualPath currentFile, Node node, Type exactType, Map<String, Object> addPar) {
        XMLDemarshaller<?> demarsh = getDemarshaller(exactType);
        if (node != null && node.getAttributes() != null) {
            Node attr = node.getAttributes().getNamedItem("ref");
            if (attr != null) {
                String ref = attr.getNodeValue();
                return demarshall(new VirtualPath(currentFile.getParent(), ref), exactType, addPar);
            }
        }
        Object obj = demarsh.demarshall(this, currentFile, node, exactType, addPar);
        if (obj != null) {
            Injector injector = getInjector();
            if (injector != null) {
                injector.injectMembers(obj);
            }
        }
        return obj;
    }

    /** Marshalls a given object and write the result to an output stream.
     * @throws IOException */
    public void marshall(OutputStream out, String name, Type exactType, Object obj) throws IOException, TransformerException {
        Document doco = db.newDocument();
        Node node = marshall(doco, name, false, exactType, obj);
        doco.appendChild(node);
        trafo.transform(new DOMSource(doco), new StreamResult(out));
    }

    /** Marshalls a given object and write to a file specified by vp */
    public void marshall(VirtualPath vp, String name, Type exactType, Object obj) throws IOException, TransformerException {
        OutputStream out = loader.createChild(vp);
        marshall(out, name, exactType, obj);
        out.close();
    }

    /** Marshalls some object into an XML document. @see XMLMarshaller */
    @SuppressWarnings("unchecked")
    public Node marshall(Document doco, String name, boolean attribute, Type exactType, Object obj) {
        return getMarshaller(exactType).marshall(this, doco, name, attribute, exactType, obj);
    }

    /** Try to load all xml files in a certain folder.
     *
     * The xml files need to have the same extension and be of the same type.
     *
     * @param clazz the type of the object to demarshall
     * @param extension the suffix of the xml files that we are loading
     * @param folder the folder where to look for files (no descending!)
     * @return a list of objects that we managed to load.
     */
    public <E> List<E> getAll(Class<E> clazz, String extension, VirtualPath folder) {
        if (extension != null) {
            extension = "." + extension;
        }
        List<VirtualPath> files = loader.getChildren(folder);
        List<E> resources = new ArrayList<E>();
        for (VirtualPath vp : files) {
            if (extension != null && !vp.getName().endsWith(extension)) {
                continue;
            }
            resources.add(demarshall(vp, clazz));
        }
        return resources;
    }

    /** Automatically demarshalls all files in a directory with a given extension.
     *
     * This function is used as a shortcut for loading a bunch of descriptions at once.
     * As an example, the army recruitment info is loaded like this.
     *
     * @param exactType the type that we convert the xml files into.
     * @param extension if given, only files with this extension are demarshalled.
     * @param folder the directory (or whatever, a VirtualPath can also be e.g. a zip file)
     * where we look for files to convert.
     */
    public List<Object> getAll(Type exactType, String extension, VirtualPath folder) {
        if (extension != null) {
            extension = "." + extension;
        }
        List<VirtualPath> files = loader.getChildren(folder);
        List<Object> resources = new ArrayList<Object>();
        for (VirtualPath vp : files) {
            if (extension != null && !vp.getName().endsWith(extension)) {
                continue;
            }
            resources.add(demarshall(vp, exactType));
        }
        return resources;
    }
}
