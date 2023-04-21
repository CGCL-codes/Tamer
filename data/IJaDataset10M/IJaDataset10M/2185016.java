package org.exist.eclipse.xquery.core;

import org.eclipse.dltk.core.ScriptNature;

/**
 * Here you find the nature for the xquery project. This nature is registered as
 * an extension point.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryNature extends ScriptNature {

    /**
	 * The natureid of the xquery project.
	 */
    public static final String NATURE_ID = XQueryCorePlugin.PLUGIN_ID + ".nature";
}
