package org.zkoss.canvas;

import org.zkoss.json.JSONAware;
import org.zkoss.json.JSONObject;

/**
 * @author simon
 *
 */
public class Text extends Drawable {

    private String _text;

    private double _x;

    private double _y;

    /**
	 * 
	 * @param text: string to paint
	 * @param x: X position
	 * @param y: Y position
	 */
    public Text(String text, double x, double y) {
        super();
        _text = text;
        _x = x;
        _y = y;
    }

    /**
	 * 
	 */
    public Text(Text text) {
        this(text._text, text._x, text._y);
    }

    /**
	 * 
	 */
    public double getX() {
        return _x;
    }

    /**
	 * 
	 */
    public double getY() {
        return _y;
    }

    /**
	 * 
	 */
    public String getText() {
        return _text;
    }

    /**
	 * 
	 */
    public Text setPosition(double x, double y) {
        _x = x;
        _y = y;
        return this;
    }

    /**
	 * 
	 */
    public Text setText(String text) {
        _text = text;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONAware getShapeJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("t", _text);
        obj.put("x", _x);
        obj.put("y", _y);
        return obj;
    }

    @Override
    public String getType() {
        return "text";
    }

    @Override
    public Object clone() {
        return new Text(this);
    }
}
