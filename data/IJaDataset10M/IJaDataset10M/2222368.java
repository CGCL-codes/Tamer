package claw.coloureyes;

import java.util.Observable;
import java.util.Observer;

/**
 * FakeImageDataProvider provides a ImageDataProvider implementation that
 * returns a fake date buffer. This is used along with a BitmapPixelSelection
 * strategy to triffer the colour pipeline for static bitmap data rather data an
 * ever changing byte[] buffer coming from hardware. This implemention observes
 * a set of Observables, when one of more indicate change, the fake data buffer
 * is sent, triggering the colour processing pipeline.
 * 
 */
public class FakeImageDataProvider implements ImageDataProvider, Observer {

    /** fake data buffer sent when any of the observed models changes */
    private static final byte[] FAKE_BUFFER = new byte[] { 0 };

    /** called when any of the observed models changes */
    private ImageDataCallback mImageDataCallback;

    /**
	 * Constructs a new instance of the FakeImageDataProvider class.
	 * 
	 * @param modelsToObserve
	 *            list of models to be observed for change, triggering the fake
	 *            data buffer to be sent
	 */
    public FakeImageDataProvider(Observable[] modelsToObserve) {
        for (Observable o : modelsToObserve) {
            o.addObserver(this);
        }
    }

    public void setImageDataCallback(ImageDataCallback imageDataCallback) {
        mImageDataCallback = imageDataCallback;
    }

    public void update(Observable arg0, Object arg1) {
        if (mImageDataCallback != null) {
            mImageDataCallback.onImageDataReceived(FAKE_BUFFER, PixelDataFormat.UNSPECIFIED, 1, 1);
        }
    }
}
