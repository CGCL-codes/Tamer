package org.geoforge.guillc.button;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import org.geoforge.lang.IShrObj;

public abstract class BtnAbs extends JButton implements IShrObj, MouseListener, Serializable {

    private void _makeModif() {
        super.setBorderPainted(false);
        super.setContentAreaFilled(false);
        super.addMouseListener(this);
    }

    protected BtnAbs() {
        super();
        this._makeModif();
    }

    protected BtnAbs(String strText) {
        super(strText);
    }

    protected BtnAbs(Action act) {
        super(act);
        this._makeModif();
    }

    protected BtnAbs(String strText, javax.swing.ImageIcon iin) {
        super(strText, iin);
    }

    protected BtnAbs(ActionListener[] alrs, String strToolTipText) {
        super();
        if (alrs != null) {
            for (int i = 0; i < alrs.length; i++) {
                if (alrs[i] != null) super.addActionListener(alrs[i]);
            }
        }
        if (strToolTipText != null) {
            super.setToolTipText(strToolTipText);
        }
        this._makeModif();
    }

    protected BtnAbs(Action act, String strToolTipText) {
        super(act);
        if (strToolTipText != null) super.setToolTipText(strToolTipText);
        this._makeModif();
    }

    protected BtnAbs(Action act, String strToolTipText, String strText, javax.swing.ImageIcon iin) {
        super(strText, iin);
        super.setAction(act);
        if (strToolTipText != null) {
            super.setToolTipText(strToolTipText);
        }
        this._makeModif();
    }

    protected BtnAbs(ActionListener alr, String strToolTipText) {
        super();
        this._addActionListener_(alr);
        if (strToolTipText != null) super.setToolTipText(strToolTipText);
        super.setBorderPainted(false);
        super.setContentAreaFilled(false);
        super.addMouseListener(this);
    }

    private void _addActionListener_(ActionListener alr) {
        if (alr != null) super.addActionListener(alr);
    }

    protected BtnAbs(ActionListener alr) {
        super();
        this._addActionListener_(alr);
        super.setBorderPainted(false);
        super.setContentAreaFilled(false);
        super.addMouseListener((MouseListener) this);
    }

    protected BtnAbs(String strText, String strToolTip, ActionListener actListenerParent) {
        this(strText, strToolTip);
        this._actListenerParent_ = actListenerParent;
        if (this._actListenerParent_ != null) addActionListener(this._actListenerParent_);
    }

    protected BtnAbs(String strText, String strToolTip) {
        super();
        if (strText != null) setText(strText);
        if (strToolTip != null) setToolTipText(strToolTip);
    }

    @Override
    public void mouseReleased(MouseEvent evtMouse) {
    }

    @Override
    public void mousePressed(MouseEvent evtMouse) {
    }

    @Override
    public void mouseClicked(MouseEvent evtMouse) {
    }

    @Override
    public void mouseEntered(MouseEvent evtMouse) {
        if (super.isEnabled()) {
            setBorderPainted(true);
            setContentAreaFilled(true);
        }
    }

    @Override
    public void mouseExited(MouseEvent evtMouse) {
        setBorderPainted(false);
        setContentAreaFilled(false);
    }

    @Override
    public void destroy() {
        this.setAction(null);
        super.removeMouseListener(this);
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void setAction(Action act) {
        Icon icnDefault = super.getIcon();
        super.setAction(act);
        if (act == null) super.setEnabled(false);
        if (icnDefault != null) super.setIcon(icnDefault);
    }

    private ActionListener _actListenerParent_ = null;
}
