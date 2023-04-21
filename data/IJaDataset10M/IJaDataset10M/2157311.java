package net.sf.sasl.aop.common.grammar.placeholder.resolver;

import java.util.HashSet;
import java.util.Set;
import net.sf.sasl.aop.common.grammar.placeholder.interpreter.IEnvironment;
import net.sf.sasl.aop.common.grammar.placeholder.interpreter.IPlaceholderResolver;
import net.sf.sasl.aop.common.grammar.placeholder.interpreter.ResolveException;
import org.apache.commons.lang.StringUtils;

/**
 * Resolver which offers some string support placeholders.<br>
 * <b>newline</b><br>
 * Returns a newline sign.<br>
 * <b>tab</b><br>
 * Returns a tab sign.<br>
 * <b>repeat{'repeatString', howOften}<b>:<br>
 * Repeats the given repeatString howOften times.<br>
 * 
 * @author Philipp Förmer
 * @since 0.0.1
 */
public class StringPlaceholderResolver implements IPlaceholderResolver {

    /**
	 * @see net.sf.sasl.aop.common.grammar.placeholder.interpreter.IPlaceholderResolver#getResolveablePlaceholders()
	 */
    public Set<String> getResolveablePlaceholders() {
        HashSet<String> set = new HashSet<String>();
        set.add("repeat");
        set.add("newline");
        set.add("tab");
        return set;
    }

    /**
	 * @see net.sf.sasl.aop.common.grammar.placeholder.interpreter.IPlaceholderResolver#resolve(java.lang.String,
	 *      java.lang.Object[],
	 *      net.sf.sasl.aop.common.grammar.placeholder.interpreter.IEnvironment)
	 */
    public Object resolve(String placeholderName, Object[] placeholderArguments, IEnvironment environment) throws ResolveException {
        if ("tab".equals(placeholderName)) {
            return "\t";
        }
        if ("newline".equals(placeholderName)) {
            return "\n";
        }
        ResolveExceptionHelper.validateArgumentCount(placeholderName, placeholderArguments, 2, 2);
        ResolveExceptionHelper.validateArgumentTypes(placeholderName, placeholderArguments, String.class, Number.class);
        return StringUtils.repeat(placeholderArguments[0].toString(), ((Number) placeholderArguments[1]).intValue());
    }
}
