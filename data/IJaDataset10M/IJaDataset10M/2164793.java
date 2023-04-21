package edu.wisc.ssec.mcidas;

/**
 * Utility class for creating <code>Calibrator</code> instances.
 * 
 * @author Bruce Flynn, SSEC
 * @version $Id: CalibratorFactory.java,v 1.1 2009/04/17 18:05:15 rboller_cvs Exp $
 */
public final class CalibratorFactory {

    /** Disallow instatiantion. */
    private CalibratorFactory() {
    }

    /**
     * Get an appropriate <code>Calibrator</code> for the sensor id provided.
     * This assumes a RAW source data format. See the McIDAS Users Guide 
     * <a href="http://www.ssec.wisc.edu/mcidas/doc/users_guide/current/app_c-1.html">Appendix C</a>
     * for a table of sensor ids.
     * @param id Sensor id from the directory block
     * @param cal Calibration block used to initialize the <code>Calibrator
     * </code>
     * @return initialized <code>Calibrator</code> with a source calibration
     * type of RAW.
     * @throws CalibratorException on an error initializing the object.
     */
    public static final Calibrator getCalibrator(final int id, final int[] cal) throws CalibratorException {
        return getCalibrator(id, Calibrator.CAL_RAW, cal);
    }

    /**
     * Get an appropriate <code>Calibrator</code> for the sensor id provided.
     * See the McIDAS Users Guide 
     * <a href="http://www.ssec.wisc.edu/mcidas/doc/users_guide/current/app_c-1.html">Appendix C</a>
     * for a table of sensor ids.
     * @param id Sensor id from the directory block
     * @param srcType the source data type from the directory block
     * @param cal Calibration block used to initialize the 
     *        <code>Calibrator</code>
     * @return initialized <code>Calibrator</code>.
     * @throws CalibratorException on an error initializing the object or if the
     *         sensor is unknown.
     */
    public static final Calibrator getCalibrator(final int id, final int srcType, final int[] cal) throws CalibratorException {
        Calibrator calibrator = null;
        switch(id) {
            case Calibrator.SENSOR_MSG_IMGR:
                calibrator = new CalibratorMsg(cal);
                calibrator.setCalType(srcType);
                break;
            case Calibrator.SENSOR_GOES8_IMGR:
            case Calibrator.SENSOR_GOES8_SNDR:
                calibrator = new CalibratorGvarG8(id, cal);
                calibrator.setCalType(srcType);
                break;
            case Calibrator.SENSOR_GOES9_IMGR:
            case Calibrator.SENSOR_GOES9_SNDR:
                calibrator = new CalibratorGvarG9(id, cal);
                calibrator.setCalType(srcType);
                break;
            case Calibrator.SENSOR_GOES10_IMGR:
            case Calibrator.SENSOR_GOES10_SNDR:
                calibrator = new CalibratorGvarG10(id, cal);
                calibrator.setCalType(srcType);
                break;
            case Calibrator.SENSOR_GOES12_IMGR:
            case Calibrator.SENSOR_GOES12_SNDR:
                calibrator = new CalibratorGvarG12(id, cal);
                calibrator.setCalType(srcType);
                break;
            default:
                throw new CalibratorException("Unknown or unimplemented sensor id: " + id);
        }
        return calibrator;
    }

    /**
   * Check if there is a <code>Calibrator</code> implemented for a sensor.
   * 
   * @param id Id of the sensor from the McIDAS Users Guide
   * <a href="http://www.ssec.wisc.edu/mcidas/doc/users_guide/current/app_c-1.html">Appendix C</a>
   * @return True if there is an implemented <code>Calibrator</code>, false
   *         otherwise.
   * @see The McIDAS Users Guide 
   */
    public static final boolean hasCalibrator(int id) {
        switch(id) {
            case Calibrator.SENSOR_GOES12_IMGR:
            case Calibrator.SENSOR_GOES12_SNDR:
            case Calibrator.SENSOR_GOES10_IMGR:
            case Calibrator.SENSOR_GOES10_SNDR:
            case Calibrator.SENSOR_GOES8_IMGR:
            case Calibrator.SENSOR_GOES8_SNDR:
            case Calibrator.SENSOR_GOES9_IMGR:
            case Calibrator.SENSOR_GOES9_SNDR:
            case Calibrator.SENSOR_MSG_IMGR:
                return true;
            default:
                return false;
        }
    }
}
