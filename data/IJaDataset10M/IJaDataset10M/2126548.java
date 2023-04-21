package net.peelmeagrape.hibernate.xmlgen;

import net.peelmeagrape.hibernate.H8IncludeInSubclass;
import net.peelmeagrape.hibernate.config.HibernateAnnotationsProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.*;
import sun.reflect.annotation.AnnotationType;
import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class GenerateMappingRun {

    private static DocumentFactory documentFactory = DocumentFactory.getInstance();

    private static Log log = LogFactory.getLog(HibernateAnnotationsProcessor.class);

    private Class entityClass;

    private Document mappingDocument;

    private Element hibernateMappingElement;

    private boolean debugMode;

    private String currentPropertyName;

    private Type currentPropertyType;

    private Class currentPropertyClass;

    private String dtdPublicId = "-//Hibernate/Hibernate Mapping DTD 3.0//EN";

    private String dtdSystemId = "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd";

    private String schema = null;

    private String catalog = null;

    private String defaultCascade = null;

    private String defaultAccess = null;

    private boolean defaultLazy = true;

    private boolean autoImport = true;

    private boolean outputDefaults = false;

    private static final String CLASS_ELEMENT_CHILD_ORDER = "(meta*,subselect?,cache?,synchronize*,(id|composite-id),key,discriminator?,natural-id?,(version|timestamp)?,(property|many-to-one|one-to-one|component|dynamic-component|properties|any|map|set|list|bag|idbag|array|primitive-array|query-list)*,((join*,subclass*)|joined-subclass*|union-subclass*),loader?,sql-insert?,sql-update?,sql-delete?,filter*)";

    private static final String MAPPING_ELEMENT_CHILD_ORDER = "(meta*,typedef*,import*,(class|subclass|joined-subclass|union-subclass)*,(query|sql-query)*,filter-def*)";

    public GenerateMappingRun(Class entityClass) {
        this.entityClass = entityClass;
    }

    public void generate() throws DocumentException {
        mappingDocument = documentFactory.createDocument();
        mappingDocument.addDocType("hibernate-mapping", dtdPublicId, dtdSystemId);
        if (schema != null) hibernateMappingElement.addAttribute("schema", schema);
        if (catalog != null) hibernateMappingElement.addAttribute("catalog", catalog);
        if (defaultCascade != null) hibernateMappingElement.addAttribute("default-cascade", defaultCascade);
        if (defaultAccess != null) hibernateMappingElement.addAttribute("default-access", defaultAccess);
        if (!defaultLazy) hibernateMappingElement.addAttribute("default-lazy", "false");
        if (!autoImport) hibernateMappingElement.addAttribute("auto-import", "false");
        hibernateMappingElement = documentFactory.createElement("hibernate-mapping");
        mappingDocument.setRootElement(hibernateMappingElement);
        processEntityClassAnnotations();
        XmlUtils.reorderChildElements(hibernateMappingElement, MAPPING_ELEMENT_CHILD_ORDER);
    }

    public Document getMappingDocument() {
        if (mappingDocument == null) throw new IllegalStateException("Call generate first");
        if (hibernateMappingElement.nodeCount() > 0) return mappingDocument; else return null;
    }

    private void processEntityClassAnnotations() throws DocumentException {
        processAllAnnotationsIntoXml(entityClass, hibernateMappingElement);
        Element parentElement = getClassMappingElement(hibernateMappingElement);
        addMappingInfoFromFields(parentElement);
        addMappingInfoFromMethods(parentElement);
        processSuperclasses(parentElement);
        if (parentElement != null) XmlUtils.reorderChildElements(parentElement, CLASS_ELEMENT_CHILD_ORDER);
    }

    private void processSuperclasses(Element parentElement) throws DocumentException {
        Class targetClass = entityClass.getSuperclass();
        do {
            if (targetClass.isAnnotationPresent(H8IncludeInSubclass.class)) {
                GenerateMappingRun targetClassGenerate = new GenerateMappingRun(targetClass);
                targetClassGenerate.addMappingInfoFromFields(parentElement);
                targetClassGenerate.addMappingInfoFromMethods(parentElement);
            }
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null);
    }

    private Element getClassMappingElement(Element hibernateMappingElement) {
        List<Element> list = hibernateMappingElement.elements();
        for (Element element : list) {
            String name = element.getName();
            if (name.equals("class")) return element;
            if (name.equals("subclass")) return element;
            if (name.equals("joined-subclass")) return element;
            if (name.equals("union-subclass")) return element;
        }
        return null;
    }

    private void addMappingInfoFromMethods(Element classElement) throws DocumentException {
        for (Method method : entityClass.getDeclaredMethods()) {
            if (debugMode) classElement.add(documentFactory.createComment("Processing method " + method.getName()));
            String name = method.getName();
            if (name.startsWith("get")) {
                currentPropertyName = Introspector.decapitalize(name.substring("get".length()));
                currentPropertyType = method.getGenericReturnType();
                currentPropertyClass = method.getReturnType();
            } else if (name.startsWith("is")) {
                currentPropertyName = Introspector.decapitalize(name.substring("is".length()));
                currentPropertyType = method.getGenericReturnType();
                currentPropertyClass = method.getReturnType();
            } else if (name.startsWith("set") && method.getParameterTypes().length == 1) {
                currentPropertyName = Introspector.decapitalize(name.substring("set".length()));
                currentPropertyType = method.getGenericParameterTypes()[0];
                currentPropertyClass = method.getParameterTypes()[0];
            } else {
                currentPropertyName = null;
                currentPropertyType = null;
                currentPropertyClass = null;
            }
            processAllAnnotationsIntoXml(method, classElement);
        }
    }

    private void addMappingInfoFromFields(Element classElement) throws DocumentException {
        for (Field field : entityClass.getDeclaredFields()) {
            if (debugMode) classElement.add(documentFactory.createComment("Processing field " + field.getName()));
            currentPropertyName = field.getName();
            currentPropertyType = field.getGenericType();
            currentPropertyClass = field.getType();
            processAllAnnotationsIntoXml(field, classElement);
        }
    }

    private void processAllAnnotationsIntoXml(AnnotatedElement annotatedElement, Element parentElement) throws DocumentException {
        if (annotatedElement.isAnnotationPresent(HibernateXml.class)) processEmbeddedXml(annotatedElement, parentElement);
        Annotation[] annotations = annotatedElement.getAnnotations();
        for (Annotation annotation : annotations) {
            processAnnotationIntoElement(annotation, annotatedElement, parentElement);
        }
    }

    private void processAnnotationIntoElement(Annotation annotation, AnnotatedElement annotatedElement, Element parentElement) throws DocumentException {
        if (annotation.annotationType().isAnnotationPresent(XmlElement.class)) {
            Element childElement = createChildElementFromAnnotation(annotation, parentElement);
            if (childElement != null) addXmlFromAnnotation(annotation, childElement, annotatedElement);
        } else if (annotation.annotationType().isAnnotationPresent(GroupingAnnotation.class)) {
            addXmlFromAnnotation(annotation, parentElement, annotatedElement);
        }
    }

    private void addXmlFromAnnotation(Annotation annotation, Element childElement, AnnotatedElement annotatedElement) throws DocumentException {
        addClassNameAttributeIfRequired(annotation, childElement);
        addPropertyNameAttributeIfRequired(annotation, childElement);
        AnnotationType annotationDescription = AnnotationUtils.getInternalAnnotationInfo(annotation);
        SortedMap<String, Method> sortedMembers = getSortedAnnotationMembers(annotationDescription);
        Map<String, Object> memberDefaults = annotationDescription.memberDefaults();
        for (Map.Entry<String, Method> entry : sortedMembers.entrySet()) {
            try {
                Method annotationMemberGetter = entry.getValue();
                String memberName = entry.getKey();
                Object defaultvalue = memberDefaults.get(memberName);
                Object value = annotationMemberGetter.invoke(annotation);
                processAnnotationValue(annotatedElement, childElement, annotation, memberName, annotationMemberGetter, defaultvalue, value);
            } catch (IllegalAccessException e) {
                log.warn("Could not get value of " + entry.getKey() + " from the " + annotation.annotationType() + " on " + annotatedElement, e);
            } catch (InvocationTargetException e) {
                log.warn("Could not get value of " + entry.getKey() + " from the " + annotation.annotationType() + " on " + annotatedElement, e);
            }
        }
    }

    private SortedMap<String, Method> getSortedAnnotationMembers(AnnotationType annotationDescription) {
        final Map<String, Method> annotationMembers = annotationDescription.members();
        SortedMap<String, Method> sortedMembers = new TreeMap<String, Method>(new Comparator<String>() {

            public int compare(String name1, String name2) {
                int index1 = getIndex(name1);
                int index2 = getIndex(name2);
                if (index1 == -1 && index2 >= 0) return -1;
                if (index2 == -1 && index1 >= 0) return 1;
                if (index2 == -1 && index1 == -1) return name1.compareTo(name2);
                if (index2 == index1) return name1.compareTo(name2);
                return index1 - index2;
            }

            private int getIndex(String name1) {
                Method memberGetter = annotationMembers.get(name1);
                if (memberGetter.isAnnotationPresent(ChildElement.class)) {
                    return memberGetter.getAnnotation(ChildElement.class).value();
                }
                return -1;
            }
        });
        for (Map.Entry<String, Method> entry : annotationMembers.entrySet()) {
            sortedMembers.put(entry.getKey(), entry.getValue());
        }
        return sortedMembers;
    }

    private void processAnnotationValue(AnnotatedElement annotatedElement, Element childElement, Annotation annotation, String memberName, Method annotationMemberGetter, Object defaultvalue, Object value) throws DocumentException {
        if (annotationMemberGetter.getReturnType().isAnnotation()) {
            processAnnotationIntoElement((Annotation) value, annotatedElement, childElement);
            return;
        }
        if (annotationMemberGetter.isAnnotationPresent(DefaultFromGenericType.class)) {
            value = addClassValuesFromGenerics(annotationMemberGetter, value);
        }
        if (annotationMemberGetter.isAnnotationPresent(DefaultFromPropertyType.class)) {
            value = addClassValueFromProperty(annotationMemberGetter, value);
        }
        if (annotationMemberGetter.isAnnotationPresent(DefaultFromSuperclass.class)) {
            value = addClassValueFromSuperclass(annotationMemberGetter, value);
        }
        if (annotationMemberGetter.isAnnotationPresent(FindParentElement.class)) {
            reparentElement(annotationMemberGetter, childElement, value);
        } else if (annotationMemberGetter.getReturnType().isArray()) {
            Object[] values = (Object[]) value;
            processAnotationWithArrayValue(annotatedElement, childElement, annotation, memberName, annotationMemberGetter, values);
        } else if (!value.equals(defaultvalue) || outputDefaults) {
            if (annotationMemberGetter.isAnnotationPresent(XmlAttribute.class)) {
                String attributeName = getAttributeName(annotationMemberGetter, memberName);
                Attribute attribute = documentFactory.createAttribute(childElement, attributeName, stringify(value));
                childElement.add(attribute);
            } else if (annotationMemberGetter.isAnnotationPresent(XmlText.class)) {
                Text text = documentFactory.createText(stringify(value));
                childElement.add(text);
            }
        }
    }

    private void reparentElement(Method annotationMemberGetter, Element childElement, Object value) {
        FindParentElement findParentElementInfo = annotationMemberGetter.getAnnotation(FindParentElement.class);
        String parentElementName = findParentElementInfo.name();
        Element parent = childElement.getParent();
        List<Element> list = parent.elements(parentElementName);
        Element newParent = null;
        if (value instanceof Boolean && ((Boolean) value)) {
            newParent = list.get(0);
        } else if (value instanceof String[] && ((String[]) value).length == 1) {
            String parentId = ((String[]) value)[0];
            String idAttributeName = findParentElementInfo.idAttributeName();
            if (parentId.length() == 0 && list.size() == 1) newParent = list.get(0); else {
                for (Element element : list) {
                    Attribute attribute = element.attribute(idAttributeName);
                    if (attribute != null && attribute.getValue().equals(parentId)) newParent = element;
                }
            }
        }
        if (newParent != null) {
            parent.remove(childElement);
            newParent.add(childElement);
            if (findParentElementInfo.reorderNewParent().length() > 0) {
                XmlUtils.reorderChildElements(newParent, findParentElementInfo.reorderNewParent());
            }
        }
    }

    private Object addClassValueFromSuperclass(Method annotationMemberGetter, Object value) {
        if (annotationMemberGetter.getReturnType().isArray()) {
            if (((Object[]) value).length == 0) {
                Class type = entityClass.getSuperclass();
                if (type != null) value = new Class[] { type };
            }
        }
        return value;
    }

    private void processAnotationWithArrayValue(AnnotatedElement annotatedElement, Element childElement, Annotation annotation, String memberName, Method annotationMemberGetter, Object[] values) throws DocumentException {
        if (annotationMemberGetter.isAnnotationPresent(XmlAttribute.class)) {
            if (values.length > 0) {
                String attributeName = getAttributeName(annotationMemberGetter, memberName);
                Attribute attribute = documentFactory.createAttribute(childElement, attributeName, stringify(values[0]));
                childElement.add(attribute);
            }
        } else {
            for (Object v : values) {
                if (v instanceof Annotation) processAnnotationIntoElement((Annotation) v, annotatedElement, childElement);
            }
        }
        if (annotationMemberGetter.isAnnotationPresent(ProcessTargetClass.class)) {
            if (values instanceof Class[] && values.length == 1) {
                Class targetClass = (Class) values[0];
                GenerateMappingRun targetClassGenerate = new GenerateMappingRun(targetClass);
                targetClassGenerate.addMappingInfoFromFields(childElement);
                targetClassGenerate.addMappingInfoFromMethods(childElement);
                XmlElement xmlElementInfo = annotation.annotationType().getAnnotation(XmlElement.class);
                if (xmlElementInfo != null && xmlElementInfo.reorderChildren().length() > 0) {
                    String childOrder = xmlElementInfo.reorderChildren();
                    XmlUtils.reorderChildElements(childElement, childOrder);
                }
            }
        }
    }

    private Object addClassValueFromProperty(Method annotationMemberGetter, Object value) {
        if (annotationMemberGetter.getReturnType().isArray()) {
            if (((Object[]) value).length == 0) value = new Class[] { currentPropertyClass };
        }
        return value;
    }

    private Object addClassValuesFromGenerics(Method annotationMemberGetter, Object value) {
        if (annotationMemberGetter.getReturnType().isArray()) {
            Object[] values = (Object[]) value;
            if (values.length == 0) {
                DefaultFromGenericType defaultFromGenericType = annotationMemberGetter.getAnnotation(DefaultFromGenericType.class);
                int index = defaultFromGenericType.value();
                if (currentPropertyType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) currentPropertyType;
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (index < 0) index = index + actualTypeArguments.length;
                    if (actualTypeArguments.length > index && index >= 0) {
                        Type actualTypeArgument = actualTypeArguments[index];
                        if (actualTypeArgument instanceof Class) {
                            Class typeArgument = (Class) actualTypeArgument;
                            value = new Class[] { typeArgument };
                        }
                    }
                }
            }
        }
        return value;
    }

    private String stringify(Object value) {
        if (value instanceof Class) {
            Class aClass = (Class) value;
            return aClass.getName();
        }
        return value.toString();
    }

    private String getAttributeName(Method annotationMemberGetter, String memberName) {
        XmlAttribute xmlAttributeInfo = annotationMemberGetter.getAnnotation(XmlAttribute.class);
        String attributeName = memberName;
        if (!"".equals(xmlAttributeInfo.value())) attributeName = xmlAttributeInfo.value();
        return attributeName;
    }

    private void addClassNameAttributeIfRequired(Annotation annotation, Element childElement) {
        if (annotation.annotationType().isAnnotationPresent(AddClassNameAsAttribute.class)) {
            AddClassNameAsAttribute classNameAsAttribute = annotation.annotationType().getAnnotation(AddClassNameAsAttribute.class);
            String classAttributeName = classNameAsAttribute.name();
            Attribute classAttribute = documentFactory.createAttribute(childElement, classAttributeName, entityClass.getName());
            childElement.add(classAttribute);
        }
    }

    private void addPropertyNameAttributeIfRequired(Annotation annotation, Element childElement) {
        if (annotation.annotationType().isAnnotationPresent(AddPropertyNameAsAttribute.class)) {
            AddPropertyNameAsAttribute propertyNameAsAttribute = annotation.annotationType().getAnnotation(AddPropertyNameAsAttribute.class);
            String classAttributeName = propertyNameAsAttribute.name();
            Attribute nameAttribute = documentFactory.createAttribute(childElement, classAttributeName, currentPropertyName);
            childElement.add(nameAttribute);
        }
    }

    private Element createChildElementFromAnnotation(Annotation annotation, Element parentElement) {
        XmlElement xmlElementInfo = annotation.annotationType().getAnnotation(XmlElement.class);
        String elementName = xmlElementInfo.name();
        Element childElement = documentFactory.createElement(elementName);
        if (xmlElementInfo.parent().equals("hibernate-mapping")) hibernateMappingElement.add(childElement); else if (parentElement == null) return null; else parentElement.add(childElement);
        return childElement;
    }

    private void processEmbeddedXml(AnnotatedElement element, Element parentElement) throws DocumentException {
        HibernateXml xmlAnnotation = element.getAnnotation(HibernateXml.class);
        String xmlFragment = xmlAnnotation.xml();
        log.debug("Generating mapping for " + element.toString() + " from " + xmlFragment);
        log.debug("Trying to parse " + xmlFragment);
        String wrappedXml = "<a>" + xmlFragment + "</a>";
        Document fragmentDoc = XmlUtils.parseXml(wrappedXml);
        Element wrappingElement = fragmentDoc.getRootElement();
        Iterator nodes = wrappingElement.nodeIterator();
        List<Node> nodeList = new ArrayList<Node>(wrappingElement.nodeCount());
        while (nodes.hasNext()) {
            Node node = (Node) nodes.next();
            nodeList.add(node);
        }
        for (Node node : nodeList) {
            parentElement.add(node.detach());
        }
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public boolean isAutoImport() {
        return autoImport;
    }

    public void setAutoImport(boolean autoImport) {
        this.autoImport = autoImport;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getDefaultCascade() {
        return defaultCascade;
    }

    public void setDefaultCascade(String defaultCascade) {
        this.defaultCascade = defaultCascade;
    }

    public String getDefaultAccess() {
        return defaultAccess;
    }

    public void setDefaultAccess(String defaultAccess) {
        this.defaultAccess = defaultAccess;
    }

    public boolean isDefaultLazy() {
        return defaultLazy;
    }

    public void setDefaultLazy(boolean defaultLazy) {
        this.defaultLazy = defaultLazy;
    }

    public String getDtdPublicId() {
        return dtdPublicId;
    }

    public void setDtdPublicId(String dtdPublicId) {
        this.dtdPublicId = dtdPublicId;
    }

    public String getDtdSystemId() {
        return dtdSystemId;
    }

    public void setDtdSystemId(String dtdSystemId) {
        this.dtdSystemId = dtdSystemId;
    }
}
