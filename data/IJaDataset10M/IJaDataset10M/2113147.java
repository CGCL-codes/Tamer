package net.sf.mxlosgi.mxlosgixmppbundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author noah
 * 
 */
public class StreamFeature implements XMLStanza {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2383602355402602935L;

    private Set<String> mechanisms = new HashSet<String>();

    private Set<String> compressionMethods = new HashSet<String>();

    private List<Feature> features = new ArrayList<Feature>();

    public StreamFeature() {
    }

    public void addFeature(String elementName, String namespace, boolean required) {
        Feature feature = new Feature(elementName, namespace);
        feature.setRequired(required);
        addFeature(feature);
    }

    public void addFeature(Feature feature) {
        features.add(feature);
    }

    public void removeFeature(String elementName, String namespace) {
        for (Iterator<Feature> iterator = features.iterator(); iterator.hasNext(); ) {
            Feature feature = iterator.next();
            String felementName = feature.getElementName();
            String fnamespace = feature.getNamespace();
            if (felementName.equals(elementName) && fnamespace.equals(namespace)) {
                iterator.remove();
            }
        }
    }

    public void removeFeature(Feature feature) {
        removeFeature(feature.getElementName(), feature.getNamespace());
    }

    public Collection<Feature> getFeatures() {
        return Collections.unmodifiableCollection(features);
    }

    public void addMechanism(String mechanism) {
        if (mechanism != null && !mechanism.isEmpty()) {
            mechanisms.add(mechanism);
        }
    }

    public void addMechanismCollection(Collection<String> collection) {
        mechanisms.addAll(collection);
    }

    public void removeMechanism(String mechanism) {
        mechanisms.remove(mechanism);
    }

    public void removeAllMechanism() {
        mechanisms.clear();
    }

    public Collection<String> getMechanisms() {
        return Collections.unmodifiableCollection(mechanisms);
    }

    /**
	 * @return the compressionMethod
	 */
    public Collection<String> getCompressionMethod() {
        return Collections.unmodifiableCollection(compressionMethods);
    }

    /**
	 * @param compressionMethod the compressionMethod to set
	 */
    public void addCompressionMethod(String compressionMethod) {
        if (compressionMethod != null && !compressionMethod.isEmpty()) {
            compressionMethods.add(compressionMethod);
        }
    }

    @Override
    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<stream:features>");
        for (Feature feature : getFeatures()) {
            buf.append(feature.toXML());
        }
        if (!getCompressionMethod().isEmpty()) {
            buf.append("<compression xmlns=\"http://jabber.org/features/compress\">");
            for (Iterator<String> it = getCompressionMethod().iterator(); it.hasNext(); ) {
                buf.append("<method>").append(it.next()).append("</method>");
            }
            buf.append("</compression>");
        }
        if (!mechanisms.isEmpty()) {
            buf.append("<mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>");
            for (Iterator<String> it = mechanisms.iterator(); it.hasNext(); ) {
                buf.append("<mechanism>").append(it.next()).append("</mechanism>");
            }
            buf.append("</mechanisms>");
        }
        buf.append("</stream:features>");
        return buf.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        StreamFeature streamFeature = (StreamFeature) super.clone();
        streamFeature.mechanisms = new HashSet<String>();
        streamFeature.compressionMethods = new HashSet<String>();
        streamFeature.features = new ArrayList<Feature>();
        for (String mechanism : this.mechanisms) {
            streamFeature.mechanisms.add(mechanism);
        }
        for (String compressionMethod : this.compressionMethods) {
            streamFeature.compressionMethods.add(compressionMethod);
        }
        for (Feature feature : this.features) {
            streamFeature.features.add((Feature) feature.clone());
        }
        return streamFeature;
    }

    public static class Feature implements XMLStanza {

        /**
		 * 
		 */
        private static final long serialVersionUID = 467310243624816368L;

        private String elementName;

        private String namespace;

        private boolean required = false;

        /**
		 * @param elementName
		 * @param namespace
		 */
        public Feature(String elementName, String namespace) {
            this.elementName = elementName;
            this.namespace = namespace;
        }

        public String getElementName() {
            return elementName;
        }

        public void setElementName(String elementName) {
            this.elementName = elementName;
        }

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        @Override
        public String toXML() {
            StringBuffer buf = new StringBuffer();
            buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\"");
            if (isRequired()) {
                buf.append(">").append("<required/>").append("</").append(getElementName()).append(">");
            } else {
                buf.append("/>");
            }
            return buf.toString();
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            Feature feature = (Feature) super.clone();
            feature.elementName = this.elementName;
            feature.namespace = this.namespace;
            feature.required = this.required;
            return feature;
        }
    }
}
