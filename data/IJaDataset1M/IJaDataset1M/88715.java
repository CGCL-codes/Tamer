package org.chargecar.honda.halleffect;

import org.chargecar.honda.StreamingSerialPortDeviceController;

/**
 * <p>
 * <code>HallEffectController</code> is the MVC controller class for the {@link HallEffectModel} and {@link HallEffectView}.
 * </p>
 *
 * @author Chris Bartley (bartley@cmu.edu)
 */
public final class HallEffectController extends StreamingSerialPortDeviceController<HallEffectEvent, HallEffectEvent> {

    private final HallEffectModel model;

    public static HallEffectController create(final String serialPortName, final HallEffectModel model) {
        final HallEffectReader reader;
        final String deviceName;
        if (StreamingSerialPortDeviceController.shouldUseFakeDevice()) {
            deviceName = "Fake HallEffect";
            reader = new HallEffectReader(new FakeHallEffect());
        } else {
            deviceName = "HallEffect";
            if (serialPortName == null) {
                reader = null;
            } else {
                reader = new HallEffectReader(serialPortName);
            }
        }
        return new HallEffectController(deviceName, reader, model);
    }

    private HallEffectController(final String deviceName, final HallEffectReader reader, final HallEffectModel model) {
        super(deviceName, reader, model);
        this.model = model;
    }
}
