package org.openliberty.arisidbeans;

import org.openliberty.arisid.ArisIdServiceFactory;
import org.openliberty.arisid.policy.PolicyHandler;
import org.openliberty.arisid.util.Base64Handler;

/**
*
*/
public class ArisIdConstants {

    public static final String ATTRIBUTE_SERVICE_PROVIDER = ArisIdServiceFactory.IGF_ATTRIBUTE_SERVICE_PROVIDER;

    public static final String DEFAULT_ATTRIBUTE_SERVICE_PROVIDER = ArisIdServiceFactory.IGF_ATTRIBUTE_SERVICE_PROV_DEFAULT;

    public static final String SECURITY_PRINCIPAL = "igf.arisidbeans.security.principal";

    public static final String SECURITY_CREDENTIALS = "igf.arisidbeans.security.credentials";

    /**
    * This default DOMImplementationSource is used if
    * DomImplementationSourceList is not set in Java system property
    * org.w3c.dom.DOMImplementationSourceList
    */
    public static final String DEFAULT_DOMIMPLEMENTATIONSOURCE = "org.apache.xerces.dom.DOMImplementationSourceImpl";

    public static final String IGF_CARML_LOC = "igf.arisidbeans.carmlloc";

    public static final String WS_POLICY_CLASS = PolicyHandler.WS_POLICY_CLASS;

    public static final String DEFAULT_WS_POLICY_CLASS = PolicyHandler.DEF_WSPOLICY_CLASS;

    public static final String BASE64_CLASS = Base64Handler.BASE64_CLASS;

    public static final String DEFAULT_BASE64_CLASS = "com.sun.org.apache.xerces.internal.impl.dv.util.Base64";

    public static final String APP_CTX_AUTHUSER = "user";

    public static final String APP_CTX_PAGESIZE = "pagesize";

    public static final String APP_CTX_LOCALE = "locale";
}
