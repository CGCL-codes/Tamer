package com.netstoke.core.contact;

import com.netstoke.common.IEntity;

/**
 * <p>The <code>IPhoneNumber</code> interface represents a phone number for an entity such as contact or organization.</p>
 * @author kmckee &lt;<a href="mailto:kevin.mckee@netstoke.com">kevin.mckee@netstoke.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public interface IPhoneNumber extends IEntity {

    /**
	 * Sets the country code.
	 * @param countryCode String
	 */
    public void setCountryCode(String countryCode);

    /**
	 * Returns the country code.
	 * @return String
	 */
    public String getCountryCode();

    /**
	 * Sets the area code.
	 * @param areaCode String
	 */
    public void setAreaCode(String areaCode);

    /**
	 * Returns the area code.
	 * @return String
	 */
    public String getAreaCode();

    /**
	 * Sets the local number
	 * @param localNumber String
	 */
    public void setLocalNumber(String localNumber);

    /**
	 * Returns the local number.
	 * @return String
	 */
    public String getLocalNumber();

    /**
	 * Sets the extension number.
	 * @param extension
	 */
    public void setExtension(String extension);

    /**
	 * Returns the extension.
	 * @return String
	 */
    public String getExtension();

    /**
	 * A helper method which attempts to pick apart 
	 * the full number and populate this <code>IPhoneNumber<code>
	 * with the proper values.
	 * @param fullNumber String
	 */
    public void setFullNumber(String fullNumber);

    /**
	 * A helper method which returns an 
	 * unformatted, concatenated version of the fields in
	 * this <code>IPhoneNumber</code>.
	 * @return String
	 */
    public String getFullNumber();

    /**
	 * Helper method which returns the concatenated string of
	 * +country_code (area_code) local_number x extension 
	 * @return String 
	 */
    public String getDisplayNumber();

    /**
	 * Sets flag indicating if this is a 
	 * primary phone number or not.
	 * @param isPrimary Boolean
	 */
    public void setIsPrimary(Boolean isPrimary);

    /**
	 * Returns flag indicating if this is a 
	 * primary phone number or not.
	 * @return Boolean
	 */
    public Boolean getIsPrimary();

    /**
	 * Sets the phone type.
	 * @param phoneType PhoneType
	 */
    public void setPhoneType(PhoneType phoneType);

    /**
	 * Returns the phone type.
	 * @return PhoneType
	 */
    public PhoneType getPhoneType();
}
