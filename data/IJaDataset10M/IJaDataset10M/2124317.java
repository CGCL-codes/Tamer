package com.lonedev.gtroot.shared;

import java.awt.Color;

public class RocketModule {

    private Color moduleColour;

    public RocketModule(Color moduleColour) {
        this.moduleColour = moduleColour;
    }

    /**
     * @return the moduleColour
     */
    public Color getModuleColour() {
        return moduleColour;
    }

    /**
     * @param moduleColour the moduleColour to set
     */
    public void setModuleColour(Color moduleColour) {
        this.moduleColour = moduleColour;
    }
}
