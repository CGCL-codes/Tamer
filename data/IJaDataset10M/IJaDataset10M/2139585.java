package au.edu.uq.itee.maenad.restlet;

import java.util.Map;
import org.restlet.resource.Variant;
import au.edu.uq.itee.maenad.restlet.auth.User;
import au.edu.uq.itee.maenad.restlet.errorhandling.InitializationException;
import au.edu.uq.itee.maenad.restlet.errorhandling.NoDataFoundException;

public class AccessDeniedResource<U extends User> extends AbstractFreemarkerResource<U> {

    public AccessDeniedResource() throws InitializationException {
        super();
        setContentTemplateName("accessDenied.html.ftl");
    }

    @Override
    protected void fillDatamodel(Map<String, Object> datamodel) throws NoDataFoundException {
    }

    @Override
    protected boolean getAllowed(U user, Variant variant) {
        return true;
    }
}
