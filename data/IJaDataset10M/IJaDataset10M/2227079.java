package jtrackbase.gui;

import jtrackbase.db.Medium;
import jtrackbase.db.MediumTypeFacade;

public class SamplerPanel extends MediumPanel {

    public SamplerPanel() {
        super(MediumTypeFacade.getSamplerType());
    }

    public SamplerPanel(Medium m) {
        super(m);
    }
}
