package net.sf.sasl.language.placeholder.aop.resolver;

import java.util.HashSet;
import java.util.Set;
import net.sf.sasl.language.placeholder.aop.interpreter.IEnvironment;
import net.sf.sasl.language.placeholder.aop.interpreter.IPlaceholderResolver;
import net.sf.sasl.language.placeholder.aop.interpreter.ResolveException;

/**
 * The Placeholder resolver offers placeholder to get access to thread
 * statistics.<br>
 * 
 * <b>curThreadId</b><br>
 * Returns the thread id the placeholder script interpreter is running at.<br>
 * <b>curThreadName</b><br>
 * Returns the thread name the placeholder script interpreter is running at.<br>
 * <b>totalThreadCount</b><br>
 * Returns the estimated number of threads currently running in the jvm process<br>
 * 
 * @author Philipp Förmer
 * @since 0.0.1 (sasl-common-aspect-library)
 */
public class ThreadPlaceholderResolver implements IPlaceholderResolver {

    /**
	 * @see net.sf.sasl.language.placeholder.aop.interpreter.IPlaceholderResolver#getResolveablePlaceholders()
	 */
    public Set<String> getResolveablePlaceholders() {
        HashSet<String> set = new HashSet<String>();
        set.add("curThreadId");
        set.add("curThreadName");
        set.add("totalThreadCount");
        return set;
    }

    /**
	 * @see net.sf.sasl.language.placeholder.aop.interpreter.IPlaceholderResolver#resolve(java.lang.String,
	 *      java.lang.Object[],
	 *      net.sf.sasl.language.placeholder.aop.interpreter.IEnvironment)
	 */
    public Object resolve(String placeholderName, Object[] placeholderArguments, IEnvironment environment) throws ResolveException {
        ResolveExceptionHelper.validateArgumentCount(placeholderName, placeholderArguments, 0, 0);
        if ("curThreadId".equals(placeholderName)) {
            return Thread.currentThread().getId();
        } else if ("curThreadName".equals(placeholderName)) {
            return Thread.currentThread().getName();
        }
        return caculateTotalThreadCount();
    }

    /**
	 * Returns the estimated number of threads currently running in the jvm
	 * process.
	 * 
	 * @return positive integer
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    private static long caculateTotalThreadCount() {
        ThreadGroup root = Thread.currentThread().getThreadGroup();
        while (root.getParent() != null) {
            root = root.getParent();
        }
        return calcThreadGroup(root);
    }

    /**
	 * Travels all ThreadGroup nodes in the ThreadGroup tree to get the
	 * estimated number of currently running threads.
	 * 
	 * @param parentGroup
	 *            non null
	 * @return positive integer
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    private static long calcThreadGroup(ThreadGroup parentGroup) {
        long childThreadCount = 0;
        ThreadGroup[] childs = new ThreadGroup[parentGroup.activeGroupCount() * 2];
        int childCount = parentGroup.enumerate(childs);
        for (int i = 0; i < childCount; i++) {
            childThreadCount += calcThreadGroup(childs[i]);
        }
        return parentGroup.activeCount() + childThreadCount;
    }
}
