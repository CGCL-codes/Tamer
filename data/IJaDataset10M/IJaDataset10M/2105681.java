package net.godcode.olivenotes.services.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.tapestry.services.ApplicationStateManager;
import org.apache.tapestry.services.ComponentClassResolver;
import org.apache.tapestry.services.Request;
import org.apache.tapestry.services.Response;
import org.slf4j.Logger;

/**
 * AccessControllerImpl
 * 
 * @author Chris Lewis Jan 6, 2008 <chris@thegodcode.net>
 * @version $Id: AccessControllerImpl.java 13 2008-01-07 21:03:06Z burningodzilla $
 */
public class AccessControllerImpl implements AccessController {

    private ApplicationStateManager asm;

    private Map<String, String> configuration;

    private ComponentClassResolver componentClassResolver;

    private Logger log;

    private Map<String, String> roleRestrictions;

    /**
	 * 
	 * @param configuration
	 * @param asm The ApplicationStateManager. We need this to access ASOs from different clients,
	 * as this controller is a shared singleton. 
	 * @param componentClassResolver
	 */
    public AccessControllerImpl(Map<String, String> configuration, ApplicationStateManager asm, ComponentClassResolver componentClassResolver, Logger log) {
        this.configuration = configuration;
        this.asm = asm;
        this.componentClassResolver = componentClassResolver;
        this.log = log;
        roleRestrictions = new HashMap<String, String>();
        roleRestrictions.put("blog/createpost", "");
    }

    /**
	 * 
	 */
    public boolean dispatch(Request request, Response response) throws IOException {
        Todo test = new Todo(componentClassResolver, log);
        test.dispatch(request);
        String requestedComponent = this.resolveLogicalPage(request);
        log.error(requestedComponent);
        boolean canAccess = true;
        if (!asm.exists(String.class)) {
            asm.set(String.class, String.valueOf(System.currentTimeMillis()));
        }
        if (!canAccess) {
            throw new RuntimeException("Access violation!");
        }
        return false;
    }

    private String resolveLogicalPage(Request request) {
        String path = request.getPath();
        if (path.equals("")) return "/";
        int dot = path.indexOf('.');
        int colon = path.indexOf(':');
        if (dot > 0) {
            return path.substring(1, dot);
        } else if (colon > 0) {
            return path.substring(1, colon);
        }
        StringBuilder sbPage = new StringBuilder("");
        int index = 1;
        while (true) {
            int nextSlash = path.indexOf('/', index);
            boolean atEnd = nextSlash < 0;
            if (atEnd) {
                sbPage.append(path.substring(index - 1));
            } else {
                sbPage.append(path.substring(index, nextSlash));
            }
            if (componentClassResolver.isPageName(sbPage.toString())) {
                if (atEnd) {
                    return sbPage.toString();
                } else {
                    throw new RuntimeException("TODO: extract context");
                }
            }
            if (atEnd) return sbPage.toString();
            index = nextSlash + 1;
        }
    }
}
