package pl.xperios.rdk.shared.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import pl.xperios.rdk.shared.Bean;

/**
 *
 * @author Praca
 */
public class Locale extends Bean<Long> {

    /**
     *
     */
    public final transient String SPLIT_REGEX = "##";

    /**
     *
     */
    protected Locale() {
        set("messages", new HashMap<String, String>());
    }

    /**
     *
     * @param id
     * @param code
     * @param name
     * @param messages
     */
    public Locale(Long id, String code, String name, Map<String, String> messages) {
        this();
        set("id", id);
        set("code", code);
        set("name", name);
        set("messages", messages);
    }

    @Override
    public String getPropertyId() {
        return "id";
    }

    /**
     *
     * @return
     */
    public Long getId() {
        return this.get("id");
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        set("id", id);
    }

    /**
     *
     * @return
     */
    public Map<String, String> getMessages() {
        return this.get("messages");
    }

    /**
     *
     * @param messages
     */
    public void setMessages(Map<String, String> messages) {
        set("messages", messages);
    }

    /**
     *
     * @return
     */
    public String getCode() {
        return this.get("code");
    }

    /**
     *
     * @param code
     */
    public void setCode(String code) {
        set("code", code);
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.get("name");
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        set("name", name);
    }

    /**
     *
     * @return
     */
    public String getDateFormat() {
        return this.get("dateFormat");
    }

    /**
     *
     * @param dateFormat
     */
    public void setDateFormat(String dateFormat) {
        set("dateFormat", dateFormat);
    }

    /**
     *
     * @return
     */
    public String getDateTimeFormat() {
        return this.get("dateTimeFormat");
    }

    /**
     *
     * @param dateTimeFormat
     */
    public void setDateTimeFormat(String dateTimeFormat) {
        set("dateTimeFormat", dateTimeFormat);
    }

    /**
     *
     * @return
     */
    public String getCurrencyFormat() {
        return this.get("currencyFormat");
    }

    /**
     *
     * @param currencyFormat
     */
    public void setCurrencyFormat(String currencyFormat) {
        set("currencyFormat", currencyFormat);
    }

    /**
     *
     * @return
     */
    public String getCurrencyPrefix() {
        return this.get("currencyPrefix");
    }

    /**
     *
     * @param currencyPrefix
     */
    public void setCurrencyPrefix(String currencyPrefix) {
        set("currencyPrefix", currencyPrefix);
    }

    /**
     *
     * @return
     */
    public String getCurrencySuffix() {
        return this.get("currencySuffix");
    }

    /**
     *
     * @param currencySuffix
     */
    public void setCurrencySuffix(String currencySuffix) {
        set("currencySuffix", currencySuffix);
    }

    @Override
    public String toString() {
        return "Locale{ID:" + getCode() + ", Name:" + getName() + ", Initialized: " + ((Map<String, String>) get("messages")).size() + "}";
    }

    /**
     *
     * @param messageKey
     * @param messageValue
     */
    public void addMessage(String messageKey, String messageValue) {
        ((Map<String, String>) get("messages")).put(messageKey, messageValue);
    }

    /**
     *
     * @param messageKey
     * @return
     */
    public String getMessage(String messageKey) {
        return ((Map<String, String>) get("messages")).get(messageKey);
    }

    /**
     *
     * @param messageKey
     * @param parameters
     * @return
     */
    public String getMessagePrepared(String messageKey, String[] parameters) {
        return prepareMessage(getMessage(messageKey), parameters);
    }

    /**
     *
     * @param message
     * @param parameters
     * @return
     */
    public String prepareMessage(String message, String[] parameters) {
        if (message == null) {
            return message;
        }
        int lastParameterUsed = 0;
        String out = message;
        int lastCharIndexUsed = -1;
        int lastChar = -1;
        while ((lastChar = out.indexOf(SPLIT_REGEX)) > lastCharIndexUsed) {
            lastCharIndexUsed = lastChar;
            String parameter = "";
            if (parameters == null || lastParameterUsed >= parameters.length) {
                parameter = SPLIT_REGEX;
            } else {
                parameter = parameters[lastParameterUsed];
            }
            lastParameterUsed++;
            out = out.replaceFirst(SPLIT_REGEX, parameter);
        }
        return out;
    }

    @Override
    public ArrayList<String> getAllPropertiesNames() {
        ArrayList<String> out = new ArrayList<String>();
        out.add("id");
        out.add("code");
        out.add("name");
        out.add("messages");
        return out;
    }

    @Override
    protected HashMap<String, String> initPropertiesClass() {
        HashMap<String, String> out = new HashMap<String, String>();
        out.put("id", BigDecimal.class.getName());
        out.put("code", String.class.getName());
        out.put("name", String.class.getName());
        out.put("messages", String.class.getName());
        return out;
    }
}
