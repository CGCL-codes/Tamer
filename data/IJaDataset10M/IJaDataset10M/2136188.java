package demo;

import java.nio.ByteBuffer;
import de.telekom.laboratories.capture.Aquire;
import de.telekom.laboratories.capture.Device;
import de.telekom.laboratories.capture.VideoMode;
import java.util.EnumSet;
import static de.telekom.laboratories.capture.VideoMode.Format.LUMINACE_8;

/**
 * @author Michael Nischt
 * @version 0.1
 */
class FlyCapture extends Capture implements Aquire {

    private final Device camera;

    private final byte[] target;

    FlyCapture(int width, int height) {
        super(width, height);
        this.target = new byte[width * height];
        camera = Device.Registry.getLocalRegistry().getDevices()[0];
        final VideoMode mode = new VideoMode(width, height, LUMINACE_8, 100.0f);
        camera.connect(mode, (Aquire) this);
    }

    @Override
    public void capture(ByteBuffer buffer) {
        buffer.get(target);
    }

    @Override
    public void capture(byte[] image, EnumSet<Flip> flip) {
        final int width = getWidth(), height = getHeight();
        if (image.length != width * height) throw new IllegalArgumentException();
        camera.capture();
        copy(target, image, flip);
    }

    @Override
    public void dispose() {
        camera.disconnect();
    }

    @Override
    public boolean isDisposed() {
        return !camera.isConnected();
    }
}
