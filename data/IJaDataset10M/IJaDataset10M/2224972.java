package cn.myapps.core.workflow.element;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Enumeration;

public class TerminateNode extends Node {

    public TerminateNode(FlowDiagram owner) {
        super(owner);
    }

    public void paint(OGraphics g) {
        _img = _owner.getImageResource("terminate.gif");
        Color old = this.bgcolor;
        if (_owner.isCurrentToEdit(this)) {
            bgcolor = DEF_CURREDITCOLOR;
        }
        if (_owner.isCurrentSelected(this)) {
            bgcolor = DEF_SELECTEDCOLOR;
        }
        for (Enumeration e = _subelems.elements(); e.hasMoreElements(); ) {
            Object te = e.nextElement();
            if (te instanceof PaintElement) {
                PaintElement se = (PaintElement) te;
                se.paint(g);
            }
        }
        resize();
        g.setColor(bgcolor);
        g.fillRect(this.x, this.y, this.width, this.height);
        g.drawImage(_img, _imgrect.x, _imgrect.y, _imgrect.width, _imgrect.height, null, this._owner);
        if (this.name != null) {
            java.awt.FontMetrics fm = _owner.getFontMetrics(font);
            int tx = (int) (_txtrect.x + (_txtrect.width - fm.stringWidth(name)) / 2);
            int ty = (int) (_txtrect.y + 2 * _txtrect.height);
            if (this._iscurrent) {
                g.drawImage(_owner.getImageResource("terminate.gif"), _txtrect.x, _txtrect.y, _txtrect.width + 30, 10 + _txtrect.height, null, this._owner);
            } else {
                g.drawImage(_owner.getImageResource("background.gif"), _txtrect.x, _txtrect.y, _txtrect.width + 30, 10 + _txtrect.height, null, this._owner);
            }
            g.setColor(java.awt.Color.black);
            g.drawString(name, tx + 13 + this.name.length(), ty - 10);
        }
        this.bgcolor = old;
    }

    public void paintMobile(OGraphics g) {
        _img = _owner.getImageResource("terminate_m.gif");
        Color old = this.bgcolor;
        if (_owner.isCurrentToEdit(this)) {
            bgcolor = DEF_CURREDITCOLOR;
        }
        if (_owner.isCurrentSelected(this)) {
            bgcolor = DEF_SELECTEDCOLOR;
        }
        for (Enumeration e = _subelems.elements(); e.hasMoreElements(); ) {
            Object te = e.nextElement();
            if (te instanceof PaintElement) {
                PaintElement se = (PaintElement) te;
                se.paintMobile(g);
            }
        }
        resizeForMobile();
        g.setColor(bgcolor);
        g.fillRect(this.x, this.y, this.width, this.height);
        if (_iscurrent) {
            _img = _owner.getImageResource("current_m.gif");
        }
        g.drawImage(_img, _imgrect.x, _imgrect.y, _imgrect.width, _imgrect.height, null, this._owner);
        if (this.name != null) {
            java.awt.FontMetrics fm = _owner.getFontMetrics(font);
            g.setColor(java.awt.Color.black);
            g.drawString(name, _txtrect.x + name.length(), _txtrect.y + 30);
        }
        this.bgcolor = old;
    }

    public void showTips(Graphics g) {
        StringBuffer tips = new StringBuffer();
        tips.append(this.name);
        drawTips(g, tips.toString());
    }
}
