package org.jtools.config.repository;

import java.net.URL;
import java.util.Collection;
import org.jpattern.xml.QName;
import org.jtools.config.ConfigUtils;
import org.jtools.config.protocol.ConfigProtocol;
import org.jtools.config.protocol.ConfigProtocolFactory;
import org.jtools.config.protocol.ForwardingConfigProtocol;
import org.jtools.io.path.PathEntry;

public class RepositoryConfigProtocolFactory implements ConfigProtocolFactory {

    static class Protocol extends ForwardingConfigProtocol {

        private RepositoryElement root = null;

        public Protocol(ConfigProtocol delegate) {
            super(delegate);
        }

        @Override
        public Object onBeginChildElement(Object parent, QName name, boolean[] invoked) throws Exception {
            if (root == null && "repository".equalsIgnoreCase(name.getLocalPart())) {
                this.root = new RepositoryElementImpl();
                invoked[0] = true;
                return this.root;
            }
            return super.onBeginChildElement(parent, name, invoked);
        }

        @Override
        public void onEndChildElement(Object parent, QName name, Object element, boolean invoked) throws Exception {
            if (element == root) {
                process(parent);
                return;
            }
            super.onEndChildElement(parent, name, element, invoked);
        }

        private void process(Object parent) throws Exception {
            Collection<RepositoryEntry> entries = root.getEntries();
            for (RepositoryEntry entry : entries) processEntry(parent, entry, getConfigLoader().getSource());
        }

        protected void processEntry(Object parent, RepositoryEntry entry, URL parentUrl) throws Exception {
            Collection<PathEntry<?>> files = entry.getEntries(parentUrl);
            for (PathEntry<?> file : files) processFile(parent, entry, file.getURI().toURL());
        }

        protected void processFile(Object parent, RepositoryEntry entry, URL url) throws Exception {
            System.out.println("processing: " + url);
            QName elementName = entry.getElementDescriptor().getName();
            boolean[] invoked = { false };
            Object result = onBeginChildElement(parent, elementName, invoked);
            if (result == null) {
                throw new RuntimeException("unable to create element " + elementName + " from class " + parent.getClass().getName());
            }
            ConfigUtils.load(result, url);
            onEndChildElement(parent, elementName, result, invoked[0]);
        }
    }

    public String getURI() {
        return "http://schema.jtools.org/repository/";
    }

    public ConfigProtocol newInstance(ConfigProtocol defaultProtocol) {
        return new Protocol(defaultProtocol);
    }
}
