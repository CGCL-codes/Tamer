package com.bluebrim.font.shared;

import java.io.*;

/**
 * Postscript data needed to represent this font on a postscript file, including name and
 * font definition.
 * Creation date: (2001-05-03 15:09:45)
 * @author Magnus Ihse <magnus.ihse@appeal.se>
 */
public interface CoFontPostscriptData extends Serializable, com.bluebrim.xml.shared.CoXmlEnabledIF {

    public static final String XML_TAG = "font-postscript-data";

    public static final String XML_POSTSCRIPT_NAME = "name";

    public static final String XML_POSTSCRIPT_DEFINITION = "data";

    /**
	 * Return a byte array containing Postscript source code which, when sent to the Postscript
	 * interpreter, will generate a definition of this font face. */
    public byte[] getPostscriptDefinition();

    /**
	 * Return the name of this font in the Postscript system, as given by the definition in the
	 * Postscript source of getPostscriptDefinition().
	 *
	 * PENDING: This is a lightweight data field, as opposed to the definition. Perhaps this should not
	 * be included here, but rather in the com.bluebrim.font.shared.CoFontFace itself, so the postscript name can be easily
	 * extracted without having to serialize the complete definition.
	 */
    public String getPostscriptName();
}
