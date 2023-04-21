package com.xentelco.asterisk.agi.codec.command.request;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.xentelco.asterisk.agi.Action;

/**
 * @author Ussama Baggili
 * 
 */
public class AgiCommandRequest implements Serializable {

    private Action agiCommand;

    public AgiCommandRequest() {
    }

    public AgiCommandRequest(Action agiCommand) {
        this.agiCommand = agiCommand;
    }

    /**
     * @return the agiCommand
     */
    public Action getAgiCommand() {
        return this.agiCommand;
    }

    /**
     * @param agiCommand
     *            the agiCommand to set
     */
    public void setAgiCommand(Action agiCommand) {
        this.agiCommand = agiCommand;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
