package org.makumba.controller;

import java.util.HashMap;

/**
 * A subclass of a {@link HashMap}, to return a unique type of map in
 * {@link Logic#computeActor(String, org.makumba.Attributes, String, org.makumba.commons.DbConnectionProvider)}.
 * 
 * @author Rudolf Mayer
 * @version $Id: MakumbaActorHashMap.java,v 1.1 Oct 5, 2008 3:45:01 PM rudi Exp $
 */
public class MakumbaActorHashMap extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;
}
