package de.olypedia;

import akme.mobile.util.NumberUtil;
import de.olypedia.ImagingSetting.ImagingSettingException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * The model for the MIDlet UI to manipulate its state with.
 *
 * @author khb
 */
public class ImagingSettingModel {

    /** calculation target focal length */
    public static final int CALCULATE_FOCAL_LENGTH = 1;

    /** calculation target aperture */
    public static final int CALCULATE_APERTURE = 2;

    /** calculation target distance */
    public static final int CALCULATE_DISTANCE = 3;

    /** calculation target depth of field */
    public static final int CALCULATE_DEPTH_OF_FIELD = 4;

    /** minimum value for calculation target */
    public static final int CALCULATE_MIN = CALCULATE_FOCAL_LENGTH;

    /** maximum value for calculation target */
    public static final int CALCULATE_MAX = CALCULATE_DEPTH_OF_FIELD;

    /** the calculation target set, default is dof */
    private int _fieldToCalculate = CALCULATE_DEPTH_OF_FIELD;

    /** the actual data */
    private ImagingSetting _data;

    /** change listeners */
    private final Vector _listeners = new Vector();

    /** circle of confusion to use, default as per ImagingSetting */
    private double _circleOfConfusion = ImagingSetting.DEFAULT_CIRCLE_OF_CONFUSION;

    /**
     * Private default constructor
     */
    private ImagingSettingModel() {
    }

    /**
     * Constructs a new ImagingSettingModel from the values passed in. 
     * The value for the property to calculate is ignored, if passed.
     * 
     * @param calculateWhat the value to calculate as per CALCULAT_*
     * @param circleOfConfusion the circle of confusion to use
     * @param focalLength focal length
     * @param aperture aperture
     * @param distance subject distance
     * @param dof depth-of-field, 0 is also infinity
     * @throws de.olypedia.ImagingSetting.ImagingSettingException
     */
    public ImagingSettingModel(int calculateWhat, double circleOfConfusion, double focalLength, double aperture, double distance, double dof) throws ImagingSettingException {
        if (calculateWhat >= ImagingSettingModel.CALCULATE_MIN && calculateWhat <= ImagingSettingModel.CALCULATE_MAX) {
            _fieldToCalculate = calculateWhat;
        } else {
            _fieldToCalculate = ImagingSettingModel.CALCULATE_DEPTH_OF_FIELD;
        }
        _circleOfConfusion = circleOfConfusion;
        updateStateInternal(focalLength, aperture, distance, dof);
    }

    /**
     * Registers a new ImagingChangeListener
     *
     * @param l listener to notify
     */
    public void addChangeListener(ImagingChangeListener l) {
        _listeners.addElement(l);
    }

    /**
     * Updates the model with new properties where one of the properties
     * is calculated from the others.
     * 
     * @param focalLength focal length
     * @param aperture aperture
     * @param distance subject distance
     * @param dof depth of field, 0 is also infinity
     * @throws de.olypedia.ImagingSetting.ImagingSettingException
     */
    private void updateStateInternal(double focalLength, double aperture, double distance, double dof) throws ImagingSettingException {
        switch(_fieldToCalculate) {
            case CALCULATE_FOCAL_LENGTH:
                _data = ImagingSetting.calculateFocalLength(_circleOfConfusion, aperture, distance, dof);
                break;
            case CALCULATE_APERTURE:
                _data = ImagingSetting.calculateAperture(_circleOfConfusion, focalLength, distance, dof);
                break;
            case CALCULATE_DISTANCE:
                _data = ImagingSetting.calculateDistance(_circleOfConfusion, focalLength, aperture, dof);
                break;
            case CALCULATE_DEPTH_OF_FIELD:
            default:
                _data = ImagingSetting.calculateDepthOfField(_circleOfConfusion, focalLength, aperture, distance);
        }
    }

    /**
     * Updates the model with new properties where one of the properties
     * is calculated from the others.
     *
     * @param focalLength focal length
     * @param aperture aperture
     * @param distance subject distance
     * @param dof depth of field, 0 is also infinity
     * @throws de.olypedia.ImagingSetting.ImagingSettingException
     */
    public void updateState(double focalLength, double aperture, double distance, double dof) throws ImagingSettingException {
        ImagingSettingModel old = clone();
        updateStateInternal(focalLength, aperture, distance, dof);
        notifyListeners(old);
    }

    /**
     * Sets the field to calculate. This also triggers a recalculate.
     * 
     * @param fieldToCalculate field to calculate as per CALCULATE_*
     * @throws de.olypedia.ImagingSetting.ImagingSettingException
     */
    public void setFieldToCalculate(int fieldToCalculate) throws ImagingSettingException {
        if (fieldToCalculate >= ImagingSettingModel.CALCULATE_MIN && fieldToCalculate <= ImagingSettingModel.CALCULATE_MAX) {
            ImagingSettingModel old = clone();
            _fieldToCalculate = fieldToCalculate;
            try {
                updateState(getFocalLength(), getAperture(), getDistance(), getDepthOfField());
                notifyListeners(old);
            } catch (ImagingSettingException e) {
                _fieldToCalculate = old._fieldToCalculate;
                throw e;
            }
        }
    }

    /**
     * Sets the circle of confusion to use. Also triggers a recalculate.
     * 
     * @param circleOfConfusion circle of confusion to use
     * @throws de.olypedia.ImagingSetting.ImagingSettingException
     */
    public void setCircleOfConfusion(double circleOfConfusion) throws ImagingSettingException {
        ImagingSettingModel old = clone();
        if (circleOfConfusion <= 0 || Double.isInfinite(circleOfConfusion)) {
            _circleOfConfusion = ImagingSetting.DEFAULT_CIRCLE_OF_CONFUSION;
        } else {
            _circleOfConfusion = circleOfConfusion;
        }
        try {
            updateState(getFocalLength(), getAperture(), getDistance(), getDepthOfField());
            notifyListeners(old);
        } catch (ImagingSettingException e) {
            _circleOfConfusion = old._circleOfConfusion;
            throw e;
        }
    }

    /**
     * Notify all change listeners in order
     * 
     * @param old copy of the model before the change
     */
    public void notifyListeners(ImagingSettingModel old) {
        if (_listeners.size() > 0) {
            Enumeration e = _listeners.elements();
            while (e.hasMoreElements()) {
                ImagingChangeListener l = (ImagingChangeListener) e.nextElement();
                l.changedImagingSetting(old, this);
            }
        }
    }

    /**
     * Accessor for the calculation target
     * @return calculation target as per CALCULATE_*
     */
    public int getFieldToCalculate() {
        return _fieldToCalculate;
    }

    /**
     * Accessor for focal length. The request is proxied to underlying 
     * ImagingSetting, but rounded to an integer with infinity mapped to 0 and
     * anything below 1 to 1.
     * 
     * @return focal length
     */
    public int getFocalLength() {
        return roundToInteger(_data.getFocalLength());
    }

    /**
     * Accessor for aperture. The request is proxied to underlying
     * ImagingSetting. Note, the model already rounds to a single decimal
     * digit.
     *
     * @return aperture
     */
    public double getAperture() {
        return _data.getAperture();
    }

    /**
     * Accessor for subject distance. The request is proxied to underlying
     * ImagingSetting, but rounded to an integer with infinity mapped to 0 and
     * anything below 1 to 1
     *
     * @return subject distance
     */
    public int getDistance() {
        return roundToInteger(_data.getDistance());
    }

    /**
     * Accessor for depth of field. The request is proxied to underlying
     * ImagingSetting, but rounded to an integer with infinity mapped to 0 and
     * anything below 1 to 1
     *
     * @return depth of field
     */
    public int getDepthOfField() {
        return roundToInteger(_data.getDepthOfField());
    }

    /**
     * Accessor for dof near limit. The request is proxied to underlying
     * ImagingSetting, but rounded to an integer with infinity mapped to 0 and
     * anything below 1 to 1
     *
     * @return dof near limit
     */
    public int getNearEdge() {
        return roundToInteger(_data.getNearEdge());
    }

    /**
     * Accessor for dof far limit. The request is proxied to underlying
     * ImagingSetting, but rounded to an integer with infinity mapped to 0 and
     * anything below 1 to 1
     *
     * @return dof far limit
     */
    public int getFarEdge() {
        return roundToInteger(_data.getFarEdge());
    }

    /**
     * Accessor for the circle of confusion
     * @return circle of confusion
     */
    public double getCircleOfConfusion() {
        return _circleOfConfusion;
    }

    public String getFocalLengthAsString() {
        return Integer.toString(getFocalLength());
    }

    public String getApertureAsString() {
        double d = getAperture();
        return Double.toString(d);
    }

    public String getDistanceAsString() {
        return Integer.toString(getDistance());
    }

    public String getDepthOfFieldAsString() {
        return Integer.toString(getDepthOfField());
    }

    public String getNearEdgeAsString() {
        return Integer.toString(getNearEdge());
    }

    public String getFarEdgeAsString() {
        return Integer.toString(getFarEdge());
    }

    /**
     * Rounds a double to an integer correctly and maps infinity to 0 and
     * anything smaller than 1 to 1 along the way.
     *
     * @param d the value to convert
     * @return the rounded integer value
     */
    private int roundToInteger(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return 0;
        } else {
            int i = (int) NumberUtil.round(d, 0);
            if (i == 0) {
                return 1;
            } else {
                return i;
            }
        }
    }

    /**
     * Does a deep copy of the instance
     * @return an instance with all the properties equal to the input instance
     */
    public ImagingSettingModel clone() {
        ImagingSettingModel copy = new ImagingSettingModel();
        copy._fieldToCalculate = this._fieldToCalculate;
        copy._circleOfConfusion = this._circleOfConfusion;
        copy._data = this._data;
        return copy;
    }
}
