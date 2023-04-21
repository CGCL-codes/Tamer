package de.xmlsicherheit.utils;

import org.eclipse.osgi.util.NLS;

/**
 * <p>Externalized strings for the de.xmlsicherheit.utils package.</p>
 *
 * <p>This plug-in is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.</p>
 *
 * <p>This plug-in is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.</p>
 *
 * <p>You should have received a copy of the GNU Lesser General Public License along
 * with this library;<br>
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA</p>
 *
 * @author Dominik Schadow (info@xml-sicherheit.de), www.xml-sicherheit.de
 * @version 1.6.1, 03.10.2006
 */
public class Messages extends NLS {

    /** The bundle name. */
    private static final String BUNDLE_NAME = "de.xmlsicherheit.utils.messages";

    /**
	 * Constructor.
	 */
    private Messages() {
    }

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    /** XPath popup window externalized strings. */
    public static String XPathSelectionTitle, XPathSelectionError;

    /** Utils externalized strings. */
    public static String ErrorDuringIdSearch;
}
