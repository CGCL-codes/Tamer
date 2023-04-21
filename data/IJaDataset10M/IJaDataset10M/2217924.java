package com.liferay.util.format;

/**
 * <a href="PhoneNumberFormat.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.1 $
 *
 */
public interface PhoneNumberFormat {

    public String format(String phoneNumber);

    public String strip(String phoneNumber);
}
