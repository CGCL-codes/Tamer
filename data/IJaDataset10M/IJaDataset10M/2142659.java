package de.schlund.pfixxml.util;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

public class SimpleResolver implements URIResolver {

    public static Transformer configure(TransformerFactory factory, String resource) throws TransformerConfigurationException {
        URIResolver resolver = new SimpleResolver(factory.getURIResolver());
        factory.setURIResolver(resolver);
        Transformer transformer = factory.newTransformer(new StreamSource(SimpleResolver.class.getResource(resource).toString()));
        transformer.setURIResolver(resolver);
        return transformer;
    }

    private URIResolver defaultResolver;

    public SimpleResolver(URIResolver defaultResolver) {
        this.defaultResolver = defaultResolver;
    }

    public Source resolve(String href, String base) throws TransformerException {
        int idx;
        String ref;
        if (href.contains(":")) {
            ref = href;
        } else {
            idx = base.lastIndexOf('/');
            if (idx == -1) {
                ref = href;
            } else {
                ref = base.substring(0, idx) + "/" + href;
            }
        }
        return defaultResolver == null ? null : defaultResolver.resolve(ref, base);
    }
}
