package net.sf.runnable;

import net.sf.pii.Pii;
import net.sf.pii.domain.InfraRedPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import motejx.extensions.nunchuk.NunchukCalibrationData;
import motejx.extensions.nunchuk.Nunchuk;

/**
 * Created by IntelliJ IDEA.
 * User: jalieven
 * Date: Dec 6, 2008
 * Time: 4:46:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class PiiRunButtons {

    private static Logger log = LoggerFactory.getLogger(PiiRunButtons.class);

    public static void main(String[] args) throws InterruptedException {
        Pii pii = new Pii(null);
        pii.setLedStatus(new boolean[] { false, true, true, false });
        log.info("Extension connected: " + pii.isExtensionControllerConnected());
        log.info("BatteryBytes: " + pii.getBatteryLevel());
        while (true) {
            if (pii.isButtonBPressed()) {
                log.info("Button B pressed");
            }
        }
    }
}
