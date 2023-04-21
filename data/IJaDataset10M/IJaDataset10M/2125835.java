package com.javathis.utilities;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import java.util.zip.*;
import javax.swing.*;
import java.awt.font.*;
import javax.swing.table.*;

/**
 * A collection of utility methods.
 */
public final class JTUtilities {

    public static final ResourceBundle COMMON_RESOURCE_BUNDLE = ResourceBundle.getBundle("com/javathis/utilities/properties/common");

    static final ResourceBundle MAIN_RESOURCE_BUNDLE = ResourceBundle.getBundle("com/javathis/utilities/properties/JTUtilities");

    public static final String LNF_METAL = "javax.swing.plaf.metal.MetalLookAndFeel";

    public static final String LNF_MOTIF = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";

    public static final String LNF_WINDOWS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

    public static final String LNF_MAC = "com.sun.java.swing.plaf.mac.MacLookAndFeel";

    public static final String ELLIPSIS = "...";

    public static final int HEX_RADIX = 16;

    public static final int OCT_RADIX = 8;

    public static final int MOTIF_HEIGHT_OFFSET = 12;

    public static final int ZIP_NO_COMPRESSION = 0;

    public static final int ZIP_LOW_COMPRESSION = 3;

    public static final int ZIP_MEDIUM_COMPRESSION = 5;

    public static final int ZIP_HIGH_COMPRESSION = 7;

    public static final int ZIP_MAX_COMPRESSION = 9;

    public static final String NBSP = "&nbsp;";

    public static final String SPACE = " ";

    public static final String EMPTY = "";

    public static final String HTML_SPACE = "%20";

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static final String EXTENSION_SEPARATOR = ".";

    private static final int CASCADE_DISTANCE = 22;

    private static final boolean ZIP_DEFAULT_SAVE_PATH = false;

    private static final FontRenderContext DEAFAULT_FONT_RENDERER_CONTEXT = new FontRenderContext(null, false, true);

    private JTUtilities() {
    }

    /**
     * Cascades the provided target component relative from the from component.
     *
     * @param from
     * @param target
     */
    public static void cascadeRelativeFrom(Component from, Component target) {
        if (from != null && target != null) {
            Point p = from.getLocation();
            p.x += CASCADE_DISTANCE;
            p.y += CASCADE_DISTANCE;
            target.setLocation(p);
        }
    }

    /**
     * Centers the target component on the screen.
     *
     * @param target
     */
    public static void centerOnScreen(Component target) {
        if (target != null) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dialogSize = target.getSize();
            if (dialogSize.height > screenSize.height) dialogSize.height = screenSize.height;
            if (dialogSize.width > screenSize.width) dialogSize.width = screenSize.width;
            target.setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
        }
    }

    /**
     * Centers the target component relitive from the from component.  If the
     * from component is null will just center the target component on the
     * screen.
     *
     * @param from
     * @param target
     */
    public static void centerRelativeFrom(Component from, Component target) {
        if (target != null) {
            if (from != null) {
                Point fromComponentLocaton = from.getLocation();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension fromComponentSize = from.getSize();
                Dimension targetComponentSize = target.getSize();
                if (targetComponentSize.height > fromComponentSize.height) {
                    targetComponentSize.height = fromComponentSize.height;
                }
                if (targetComponentSize.width > fromComponentSize.width) {
                    targetComponentSize.width = fromComponentSize.width;
                }
                target.setLocation(fromComponentLocaton.x + ((fromComponentSize.width - targetComponentSize.width) / 2), fromComponentLocaton.y + ((fromComponentSize.height - targetComponentSize.height) / 2));
            } else centerOnScreen(target);
        }
    }

    /**
     * Cleans the provided dirty path of it drive letters and/or first backslash.
     * If nothing needs to be done it will return the same string, if the
     * provided dirty path is null then a null will be returned.
     * <p>
     * Examples:
     * <pre>
     *     'C:\java\bin\java.exe' becomes 'java\bin\java.exe'
     *     '\java\bin\java.exe'   becomes 'java\bin\java.exe'
     *     'java\bin\java.exe'    becomes 'java\bin\java.exe'
     * </pre>
     *
     * @param   dirtyPath
     * @return  String
     */
    public static String cleanPath(String dirtyPath) {
        String cleanPath = null;
        if (dirtyPath != null) {
            if (dirtyPath.length() > 3 && dirtyPath.charAt(1) == ':') cleanPath = dirtyPath.substring(3); else if (dirtyPath.startsWith(FILE_SEPARATOR)) cleanPath = dirtyPath.substring(1); else cleanPath = dirtyPath;
        }
        return cleanPath;
    }

    /**
     * Copies the source file to the destination file.  Returns a boolean that
     * represents if the copy fas successful or not.
     *
     * @param   soureFile
     * @param   destFile
     * @return  boolean
     */
    public static boolean copyFile(File soureFile, File destFile) {
        boolean copySuccess = false;
        if (soureFile != null && destFile != null && soureFile.exists()) {
            try {
                new File(destFile.getParent()).mkdirs();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destFile));
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(soureFile));
                for (int currentByte = in.read(); currentByte != -1; currentByte = in.read()) out.write(currentByte);
                in.close();
                out.close();
                copySuccess = true;
            } catch (Exception e) {
                copySuccess = false;
            }
        }
        return copySuccess;
    }

    /**
     * Replaces all HTML Space (<code>%20</code>) codes with a regular space.
     * This is a workaround for the current JDK 1.4.0 bug id# 4639610.
     * (new since 1.4.0)
     * <P>
     * When you call getFile() on a URL object it returns a String with %20 code
     * representing spaces.  Oh chorse File objects do not like this, so they
     * cannot find the files.  This is a clean universal solutions until they
     * fix it. (Either URL cannot return these codes, or a File object has to
     * understand them).
     *
     * @param   urlFileString
     * @return  String
     */
    public static String fixURLFileString(String urlFileString) {
        return urlFileString.replaceAll(HTML_SPACE, SPACE);
    }

    /**
     * Returns a {@link BufferedImage} refernced by the provided String representing
     * a complete path name, or class path.  Will return null if location or file
     * is invalid.
     *
     * @param   resourceLocation
     * @return  BufferedImage
     */
    public static BufferedImage getBufferedImage(String resourceLocation) {
        BufferedImage bImage = null;
        Image image = getImageIcon(resourceLocation).getImage();
        if (image != null) {
            bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            bImage.createGraphics().drawImage(image, 0, 0, null);
        }
        return bImage;
    }

    /**
     * Converts a Hex Color Triplet to a Color object.
     * <p>
     * Example:
     * <p>
     * The String <code>ff0000</code> would return the object <code>Color.red</code>.
     * <p>
     * Note: The String for the hex color triplet should be in hex pairs, and
     * total of 6 characters.  If an invalid or null String is provided a null
     * Color object will be returned.
     * @param   hexColorTriplet
     * @return  Color
     */
    public static Color getColor(String hexColorTriplet) {
        Color color = null;
        final int REQUIRED_LENGTH = 6;
        final int INVALID_VALUE = -1;
        if (hexColorTriplet != null && hexColorTriplet.length() == REQUIRED_LENGTH) {
            String redString = hexColorTriplet.substring(0, 2);
            String greenString = hexColorTriplet.substring(2, 4);
            String blueString = hexColorTriplet.substring(4, 6);
            int redInt = INVALID_VALUE;
            int greenInt = INVALID_VALUE;
            int blueInt = INVALID_VALUE;
            try {
                redInt = Integer.parseInt(redString, HEX_RADIX);
                greenInt = Integer.parseInt(greenString, HEX_RADIX);
                blueInt = Integer.parseInt(blueString, HEX_RADIX);
            } catch (NumberFormatException e) {
            }
            if (redInt != INVALID_VALUE && greenInt != INVALID_VALUE && blueInt != INVALID_VALUE) color = new Color(redInt, greenInt, blueInt);
        }
        return color;
    }

    /**
     * Returns the width of the column based on the the width of the
     * header renderer, and each cell for the column which ever is greater.
     * <p>
     * If the JTable has a custom renderer for the cell header then the width
     * of this will be used.  Otherwise the width of the String contained in the
     * header will be used.
     * <p>
     * It iterates through each row of the column and finds the largest
     * preferred width of the cells, cell renderer.
     * <p>
     * After that it returns the largest width.
     * <p>
     * This is useful for determining the width required to display the logest
     * cell in that column, or if they are all shorter than the header then the
     * the return value will allow for displaying the complete header.
     *
     * @param   tableColumn
     * @return  int
     */
    public static int getColumnWidth(JTable table, int columnIndex) {
        int maxCellWidth = 0;
        int headerWidth = 0;
        int textWidth = 0;
        if (table != null && 0 <= columnIndex) {
            TableColumn tableColumn = table.getColumnModel().getColumn(columnIndex);
            TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();
            FontRenderContext frc = null;
            TableCellRenderer cellRenderer = null;
            Component currentCell = null;
            if (headerRenderer != null) {
                Component headerComponent = headerRenderer.getTableCellRendererComponent(table, tableColumn.getHeaderValue(), false, false, 0, columnIndex);
                if (headerComponent != null) headerWidth = headerComponent.getSize().width;
            }
            if (headerWidth <= 0) {
                Graphics2D g2d = (Graphics2D) table.getGraphics();
                if (g2d != null) frc = g2d.getFontRenderContext(); else frc = new FontRenderContext(null, false, true);
                if (tableColumn.getHeaderValue() instanceof String) textWidth = (int) getTextWidth(table.getFont(), (String) tableColumn.getHeaderValue()); else headerWidth = tableColumn.getWidth();
                headerWidth = Math.max(headerWidth, textWidth);
            }
            for (int i = 0, currentCellWidth = 0, rowCount = table.getRowCount(); i < rowCount; i++) {
                cellRenderer = table.getCellRenderer(i, columnIndex);
                currentCell = cellRenderer.getTableCellRendererComponent(table, table.getValueAt(i, columnIndex), false, false, i, columnIndex);
                currentCellWidth = currentCell.getPreferredSize().width;
                if (currentCellWidth > maxCellWidth) maxCellWidth = currentCellWidth;
            }
        }
        return Math.max(maxCellWidth, headerWidth);
    }

    /**
     * Returns a String representing the file extension of the provided file
     * always in lower case.  Will return null if there is no extension.
     * <p>
     * Note:  An extension is classified as the chracters after the last period
     * in the file name.
     * <p>
     * Warning:  If the file or path does not exist there is no way to tell if
     * the last part of the path is really a file or directory since the
     * <code>File</code> object strips trailling slashes making the last entry
     * in a path statement a file by default.
     *
     * @param   file
     * @return  String
     */
    public static String getFileExtension(File file) {
        String fileExtension = null;
        if (file != null && !file.isDirectory()) {
            String fileName = file.getName();
            int extIndex = fileName.lastIndexOf(EXTENSION_SEPARATOR) + 1;
            fileExtension = fileName.substring(extIndex).toLowerCase();
        }
        return fileExtension;
    }

    /**
     * Converts a Color object to a Hex Color Triplet.
     * <p>
     * Example:
     * <p>
     * The object <code>Color.red</code> would return <code>ff0000</code>.
     * <p>
     * Note: If an invalid or null Color object is provided a null String will
     * be returned.
     *
     * @param   color
     * @return  String
     */
    public static String getHexColorTriplet(Color color) {
        String hexColorTriplet = null;
        final String ZERO_FLUFF = "0";
        if (color != null) {
            StringBuffer hexColorBuffer = new StringBuffer();
            if (color.getRed() < HEX_RADIX) hexColorBuffer.append(ZERO_FLUFF);
            hexColorBuffer.append(Integer.toHexString(color.getRed()));
            if (color.getGreen() < HEX_RADIX) hexColorBuffer.append(ZERO_FLUFF);
            hexColorBuffer.append(Integer.toHexString(color.getGreen()));
            if (color.getBlue() < HEX_RADIX) hexColorBuffer.append(ZERO_FLUFF);
            hexColorBuffer.append(Integer.toHexString(color.getBlue()));
            hexColorTriplet = hexColorBuffer.toString();
        }
        return hexColorTriplet;
    }

    /**
     * Returns an {@link Image} refernced by the provided String representing
     * a complete path name, or class path.  Will return null if location or file
     * is invalid.
     *
     * @param   resourceLocation
     * @return  Image
     */
    public static Image getImage(final String resourceLocation) {
        return getImageIcon(resourceLocation).getImage();
    }

    /**
     * Returns an {@link ImageIcon} refernced by the provided String representing
     * a complete path name, or class path.  Will return null if location or file
     * is invalid.
     *
     * @param   resourceLocation
     * @return  ImageIcon
     */
    public static ImageIcon getImageIcon(String resourceLocation) {
        java.net.URL url = ClassLoader.getSystemResource(resourceLocation);
        return (url != null ? new ImageIcon(url) : new ImageIcon(resourceLocation));
    }

    /**
     * Returns the element at the provided index in the provided String.
     * <p>
     * Note:  An elements in a String are delimeted by a space.
     *
     * @param   string
     * @param   elementIndex
     * @return  String
     */
    public static String getSubElement(String string, int elementIndex) {
        return getSubElement(string, elementIndex, SPACE);
    }

    /**
     * Returns the element at the provided index in the provided String, using
     * the provided delimeter for determining elements.
     *
     * @param   string
     * @param   elementIndex
     * @param   delimeter
     * @return  String
     */
    public static String getSubElement(String string, int elementIndex, String delimeter) {
        StringTokenizer tokenizer = new StringTokenizer(string, delimeter, false);
        for (int i = 0; i < elementIndex; i++) tokenizer.nextToken();
        return tokenizer.nextToken().trim();
    }

    /**
     * Returns a new string that is a subString of the provided string. The
     * subString begins with the character at the specified index and extends
     * to the end of the provided string.
     * <p>
     * Deligates to getSubString(String, int, int).
     * <p>
     * Note:  Currently is a wrapper method of the String.substring(int, int) method.
     * Except that it will not throw an exception if you provide an invalid
     * index.  It will just return an empty string.
     * <p>
     * Advatages:  First, if ever there is a need to change the method of getting
     * a subString then you only change it once here, and the code that uses
     * this method does not need to be changed or recompiled.  Second if a bug
     * creeps into a future version of the Java String then it can be fixed here,
     * again preventing changes to the code that uses this method and recompiles.
     * Finilly, it provides a simple consistant way of gettings a subString
     * without the worry of throwing an exception or getting a null pointer
     * when trying to use the returned String.
     *
     * @param   string
     * @param   beginIndex
     * @return  String
     */
    public static String getSubString(String string, int beginIndex) {
        return getSubString(string, beginIndex, string.length());
    }

    /**
     * Returns a new string that is a subString of the provided string. The
     * subString begins at the specified beginIndex and extends to the character
     * before the endIndex.  Making the length of the subString is endIndex-beginIndex.
     * <p>
     * Note:  Currently is a wrapper method of the String.substring(int, int) method.
     * Except that it will not throw an exception if you provide an invalid
     * index.  It will just return an empty string.
     * <p>
     * Advatages:  First, if ever there is a need to change the method of getting
     * a subString then you only change it once here, and the code that uses
     * this method does not need to be changed or recompiled.  Second if a bug
     * creeps into a future version of the Java String then it can be fixed here,
     * again preventing changes to the code that uses this method and recompiles.
     * Finilly, it provides a simple consistant way of gettings a subString
     * without the worry of throwing an exception or getting a null pointer
     * when trying to use the returned String.
     *
     * @param   string
     * @param   beginIndex
     * @param   endIndex
     * @return  String
     */
    public static String getSubString(String string, int beginIndex, int endIndex) {
        String subString = EMPTY;
        if (string != null) {
            try {
                subString = string.substring(beginIndex, endIndex).trim();
            } catch (Exception e) {
                subString = EMPTY;
            }
        }
        return subString;
    }

    /**
     * Returns the height of the provided <code>text</code> based on the
     * provided {@link Font}.
     * <p>
     * Deligates to {@link #getTextHeight(Font, String, FontRenderContext)},
     * using a default {@link FontRenderContext}.
     *
     * @param   textFont
     * @param   text
     * @return  double
     */
    public static double getTextHeight(Font textFont, String text) {
        return getTextHeight(textFont, text, DEAFAULT_FONT_RENDERER_CONTEXT);
    }

    /**
     * Returns the height of the provided <code>text</code> based on the
     * provided {@link Font} and {@link FontRenderContext}.
     * <p>
     * Note: If the provided {@link FontRenderContext} is null then a default
     * will be
     *
     * @param   textFont
     * @param   text
     * @param   textFRC
     * @return  double
     */
    public static double getTextHeight(Font textFont, String text, FontRenderContext textFRC) {
        double height = 0.0;
        if (textFont != null && text != null) {
            if (textFRC != null) height = textFont.getStringBounds(text, textFRC).getHeight(); else height = textFont.getStringBounds(text, DEAFAULT_FONT_RENDERER_CONTEXT).getHeight();
        }
        return height;
    }

    /**
     * Returns the width of the provided <code>text</code> based on the
     * provided {@link Font}.
     * <p>
     * Deligates to {@link #getTextWidth(Font, String, FontRenderContext)},
     * using a default {@link FontRenderContext}.
     *
     * @param   textFont
     * @param   text
     * @return  double
     */
    public static double getTextWidth(Font textFont, String text) {
        return getTextWidth(textFont, text, DEAFAULT_FONT_RENDERER_CONTEXT);
    }

    /**
     * Returns the width of the provided <code>text</code> based on the
     * provided {@link Font} and {@link FontRenderContext}.
     * <p>
     * Note: If the provided {@link FontRenderContext} is null then a default
     * will be used.
     *
     * @param   textFont
     * @param   text
     * @param   textFRC
     * @return  double
     */
    public static double getTextWidth(Font textFont, String text, FontRenderContext textFRC) {
        double width = 0.0;
        if (textFont != null && text != null) {
            if (textFRC != null) width = textFont.getStringBounds(text, textFRC).getWidth(); else width = textFont.getStringBounds(text, DEAFAULT_FONT_RENDERER_CONTEXT).getWidth();
        }
        return width;
    }

    /**
     * Return true if the underlying platform supports and or permits
     * the provided look and feel.  This method returns false if the look
     * and feel depends on special resources, legal agreements that
     * aren't defined for the current platform, or if any exceptions occur.
     *
     * @param   lnf
     * @return  boole
     */
    public static boolean isAvailableLookAndFeel(String lnf) {
        boolean isSupported = false;
        try {
            Class lnfClass = Class.forName(lnf);
            LookAndFeel newLNF = (LookAndFeel) (lnfClass.newInstance());
            isSupported = newLNF.isSupportedLookAndFeel();
        } catch (Exception e) {
            isSupported = false;
        }
        return isSupported;
    }

    /**
     * Replace all HTML 'No Break Space' (<code>&amp;nbsp;</code>) tags with a
     * regualar space.
     *
     * @param   nbspString
     * @return  String
     */
    public static String nbspToSpace(String nbspString) {
        return nbspString.replaceAll(NBSP, SPACE);
    }

    /**
     * Iterates through each column of the provided {@link JTable} and sets
     * their individual widths based on the columns width as determined by
     * {@link #getColumnWidth(JTable, int)}.
     * <p>
     * Note: If the table is null then nothing will be done.  The <code>padding</code>
     * is the abount of extra space added to the width prior to setting it to
     * the column.  The <code>widthGovernor</code> will govern the width of each
     * column to no larger than this size.
     *
     * @param table
     * @param padding
     * @param widthGovernor
     */
    public static void setAllColumnWidths(JTable table, int padding, int widthGovernor) {
        if (table != null) {
            int columnCount = table.getColumnCount();
            TableColumnModel columnModel = table.getColumnModel();
            TableColumn tableColumn = null;
            for (int i = 0, columnWidth = 0; i < columnCount; i++) {
                tableColumn = columnModel.getColumn(i);
                columnWidth = getColumnWidth(table, i);
                columnWidth += padding;
                if (widthGovernor > 0 && columnWidth > widthGovernor) columnWidth = widthGovernor;
                tableColumn.setPreferredWidth(columnWidth);
                tableColumn.setMinWidth(columnWidth);
                tableColumn.setMaxWidth(columnWidth);
                tableColumn.setWidth(columnWidth);
            }
        }
    }

    /**
     * Consistent way to display a simple error message dialog.  All parameters
     * are optional, use only the ones you want or, may have available at the time
     * needed to display dialog.
     * <p>
     * Main purpose is to allow the chaning of the how the message is displayed
     * in the future without changing an applications code.
     *
     * @param parentComponent
     * @param message
     * @param title
     */
    public static void showErrorMessage(Component parentComponent, String message, String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Consistent way to display a simple error message dialog.  All parameters
     * are optional, use only the ones you want or, may have available at the time
     * needed to display dialog.
     * <p>
     * Main purpose is to allow the chaning of the how the message is displayed
     * in the future without changing an applications code.
     *
     * @param parentComponent
     * @param message
     * @param title
     */
    public static void showErrorMessage(Component parentComponent, String[] message, String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showFeatureNotSupportedMessage(Component parentComponent) {
        showInfoMessage(parentComponent, JTUtilities.COMMON_RESOURCE_BUNDLE.getString("unsupportedFeatureError.Message"), JTUtilities.COMMON_RESOURCE_BUNDLE.getString("unsupportedFeatureError.Title"));
    }

    /**
     * Consistent way to display a simple info message dialog.  All parameters
     * are optional, use only the ones you want or, may have available at the time
     * needed to display dialog.
     * <p>
     * Main purpose is to allow the chaning of the how the message is displayed
     * in the future without changing an applications code.
     *
     * @param parentComponent
     * @param message
     * @param title
     */
    public static void showInfoMessage(Component parentComponent, String message, String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Consistent way to display a simple info message dialog.  All parameters
     * are optional, use only the ones you want or, may have available at the time
     * needed to display dialog.
     * <p>
     * Main purpose is to allow the chaning of the how the message is displayed
     * in the future without changing an applications code.
     *
     * @param parentComponent
     * @param message
     * @param title
     */
    public static void showInfoMessage(Component parentComponent, String[] message, String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Consistent way to display a simple warning message dialog.  All parameters
     * are optional, use only the ones you want or, may have available at the time
     * needed to display dialog.
     * <p>
     * Main purpose is to allow the chaning of the how the message is displayed
     * in the future without changing an applications code.
     *
     * @param parentComponent
     * @param message
     * @param title
     */
    public static void showWarningMessage(Component parentComponent, String message, String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Consistent way to display a simple warning message dialog.  All parameters
     * are optional, use only the ones you want or, may have available at the time
     * needed to display dialog.
     * <p>
     * Main purpose is to allow the chaning of the how the message is displayed
     * in the future without changing an applications code.
     *
     * @param parentComponent
     * @param message
     * @param title
     */
    public static void showWarningMessage(Component parentComponent, String message[], String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Replace all regualar spaces with an HTML 'No Break Space'
     * (<code>&amp;nbsp;</code>) tag.
     *
     * @param   spaceString
     * @return  String
     */
    public static String spaceToNBSP(String spaceString) {
        return spaceString.replaceAll(SPACE, NBSP);
    }

    /**
     * Takes the files in the provided file array and compresses them into a
     * single ZIP file.  It will use the default settings for comments, saving
     * the path info, and the compression level.
     * <p>
     * The default settings for comments is null, saving the path info is false,
     * and the compression level is set to maximum compression.
     *
     * @param   zippedFile
     * @param   filesToZip
     * @throws  IOException
     * @throws  FileNotFoundException
     */
    public static void toZip(File zippedFile, File[] filesToZip) throws IOException, FileNotFoundException {
        toZip(zippedFile, filesToZip, null, ZIP_DEFAULT_SAVE_PATH, ZIP_MAX_COMPRESSION);
    }

    /**
     * Takes the files in the provided file array, compresses them into a single
     * ZIP and will save path info depending on the provided boolean.  It will
     * use the default settings for comments, and the compression level.
     * <p>
     * The default settings for comments is null, saving the path info is false,
     * and the compression level is set to maximum compression.
     *
     * @param   zippedFile
     * @param   filesToZip
     * @param   savePath
     * @throws  IOException
     * @throws  FileNotFoundException
     */
    public static void toZip(File zippedFile, File[] filesToZip, boolean savePath) throws IOException, FileNotFoundException {
        toZip(zippedFile, filesToZip, null, savePath, ZIP_MAX_COMPRESSION);
    }

    /**
     * Takes the files in the provided file array, compresses them into a single
     * ZIP, at the compression level provided, and will save path info depending
     * on the provided boolean.  It will use the default settings for comments.
     * <p>
     * The default settings for comments is null, saving the path info is false,
     * and the compression level is set to maximum compression.
     *
     * @param   zippedFile
     * @param   filesToZip
     * @param   savePath
     * @param   compressionLevel
     * @throws  IOException
     * @throws  FileNotFoundException
     */
    public static void toZip(File zippedFile, File[] filesToZip, boolean savePath, int compressionLevel) throws IOException, FileNotFoundException {
        toZip(zippedFile, filesToZip, null, savePath, compressionLevel);
    }

    /**
     * Takes the files in the provided file array, compresses them into a single
     * ZIP file, and adds the provided comments.  It will use the default
     * settings for saving the path info, and the compression level.
     * <p>
     * The default settings for saving the path info is false, and the
     * compression level is set to maximum compression.
     *
     * @param   zippedFile
     * @param   filesToZip
     * @param   zipComment
     * @throws  IOException
     * @throws  FileNotFoundException
     */
    public static void toZip(File zippedFile, File[] filesToZip, String zipComment) throws IOException, FileNotFoundException {
        toZip(zippedFile, filesToZip, zipComment, ZIP_DEFAULT_SAVE_PATH, ZIP_MAX_COMPRESSION);
    }

    /**
     * Takes the files in the provided file array, compresses them into a single
     * ZIP file, adds the provided comments, and will save path info depending
     * on the provided boolean.  It will use the default settings for the
     * compression level.
     * <p>
     * The default setting for compression level is maximum compression.
     *
     * @param zippedFile
     * @param filesToZip
     * @param zipComment
     * @param savePath
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void toZip(File zippedFile, File[] filesToZip, String zipComment, boolean savePath) throws IOException, FileNotFoundException {
        toZip(zippedFile, filesToZip, zipComment, savePath, ZIP_MAX_COMPRESSION);
    }

    /**
     * Takes the files in the provided file array, compresses them into a single
     * ZIP file, at the compression level provided, adds the provided comments,
     * and will save path info depending on the provided boolean.
     *
     * @param   zippedFile
     * @param   filesToZip
     * @param   zipComment
     * @param   savePath
     * @param   compressionLevel
     * @throws  IOException
     * @throws  FileNotFoundException
     * @throws  ZipException
     */
    public static void toZip(File zippedFile, File[] filesToZip, String zipComment, boolean savePath, int compressionLevel) throws IOException, FileNotFoundException, ZipException {
        if (zippedFile != null && filesToZip != null) {
            new File(zippedFile.getParent()).mkdirs();
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new CheckedOutputStream(new FileOutputStream(zippedFile), new CRC32())));
            if (ZIP_NO_COMPRESSION <= compressionLevel && compressionLevel <= ZIP_MAX_COMPRESSION) out.setLevel(compressionLevel); else out.setLevel(ZIP_MAX_COMPRESSION);
            if (zipComment != null) out.setComment(zipComment);
            for (int i = 0; i < filesToZip.length; i++) {
                BufferedInputStream in;
                if (savePath) {
                    in = new BufferedInputStream(new FileInputStream(filesToZip[i]));
                    out.putNextEntry(new ZipEntry(cleanPath(filesToZip[i].getAbsolutePath())));
                } else {
                    in = new BufferedInputStream(new FileInputStream(filesToZip[i]));
                    out.putNextEntry(new ZipEntry(filesToZip[i].getName()));
                }
                for (int c = in.read(); c != -1; c = in.read()) out.write(c);
                in.close();
            }
            out.close();
        } else throw new ZipException(MAIN_RESOURCE_BUNDLE.getString("default.ZipException.text"));
    }

    /**
     * Unzip's the provided zip file to the provided directory.
     *
     * @param   zippedFile
     * @param   destDir
     * @throws  IOException
     * @throws  ZipException
     */
    public static void unZip(File zippedFile, File destDir) throws IOException, ZipException {
        if (zippedFile != null && destDir != null) {
            ZipFile zipFile = new ZipFile(zippedFile);
            Enumeration zipEntries = zipFile.entries();
            ZipEntry currentEntry;
            File currentFile;
            BufferedOutputStream out;
            InputStream in;
            while (zipEntries.hasMoreElements()) {
                currentEntry = (ZipEntry) zipEntries.nextElement();
                if (!currentEntry.isDirectory()) {
                    currentFile = new File(destDir.getAbsolutePath() + File.separator + currentEntry.getName());
                    new File(currentFile.getParent()).mkdirs();
                    out = new BufferedOutputStream(new FileOutputStream(currentFile));
                    in = zipFile.getInputStream(currentEntry);
                    for (int currentByte = in.read(); currentByte != -1; currentByte = in.read()) out.write(currentByte);
                    in.close();
                    out.close();
                }
            }
        } else throw new ZipException(MAIN_RESOURCE_BUNDLE.getString("default.ZipException.text"));
    }

    /**
     * This method returns a String containing the full Class name and method
     * that called the method that called this method.
     * <p>
     * Example:
     * <p>
     * If the <code>foo()</code> method in Object <code>com.some.package.ClassA</code>
     * calls the <code>bar()</code> method in Object <code>com.someother.package.ClassB</code>
     * and <code>com.someother.package.ClassB</code> calls <code>JTUtilities.whoCalledMe()</code>
     * method.  Then <code>JTUtilities.whoCalledMe()</code> would return to
     * <code>com.someother.package.ClassB</code> the following String:
     * <pre>
     *          com.some.package.ClassA.foo
     * </pre>
     *
     * @return String
     */
    public static String whoCalledMe() {
        final String AT = "at ";
        final char END_CHAR = '(';
        final int AT_OFFSET = 2;
        StringWriter stringWriter = new StringWriter();
        new Throwable().printStackTrace(new PrintWriter(stringWriter));
        String callStack = stringWriter.toString();
        int atPos = callStack.indexOf(AT);
        atPos = callStack.indexOf(AT, atPos + AT_OFFSET);
        atPos = callStack.indexOf(AT, atPos + AT_OFFSET);
        int endPos = callStack.indexOf(END_CHAR, atPos);
        return callStack.substring(atPos + AT_OFFSET, endPos).trim();
    }
}
