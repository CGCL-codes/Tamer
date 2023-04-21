package com.acv.service.emailEngine;

import com.acv.service.configuration.BaseConfiguration;
import com.acv.service.configuration.ConfigurationManager;

/**
 * Configuration class for the email engine. It extends the
 * {@link BaseConfiguration} class.
 *
 * @author Mickael Guesnon
 */
public class EmailEngineConfiguration extends BaseConfiguration {

    public static final String SMTP_HOSTNAME = "SMTP_HOST";

    public static final String SMTP_LOGIN = "SMTP_LOGIN";

    public static final String SMTP_PASSWORD = "SMTP_PASSWORD";

    public static final String EMAIL_SRC_ADR = "EMAIL_FROM";

    public static final String SMTP_EFORMS = "SMTP_EFORMS";

    private static EmailEngineConfiguration instance;

    /**
	 * Private construtor for singleton pattern.
	 */
    private EmailEngineConfiguration() {
        super.currentClassName = getClass().getName();
    }

    /**
	 * Static instance acessor method.
	 *
	 * @return {@link EmailEngineConfiguration} The instance of the
	 *         configuration class.
	 */
    public static EmailEngineConfiguration getInstance() {
        if (instance == null) instance = new EmailEngineConfiguration();
        return instance;
    }

    @Override
    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }
}
