package src;

public class Camera extends BehavingThread implements Mappable {

    public volatile float fPosX = 0;

    public volatile float fPosY = 0;

    public volatile float fPosZ = 0;

    public volatile float fRotX = 0;

    public volatile float fRotY = 0;

    public volatile float fRotZ = 0;

    public volatile float fLookX = 0;

    public volatile float fLookY = 0;

    public volatile float fLookZ = 0;

    public volatile int iVelX = 0;

    public volatile int iVelY = 0;

    public volatile int iVelZ = 0;

    public volatile int iLookMapCellX = 0;

    public volatile int iLookMapCellZ = 0;

    public volatile boolean bMouseClicked = false;

    public volatile int iMouseClickedX = 0;

    public volatile int iMouseClickedY = 0;

    public Camera(float fpx, float fpy, float fpz, float frx, float fry, float frz) {
        fPosX = fpx;
        fPosY = fpy;
        fPosZ = fpz;
        fRotX = frx;
        fRotY = fry;
        fRotZ = frz;
        fLookX = fPosX;
        fLookZ = fPosZ - Mappable.iMapCellSize;
        remap(null);
    }

    public void remap(Map map) {
        iLookMapCellX = (int) fLookX / iMapCellSize;
        iLookMapCellZ = (int) fLookZ / iMapCellSize;
    }

    public void behave() {
        if (iVelX != 0 || iVelZ != 0 || iVelY != 0) {
            fPosX += iVelX;
            fPosY += iVelY;
            fPosZ += iVelZ;
            fLookX += iVelX;
            fLookZ += iVelZ;
            remap(null);
        }
    }
}
