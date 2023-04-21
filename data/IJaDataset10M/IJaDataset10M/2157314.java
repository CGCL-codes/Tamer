package javax.media.nativewindow.awt;

import javax.media.nativewindow.*;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.image.ColorModel;
import javax.media.nativewindow.AbstractGraphicsConfiguration;
import jogamp.nativewindow.Debug;

/** A wrapper for an AWT GraphicsConfiguration allowing it to be
    handled in a toolkit-independent manner. */
public class AWTGraphicsConfiguration extends DefaultGraphicsConfiguration implements Cloneable {

    private GraphicsConfiguration config;

    AbstractGraphicsConfiguration encapsulated;

    public AWTGraphicsConfiguration(AWTGraphicsScreen screen, CapabilitiesImmutable capsChosen, CapabilitiesImmutable capsRequested, GraphicsConfiguration config, AbstractGraphicsConfiguration encapsulated) {
        super(screen, capsChosen, capsRequested);
        this.config = config;
        this.encapsulated = encapsulated;
    }

    public AWTGraphicsConfiguration(AWTGraphicsScreen screen, CapabilitiesImmutable capsChosen, CapabilitiesImmutable capsRequested, GraphicsConfiguration config) {
        super(screen, capsChosen, capsRequested);
        this.config = config;
        this.encapsulated = null;
    }

    /**
   * @param capsChosen if null, <code>capsRequested</code> is copied and aligned 
   *        with the graphics capabilties of the AWT Component to produce the chosen Capabilties.
   *        Otherwise the <code>capsChosen</code> is used.
   */
    public static AWTGraphicsConfiguration create(Component awtComp, CapabilitiesImmutable capsChosen, CapabilitiesImmutable capsRequested) {
        AWTGraphicsScreen awtScreen = null;
        AWTGraphicsDevice awtDevice = null;
        GraphicsDevice awtGraphicsDevice = null;
        GraphicsConfiguration awtGfxConfig = awtComp.getGraphicsConfiguration();
        if (null != awtGfxConfig) {
            awtGraphicsDevice = awtGfxConfig.getDevice();
            if (null != awtGraphicsDevice) {
                awtDevice = new AWTGraphicsDevice(awtGraphicsDevice, AbstractGraphicsDevice.DEFAULT_UNIT);
                awtScreen = new AWTGraphicsScreen(awtDevice);
            }
        }
        if (null == awtScreen) {
            awtScreen = (AWTGraphicsScreen) AWTGraphicsScreen.createScreenDevice(-1, AbstractGraphicsDevice.DEFAULT_UNIT);
            awtDevice = (AWTGraphicsDevice) awtScreen.getDevice();
            awtGraphicsDevice = awtDevice.getGraphicsDevice();
        }
        if (null == capsChosen) {
            GraphicsConfiguration gc = awtGraphicsDevice.getDefaultConfiguration();
            capsChosen = setupCapabilitiesRGBABits(capsChosen, gc);
        }
        return new AWTGraphicsConfiguration(awtScreen, capsChosen, capsRequested, awtGfxConfig);
    }

    @Override
    public Object clone() {
        return super.clone();
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        return config;
    }

    @Override
    public AbstractGraphicsConfiguration getNativeGraphicsConfiguration() {
        return (null != encapsulated) ? encapsulated : this;
    }

    /**
   * Sets up the Capabilities' RGBA size based on the given GraphicsConfiguration's ColorModel.
   *
   * @param capabilities the Capabilities object whose red, green, blue, and alpha bits will be set
   * @param gc the GraphicsConfiguration from which to derive the RGBA bit depths
   * @return the passed Capabilities
   */
    public static CapabilitiesImmutable setupCapabilitiesRGBABits(CapabilitiesImmutable capabilitiesIn, GraphicsConfiguration gc) {
        Capabilities capabilities = (Capabilities) capabilitiesIn.cloneMutable();
        ColorModel cm = gc.getColorModel();
        if (null == cm) {
            throw new NativeWindowException("Could not determine AWT ColorModel");
        }
        int cmBitsPerPixel = cm.getPixelSize();
        int bitsPerPixel = 0;
        int[] bitesPerComponent = cm.getComponentSize();
        if (bitesPerComponent.length >= 3) {
            capabilities.setRedBits(bitesPerComponent[0]);
            bitsPerPixel += bitesPerComponent[0];
            capabilities.setGreenBits(bitesPerComponent[1]);
            bitsPerPixel += bitesPerComponent[1];
            capabilities.setBlueBits(bitesPerComponent[2]);
            bitsPerPixel += bitesPerComponent[2];
        }
        if (bitesPerComponent.length >= 4) {
            capabilities.setAlphaBits(bitesPerComponent[3]);
            bitsPerPixel += bitesPerComponent[3];
        } else {
            capabilities.setAlphaBits(0);
        }
        if (Debug.debugAll()) {
            if (cmBitsPerPixel != bitsPerPixel) {
                System.err.println("AWT Colormodel bits per components/pixel mismatch: " + bitsPerPixel + " != " + cmBitsPerPixel);
            }
        }
        return capabilities;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + getScreen() + ",\n\tchosen    " + capabilitiesChosen + ",\n\trequested " + capabilitiesRequested + ",\n\t" + config + ",\n\tencapsulated " + encapsulated + "]";
    }
}
