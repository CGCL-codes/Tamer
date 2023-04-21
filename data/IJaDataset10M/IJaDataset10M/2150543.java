package org.jabber.jabberbeans.sax;

import org.xml.sax.*;

/** 
 * Each instance of this class is responsible for handling a single XML
 * namesapce, or element type, All packet and extension types are represented
 * by SubHandlers, and each subhandler per connection has an
 * HandlerEntry.
 *
 * @author  David Waite <a href="mailto:dwaite@jabber.com">
 *                      <i>&lt;dwaite@jabber.com&gt;</i></a>
 * @author  $Author: dwaite $
 * @version $Revision: 1.1 $ 
 */
public class HandlerEntry {

    /** textual description of extension */
    private String description;

    /** element, if this is an element (packet) type, otherwise null */
    private String element;

    /** xmlns, if this is an extension type, otherwise null */
    private String xmlns;

    /** <code>String</code> holding the name of the Class which is handler 
     * for XML parsing of this packet or extension*/
    private String handler;

    /** <code>String</code> holding the name of the Class which is used to
     * build an instance of this type, if one is easily identified. Otherwise
     * null */
    private String builder;

    /** <code>String</code> holding the name of the Class which is the output
     * of the handler (and builder, if there is one) of this Class */
    private String product;

    /** simple lazy cache of the handler to save on expensive 
     * Class.forName.newInstance() calls when doing a create.
     * 
     * Note that because this is one instance, there will be duplication of
     * the HandlerEntry classes per connection */
    private SubHandler handlerClass;

    /** The prefix of the entries in the .properties file, used for two-round 
     *  lookup */
    private String propPrefix;

    /** 
     * used to output a debug of the data in the Entry 
     *
     * @return <code>String</code> with some data on this Entry
     */
    public String toString() {
        return new String(((element != null) ? ("element:" + element) : "") + ((xmlns != null) ? ("xmlns:" + xmlns) : "") + "|" + description);
    }

    /**
     * Creates a new <code>HandlerEntry</code> instance.
     *
     * @param propPrefix a <code>String</code> with the prefix in the
     *                   .properties file
     * @param xmlns a <code>String</code> with the XML namespace of the
     *              element, if any.
     * @param element a <code>String</code> with the element name, if any
     */
    public HandlerEntry(String propPrefix, String xmlns, String element) {
        this.xmlns = xmlns;
        this.element = element;
        this.propPrefix = propPrefix;
        builder = null;
        handlerClass = null;
        handler = null;
        product = null;
    }

    /**
     * returns a hash value for entry into a HashTable
     *
     * @return int hash value
     */
    public int hashCode() {
        if (xmlns != null) return xmlns.hashCode(); else return element.hashCode();
    }

    /**
     * gets the XML namespace of associated with this entry, if any
     *
     * @return a <code>String</code> value, or null if no namespace
     */
    public String getXMLNS() {
        return xmlns;
    }

    /**
     * gets the element associated with this entry, if any
     *
     * @return a <code>String</code> value, or null if no element
     */
    public String getElement() {
        return element;
    }

    /**
     * get the Prefix associated with this entry in the .properties file
     *
     * @return a <code>String</code> value
     */
    public String getPrefix() {
        return propPrefix;
    }

    /**
     * get the name of the class which is used to build instances of the
     * product
     *
     * @return a <code>String</code> value, or null if none needed
     */
    public String getBuilder() {
        return builder;
    }

    /**
     * set the name of the class which is used to build instances of the
     * product
     *
     * @param b a <code>String</code> value, or null if none needed
     */
    public void setBuilder(String b) {
        builder = b;
    }

    /**
     * get the name of the handler which is used to process XML and
     * create instances of the product
     *
     * @return a <code>String</code> value
     */
    public String getHandler() {
        return handler;
    }

    /**
     * set the name of the handler which is used to process XML and
     * create instances of the product
     *
     * @param handler a <code>String</code> value
     */
    public synchronized void setHandler(String handler) {
        this.handler = handler;
        handlerClass = null;
    }

    /**
     * get the name of the class which is the product of the builder and
     * handler, if only one class is the result
     *
     * @return a <code>String</code> value, or null if more than one
     *         possible
     */
    public String getProduct() {
        return product;
    }

    /**
     * set the name of the class which is the product of the builder and
     * handler, if only one class is the result
     *
     * @param product a <code>String</code> value, or null if more than one
     *                possible
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * get the textual description of what this element or namespace is
     * used for
     *
     * @return a <code>String</code> description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the textual description of what this element or namespace is
     * used for
     *
     * @param desc a <code>String</code> description
     */
    public void setDescription(String desc) {
        description = desc;
    }

    /**
     * get an instance of the handler for this entry - if none exists so
     * far, one will be instantiated. Note that you will need to create
     * a new instance if you are going to do something like handle the
     * same type of tag twice.
     *
     * @return a <code>SubHandler</code> value
     * @exception ClassNotFoundException if the class does not exist
     * @exception InstantiationException if there is a problem creating 
     *            the class
     * @exception IllegalAccessException if there is an access problem
     *            with the class (such as it not being declared public)
     */
    public SubHandler getHandlerClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (handlerClass == null) handlerClass = (SubHandler) Class.forName(handler).newInstance();
        return handlerClass;
    }

    /**
     * creates a new instance of the handler for this entry, independant
     * of the internal one-item cache.
     *
     * @return a new <code>SubHandler</code>
     * @exception ClassNotFoundException if the class does not exist
     * @exception InstantiationException if there is a problem creating 
     *            the class
     * @exception IllegalAccessException if there is an access problem
     *            with the class (such as it not being declared public)
     */
    public SubHandler newInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return (SubHandler) Class.forName(handler).newInstance();
    }
}
