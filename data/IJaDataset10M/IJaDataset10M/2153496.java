package org.red5.samples.services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;

/**
 * The Echo service is used to test all of the different datatypes 
 * and to make sure that they are being returned properly.
 *
 * @author The Red5 Project (red5@osflash.org)
 * @author Chris Allen (mrchrisallen@gmail.com)
 */
public interface IEchoService {

    /**
	 * Used to verify that Spring has loaded the bean.
	 *
	 */
    public abstract void startUp();

    /**
	 * Verifies that a boolean that is passed in returns correctly.
	 * 
	 * @param bool
	 * @return input value
	 */
    public abstract boolean echoBoolean(boolean bool);

    /**
	 * Verifies that a Number that is passed in returns correctly.
	 * 
	 * Flash Number = double
	 * 
	 * @param num
	 * @return input value
	 */
    public abstract double echoNumber(double num);

    /**
	 * Verifies that a String that is passed in returns correctly.
	 * 
	 * @param string
	 * @return input value
	 */
    public abstract String echoString(String string);

    /**
	 * Verifies that a Date that is passed in returns correctly.
	 * 
	 * @param date
	 * @return input value
	 */
    public abstract Date echoDate(Date date);

    /**
	 * Verifies that a Flash Object that is passed in returns correctly.
	 * Flash Object = java.utils.Map
	 * 
	 * @param obj
	 * @return input value
	 */
    public abstract Map echoObject(Map obj);

    /**
	 * Verifies that a Flash simple Array that is passed in returns correctly.
	 * Flash simple Array = Object[]
	 * 
	 * @param array
	 * @return input value
	 */
    public abstract Object[] echoArray(Object[] array);

    /**
	 * Verifies that a Flash multi-dimensional Array that is passed in returns itself.
	 * Flash multi-dimensional Array = java.utils.List
	 * 
	 * @param list
	 * @return input value
	 */
    public abstract List echoList(List list);

    /**
	 * Verifies that Flash XML that is passed in returns itself.
	 * Flash XML = org.w3c.dom.Document
	 * 
	 * @param xml
	 * @return input value
	 */
    public abstract Document echoXML(Document xml);
}
