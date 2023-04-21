package org.jfree.resources;

import java.util.ListResourceBundle;

/**
 * Localised resources for the JCommon Class Library.
 */
public class JCommonResources extends ListResourceBundle {

    /**
     * Default constructor.
     */
    public JCommonResources() {
    }

    /**
     * Returns the array of strings in the resource bundle.
     *
     * @return The resources.
     */
    public Object[][] getContents() {
        return CONTENTS;
    }

    /** The resources to be localised. */
    private static final Object[][] CONTENTS = { { "project.name", "JCommon" }, { "project.version", "1.0.14" }, { "project.info", "http://www.jfree.org/jcommon/" }, { "project.copyright", "(C)opyright 2000-2008, by Object Refinery Limited and Contributors" } };
}
