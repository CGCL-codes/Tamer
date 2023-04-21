package com.bitmovers.maui.components;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import com.bitmovers.utilities.StringParser;
import com.bitmovers.maui.engine.ServerConfigurationManager;
import com.bitmovers.maui.engine.wmlcompositor.WMLCompositor;
import com.bitmovers.maui.engine.resourcemanager.ResourceManager;
import com.bitmovers.maui.engine.resourcemanager.ResourceNotFoundException;
import com.bitmovers.maui.MauiApplication;
import com.bitmovers.maui.engine.AuthorizationManager;

/** This is the component which describes the "desktop" within which a
  * <code>MauiApplication</code>. It can be used for defining various desktop like
  * attributes, like background color, text color, etc.  <code>MDesktop</code> can also
  * be used to wrap html code around the <code>MauiApplication</code>.
  *
  */
public class MDesktop {

    private static MDesktop desktop;

    private String backgroundColor;

    private String textColor;

    private String linkColor;

    private String vlinkColor;

    private String alinkColor;

    private String[] preamble = new String[2];

    private String[] postamble = new String[2];

    private MauiApplication mauiApplication;

    private static boolean initDone = false;

    private boolean ambleSet = false;

    private String backgroundImage;

    private AuthorizationManager am = AuthorizationManager.getInstance();

    /** Returns a string in hex representing a color from the Maui Server properties.
		* The method checks the format of the string, and returns it if valid, else returns
		* the aDefault string which is know to be valid. A valid string in hex representing a color
		* must be in a format '<code>#H1H2H3</code>' where where Hn is a two-digit hex number.
		*
		* <p>
  	*
 		* <pre>
 		* Example:<p>
	  *
	  * String bkgColor = testColor (theSCM, 
	  *                              theSCM.MAUI_DESKTOP_BACKGROUND_COLOR, 
	  *                              theSCM.MAUI_DESKTOP_BACKGROUND_COLOR_VALUE);
		*
	  * desktop.setBackgroundColor (bkgColor);
		*
		*</pre>
		*
		* The above example sets the desktop background color using the Maui properties file.
		* If the value of <code>theSCM.MAUI_DESKTOP_BACKGROUND_COLOR</code> is valid, then it
		* is returned, else the value of <code>theSCM.MAUI_DESKTOP_BACKGROUND_COLOR_VALUE</code> is
		* returned instead.
		* 
		*
	  * @param aSCM     Instance of <code>ServerConfigurationManager</code>.
	  *
	  * @param aKey     The property value to be returned if valid.
	  *
	  * @param aDefault The property value to be return if <code>aKey</code> is invalid.
	  *  
	  *	@return Valid string in hex representing a color from Maui properties. 
	  *
	  
	  */
    public static String testColor(ServerConfigurationManager aSCM, String aKey, String aDefault) {
        return testColor(aSCM.getProperty(aKey), aDefault);
    }

    /** Returns a string in hex representing a color if valid, else returns the default string.
		* A valid string in hex representing a color must be in a format '<code>#H1H2H3</code>' 
		* where where Hn is a two-digit hex number.
		*
		* @param aColor   The string to be returned if valid.
		* 
		* @param aDefault The string to be returned if <code>aColor</code> is invalid.
		*
		* @return Valid string in hex representing a color.
	  */
    public static String testColor(String aColor, String aDefault) {
        String retVal = aColor;
        try {
            Integer.parseInt((retVal.startsWith("#") ? retVal.substring(1) : retVal), 16);
        } catch (NumberFormatException e) {
            System.err.println("Bad color code of " + retVal + ".  Using default color of " + aDefault);
            retVal = aDefault;
        }
        return retVal;
    }

    /** Returns a <code>Color</code> object converting a given string in hex representing a color 
		* (e.g. '<code>#336699</code>'). This method converts the hex to decimal and returns a Color object. 
		* The string must be 7 characters long and must be in the format of  '<code>#H1H2H3</code>'
		* where Hn is a two-digit hex number. If any other argument is passed, an <code>IllegalArgumentException</code>
	  * is thrown.
	  *
	  * @param hexString The string to be converted into Color object.
	  *
	  * @return a Color object converting a given string in hex representing.
	  *
	  * @exception IllegalArgumentException If invalid hex color.
	  */
    public static Color getColorFromHexString(String hexString) throws IllegalArgumentException {
        if (!hexString.startsWith("#") && (hexString.length() != 7)) {
            throw new IllegalArgumentException("'" + hexString + "' is not a valid hex colour string. The string must 7 characters long and must be in the format of '#H1H2H3' where Hn is a two-digit hex number.");
        }
        try {
            String hexRed = hexString.substring(1, 3);
            String hexGreen = hexString.substring(3, 5);
            String hexBlue = hexString.substring(5, 7);
            int red = Integer.parseInt(hexRed, 16);
            int green = Integer.parseInt(hexGreen, 16);
            int blue = Integer.parseInt(hexBlue, 16);
            return new Color(red, green, blue);
        } catch (Exception exception) {
            throw new IllegalArgumentException("'" + hexString + "' is not a valid hex colour string. The string must 7 characters long and must be in the format of '#H1H2H3' where Hn is a two-digit hex number. (" + exception.getMessage() + ")");
        }
    }

    /** Returns a string hex representation given a <code>Color</code> object. Format of the string is
	  * based on the standard used by <code>HTML</code>. (e.g. '<code>#H1H2H3</code>', 
	  * where <code>Hn</code> is a hex number, 00 <= Hn <= FF).
	  *
	  * @param colour A <code>Color</code> object that needs to be converted into a <code>String</code>.
	  *
	  * @return A string hex representation given a <code>Color</code> object
	  */
    public static String getHexStringFromColor(Color colour) {
        String red = Integer.toHexString(colour.getRed());
        String green = Integer.toHexString(colour.getGreen());
        String blue = Integer.toHexString(colour.getBlue());
        return "#" + zeroLead(red) + zeroLead(green) + zeroLead(blue);
    }

    /** Sets initial settings for the <code>ServerConfigurationManager</code>.
	  *
	  */
    public static void initialize() {
        if (!initDone) {
            ServerConfigurationManager theSCM = ServerConfigurationManager.getInstance();
            String theDesktopClass = theSCM.getProperty(theSCM.MAUI_DESKTOP_CLASS);
            if (!theDesktopClass.equals(theSCM.MAUI_DESKTOP_CLASS_VALUE)) {
                try {
                    desktop = (MDesktop) Class.forName(theDesktopClass).newInstance();
                } catch (Exception e) {
                    System.err.println("[MDesktop] Couldn't create MDesktop " + theDesktopClass + " because: " + e + ".  Using default MDesktop");
                }
            }
            if (desktop == null) {
                desktop = new MDesktop();
            }
            desktop.setBackgroundColor(testColor(theSCM, theSCM.MAUI_DESKTOP_BACKGROUND_COLOR, theSCM.MAUI_DESKTOP_BACKGROUND_COLOR_VALUE));
            desktop.setTextColor(testColor(theSCM, theSCM.MAUI_DESKTOP_TEXT_COLOR, theSCM.MAUI_DESKTOP_TEXT_COLOR_VALUE));
            desktop.setLinkColor(testColor(theSCM, theSCM.MAUI_DESKTOP_LINK_COLOR, theSCM.MAUI_DESKTOP_LINK_COLOR_VALUE));
            desktop.setPreamble(theSCM.getProperty(theSCM.MAUI_DESKTOP_PREAMBLE));
            desktop.setPostamble(theSCM.getProperty(theSCM.MAUI_DESKTOP_POSTAMBLE));
            desktop.setBackgroundImage(theSCM.getProperty(theSCM.MAUI_DESKTOP_BACKGROUND_IMAGE));
            initDone = true;
        }
    }

    /** Returns an instance of <code>MDesktop</code> having called <code>initialize()</code>.
	  *
	  */
    public static final MDesktop getInstance() {
        if (!initDone) {
            initialize();
        }
        return desktop;
    }

    /** Appends '0' character to the string if the length of the string is less than two.
		* 
		* @param aNumber A string representing a number that may require a leading zero.
		*
		*	@return A string with a leading zero '0' character.
	  *
	  */
    protected static String zeroLead(String aNumber) {
        String retVal = aNumber;
        if (retVal.length() < 2) {
            retVal = "0" + retVal;
        }
        return retVal;
    }

    /** Adjusts the string color hex representation to conform to the <code>HTML</code> safe colors. 
	  *	The methods checks the color string in hex and makes any adjustments so as the color
	  * stays within the bounds of '<code>#000000</code>' to '<code>#FFFFFF</code>'.
	  *
	  * @param aColor      A color string that may need adjusting.
	  *
	  * @param aAdjustment Factor to adjust by.
	  *
	  * @return A string that has been adjusted as <code>HTML</code> safe color. 
	  */
    public static String adjustColor(String aColor, int aAdjustment) {
        String theColor = aColor.substring(1);
        StringBuffer retVal = new StringBuffer(6);
        for (int i = 0; i < 3; i++) {
            String theColorToAdjust = theColor.substring(i * 2, i * 2 + 2);
            int theIntColor = Integer.parseInt(theColorToAdjust, 16);
            theIntColor += aAdjustment;
            if (theIntColor < 0) {
                theIntColor = 0;
            } else if (theIntColor > 255) {
                theIntColor = 255;
            }
            retVal.append(zeroLead(Integer.toHexString(theIntColor)));
        }
        return retVal.toString();
    }

    /** Sets the background of the <code>MDesktop</code> instance. The method validates
		* the input and sets the background either to the new valid string color, or default
		* color otherwise.
		*
		* @param aColor A string color hex representation that will set the background 
		*		            color of the <code>MDesktop</code>.
	  *
	  */
    private void setBackgroundColor(String aColor) {
        backgroundColor = testColor(aColor, ServerConfigurationManager.MAUI_DESKTOP_BACKGROUND_COLOR_VALUE);
    }

    /** Returns a string hex representation background color of the <code>MDesktop</code> instance.
	  * The format of the string is '<code>#H1H2H3</code>'.
	  *
	  * @return A string color hex representation.
	  */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /** Sets the text color of the <code>MDesktop</code> instance. The method validates
		* the input and sets the text either to the new valid string color, or default
		* color otherwise.
		*
		* @param aColor A string color hex representation that will set the text 
		*		            color of the <code>MDesktop</code>.
	  *
	  */
    private void setTextColor(String aColor) {
        textColor = testColor(aColor, ServerConfigurationManager.MAUI_DESKTOP_TEXT_COLOR_VALUE);
    }

    /** Returns a string hex representation text color of the <code>MDesktop</code> instance.
	  * The format of the string is '<code>#H1H2H3</code>'.
	  *
	  * @return A string color hex representation.
	  */
    public String getTextColor() {
        return textColor;
    }

    /** Sets the hyperlink color of the <code>MDesktop</code> instance. The method adjusts 
		* and validates the color. The hyperlink is set to either the new valid string color, or 
		* default color otherwise. 
		*
		* @param aColor A string color hex representation that will set the hyperlink 
		*		            color of the <code>MDesktop</code>.
	  *
	  */
    private void setLinkColor(String aColor) {
        linkColor = testColor(aColor, ServerConfigurationManager.MAUI_DESKTOP_LINK_COLOR_VALUE);
        vlinkColor = "#" + adjustColor(linkColor, -33);
        alinkColor = "#" + adjustColor(linkColor, +33);
    }

    /** Returns a string hex representation hyperlink color of the <code>MDesktop</code> instance.
	  * The format of the string is '<code>#H1H2H3</code>'.
	  *
	  * @return A string color hex representation.
	  */
    public String getLinkColor() {
        return linkColor;
    }

    /** Returns the file content as <code>String</code>. The method enables to open
		* a file copy its content to a buffer and return the buffer as a string after
		* closing the file. The types of files that will be read will typically be pre and post
		* amble files. This files contain the <code>HTML</code> or <code>WML</code>that will be 
		* needed to wrap the <code>MauiApplication</code> and convert the MDestop from plain 
		* background to content filled and active 'desktop'.
		*
		* @param aFile valid instance of <code>File</code>.
		*
		* @return Content of the instance of the <code>File</code>.
	  *
	  */
    private static String readFile(File aFile) {
        StringBuffer retVal = new StringBuffer(2000);
        try {
            BufferedReader theInput = new BufferedReader(new FileReader(aFile));
            char[] theData = new char[2000];
            int theCount = 0;
            while ((theCount = theInput.read(theData, 0, 2000)) > 0) {
                retVal.append(theData, 0, theCount);
            }
            theInput.close();
        } catch (IOException e) {
            System.err.println("[MDesktop] " + e);
        }
        return retVal.toString();
    }

    /** This needs documentation.
		* 
	  *
	  */
    public static String[] setAmble(String aFile, String aDefaultFile, String aLocation) {
        ResourceManager theRM = ResourceManager.getInstance();
        String[] theExtensions = new String[] { ".wml", ".html" };
        String theFileName = new String(aFile);
        int theIndex = 0;
        if ((theIndex = aFile.lastIndexOf(".")) != -1) {
            theFileName = theFileName.substring(0, theIndex);
        }
        String[] retVal = new String[theExtensions.length];
        for (int i = 0; i < theExtensions.length; i++) {
            try {
                retVal[i] = theRM.getResourceString(theFileName + theExtensions[i]);
            } catch (ResourceNotFoundException e) {
                File theLocation = new File((aLocation == null ? "" : aLocation));
                File theFile = new File(aLocation, theFileName + theExtensions[i]);
                if (!theFile.exists()) {
                    if (aDefaultFile != null && !(aFile.equals(aDefaultFile))) {
                        System.err.println("Preamble or Postamble file " + theFileName + theExtensions[i] + " wasn't found.  Using default");
                        String theDefaultName = aDefaultFile + theExtensions[i];
                        retVal[i] = setAmble(theDefaultName, theDefaultName, null)[0];
                    } else {
                        retVal[i] = "";
                    }
                } else {
                    retVal[i] = readFile(theFile);
                }
            }
        }
        return retVal;
    }

    /** Sets the pre-amble for the <code>MDesktop</code> instance. This wraps the <code>MauiApplication</code>
		* displaying a content filled and active </code>MDesktop</code> instance.
		* 
		* @param aPreamble The name of the file containing the pre-amble content.
	  *
	  */
    private void setPreamble(String aPreamble) {
        preamble = setAmble(aPreamble, ServerConfigurationManager.MAUI_DESKTOP_PREAMBLE_VALUE, null);
    }

    /** Sets the post-amble for the <code>MDesktop</code> instance. This wraps the <code>MauiApplication</code>
		* displaying a content filled and active </code>MDesktop</code> instance.
		* 
		* @param aPostamble The name of the file containing the post-amble content.
	  *
	  */
    private void setPostamble(String aPostamble) {
        postamble = setAmble(aPostamble, ServerConfigurationManager.MAUI_DESKTOP_POSTAMBLE_VALUE, null);
    }

    /** Sets the image background for the <code>MDesktop</code> instance.  
		*
		* @param aBackgroundImage Full path of the image to use as a background.
	  *
	  */
    private void setBackgroundImage(String aBackgroundImage) {
        backgroundImage = aBackgroundImage;
    }

    /** This method needs to be documented.
	  *
	  * @invisible
	  * 
	  */
    public void fillParserValues(StringParser aParser, boolean aWml) {
        String theBackgroundImage = mauiApplication.getBackgroundImage();
        if (theBackgroundImage == null) {
            theBackgroundImage = backgroundImage;
        }
        if (theBackgroundImage != null) {
            String theBackground = (theBackgroundImage.indexOf("://") != -1 ? backgroundImage : mauiApplication.getServletURL() + "?getImage=true&path=" + theBackgroundImage);
            aParser.setVariable("background", "background=\"" + theBackground + "\"");
        }
        String theBackgroundColor = mauiApplication.getBackgroundColor();
        if (theBackgroundColor == null) {
            theBackgroundColor = backgroundColor;
        }
        aParser.setVariable("backgroundColor", theBackgroundColor);
        aParser.setVariable("textColor", textColor);
        aParser.setVariable("linkColor", linkColor);
        aParser.setVariable("vlinkColor", vlinkColor);
        aParser.setVariable("alinkColor", alinkColor);
        if (am.isAuthorized(am.AUTHORIZATION_CLIENTBRANDING)) {
            aParser.setVariable("desktopPreamble", preamble[aWml ? 0 : 1]);
            aParser.setVariable("desktopPostamble", postamble[aWml ? 0 : 1]);
        }
    }

    /** Sets instance of <code>MauiApplication</code>.
		*
		* @param aMauiApplication instance of <code>MauiApplication</code> that needs to be set.
	  *
	  */
    public void setApplication(MauiApplication aMauiApplication) {
        mauiApplication = aMauiApplication;
    }

    /** Returns <code>MauiApplication</code>
	  *
	  */
    public MauiApplication getApplication() {
        return mauiApplication;
    }
}
