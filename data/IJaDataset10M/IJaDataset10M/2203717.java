package com.jcorporate.expresso.core.dataobjects;

import org.apache.oro.text.regex.Pattern;
import java.util.Iterator;

/**
 * This interface will eventually have the same public functionality as
 * <code>DBField</code>.  It is just used as &quot;Interface&quote; glue right
 * now.  It will be fleshed out significantly in future versions.
 * <p>
 * This interface represents the descriptive data for a particular DataField.
 * </p>
 *
 * @author Michael Rimov
 * @version $Revision: 3 $ on  $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 * @since Expresso 5.0
 */
public interface DataFieldMetaData {

    /**
     * Does this field allow nulls?
     *
     * @return    boolean True if the field allows null, else false if it does not
     */
    public boolean allowsNull();

    /**
     * Return the description of this field.  This field name is not i18n'ized,
     * use DataObjectMetaData with Locale parameter to get the i18n'ized escription
     *
     * @return    String Description of the field
     */
    public String getDescription();

    /**
     * Return the length of this field as an integer
     *
     * @return    int The length of this field in characters
     */
    public int getLengthInt();

    /**
     * Return the value for the lookupObject for this field
     *
     * @return String for the lookup object for this field
     */
    public String getLookupObject();

    /**
     * When you get a lookup object, to perform a complete mapping between the
     * two, you need to know what field name in the remote object maps to this
     * field.
     * @return java.lang.String or null if there is no lookup field
     * @throws IllegalArgumentException if the field name does not exist
     */
    public String getLookupField();

    /**
     * Future versions of Expresso will rely more on objects that are shown as
     * unique by a combination of their classname and their definition names.  All
     * instances of Defineable must have a corresponding definition name.
     * @return java.lang.String
     */
    public String getLookupDefinition();

    /**
     * Return the name of the field
     *
     * @return    String The name of this field
     */
    public String getName();

    /**
     * Return the precision of this field as an integer
     *
     * @return    int The precision of this field
     */
    public int getPrecision();

    /**
     * Return the database type of the field as specified with a
     * string in the DBObject itself
     *
     * @return    The type of this field
     */
    public String getTypeString();

    /**
     * Is this field a key field?
     *
     * @return true if this field is a key field
     */
    public boolean isKey();

    /**
     * Is this field multi-valued?
     *
     * @return    boolean True if the field is multi-valued, else false
     */
    public boolean isMultiValued();

    /**
     * Return the field's read-only
     *
     * @return True if the field is readonly, else false if it is not
     */
    public boolean isReadOnly();

    /**
     * is the field a boolean?
     * cache answer for efficiency, since field type is immutable
     *
     * @return True if the field is boolean
     */
    public boolean isBooleanType();

    /**
     * Is this field fall into the classification of a long character
     * object?
     * @return boolean true if it does
     */
    public boolean isCharacterLongObjectType();

    /**
     * Does this field fall into a class of BLOB object data types?
     * @return boolean true if it does
     */
    public boolean isLongObjectType();

    /**
     * Is it a BLOB?  This is calculated by if it is a long object
     * and is NOT a character long object.
     * @return boolean True if it is
     */
    public boolean isBinaryObjectType();

    /**
     * is the field a quoted text field?
     * cache answer for efficiency, since field type is immutable
     *
     * @return True if the field is text field
     */
    public boolean isQuotedTextType();

    /**
     * is the field a numeric field?
     * cache answer for efficiency, since field type is immutable
     *
     * @return true if the field is numberic field
     */
    public boolean isNumericType();

    /**
     * is the field a date or time field?
     * cache answer for efficiency, since field type is immutable
     *
     * @return true if the field is date or time field
     */
    public boolean isDateType();

    /**
     * is the field a date or time field?
     * cache answer for efficiency, since field type is immutable
     * author 		Yves Henri AMAIZO
     * @return true if the field is date field
     */
    public boolean isDateOnlyType();

    public boolean isFloatingPointType();

    /**
     * is the field a date or time field?
     * cache answer for efficiency, since field type is immutable
     * author 		Yves Henri AMAIZO
     *
     * @return true if the field is time field
     */
    public boolean isTimeType();

    /**
     * is the field a date or time field?
     * cache answer for efficiency, since field type is immutable
     * author 		Yves Henri AMAIZO
     *
     * @return true if the field is dateTime or Timestamp field
     */
    public boolean isDateTimeType();

    /**
     * Return the field's secret status
     *
     * @return True if the field is secret, else false if it is not
     */
    public boolean isSecret();

    /**
     * Return the field's hashed status
     *
     * @todo This is not completely implemented yet.
     * @return True if the field is secret, else false if it is not
     */
    public boolean isHashed();

    /**
     * Return the field's hashed status
     *
     * @todo This is not completely implemented yet.
     * @return True if the field is secret, else false if it is not
     */
    public boolean isEncrypted();

    /**
     * Is this field a virtual field? E.g. not stored in the database
     *
     * @return    boolean True if the field is virtual, else false
     */
    public boolean isVirtual();

    /**
     * Set this field as a key field (or not)
     *
     * @param newKey Is this field a key?
     */
    public void setKey(boolean newKey);

    /**
     * Set the value for the "lookup object" for this field. This is
     * a database object name which can be used to look up valid
     * values for this field by the user. This is used by the standard
     * maintenance forms to create a "Lookup" link alongside the
     * field if this value is set
     *
     * @param objectName the classname of the lookup object
     */
    public void setLookupObject(String objectName);

    /**
     * Set this field to be "multi-valued". A multi-valued field has
     * a specific set of valid values, often from another database
     * object. Any multi-valued field may be used in a call to the
     * getValues method, which will return a hashtable of the valid
     * values for the field and descriptions for those values.
     *
     * @param    newMulti True if the field is multi-valued, false if it is not
     * @see com.jcorporate.expresso.core.dbobj.DBObject.getFieldValue
     */
    public void setMultiValued(boolean newMulti);

    /**
     * Set the field's hashed status.  Only works if the field is a string
     * data type (in the future CLOB should be ok too) [This is currently not
     * yet implemented and tested]
     *
     * @param newValue true if you want this field hashed.
     * @throws Exception on error
     */
    public void setHashed(boolean newValue);

    /**
     * Set the field's encrypted status
     * @todo This is not completely implemented yet.
     *
     * @param newValue new value if you want an encrypted field
     * @throws Exception if the field type is not a string data type
     */
    public void setEncrypted(boolean newValue);

    /**
     * Set the field as a read-only field. Read only fields are still used
     * against the database, but are not offered for updating when the
     * automatic database maintenance servlet creates a form on the screen. Note
     * this is different from the setAutoIncremented method below, which
     * means this field will not participate in any add or update statement
     * to the database.
     */
    public void setReadOnly();

    /**
     * Is this field an auto-incremented field?
     *
     * @return true if this field is autoincremented
     */
    public boolean isAutoIncremented();

    /**
     * Set the field as a 'secret' field. Secret fields are not shown
     * in listings of data from this database object, and are only available
     * to users with update, add or delete permissions
     */
    public void setSecret();

    /**
     * Set this field as a virtual field. A virtual field is part of the object
     * but not stored in the database table.
     *
     * @param    newVirtual True to make this object virtual, false if it is not
     */
    public void setVirtual(boolean newVirtual);

    /**
     * Set a regular expression "mask" for this field that specifies it's
     * valid values.  The mask should already be compiled by the regular
     * expression compiler
     * @param newMask The compiled regular expression mask
     *
     */
    public void setMask(Pattern newMask);

    /**
     * Get the compiled regular expression for this field.
     * @return the precompiled regular expression mask
     */
    public Pattern getMask();

    /**
     * Removes an attribute from this Field object.
     * @param attribName The name of the attribute to remove from this field.
     */
    public void removeAttribute(String attribName);

    /**
     * Sets an attribute for this particular field.
     * @param attribName the name of the attribute
     * @param attribValue the value of the attribute by this name
     */
    public void setAttribute(String attribName, Object attribValue);

    /**
     * Returns an attribute keyed by name.  An attribute is an arbitrary object
     * you can associate with a particular db field.
     * @param attribName the name of the attribute to get
     * @return java.lang.Object the object associated with this attribute name
     */
    public Object getAttribute(String attribName);

    /**
     * Retrieve a list of all attribute names in the field
     * @return java.util.Set
     */
    public java.util.Set getAllAttributes();

    /**
     * Returns a copy of all attributes associated with this field.
     * @return a valid Iterator to a HashMap OR null if no attributes exist for
     * this field
     */
    public Iterator getAttributesIterator();
}
