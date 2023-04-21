package com.cameocontrol.cameo.control;

public class CameoCue extends BasicCue {

    public String getIDValue() {
        return "cameo";
    }

    public CameoCue() {
    }

    public CameoCue(String d) {
        super(d);
    }

    public CameoCue(ConsoleCue cue) {
        super(cue);
    }
}
