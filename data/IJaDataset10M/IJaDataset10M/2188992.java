package org.oobench.patterns.mvc;

import org.oobench.patterns.mvc.common.*;

public class DumbColorsView extends View {

    private String name;

    int drawCount;

    DumbColorsView(Model aModel, String aName) {
        super(aModel);
        name = aName;
    }

    public Controller makeController() {
        return new ColorsController(this);
    }

    public void draw() {
        ++drawCount;
    }

    int getDrawCount() {
        return drawCount;
    }
}
