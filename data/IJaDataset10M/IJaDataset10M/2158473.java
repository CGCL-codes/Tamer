package com.jcorporate.expresso.core.dbobj;

import com.jcorporate.expresso.core.i18n.Messages;
import com.jcorporate.expresso.core.misc.StringUtil;
import org.apache.log4j.Logger;
import java.util.Locale;

/**
 * An ISO valid value is a enumerated collection very similar to a
 * pure Struts label and value bean with <em>internationalization</em>
 * (i18n) support.  It typically represent an item of data that is
 * displayed in a drop-down list box or a menu selection.  A valid
 * value has a real value as known as a key and description.
 * <p>
 * To support internationalisation (i18n) the <code>description</code>
 * field also has a dual role. The description field is used a look-up
 * key into a resource bundle during <em>canonisation</em>.
 * </p>
 * @author Peter A. Pilgrim, Fri Dec 27 22:33:27 GMT 2002, Created XeNoNSoFT.com
 * @version $Id: ISOValidValue.java 3 2006-03-01 11:17:08Z gpolancic $
 */
public class ISOValidValue extends ValidValue {

    private static Logger log = Logger.getLogger(ISOValidValue.class);

    /**
     * Default constructor for creating a valid value.
     * Please note: no canonization takes place within this method.
     */
    public ISOValidValue() {
        super();
    }

    /**
     * Original constructor for creating a valid value.
     * Please note: no canonization takes place within this method.
     *
     * @param   newValue the real value of the enumeration
     * @param   newDescrip the description of the enumeration
     */
    public ISOValidValue(String newValue, String newDescrip) {
        super(newValue, newDescrip);
    }

    /**
     * Constructor of valid value enumeration which automatically
     * <b>canonizes</b> the description attribute if it can be found
     * in�the schema resource bundle in�the default locale
     *
     * @param schemaClass the fully qualified class name of the schema
     * @param prefix the optional prefix argument
     * @param value the real value string of the enumeration
     * @param description the description string of the enumeration
     * that is used as look-up key in�the resource bundle
     *
     * @see #canonize( String schemaClass, Locale locale, String prefix )
     */
    public ISOValidValue(String schemaClass, String prefix, String value, String description) {
        this(schemaClass, Locale.getDefault(), prefix, value, description);
    }

    /**
     * Constructor of valid value enumeration which automatically
     * <b>canonizes</b> the description attribute if it can be found
     * in�the schema resource bundle
     *
     * @param schemaClass the fully qualified class name of the schema
     * @param locale the locale
     * @param prefix the optional prefix argument
     * @param value the real value string of the enumeration
     * @param description the description string of the enumeration
     * that is used as look-up key in�the resource bundle
     *
     * @see #canonize( String schemaClass, Locale locale, String prefix )
     */
    public ISOValidValue(String schemaClass, Locale locale, String prefix, String value, String description) {
        setValue(value);
        setDescription(description);
        canonize(schemaClass, locale, prefix);
    }

    /**
     * Constructor of valid value enumeration which automatically
     * <b>canonizes</b> the description attribute if it can be found
     * in�the schema resource bundle
     *
     * @param schemaClass the fully qualified class name of the schema
     * @param request the request context
     * @param prefix the optional prefix argument
     * @param value the real value string of the enumeration
     * @param description the description string of the enumeration
     * that is used as look-up key in�the resource bundle
     *
     * @see #canonize( RequestContext request, String schemaClass, String prefix )
     */
    public ISOValidValue(RequestContext request, String schemaClass, String prefix, String value, String description) {
        setValue(value);
        setDescription(description);
        canonize(request, schemaClass, prefix);
    }

    /**
     * Gets the real value of the valid value
     *
     * @return the value string
     *
     * @see #setValue
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the real value of the valid value
     *
     * @param value the new value string
     *
     * @see #getValue
     */
    public void setValue(String value) {
        this.value = StringUtil.notNull(value);
    }

    /**
     * Gets the real value of the valid value as a cache key
     *
     * @return the value string as a cache key
     *
     * @see #getValue
     */
    public String getKey() {
        return getValue();
    }

    /**
     * Gets the description of the valid value
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the real description of the valid value
     *
     * @param description the new description string
     *
     * @see #getDescription
     */
    public void setDescription(String description) {
        this.description = StringUtil.notNull(description);
    }

    /**
     * This is method will attempt to convert the
     * <code>description</code> attribute into a the default locale value
     * string from a message resource bundle. If the
     * <code>prefix</code> parameter is not null then the
     * <code>description</code> attribute is prepended with a full
     * stop "." to the original description string to make a new look
     * up key.  Otherwise if the <code>prefix</code> parameter is null
     * then original description is the look up key.  The key value is
     * used to look up the description from a resource bundle.
     *
     * @param schemaClass the fully qualified class name of the schema
     * @param prefix the optional prefix argument
     *
     * @see #canonize( String schemaClass, Locale locale, String prefix )
     *
     * @return result the string if found in�the resource bundle or null
     */
    public String canonize(String schemaClass, String prefix) {
        return canonize(schemaClass, Locale.getDefault(), prefix);
    }

    /**
     * This is method will attempt to convert the
     * <code>description</code> attribute into a localised value
     * string from a message resource bundle. If the
     * <code>prefix</code> parameter is not null then the
     * <code>description</code> attribute is prepended with a full
     * stop "." to the original description string to make a new look
     * up key.  Otherwise if the <code>prefix</code> parameter is null
     * then original description is the look up key.  The key value is
     * used to look up the description from a resource bundle.
     *
     * @param schemaClass the fully qualified class name of the schema
     * @param locale the locale
     * @param prefix the optional prefix argument
     *
     * @see #getDescription
     *
     * @return result the string if found in�the resource bundle or null
     */
    public String canonize(String schemaClass, Locale locale, String prefix) {
        String key = (prefix != null ? prefix + "." + description : description);
        String result = null;
        try {
            result = Messages.getString(schemaClass, locale, key, new Object[0]);
            if (result != null) description = result;
        } catch (IllegalArgumentException iae) {
            result = null;
        }
        return result;
    }

    /**
     * This is method will attempt to convert the
     * <code>description</code> attribute into a localised value
     * string from a message resource bundle. If the
     * <code>prefix</code> parameter is not null then the
     * <code>description</code> attribute is prepended with a full
     * stop "." to the original description string to make a new look
     * up key.  Otherwise if the <code>prefix</code> parameter is null
     * then original description is the look up key.  The key value is
     * used to look up the description from a resource bundle.
     *
     *
     * @param schemaClass the fully qualified class name of the schema
     * @param request the request context containing locale and dbcontext
     * @param prefix the optional prefix argument
     *
     *
     * @see com.jcorporate.expresso.core.i18n.Messages#getString(String schemaClass, Locale l, String stringCode, Object[] args)
     * @see #canonize( String schemaClass, String prefix )
     * @see #canonize( String schemaClass, Locale locale, String prefix )
     *
     * @return result the string if found in�the resource bundle or null
     */
    public String canonize(RequestContext request, String schemaClass, String prefix) {
        String key = (prefix != null ? prefix + "." + description : description);
        String result = null;
        try {
            result = Messages.getString(schemaClass, request.getLocale(), key, new Object[0]);
            if (result != null) description = result;
        } catch (IllegalArgumentException iae) {
            result = null;
        }
        return result;
    }

    /**
     * Human readable string for debugging
     * @return java.lang.String
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ISOValidValue@" + Integer.toHexString(this.hashCode()) + "{");
        buf.append("value:`" + value + "' (`" + description + "')");
        buf.append("}");
        return buf.toString();
    }
}
