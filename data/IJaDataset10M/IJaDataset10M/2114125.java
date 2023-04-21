package com.volantis.testtools.config;

import java.lang.StringBuffer;

/**
 *
 * A config builder for SMS channels.
 *
 */
public class SMSChannelConfigBuilder extends ChannelConfigBuilder {

    private ConfigValueChannelSms value;

    /** Creates a new instance of SMSChannelConfigBuilder */
    public SMSChannelConfigBuilder(ConfigValueChannelSms value) {
        super(value);
        this.value = value;
    }

    protected void renderChannelAttributes(StringBuffer config) {
        addArgumentElement(config, "smsc-ip", value.address);
        addArgumentElement(config, "smsc-port", value.port);
        addArgumentElement(config, "smsc-user", value.userName);
        addArgumentElement(config, "smsc-password", value.password);
        addArgumentElement(config, "smsc-bindtype", value.bindtype);
        addArgumentElement(config, "smsc-svctype", value.serviceType);
        addArgumentElement(config, "smsc-svcaddr", value.serviceAddress);
        addArgumentElement(config, "smsc-supportsmulti", value.supportsMulti);
    }
}
