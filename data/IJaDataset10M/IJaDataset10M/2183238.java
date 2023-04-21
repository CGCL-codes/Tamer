package com.quikj.application.web.talk.client.whiteboard;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Enumeration;
import net.n3.nanoxml.IXMLElement;
import com.quikj.application.web.talk.messaging.TalkMessageParser;

/**
 * 
 * @author amit
 */
public class TextObject implements WhiteBoardObjectInterface {

    private Color color = null;

    private Canvas canvas;

    private int x = -1;

    private int y = -1;

    private String text = null;

    /** Creates a new instance of TextObject */
    public TextObject(Canvas canvas, int x, int y, String text) {
        this.canvas = canvas;
        this.x = x;
        this.y = y;
        this.text = text;
        color = canvas.getGraphics().getColor();
    }

    public TextObject(Canvas canvas, IXMLElement node) throws WhiteBoardException {
        this.canvas = canvas;
        Enumeration e = node.enumerateAttributeNames();
        while (e.hasMoreElements() == true) {
            String attr_name = (String) e.nextElement();
            String attr_value = node.getAttribute(attr_name, null);
            if (attr_name.equals("label") == true) {
                text = attr_value;
            } else if (attr_name.equals("x") == true) {
                try {
                    x = Integer.parseInt(attr_value);
                } catch (NumberFormatException ex) {
                    throw new WhiteBoardException("attribute \"x\" does not have a numeric value");
                }
            } else if (attr_name.equals("y") == true) {
                try {
                    y = Integer.parseInt(attr_value);
                } catch (NumberFormatException ex) {
                    throw new WhiteBoardException("attribute \"y\" does not have a numeric value");
                }
            }
        }
        if ((text == null) || (x == -1) || (y == -1)) {
            throw new WhiteBoardException("Mandatory paramter(s) missing");
        }
    }

    public void draw() {
        Graphics g = canvas.getGraphics();
        g.setColor(color);
        g.drawString(text, x, y);
    }

    public String format() {
        return "<text label=\"" + TalkMessageParser.encodeXMLString(text) + "\" x=\"" + x + "\" y=\"" + y + "\"/>\n";
    }
}
