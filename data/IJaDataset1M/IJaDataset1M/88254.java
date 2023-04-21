package jogamp.nativewindow;

import javax.media.nativewindow.*;

public class DefaultGraphicsConfigurationFactoryImpl extends GraphicsConfigurationFactory {

    protected AbstractGraphicsConfiguration chooseGraphicsConfigurationImpl(CapabilitiesImmutable capsChosen, CapabilitiesImmutable capsRequested, CapabilitiesChooser chooser, AbstractGraphicsScreen screen) {
        return new DefaultGraphicsConfiguration(screen, capsChosen, capsRequested);
    }
}
