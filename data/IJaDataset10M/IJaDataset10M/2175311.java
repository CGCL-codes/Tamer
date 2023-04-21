package org.jazzteam.edu.algo.numberConverterHor1zont.converter;

import org.jazzteam.edu.algo.numberConverterHor1zont.localization.ILocalizator;

public interface IConverter {

    /**
	 * Converting long value to string
	 * 
	 * @param number
	 * @return
	 * @version 1
	 */
    String convertNumber(long number);

    /**
	 * Converting string number to string
	 * 
	 * @param number
	 * @return
	 * @version 1
	 */
    String convertNumber(String number);

    /**
	 * Set localizator
	 * 
	 * @param localizator
	 * @version 1
	 */
    void setLocalizator(ILocalizator localizator);
}
