package ICEImage;

/**
 * A listener interface used by the Camera to know when to send images
 * and to send them accordingly.
 *
 * Your application should implement this listener and send itself
 * to the Camera upon construction, or register itself after construction
 * using setCameraListener(CameraListener cameraListener) on Camera.
 **/
public interface CameraListener {

    /**
	 * Returns whether or not the listening application wants to receive the image.
	 **/
    public boolean requestImage();

    /**
	 * Returns whether or not the listening application has received the image.
	 **/
    public boolean hasReported();

    /**
	 * Used by the camera to send the image to the listening application.
	 **/
    public void sendImage(ColorImage image);
}
