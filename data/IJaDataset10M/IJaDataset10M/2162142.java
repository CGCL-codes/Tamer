package lu.fisch.structorizer.elements;

import java.util.Vector;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import lu.fisch.graphics.*;
import lu.fisch.utils.*;

public class Subqueue extends Element {

    public Subqueue() {
        super("");
    }

    public Subqueue(StringList _strings) {
        super(_strings);
    }

    public Vector children = new Vector();

    public Rect prepareDraw(Canvas _canvas) {
        Rect subrect = new Rect();
        rect.top = 0;
        rect.left = 0;
        rect.right = 0;
        rect.bottom = 0;
        if (children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                subrect = ((Element) children.get(i)).prepareDraw(_canvas);
                rect.right = Math.max(rect.right, subrect.right);
                rect.bottom += subrect.bottom;
            }
        } else {
            rect.right = 2 * Element.E_PADDING;
            FontMetrics fm = _canvas.getFontMetrics(Element.font);
            rect.bottom = fm.getHeight() + 2 * Math.round(Element.E_PADDING / 2);
        }
        return rect;
    }

    public void draw(Canvas _canvas, Rect _top_left) {
        Rect myrect;
        Rect subrect;
        Color drawColor = getColor();
        FontMetrics fm = _canvas.getFontMetrics(Element.font);
        Canvas canvas = _canvas;
        if (selected == true) {
            drawColor = Element.E_DRAWCOLOR;
        }
        rect = _top_left.copy();
        myrect = _top_left.copy();
        myrect.bottom = myrect.top;
        if (children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                subrect = ((Element) children.get(i)).prepareDraw(_canvas);
                myrect.bottom += subrect.bottom;
                if (i == children.size() - 1) {
                    myrect.bottom = _top_left.bottom;
                }
                ((Element) children.get(i)).draw(_canvas, myrect);
                myrect.top += subrect.bottom;
            }
        } else {
            rect = _top_left.copy();
            canvas.setBackground(drawColor);
            canvas.setColor(drawColor);
            myrect = _top_left.copy();
            canvas.fillRect(myrect);
            canvas.setColor(Color.BLACK);
            canvas.writeOut(_top_left.left + ((_top_left.right - _top_left.left) / 2) - (_canvas.stringWidth("∅") / 2), _top_left.top + ((_top_left.bottom - _top_left.top) / 2) + (fm.getHeight() / 2), "∅");
            canvas.drawRect(_top_left);
        }
    }

    public int getSize() {
        return children.size();
    }

    public int getIndexOf(Element _ele) {
        return children.indexOf(_ele);
    }

    public Element getElement(int _index) {
        return (Element) children.get(_index);
    }

    public void addElement(Element _element) {
        children.add(_element);
        _element.parent = this;
    }

    public void removeElement(Element _element) {
        children.removeElement(_element);
    }

    public void removeElement(int _index) {
        children.removeElement(children.get(_index));
    }

    @Override
    public Element selectElementByCoord(int _x, int _y) {
        Element res = super.selectElementByCoord(_x, _y);
        Element sel = null;
        for (int i = 0; i < children.size(); i++) {
            sel = ((Element) children.get(i)).selectElementByCoord(_x, _y);
            if (sel != null) {
                selected = false;
                res = sel;
            }
        }
        return res;
    }

    @Override
    public Element getElementByCoord(int _x, int _y) {
        Element res = super.getElementByCoord(_x, _y);
        Element sel = null;
        for (int i = 0; i < children.size(); i++) {
            sel = ((Element) children.get(i)).getElementByCoord(_x, _y);
            if (sel != null) {
                res = sel;
            }
        }
        return res;
    }

    public Element copy() {
        Element ele = new Subqueue();
        ele.setColor(this.getColor());
        for (int i = 0; i < children.size(); i++) {
            ((Subqueue) ele).addElement(((Element) children.get(i)).copy());
        }
        return ele;
    }
}
