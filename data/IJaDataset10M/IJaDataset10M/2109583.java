package com.memoire.bu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import com.memoire.fu.FuFactoryInteger;

/**
 * A popup menu which follows action events and has simplified adding methods.
 */
public class BuPopupMenu extends JPopupMenu implements BuMenuInterface {

    private boolean sticky_;

    public BuPopupMenu() {
        this(null);
    }

    public BuPopupMenu(String _title) {
        this(BuPreferences.BU.getBooleanProperty("popupmenu.stickable", false), _title);
    }

    public BuPopupMenu(boolean _stickable) {
        this(_stickable, null);
    }

    public BuPopupMenu(boolean _stickable, String _title) {
        super(_title);
    }

    protected String _(String _s) {
        return BuResource.BU.getString(_s);
    }

    protected static final String __(String _s) {
        return BuResource.BU.getString(_s);
    }

    public boolean isSticky() {
        return sticky_;
    }

    public void setSticky(boolean _sticky) {
        sticky_ = _sticky;
    }

    public void actionPerformed(ActionEvent _evt) {
        Component c = getInvoker();
        if (c instanceof ActionListener) ((ActionListener) c).actionPerformed(_evt);
        for (Enumeration e = actionListeners_.elements(); e.hasMoreElements(); ) {
            ActionListener l = (ActionListener) e.nextElement();
            if (l != c) l.actionPerformed(_evt);
        }
    }

    private Vector actionListeners_ = new Vector();

    public void addActionListener(ActionListener _l) {
        actionListeners_.addElement(_l);
    }

    public void removeActionListener(ActionListener _l) {
        actionListeners_.removeElement(_l);
    }

    public BuSeparator addSeparator(String _text) {
        BuSeparator r = new BuSeparator(_text);
        this.add(r);
        return r;
    }

    public void addSubMenu(JMenu _m, boolean _enabled) {
        _m.setEnabled(_enabled);
        _m.addActionListener(this);
        this.add(_m);
    }

    public BuMenuItem addMenuItem(String _s, String _cmd, boolean _enabled) {
        return addMenuItem(_s, _cmd, null, _enabled, 0);
    }

    public BuMenuItem addMenuItem(String _s, String _cmd, Icon _icon, boolean _enabled) {
        return addMenuItem(_s, _cmd, _icon, _enabled, 0);
    }

    public BuMenuItem addMenuItem(String _s, String _cmd, Icon _icon, boolean _enabled, int _key) {
        Icon icon = _icon;
        if (icon == null) icon = BuResource.BU.loadMenuCommandIcon(_cmd);
        if ((icon instanceof BuIcon) && ((BuIcon) icon).isDefault()) icon = null;
        if (icon == null) icon = BuResource.BU.getMenuIcon("aucun");
        BuMenuItem r = new BuMenuItem();
        r.setName("mi" + _cmd);
        r.setActionCommand(_cmd);
        r.setText(_s);
        if (icon instanceof BuIcon) r.setIcon((BuIcon) icon); else r.setIcon(icon);
        r.setHorizontalTextPosition(SwingConstants.RIGHT);
        r.addActionListener(this);
        r.setEnabled(_enabled);
        if (_key != 0) {
            if ((_key >= KeyEvent.VK_F1) && (_key <= KeyEvent.VK_F12)) r.setAccelerator(KeyStroke.getKeyStroke(_key, InputEvent.ALT_MASK)); else r.setAccelerator(KeyStroke.getKeyStroke(_key, InputEvent.CTRL_MASK));
        }
        this.add(r);
        return r;
    }

    public BuCheckBoxMenuItem addCheckBox(String _s, String _cmd, boolean _enabled, boolean _checked) {
        return addCheckBox(_s, _cmd, null, _enabled, _checked);
    }

    public BuCheckBoxMenuItem addCheckBox(String _s, String _cmd, Icon _icon, boolean _enabled, boolean _checked) {
        Icon icon = _icon;
        if (icon == null) icon = BuResource.BU.loadMenuCommandIcon(_cmd);
        if ((icon instanceof BuIcon) && ((BuIcon) icon).isDefault()) icon = null;
        if (icon == null) icon = BuResource.BU.getMenuIcon("aucun");
        BuCheckBoxMenuItem r = new BuCheckBoxMenuItem();
        r.setText(_s);
        r.setName("cbmi" + _cmd);
        r.setActionCommand(_cmd);
        if (icon instanceof BuIcon) r.setIcon((BuIcon) icon); else r.setIcon(icon);
        r.setHorizontalTextPosition(SwingConstants.RIGHT);
        r.addActionListener(this);
        r.setEnabled(_enabled);
        r.setSelected(_checked);
        this.add(r);
        return r;
    }

    public BuRadioButtonMenuItem addRadioButton(String _s, String _cmd, boolean _enabled, boolean _checked) {
        return addRadioButton(_s, _cmd, null, _enabled, _checked);
    }

    private ButtonGroup group_;

    public BuRadioButtonMenuItem addRadioButton(String _s, String _cmd, Icon _icon, boolean _enabled, boolean _checked) {
        if (group_ == null) group_ = new ButtonGroup();
        Icon icon = _icon;
        if (icon == null) icon = BuResource.BU.loadMenuCommandIcon(_cmd);
        if ((icon instanceof BuIcon) && ((BuIcon) icon).isDefault()) icon = null;
        if (icon == null) icon = BuResource.BU.getMenuIcon("aucun");
        BuRadioButtonMenuItem r = new BuRadioButtonMenuItem();
        r.setText(_s);
        r.setName("rbmi" + _cmd);
        r.setActionCommand(_cmd);
        if (icon instanceof BuIcon) r.setIcon((BuIcon) icon); else r.setIcon(icon);
        r.setHorizontalTextPosition(SwingConstants.RIGHT);
        r.addActionListener(this);
        r.setEnabled(_enabled);
        r.setSelected(_checked);
        group_.add(r);
        this.add(r);
        return r;
    }

    public void computeMnemonics() {
        Component[] c = null;
        Hashtable t = null;
        try {
            c = getComponents();
        } catch (Exception ex) {
        }
        if (c == null) return;
        for (int i = 0; i < c.length; i++) {
            if (c[i] instanceof JMenuItem) {
                JMenuItem mi = (JMenuItem) c[i];
                int mn = mi.getMnemonic();
                if (mn <= 0) {
                    String tx = BuLib.candidateMnemonics(mi.getText());
                    if (tx != null) for (int j = 0; j < tx.length(); j++) {
                        mn = tx.charAt(j);
                        if (t == null) t = new Hashtable();
                        if (t.get(FuFactoryInteger.get(mn)) == null) {
                            t.put(FuFactoryInteger.get(mn), mi);
                            mi.setMnemonic(mn);
                            break;
                        }
                    }
                }
                if (mi instanceof BuMenu) ((BuMenu) mi).computeMnemonics();
            }
        }
    }
}
