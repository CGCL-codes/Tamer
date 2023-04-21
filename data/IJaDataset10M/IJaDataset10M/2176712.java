package com.fddtool.services.email;

import com.fddtool.si.PropertyReader;

/**
 * This class is a simple wrapper, used to retrieve properties from
 * email.properties file.
 * 
 * @author Serguei Khramtchenko
 */
public class EmailProperties extends PropertyReader {

    /**
     * Name of the property file where email settings are.
     */
    public static final String PROPERTIES_FILE = "email.properties";

    /**
     * Key for the email address of support team.
     */
    private static final String KEY_SUPPORT_ADDRESS = "fddpma.email.support.address";

    /**
     * Key for the name support team.
     */
    private static final String KEY_SUPPORT_NAME = "fddpma.email.support.name";

    /**
     * Key for the name of SMTP server to use for sending email messages.
     */
    private static final String KEY_SMTP_SERVER = "fddpma.email.smtp_server";

    /**
     * Key for the user of SMTP server to use for sending email messages.
     */
    private static final String KEY_SMTP_USER = "fddpma.email.smtp_user";

    /**
     * Key for the password of SMTP server to use for sending email messages.
     */
    private static final String KEY_SMTP_PASSWORD = "fddpma.email.smtp_password";

    /**
     * Default email address of support team.
     */
    private static final String DEFAULT_SUPPORT_ADDRESS = "support@fddpma.net";

    /**
     * Default name of support team.
     */
    private static final String DEFAULT_SUPPORT_NAME = "FDDPMA Support";

    /**
     * Default SMTP server to use for sending email messages.
     */
    private static final String DEFAULT_SMTP_SERVER = "";

    /**
     * Default password for SMTP server.
     */
    private static final String DEFAULT_SMTP_PASSWORD = "";

    /**
     * Default user name for SMTP server.
     */
    private static final String DEFAULT_SMTP_USER = "";

    /**
     * Single instance of this class, part of singleton implementation.
     */
    private static EmailProperties instance = new EmailProperties();

    /**
     * Private constructor, part of singleton implementation.
     */
    private EmailProperties() {
        super();
    }

    protected String getPropertyFileName() {
        return PROPERTIES_FILE;
    }

    /**
     * Returns the email address of support group. Most of the emails that
     * FDDPMA sends will be coming from this address.
     * 
     * @return String containg email address of support group.
     */
    public static String getSupportAddress() {
        return instance.properties.getProperty(KEY_SUPPORT_ADDRESS, DEFAULT_SUPPORT_ADDRESS);
    }

    /**
     * Returns the name of support group. This name appears in FROM filed of
     * email messages sent by FDDPMA.
     * 
     * @return String containg name of support group.
     */
    public static String getSupportName() {
        return instance.properties.getProperty(KEY_SUPPORT_NAME, DEFAULT_SUPPORT_NAME);
    }

    /**
     * Returns the name of SMTP server that will be used to send email messages .
     * 
     * @return String containing name of SMTP server.
     */
    public static String getSmtpServer() {
        return instance.properties.getProperty(KEY_SMTP_SERVER, DEFAULT_SMTP_SERVER);
    }

    /**
     * Returns the password to be used when connecting to SMTP server.
     * 
     * @return String the password, or empty string if the password is not
     *         defined.
     */
    public static String getSmtpPassword() {
        return instance.properties.getProperty(KEY_SMTP_PASSWORD, DEFAULT_SMTP_PASSWORD);
    }

    /**
     * Returns the name of SMTP server that will be used to send email messages. * *
     * 
     * @return String containing username for SMTP server, or empty string if user name is not defined.
     */
    public static String getSmtpUsername() {
        return instance.properties.getProperty(KEY_SMTP_USER, DEFAULT_SMTP_USER);
    }
}
