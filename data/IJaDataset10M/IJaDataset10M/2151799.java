package fr.cnes.sitools.security.authorization;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.ext.wadl.ApplicationInfo;
import org.restlet.ext.wadl.DocumentationInfo;
import org.restlet.routing.Router;
import fr.cnes.sitools.common.application.ContextAttributes;
import fr.cnes.sitools.common.application.SitoolsApplication;
import fr.cnes.sitools.common.model.Category;
import fr.cnes.sitools.notification.business.NotifierFilter;

/**
 * Application for managing projects Dependencies : DataSets
 * 
 * TODO Constructor with all generic security configuration (Authenticator infos) Configure security application by
 * spring or from server main
 * 
 * @author AKKA Technologies
 * 
 */
public final class AuthorizationApplication extends SitoolsApplication {

    /** id of the default global authorization */
    private static String defaultAuthorization = "default";

    /** Store */
    private AuthorizationStore store = null;

    /**
   * Constructor
   * 
   * @param context
   *          Restlet Host Context
   */
    public AuthorizationApplication(Context context) {
        super(context);
        this.store = (AuthorizationStore) context.getAttributes().get(ContextAttributes.APP_STORE);
    }

    @Override
    public void sitoolsDescribe() {
        setCategory(Category.SYSTEM);
        setName("AuthorizationApplication");
        setDescription("Authorizations for resource (application) access management.\n" + "Be carefull with this application, administrator must have all authorizations\n" + "Do not let public user have PUT/POST/DELETE authorizations");
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attachDefault(AuthorizationCollectionResource.class);
        router.attach("/{resId}", AuthorizationResource.class);
        router.attach("/{resId}/authorizer", AuthorizerResource.class);
        NotifierFilter notifier = new NotifierFilter(getContext());
        notifier.setNext(router);
        return notifier;
    }

    /**
   * Gets the store value
   * @return the store
   */
    public AuthorizationStore getStore() {
        return store;
    }

    /**
   * Gets the defaultAuthorization value
   * 
   * @return the defaultAuthorization
   */
    public String getDefaultAuthorization() {
        return defaultAuthorization;
    }

    @Override
    public ApplicationInfo getApplicationInfo(Request request, Response response) {
        ApplicationInfo result = super.getApplicationInfo(request, response);
        DocumentationInfo docInfo = new DocumentationInfo("Authorization application in Sitools2.");
        docInfo.setTitle("API documentation.");
        result.getDocumentations().add(docInfo);
        return result;
    }
}
