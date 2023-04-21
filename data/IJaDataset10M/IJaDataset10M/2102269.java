package org.personalsmartspace.ipojo.manipulation.annotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.felix.ipojo.metadata.Attribute;
import org.apache.felix.ipojo.metadata.Element;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * Collect metadata from classes by parsing annotation.
 * This class collects type (i.e.) annotations and create method & field collectors.
* @author <a href="mailto:patx.cheevers@intel.com">Persist Project Team</a>

 */
public class PSSMetadataCollector extends EmptyVisitor implements Opcodes {

    /**
     * Class name.
     */
    private String m_className;

    /**
     * Root element of computed metadata.
     */
    private Element m_elem = null;

    /**
     * True if the visited class is a component type declaration (i.e. contains the @component annotation).
     */
    private boolean m_containsAnnotation = false;

    /**
     * Map of [element ids, element].
     * This map is used to easily get an already created element.
     */
    private Map m_ids = new HashMap();

    /**
     * Map of [element, referto].
     * This map is used to recreate the element hierarchie.
     * Stored element are added under referred element.
     */
    private Map m_elements = new HashMap();

    /**
     * XML document parser.
     */
    private DocumentBuilder m_builder;

    public Element getElem() {
        return m_elem;
    }

    public boolean isAnnotated() {
        return m_containsAnnotation;
    }

    /**
     * Start visiting a class.
     * Initialize the getter/setter generator, add the _cm field, add the pojo interface.
     * @param version : class version
     * @param access : class access
     * @param name : class name
     * @param signature : class signature
     * @param superName : class super class
     * @param interfaces : implemented interfaces
     * @see org.objectweb.asm.ClassAdapter#visit(int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     */
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        m_ids = new HashMap();
        m_elements = new HashMap();
        m_className = name;
    }

    /**
     * Visit class annotations.
     * This method detects @component and @provides annotations.
     * @param desc : annotation descriptor.
     * @param visible : is the annotation visible at runtime.
     * @return the annotation visitor.
     * @see org.objectweb.asm.ClassAdapter#visitAnnotation(java.lang.String, boolean)
     */
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.equals("Lorg/apache/felix/ipojo/annotations/Component;")) {
            m_elem = new Element("component", "");
            m_containsAnnotation = true;
            m_elem.addAttribute(new Attribute("className", m_className.replace('/', '.')));
            return new ComponentVisitor();
        }
        if (desc.equals("Lorg/apache/felix/ipojo/annotations/Handler;")) {
            m_elem = new Element("handler", "");
            m_containsAnnotation = true;
            m_elem.addAttribute(new Attribute("classname", m_className.replace('/', '.')));
            return new HandlerVisitor();
        }
        if (desc.equals("Lorg/apache/felix/ipojo/annotations/Provides;")) {
            return new ProvidesVisitor();
        }
        if (desc.equals("Lorg/apache/felix/ipojo/annotations/HandlerDeclaration;")) {
            return new HandlerDeclarationVisitor();
        }
        if (PSSCustomAnnotationVisitor.isCustomAnnotation(desc)) {
            Element elem = PSSCustomAnnotationVisitor.buildElement(desc);
            return new PSSCustomAnnotationVisitor(elem, this, true, true);
        }
        return null;
    }

    /**
     * Visit a field.
     * Call the field collector visitor.
     * @param access : field access.
     * @param name : field name
     * @param desc : field descriptor
     * @param signature : field signature
     * @param value : field value (static field only)
     * @return the field visitor.
     * @see org.objectweb.asm.ClassAdapter#visitField(int, java.lang.String, java.lang.String, java.lang.String, java.lang.Object)
     */
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return new PSSFieldCollector(name, this);
    }

    /**
     * Visit a method.
     * Call the method collector visitor.
     * @param access : method access
     * @param name : method name
     * @param desc : method descriptor
     * @param signature : method signature
     * @param exceptions : method exceptions
     * @return the Method Visitor.
     * @see org.objectweb.asm.ClassAdapter#visitMethod(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     */
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new PSSMethodCollector(name, this);
    }

    /**
     * End of the visit : compute final elements.
     * @see org.objectweb.asm.commons.EmptyVisitor#visitEnd()
     */
    public void visitEnd() {
        Set elems = getElements().keySet();
        Iterator it = elems.iterator();
        while (it.hasNext()) {
            Element current = (Element) it.next();
            String reference = (String) getElements().get(current);
            if (reference == null) {
                m_elem.addElement(current);
            } else {
                Element ref = (Element) getIds().get(reference);
                if (ref == null) {
                    m_elem.addElement(current);
                } else {
                    ref.addElement(current);
                }
            }
        }
    }

    protected Map getIds() {
        return m_ids;
    }

    protected Map getElements() {
        return m_elements;
    }

    /**
     * Creates a 'fresh' document builder.
     * @return a new document builder is not already created, else reset
     * the created one, and return it.
     */
    protected DocumentBuilder getFreshDocumentBuilder() {
        if (m_builder == null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            try {
                m_builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            return m_builder;
        }
        m_builder.reset();
        return m_builder;
    }

    /**
     * Parse the @provides annotation.
     */
    private class ProvidesVisitor extends EmptyVisitor implements AnnotationVisitor {

        /**
         * Provides element.
         */
        Element m_prov = new Element("provides", "");

        /**
         * Visit @provides annotation attributes.
         * @param arg0 : annotation attribute name
         * @param arg1 : annotation attribute value
         * @see org.objectweb.asm.commons.EmptyVisitor#visit(java.lang.String, java.lang.Object)
         */
        public void visit(String arg0, Object arg1) {
            if (arg0.equals("factory")) {
                m_prov.addAttribute(new Attribute("factory", arg1.toString()));
            }
            if (arg0.equals("strategy")) {
                m_prov.addAttribute(new Attribute("strategy", arg1.toString()));
            }
        }

        /**
         * Visit specifications array.
         * @param arg0 : attribute name
         * @return a visitor visiting each element of the array.
         * @see org.objectweb.asm.commons.EmptyVisitor#visitArray(java.lang.String)
         */
        public AnnotationVisitor visitArray(String arg0) {
            if (arg0.equals("specifications")) {
                return new InterfaceArrayVisitor();
            } else {
                return null;
            }
        }

        /**
         * End of the visit.
         * Append to the parent element the computed "provides" element.
         * @see org.objectweb.asm.commons.EmptyVisitor#visitEnd()
         */
        public void visitEnd() {
            getIds().put("provides", m_prov);
            getElements().put(m_prov, null);
        }

        private class InterfaceArrayVisitor extends EmptyVisitor {

            /**
             * List of parsed interface.
             */
            private String m_itfs;

            /**
             * Visit one element of the array.
             * @param arg0 : null
             * @param arg1 : element value.
             * @see org.objectweb.asm.commons.EmptyVisitor#visit(java.lang.String, java.lang.Object)
             */
            public void visit(String arg0, Object arg1) {
                if (m_itfs == null) {
                    m_itfs = "{" + ((Type) arg1).getClassName();
                } else {
                    m_itfs += "," + ((Type) arg1).getClassName();
                }
            }

            /**
             * End of the array visit.
             * Add the attribute to 'provides' element.
             * @see org.objectweb.asm.commons.EmptyVisitor#visitEnd()
             */
            public void visitEnd() {
                m_prov.addAttribute(new Attribute("specifications", m_itfs + "}"));
            }
        }
    }

    /**
     * Parse the @component annotation.
     */
    private class ComponentVisitor extends EmptyVisitor implements AnnotationVisitor {

        /**
         * Factory attribute.
         */
        private String m_factory;

        /**
         * Is the component an immediate component?
         */
        private String m_immediate;

        /**
         * Component name (cannot be null).
         */
        private String m_name;

        /**
         * Does the component exposes its architecture?
         */
        private String m_architecture;

        /**
         * Does the component propagate configuration to provided services?
         */
        private String m_propagation;

        /**
         * Managed Service PID.
         */
        private String m_managedservice;

        /**
         * Factory-Method.
         */
        private String m_method;

        /**
         * Version.
         */
        private String m_version;

        /**
         * Element properties.
         */
        private Element m_props;

        /**
         * Visit @component annotation attribute.
         * @param arg0 : attribute name
         * @param arg1 : attribute value
         * @see org.objectweb.asm.commons.EmptyVisitor#visit(java.lang.String, java.lang.Object)
         */
        public void visit(String arg0, Object arg1) {
            if (arg0.equals("public_factory")) {
                m_factory = arg1.toString();
                return;
            }
            if (arg0.equals("name")) {
                m_name = arg1.toString();
                return;
            }
            if (arg0.equals("immediate")) {
                m_immediate = arg1.toString();
                return;
            }
            if (arg0.equals("architecture")) {
                m_architecture = arg1.toString();
                return;
            }
            if (arg0.equals("propagation")) {
                m_propagation = arg1.toString();
                return;
            }
            if (arg0.equals("managedservice")) {
                m_managedservice = arg1.toString();
                return;
            }
            if (arg0.equals("factory_method")) {
                m_method = arg1.toString();
                return;
            }
            if (arg0.equals("version")) {
                m_version = arg1.toString();
                return;
            }
        }

        /**
         * End of the visit.
         * Append to the "component" element computed attribute.
         * @see org.objectweb.asm.commons.EmptyVisitor#visitEnd()
         */
        public void visitEnd() {
            if (m_name == null) {
                m_name = m_className.replace('/', '.');
            }
            m_elem.addAttribute(new Attribute("name", m_name));
            if (m_factory != null && m_factory.equalsIgnoreCase("false")) {
                m_elem.addAttribute(new Attribute("public", "false"));
            } else {
                m_elem.addAttribute(new Attribute("public", "true"));
            }
            if (m_architecture != null) {
                m_elem.addAttribute(new Attribute("architecture", m_architecture));
            }
            if (m_immediate != null) {
                m_elem.addAttribute(new Attribute("immediate", m_immediate));
            }
            if (m_method != null) {
                m_elem.addAttribute(new Attribute("factory-method", m_method));
            }
            if (m_version != null) {
                m_elem.addAttribute(new Attribute("version", m_version));
            }
            if (m_propagation != null) {
                if (m_props == null) {
                    m_props = new Element("properties", "");
                    getElements().put(m_props, null);
                    getIds().put("properties", m_props);
                }
                m_props.addAttribute(new Attribute("propagation", m_propagation));
            }
            if (m_managedservice != null) {
                if (m_props == null) {
                    m_props = new Element("properties", "");
                    getElements().put(m_props, null);
                    getIds().put("properties", m_props);
                }
                m_props.addAttribute(new Attribute("pid", m_managedservice));
            }
        }
    }

    /**
     * Parses the @Handler annotation.
     */
    private class HandlerVisitor extends EmptyVisitor implements AnnotationVisitor {

        /**
         * Visit @handler annotation attributes.
         * @param arg0 : annotation attribute name
         * @param arg1 : annotation attribute value
         * @see org.objectweb.asm.commons.EmptyVisitor#visit(java.lang.String, java.lang.Object)
         */
        public void visit(String arg0, Object arg1) {
            if (arg0.equals("name")) {
                m_elem.addAttribute(new Attribute("name", arg1.toString()));
                return;
            }
            if (arg0.equals("namespace")) {
                m_elem.addAttribute(new Attribute("namespace", arg1.toString()));
                return;
            }
            if (arg0.equals("level")) {
                m_elem.addAttribute(new Attribute("level", arg1.toString()));
                return;
            }
            if (arg0.equals("architecture")) {
                m_elem.addAttribute(new Attribute("architecture", arg1.toString()));
                return;
            }
        }
    }

    /**
     * Parse the @HandlerDeclaration annotation.
     */
    private class HandlerDeclarationVisitor extends EmptyVisitor implements AnnotationVisitor {

        /**
         * XML accepted by the handler.
         */
        private String m_value;

        /**
         * Parses the value attribute.
         * @param arg0 'value'
         * @param arg1 the value
         * @see org.objectweb.asm.commons.EmptyVisitor#visit(java.lang.String, java.lang.Object)
         */
        public void visit(String arg0, Object arg1) {
            this.m_value = (String) arg1;
        }

        /**
         * End of the visit.
         * Builds the XML document.
         * @see org.objectweb.asm.commons.EmptyVisitor#visitEnd()
         */
        public void visitEnd() {
            DocumentBuilder builder = getFreshDocumentBuilder();
            InputStream is = new ByteArrayInputStream(m_value.getBytes());
            Document document = null;
            try {
                document = builder.parse(is);
                convertDOMElements(m_elem, document.getDocumentElement());
            } catch (Exception e) {
                System.err.println("[warning] Cannot convert " + m_value + " to iPOJO Elements.");
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    System.err.println("[warning] Cannot close correctly the value input stream");
                }
            }
        }

        /**
         * Converts recursively the given XML Element into an iPOJO Element.
         * @param root iPOJO root Element
         * @param xmlElement DOM Element to be converted
         */
        private void convertDOMElements(final Element root, final org.w3c.dom.Element xmlElement) {
            Element converted = new Element(xmlElement.getLocalName(), xmlElement.getNamespaceURI());
            if (xmlElement.hasAttributes()) {
                NamedNodeMap attributes = xmlElement.getAttributes();
                for (int i = 0; i < attributes.getLength(); i++) {
                    Attr attr = (Attr) attributes.item(i);
                    converted.addAttribute(new Attribute(attr.getName(), attr.getNamespaceURI(), attr.getValue()));
                }
            }
            if (xmlElement.hasChildNodes()) {
                NodeList childs = xmlElement.getChildNodes();
                for (int i = 0; i < childs.getLength(); i++) {
                    convertDOMElements(converted, (org.w3c.dom.Element) childs.item(i));
                }
            }
            root.addElement(converted);
        }
    }
}
