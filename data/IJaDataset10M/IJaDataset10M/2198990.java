package edu.psu.its.lionshare.security.authorization;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Enumeration;
import java.util.Set;
import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * A Class representation of an attribute that could be used to make 
 * authorization decisions in a policy.
 *
 * @author Lorin Metzger
 *
 */
public class AuthorizationAttribute {

    private static final Log LOG = LogFactory.getLog(AuthorizationAttribute.class);

    public static final String EDU_PERSON_NAME = "urn:mace:dir:attribute-def:eduPersonPrincipalName";

    public static final String NAME_CONSTANT = "_name";

    public static final String DISPLAY_CONSTANT = "_display";

    private static ResourceBundle attribute_displays = null;

    /**
   * The name/id of this attribute
   */
    private String attribute_name;

    private String attribute_display;

    /**
   * The list of values associated with this attribute.
   */
    private Set attribute_values;

    static {
        try {
            attribute_displays = ResourceBundle.getBundle("AuthorizationDisplays");
        } catch (Exception e) {
            LOG.debug("Unable to read Properties File" + e);
        }
    }

    /**
   *
   * Constructs a new AuthorizationAttribute with an empty values list.
   *
   * @param String attribute - The name/id of the attribute.
   * 
   */
    public AuthorizationAttribute(String attribute, String display) {
        this.attribute_name = attribute;
        this.attribute_display = display;
    }

    /**
   *
   * Returns the name/id of this attribute.
   *
   * @return String - attribute name.
   *
   */
    public String getAttributeName() {
        return this.attribute_name;
    }

    public String getAttributeDisplayComponent() {
        return this.attribute_display;
    }

    public AuthorizationAttribute newInstance() {
        return new AuthorizationAttribute(this.attribute_name, this.attribute_display);
    }

    /**
   *
   * Add a new attribute value to the list of values.
   *
   * @param String value - The attribute value.
   *
   */
    public void addAttributeValue(String value) {
        if (attribute_values == null) {
            attribute_values = new HashSet();
        }
        attribute_values.add(value);
    }

    /**
   *
   * Returns the list of attributes values associated with this attribute.
   *
   * @return List - Attribute values.
   *
   */
    public List getAttributeValues() {
        if (attribute_values == null) {
            return new ArrayList();
        }
        return new ArrayList(attribute_values);
    }

    /**
   *
   * These attributes are going to be hard coded for now, they could 
   * eventually be loaded from a properties or configuration file.
   *
   * @return List - A <String>List of available AuthorizationAttributes.
   *
   */
    public static List getAvailableAuthorizationAttributes() {
        ArrayList list = new ArrayList();
        for (Enumeration e = attribute_displays.getKeys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            if (key.indexOf(NAME_CONSTANT) != -1) {
                String name = attribute_displays.getString(key);
                list.add(name);
            }
        }
        return list;
    }

    /**
   *
   * The display component used to display/edit the property with the given
   * name. Hopefully we will eventually be able to pull this information from
   * a properties file. Hardcoded for now.
   *
   * @param String id - The name/id of the attribute.
   * 
   * @return String - Display component class as a String, null
   *                  if attribute id does not exists.
   */
    public static String getDisplayComponentForId(String id) {
        if (attribute_displays != null) {
            for (Enumeration e = attribute_displays.getKeys(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                if (key.indexOf(NAME_CONSTANT) != -1) {
                    String name = attribute_displays.getString(key);
                    if (name.equals(id)) {
                        int idx = key.indexOf(NAME_CONSTANT);
                        key = key.substring(0, idx) + DISPLAY_CONSTANT;
                        return attribute_displays.getString(key);
                    }
                }
            }
        }
        return null;
    }

    public static String getPrincipleAttributeName() {
        return EDU_PERSON_NAME;
    }
}
