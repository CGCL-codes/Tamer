package at.vartmp.jschnellen.core.addons;

/**
 * @author Lukas Tischler [tischler.lukas_AT_gmail.com]
 * 
 *         <h3>Implementation example</h3>
 * 
 *         <pre>
 * public void executeAddons() {
 * 	Throwable t = new Throwable();
 * 	StackTraceElement[] elements = t.getStackTrace();
 * 	if (elements.length &lt; 2) return; 
 * 	// elements[0] is this method
 * 	String caller =  elements[1].getMethodName();
 * 	for (Addon a : this.addons) {
 * 	try {
 * 		java.lang.reflect.Method m = a.getClass().getMethod(&quot;execute&quot;);
 * 	For an = m.getAnnotation(For.class);
 * 	if(an.forMethod().equals(caller))
 * 		a.execute();
 * 	} catch (Exception e) {
 * 		e.printStackTrace();
 * 	}
 * }
 * </pre>
 * 
 * <br>
 * <br>
 *         The invokation of the execute-Method defined in this interface should
 *         be in the following way:
 * 
 *         <pre>
 * public void theMethodToExecute() {
 * 	this.executeAddons(true);
 * 	// additional code to execute...
 * 	this.executeAddons(false);
 * }
 * </pre>
 * 
 */
public interface AddonPlugger {

    /**
	 * This method executes the addons for the class implementing this
	 * interface. How the addons are executed is implementation specific. One
	 * use case is for example, to add for different methods different addons
	 * and in this method we get the name of the method.
	 * 
	 * @param before
	 *            set this to <code>true</code> if the addons marked as "before"
	 *            should be invoked. Otherwise set this to <code>false</code>.
	 */
    void executeAddons(boolean before);

    /**
	 * Register an addon for a given method.
	 * 
	 * @param methodName
	 *            the method name
	 * @param addon
	 *            the addon to register
	 */
    void registerAddon(String methodName, Addon addon);
}
