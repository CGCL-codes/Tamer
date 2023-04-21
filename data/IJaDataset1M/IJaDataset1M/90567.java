package org.gwtoolbox.bean.client.attributes;

import java.util.HashMap;

/**
 * @author Uri Boness
 */
public class FormatAttributes extends HashMap<String, Object> {

    public String value() {
        return (String) get("value");
    }
}
