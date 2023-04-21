package org.luizzeross.jwaterluma;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author luiz.rsi
 */
public class JWLTheme implements Serializable, Cloneable {

    private Color background;

    private Color foreground;

    private String message;

    private String imageURL;

    private String name;

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHTMLMessage() {
        String html = "<html><body><center>";
        if (!getMessage().isEmpty()) {
            html = html + "<h1>" + getMessage() + "</h1>";
            if (!getImageURL().isEmpty()) html = html + "<br>";
        }
        if (!getImageURL().isEmpty()) {
            html = html + "<img src='" + getImageURL() + "' />";
        }
        html = html + "</center></body></html>";
        System.out.println("Message updated: " + html);
        return html;
    }

    public JWLTheme() {
        background = Color.WHITE;
        foreground = Color.BLUE;
        message = new String(JWaterLuma.DEFAULT_MESSAGE);
        imageURL = new String(JWaterLuma.URL_DEFAULT_APP_SPLASH_ICON);
    }

    @Override
    public String toString() {
        return name;
    }
}
