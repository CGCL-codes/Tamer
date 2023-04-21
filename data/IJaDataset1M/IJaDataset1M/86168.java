package net.sf.mxlosgi.disco;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.sf.mxlosgi.utils.StringUtils;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmpp.XmlStanza;

/**
 * @author noah
 * 
 */
public class DiscoInfoPacketExtension implements PacketExtension {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8035400809891387928L;

    public static final String ELEMENTNAME = "query";

    public static final String NAMESPACE = "http://jabber.org/protocol/disco#info";

    private List<Feature> features = Collections.synchronizedList(new LinkedList<Feature>());

    private List<Identity> identities = Collections.synchronizedList(new LinkedList<Identity>());

    private String node;

    private List<PacketExtension> packetExtensions = new CopyOnWriteArrayList<PacketExtension>();

    public DiscoInfoPacketExtension() {
    }

    public void addFeature(Feature feature) {
        features.add(feature);
    }

    public Feature[] getFeatures() {
        return features.toArray(new Feature[] {});
    }

    public boolean containFeature(Feature feature) {
        return features.contains(feature);
    }

    public boolean containFeature(String strFeature) {
        for (Feature feature : getFeatures()) {
            String f = feature.getFeature();
            if (f.equals(strFeature)) {
                return true;
            }
        }
        return false;
    }

    public void addIdentity(String category, String type, String name) {
        identities.add(new Identity(category, type, name));
    }

    public void addIdentity(Identity identity) {
        identities.add(identity);
    }

    public Identity[] getIdentities() {
        return identities.toArray(new Identity[] {});
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    @Override
    public String getElementName() {
        return ELEMENTNAME;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    /**
	 * Returns an unmodifiable collection of the packet extensions
	 * attached to the packet.
	 * 
	 * @return the packet extensions.
	 */
    public synchronized PacketExtension[] getExtensions() {
        return packetExtensions.toArray(new PacketExtension[] {});
    }

    /**
	 * Returns the first extension of this packet that has the given
	 * namespace.
	 * 
	 * @param namespace
	 *                  the namespace of the extension that is desired.
	 * @return the packet extension with the given namespace.
	 */
    public PacketExtension getExtension(String namespace) {
        return getExtension(null, namespace);
    }

    /**
	 * Returns the first packet extension that matches the specified
	 * element name and namespace, or <tt>null</tt> if it doesn't exist.
	 * 
	 * @param elementName
	 *                  the XML element name of the packet extension. (May
	 *                  be null)
	 * @param namespace
	 *                  the XML element namespace of the packet extension.
	 * @return the extension, or <tt>null</tt> if it doesn't exist.
	 */
    public PacketExtension getExtension(String elementName, String namespace) {
        if (namespace == null) {
            return null;
        }
        for (PacketExtension ext : packetExtensions) {
            if ((elementName == null || elementName.equals(ext.getElementName())) && namespace.equals(ext.getNamespace())) {
                return ext;
            }
        }
        return null;
    }

    /**
	 * Adds a packet extension to the packet.
	 * 
	 * @param extension
	 *                  a packet extension.
	 */
    public void addExtension(PacketExtension extension) {
        if (extension != null) {
            packetExtensions.add(extension);
        }
    }

    /**
	 * Removes a packet extension from the packet.
	 * 
	 * @param extension
	 *                  the packet extension to remove.
	 */
    public void removeExtension(PacketExtension extension) {
        packetExtensions.remove(extension);
    }

    /**
	 * Returns the extension sub-packets (including properties data) as an
	 * XML String, or the Empty String if there are no packet extensions.
	 * 
	 * @return the extension sub-packets as XML or the Empty String if
	 *         there are no packet extensions.
	 */
    protected synchronized String getExtensionsXML() {
        StringBuilder buf = new StringBuilder();
        for (PacketExtension extension : getExtensions()) {
            buf.append(extension.toXML());
        }
        return buf.toString();
    }

    @Override
    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\"");
        if (node != null) {
            buf.append(" node=\"" + StringUtils.escapeForXML(node) + "\"");
        }
        buf.append(">");
        for (Iterator<Identity> it = identities.iterator(); it.hasNext(); ) {
            Identity identity = it.next();
            buf.append(identity.toXML());
        }
        for (Iterator<Feature> it = features.iterator(); it.hasNext(); ) {
            buf.append(it.next().toXML());
        }
        buf.append(getExtensionsXML());
        buf.append("</" + getElementName() + ">");
        return buf.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        DiscoInfoPacketExtension extension = (DiscoInfoPacketExtension) super.clone();
        extension.features = Collections.synchronizedList(new LinkedList<Feature>());
        for (Feature feature : this.features) {
            extension.features.add((Feature) feature.clone());
        }
        extension.identities = Collections.synchronizedList(new LinkedList<Identity>());
        for (Identity identity : this.identities) {
            extension.identities.add((Identity) identity.clone());
        }
        return extension;
    }

    /**
	 * @author noah
	 *
	 */
    public static class Feature implements XmlStanza {

        /**
		 * 
		 */
        private static final long serialVersionUID = 5108018446162031793L;

        private String feature;

        /**
		 * @param feature
		 */
        public Feature(String feature) {
            this.feature = feature;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        @Override
        public String toXML() {
            StringBuffer buf = new StringBuffer();
            if (feature != null) {
                buf.append("<feature var=\"" + feature + "\"/>");
            }
            return buf.toString();
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            Feature feature = (Feature) super.clone();
            feature.feature = this.feature;
            return feature;
        }
    }

    public static class Identity implements XmlStanza {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1898334119766187045L;

        private String category;

        private String type;

        private String name;

        public Identity(String category, String type) {
            super();
            this.category = category;
            this.type = type;
        }

        public Identity(String category, String type, String name) {
            super();
            this.category = category;
            this.type = type;
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toXML() {
            StringBuffer buf = new StringBuffer();
            buf.append("<identity");
            if (category != null) {
                buf.append(" category=\"" + category + "\"");
            }
            if (type != null) {
                buf.append(" type=\"" + type + "\"");
            }
            if (name != null) {
                buf.append(" name=\"" + name + "\"");
            }
            buf.append("/>");
            return buf.toString();
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            Identity identity = (Identity) super.clone();
            identity.category = this.category;
            identity.type = this.type;
            identity.name = this.name;
            return identity;
        }
    }
}
