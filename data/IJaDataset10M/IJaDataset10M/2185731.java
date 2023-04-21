package net.sf.sasl.aop.common.grammar.placeholder.resolver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import net.sf.sasl.aop.common.grammar.placeholder.interpreter.IEnvironment;
import net.sf.sasl.aop.common.grammar.placeholder.interpreter.IPlaceholderResolver;
import net.sf.sasl.aop.common.grammar.placeholder.interpreter.ResolveException;

/**
 * The placeholder resolver offers some placeholders to get access to local host
 * informations.<br>
 * <b>hostName</b>:<br>
 * Returns the local host name.<br>
 * <b>hostAddress</b><br>
 * Returns the local host address<br>
 * <b>canonicalHostName</b><br>
 * Returns the canonical local host name.
 * 
 * @author Philipp Förmer
 * @since 0.0.1
 */
public class HostPlaceholderResolver implements IPlaceholderResolver {

    /**
	 * @see net.sf.sasl.aop.common.grammar.placeholder.interpreter.IPlaceholderResolver#getResolveablePlaceholders()
	 * @since 0.0.1
	 */
    public Set<String> getResolveablePlaceholders() {
        HashSet<String> set = new HashSet<String>();
        set.add("hostName");
        set.add("hostAddress");
        set.add("canonicalHostName");
        return set;
    }

    /**
	 * @see net.sf.sasl.aop.common.grammar.placeholder.interpreter.IPlaceholderResolver#resolve(java.lang.String,
	 *      java.lang.Object[],
	 *      net.sf.sasl.aop.common.grammar.placeholder.interpreter.IEnvironment)
	 * @since 0.0.1
	 */
    public Object resolve(String placeholderName, Object[] placeholderArguments, IEnvironment environment) throws ResolveException {
        ResolveExceptionHelper.validateArgumentCount(placeholderName, placeholderArguments, 0, 0);
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException exception) {
            throw new ResolveException("Host not found.", exception);
        }
        if ("hostName".equals(placeholderName)) {
            return inetAddress.getHostName();
        } else if ("hostAddress".equals(placeholderName)) {
            return inetAddress.getHostAddress();
        }
        return inetAddress.getCanonicalHostName();
    }
}
