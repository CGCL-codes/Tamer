package jist.swans.radio;

import jist.swans.field.FieldInterface;
import jist.swans.mac.MacInterface;
import jist.swans.misc.Message;
import jist.swans.Constants;
import jist.runtime.JistAPI;

/** 
 * <code>RadioNoise</code> is an abstract class which implements some functionality
 * that is common to the independent and additive radio noise simulation models.
 *
 * @author Rimon Barr &lt;barr+jist@cs.cornell.edu&rt;
 * @version $Id: RadioNoise.java,v 1.1 2006/10/21 00:04:04 lmottola Exp $
 * @since SWANS1.0
 */
public abstract class RadioNoise implements RadioInterface {

    /**
   * radio properties.
   */
    protected RadioInfo radioInfo;

    /**
   * radio mode: IDLE, SENSING, RECEIVING, SENDING, SLEEP.
   */
    protected byte mode;

    /**
   * message being received.
   */
    protected Message signalBuffer;

    /**
   * end of transmission time.
   */
    protected long signalFinish;

    /**
   * transmission signal strength.
   */
    protected double signalPower_mW;

    /**
   * number of signals being received.
   */
    protected int signals;

    /**
   * field entity downcall reference.
   */
    protected FieldInterface fieldEntity;

    /**
   * self-referencing radio entity reference.
   */
    protected RadioInterface self;

    /**
   * mac entity upcall reference.
   */
    protected MacInterface macEntity;

    /**
   * Create a new radio.
   *
   * @param id radio identifier
   * @param sharedInfo shared radio properties
   */
    protected RadioNoise(int id, RadioInfo.RadioInfoShared sharedInfo) {
        mode = Constants.RADIO_MODE_IDLE;
        radioInfo = new RadioInfo(new RadioInfo.RadioInfoUnique(), sharedInfo);
        radioInfo.unique.id = new Integer(id);
        unlockSignal();
        signals = 0;
        this.self = (RadioInterface) JistAPI.proxy(this, RadioInterface.class);
    }

    /**
   * Return self-referencing radio entity reference.
   *
   * @return self-referencing radio entity reference
   */
    public RadioInterface getProxy() {
        return this.self;
    }

    /**
   * Set upcall field entity reference.
   *
   * @param fieldEntity upcall field entity reference
   */
    public void setFieldEntity(FieldInterface fieldEntity) {
        if (!JistAPI.isEntity(fieldEntity)) throw new IllegalArgumentException("entity expected");
        this.fieldEntity = fieldEntity;
    }

    /**
   * Set downcall mac entity reference.
   *
   * @param macEntity downcall mac entity reference
   */
    public void setMacEntity(MacInterface macEntity) {
        if (!JistAPI.isEntity(macEntity)) throw new IllegalArgumentException("entity expected");
        this.macEntity = macEntity;
    }

    /**
   * Return radio properties.
   *
   * @return radio properties
   */
    public RadioInfo getRadioInfo() {
        return radioInfo;
    }

    /**
   * Set radio mode. Also notifies mac entity.
   *
   * @param mode radio mode
   */
    public void setMode(byte mode) {
        if (this.mode != mode) {
            this.mode = mode;
            this.macEntity.setRadioMode(mode);
        }
    }

    /**
   * Turn radio off (sleep) or on.
   *
   * @param sleep whether to turn off radio
   */
    public void setSleepMode(boolean sleep) {
        setMode(sleep ? Constants.RADIO_MODE_SLEEP : Constants.RADIO_MODE_IDLE);
    }

    /**
   * Lock onto current packet signal.
   *
   * @param msg packet currently on the air
   * @param power_mW signal power (units: mW)
   * @param duration time to EOT (units: simtime)
   */
    protected void lockSignal(Message msg, double power_mW, long duration) {
        signalBuffer = msg;
        signalPower_mW = power_mW;
        signalFinish = JistAPI.getTime() + duration;
        this.macEntity.peek(msg);
    }

    /**
   * Unlock from current packet signal.
   */
    protected void unlockSignal() {
        signalBuffer = null;
        signalPower_mW = 0;
        signalFinish = -1;
    }

    /** {@inheritDoc} */
    public void transmit(Message msg, long delay, long duration) {
        if (mode == Constants.RADIO_MODE_SLEEP) return;
        if (mode == Constants.RADIO_MODE_TRANSMITTING) throw new RuntimeException("radio already transmitting");
        signalBuffer = null;
        if (delay == Constants.RADIO_NOUSER_DELAY) delay = Constants.RADIO_PHY_DELAY;
        setMode(Constants.RADIO_MODE_TRANSMITTING);
        JistAPI.sleep(delay);
        fieldEntity.transmit(radioInfo, msg, duration);
        JistAPI.sleep(duration);
        self.endTransmit();
    }

    /** {@inheritDoc} */
    public void endTransmit() {
        if (mode == Constants.RADIO_MODE_SLEEP) return;
        if (mode != Constants.RADIO_MODE_TRANSMITTING) throw new RuntimeException("radio is not transmitting");
        setMode(signals > 0 ? Constants.RADIO_MODE_RECEIVING : Constants.RADIO_MODE_IDLE);
    }
}
