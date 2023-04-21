package restdom.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import restdom.validation.ValidationErrors;

public class RestController extends MultiActionController {

    public static final String METHOD_PUT = "PUT";

    public static final String METHOD_DELETE = "DELETE";

    public RestController() {
        setSupportedMethods(new String[] { METHOD_HEAD, METHOD_GET, METHOD_POST, METHOD_PUT, METHOD_DELETE });
    }

    @Override
    protected Object newCommandObject(Class clazz) throws Exception {
        return super.newCommandObject(clazz);
    }

    @Override
    protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception {
        return super.createBinder(request, command);
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
    }

    public void create(HttpServletRequest request, HttpServletResponse response) {
    }

    public void read(HttpServletRequest request, HttpServletResponse response) {
    }

    public long readLastModified(HttpServletRequest request) {
        return -1L;
    }

    public void handleException(HttpServletRequest request, HttpServletResponse response, ValidationErrors ex) {
    }
}
