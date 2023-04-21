package joelib2.io.types.chemrss;

import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX2 implementation for a RSS handler.
 *
 * @.author  egonw
 */
public class RSSHandler extends DefaultHandler {

    private String cData;

    private String cmlString;

    private String itemDate;

    private String itemDesc;

    private String itemLink;

    private String itemTitle;

    private boolean readdedNamespace;
}
