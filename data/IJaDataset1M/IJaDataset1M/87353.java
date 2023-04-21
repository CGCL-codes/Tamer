package edu.brook.cv.paper;

import org.ascape.model.event.ScapeEvent;

public class CVModelIRun3and4and5 extends CVModelI {

    /**
     * 
     */
    private static final long serialVersionUID = -3581711081946699315L;

    public void createScape() {
        super.createScape();
        copVision = 7.0;
        personVision = 7.0;
        initialCopDensity = .074;
        jailTerm = Integer.MAX_VALUE;
    }

    public void scapeSetup(ScapeEvent scapeEvent) {
        super.scapeSetup(scapeEvent);
        setLegitimacy(0.90);
    }

    public void scapeIterated(ScapeEvent event) {
        super.scapeIterated(event);
    }
}
