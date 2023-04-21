package com.breaktrycatch.lib.display;

import java.awt.Rectangle;
import processing.core.PApplet;
import processing.core.PImage;
import com.breaktrycatch.needmorehumans.utils.LogRepository;

public class ImageFrame extends DisplayObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected PImage _img;

    private boolean errorTint;

    public ImageFrame(PApplet app, PImage img) {
        super(app);
        _img = img;
        width = _img.width;
        height = _img.height;
        errorTint = false;
    }

    public ImageFrame(PApplet app) {
        super(app);
    }

    public void errorTint() {
        errorTint = true;
    }

    public void regularTint() {
        errorTint = false;
    }

    @Override
    public void draw() {
        super.draw();
        if (_img != null && visible) {
            if (errorTint == true) {
                _renderTarget.tint(0xFFFF6666);
            }
            _renderTarget.image(_img, 0, 0);
        }
    }

    public PImage getDisplay() {
        return _img;
    }

    public void setImage(PImage imng) {
        _img = imng;
    }
}
