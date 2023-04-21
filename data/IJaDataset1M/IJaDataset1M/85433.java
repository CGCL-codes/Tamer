package acide.utils;

import java.awt.Color;
import java.util.Scanner;

/**
 * ACIDE - A Configurable IDE utilities class.
 * 
 * @version 0.8
 */
public class AcideUtilities {

    /**
	 * ACIDE - A Configurable IDE utilities unique class instance.
	 */
    private static AcideUtilities _instance;

    /**
	 * Returns the ACIDE - A Configurable IDE utilities unique class instance.
	 * 
	 * @return the ACIDE - A Configurable IDE utilities unique class instance.
	 */
    public static AcideUtilities getInstance() {
        if (_instance == null) _instance = new AcideUtilities();
        return _instance;
    }

    /**
	 * Parses a java.awt.Color[r=x,g=x,b=x] format into a color.
	 * 
	 * @param color
	 *            string that contains the color.
	 * 
	 * @return the parsed color from the string.
	 */
    public Color parseStringToColor(String color) {
        Scanner scanner = new Scanner(color);
        scanner.useDelimiter("\\D+");
        return new Color(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
    }
}
