package net.eejits.ejbgen;

import java.io.*;
import java.util.Vector;
import net.eejits.io.*;

/**
 * The <code>BeanClass</code> is responsible for generating the source file 
 * for a bean's implementation. A bean will be based on the entity that is 
 * specified when the object is created.
 * 
 * @author Nick Sharples
 */
public class BeanClass implements ClassGenerator {

    /**
	 * The template map of sources that releate to a bean class file
	 */
    private static TemplateMap TEMPLATE_MAP;

    static {
        try {
            TEMPLATE_MAP = new TemplateMap("bean-templates.xml", BeanClass.class);
        } catch (IOException ex) {
            System.err.println("[BeanClass] Could not load templates: " + ex.getMessage());
        }
    }

    /**
	 * Return the templates that relate to the bean class file
	 */
    public static TemplateMap getTemplateMap() {
        return TEMPLATE_MAP;
    }

    /**
	 * The entity that the bean will represent
	 */
    private Entity m_entity;

    /**
	 * Create an instance of the <code>BeanClass</code> for the specifed entity
	 * 
	 * @param ent the entity on which to base the bean
	 */
    public BeanClass(Entity ent) {
        this.m_entity = ent;
    }

    /**
	 * Returns the name of the class
	 * @return the name of the class
	 */
    public String getClassName() {
        return this.m_entity.getPackageName() + "." + this.m_entity.getObjectName() + "Bean";
    }

    /**
	 * Generates the implementation of the bean's load method
	 * @return a String representation of the method implementation
	 */
    public String getLoadImp() {
        StringBuffer imps = new StringBuffer();
        Attribute[] atts = this.m_entity.getAttributes();
        for (int ndx = 0; ndx < atts.length; ndx++) {
            imps.append(atts[ndx].getLoadImplementation());
        }
        return imps.toString();
    }

    /**
	 * Generates the implementation of the bean's store method
	 * @return a String representation of the method implementation
	 */
    public String getStoreImp() {
        StringBuffer imps = new StringBuffer();
        Attribute[] atts = this.m_entity.getAttributes();
        for (int ndx = 0; ndx < atts.length; ndx++) {
            imps.append(atts[ndx].getStoreImplemenation());
        }
        return imps.toString();
    }

    /**
	 * Returns the source text for the bean
	 * @return the text for the source file
	 */
    public String getSource() {
        StringWriter stringOut = new StringWriter();
        StringBuffer impl = new StringBuffer();
        impl.append("// Create implementations ...\n");
        CreateMethod[] creators = this.m_entity.getCreateMethods();
        for (int ndx = 0; ndx < creators.length; ndx++) impl.append(creators[ndx].getImplementation());
        impl.append("// ... end create implementations\n");
        impl.append("\n");
        impl.append("// Attribute implementations ...\n");
        Attribute[] attrs = this.m_entity.getAttributes();
        for (int ndx = 0; ndx < attrs.length; ndx++) impl.append(attrs[ndx].getBeanImplementation());
        impl.append("// ... end attribute implementations\n");
        try {
            StringReplaceWriter beanOut = new StringReplaceWriter(stringOut, TemplateMap.getToken("object"), this.m_entity.getObjectName());
            StringReplaceWriter extendsOut = null;
            if (this.m_entity.getExtends() != null) extendsOut = new StringReplaceWriter(beanOut, TemplateMap.getToken("extends"), " extends " + this.m_entity.getExtends()); else extendsOut = new StringReplaceWriter(beanOut, TemplateMap.getToken("extends"), "");
            StringReplaceWriter pkgOut = new StringReplaceWriter(extendsOut, TemplateMap.getToken("package"), this.m_entity.getPackageName());
            StringReplaceWriter impOut = new StringReplaceWriter(pkgOut, TemplateMap.getToken("getset.imp"), impl.toString());
            StringReplaceWriter loadOut = new StringReplaceWriter(impOut, TemplateMap.getToken("load.imp"), this.getLoadImp());
            StringReplaceWriter storeOut = new StringReplaceWriter(loadOut, TemplateMap.getToken("store.imp"), this.getStoreImp());
            CRLFWriter crlfOut = new CRLFWriter(storeOut);
            crlfOut.write(TEMPLATE_MAP.getValue("bean.class"));
        } catch (Exception ex) {
            System.err.println("[BeanClass] Could not write source: " + ex.getMessage());
        }
        return stringOut.getBuffer().toString();
    }
}
