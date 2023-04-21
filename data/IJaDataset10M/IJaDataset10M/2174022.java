package org.jabber.jabberbeans.sax.Extension;

import org.jabber.jabberbeans.Extension.*;
import org.jabber.jabberbeans.sax.SubHandler;
import org.xml.sax.SAXException;
import org.xml.sax.AttributeList;

/**
 * Handler class to build jabber:iq:search (result) objects.
 *
 * @author  David Waite <a href="mailto:dwaite@jabber.com">
 *                      <i>&lt;dwaite@jabber.com&gt;</i></a>
 * @author  $Author: mass $
 * @version $Revision: 1.6 $ 
 */
class IQSearchResultHandler extends SubHandler {

    /** inner handler for building search results */
    SearchResultHandler itemHandler;

    /** builder for building an IQSearchResult extension */
    IQSearchResultBuilder builder;

    /**
     * Creates a new <code>IQSearchResultHandler</code> instance.
     */
    public IQSearchResultHandler() {
        itemHandler = new SearchResultHandler();
        builder = new IQSearchResultBuilder();
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
        builder.reset();
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
        setChildSubHandler(itemHandler, name, attributes);
    }

    /**
     * <code>handleEndElement</code> is overloaded by the new class to
     * provide logic to handle element code.
     *
     * @param name a <code>String</code> value
     * @exception SAXException if an error occurs
     */
    protected void handleEndElement(String name) throws SAXException {
    }

    /**
     * Stophandler is the same as end element, except that it is called saying
     * that the subhandler is no longer in scope.
     *
     * @param Object a value being returned to the parent - the parent is 
     * meant to interpret this result.
     */
    protected Object stopHandler(String name) {
        return builder.build();
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
        builder.addSearchResult((SearchResult) o);
    }
}
