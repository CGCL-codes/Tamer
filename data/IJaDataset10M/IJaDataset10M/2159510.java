package org.xmlcml.cml.units;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author pm286
 *
 */
public class Pressure extends Quantity {

    private static final Pattern pressurePattern = Pattern.compile("(\\-?\\d+(\\.\\d+)?) *(atm|Pa|gPa)");

    private static Map<String, Pressure> pressureMap;

    static {
        pressureMap = new HashMap<String, Pressure>();
        pressureMap.put("default", new Pressure(1.0, JumboUnit.BAR));
    }

    ;

    protected static String getRole() {
        return "pressure";
    }

    private Pressure(double val, JumboUnit units) {
        super(val, units);
    }

    /**
	 * 
	 * @param s pressure string
	 * @return time or null
	 */
    public static Quantity getPressure(String s) {
        Quantity pressure = null;
        Matcher matcher = pressurePattern.matcher(s);
        if (matcher.matches()) {
            JumboUnit unit = getUnit(matcher, getRole(), 3, JumboUnit.pressureMap);
            pressure = new Pressure(new Double(matcher.group(1)).doubleValue(), unit);
        } else {
            pressure = pressureMap.get(s.toLowerCase());
        }
        return pressure;
    }
}
