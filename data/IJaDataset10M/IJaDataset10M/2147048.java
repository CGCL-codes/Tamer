package commonapp.gui;

import common.IO;
import common.StringUtils;
import common.log.Log;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipFile;
import javax.swing.ImageIcon;

/**
   This class provides a global access instance (@link #main}) that
   supports the methods used to obtain icons.  The ImageIcon objects, both
   unsized and resized, are maintained in a HashMap cache.  A "bad" icon
   (ourBadIcon) is hard-coded as from a byte sequence and is returned when
   the requested icon is not available.
*/
public final class IconFactory {

    public static final IconFactory main = new IconFactory();

    /** Don't resize the icon. */
    public static final IconSize SIZE_NO_RESIZE = new IconSize(-1, -1);

    /** Label focus icon. */
    public static final IconSize SIZE_FOCUS = new IconSize(4, 8);

    /** Menu icon. */
    public static final IconSize SIZE_MENU = new IconSize(16, 16);

    /** Small icon. */
    public static final IconSize SIZE_CELL = new IconSize(10, 10);

    /** Tree node icon. */
    public static final IconSize SIZE_TREE = new IconSize(16, 16);

    /** Tab icon. */
    public static final IconSize SIZE_TAB = new IconSize(16, 16);

    /** Toolbar button icon. */
    public static final IconSize SIZE_TOOLBAR = new IconSize(16, 16);

    /** Button icon. */
    public static final IconSize SIZE_BUTTON = new IconSize(16, 16);

    /** Label icon. */
    public static final IconSize SIZE_LABEL = new IconSize(20, 20);

    /** Dialog icon. */
    public static final IconSize SIZE_DIALOG = new IconSize(48, 48);

    /** Throbber icon. */
    public static final IconSize SIZE_THROB = new IconSize(32, 32);

    /** Bad icon name. */
    public static final String BAD_NAME = "bad";

    /** JVM-based icons, prefix. */
    public static final String DEFAULT_NAME = "default";

    /** Warning icon. */
    public static final String DEFAULT_WARN = "defaultWarn";

    /** Error icon. */
    public static final String DEFAULT_ERROR = "defaultError";

    /** Help icon. */
    public static final String DEFAULT_HELP = "defaultInform";

    /** Confirm icon. */
    public static final String DEFAULT_CONFIRM = "defaultQuestion";

    /** Information icon. */
    public static final String DEFAULT_INFO = "defaultInform";

    /** About icon. */
    public static final String DEFAULT_ABOUT = "defaultAbout";

    private static ImageIcon ourBadIcon = null;

    private static double ourSizeFactor;

    /**
     Sets the global size scaling factor.

     @param theSizeFactor the size scaling factor.
  */
    public static void setSizeFactor(double theSizeFactor) {
        ourSizeFactor = theSizeFactor;
    }

    private HashMap<String, ImageIcon> myIconCache = new HashMap<String, ImageIcon>();

    private HashMap<String, String> myIconPaths = new HashMap<String, String>();

    private ArrayList<String> myZipNames = new ArrayList<String>();

    private ArrayList<ZipFile> myZipFiles = new ArrayList<ZipFile>();

    private ArrayList<String> myTargetNames = new ArrayList<String>();

    /**
     Constructs a new IconFactory.

     <p>Creates and adds ourBadIcon to the table.
  */
    private IconFactory() {
        if (ourBadIcon == null) {
            ourSizeFactor = 1.0;
            ourBadIcon = new ImageIcon(new byte[] { -1, -40, -1, -32, 0, 16, 74, 70, 73, 70, 0, 1, 1, 1, 0, 72, 0, 72, 0, 0, -1, -2, 0, 23, 67, 114, 101, 97, 116, 101, 100, 32, 119, 105, 116, 104, 32, 84, 104, 101, 32, 71, 73, 77, 80, -1, -37, 0, 67, 0, 8, 6, 6, 7, 6, 5, 8, 7, 7, 7, 9, 9, 8, 10, 12, 20, 13, 12, 11, 11, 12, 25, 18, 19, 15, 20, 29, 26, 31, 30, 29, 26, 28, 28, 32, 36, 46, 39, 32, 34, 44, 35, 28, 28, 40, 55, 41, 44, 48, 49, 52, 52, 52, 31, 39, 57, 61, 56, 50, 60, 46, 51, 52, 50, -1, -64, 0, 11, 8, 0, 10, 0, 10, 1, 1, 34, 0, -1, -60, 0, 21, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 3, -1, -60, 0, 36, 16, 0, 2, 1, 4, 1, 3, 5, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 17, 18, 6, 0, 33, 49, 7, 8, 34, 81, 113, 114, -1, -38, 0, 8, 1, 1, 0, 0, 63, 0, -105, -86, -42, 51, 71, -55, 110, 23, 90, -66, 95, 87, 31, 32, -82, 120, -106, -53, 107, -74, 67, 35, -69, -62, -123, 116, 66, 3, 124, 9, -104, 43, 3, -100, 102, 54, 101, 82, -57, 84, 124, -96, 122, -119, 45, -44, -49, 88, -102, 85, 52, 72, 102, 93, 66, -22, -8, 27, 12, 6, 96, 59, -25, -80, 102, -3, 62, 122, 47, -65, -41, -42, 71, -18, 87, -118, -47, -91, 92, -21, 74, -10, -87, 118, -124, 72, 66, 54, 68, -28, -27, 124, 119, 49, -58, 79, -16, -65, 67, -91, -114, -65, -1, -39 });
            ourBadIcon.setDescription(BAD_NAME);
        }
        myIconCache.put(BAD_NAME, ourBadIcon);
    }

    /**
     Adds an icon archive zip file to the list of archives.  If the archive
     includes a file named "icon.map", this method reads the file and adds the
     name=value pairs as icon maps.

     @param theFile the zip archive file.
  */
    public void loadIconMap(File theFile) {
        if ((theFile != null) && theFile.exists()) {
            try {
                ZipFile zip = new ZipFile(theFile);
                String[] lines = IO.readLines(zip, "icon.map");
                if (lines != null) {
                    Log.main.println(Log.VERBOSE, "Loading icon map from " + theFile);
                    for (String line : lines) {
                        line = line.trim();
                        int sharp = line.indexOf("#");
                        if (sharp == 0) {
                            continue;
                        } else if (sharp > 0) {
                            line = line.substring(0, sharp).trim();
                        }
                        if (line.equals("")) {
                            continue;
                        }
                        String[] args = StringUtils.parseString(line, '=');
                        if (args.length == 2) {
                            addIconMap(args[0].trim(), args[1].trim());
                        }
                    }
                }
                addZipFile(zip);
            } catch (Exception e) {
                Log.main.println(Log.FAULT, "Can't open image archive: " + theFile.getAbsolutePath() + ", " + e.getMessage());
            }
        }
    }

    /**
     Returns an un-scaled icon for the image located at the given path.

     @param thePath a String denoting the relative path for an image file.

     @return a new icon for the given image.
  */
    public ImageIcon getIcon(String thePath) {
        return getIcon(thePath, SIZE_NO_RESIZE);
    }

    /**
     Returns a scaled icon for the image located at the given path.

     @param thePath a String denoting the relative path for an image file.

     @param theSize a icon size specification.

     @return a new scaled icon for the given image.
  */
    public ImageIcon getIcon(String thePath, IconSize theSize) {
        return getIcon(thePath, theSize, true);
    }

    /**
     Returns a scaled icon for the image located at the given path.

     @param thePath a String denoting the relative path for an image file.

     @param theSize a icon size specification.

     @param theMessageFlag display an error message if the icon is not found.
     If false, a null is returned if the icon is not found.  This is used by
     the throbber to get an arbitrary number of icons.

     @return a new scaled icon for the given image.  If theMessageFlag is true
     and the icon is not found, a standard "not-found" icon is returned.  If
     theMessageFlag if false and the icon is not found, the return value is
     null.
  */
    public ImageIcon getIcon(String thePath, IconSize theSize, boolean theMessageFlag) {
        ImageIcon icon = null;
        String path = thePath;
        try {
            if ((path != null) && (theSize != null)) {
                String key = path + "." + theSize.myKey;
                icon = myIconCache.get(key);
                if (icon == null) {
                    icon = getRawIcon(path);
                    if (theSize.myResize) {
                        icon = new ImageIcon(icon.getImage().getScaledInstance(theSize.myWidth, theSize.myHeight, Image.SCALE_SMOOTH));
                    }
                    if (icon == null) {
                        throw new Exception("Null icon");
                    }
                    myIconCache.put(key, icon);
                }
            }
        } catch (Exception e) {
            if (theMessageFlag) {
                Log.main.println(Log.FAULT, "icon not found: " + thePath + ", " + e.getMessage());
                icon = ourBadIcon;
                if ((thePath != null) && (theSize != null)) {
                    myIconCache.put(thePath + "." + theSize.myKey, icon);
                } else if (thePath != null) {
                    myIconCache.put(thePath, icon);
                }
            } else {
                icon = null;
            }
        }
        return icon;
    }

    /**
     Returns an un-scaled icon from the java jars.

     @param theName a String for the name for an icon file in the java jars.

     @return a new un-scaled default icon for the given name.
  */
    public ImageIcon getDefaultIcon(String theName) {
        return new ImageIcon(ClassLoader.getSystemResource("javax/swing/plaf/metal/icons/" + theName.substring(DEFAULT_NAME.length()) + ".gif"));
    }

    /**
     Returns a new copy of the given icon.

     @param theIcon an ImageIcon to make a copy of.

     @return a new icon copied from the given icon.
  */
    public ImageIcon getIcon(ImageIcon theIcon) {
        return getIcon(theIcon, SIZE_NO_RESIZE);
    }

    /**
     Returns a new icon that is a scaled copy of the given icon.

     @param theIcon an ImageIcon to make a scaled copy of.

     @param theSize a icon size specification.

     @return a new scaled icon copied from the given icon.
  */
    public ImageIcon getIcon(ImageIcon theIcon, IconSize theSize) {
        ImageIcon icon = null;
        if (!theSize.myResize) {
            icon = new ImageIcon(theIcon.getImage());
        } else {
            icon = new ImageIcon(theIcon.getImage().getScaledInstance(theSize.myWidth, theSize.myHeight, Image.SCALE_SMOOTH));
        }
        return icon;
    }

    /**
     Returns an icon size object for the specified width and height.

     @param theWidth the icon width.

     @param theHeight the icon height.

     @return an icon size specification object.
  */
    public IconSize size(int theWidth, int theHeight) {
        return new IconSize(theWidth, theHeight);
    }

    /**
     Adds a name = value pair to the icon path map.

     @param theName the icon path name.

     @param theValue the icon map.
  */
    private void addIconMap(String theName, String theValue) {
        if ((theName != null) && (theValue != null)) {
            myIconPaths.put(theName, theValue);
        }
    }

    /**
     Retrieves a (possible) compound, fully resolved icon file path for the
     given (generic) icon name.

     @param theIconName a String for the (generic) icon name.

     @return String the icon file path.
  */
    private ArrayList<String> getFullIconPath(String theIconName) {
        ArrayList<String> names = new ArrayList<String>();
        synchronized (myTargetNames) {
            myTargetNames.clear();
            getFullIconPath(names, theIconName);
            myTargetNames.notifyAll();
        }
        return names;
    }

    /**
     Retrieves a (possible) compound, fully resolved icon file path for the
     given (generic) icon name.

     @param theIconName a String for the (generic) icon name.
  */
    private void getFullIconPath(ArrayList<String> theNames, String theIconName) {
        if (myTargetNames.contains(theIconName)) {
            Log.main.println(Log.FAULT, "Recursive icon path reference: " + theIconName);
        } else {
            myTargetNames.add(theIconName);
            String[] paths = StringUtils.parseString(theIconName, ',');
            for (String name : paths) {
                name = name.trim();
                if (name.equals("")) {
                    continue;
                }
                String target = myIconPaths.get(name);
                if (target == null) {
                    theNames.add(name);
                } else {
                    getFullIconPath(theNames, target);
                }
            }
        }
    }

    private ImageIcon getRawIcon(String theName) throws Exception {
        ImageIcon icon = myIconCache.get(theName);
        if (icon == null) {
            String name = theName;
            ArrayList<String> names = getFullIconPath(theName);
            if (names.size() == 0) {
                throw new Exception("Invalid icon path: " + theName);
            } else if (names.size() > 1) {
                icon = getCompositeIcon(names);
            } else {
                name = names.get(0);
                if (name.startsWith(DEFAULT_NAME)) {
                    icon = getDefaultIcon(name);
                }
                if (icon == null) {
                    icon = getZipIcon(name);
                }
            }
            if (icon == null) {
                throw new Exception("Null icon");
            }
            myIconCache.put(theName, icon);
            if (!name.equals(theName)) {
                myIconCache.put(name, icon);
            }
        }
        return icon;
    }

    /**
     Finds the icon from the one of the zip image archives.

     @param theName the zip archive file name.

     @return the ImageIcon associated with the name, or null if the
     icon is not found.

     @throw Exception for a zip file i/o error or a image conversion
     error.
  */
    private ImageIcon getZipIcon(String theName) throws Exception {
        ImageIcon icon = null;
        try {
            for (ZipFile zipFile : myZipFiles) {
                byte[] bytes = IO.readBytes(zipFile, theName);
                if (bytes != null) {
                    icon = new ImageIcon(bytes);
                    break;
                }
            }
        } catch (Exception e) {
            throw new Exception("Can't load icon: " + theName + ", " + e.getMessage());
        }
        return icon;
    }

    /**
     Combines two or more icons to create a composite icon.

     @param theNames the list of icon names to be combined.  All names need
     not be completely resolved.

     @return the composite icon or null on error.
  */
    private ImageIcon getCompositeIcon(ArrayList<String> theNames) throws Exception {
        ImageIcon icon = null;
        BufferedImage finalImage = null;
        Graphics2D finalGraphics = null;
        ImageIcon currentIcon;
        for (String path : theNames) {
            if ((path == null) || path.equals("")) {
                continue;
            }
            currentIcon = getRawIcon(path);
            if (currentIcon == null) {
                continue;
            }
            if (finalImage == null) {
                int width = currentIcon.getIconWidth();
                int height = currentIcon.getIconHeight();
                finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
                finalGraphics = finalImage.createGraphics();
                finalGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
            currentIcon.paintIcon(null, finalGraphics, 0, 0);
        }
        if (finalImage != null) {
            icon = new ImageIcon(finalImage);
        }
        return icon;
    }

    /**
     Adds the specified zip file to the list of zip files if the name is unique.

     @param theFile the zip file to be added to the list.
  */
    private void addZipFile(ZipFile theFile) {
        if (theFile != null) {
            String name = theFile.getName().toUpperCase();
            if (!myZipNames.contains(name)) {
                myZipNames.add(name);
                myZipFiles.add(theFile);
            }
        }
    }

    /**
     A static class to encapsulate icon sizes.
  */
    public static class IconSize {

        private int myWidth = -1;

        private int myHeight = -1;

        private String myKey = "";

        private boolean myResize = true;

        /**
       Constructs a new IconSize.

       @param theWidth the width.

       @param theHeight the height.
    */
        private IconSize(int theWidth, int theHeight) {
            myWidth = (int) ((theWidth * ourSizeFactor) + 0.5);
            myHeight = (int) ((theHeight * ourSizeFactor) + 0.5);
            if ((myWidth < 1) || (myHeight < 1)) {
                myResize = false;
                myWidth = -1;
                myHeight = -1;
            }
            myKey = String.valueOf(myWidth) + "." + String.valueOf(myHeight);
        }

        public int getWidth() {
            return myWidth;
        }

        public int getHeight() {
            return myHeight;
        }
    }
}
