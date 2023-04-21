package org.jnokii.core;

/**
 *  Insert the type's description here. Creation date: (8.2. 2001 23:19:40)
 *
 *@author     martin
 *@created    3 January 2002
 *@author:
 */
public interface JNokii {

    /**
   *  Delete the SMS message in the location
   *
   *@param  location  Value of param is 0 - 255
   */
    void deleteSMSMessage(int location);

    /**
   *  Stop communication with this phone
   */
    void end();

    /**
   *  Insert the method's description here. Creation date: (8.2. 2001 23:43:26)
   *
   *@return    byte
   */
    byte getBatteryLevel();

    /**
   *  Insert the method's description here. Creation date: (8.2. 2001 23:44:41)
   *
   *@return    byte
   */
    PowerType getPowerSource();

    /**
   *  Tells us how strong the signal is
   *
   *@return    the signal level in a range that makes sense for this phone
   */
    int getSignalLevel();

    /**
   *  Requests the sms message that is in the location specified by location
   *
   *@param  location  This value is between 0 and 255
   */
    void getSMSMessage(int location);

    /**
   *  System specific manner of specifying which port that we should use.
   *
   *@param  port  java.lang.String
   */
    void initialize(String port);

    /**
   * What is the phone we are talking to?
   *
   * @return A printable name for the phone
   */
    public String phoneId();
}
