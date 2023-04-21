package ch.unibe.id.se.a3ublogin.view.jsphelpers;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import ch.unibe.id.se.a3ublogin.business.BusinessManager;
import ch.unibe.id.se.a3ublogin.exceptions.BusinessException;
import ch.unibe.id.se.a3ublogin.exceptions.LoginException;

public class ErrorLogic {

    /** instance of the singleton */
    private static ErrorLogic instance = null;

    /** returns the singleton */
    public static synchronized ErrorLogic getInstance() {
        if (instance == null) instance = new ErrorLogic();
        return instance;
    }

    /** privat constructor */
    private ErrorLogic() {
    }

    /**
	 * does an anymous bind, then a user bind to authenticate user and then
	 * reads all accessible ldapattributes for this user and stores them in the
	 * ticket if everything was ok, then the retured userDN contains the
	 * username
	 * 
	 * @param A3ubLoginBucketBean_v01 -
	 *            wich is almost empty
	 * 
	 * @return A3ubLoginBucketBean_v01 - containig all the informations about
	 *         the user and its login state
	 * @throws BusinessException
	 * @throws LoginException
	 */
    public void prepareSession(HttpServletRequest request) {
        @SuppressWarnings("unused") String clientip = null;
        if (request.getHeader("X-Forwarded-For") == null) {
            clientip = request.getRemoteAddr();
        } else {
            clientip = request.getHeader("X-Forwarded-For");
        }
        prepareErrorSession(request);
    }

    @SuppressWarnings("unchecked")
    private void prepareErrorSession(HttpServletRequest request) {
        BusinessManager man = BusinessManager.getInstance();
        Map<String, Properties> map = man.getMapOfAllProperties();
        TreeMap<String, String> tmap = new TreeMap();
        Collection<String> iter = map.keySet();
        for (String key : iter) {
            Properties prop = map.get(key);
            String url = prop.getProperty("WelcomePage");
            String name = prop.getProperty("ApplicationName");
            if (name != null && url != null) {
                tmap.put(name, url);
            }
        }
        request.getSession().setAttribute("error_keys", tmap.keySet().iterator());
        request.getSession().setAttribute("error_values", tmap);
    }

    public static void main(String[] args) {
    }
}
