package org.nightlabs.jfire.web.demoshop.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 * @author khaled
 */
public class WebShopConfig {

    private static final String PROPERTY_ORGANISATION_ID = "organisationId";

    private static final String PROPERTY_USER_ID = "userId";

    private static final String PROPERTY_WORKSTATION_ID = "workstationId";

    private static final String PROPERTY_PASSWORD = "password";

    private static final String PROPERTY_CURRENCY_ID = "currencyId";

    private static final String PROPERTY_PROVIDER_URL = "providerUrl";

    private static final String PROPERTY_INITIAL_CONTEXT_FACTORY = "initialContextFactory";

    /**
	 * The serial version of this class.
	 */
    private static final long serialVersionUID = 1L;

    private String organisationId = null;

    private String userId = null;

    private String workstationId = null;

    private String password = null;

    private String currencyId = null;

    private String providerUrl = null;

    private String initialContextFactory = null;

    /**
	 * Create a new WebShopConfig by loading from the properties file.
	 */
    public WebShopConfig() {
        Properties config = new Properties();
        try {
            InputStream in = getClass().getResourceAsStream("config.properties");
            config.load(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Loading configuration failed", e);
        }
        organisationId = config.getProperty(PROPERTY_ORGANISATION_ID);
        if (organisationId == null) throw new IllegalStateException("Property " + PROPERTY_ORGANISATION_ID + " not set");
        userId = config.getProperty(PROPERTY_USER_ID);
        if (userId == null) throw new IllegalStateException("Property " + PROPERTY_USER_ID + " not set");
        workstationId = config.getProperty(PROPERTY_WORKSTATION_ID);
        password = config.getProperty(PROPERTY_PASSWORD);
        if (password == null) throw new IllegalStateException("Property " + PROPERTY_PASSWORD + " not set");
        currencyId = config.getProperty(PROPERTY_CURRENCY_ID);
        if (currencyId == null) throw new IllegalStateException("Property " + PROPERTY_CURRENCY_ID + " not set");
        providerUrl = config.getProperty(PROPERTY_PROVIDER_URL);
        if (providerUrl == null) throw new IllegalStateException("Property " + PROPERTY_PROVIDER_URL + " not set");
        initialContextFactory = config.getProperty(PROPERTY_INITIAL_CONTEXT_FACTORY);
        if (initialContextFactory == null) throw new IllegalStateException("Property " + PROPERTY_INITIAL_CONTEXT_FACTORY + " not set");
    }

    /**
	 * Get the currencyId.
	 * @return the currencyId
	 */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
	 * Set the currencyId.
	 * @param currencyId the currencyId to set
	 */
    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    /**
	 * Get the organisationId.
	 * @return the organisationId
	 */
    public String getOrganisationId() {
        return organisationId;
    }

    /**
	 * Set the organisationId.
	 * @param organisationId the organisationId to set
	 */
    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    /**
	 * Get the password.
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * Set the password.
	 * @param password the password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * Get the userId.
	 * @return the userId
	 */
    public String getUserId() {
        return userId;
    }

    /**
	 * Set the userId.
	 * @param userId the userId to set
	 */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProviderUrl() {
        return providerUrl;
    }

    public void setProviderUrl(String providerUrl) {
        this.providerUrl = providerUrl;
    }

    public String getInitialContextFactory() {
        return initialContextFactory;
    }

    public void setInitialContextFactory(String initialContextFactory) {
        this.initialContextFactory = initialContextFactory;
    }
}
