package com.breaktrycatch.needmorehumans.control.display;

import java.awt.Rectangle;
import java.text.DecimalFormat;
import processing.core.PApplet;
import com.breaktrycatch.lib.display.DisplayObject;
import com.breaktrycatch.lib.display.ImageFrame;
import com.breaktrycatch.lib.display.TextField;
import com.breaktrycatch.needmorehumans.view.GameView;

public class HeightMarker extends DisplayObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private DecimalFormat _formatter;

    private ImageFrame _background;

    private TextField _text;

    private Rectangle _bounds;

    public HeightMarker(PApplet app) {
        super(app);
        _formatter = new DecimalFormat("##0.00");
        _background = new ImageFrame(app, app.loadImage("../data/world/height-marker-background.png"));
        _background.x = -_background.width / 2 + 20;
        _background.y = -_background.height / 2;
        _text = new TextField(app);
        _text.setFont(app.loadFont("../data/fonts/AnonimRound-48.vlw"));
        _text.setColor(0x000000);
        _text.y = 10;
        _text.x = -5;
        add(_background);
        add(_text);
    }

    public void setBounds(Rectangle rect) {
        _bounds = rect;
    }

    @Override
    public void draw() {
        _text.setText(String.valueOf(_formatter.format(getDisplayValue() / GameView.HEIGHT_DIVISOR)) + "M");
    }

    public float getDisplayValue() {
        return _bounds.height - y;
    }
}
