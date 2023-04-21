package org.omegat.filters3.xml.openxml;

import org.omegat.filters3.xml.DefaultXMLDialect;

/**
 * Dialect of Open XML files.
 *
 * @author Didier Briel
 */
public class OpenXMLDialect extends DefaultXMLDialect {

    /**
     * Actually defines the dialect.
     * It cannot be done during creation, because options are not known
     * at that step.
     */
    public void defineDialect(OpenXMLOptions options) {
        defineParagraphTags(new String[] { "w:p", "w:tab", "w:br", "si", "a:p" });
        if (options.getTranslateHiddenText()) defineOutOfTurnTag("w:instrText"); else defineIntactTag("w:instrText");
        defineIntactTags(new String[] { "authors", "p:attrName", "a:tableStyleId", "wp:align", "w:drawing" });
    }
}
