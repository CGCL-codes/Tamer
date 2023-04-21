package edu.asu.cri.MirkE.gui;

import java.util.*;
import java.awt.*;
import java.awt.color.*;
import edu.asu.cri.MirkE.exceptions.MirkEApplicationException;
import edu.asu.cri.MirkE.exceptions.MirkEFileNotFoundException;
import edu.asu.cri.MirkE.exceptions.MirkESystemException;
import java.io.*;
import java.beans.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.asu.cri.MirkE.*;

/**

@author Scott Menor
@version 0.3.0, 2 februrary, 2004
*/
public class DataSetColorModel {

    static Map wavelengthToCIE_XYZ = null;

    static ICC_ColorSpace rgbSpace;

    /**
     * Logger
     * */
    private static final Log log = LogFactory.getLog(DataSetColorModel.class);

    /**
		@param mirke
	 @param query HQL query

	 @return color
	 */
    public static java.awt.Color toColor(MirkE mirke, String query) {
        return null;
    }

    /**
	 * @param mirke
	 * @param observableToMinValueMap
	 * @param observableToMeanValueMap
	 * @param observableToMaxValueMap
	 * @return color
	 * @throws MirkEApplicationException
	 */
    public static java.awt.Color toColor(MirkE mirke, Map observableToMinValueMap, Map observableToMeanValueMap, Map observableToMaxValueMap) throws MirkESystemException {
        Color color = Color.white;
        Set observableToMeanValueMapSet = observableToMeanValueMap.entrySet();
        Iterator observableToMeanValueSetIterator = observableToMeanValueMapSet.iterator();
        while (observableToMeanValueSetIterator.hasNext()) {
            Map.Entry observableToMeanValueEntry = (Map.Entry) observableToMeanValueSetIterator.next();
            String observableName = observableToMeanValueEntry.getKey().toString();
            String wavelengthString = observableName.substring(2, observableName.indexOf("nm"));
            Integer wavelength = new Integer(wavelengthString);
            double minValue = ((Double) observableToMinValueMap.get(observableName)).doubleValue();
            double meanValue = ((Double) observableToMeanValueEntry.getValue()).doubleValue();
            double maxValue = ((Double) observableToMaxValueMap.get(observableName)).doubleValue();
            if (maxValue - minValue != 0) {
                try {
                    color = wavelengthToColor(wavelength, (meanValue - minValue) / (maxValue - minValue));
                } catch (MirkEFileNotFoundException nf) {
                    throw new MirkESystemException(true);
                } catch (MirkEApplicationException ex) {
                    throw new MirkESystemException(true);
                }
            }
        }
        return color;
    }

    /**

	 @return wavelengthToCIE_XYZ <code>Map</code> from wavelength in nm to a <code>Vector</code> containing components of a corresponding CIE_XYZ color
	 */
    public static Map getWavelengthToCIE_XYZ() throws MirkEFileNotFoundException {
        XMLDecoder decoder = null;
        File file = new File("configuration", "wavelengthToCIE_XYZ.xml");
        if (wavelengthToCIE_XYZ == null) {
            try {
                decoder = new XMLDecoder(new FileInputStream(file));
            } catch (FileNotFoundException fn) {
                throw new MirkEFileNotFoundException((Throwable) fn);
            } catch (IOException io) {
                io.printStackTrace();
            }
            wavelengthToCIE_XYZ = (HashMap) decoder.readObject();
            decoder.close();
        }
        return wavelengthToCIE_XYZ;
    }

    /** Default constructor
		*/
    public DataSetColorModel() {
    }

    /**
	 * @param wavelength
	 * @return CIE XYZ components corresponding to a specific <code>wavelength<code> of monochromatic light at a standard intensity
     */
    public static float[] wavelengthToCIE_XYZcomponents(Integer wavelength) throws MirkEApplicationException, MirkEFileNotFoundException {
        Vector CIE_XYZcomponentsFromWavelength = (Vector) getWavelengthToCIE_XYZ().get(wavelength);
        float[] CIE_XYZcomponents = new float[CIE_XYZcomponentsFromWavelength.size()];
        for (int i = 0; i < CIE_XYZcomponentsFromWavelength.size(); i++) {
            CIE_XYZcomponents[i] = ((Float) CIE_XYZcomponentsFromWavelength.elementAt(i)).floatValue();
        }
        return CIE_XYZcomponents;
    }

    /** Convert from a wavelength of monochromatic light to an approximate Color

		@param wavelength an <code>double</code> wavelength (in nm)
		@param alpha The alpha channel component of the returned <code>Color</code>

		@return color
	 * @throws MirkEApplicationException
		*/
    public static Color wavelengthToColor(double wavelength, double alpha) throws MirkEApplicationException, MirkEFileNotFoundException {
        return wavelengthToColor(new Integer((int) Math.round(wavelength)), (float) alpha);
    }

    /** Convert from a wavelength of monochromatic light to an approximate Color

		@param wavelength an <code>Integer</code> wavelength (in nm)
		@param alpha The alpha channel component of the returned <code>Color</code>

		@return color
	 * @throws MirkEApplicationException
		*/
    public static Color wavelengthToColor(Integer wavelength, double alpha) throws MirkEApplicationException, MirkEFileNotFoundException {
        return wavelengthToColor(wavelength, (float) alpha);
    }

    /** Convert from XYZ values to a Color

		@param xyzValues

		@return color
	 * @throws MirkEApplicationException
		*/
    public static Color xyzToColor(float[] xyzValues) throws MirkEApplicationException {
        return xyzToColor(xyzValues, 1.0f);
    }

    /** Convert from XYZ values to a Color
	 *
	 *@param xyzValues
	 * @param alpha
	 *
	 * @return color
	 * @throws MirkEApplicationException
	 */
    public static Color xyzToColor(float[] xyzValues, float alpha) throws MirkEApplicationException {
        if (rgbSpace == null) {
            rgbSpace = new ICC_ColorSpace(ICC_Profile.getInstance(ICC_ColorSpace.CS_sRGB));
        }
        if (alpha > 1) {
            alpha = 1f;
        } else if (alpha < 0) {
            alpha = 0f;
        }
        Color color = new Color(rgbSpace, rgbSpace.fromCIEXYZ(xyzValues), alpha);
        return color;
    }

    /** Convert from a wavelength of monochromatic light to an approximate Color
	 *
	 * @param wavelength an <code>Integer</code> wavelength (in nm)
	 * @param alpha The alpha channel component of the returned <code>Color</code>
	 *
	 * @return color
	 * @throws MirkEApplicationException
	 */
    public static Color wavelengthToColor(Integer wavelength, float alpha) throws MirkEApplicationException, MirkEFileNotFoundException {
        if (alpha < 0) {
            alpha = 0f;
        } else if (alpha > 1) {
            alpha = 1f;
        }
        return xyzToColor(wavelengthToCIE_XYZcomponents(wavelength), alpha);
    }

    /**
     * @param observableToMeanValueMap
	 * @return a representative <code>Color</code> for the observable
	 * @throws MirkEApplicationException
	 */
    public static Color observableToMeanValueMapToColor(Map observableToMeanValueMap) throws MirkEApplicationException, MirkEFileNotFoundException {
        Set entrySet = observableToMeanValueMap.entrySet();
        float floatValue = 0;
        Iterator entrySetIterator = entrySet.iterator();
        while (entrySetIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) entrySetIterator.next();
            Double value = (Double) entry.getValue();
            floatValue = (float) value.doubleValue();
        }
        return wavelengthToColor((600), (float) .5 * floatValue);
    }

    /** Simple conversion from an array of floats to a Vector

		@param values

		@return vector
		*/
    public static Vector toVector(float[] values) {
        Vector vector = new Vector();
        for (int i = 0; i < values.length; i++) {
            vector.add(i, new Float(values[i]));
        }
        return vector;
    }
}
