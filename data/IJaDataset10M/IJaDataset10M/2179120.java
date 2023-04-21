package net.sf.unruly.impl.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import java.lang.reflect.Method;
import java.util.Set;
import java.io.Serializable;

/**
 * @author Jeff Drost
 * This interface implements both the visited and the visitor aspect of the
 * visitor pattern.  Any instance that implements this interface can
 * provide services by implementing visitXXX() methods, and accept them
 * (using the accept() method).  In most cases an instance will not want to
 * receieve it's own services.
 */
public interface UnrulyInterceptor extends MethodInterceptor, Serializable {

    /**
	 * Return true if the interceptor actually intercepts the given method (does
	 * more than just invoking the super implementation).
	 */
    boolean intercepts(Method method);

    /**
	 * The accept method should be implemented to allow an instance to receieve
	 * a visitor's services.  If the instance "needs" (is a consumer of) any particular
	 * service, it will pass itself into the approriate visitXXX() method.
	 */
    void accept(UnrulyInterceptor visitor);

    /**
	 * if an instance wants to add interfaces to the enhanced class, then it may
	 * add classes to this set.
	 */
    void declareInterfaces(Set<Class> declaredInterfaces);

    void visit(UnrulyInterceptor interceptor);

    void setEnhancedInstance(Object instance);
}
