package com.pcmsolutions.device.EMU.E4.events.preset;

public abstract class LinkEvent extends PresetEvent {

    private Integer link;

    public LinkEvent(Object source, Integer preset, Integer link) {
        super(source, preset);
        this.link = link;
    }

    public Integer getLink() {
        return link;
    }
}
