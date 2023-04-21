package com.anaxima.eslink.tools.sasdoc.tags;

import com.anaxima.eslink.tools.sasdoc.ClassDoc;
import com.anaxima.eslink.tools.sasdoc.elements.AbstractElement;

/**
 * Implementierung eines Parsers f�r das &at;author Tag.
 * 
 * @author Thomas Vater
 */
public class AuthorTagParser extends AbstractTagParser {

    /**
	 * Erzeugt und Initialisiert diesen Tag-Parser.
	 */
    public AuthorTagParser() {
        super("author", "Autoren:", true);
    }

    /**
	 * @see com.anaxima.eslink.tools.sasdoc.AbstractTagParser#parse(com.anaxima.eslink.tools.sasdoc.TagData)
	 */
    public String parse(ClassDoc argClassDoc, AbstractElement argClassInfo, int argLevel, TagData argTagData) throws TagException {
        if (!isResponsible(argTagData)) throw new TagParserNotResponsibleException();
        StringBuffer buf = new StringBuffer();
        if (hasSeenTag()) {
            if (!isAllowMultiple()) throw new TagException("@" + this.getName() + "-Tag ist nur einmal erlaubt.");
        } else {
            buf.append("<dt><b>");
            buf.append(getTitle());
            buf.append("</b></dt>\n");
            setSeenTag(true);
        }
        buf.append("<dd>");
        buf.append(argTagData.getText());
        buf.append("</dd>\n");
        return buf.toString();
    }
}
