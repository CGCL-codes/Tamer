package org.openremote.beehive.api.dto.modeler;

import org.openremote.beehive.domain.modeler.Slider;
import org.openremote.beehive.domain.modeler.SliderCommandRef;

@SuppressWarnings("serial")
public class SliderCommandRefDTO extends CommandRefItemDTO {

    private SliderDTO slider;

    public SliderCommandRefDTO() {
    }

    public SliderCommandRefDTO(SliderCommandRef commandRef, String deviceName) {
        if (commandRef != null) {
            setId(commandRef.getOid());
            setDeviceCommand(commandRef.getDeviceCommand().toDTO());
            setDeviceName(deviceName);
        }
    }

    public SliderDTO getSlider() {
        return slider;
    }

    public void setSlider(SliderDTO slider) {
        this.slider = slider;
    }

    public SliderCommandRef toSliderCommandRef(Slider slider) {
        SliderCommandRef sliderCommandRef = new SliderCommandRef(slider);
        if (getDeviceCommand() != null) {
            sliderCommandRef.setDeviceCommand(getDeviceCommand().toDeviceCommand());
        }
        return sliderCommandRef;
    }
}
