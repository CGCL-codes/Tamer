package org.tripcom.api.interfaces;

import org.openrdf.model.Statement;
import java.net.URI;
import java.util.Set;
import javax.security.auth.callback.Callback;
import org.tripcom.api.auxiliary.Pair;
import org.tripcom.integration.exception.TSAPIException;
import org.tripcom.integration.entry.Template;

/**
 * original API interface
 * 
 * superseeded by new functionality, but still used by 
 * some classes
 */
public interface ExtendedAPI extends CoreAPI {

    public void out(Set<Statement> t, URI space) throws TSAPIException;

    public Set<Set<Statement>> rdmultiple(Template query, URI space, int timeout) throws TSAPIException;

    public URI subscribe(Template query, Callback callback, URI space) throws TSAPIException;

    public void unsubscribe(URI subscription) throws TSAPIException;
}
