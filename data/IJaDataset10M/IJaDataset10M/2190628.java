package shu.cms.measure.meter;

import shu.cms.colorspace.depend.*;
import shu.cms.colorspace.independ.*;
import shu.cms.measure.meter.Meter.*;
import shu.cms.Spectra;
import shu.util.log.Logger;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class ShareMemoryMeter extends Meter {

    public ShareMemoryMeter() {
        this.getConnector();
    }

    public CIEXYZ[] measure(RGB[] rgbArray) {
        return connector.measure(rgbArray);
    }

    private int backgroundIndex = 0;

    private int blankIndex = 0;

    public static interface ShareMemoryInterface {

        public CIEXYZ[] measure(RGB[] rgbArray, int backgroundIndex, int blankIndex);

        public CIEXYZ[] measure(RGB[] rgbArray);
    }

    /**
   * �q��P��, ��w�I���M���⪺index. (GUI�ݩ|���䴩)
   * @param rgbArray RGB[]
   * @return CIEXYZ[]
   */
    public CIEXYZ[] measure2(RGB[] rgbArray) {
        return connector.measure(rgbArray, backgroundIndex, blankIndex);
    }

    private ShareMemoryInterface getConnector() {
        if (null == connector) {
            try {
                connector = (ShareMemoryInterface) Class.forName("vv.cms.lcd.calibrate.shm.ShareMemoryConnector").newInstance();
            } catch (ClassNotFoundException ex) {
                Logger.log.error("", ex);
            } catch (IllegalAccessException ex) {
                Logger.log.error("", ex);
            } catch (InstantiationException ex) {
                Logger.log.error("", ex);
            }
        }
        return connector;
    }

    private ShareMemoryInterface connector = null;

    /**
   * calibrate
   *
   */
    public void calibrate() {
        throw new UnsupportedOperationException();
    }

    /**
   * close
   *
   */
    public void close() {
        throw new UnsupportedOperationException();
    }

    /**
   * getCalibrationCount
   *
   * @return String
   */
    public String getCalibrationCount() {
        throw new UnsupportedOperationException();
    }

    /**
   * getCalibrationDescription
   *
   * @return String
   */
    public String getCalibrationDescription() {
        throw new UnsupportedOperationException();
    }

    /**
   * getLastCalibration
   *
   * @return String
   */
    public String getLastCalibration() {
        throw new UnsupportedOperationException();
    }

    /**
   * getType
   *
   * @return Instr
   */
    public Instr getType() {
        return Instr.Platform;
    }

    /**
   * isConnected
   *
   * @return boolean
   */
    public boolean isConnected() {
        throw new UnsupportedOperationException();
    }

    /**
   * setPatchIntensity
   *
   * @param patchIntensity PatchIntensity
   */
    public void setPatchIntensity(PatchIntensity patchIntensity) {
        throw new UnsupportedOperationException();
    }

    /**
   * setScreenType
   *
   * @param screenType ScreenType
   */
    public void setScreenType(ScreenType screenType) {
        throw new UnsupportedOperationException();
    }

    /**
   * triggerMeasurementInXYZ
   *
   * @return double[]
   */
    public double[] triggerMeasurementInXYZ() {
        throw new UnsupportedOperationException();
    }

    /**
   *
   * @return double[]
   * @deprecated
   */
    public double[] triggerMeasurementInSpectrum() {
        throw new UnsupportedOperationException();
    }

    public Spectra triggerMeasurementInSpectra() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        ShareMemoryMeter platformmeter = new ShareMemoryMeter();
    }

    public void setBlankIndex(int blankIndex) {
        this.blankIndex = blankIndex;
    }

    public void setBackgroundIndex(int backgroundIndex) {
        this.backgroundIndex = backgroundIndex;
    }
}
