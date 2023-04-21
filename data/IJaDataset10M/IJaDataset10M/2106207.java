package com.xentelco.asterisk.agi.command;

import java.util.HashMap;
import java.util.Map;
import com.xentelco.asterisk.agi.AgiCommandResponse;
import com.xentelco.asterisk.agi.codec.command.response.AgiCommandResponseUtils;

/**
 * @author Ussama Baggili
 * 
 */
public class DatabaseGet extends AbstractCommand {

    private static final String command = "DATABASE GET";

    private String family;

    private String key;

    private String responseValue;

    static {
        Map<String, CommandResult> resultInfo = new HashMap<String, CommandResult>();
        resultInfo.put("1", CommandResult.SUCCESS);
        resultInfo.put("0", CommandResult.FAILURE);
        AgiCommandResponse.commandResultTypes.put(DatabaseGet.class, resultInfo);
    }

    /**
     * 
     */
    public DatabaseGet() {
    }

    /**
     * @param family
     * @param key
     */
    public DatabaseGet(String family, String key) {
        this.family = family;
        this.key = key;
    }

    /**
     * 
     * @return "DATABASE DELTREE \<family\> \<keytree\> "
     */
    public String getCommand() {
        return new StringBuffer().append(command).append(' ').append(family).append(' ').append(key).toString();
    }

    @Override
    public void processResult(String unprocessedResult) {
        super.processResult(unprocessedResult);
        responseValue = AgiCommandResponseUtils.getRequestVariableValue(unprocessedResult);
    }

    /**
     * @return the family
     */
    public String getFamily() {
        return this.family;
    }

    /**
     * @param family
     *            the family to set
     */
    public void setFamily(String family) {
        this.family = family;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the responseValue
     */
    public String getResponseValue() {
        return this.responseValue;
    }

    /**
     * @param responseValue
     *            the responseValue to set
     */
    public void setResponseValue(String responseValue) {
        this.responseValue = responseValue;
    }
}
