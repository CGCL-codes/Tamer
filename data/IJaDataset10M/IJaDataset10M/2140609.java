package org.openuss.system;

/**
 * Global System Property Ids
 * 
 * @author Ingo Dueppe
 */
public interface SystemProperties {

    public static final String REPOSITORY_PATH = "repository.path";

    public static final Long REPOSITORY_PATH_ID = 1L;

    public static final String MAIL_FROM_ADDRESS = "mail.from.address";

    public static final String MAIL_FROM_NAME = "mail.from.name";

    public static final String MAIL_HOST_NAME = "mail.host.name";

    public static final String MAIL_HOST_PORT = "mail.host.port";

    public static final String MAIL_HOST_USR = "mail.host.user";

    public static final String MAIL_HOST_PWD = "mail.host.password";

    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";

    public static final String MAIL_SMTP_SOCKETFACTORY_PORT = "mail.smtp.socketFactory.port";

    public static final String MAIL_SMTP_SOCKETFACTORY_CLASS = "mail.smtp.socketFactory.class";

    public static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";

    public static final String OPENUSS_SERVER_URL = "openuss.server.url";

    public static final String COPYRIGHT = "openuss.copyright";

    public static final String SHIBBOLETH_START_AUTH_URL = "shibboleth.start.auth.url";

    public static final String DOCUMENTATION_URL = "documentation.url";

    public static final String SUPPORT_URL = "support.url";

    public static final String BUGTRACKING_URL = "bugtracking.url";

    public static final String IMPRESSUM_TEXT = "impressum.text";

    public static final String PROVIDER_URL = "provider.url";

    public static final String GETTING_STARTED = "getting.started";

    /**
	 * JVM Application Parameter to define the server id.<br/> Add
	 * <code>-Dopenuss.instance.id</code> to the server jvm startup to define
	 * the server id.
	 **/
    public static final String OPENUSS_INSTANCE_ID = "openuss.instance.id";
}
