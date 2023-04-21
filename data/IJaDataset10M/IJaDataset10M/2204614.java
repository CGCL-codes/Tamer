package org.plazmaforge.bsolution.personality.client.swing;

import org.plazmaforge.bsolution.personality.client.swing.resources.GUIPersonalityResources;

/**
 * @author Oleh Hapon
 * Date: 17.09.2004
 * Time: 7:47:35
 * $Id: GUIPersonalityEnvironment.java,v 1.2 2010/04/28 06:28:24 ohapon Exp $
 */
public class GUIPersonalityEnvironment {

    private static GUIPersonalityResources resources;

    static {
        resources = new GUIPersonalityResources();
    }

    public static GUIPersonalityResources getResources() {
        return resources;
    }
}
