package gui;

import guitar.Application;

/**
 *
 * @author martin
 */
public class PositionBox extends ScrollBox {

    public PositionBox(Application app) {
        super();
    }

    public String getText() {
        return Integer.toString(app.getPatternPosition());
    }

    public void previous() {
        int _value = app.getPatternPosition();
        if (_value == 1) app.setPatternPosition(app.getNumberScalePositions()); else app.setPatternPosition(_value - 1);
    }

    public void next() {
        int _value = app.getPatternPosition();
        if (_value >= app.getNumberScalePositions()) app.setPatternPosition(1); else app.setPatternPosition(_value + 1);
    }

    public int getNumberChoices() {
        return app.getNumberScalePositions();
    }
}
