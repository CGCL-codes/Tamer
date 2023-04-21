package org.arastreju.core.model.semantic;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.arastreju.api.ontology.Namespace;
import org.arastreju.api.ontology.namespace.MutableNamespace;
import org.arastreju.api.security.Identity;
import de.lichtflut.infra.Infra;

/**
 * Persistent representation of a Namespace.
 * 
 * Created: 11.07.2008 
 *
 * @author Oliver Tigges
 */
@NamedQueries({ @NamedQuery(name = NamespaceDBO.FIND_BY_URI, query = "SELECT n FROM NamespaceDBO n WHERE n.uri = :uri"), @NamedQuery(name = NamespaceDBO.FIND_ALL, query = "SELECT n FROM NamespaceDBO n") })
@javax.persistence.Entity
@Table(name = "NAMESPACE")
public class NamespaceDBO implements MutableNamespace, Comparable<Namespace> {

    public static final String FIND_BY_URI = "NamespaceDBO:findByUri";

    public static final String FIND_ALL = "NamespaceDBO:findALL";

    public static final String PARAM_URI = "uri";

    public static final String PARAM_PREFIX = "prefix";

    @Id
    @GeneratedValue
    private Long id;

    private String uri;

    private String prefix;

    /** 
	 * JPA constructor
	 */
    protected NamespaceDBO() {
    }

    /**
	 * Constructor for new namespaces of unknown descent 
	 * @param uri
	 * @param defaultPrefix
	 */
    public NamespaceDBO(String uri, String defaultPrefix) {
        if (uri == null || defaultPrefix == null) {
            throw new IllegalStateException("may not be null: uri=" + uri + " prefix=" + prefix);
        }
        this.uri = uri;
        this.prefix = defaultPrefix;
    }

    /**
	 * Constructor for new namespaces
	 * @param uri
	 * @param defaultPrefix
	 */
    public NamespaceDBO(Identity owner, String uri, String defaultPrefix) {
        if (uri == null || defaultPrefix == null) {
            throw new IllegalStateException("may not be null: uri=" + uri + " prefix=" + prefix);
        }
        this.uri = uri;
        this.prefix = defaultPrefix;
    }

    public Long getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isRegistered() {
        return true;
    }

    public String getDefaultPrefix() {
        return prefix;
    }

    public void setDefaultPrefix(final String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return uri;
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Namespace) {
            Namespace other = (Namespace) obj;
            return uri.equals(other.getUri());
        }
        return super.equals(obj);
    }

    public int compareTo(final Namespace other) {
        return Infra.compare(getUri(), other.getUri());
    }
}
