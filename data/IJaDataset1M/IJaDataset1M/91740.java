package org.nist.usarui;

import java.util.*;
import java.util.regex.*;

/**
 * Represents a string of data taken from the USAR socket.
 *
 * @author Stephen Carlson (NIST)
 */
public class USARPacket {

    /**
	 * Splits packets apart on braces.
	 */
    public static final Pattern SPLITTER = Pattern.compile("[{}]");

    /**
	 * Cleans off "_xxx" suffixes on strings.
	 */
    public static final Pattern SUFFIX = Pattern.compile("_[0-9]+$");

    private Map<String, String> params;

    private String message;

    private boolean response;

    private String type;

    /**
	 * Constructs a USAR packet from the status message.
	 *
	 * @param message the message to handle
	 * @param response whether the message was sent from the server (true) or client (false)
	 */
    public USARPacket(String message, boolean response) {
        String[] parts = SPLITTER.split(message);
        String key, part;
        int index, dup;
        Map<String, String> values = new LinkedHashMap<String, String>(32);
        if (message.length() < 0) throw new IllegalArgumentException("USARPacket cannot be made from empty string");
        type = null;
        for (String value : parts) {
            part = value.trim();
            if (part.length() > 0) {
                index = part.indexOf(' ');
                if (type == null) {
                    if (index < 0) type = part.toUpperCase(); else type = part;
                } else if (index > 0) {
                    key = part.substring(0, index);
                    if (values.containsKey(key)) {
                        for (dup = 0; values.containsKey(key + "_" + dup); dup++) {
                        }
                        key += "_" + dup;
                    }
                    values.put(key, part.substring(index + 1).trim());
                } else values.put("Other", part);
            }
        }
        if (type == null) throw new IllegalArgumentException("No type in USARPacket message");
        this.message = message;
        this.response = response;
        params = Collections.unmodifiableMap(values);
    }

    /**
	 * Duplicates another USAR packet.
	 *
	 * @param other the packet to copy
	 */
    public USARPacket(USARPacket other) {
        message = other.getMessage();
        params = new LinkedHashMap<String, String>(other.getParams());
        response = other.isResponse();
        type = other.getType();
    }

    /**
	 * Gets the value of a specified parameter.
	 *
	 * @param key the parameter to get
	 * @return its value, or null if not present
	 */
    public String getParam(String key) {
        return params.get(key);
    }

    /**
	 * Gets the entire list of available parameters.
	 *
	 * @return the parameters input
	 */
    public Map<String, String> getParams() {
        return params;
    }

    /**
	 * Returns this packet's "original" message.
	 * 
	 * @return the message used to construct the packet
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * Gets the packet's type (first word received)
	 *
	 * @return the packet type (e.g. "NFO", "SEN")
	 */
    public String getType() {
        return type;
    }

    /**
	 * Gets whether this packet is a server response.
	 *
	 * @return whether the packet was received (not sent)
	 */
    public boolean isResponse() {
        return response;
    }

    public String toString() {
        boolean blu = params.size() > 0 || type.indexOf(' ') < 0;
        StringBuilder output = new StringBuilder(512);
        if (response) output.append("&gt;&nbsp;<i>");
        if (blu) output.append("<font color=\"#000099\">");
        output.append(type);
        if (blu) output.append("</font>");
        for (Map.Entry<String, String> param : params.entrySet()) {
            output.append(" {<font color=\"#009900\">");
            output.append(SUFFIX.matcher(param.getKey()).replaceAll(""));
            output.append("</font> ");
            output.append(param.getValue());
            output.append("}");
        }
        if (response) output.append("</i>");
        return Utils.asHTML(output.toString());
    }
}
