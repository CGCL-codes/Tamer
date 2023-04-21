package org.apache.myfaces.view.facelets.tag;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

/**
 * Delegate class for TagLibraries
 * 
 * @see TagLibrary
 * @author Jacob Hookom
 * @version $Id: TagHandlerFactory.java,v 1.4 2008/07/13 19:01:35 rlubke Exp $
 */
interface TagHandlerFactory {

    /**
     * A new TagHandler instantiated with the passed TagConfig
     * 
     * @param cfg
     *            TagConfiguration information
     * @return a new TagHandler
     * @throws FacesException
     * @throws ELException
     */
    public TagHandler createHandler(TagConfig cfg) throws FacesException, ELException;
}
