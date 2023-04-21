package de.xmlsicherheit.utils;

/**
 * <p>Contains global variables and their for the XML-Security Plug-In.</p>
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
public class Globals {

    /** The maximum number of characters in the password field. */
    public static final int MAX_CHARACTERS_PASSWORD = 20;

    /** Maximum length of signature and encryption ID. */
    public static final int ID_LIMIT = 20;

    /** Maximum length of certificate information (like OU). */
    public static final int CERTIFICATE_LIMIT = 50;

    /** Maximum length of keystore information (like alias). */
    public static final int KEYSTORE_DATA_LIMIT = 20;

    /** Group numerator. */
    public static final int GROUP_NUMERATOR = 100;

    /** Margin for GUI elements. */
    public static final int MARGIN = 5;

    /** Large margin for GUI elements. */
    public static final int LARGE_MARGIN = 10;

    /** Default normal button width. */
    public static final int BUTTON_WIDTH = 70;

    /** Default large button width. */
    public static final int LARGE_BUTTON_WIDTH = 100;

    /** Default button height. */
    public static final int BUTTON_HEIGHT = 18;

    /** Default combo width. */
    public static final int COMBO_WIDTH = 150;

    /** Default combo margin. */
    public static final int COMBO_MARGIN = 20;

    /** Default echo button width. */
    public static final int ECHO_BUTTON_WIDTH = 20;

    /** Default echo button height. */
    public static final int ECHO_BUTTON_HEIGHT = 15;

    /** Default short text width. */
    public static final int SHORT_TEXT_WIDTH = 200;

    /** Default large text width. */
    public static final int LARGE_TEXT_WIDTH = 225;

    /** Default extension name for JKS dialog. */
    public static final String[] JKS_EXTENSION_NAME = { "Java Keystore (*.jks)" };

    /** Default extension for JKS dialog. */
    public static final String[] JKS_EXTENSION = { "*.jks" };

    /** Default extension name for key file dialog. */
    public static final String[] KEY_FILE_EXTENSION_NAME = { "Key File (*.*)" };

    /** Default extension for key file dialog. */
    public static final String[] KEY_FILE_EXTENSION = { "*.*" };

    /** Default extensions for detached file dialog. */
    public static final String[] DETACHED_FILE_EXTENSION = { "*.xml", "*" };

    /** Default extension names for detached file dialog. */
    public static final String[] DETACHED_FILE_EXTENSION_NAME = { "XML document (*.xml)", "All files (*.*)" };

    /** The icon for the echo password button. */
    public static final String ECHO_PASSWORD = "icons/but_echo_password.gif";
}
