package org.openoffice.da.comp.w2lcommon.helper;

import com.sun.star.beans.PropertyValue;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

/** This class defines convenience methods to access the OOo registry
 *  using a given base path 
 */
public class RegistryHelper {

    private XComponentContext xContext;

    /** Construct a new RegistryHelper using a given component context
	 * 
	 * @param xContext the context to use to create new services
	 */
    public RegistryHelper(XComponentContext xContext) {
        this.xContext = xContext;
    }

    /** Get a registry view relative to the given path
     * 
     * @param sPath the base path within the registry
     * @param bUpdate true if we need update access
     * @return the registry view
     * @throws com.sun.star.uno.Exception
     */
    public Object getRegistryView(String sPath, boolean bUpdate) throws com.sun.star.uno.Exception {
        Object provider = xContext.getServiceManager().createInstanceWithContext("com.sun.star.configuration.ConfigurationProvider", xContext);
        XMultiServiceFactory xProvider = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, provider);
        PropertyValue[] args = new PropertyValue[1];
        args[0] = new PropertyValue();
        args[0].Name = "nodepath";
        args[0].Value = sPath;
        String sServiceName = bUpdate ? "com.sun.star.configuration.ConfigurationUpdateAccess" : "com.sun.star.configuration.ConfigurationAccess";
        Object view = xProvider.createInstanceWithArguments(sServiceName, args);
        return view;
    }

    /** Dispose a previously obtained registry view
     * 
     * @param view the view to dispose
     */
    public void disposeRegistryView(Object view) {
        XComponent xComponent = (XComponent) UnoRuntime.queryInterface(XComponent.class, view);
        xComponent.dispose();
    }
}
