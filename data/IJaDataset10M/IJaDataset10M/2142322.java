package net.sf.mxlosgi.mxlosgixmppparserbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;
import org.xmlpull.v1.XmlPullParser;

/**
 * @author noah
 *
 */
public interface ExtensionParser {

    /**
	 * 
	 * @return
	 */
    public String getElementName();

    /**
	 * 
	 * @return
	 */
    public String getNamespace();

    /**
	 * 
	 * @param parser
	 * @return
	 */
    public PacketExtension parseExtension(XmlPullParser parser, XMPPParser xmppParser) throws Exception;
}
