package cn.myapps.core.dynaform.dts.excelimport;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Enumeration;
import cn.myapps.core.dynaform.dts.excelimport.utility.CommonUtil;

public class DetailSheet extends AbstractSheet {

    private static final String HEAD_TEXT = "DetailSheet";

    private static final Color HEAD_COLOR = new Color(255, 128, 64);

    private static final Color TEXT_COLOR = new Color(255, 255, 255);

    private Rectangle _headRect;

    private Rectangle _textRect;

    public DetailSheet(ExcelMappingDiagram owner) {
        super(owner);
    }

    /**
	 * @param g
	 * @roseuid 3E043760021D
	 */
    public void paint(Graphics g) {
        if (_img == null) {
        }
        Color old = this.bgcolor;
        if (_owner.isCurrentToEdit(this)) {
            bgcolor = DEF_CURREDITCOLOR;
        }
        if (_owner.isCurrentSelected(this)) {
            bgcolor = DEF_SELECTEDCOLOR;
        }
        for (Enumeration e = _subelems.elements(); e.hasMoreElements(); ) {
            Element se = (Element) e.nextElement();
            if (!(se instanceof Node)) {
                se.paint(g);
            }
        }
        resize();
        g.setColor(HEAD_COLOR);
        g.fillRect(x, y, _headRect.width, _headRect.height);
        g.drawRect(x, y, getRect().width, getRect().height);
        java.awt.FontMetrics fm = _owner.getFontMetrics(font);
        int hx = x;
        int hy = (int) (y + _headRect.height / 2);
        g.setColor(java.awt.Color.black);
        g.drawString(HEAD_TEXT, hx + 2, hy + 2);
        g.setColor(TEXT_COLOR);
        g.fillRect(x, y + _headRect.height, _textRect.width, _textRect.height);
        int tx = x;
        int ty = (int) (y + _headRect.height + _textRect.height / 2);
        g.setColor(java.awt.Color.black);
        g.drawString(name, tx + 2, ty + 2);
        g.setColor(bgcolor);
        g.drawRect(x, y, getRect().width, getRect().height);
        this.bgcolor = old;
    }

    public void showTips(Graphics g) {
        StringBuffer tips = new StringBuffer();
        tips.append("hello master sheet");
        if (description != null && !description.trim().equals("") && !description.trim().equals("null")) {
            tips.append(CommonUtil.foldString("备注：" + description, 20));
            tips.append("\n");
        }
        drawTips(g, tips.toString());
    }

    public static void main(String[] args) {
    }

    public void resize() {
        java.awt.FontMetrics fm = _owner.getFontMetrics(font);
        _headRect = new Rectangle();
        _headRect.setBounds(x, y, fm.stringWidth(HEAD_TEXT) + fm.getDescent() * HEAD_TEXT.length() + 10, fm.getHeight() + 5);
        _textRect = new Rectangle();
        _textRect.setBounds(x, y + _headRect.height, fm.stringWidth(name) + fm.getDescent() * name.length() + 5, fm.getHeight() + 5);
    }

    public Rectangle getRect() {
        Rectangle r = new Rectangle(_headRect);
        if (_textRect != null) {
            r.add(_textRect);
        }
        return r;
    }
}
