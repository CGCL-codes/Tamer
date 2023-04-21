package com.sun.facelets.tag;

import javax.el.ELException;
import javax.faces.FacesException;

/**
 * Delegate class for TagLibraries
 * 
 * @see TagLibrary
 * @author Jacob Hookom
 * @version $Id: TagHandlerFactory.java,v 1.3 2005/08/24 04:38:48 jhook Exp $
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
