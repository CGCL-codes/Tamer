package com.gempukku.animator.showcase.tutorials.tutorial1;

import com.gempukku.animator.showcase.tutorials.TutorialDisplayPanel;
import java.awt.Dimension;

public class Panel4 extends TutorialDisplayPanel {

    public Panel4(Dimension dimension) {
        super(dimension);
        addNormalTextLabel(780, "What does it do? Well - to be honest - nothing.", 0);
        addNormalTextLabel(780, "But read on...", 3000);
    }
}
