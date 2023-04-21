package de.ios.framework.gui;

import java.awt.*;
import java.applet.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import de.ios.framework.db.*;
import de.ios.framework.basic.*;

/**
 * BackgroundContainer-Implelemtation for Applets.
 * Can be uses as Panel.
 * @see StandaloneApplet
 */
public class BackgroundApplet extends Applet implements BackgroundContainer {

    protected static String CLASS = "BackgroundApplet";

    /**
   * Some Standardparameters, BGCOLOR and BACKGROUND are used by the Applet itself,
   * the DB_* stuff is available to the childs (protected) / How to get the DB-Parameters:
   * user = getOptionalParameter(DB_USER, userDefault); ... 
   */
    public static final String BACKGROUND_COLOR = BackgroundFrame.BACKGROUND_COLOR;

    public static final String BACKGROUND_IMAGE = BackgroundFrame.BACKGROUND_IMAGE;

    public static final String DATABASE = BackgroundFrame.DATABASE;

    public static final String DB_USER = BackgroundFrame.DB_USER;

    public static final String DB_PASSWORD = BackgroundFrame.DB_PASSWORD;

    public static final String DB_OWNER = BackgroundFrame.DB_OWNER;

    public static final String SMTPHOST = BackgroundFrame.SMTPHOST;

    public static final String SMTPPORT = BackgroundFrame.SMTPPORT;

    /**
   * The Constructor.
   */
    public BackgroundApplet() {
        super();
        Parameters.registerApplet(this);
    }

    /**
   * Get the name of the Container. Overloaded in derivations.
   * Depricated. Use View.getViewName().
   */
    public String getContainerName() {
        return "Applet";
    }

    /**
   * init()
   */
    public void init() {
        super.init();
        loadDefaults();
    }

    /**
   * Set the Color and Background from the Applet's Parameters (doesn't work inside the Constructor!)
   */
    public void loadDefaults() {
        try {
            String BGColor = Parameters.getParameter((String) BACKGROUND_COLOR);
            String BGImage = Parameters.getParameter((String) BACKGROUND_IMAGE);
            if (BGColor != null) {
                try {
                    setBackground(new Color(Integer.parseInt(BGColor, 16)));
                } catch (NumberFormatException e) {
                    System.err.println(CLASS + ": Invalid BackgroundColor (" + BGColor + ")!");
                    e.printStackTrace();
                }
            }
            if (BGImage != null) {
                setBackgroundParameters(true, 0, 0, true);
                try {
                    setBackgroundImage(new URL(BGImage));
                } catch (MalformedURLException e) {
                    System.err.println(CLASS + ": Invalid BackgroundURL (" + BGImage + ")!");
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException e) {
        }
    }

    /**
   * Information about the additional Parameters supported by this Class
   */
    public String[][] getParameterInfo() {
        String[][] npi, pi = super.getParameterInfo();
        String[] mypi = { BACKGROUND_IMAGE, "String", "URL to an Image used as repeated Background", BACKGROUND_COLOR, "HexString", "Hex-Value of the BackGroundColor to be set (without any '0x', '$' or 'H')", DATABASE, "String", "String to be used for connecting to a Database", DB_USER, "String", "Username to be used for connecting to a Database", DB_PASSWORD, "String", "Password to be used for connecting to a Database", DB_OWNER, "String", "Owner of the Database-Tables (usage of DataBaseConnect.verifyOwner strongly recommanded!)", SMTPHOST, "String", "String to be used for defining a SMTP-Host for sending email", SMTPPORT, "String", "String to be used for defining the port of a SMTP-Host for sending email" };
        int s = 0;
        if (pi != null) s = pi.length;
        npi = new String[s + 1][];
        System.arraycopy(pi, 0, npi, 0, s);
        npi[s] = mypi;
        return npi;
    }

    /**
   * Get optional String-Parameter if defined
   */
    public String getOptionalParameter(String name, String value) {
        return Parameters.getOptionalParameter(name, value);
    }

    /**
   * Show the BackgroundImage
   */
    public void paint(Graphics g) {
        int xPos, yPos, xRepeat, yRepeat, width, height, imageHeight, imageWidth;
        Rectangle r = bounds();
        Point p = location();
        if (backgroundImage != null) {
            xRepeat = 1;
            yRepeat = 1;
            imageHeight = backgroundImage.getHeight(this);
            imageWidth = backgroundImage.getWidth(this);
            if (backgroundAutoOffset || backgroundAutoFill) {
                if ((imageHeight == -1) || (imageWidth == -1)) System.err.println(CLASS + ": Die Image-Dimensions sind noch nicht verfuegbar (Das Image wird vermutlich noch geladen)!"); else {
                    if (backgroundAutoOffset) {
                        backgroundXOffset = ((r.x / imageWidth) * imageWidth) - r.x;
                        backgroundYOffset = ((r.y / imageHeight) * imageHeight) - r.y;
                    }
                    xRepeat = (r.width + imageWidth - (backgroundXOffset + 1)) / imageWidth;
                    yRepeat = (r.height + imageHeight - (backgroundYOffset + 1)) / imageHeight;
                }
            }
            for (int xr = 0; xr < xRepeat; xr++) for (int yr = 0; yr < yRepeat; yr++) if (backgroundAutoFill || (xr == 0) || (yr == 0) || (xr == (xRepeat - 1)) || (yr == (yRepeat - 1))) g.drawImage(backgroundImage, backgroundXOffset + (xr * imageWidth), backgroundYOffset + (yr * imageHeight), this);
        }
        try {
            paintComponents(g);
        } catch (NullPointerException ne) {
            Debug.printException(this, ne);
        }
    }

    /**
   * Focus-Handling
   */
    protected Component lastFocus = null;

    protected Vector focusOrder = null;

    /**
   * Get the last Component with got focus.
   */
    public Component getFocus() {
        return lastFocus;
    }

    /**
   * Set Focus-order.
   * @param v Vector of Components.
   */
    public void setFocusOrder(Vector v) {
        focusOrder = v;
    }

    /**
   * Component.handleEvent.
   */
    public boolean handleEvent(Event e) {
        if (e.id == Event.GOT_FOCUS) lastFocus = (Component) e.target;
        if (e.id == Event.KEY_PRESS) {
            if (e.key == 9) {
                if (!(getParent() instanceof BackgroundContainer)) if (FocusTool.setNextFocus(this, getFocus(), focusOrder)) return true;
            }
        }
        return super.handleEvent(e);
    }

    /**
   * Define the Image's URL to be used as the Bachground
   */
    public BackgroundContainer setBackgroundImage(URL url) {
        return setBackgroundImage(getImage(url));
    }

    /**
   * Define the Image to be used as the Background
   */
    public synchronized BackgroundContainer setBackgroundImage(Image image) {
        backgroundImage = image;
        if (image != null) {
            MediaTracker m = new MediaTracker(this);
            m.addImage(backgroundImage, 0);
            try {
                m.waitForID(0);
            } catch (InterruptedException e) {
                System.err.println(CLASS + ": Loading of the BackgroundImage was interrupted!");
            }
            if (m.isErrorID(0)) {
                System.err.println(CLASS + ": Could not load the BackgroundImage, errors: " + m.getErrorsID(0));
                backgroundImage = null;
            }
        }
        return this;
    }

    /**
   * How to display the BackgroundImage
   */
    public synchronized BackgroundContainer setBackgroundParameters(boolean autoFill, int xOffset, int yOffset, boolean autoOffset) {
        backgroundXOffset = xOffset;
        backgroundYOffset = yOffset;
        backgroundAutoFill = autoFill;
        backgroundAutoOffset = autoOffset;
        return this;
    }

    /**
   * Get the Applet's BachgroundImage (Image!)
   */
    public Image getBackgroundImage() {
        return backgroundImage;
    }

    /**
   * Support for BackgroundCards 
   */
    public BackgroundCards getCardManager() {
        Container c = getParent();
        while (c != null && !(c instanceof BackgroundCards)) c = c.getParent();
        return (BackgroundCards) c;
    }

    protected Image backgroundImage = null;

    protected boolean backgroundAutoFill = false, backgroundAutoOffset = false;

    protected int backgroundXOffset = 0, backgroundYOffset = 0;
}
