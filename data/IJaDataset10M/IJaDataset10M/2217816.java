package com.beem.project.beem.smack.avatar;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * A PacketExtensionProvider to parse the Avatar data.
 * XML namespace urn:xmpp:avatar:data
 */
public class AvatarProvider implements PacketExtensionProvider {

    /**
	 * Creates a new AvatarProvider.
	 * ProviderManager requires that every PacketExtensionProvider has a public, no-argument constructor
	 */
    public AvatarProvider() {
    }

    @Override
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        AvatarMetadataExtension metadata = new AvatarMetadataExtension();
        boolean done = false;
        StringBuilder buffer = new StringBuilder();
        while (!done) {
            int eventType = parser.getEventType();
            if (eventType == XmlPullParser.START_TAG) {
                if ("data".equals(parser.getName())) {
                    String data = parser.nextText();
                    AvatarExtension avatar = new AvatarExtension(data);
                    return avatar;
                }
            }
            parser.next();
        }
        return null;
    }
}
