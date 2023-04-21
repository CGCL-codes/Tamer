package com.loribel.tools.xml.template;

import com.loribel.commons.exception.GB_NullParameterException;
import com.loribel.tools.xml.template.def.GB_XmlGuiTemplate2;

/**
 * Factory to buil templates to build GUI to represent XML Node.
 *
 * @author Gr�gory Borelli
 * @version 2003/09/25 - 10:34:55 - gen 7.11
 */
public class GB_XmlGuiTemplateFactory {

    /**
     * Instance of the singleton.
     */
    private static GB_XmlGuiTemplateFactory instance;

    /**
     * Default constructor for the singleton.
     */
    protected GB_XmlGuiTemplateFactory() {
        super();
    }

    /**
     * Get Instance of the singleton..
     * Use the method <tt>buildInstance()</tt> to init the property the first
     * time.
     *
     * @return GB_XmlGuiTemplateFactory - <tt>instance</tt>
     */
    public static GB_XmlGuiTemplateFactory getInstance() {
        if (instance == null) {
            instance = buildInstance();
        }
        return instance;
    }

    /**
     * Return the instance of the singleton. <br />
     * If there was no initialization with method {@link
     * #initWith(GB_XmlGuiTemplateFactory)}, instance is initialized with
     * default method <tt>buildInstance()/tt>.
     *
     * @return GB_XmlGuiTemplateFactory - <tt>instance</tt>
     */
    private static GB_XmlGuiTemplateFactory buildInstance() {
        return new GB_XmlGuiTemplateFactory();
    }

    /**
     * You must use this static method if you want to use an other
     * initialization than by default. <br />
     * By default, the instance is build by <tt>buildInstance()/tt> and use an
     * instance of this class. <br />
     * @throw GB_NullParameterException : if a_instance is null.
     *
     * @param a_instance GB_XmlGuiTemplateFactory - instance to initialize the singleton
     */
    public static void initWith(GB_XmlGuiTemplateFactory a_instance) {
        if (a_instance == null) {
            throw new GB_NullParameterException(GB_XmlGuiTemplateFactory.class, "iniWith", "a_instance");
        }
        instance = a_instance;
    }

    /**
     * Return a template.
     *
     * @param a_id String -
     *
     * @return GB_XmlGuiTemplate
     */
    public GB_XmlGuiTemplate newXmlGuiTemplate(String a_id) {
        return new GB_XmlGuiTemplate2();
    }
}
