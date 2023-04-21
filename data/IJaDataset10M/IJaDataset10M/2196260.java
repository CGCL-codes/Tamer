package org.apache.ws.jaxme.generator.sg.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.ws.jaxme.XMLWriter;
import org.apache.ws.jaxme.generator.sg.ComplexContentSG;
import org.apache.ws.jaxme.generator.sg.ComplexTypeSG;
import org.apache.ws.jaxme.generator.sg.Context;
import org.apache.ws.jaxme.generator.sg.GroupSG;
import org.apache.ws.jaxme.generator.sg.ObjectSG;
import org.apache.ws.jaxme.generator.sg.ParticleSG;
import org.apache.ws.jaxme.generator.sg.SGFactory;
import org.apache.ws.jaxme.generator.sg.SchemaSG;
import org.apache.ws.jaxme.generator.sg.SchemaSGChain;
import org.apache.ws.jaxme.generator.sg.TypeSG;
import org.apache.ws.jaxme.impl.JAXBContextImpl;
import org.apache.ws.jaxme.impl.XMLWriterImpl;
import org.apache.ws.jaxme.js.JavaConstructor;
import org.apache.ws.jaxme.js.JavaField;
import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaQName;
import org.apache.ws.jaxme.js.JavaQNameImpl;
import org.apache.ws.jaxme.js.JavaSource;
import org.apache.ws.jaxme.js.JavaSourceFactory;
import org.apache.ws.jaxme.js.Parameter;
import org.apache.ws.jaxme.js.TextFile;
import org.apache.ws.jaxme.logging.Logger;
import org.apache.ws.jaxme.logging.LoggerAccess;
import org.apache.ws.jaxme.util.DOMSerializer;
import org.apache.ws.jaxme.xs.XSElement;
import org.apache.ws.jaxme.xs.XSGroup;
import org.apache.ws.jaxme.xs.XSSchema;
import org.apache.ws.jaxme.xs.XSType;
import org.apache.ws.jaxme.xs.jaxb.JAXBGlobalBindings;
import org.apache.ws.jaxme.xs.jaxb.JAXBJavaType;
import org.apache.ws.jaxme.xs.jaxb.JAXBSchema;
import org.apache.ws.jaxme.xs.types.XSNCName;
import org.apache.ws.jaxme.xs.xml.XsQName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class JAXBSchemaSG implements SchemaSGChain {

    private static final Logger log = LoggerAccess.getLogger(JAXBSchemaSG.class);

    private final Map elementsByName = new HashMap();

    private final Map groupsByName = new HashMap();

    private final Map typesByName = new HashMap();

    private final SGFactory factory;

    private final XSSchema xsSchema;

    private final JavaSourceFactory javaSourceFactory = new JavaSourceFactory();

    private TypeSG[] typesByOrder;

    private ObjectSG[] elementsByOrder;

    private ObjectSG[] objectsByOrder;

    private GroupSG[] groupsByOrder;

    /** <p>Creates a new instance of JAXBSchemaSG.</p>
   */
    public JAXBSchemaSG(SGFactory pFactory, XSSchema pSchema) {
        factory = pFactory;
        xsSchema = pSchema;
        javaSourceFactory.setOverwriteForced(pFactory.getGenerator().isForcingOverwrite());
        javaSourceFactory.setSettingReadOnly(pFactory.getGenerator().isSettingReadOnly());
    }

    public SchemaSG getSchema(SchemaSG pController) {
        return pController;
    }

    public Locator getLocator(SchemaSG pController) {
        return xsSchema.getLocator();
    }

    public SGFactory getFactory(SchemaSG pController) {
        return factory;
    }

    protected XSSchema getXSSchema() {
        return xsSchema;
    }

    public void init(SchemaSG pController) throws SAXException {
        final String mName = "init";
        log.finest(mName, "->");
        Object[] childs = getXSSchema().getChilds();
        List elements = new ArrayList();
        List types = new ArrayList();
        List groups = new ArrayList();
        List objects = new ArrayList();
        for (int i = 0; i < childs.length; i++) {
            Object o = childs[i];
            log.finest(mName, "Child" + o);
            if (o instanceof XSType) {
                TypeSG typeSG = pController.getType(((XSType) o).getName());
                if (typeSG == null) {
                    throw new IllegalStateException("TypeSG not created");
                }
                types.add(typeSG);
            } else if (o instanceof XSGroup) {
                GroupSG groupSG = pController.getGroup(((XSGroup) o).getName());
                if (groupSG == null) {
                    throw new IllegalStateException("GroupSG not created");
                }
                groups.add(groupSG);
            } else if (o instanceof XSElement) {
                ObjectSG objectSG = pController.getElement(((XSElement) o).getName());
                if (objectSG == null) {
                    throw new IllegalStateException("ObjectSG not created");
                }
                objects.add(objectSG);
                elements.add(objectSG);
            }
        }
        elementsByOrder = (ObjectSG[]) elements.toArray(new ObjectSG[elements.size()]);
        typesByOrder = (TypeSG[]) types.toArray(new TypeSG[types.size()]);
        groupsByOrder = (GroupSG[]) groups.toArray(new GroupSG[groups.size()]);
        objectsByOrder = (ObjectSG[]) objects.toArray(new ObjectSG[objects.size()]);
        log.finest(mName, "<-");
    }

    public TypeSG getType(SchemaSG pController, XsQName pName) throws SAXException {
        TypeSG typeSG = (TypeSG) typesByName.get(pName);
        if (typeSG != null) {
            return typeSG;
        }
        XSType type = getXSSchema().getType(pName);
        if (type == null) {
            return null;
        }
        typeSG = pController.getFactory().getTypeSG(type);
        typesByName.put(pName, typeSG);
        return typeSG;
    }

    public GroupSG[] getGroups(SchemaSG pController) throws SAXException {
        return groupsByOrder;
    }

    public GroupSG getGroup(SchemaSG pController, XsQName pName) throws SAXException {
        GroupSG groupSG = (GroupSG) groupsByName.get(pName);
        if (groupSG != null) {
            return groupSG;
        }
        XSGroup group = getXSSchema().getGroup(pName);
        if (group == null) {
            return null;
        }
        groupSG = pController.getFactory().getGroupSG(group);
        groupsByName.put(pName, groupSG);
        return groupSG;
    }

    public TypeSG[] getTypes(SchemaSG pController) throws SAXException {
        return typesByOrder;
    }

    public ObjectSG[] getObjects(SchemaSG pController) throws SAXException {
        return objectsByOrder;
    }

    public ObjectSG getElement(SchemaSG pController, XsQName pName) throws SAXException {
        ObjectSG objectSG = (ObjectSG) elementsByName.get(pName);
        if (objectSG != null) {
            return objectSG;
        }
        XSElement element = getXSSchema().getElement(pName);
        if (element == null) {
            return null;
        }
        objectSG = pController.getFactory().getObjectSG(element);
        elementsByName.put(pName, objectSG);
        return objectSG;
    }

    public String getCollectionType(SchemaSG pController) {
        XSSchema schema = getXSSchema();
        if (schema instanceof JAXBSchema) {
            JAXBSchema jaxbSchema = (JAXBSchema) schema;
            JAXBGlobalBindings globalBindings = jaxbSchema.getJAXBGlobalBindings();
            if (globalBindings != null) {
                String result = globalBindings.getCollectionType();
                if (result != null) {
                    return result;
                }
            }
        }
        return ArrayList.class.getName();
    }

    public JavaSourceFactory getJavaSourceFactory(SchemaSG pController) {
        return javaSourceFactory;
    }

    public void generate(SchemaSG pController) throws SAXException {
        TypeSG[] types = pController.getTypes();
        for (int i = 0; i < types.length; i++) {
            types[i].generate();
        }
        ObjectSG[] objects = pController.getObjects();
        for (int i = 0; i < objects.length; i++) {
            objects[i].generate();
        }
        pController.generateJaxbProperties();
    }

    public ObjectSG[] getElements(SchemaSG pController) throws SAXException {
        return elementsByOrder;
    }

    public boolean isBindingStyleModelGroup(SchemaSG pController) {
        XSSchema schema = getXSSchema();
        if (schema instanceof JAXBSchema) {
            JAXBSchema jaxbSchema = (JAXBSchema) schema;
            JAXBGlobalBindings globalBindings = jaxbSchema.getJAXBGlobalBindings();
            if (globalBindings != null) {
                return globalBindings.isBindingStyleModelGroup();
            }
        }
        return false;
    }

    public boolean isChoiceContentProperty(SchemaSG pController) {
        XSSchema schema = getXSSchema();
        if (schema instanceof JAXBSchema) {
            JAXBSchema jaxbSchema = (JAXBSchema) schema;
            JAXBGlobalBindings globalBindings = jaxbSchema.getJAXBGlobalBindings();
            if (globalBindings != null) {
                return globalBindings.isChoiceContentProperty();
            }
        }
        return false;
    }

    public boolean isFailFastCheckEnabled(SchemaSG pController) {
        XSSchema schema = getXSSchema();
        if (schema instanceof JAXBSchema) {
            JAXBSchema jaxbSchema = (JAXBSchema) schema;
            JAXBGlobalBindings globalBindings = jaxbSchema.getJAXBGlobalBindings();
            if (globalBindings != null) {
                return globalBindings.isEnableFailFastCheck();
            }
        }
        return false;
    }

    public boolean isJavaNamingConventionsEnabled(SchemaSG pController) {
        XSSchema schema = getXSSchema();
        if (schema instanceof JAXBSchema) {
            JAXBSchema jaxbSchema = (JAXBSchema) schema;
            JAXBGlobalBindings globalBindings = jaxbSchema.getJAXBGlobalBindings();
            if (globalBindings != null) {
                return globalBindings.isEnableJavaNamingConventions();
            }
        }
        return true;
    }

    public boolean isFixedAttributeConstantProperty(SchemaSG pController) {
        XSSchema schema = getXSSchema();
        if (schema instanceof JAXBSchema) {
            JAXBSchema jaxbSchema = (JAXBSchema) schema;
            JAXBGlobalBindings globalBindings = jaxbSchema.getJAXBGlobalBindings();
            if (globalBindings != null) {
                return globalBindings.isFixedAttributeAsConstantProperty();
            }
        }
        return false;
    }

    public boolean isGeneratingIsSetMethod(SchemaSG pController) {
        XSSchema schema = getXSSchema();
        if (schema instanceof JAXBSchema) {
            JAXBSchema jaxbSchema = (JAXBSchema) schema;
            JAXBGlobalBindings globalBindings = jaxbSchema.getJAXBGlobalBindings();
            if (globalBindings != null) {
                return globalBindings.isGenerateIsSetMethod();
            }
        }
        return false;
    }

    public boolean isUnderscoreWordSeparator(SchemaSG pController) {
        XSSchema schema = getXSSchema();
        if (schema instanceof JAXBSchema) {
            JAXBSchema jaxbSchema = (JAXBSchema) schema;
            JAXBGlobalBindings globalBindings = jaxbSchema.getJAXBGlobalBindings();
            if (globalBindings != null) {
                return JAXBGlobalBindings.UnderscoreBinding.AS_WORD_SEPARATOR.equals(globalBindings.getUnderscoreBinding());
            }
        }
        return true;
    }

    public JAXBJavaType[] getJAXBJavaTypes(SchemaSG pController) {
        XSSchema schema = getXSSchema();
        if (schema instanceof JAXBSchema) {
            JAXBSchema jaxbSchema = (JAXBSchema) schema;
            JAXBGlobalBindings globalBindings = jaxbSchema.getJAXBGlobalBindings();
            if (globalBindings != null) {
                return globalBindings.getJavaType();
            }
        }
        return new JAXBJavaType[0];
    }

    public XsQName[] getTypesafeEnumBase(SchemaSG pController) {
        XSSchema schema = getXSSchema();
        if (schema instanceof JAXBSchema) {
            JAXBSchema jaxbSchema = (JAXBSchema) schema;
            JAXBGlobalBindings globalBindings = jaxbSchema.getJAXBGlobalBindings();
            if (globalBindings != null) {
                return globalBindings.getTypesafeEnumBase();
            }
        }
        return new XsQName[] { XSNCName.getInstance().getName() };
    }

    public Document getConfigFile(SchemaSG pController, String pPackageName, List pContextList) throws SAXException {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(false);
        final DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new SAXException("Failed to create a DocumentBuilder: " + e.getMessage(), e);
        }
        final Document doc = db.newDocument();
        final String uri = JAXBContextImpl.CONFIGURATION_URI;
        final Element root = doc.createElementNS(uri, "Configuration");
        root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns", uri);
        doc.appendChild(root);
        for (Iterator iter = pContextList.iterator(); iter.hasNext(); ) {
            Object o = iter.next();
            Context ctx, typeCtx;
            if (o instanceof ObjectSG) {
                ObjectSG oSG = (ObjectSG) o;
                ctx = oSG.getClassContext();
                typeCtx = oSG.getTypeSG().getComplexTypeSG().getClassContext();
            } else {
                TypeSG tSG = (TypeSG) o;
                ctx = typeCtx = tSG.getComplexTypeSG().getClassContext();
            }
            String packageName = ctx.getXMLInterfaceName().getPackageName();
            if (packageName == null) {
                packageName = "";
            }
            if (!packageName.equals(pPackageName)) {
                continue;
            }
            Element manager = doc.createElementNS(uri, "Manager");
            root.appendChild(manager);
            manager.setAttributeNS(null, "elementInterface", ctx.getXMLInterfaceName().toString());
            manager.setAttributeNS(null, "elementClass", ctx.getXMLImplementationName().toString());
            manager.setAttributeNS(null, "handlerClass", typeCtx.getXMLHandlerName().toString());
            manager.setAttributeNS(null, "driverClass", typeCtx.getXMLSerializerName().toString());
            manager.setAttributeNS(null, "validatorClass", ctx.getXMLValidatorName().toString());
            if (o instanceof ObjectSG) {
                XsQName name = ((ObjectSG) o).getName();
                manager.setAttributeNS(null, "qName", name.toString());
                if (name.getPrefix() != null) {
                    manager.setAttributeNS(null, "prefix", name.getPrefix());
                }
            }
        }
        return doc;
    }

    protected String generateConfigFile(SchemaSG pController, String pPackageName, List pContextList) throws SAXException {
        Document doc = pController.getConfigFile(pPackageName, pContextList);
        StringWriter sw = new StringWriter();
        XMLWriter xw = new XMLWriterImpl();
        try {
            xw.setWriter(sw);
        } catch (JAXBException e) {
            throw new SAXException(e);
        }
        DOMSerializer ds = new DOMSerializer();
        ds.serialize(doc, xw);
        return sw.toString();
    }

    public void generateJaxbProperties(SchemaSG pController) throws SAXException {
        List contextList = new ArrayList();
        ObjectSG[] elements = pController.getElements();
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].getTypeSG().isComplex()) {
                contextList.add(elements[i]);
            }
        }
        TypeSG[] types = pController.getTypes();
        for (int i = 0; i < types.length; i++) {
            if (types[i].isComplex()) {
                contextList.add(types[i]);
            }
        }
        Set packages = new HashSet();
        for (Iterator iter = contextList.iterator(); iter.hasNext(); ) {
            Object o = iter.next();
            Context ctx;
            if (o instanceof ObjectSG) {
                ctx = ((ObjectSG) o).getClassContext();
            } else {
                ctx = ((TypeSG) o).getComplexTypeSG().getClassContext();
            }
            String packageName = ctx.getXMLInterfaceName().getPackageName();
            if (packages.contains(packageName)) {
                continue;
            }
            TextFile textFile = pController.getJavaSourceFactory().newTextFile(packageName, "jaxb.properties");
            textFile.addLine(JAXBContext.JAXB_CONTEXT_FACTORY + "=" + JAXBContextImpl.class.getName());
            packages.add(packageName);
            String configFile = generateConfigFile(pController, packageName, contextList);
            TextFile confFile = pController.getJavaSourceFactory().newTextFile(packageName, "Configuration.xml");
            confFile.setContents(configFile);
            getObjectFactory(pController, packageName, contextList);
        }
    }

    protected JavaSource getObjectFactory(SchemaSG pController, String pPackageName, List pContextList) throws SAXException {
        JavaQName qName = JavaQNameImpl.getInstance(pPackageName, "ObjectFactory");
        JavaSource js = pController.getJavaSourceFactory().newJavaSource(qName, "public");
        JavaField jf = js.newJavaField("jaxbContext", JAXBContextImpl.class, "private");
        JavaField properties = js.newJavaField("properties", Map.class, "private");
        JavaConstructor jcon = js.newJavaConstructor("public");
        jcon.addThrows(JAXBException.class);
        jcon.addLine(jf, " = (", JAXBContextImpl.class, ") ", JAXBContext.class, ".newInstance(", JavaSource.getQuoted(pPackageName), ");");
        JavaMethod newInstanceMethod = js.newJavaMethod("newInstance", Object.class, "public");
        newInstanceMethod.addThrows(JAXBException.class);
        Parameter pElementInterface = newInstanceMethod.addParam(Class.class, "pElementInterface");
        newInstanceMethod.addLine("return ", jf, ".getManager(", pElementInterface, ").getElementJ();");
        {
            JavaMethod getPropertyMethod = js.newJavaMethod("getProperty", Object.class, "public");
            Parameter pName = getPropertyMethod.addParam(String.class, "pName");
            getPropertyMethod.addIf(properties, " == null");
            getPropertyMethod.addLine("return null;");
            getPropertyMethod.addEndIf();
            getPropertyMethod.addLine("return ", properties, ".get(", pName, ");");
        }
        {
            JavaMethod setPropertyMethod = js.newJavaMethod("setProperty", void.class, "public");
            Parameter pName = setPropertyMethod.addParam(String.class, "pName");
            Parameter pValue = setPropertyMethod.addParam(Object.class, "pValue");
            setPropertyMethod.addIf(properties, " == null");
            setPropertyMethod.addLine(properties, " = new ", HashMap.class, "();");
            setPropertyMethod.addEndIf();
            setPropertyMethod.addLine(properties, ".put(", pName, ", ", pValue, ");");
        }
        Set contextSet = new HashSet();
        for (Iterator iter = pContextList.iterator(); iter.hasNext(); ) {
            Object o = iter.next();
            TypeSG typeSG;
            if (o instanceof ObjectSG) {
                ObjectSG objectSG = ((ObjectSG) o);
                typeSG = objectSG.getTypeSG();
                generateCreateMethod(js, null, objectSG.getClassContext());
            } else if (o instanceof TypeSG) {
                typeSG = (TypeSG) o;
            } else {
                continue;
            }
            generateCreateMethod(js, contextSet, typeSG, null);
        }
        return js;
    }

    private void generateCreateMethod(JavaSource pJs, Set pContextSet, TypeSG pType, String pPrefix) throws SAXException {
        if (!pType.isComplex() || pContextSet.contains(pType)) {
            return;
        }
        String prefix = generateCreateMethod(pJs, pPrefix, pType.getComplexTypeSG().getClassContext());
        pContextSet.add(pType);
        generateCreateMethods(pJs, pType, prefix, pContextSet);
    }

    /** Generate create methods for the given particles.
   */
    private void generateCreateMethods(JavaSource pJs, ParticleSG[] pParticles, String pName, Set pContextSet) throws SAXException {
        for (int i = 0; i < pParticles.length; i++) {
            ParticleSG particle = pParticles[i];
            if (particle.isGroup()) {
                GroupSG group = particle.getGroupSG();
                generateCreateMethods(pJs, group.getParticles(), pName, pContextSet);
            } else if (particle.isElement()) {
                ObjectSG oSG = particle.getObjectSG();
                if (oSG.isGlobal()) {
                    continue;
                }
                TypeSG tSG = oSG.getTypeSG();
                if (tSG.isGlobalType()) {
                    continue;
                }
                generateCreateMethod(pJs, pContextSet, tSG, pName);
            } else if (particle.isWildcard()) {
                throw new IllegalStateException("TODO: Add support for wildcards here.");
            } else {
                throw new IllegalStateException("Invalid class type");
            }
        }
    }

    /** Generate create methods for the content.
   */
    private void generateCreateMethods(JavaSource pJs, TypeSG pType, String pName, Set pContextSet) throws SAXException {
        ComplexTypeSG ctSG = pType.getComplexTypeSG();
        if (ctSG.hasSimpleContent()) {
            return;
        }
        ComplexContentSG ccSG = ctSG.getComplexContentSG();
        if (ccSG.isEmpty()) {
            return;
        }
        GroupSG group = ccSG.getGroupSG();
        generateCreateMethods(pJs, group.getParticles(), pName, pContextSet);
    }

    /**
   * Generic util method for generating the create<NAME> methods for the object factory.
   * @param pSource the java source object to add the method
   * @param pContext the Class Context from either an ObjectSG or a TypeSG
   */
    private String generateCreateMethod(JavaSource pSource, String pPrefix, Context pContext) {
        JavaQName resultName = pContext.getXMLInterfaceName();
        String className = resultName.isInnerClass() ? resultName.getInnerClassName() : resultName.getClassName();
        String result = Character.toUpperCase(className.charAt(0)) + className.substring(1);
        boolean anonymous = pPrefix != null;
        if (anonymous) {
            result = pPrefix + result;
        }
        String methodName = "create" + result;
        JavaMethod createMethod = pSource.newJavaMethod(methodName, resultName, "public");
        createMethod.addThrows(JAXBException.class);
        if (anonymous) {
            createMethod.addLine("return (", resultName, ") newInstance(", resultName, ".class);");
        } else {
            createMethod.addLine("return (", resultName, ") newInstance(", resultName, ".class);");
        }
        return result;
    }
}
