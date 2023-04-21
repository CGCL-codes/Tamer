package net.sf.nic;

import net.sf.nic.util.IOUtils;
import java.net.URI;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Is the abstract base to all source implementations.
 *
 * @author Juergen_Kellerer, 2009-12-07
 * @version 1.0
 */
public abstract class AbstractSource extends AbstractIncludableSource implements Source {

    protected Set<Scope> scopes = EnumSet.of(Scope.Application);

    protected String sourceURI;

    protected String pathPrefix;

    /**
	 * {@inheritDoc}
	 */
    public Set<Scope> getScopes() {
        return scopes;
    }

    /**
	 * Sets the scopes to the given list.
	 *
	 * @param scopes the scopes of this source.
	 */
    public void setScopes(Scope... scopes) {
        if (scopes == null || scopes.length == 0) this.scopes = Collections.emptySet(); else if (scopes.length == 1) this.scopes = EnumSet.of(scopes[0]); else this.scopes = EnumSet.of(scopes[0], scopes);
    }

    /**
	 * {@inheritDoc}
	 */
    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = IOUtils.normalizePath(pathPrefix, false, true);
    }

    /**
	 * {@inheritDoc}
	 */
    public URI getURI() {
        return URI.create(sourceURI);
    }

    public String getSourceURI() {
        return sourceURI;
    }

    public void setSourceURI(String sourceURI) {
        this.sourceURI = sourceURI;
    }

    /**
	 * Returns the relative path used inside the target.
	 *
	 * @return the relative path used inside the target.
	 */
    protected String getRelativeTargetPath() {
        String prefix = pathPrefix == null ? "" : pathPrefix;
        String path;
        if (properties != null) path = KnownProperties.FinalName.get(this, sourceURI); else path = sourceURI;
        path = IOUtils.extractPath(path, true);
        return prefix.concat(path);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractSource that = (AbstractSource) o;
        if (locales != null ? !locales.equals(that.locales) : that.locales != null) return false;
        if (pathPrefix != null ? !pathPrefix.equals(that.pathPrefix) : that.pathPrefix != null) return false;
        if (platforms != null ? !platforms.equals(that.platforms) : that.platforms != null) return false;
        if (!scopes.equals(that.scopes)) return false;
        if (!sourceURI.equals(that.sourceURI)) return false;
        return true;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int hashCode() {
        int result = scopes.hashCode();
        result = 31 * result + sourceURI.hashCode();
        result = 31 * result + (pathPrefix != null ? pathPrefix.hashCode() : 0);
        result = 31 * result + (platforms != null ? platforms.hashCode() : 0);
        result = 31 * result + (locales != null ? locales.hashCode() : 0);
        return result;
    }
}
