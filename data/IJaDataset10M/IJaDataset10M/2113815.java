package net.sf.janalogtv.demo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.sf.janalogtv.AnalogTV;
import net.sf.janalogtv.Input;
import net.sf.janalogtv.Reception;
import net.sf.janalogtv.util.AppBase;

/**
 * A demo application showing how to show an image with AnalogTV.
 * It also shows how AnalogTV can simulate some defects present in old TV sets.
 */
public class Picture extends AppBase {

    private Input input;

    private Reception reception;

    private long startTime;

    /**
	 * Create a new instance of the Picture demo application.
	 */
    public Picture() {
        super("Picture");
        input = new Input();
        reception = new Reception(input, 0.7);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void initialize(AnalogTV tv) {
        input.setupSync(true, false);
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("picture.jpg"));
            input.drawImage(image);
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
        reception.setMultipath(0.5);
        tv.setBrightnessControl(0.05);
        tv.setContrastControl(1.0);
        tv.setColorControl(0.9);
        tv.setSquishControl(0.05);
        tv.setHorizDesync(4.0);
        tv.setFlutterHorizDesync(true);
        tv.setSqeezeBottom(5.0);
        startTime = System.currentTimeMillis();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void updateFrame(AnalogTV tv) {
        input.setupTeletext();
        reception.update();
        tv.setPowerup((System.currentTimeMillis() - startTime) / 1000.0);
        tv.initSignal(0.01);
        tv.addSignal(reception);
    }

    /**
	 * Program entry point.
	 * @param args command line arguments.
	 */
    public static void main(String[] args) {
        Picture demo = new Picture();
        demo.run();
    }
}
