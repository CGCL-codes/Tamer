package net.sf.jlibdc1394.impl.cmu;

import net.sf.jlibdc1394.CameraException;
import net.sf.jlibdc1394.VideoModeController;

/**
 * 
 * @author     Alexander Bieber <fleque@users.sourceforge.net>
 */
public class TestCMU {

    public static void main(String[] args) {
        try {
            JDC1394CamCMU test = new JDC1394CamCMU();
            VideoModeController testModes = test.getVideoModes();
        } catch (CameraException e) {
            e.printStackTrace();
        }
    }
}
