package net.sf.traser.common.identification;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import net.sf.traser.common.Identifier;
import net.sf.traser.common.Initializable;
import org.apache.axiom.om.OMElement;

/**
 * This class implements the resolver interface and is capable of resolving SSCC
 * identifiers based on mapping from company prefixes to server addresses 
 * obatined from local configuration.
 * @author Marcell Szathmari
 */
public class LocalDefSSCCResolver implements Resolver, Initializable {

    /**
     * This field is used to LOG messages.
     */
    private static final Logger LOG = Logger.getLogger("net.sf.traser.decoder.impl.SSCCDecoder");

    /**
     *
     */
    private Map<String, URI> compPrefixes = new HashMap<String, URI>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(OMElement config) {
        Iterator<?> iter = config.getChildrenWithName(new QName("company"));
        OMElement pref;
        while (iter.hasNext()) {
            pref = (OMElement) iter.next();
            String uri = pref.getText();
            String prefix = pref.getAttributeValue(new QName("prefix"));
            try {
                compPrefixes.put(prefix, new URI(uri));
            } catch (URISyntaxException ex) {
                LOG.log(Level.SEVERE, "Provided address (" + pref.getText() + ") for company prefix (" + prefix + ") is not a proper URI.", ex);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identifier resolve(String rep) throws SchemeException {
        if (!rep.startsWith("(00)")) {
            throw new SchemeException("Not an SSCC code. All SSCC codes start with (00)");
        }
        if (rep.length() != 22) {
            throw new SchemeException("Not an SSCC code. All SSCC codes are 22 digits long");
        }
        String id = rep.substring(5);
        for (int i = 1; i < id.length(); ++i) {
            String tmp = id.substring(0, i);
            URI uri = compPrefixes.get(tmp);
            if (uri != null) {
                return new SimpleSSCCIdentifier(rep, uri);
            }
        }
        throw new SchemeException("Company prefix could not be resolved.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identifier tryResolve(String rep) {
        if (rep != null) {
            if (!rep.startsWith("(00)")) {
                return null;
            }
            if (rep.length() != 22) {
                return null;
            }
            String id = rep.substring(5);
            for (int i = 1; i < id.length(); ++i) {
                String tmp = id.substring(0, i);
                URI uri = compPrefixes.get(tmp);
                if (uri != null) {
                    return new SimpleSSCCIdentifier(rep, uri);
                }
            }
        }
        return null;
    }

    /**
     * Basic implementation of Identifier, used by the resolve method.
     */
    private static class SimpleSSCCIdentifier implements Identifier {

        /**
         * Field holding value of the id part of the identifier. 
         */
        private String id;

        /**
         * Field holding value of the uriPart part of the identifier.
         */
        private URI uri;

        /**
         * {@inheritDoc}
         */
        public String getId() {
            return id;
        }

        /**
         * {@inheritDoc}
         */
        public URI getHost() {
            return uri;
        }

        /**
         * The constructor that takes the id and uri of the identifier to make.
         * @param id the id of the identifier.
         * @param uri the uri of the identifier.
         */
        public SimpleSSCCIdentifier(String id, URI uri) {
            this.id = id;
            this.uri = uri;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return id;
        }
    }
}
