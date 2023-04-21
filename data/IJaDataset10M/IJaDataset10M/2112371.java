package com.google.code.sagetvaddons.sagealert.server.events;

import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;

/**
 * @author dbattams
 *
 */
public final class SmtpTestEvent implements SageAlertEvent {

    /**
	 * 
	 */
    public SmtpTestEvent() {
    }

    public String getLongDescription() {
        return "If you're reading this message then your current SMTP settings in SageAlert are valid!  Be sure to save the settings after testing them.";
    }

    public String getMediumDescription() {
        return getLongDescription();
    }

    public String getShortDescription() {
        return getLongDescription();
    }

    public String getSource() {
        return "SmtpTestEvent";
    }

    public String getSubject() {
        return "SMTP Server Settings Test Results";
    }
}
