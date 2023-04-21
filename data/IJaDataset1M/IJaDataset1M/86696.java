package org.jaffa.security;

import org.apache.log4j.Logger;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.ejb.EJBContext;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;
import java.security.AccessControlException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import org.jaffa.presentation.portlet.component.ComponentManager;

/** Security Manager is the main interface to the BusinessFunction and Component Security Architecture.
 * It provide a mechanism for setting the security context for a thread of execution, and then
 * provide a guard for securing code in that thread. The access to the
 * guarded code is derived from a role based security policy file.
 *
 */
public class SecurityManager {

    /** Set up Logging for Log4J */
    private static Logger log = Logger.getLogger(SecurityManager.class);

    /** Bind a security context to a thread and contine executing the thread by running the
     * supplied method against the specified object with the supplied paramters.
     * In this case the security context is derived from a HttpServletRequest.
     * @param ctx Web Server Request Context to use
     * @param obj The object contains the method to execute under the thread security context
     * @param method The name of the method to execute in the specified object
     * @param args An Object array of argument to pass to the method. If there are no parameters for the method null can be passed
     * @throws Exception Returns any Exception that the method being invoked may return
     * @return Returns any Object that the method being invoked can return
     */
    public static Object runWithContext(HttpServletRequest ctx, Object obj, String method, Object[] args) throws Exception {
        return runWithContext(new SecurityContext(ctx), obj, method, args);
    }

    /** Bind a security context to a thread and contine executing the thread by running the
     * supplied method against the specified object with the supplied paramters.
     * In this case the security context is derived from a HttpServletRequest.
     * @return Returns any Object that the method being invoked can return
     * @param sig This is an array of classes that represent the signature to the supplied method. This will be used for introspection for
     * the supplied method on the given object
     * @param ctx Web Server Request Context to use
     * @param obj The object contains the method to execute under the thread security context
     * @param method The name of the method to execute in the specified object
     * @param args An Object array of argument to pass to the method. If there are no parameters for the method null can be passed
     * @throws Exception Returns any Exception that the method being invoked may return
     */
    public static Object runWithContext(HttpServletRequest ctx, Object obj, String method, Object[] args, Class[] sig) throws Exception {
        return runWithContext(new SecurityContext(ctx), obj, method, args, sig);
    }

    /** Bind a security context to a thread and contine executing the thread by running the
     * supplied method against the specified object with the supplied paramters. The signature of
     * the method is introspected used the classes associated to the objects in the parameter array
     * If these classes are not able to specified the methods signiture, use the variation of this method
     * that allows the class[] singature to be supplied.
     * In this case the security context is derived from a HttpServletRequest.
     * @return Returns any Object that the method being invoked can return
     * the supplied method on the given object
     * @param ctx Web Server Request Context to use
     * @param obj The object contains the method to execute under the thread security context
     * @param method The name of the method to execute in the specified object
     * @param args An Object array of argument to pass to the method. If there are no parameters for the method null can be passed
     * @throws Exception Returns any Exception that the method being invoked may return
     */
    public static Object runWithContext(HttpServletRequest ctx, Object obj, Method method, Object[] args) throws Exception {
        return runWithContext(new SecurityContext(ctx), obj, method, args);
    }

    /** Bind a security context to a thread and contine executing the thread by running the
     * supplied method against the specified object with the supplied paramters.
     * In this case the security context is derived from an EJBContext.
     * @param ctx Web Server Request Context to use
     * @param obj The object contains the method to execute under the thread security context
     * @param method The name of the method to execute in the specified object
     * @param args An Object array of argument to pass to the method. If there are no parameters for the method null can be passed
     * @throws Exception Returns any Exception that the method being invoked may return
     * @return Returns any Object that the method being invoked can return
     */
    public static Object runWithContext(EJBContext ctx, Object obj, String method, Object[] args) throws Exception {
        return runWithContext(new SecurityContext(ctx), obj, method, args);
    }

    /** Bind a security context to a thread and contine executing the thread by running the
     * supplied method against the specified object with the supplied paramters.
     * In this case the security context is derived from an EJBContext.
     * @return Returns any Object that the method being invoked can return
     * @param sig This is an array of classes that represent the signature to the supplied method. This will be used for introspection for
     * the supplied method on the given object
     * @param ctx Web Server Request Context to use
     * @param obj The object contains the method to execute under the thread security context
     * @param method The name of the method to execute in the specified object
     * @param args An Object array of argument to pass to the method. If there are no parameters for the method null can be passed
     * @throws Exception Returns any Exception that the method being invoked may return
     */
    public static Object runWithContext(EJBContext ctx, Object obj, String method, Object[] args, Class[] sig) throws Exception {
        return runWithContext(new SecurityContext(ctx), obj, method, args, sig);
    }

    /** Bind a security context to a thread and contine executing the thread by running the
     * supplied method against the specified object with the supplied paramters. The signature of
     * the method is introspected used the classes associated to the objects in the parameter array
     * If these classes are not able to specified the methods signiture, use the variation of this method
     * that allows the class[] singature to be supplied.
     * In this case the security context is derived from an EJBContext.
     * @return Returns any Object that the method being invoked can return
     * the supplied method on the given object
     * @param ctx Web Server Request Context to use
     * @param obj The object contains the method to execute under the thread security context
     * @param method The name of the method to execute in the specified object
     * @param args An Object array of argument to pass to the method. If there are no parameters for the method null can be passed
     * @throws Exception Returns any Exception that the method being invoked may return
     */
    public static Object runWithContext(EJBContext ctx, Object obj, Method method, Object[] args) throws Exception {
        return runWithContext(new SecurityContext(ctx), obj, method, args);
    }

    /** Run the guarded business function, only if the current thread has access
     * @param functionName Name of the business function being guarded
     * @param action An action object which will be executed, this should contain the guarded code
     * @throws AccessControlException This is thrown if the user doesn't have authorization for this function
     * @return  Returns back the object that the guarded code returned
     */
    public static Object runFunction(String functionName, PrivilegedAction action) throws AccessControlException {
        if (hasAccess(functionName)) return action.run(); else {
            log.info("Access Denied To Business Function: " + functionName);
            throw new AccessControlException("Business Function:" + functionName);
        }
    }

    /** Run the guarded business function, only if the current thread has access.
     * This guarded function may throw a PrivilegedActionException which will contain
     * the real exception
     * @return Returns back the object that the guarded code returned
     * @param functionName Name of the business function being guarded
     * @param action An action object which will be executed, this should contain the guarded code
     * @throws PrivilegedActionException This is the wrapped exception the the guarded code threw
     * @throws AccessControlException This is thrown if the user doesn't have authorization for this function
     */
    public static Object runFunction(String functionName, PrivilegedExceptionAction action) throws PrivilegedActionException, AccessControlException {
        if (hasAccess(functionName)) {
            try {
                return action.run();
            } catch (Exception e) {
                throw new PrivilegedActionException(e);
            }
        } else {
            log.info("Access Denied To Business Function: " + functionName);
            throw new AccessControlException("Business Function:" + functionName);
        }
    }

    /** See if the current thread has access to the named component.
     * This can be used by a Component Manager to preempt a security violation
     * @param componentName Name of component to check
     * @return true, if the current thread has access to this component, otherwise false is returned
     */
    public static boolean checkComponentAccess(String componentName) {
        return hasComponentAccess(componentName, null);
    }

    /** See if the current thread has access to the named business function.
     * @param functionName Name of business function to check
     * @return true, if the current thread has access to this business function, otherwise false is returned
     */
    public static boolean checkFunctionAccess(String functionName) {
        return hasAccess(functionName, null);
    }

    /** Get the Security Prinipal Object for the Current User. If this is called
     * 'outsite' or the Jaffa framework it will return null. Typically jaffa security
     * is backed by either Web Container or EJB Container security, and this will
     * return Principle as created by the Web/EJB container that is associated to
     * the thread of execution calling this method
     * <p>
     * In a typical web environment <code>SecurityManager.getPrincipal().getName()</code>
     * will return you the username used to log on.
     * <p>
     * @return The security principal associated to the current thread
     */
    public static Principal getPrincipal() {
        SecurityContext ctx = getCurrentContext();
        if (ctx == null) return null; else return ctx.getPrincipal();
    }

    /** Get the roles for this user. Uses the cached list of roles to check which
     * ones the container grants this user access to. The user is obtained from the
     * current SecurityContext bound to the thread
     */
    public static List getUserRoles() {
        ArrayList al = new ArrayList();
        Set rl = PolicyManager.getRoleSet();
        if (rl != null) {
            SecurityContext ctx = SecurityManager.getCurrentContext();
            if (ctx != null) {
                for (Iterator it = rl.iterator(); it.hasNext(); ) {
                    String role = (String) it.next();
                    if (ctx.inRole(role)) {
                        if (al == null) al = new ArrayList();
                        al.add(role);
                    }
                }
            }
        }
        return al;
    }

    /** Add this context to the current thread
     */
    static void bindToThread(SecurityContext ctx) {
        ((SecurityContextStack) stack.get()).push(ctx);
    }

    /** Remove the current context from the current thread
     */
    static void unbindFromThread() {
        ((SecurityContextStack) stack.get()).pop();
    }

    /** Return the current security context for this thread
     */
    static SecurityContext getCurrentContext() {
        return ((SecurityContextStack) stack.get()).getContext();
    }

    /** See if the give context has access to a function
     * Use by the tag libraries, doesn't require the context to be bound to the thread
     */
    static boolean checkFunctionAccess(String functionName, SecurityContext ctx) throws SecurityException {
        if (ctx == null) throw new SecurityException("No Context Supplied For Security Check");
        return hasAccess(functionName, ctx);
    }

    /** See if the give context has access to a component
     * Use by the tag libraries, doesn't require the context to be bound to the thread
     */
    static boolean checkComponentAccess(String componentName, SecurityContext ctx) throws SecurityException {
        if (ctx == null) throw new SecurityException("No Context Supplied For Security Check");
        return hasComponentAccess(componentName, ctx);
    }

    /** Bind a security context to a thread and contine executing the thread by running the
     * supplied method against the specified object with the supplied paramters.
     */
    private static Object runWithContext(SecurityContext ctx, Object obj, String method, Object[] args) throws Exception {
        Class[] sig = null;
        if (args == null || args.length == 0) {
            sig = new Class[] {};
            args = new Object[] {};
        } else {
            if (log.isDebugEnabled()) log.debug("Building Method Signature");
            sig = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                sig[i] = args[i].getClass();
                if (log.isDebugEnabled()) log.debug("Parm " + i + " is class " + sig[i].getName());
            }
        }
        Method m = null;
        try {
            m = obj.getClass().getMethod(method, sig);
        } catch (SecurityException e) {
            log.error("No Access To Introspect For Method " + method + " on Class " + obj.getClass(), e);
            throw e;
        } catch (NoSuchMethodException e) {
            log.error("No Such Method " + method + " on Class " + obj.getClass(), e);
            throw e;
        }
        return runWithContext(ctx, obj, m, args);
    }

    /** Bind a security context to a thread and contine executing the thread by running the
     * supplied method against the specified object with the supplied paramters.
     */
    private static Object runWithContext(SecurityContext ctx, Object obj, String method, Object[] args, Class[] sig) throws Exception {
        if (sig == null) sig = new Class[] {};
        if (args == null) args = new Object[] {};
        if (sig.length != args.length) throw new RuntimeException("Parameter List and Signature List are different sizes!");
        Method m = null;
        try {
            m = obj.getClass().getMethod(method, sig);
        } catch (SecurityException e) {
            log.error("No Access To Introspect For Method " + method + " on Class " + obj.getClass(), e);
            throw e;
        } catch (NoSuchMethodException e) {
            log.error("No Such Method " + method + " on Class " + obj.getClass(), e);
            throw e;
        }
        return runWithContext(ctx, obj, m, args);
    }

    /** Bind a security context to a thread and contine executing the thread by running the
     * supplied method against the specified object with the supplied paramters.
     */
    private static Object runWithContext(SecurityContext ctx, Object obj, Method method, Object[] args) throws Exception {
        bindToThread(ctx);
        try {
            return method.invoke(obj, args);
        } finally {
            unbindFromThread();
        }
    }

    /** Create a ThreadLocal variable for storing the SecurityContextStack
     */
    private static class ThreadLocalContextStack extends ThreadLocal {

        /** Set the thread local variable to hold a new security context stack
         * @return this doesn't return anything
         */
        public Object initialValue() {
            return new SecurityContextStack();
        }
    }

    /** Make an instance of the SecurityContextStack For each new thread as they
     * go through the security manager
     */
    private static ThreadLocalContextStack stack = new ThreadLocalContextStack();

    /** Check the Policy and the Current SecurityContext to see if access to this function
     * should be granted.
     */
    private static boolean hasAccess(String functionName) {
        return hasAccess(functionName, null);
    }

    /** Check the Policy and the Current SecurityContext to see if access to this function
     * should be granted.
     *
     * If no security context is supplied (i.e it is null) it uses the context of the
     * current thread
     */
    private static boolean hasAccess(String functionName, SecurityContext ctx) {
        String[] roles = PolicyManager.getRolesForFunction(functionName);
        if (roles == null) {
            if (log.isDebugEnabled()) log.debug("No Roles have access to function " + functionName);
            return false;
        }
        if (ctx == null) ctx = getCurrentContext();
        if (ctx == null || ctx.getPrincipal() == null) {
            if (log.isDebugEnabled()) log.debug("hasAccess(): No Security Context, therefore No Access To Function : " + functionName);
            return false;
        }
        if (log.isDebugEnabled()) log.debug("Checking Access To Business Function : " + functionName + " for User " + ctx.getPrincipal().getName());
        for (int i = 0; i < roles.length; i++) {
            if (ctx.inRole(roles[i])) return true; else if (log.isDebugEnabled()) log.debug("User " + ctx.getPrincipal().getName() + " Has No Access To Role : " + roles[i]);
        }
        return false;
    }

    /** Check the Policy and the Current SecurityContext to see if access to this component
     * should be granted.
     *
     * If no security context is supplied (i.e it is null) it uses the context of the
     * current thread.
     */
    private static boolean hasComponentAccess(String componentName, SecurityContext ctx) {
        if (ComponentManager.find(componentName) == null) {
            if (log.isDebugEnabled()) log.debug("Access to " + componentName + " is false, since it is not defined");
            return false;
        }
        String[] roles = PolicyManager.getRolesForComponent(componentName);
        if (roles == null) return true;
        if (roles.length == 0) {
            if (log.isDebugEnabled()) log.debug("No Roles have access to component " + componentName);
            return false;
        }
        if (ctx == null) ctx = getCurrentContext();
        if (ctx == null || ctx.getPrincipal() == null) {
            if (log.isDebugEnabled()) log.debug("hasAccess(): No Security Context, therefore No Access To Component : " + componentName);
            return false;
        }
        if (log.isDebugEnabled()) log.debug("Checking Access To Component : " + componentName + " for User " + ctx.getPrincipal().getName());
        for (int i = 0; i < roles.length; i++) {
            if (ctx.inRole(roles[i])) return true; else if (log.isDebugEnabled()) log.debug("User " + ctx.getPrincipal().getName() + " Has No Access To Role : " + roles[i]);
        }
        return false;
    }
}
