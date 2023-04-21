package org.jfor.jfor.converter;

import org.jfor.jfor.converter.AbstractBuilder;
import org.jfor.jfor.converter.BuilderContext;
import org.jfor.jfor.converter.IBuilder;
import org.jfor.jfor.rtflib.rtfdoc.IRtfHyperLinkContainer;
import org.jfor.jfor.rtflib.rtfdoc.IRtfTextContainer;
import org.jfor.jfor.rtflib.rtfdoc.RtfAttributes;
import org.jfor.jfor.rtflib.rtfdoc.RtfHyperLink;
import org.jfor.jfor.rtflib.rtfdoc.RtfStyleSheetTable;
import org.xml.sax.Attributes;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * This class build <fo:basic-link> process.
 *
 * @author <a href="mailto:a.putz@skynamics.com">Andreas Putz</a>
 * @version 1.0
 *
 * {\field {\*\fldinst HYPERLINK "http://www.test.de"   }{\fldrslt Joe Smith}}
 */
public class BasicLinkBuilder extends AbstractBuilder {

    /** Name of fo basic link tag */
    private static String FO_BASIC_LINK = "fo:basic-link";

    /** Link */
    private RtfHyperLink link;

    /** Attributes */
    private Attributes attributes = null;

    /**
	 * Default constructor.
	 *
	 * @param ctx a <code>BuilderContext</code> value
	 */
    public BasicLinkBuilder(BuilderContext ctx) {
        super(ctx);
    }

    /**
	 * Return the appropriate builder for given element if we have one.
	 * @param ctx a <code>BuilderContext</code> value
	 * @param rawName a <code>String</code> value
	 * @param attr an <code>Attributes</code> value
	 * @return an <code>IBuilder</code> value
	 */
    public IBuilder getBuilder(BuilderContext ctx, String rawName, Attributes attr) {
        if (rawName.equals(FO_BASIC_LINK)) {
            return new BasicLinkBuilder(ctx);
        } else {
            return null;
        }
    }

    /**
	 * Called by Converter at the start of an element.
	 * @param rawName a <code>String</code> value
	 * @param attr an <code>Attributes</code> value
	 * @exception IOException if an error occurs
	 */
    public void start(String rawName, Attributes attr) throws IOException {
        attributes = attr;
    }

    /**
	 * Called by the parser for Text nodes.
	 * @param str Character string
	 * @exception IOException throwes on error
	 */
    public void characters(String str) throws IOException {
        final IRtfHyperLinkContainer c = (IRtfHyperLinkContainer) m_context.getContainer(IRtfHyperLinkContainer.class, true, this);
        link = c.newHyperLink(str, this.getAttributes());
        if (attributes == null) {
            return;
        }
        int len = attributes.getLength();
        for (int i = 0; i < len; i++) {
            String name = attributes.getQName(i);
            setAttribute(link, attributes.getQName(i), attributes.getValue(i));
        }
    }

    /**
	 * Called by Converter at the end of an element.
	 *
	 * @exception IOException if an error occurs
	 */
    public void end() throws IOException {
    }

    /**
	 * Sets a attribute of the image.
	 *
	 * @param name Name of attribute
	 * @param value Value of attribute
	 *
	 * @return
	 * true    If required attributes are set
	 * false   Required attributes are not complete set
	 *
	 * @exception IOException On attribute error
	 */
    private boolean setAttribute(RtfHyperLink hyperlink, String name, String value) throws IOException {
        boolean successful = false;
        if (name == null || value == null) {
            return successful;
        }
        if (name.equalsIgnoreCase("external-destination")) {
            hyperlink.setExternalURL(value);
        } else if (name.equalsIgnoreCase("internal-destination")) {
            hyperlink.setInternalURL(value);
        }
        return true;
    }

    /**
	 * Gets the attributes of the link.
	 *
	 * @return Format attributes of Stylesheet
	 * @throws ConverterException Throwed on convert error.
	 */
    private RtfAttributes getAttributes() throws ConverterException {
        RtfAttributes rtfAttrs = new RtfAttributes();
        String jClass = getAttribute(attributes, FO_BASIC_LINK, "jfor-class", false);
        if (jClass == null) {
            jClass = RtfStyleSheetTable.getInstance().getDefaultStyleName();
        }
        if (jClass != null) {
            int success = RtfStyleSheetTable.getInstance().addStyleToAttributes(jClass, rtfAttrs);
            if (success == RtfStyleSheetTable.STATUS_DEFAULT) {
                m_context.log.logWarning("jfor-class '" + jClass + "' is not defined, set to default.");
            }
        }
        rtfAttrs = TextAttributesConverter.convertAttributes(attributes, rtfAttrs, m_context.log);
        return rtfAttrs;
    }
}
