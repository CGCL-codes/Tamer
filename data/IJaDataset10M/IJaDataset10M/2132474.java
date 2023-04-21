package org.jabber.jabberbeans.sax.Extension;

import org.jabber.jabberbeans.Extension.AgentBuilder;
import org.jabber.jabberbeans.sax.SubHandler;
import org.xml.sax.*;

/**
 * Handler class to build jabber:iq:agent objects
 *
 * @author  David Waite <a href="mailto:dwaite@jabber.com">
 *                      <i>&lt;dwaite@jabber.com&gt;</i></a>
 * @author  $Author: mass $
 * @version $Revision: 1.5 $ 
 */
public class IQAgentHandler extends SubHandler {

    /** Agent object builder */
    private AgentBuilder builder;

    /** used to capture data between element tags */
    StringBuffer elementChars;

    /**
     * Creates a new <code>IQAgentHandler</code> instance.
     */
    public IQAgentHandler() {
        super();
        builder = new AgentBuilder();
    }

    /**
     * This is an exact copy of the characters function in the main handler
     *
     * @param ch character string detected
     * @param start start position
     * @param length length of string
     * @exception SAXException thrown on error
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        elementChars.append(ch, start, length);
    }

    /**
     * Gets called when the underlying engine decides to pass an entity and
     * all sub-entities off to your subhandler.<p>
     *
     * Upon seeing the element that this subhandler handles, we call this
     * constructor, passing in the attributes.
     *
     * @param name name of the element which we are handling.
     * @param attributes list of attributes on this element
     */
    protected void startHandler(String name, AttributeList attributes) throws SAXException {
        elementChars = new StringBuffer();
        builder.reset();
        builder.setIQAgent(true);
    }

    /**
     * <code>handleStartElement</code> is overloaded by the new class to
     * provide logic to handle the element code.
     *
     * @param name a <code>String</code> value
     * @param attributes an <code>AttributeList</code> value
     * @exception SAXException if an error occurs
     */
    protected void handleStartElement(String name, AttributeList attributes) throws SAXException {
        elementChars = new StringBuffer();
    }

    /**
     * <code>handleEndElement</code> is overloaded by the new class to
     * provide logic to handle element code.
     *
     * @param name a <code>String</code> value
     * @exception SAXException if an error occurs
     */
    protected void handleEndElement(String name) throws SAXException {
        if (name.equals("name")) builder.setName(new String(elementChars));
        if (name.equals("description")) builder.setDescription(new String(elementChars));
        if (name.equals("service")) builder.setService(new String(elementChars));
        if (name.equals("transport")) builder.setTransport(new String(elementChars));
        if (name.equals("register")) builder.setRegister(true);
        if (name.equals("search")) builder.setSearchable(true);
    }

    /**
     * Stophandler is the same as end element, except that it is called saying
     * that the subhandler is no longer in scope.
     *
     * @param Object a value being returned to the parent - the parent is 
     * meant to interpret this result.
     */
    protected Object stopHandler(String name) throws SAXException {
        try {
            return builder.build();
        } catch (InstantiationException e) {
            e.fillInStackTrace();
            throw new SAXException(e);
        }
    }

    /**
     * <code>receiveChildData</code> is called when a child handler exits,
     * returning control to this code. The now-defunct handler along with the
     * data object are both returned.
     *
     * @param subHandler a <code>SubHandler</code> value
     * @param o an <code>Object</code> value
     */
    protected void receiveChildData(SubHandler subHandler, Object o) {
    }
}
