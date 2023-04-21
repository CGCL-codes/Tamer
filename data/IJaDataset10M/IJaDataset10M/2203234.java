package com.quikj.application.web.talk.client.whiteboard;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import net.n3.nanoxml.IXMLElement;

/**
 * 
 * @author amit
 */
public class ImageObject implements WhiteBoardObjectInterface {

    private Canvas canvas;

    private URL url;

    private Image image = null;

    public ImageObject(Canvas canvas, Applet applet, IXMLElement node) throws WhiteBoardException, MalformedURLException {
        this.canvas = canvas;
        Enumeration e = node.enumerateAttributeNames();
        while (e.hasMoreElements() == true) {
            String attr_name = (String) e.nextElement();
            String attr_value = node.getAttribute(attr_name, null);
            if (attr_name.equals("url") == true) {
                try {
                    this.url = new URL(attr_value);
                } catch (MalformedURLException ex) {
                    if (attr_value.startsWith("http://") == false) {
                        this.url = new URL("http://" + attr_value);
                    }
                }
                image = applet.getImage(this.url);
                if (image == null) {
                    throw new WhiteBoardException("The image file could not be downloaded");
                }
            }
        }
        if (url == null) {
            throw new WhiteBoardException("Mandatory paramter(s) missing");
        }
    }

    /** Creates a new instance of TextObject */
    public ImageObject(Canvas canvas, Applet applet, String url) throws MalformedURLException, WhiteBoardException {
        this.canvas = canvas;
        try {
            this.url = new URL(url);
        } catch (MalformedURLException ex) {
            if (url.startsWith("http://") == false) {
                this.url = new URL("http://" + url);
            }
        }
        image = applet.getImage(this.url);
        if (image == null) {
            throw new WhiteBoardException("The image file could not be downloaded");
        }
    }

    public void draw() {
        Graphics g = canvas.getGraphics();
        g.drawImage(image, 0, 0, canvas);
    }

    public String format() {
        return "<image url=\"" + url.toString() + "\"/>\n";
    }
}
