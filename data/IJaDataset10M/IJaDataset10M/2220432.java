package eyetrackercalibrator.gui.util;

/**
 * This class help translate frames number between universal, eye and scene
 * @author ruj
 */
public class FrameTranslator {

    int eyeOffset = 0;

    int sceneOffset = 0;

    public FrameTranslator() {
    }

    public void setSynchronizePoint(int eyeOffset, int screenOffset) {
        if (eyeOffset > screenOffset) {
            this.eyeOffset = eyeOffset - screenOffset;
            this.sceneOffset = 0;
        } else {
            this.eyeOffset = 0;
            this.sceneOffset = screenOffset - eyeOffset;
        }
    }

    public int getEyeFrame(int currentUniversalFrame) {
        return currentUniversalFrame + this.eyeOffset;
    }

    public int getSceneFrame(int currentUniversalFrame) {
        return currentUniversalFrame + this.sceneOffset;
    }

    public int getEyeFrameFromSceneFrame(int sceneFrame) {
        return getEyeFrame(getUniversalFrameFromSceneFrame(sceneFrame));
    }

    public int getSceneFrameFromEyeFrame(int eyeFrame) {
        return getSceneFrame(getUniversalFrameFromEyeFrame(eyeFrame));
    }

    public int getUniversalFrameFromEyeFrame(int eyeFrame) {
        return eyeFrame - this.eyeOffset;
    }

    public int getUniversalFrameFromSceneFrame(int sceneFrame) {
        return sceneFrame - this.sceneOffset;
    }
}
