package com.jdiv.input;

import com.jdiv.JDiv;
import com.jdiv.extensions.JFontImage;
import com.jdiv.util.JNumber;

/**
 * @author  Joyal
 */
public class JWrite {

    /**
 * @uml.property  name="id"
 */
    private int id;

    /**
 * @uml.property  name="x"
 */
    private int x;

    /**
 * @uml.property  name="y"
 */
    private int y;

    /**
 * @uml.property  name="file"
 */
    private int file;

    /**
 * @uml.property  name="align"
 */
    private int align;

    /**
 * @uml.property  name="text"
 */
    private String text;

    /**
 * @uml.property  name="num"
 * @uml.associationEnd  
 */
    private JNumber num;

    /**
 * @return
 * @uml.property  name="num"
 */
    public JNumber getNum() {
        return num;
    }

    /**
 * @param num
 * @uml.property  name="num"
 */
    public void setNum(JNumber num) {
        this.num = num;
    }

    public JWrite() {
        JDiv.writes.add(this);
        this.id = JDiv.writes.size() - 1;
    }

    public JWrite(int file, int x, int y, int align, String text) {
        JDiv.writes.add(this);
        this.id = JDiv.writes.size() - 1;
        this.file = file;
        this.x = x;
        this.y = y;
        this.align = align;
        this.text = text;
    }

    public JWrite(int file, int x, int y, int align, JNumber num) {
        JDiv.writes.add(this);
        this.id = JDiv.writes.size() - 1;
        this.file = file;
        this.x = x;
        this.y = y;
        this.align = align;
        this.num = num;
    }

    /**
 * @return
 * @uml.property  name="id"
 */
    public int getId() {
        return id;
    }

    /**
 * @return
 * @uml.property  name="file"
 */
    public int getFile() {
        return file;
    }

    /**
 * @param file
 * @uml.property  name="file"
 */
    public void setFile(int file) {
        this.file = file;
    }

    /**
 * @param id
 * @uml.property  name="id"
 */
    public void setId(int id) {
        this.id = id;
    }

    /**
 * @return
 * @uml.property  name="x"
 */
    public int getX() {
        return x;
    }

    /**
 * @param x
 * @uml.property  name="x"
 */
    public void setX(int x) {
        this.x = x;
    }

    /**
 * @return
 * @uml.property  name="y"
 */
    public int getY() {
        return y;
    }

    /**
 * @param y
 * @uml.property  name="y"
 */
    public void setY(int y) {
        this.y = y;
    }

    /**
 * @return
 * @uml.property  name="text"
 */
    public String getText() {
        if (num != null) text = "" + num.getNum();
        return text;
    }

    /**
 * @param text
 * @uml.property  name="text"
 */
    public void setText(String text) {
        this.text = text;
    }

    /**
 * @return
 * @uml.property  name="align"
 */
    public int getAlign() {
        return align;
    }

    /**
 * @param align
 * @uml.property  name="align"
 */
    public void setAlign(int align) {
        this.align = align;
    }

    public int lenght() {
        int ancho = 0;
        if (getFile() != 0) {
            for (int j = 0; j < text.length(); j++) {
                JFontImage font = JDiv.fnts.get(file - 1).getFont(text.charAt(j));
                ancho += font.getAncho() + 2;
            }
        } else {
            ancho = text.length() * 5;
        }
        return ancho;
    }

    public int getHeight() {
        int alto = 0;
        for (int j = 0; j < text.length(); j++) {
            JFontImage font = JDiv.fnts.get(file - 1).getFont(text.charAt(j));
            if (alto < font.getAlto()) alto = font.getAlto();
        }
        return alto;
    }

    public int maxVOffset() {
        int voffset = 0;
        for (int j = 0; j < text.length(); j++) {
            JFontImage font = JDiv.fnts.get(file - 1).getFont(text.charAt(j));
            if (voffset < font.getVoffset()) voffset = font.getVoffset();
        }
        return voffset;
    }
}
