package shu.cms.lcd.calibrate.measured.find;

import shu.cms.colorspace.depend.*;
import shu.cms.colorspace.depend.RGBBase.*;
import shu.cms.colorspace.depend.DeviceDependentSpace.MaxValue;
import shu.cms.colorspace.independ.*;
import shu.cms.lcd.calibrate.measured.algo.*;
import shu.cms.measure.cp.*;

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
public abstract class CalibratorAccessAdapter extends AlogorithmAdapter implements CalibratorAccessIF {

    /**
   *
   * @param white CIEXYZ
   * @param cpm CPCodeMeasurement
   * @param jndi JNDIInterface
   * @param quadrant DeltauvQuadrant
   * @param maxCode double
   * @param forceTrigger boolean
   * @deprecated
   */
    public CalibratorAccessAdapter(CIEXYZ white, CPCodeMeasurement cpm, JNDIInterface jndi, DeltauvQuadrant quadrant, double maxCode, boolean forceTrigger) {
        super(white, cpm, jndi, quadrant, maxCode, forceTrigger);
    }

    public CalibratorAccessAdapter(CIEXYZ white, MeasureInterface mi, JNDIInterface jndi, DeltauvQuadrant quadrant, double maxCode, boolean forceTrigger) {
        super(white, mi, jndi, quadrant, maxCode, forceTrigger);
    }

    public CalibratorAccessAdapter(CIEXYZ white, CPCodeMeasurement cpm, JNDIInterface jndi) {
        super(white, cpm, jndi, false);
    }

    /**
   * trace
   *
   * @param msg String
   */
    public void trace(String msg) {
    }

    /**
   * getTargetxyY
   *
   * @param index int
   * @return CIExyY
   */
    public abstract CIExyY getTargetxyY(int index);

    /**
   * addMaxAroundTouched
   *
   */
    public void addMaxAroundTouched() {
    }

    /**
   * getIndexNearestAlogorithm
   *
   * @return NearestAlogorithm
   */
    public abstract NearestAlgorithm getIndexNearestAlogorithm();

    /**
   * getInitStep
   *
   * @return MaxValue
   */
    public MaxValue getInitStep() {
        return RGBBase.MaxValue.Int8Bit;
    }
}
