package jaron.flightgear;

import java.util.HashMap;

/**
 * The <code>FlightGearParser</code> is an abstract class that must be implemented
 * by a subclass.
 * 
 * @author      jarontec gmail com
 * @version     1.2
 * @since       1.0
 */
public abstract class FlightGearParser {

    protected HashMap<String, String> data;

    /**
   * Parses the data which is received via the FlightGear generic i/o interface.
   *  
   * @param data    the data received from FlightGear
   * @return        <code>true</code> if the parsing was successful
   */
    public abstract Boolean parse(String data);

    /**
   * Returns a <code>double</code> value that is parsed from the FlightGear output.
   * If the value could not be found or converted then 0 is returned.
   * 
   * @param key a reference to the FlightGear data
   * @return    the <code>double</code> value referenced by the <code>key</code>
   */
    public double getDouble(String key) {
        double d = 0;
        try {
            String s = (String) data.get(key);
            if (s != null) d = Double.valueOf(s).doubleValue();
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException in FlightGearParser::getDouble(): " + e.getMessage());
        }
        return d;
    }

    /**
   * Returns a <code>String</code> value that is parsed from the FlightGear output.
   * If the value could not be found an empty string is returned.
   * 
   * @param key a reference to the FlightGear data
   * @return    the <code>double</code> value referenced by the <code>key</code>
   */
    public String getString(String key) {
        String s = (String) data.get(key);
        if (s != null) return s; else return "";
    }

    /**
   * Returns true if the parser has some data available. This is used to determine
   * if the FlightGear data has been correctly received.
   *  
   * @return a boolean value indicating if data is available
   */
    public Boolean hasData() {
        return data.size() > 0;
    }
}
