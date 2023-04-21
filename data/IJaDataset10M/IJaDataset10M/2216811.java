package org.jpedal.objects;

import java.io.Serializable;
import java.util.StringTokenizer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_String;

/**
 * store data relating to page sizes set in PDF (MediaBox, CropBox, rotation)
 */
public class PdfPageData implements Serializable {

    private static final String SEPARATOR = "|", ROTATION_SEPARATOR = "/";

    private final boolean debug = false;

    private boolean valuesSet = false;

    /** any rotation on page (defined in degress) */
    private Vector_Int rotationXX;

    /** max media string for page */
    private Vector_String mediaString;

    /** max crop string for page */
    private Vector_String cropString;

    /** max x size on current object */
    private Vector_Int mediaBoxWidths;

    /** max y size on current object */
    private Vector_Int mediaBoxHeights;

    /** min x size on current object */
    private Vector_Int mediaBoxXs;

    /** min y size on current object */
    private Vector_Int mediaBoxYs;

    /** max x crop size on current object */
    private Vector_Int cropBoxWidths;

    /** max y crop size on current object */
    private Vector_Int cropBoxHeights;

    /** min x crop size on current object */
    private Vector_Int cropBoxXs;

    /** min y crop size on current object */
    private Vector_Int cropBoxYs;

    private int lastPage = -1;

    private int raw_rotation = 0;

    private int pagesRead = -1;

    private String defaultMediaValue = null;

    /** any rotation on page (defined in degress) */
    private int rotation = 0;

    /** max media string for page */
    private Vector_String rawValues = new Vector_String(500);

    /** current x and y read from page info */
    private int cropBoxX = -99999, cropBoxY = -1, cropBoxW = -1, cropBoxH = -1;

    /** current x and y read from page info */
    private int mediaBoxX = -1, mediaBoxY, mediaBoxW, mediaBoxH;

    /** string representation of crop box */
    private String cropValue = "";

    /** string representation of media box */
    private String mediaValue = "";

    private int defaultrotation;

    private int defaultcropBoxX, defaultcropBoxY, defaultcropBoxW, defaultcropBoxH;

    private int defaultmediaBoxX, defaultmediaBoxY, defaultmediaBoxW, defaultmediaBoxH;

    public PdfPageData() {
        if (debug) {
            rotationXX = new Vector_Int(20);
            mediaString = new Vector_String(20);
            cropString = new Vector_String(20);
            mediaBoxWidths = new Vector_Int(20);
            mediaBoxHeights = new Vector_Int(20);
            mediaBoxXs = new Vector_Int(20);
            mediaBoxYs = new Vector_Int(20);
            cropBoxWidths = new Vector_Int(20);
            cropBoxHeights = new Vector_Int(20);
            cropBoxXs = new Vector_Int(20);
            cropBoxYs = new Vector_Int(20);
        }
    }

    /**
     * make sure a value set for crop and media box (used internally to trap 'odd' settings and insure setup correctly)
     */
    public void checkSizeSet(int pageNumber) {
        if (debug) {
            if ((mediaBoxW == 0) | (mediaBoxH == 0)) {
                LogWriter.writeLog("NO page co-ords set - using 800 * 800");
                mediaBoxW = 800;
                mediaBoxH = 800;
                cropBoxW = 800;
                cropBoxH = 800;
            }
            mediaBoxXs.setElementAt(mediaBoxX, pageNumber);
            mediaBoxYs.setElementAt(mediaBoxY, pageNumber);
            mediaBoxWidths.setElementAt(mediaBoxW, pageNumber);
            mediaBoxHeights.setElementAt(mediaBoxH, pageNumber);
            cropBoxXs.setElementAt(cropBoxX, pageNumber);
            cropBoxYs.setElementAt(cropBoxY, pageNumber);
            cropBoxWidths.setElementAt(cropBoxW, pageNumber);
            cropBoxHeights.setElementAt(cropBoxH, pageNumber);
            cropString.setElementAt(cropValue, pageNumber);
            mediaString.setElementAt(mediaValue, pageNumber);
            System.out.println(pageNumber + " " + raw_rotation + "<<checkSizeSet media=" + mediaValue + " " + cropValue);
        }
        if (mediaValue == null) mediaValue = defaultMediaValue;
        if (raw_rotation != 0) {
            StringBuffer newValue = new StringBuffer("" + raw_rotation);
            newValue.append(ROTATION_SEPARATOR);
            int previousValue = mediaValue.lastIndexOf(ROTATION_SEPARATOR);
            if (previousValue == -1) newValue.append(mediaValue); else newValue.append(mediaValue.substring(previousValue + 1));
            mediaValue = newValue.toString();
        }
        if ((cropValue != null) && (!mediaValue.equals(cropValue))) {
            StringBuffer combinedValue = new StringBuffer(mediaValue);
            combinedValue.append(SEPARATOR);
            combinedValue.append(cropValue);
            rawValues.setElementAt(combinedValue.toString(), pageNumber);
        } else if ((mediaValue != null) && (mediaValue.equals(defaultMediaValue))) {
            rawValues.setElementAt(null, pageNumber);
        } else rawValues.setElementAt(mediaValue, pageNumber);
        if (pagesRead < pageNumber) pagesRead = pageNumber;
        mediaValue = null;
        cropValue = null;
    }

    /**
     * return height of mediaBox
     */
    public final int getMediaBoxHeight(int pageNumber) {
        setSizeForPage(pageNumber);
        if ((debug) && (mediaBoxHeights.elementAt(pageNumber) != mediaBoxH)) {
            System.out.println("Wrong height " + mediaBoxHeights.elementAt(pageNumber) + " " + mediaBoxH);
            System.exit(1);
        }
        return mediaBoxH;
    }

    /** return rotation value (for outside class) */
    public final int getRotation(int pageNumber) {
        setSizeForPage(pageNumber);
        if ((debug) && (rotationXX.elementAt(pageNumber) != rotation)) {
            System.out.println("Wrong rotation " + rotationXX.elementAt(pageNumber) + " " + rotation);
            System.exit(1);
        }
        return rotation;
    }

    /**
     * return mediaBox y value
     */
    public final int getMediaBoxY(int pageNumber) {
        setSizeForPage(pageNumber);
        if ((debug) && (mediaBoxYs.elementAt(pageNumber) != mediaBoxY)) {
            System.out.println("Wrong y " + mediaBoxYs.elementAt(pageNumber) + " " + mediaBoxY);
            System.exit(1);
        }
        return mediaBoxY;
    }

    /**
     * return mediaBox x value
     */
    public final int getMediaBoxX(int pageNumber) {
        setSizeForPage(pageNumber);
        if ((debug) && (mediaBoxXs.elementAt(pageNumber) != mediaBoxX)) {
            System.out.println("Wrong x " + mediaBoxXs.elementAt(pageNumber) + " " + mediaBoxX);
            System.exit(1);
        }
        return mediaBoxX;
    }

    /**
	 * set string with raw values and assign values to crop and media size
	 */
    public void setMediaBox(String value) {
        if (debug) {
            String media_box = Strip.removeArrayDeleminators(value);
            StringTokenizer media_values = new StringTokenizer(media_box, " ");
            mediaValue = media_box;
            cropValue = media_box;
            mediaBoxX = ((int) Float.parseFloat(media_values.nextToken()));
            mediaBoxY = ((int) Float.parseFloat(media_values.nextToken()));
            mediaBoxW = ((int) Float.parseFloat(media_values.nextToken())) - mediaBoxX;
            mediaBoxH = ((int) Float.parseFloat(media_values.nextToken())) - mediaBoxY;
            cropBoxX = mediaBoxX;
            cropBoxY = mediaBoxY;
            cropBoxW = mediaBoxW;
            cropBoxH = mediaBoxH;
            System.out.println("Set media to=" + mediaValue);
        }
        mediaValue = Strip.removeArrayDeleminators(value);
        cropValue = null;
        if (defaultMediaValue == null) {
            defaultMediaValue = mediaValue;
        }
    }

    /**
	 * set crop with values and align with media box
	 */
    public void setCropBox(String value) {
        if (debug) {
            String cropBox = Strip.removeArrayDeleminators(value);
            StringTokenizer media_values = new StringTokenizer(cropBox, " ");
            cropValue = cropBox;
            cropBoxX = ((int) Float.parseFloat(media_values.nextToken()));
            cropBoxY = ((int) Float.parseFloat(media_values.nextToken()));
            cropBoxW = ((int) Float.parseFloat(media_values.nextToken())) - cropBoxX;
            cropBoxH = ((int) Float.parseFloat(media_values.nextToken())) - cropBoxY;
        }
        cropValue = Strip.removeArrayDeleminators(value);
    }

    public int setPageRotation(String value, int pageNumber) {
        if (debug) {
            raw_rotation = 0;
            try {
                raw_rotation = Integer.parseInt(value);
                if (raw_rotation < 0) raw_rotation = 360 + raw_rotation;
                rotationXX.setElementAt(raw_rotation, pageNumber);
            } catch (Exception e) {
                LogWriter.writeLog("Exception " + e + " reading rotation");
            }
        }
        raw_rotation = Integer.parseInt(value);
        if (raw_rotation < 0) raw_rotation = 360 + raw_rotation;
        return raw_rotation;
    }

    /**
     * return width of media box
     */
    public final int getMediaBoxWidth(int pageNumber) {
        setSizeForPage(pageNumber);
        if ((debug) && (mediaBoxWidths.elementAt(pageNumber) != mediaBoxW)) {
            System.out.println("Wrong w " + mediaBoxWidths.elementAt(pageNumber) + " " + mediaBoxW);
            System.exit(1);
        }
        return mediaBoxW;
    }

    /**
     * return mediaBox string found in PDF file
     */
    public String getMediaValue(int currentPage) {
        String raw = rawValues.elementAt(currentPage);
        if (raw.length() == 0) raw = defaultMediaValue;
        int rotPrefix = raw.indexOf(ROTATION_SEPARATOR);
        if (rotPrefix != -1) {
            raw = raw.substring(rotPrefix + 1);
        }
        int separator = raw.indexOf(SEPARATOR);
        if (separator == -1) return raw; else return raw.substring(0, separator);
    }

    /**
     * return cropBox string found in PDF file
     */
    public String getCropValue(int currentPage) {
        String raw = rawValues.elementAt(currentPage);
        if (raw.length() == 0) raw = defaultMediaValue;
        int rotPrefix = raw.indexOf(ROTATION_SEPARATOR);
        if (rotPrefix != -1) raw = raw.substring(rotPrefix + 1);
        int separator = raw.indexOf(SEPARATOR);
        if (separator == -1) return raw; else return raw.substring(separator + 1);
    }

    /**
     * return x value for cropBox
     */
    public int getCropBoxX(int pageNumber) {
        setSizeForPage(pageNumber);
        if ((debug) && (cropBoxXs.elementAt(pageNumber) != cropBoxX)) {
            System.out.println("Wrong x " + cropBoxXs.elementAt(pageNumber) + " " + cropBoxX);
            System.exit(1);
        }
        return cropBoxX;
    }

    /**
     * return cropBox width
     */
    public int getCropBoxWidth(int pageNumber) {
        setSizeForPage(pageNumber);
        if ((debug) && (cropBoxWidths.elementAt(pageNumber) != cropBoxW)) {
            System.out.println("Wrong w " + cropBoxWidths.elementAt(pageNumber) + " " + cropBoxW);
            System.exit(1);
        }
        return cropBoxW;
    }

    /**
     * return y value for cropox
     */
    public int getCropBoxY(int pageNumber) {
        setSizeForPage(pageNumber);
        if ((debug) && (cropBoxYs.elementAt(pageNumber) != cropBoxY)) {
            System.out.println("Wrong y " + cropBoxYs.elementAt(pageNumber) + " " + cropBoxY);
            System.exit(1);
        }
        return cropBoxY;
    }

    /**
     * return cropBox height
     */
    public int getCropBoxHeight(int pageNumber) {
        setSizeForPage(pageNumber);
        if ((debug) && (cropBoxHeights.elementAt(pageNumber) != cropBoxH)) {
            System.out.println("Wrong h " + cropBoxHeights.elementAt(pageNumber) + " " + cropBoxH);
            System.exit(1);
        }
        return cropBoxH;
    }

    /**see if current figures generated for this page and setup if not*/
    private void setSizeForPage(int pageNumber) {
        if (pageNumber > pagesRead) {
            mediaBoxX = 0;
            mediaBoxY = 0;
            mediaBoxW = 0;
            mediaBoxH = 0;
            cropBoxX = 0;
            cropBoxY = 0;
            cropBoxW = 0;
            cropBoxH = 0;
        } else if ((pageNumber > 0) && (lastPage != pageNumber)) {
            StringTokenizer media_values;
            lastPage = pageNumber;
            rotation = 0;
            boolean usingDefault = false;
            String raw = rawValues.elementAt(pageNumber);
            if ((raw.length() == 0) && (defaultMediaValue != null)) {
                raw = defaultMediaValue;
                usingDefault = true;
            }
            if (valuesSet && usingDefault) {
                rotation = defaultrotation;
                cropBoxX = defaultcropBoxX;
                mediaBoxX = defaultmediaBoxX;
                cropBoxY = defaultcropBoxY;
                mediaBoxY = defaultmediaBoxY;
                cropBoxW = defaultcropBoxW;
                mediaBoxW = defaultmediaBoxW;
                cropBoxH = defaultcropBoxH;
                mediaBoxH = defaultmediaBoxH;
            } else {
                if (debug) System.out.println("raw=" + raw);
                int rotPrefix = raw.indexOf(ROTATION_SEPARATOR);
                if (rotPrefix != -1) {
                    String rotValue = raw.substring(0, rotPrefix);
                    rotation = Integer.parseInt(rotValue);
                    raw = raw.substring(rotPrefix + 1);
                }
                if (debug) System.out.println("Set rotation=" + rotation + " page " + pageNumber);
                int separator = raw.indexOf(SEPARATOR);
                if (separator == -1) media_values = new StringTokenizer(raw, " "); else media_values = new StringTokenizer(raw.substring(0, separator), " ");
                mediaBoxX = 0;
                mediaBoxY = 0;
                mediaBoxW = 800;
                mediaBoxH = 800;
                if (media_values.countTokens() == 4) {
                    mediaBoxX = ((int) Float.parseFloat(media_values.nextToken()));
                    mediaBoxY = ((int) Float.parseFloat(media_values.nextToken()));
                    mediaBoxW = ((int) Float.parseFloat(media_values.nextToken())) - mediaBoxX;
                    mediaBoxH = ((int) Float.parseFloat(media_values.nextToken())) - mediaBoxY;
                }
                if (separator != -1) {
                    media_values = new StringTokenizer(raw.substring(separator + 1), " ");
                    cropBoxX = ((int) Float.parseFloat(media_values.nextToken()));
                    cropBoxY = ((int) Float.parseFloat(media_values.nextToken()));
                    cropBoxW = ((int) Float.parseFloat(media_values.nextToken())) - cropBoxX;
                    cropBoxH = ((int) Float.parseFloat(media_values.nextToken())) - cropBoxY;
                } else {
                    cropBoxX = mediaBoxX;
                    cropBoxY = mediaBoxY;
                    cropBoxW = mediaBoxW;
                    cropBoxH = mediaBoxH;
                }
            }
            if (usingDefault && !valuesSet) {
                defaultrotation = rotation;
                defaultcropBoxX = cropBoxX;
                defaultmediaBoxX = mediaBoxX;
                defaultcropBoxY = cropBoxY;
                defaultmediaBoxY = mediaBoxY;
                defaultcropBoxW = cropBoxW;
                defaultmediaBoxW = mediaBoxW;
                defaultcropBoxH = cropBoxH;
                defaultmediaBoxH = mediaBoxH;
                valuesSet = true;
            }
        }
    }
}
