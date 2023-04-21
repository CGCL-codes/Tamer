package magoffin.matt.ieat.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magoffin.matt.ieat.domain.AppContext;
import magoffin.matt.ieat.domain.UiParameter;
import magoffin.matt.util.StringUtil;

/**
 * Class to support the AppContext configuration.
 * 
 * <p>This class is designed to be stored in the application's ServletContext, 
 * for holding the AppContext data which does not change (at least much) 
 * over the life of the running application.</p>
 * 
 * @author Matt Magoffin (spamsqr@msqr.us)
 * @version $Revision: 26 $ $Date: 2009-05-03 19:35:54 -0400 (Sun, 03 May 2009) $
 */
public class AppContextSupport implements Serializable {

    private static final long serialVersionUID = 4050197523060766258L;

    private AppContext appContext;

    private Map<String, String> attributes;

    /**
	 * Construct a new AppContextSupport object and initialize with an 
	 * AppContext object.
	 * 
	 * @param appContext the AppContext to initialize with
	 */
    public AppContextSupport(AppContext appContext) {
        setAppContext(appContext);
    }

    /**
	 * Initialize this instance.
	 */
    @SuppressWarnings("unchecked")
    private void init() {
        if (this.appContext == null) {
            throw new IllegalArgumentException("appContext can not be null");
        }
        attributes = new HashMap<String, String>(this.appContext.getMeta().size());
        for (UiParameter param : (List<UiParameter>) this.appContext.getMeta()) {
            attributes.put(param.getKey(), param.getValue());
        }
    }

    /**
	 * Return <em>true</em> if the specifiec parameter is defined as true.
	 * 
	 * <p>The values of <code>1</code>, <code>t</code>, <code>true</code>, 
	 * <code>y</code>, and <code>yes</code> are considered <em>true</em>.</p>
	 * 
	 * @param paramName the AppContext meta parameter to test for truth
	 * @return boolean
	 */
    public boolean isParameterTrue(String paramName) {
        String s = attributes.get(paramName);
        return StringUtil.parseBoolean(s);
    }

    /**
	 * Returns a debug-friendly string representing the meta parameters in this 
	 * instance's AppContext object.
	 */
    @Override
    public String toString() {
        return "AppContextSupport" + attributes;
    }

    /**
	 * Get the AppContext.
	 * 
	 * @return the AppContext
	 */
    public AppContext getAppContext() {
        return appContext;
    }

    /**
	 * Set the AppContext.
	 * 
	 * @param appContext the AppContext to set
	 */
    public void setAppContext(AppContext appContext) {
        this.appContext = appContext;
        init();
    }
}
