package com.gempukku.animator.showcase.tutorials.tutorial2;

import com.gempukku.animator.showcase.tutorials.TutorialDisplayPanel;
import java.awt.Dimension;
import java.awt.Rectangle;

public class Panel5 extends TutorialDisplayPanel {

    public Panel5(Dimension dimension) {
        super(dimension);
        addNormalTextLabel(670, "Now we need to add it to the component and start the animation loop.", 0);
        addCodeTyping(670, "animatorComponent.addAnimated(new OvalAnimated());\nanimatorComponent.startAnimator();", 3000, 3500);
        embedExample(new Example1(), new Rectangle(680, 40, 50, 190), 9000);
    }
}
