package scene;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.media.opengl.GL;

public class CameraWalkMouseHandler extends MouseHandler {

    private boolean droite = false, gauche = false, haut = false, bas = false;

    private CameraWalk camera;

    private int ancMouseX, ancMouseY;

    public CameraWalkMouseHandler() {
    }

    public CameraWalkMouseHandler(CameraWalk _camera) {
        this.setCamera(_camera);
    }

    /**
	 * @return the camera
	 */
    private CameraWalk getCamera() {
        return camera;
    }

    /**
	 * @param camera the camera to set
	 */
    private void setCamera(CameraWalk camera) {
        this.camera = camera;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.camera.setRo_step((float) (this.camera.DEUXPI / 360 * 0.07 * this.camera.getGlscene3d().fps));
        this.camera.setTheta_step((float) (this.camera.DEUXPI / 360 * 0.07 * this.camera.getGlscene3d().fps));
        if (e.isShiftDown() && e.isControlDown()) {
            int directionX = (int) Math.signum(e.getX() - ancMouseX);
            int directionY = (int) Math.signum(e.getY() - ancMouseY);
            if (directionY < 0) {
                haut = true;
                bas = false;
            }
            if (directionX > 0) {
                gauche = false;
                droite = true;
            }
            if (directionY > 0) {
                bas = true;
                haut = false;
            }
            if (directionX < 0) {
                droite = false;
                gauche = true;
            }
            if (haut) getCamera().incRho(-1);
            if (bas) getCamera().incRho(1);
            if (gauche) getCamera().incTheta(-1);
            if (droite) getCamera().incTheta(1);
            if (haut | gauche | bas | droite) {
                getCamera().majLookAtX();
                getCamera().majLookAtY();
                getCamera().majLookAtZ();
                droite = bas = haut = gauche = false;
            }
            ancMouseX = e.getX();
            ancMouseY = e.getY();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        if (rotation > 0) mouseWheelDown(e); else if (rotation < 0) mouseWheelUp(e);
    }

    public void mouseWheelDown(MouseEvent arg0) {
        if (arg0.isShiftDown()) {
            this.getCamera().goToBackward();
        }
    }

    public void mouseWheelUp(MouseEvent arg0) {
        if (arg0.isShiftDown()) {
            this.getCamera().goToForward();
        }
    }
}
