package sale;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
  * A MenuSheetItem that has a label and an associated action.
  *
  * @see Action
  * @see MenuSheet
  * @see MenuSheetSeparator
  *
  * @author Steffen Zschaler
  * @version 2.0 20/05/1999
  * @since v1.0
  */
public class MenuSheetItem extends MenuSheetObject implements ActionListener {

    /**
    * The JMenuItem peer.
    */
    protected transient JMenuItem m_jmiPeer = null;

    /**
    * The JMenu peer.
    */
    protected transient JMenu m_jmMenuPeer = null;

    /**
    * The monitor synchronizing access to the peers.
    */
    private transient Object m_oPeerLock;

    /**
    * Return the monitor used to synchronized access to the peers.
    *
    * @override Never
    */
    protected Object getPeerLock() {
        if (m_oPeerLock == null) {
            m_oPeerLock = new Object();
        }
        return m_oPeerLock;
    }

    /**
    * The action associated with this MenuSheetItem.
    *
    * @serial
    */
    protected Action m_aAction;

    /**
    * The monitor synchronizing accesses to m_aAction.
    */
    private transient Object m_oActionLock;

    /**
    * Return the monitor used to synchronized access to the m_aAction.
    *
    * @override Never
    */
    protected Object getActionLock() {
        if (m_oActionLock == null) {
            m_oActionLock = new Object();
        }
        return m_oActionLock;
    }

    /**
    * Can this MenuSheetItem be clicked?
    *
    * @serial
    */
    private boolean m_fEnabled = true;

    /**
    * The Images associated with the icons of this MenuSheetItem( [0]:DefaultImage, [1]:PressedImage,
    * [2]:DisabledImage, [3]:PressedDiabledImage ).
    *
    * @serial
    */
    protected Image m_aiImages[] = null;

    /**
    * The Mnemonic of this MenuSheetItem.
    *
    * @serial
    */
    protected char m_cMnemonic;

    /**
    * The KeyStroke of this MenuSheetItem.
    *
    * @serial
    */
    protected KeyStroke m_ksKeyStroke;

    private void writeObject(java.io.ObjectOutputStream oos) throws java.io.IOException {
        util.Debug.print("Starting to write menu sheet item: \"" + getCaption() + "\" <" + getTag() + ">", -1);
        oos.defaultWriteObject();
        util.Debug.print("Done writing menu sheet item: \"" + getCaption() + "\" <" + getTag() + ">", -1);
    }

    /**
    * Create a new MenuSheetItem with caption, tag, an action, and a mnemonic.
    *
    * @param sCaption the caption of the new MenuSheetItem.
    * @param sTag the tag that will identify this MenuSheetItem.
    * @param aAction the action to perform when this MenuSheetItem is selected.
    * @param cMnemonic the mnemonic of the new MenuSheetItem
    */
    public MenuSheetItem(String sCaption, String sTag, Action aAction, char cMnemonic) {
        super(sCaption, sTag);
        m_cMnemonic = cMnemonic;
        m_aAction = aAction;
    }

    /**
    * Create a new MenuSheetItem with caption, tag and action; initially enabled.
    *
    * @param sCaption the caption of the new MenuSheetItem.
    * @param sTag the tag that will identify this MenuSheetItem.
    * @param aAction the action to perform when this MenuSheetItem is selected.
    */
    public MenuSheetItem(String sCaption, String sTag, Action aAction) {
        this(sCaption, sTag, aAction, '\0');
    }

    /**
    * Create a new MenuSheetItem with caption and action. The MenuSheetItem will have
    * a default, unique tag.
    *
    * @param sCaption the caption of the new MenuSheetItem.
    * @param aAction the action to perform when this MenuSheetItem is selected.
    */
    public MenuSheetItem(String sCaption, Action aAction) {
        this(sCaption, null, aAction, '\0');
    }

    private void setIcon(ImageIcon iiImageIcon, int nIndex) {
        if (m_aiImages == null) m_aiImages = new Image[4];
        m_aiImages[nIndex] = iiImageIcon.getImage();
        synchronized (getPeerLock()) {
            if (m_jmiPeer != null) {
                switch(nIndex) {
                    case DEFAULT_IMAGE:
                        m_jmiPeer.setIcon(iiImageIcon);
                        break;
                    case SELECTED_IMAGE:
                        m_jmiPeer.setSelectedIcon(iiImageIcon);
                        break;
                    case DISABLED_IMAGE:
                        m_jmiPeer.setDisabledIcon(iiImageIcon);
                        break;
                    case DISABLED_SELECTED_IMAGE:
                        m_jmiPeer.setDisabledSelectedIcon(iiImageIcon);
                        break;
                }
            }
            if (m_jmMenuPeer != null) {
                switch(nIndex) {
                    case DEFAULT_IMAGE:
                        m_jmMenuPeer.getItem(0).setIcon(iiImageIcon);
                        break;
                    case SELECTED_IMAGE:
                        m_jmMenuPeer.getItem(0).setSelectedIcon(iiImageIcon);
                        break;
                    case DISABLED_IMAGE:
                        m_jmMenuPeer.getItem(0).setDisabledIcon(iiImageIcon);
                        break;
                    case DISABLED_SELECTED_IMAGE:
                        m_jmMenuPeer.getItem(0).setDisabledSelectedIcon(iiImageIcon);
                        break;
                }
            }
        }
    }

    /**
    * Set the caption of this MenuSheetItem. If the MenuSheetItem is already on display,
    * also the peer's caption is set.
    *
    * @override Never
    *
    * @param sCaption the new caption.
    */
    public void setCaption(String sCaption) {
        synchronized (getPeerLock()) {
            if (m_jmiPeer != null) {
                m_jmiPeer.setText(sCaption);
            }
            if (m_jmMenuPeer != null) {
                m_jmMenuPeer.setText(sCaption);
                m_jmMenuPeer.getItem(0).setText(sCaption);
            }
        }
        super.setCaption(sCaption);
    }

    /**
    * Set the enabled state of this MenuSheetItem. If the MenuSheetItem is already on
    * display, also the peer's enabled state is set.
    *
    * @override Never
    *
    * @param fEnabled the new enabled state.
    */
    public void setEnabled(boolean fEnabled) {
        m_fEnabled = fEnabled;
        synchronized (getPeerLock()) {
            if (m_jmiPeer != null) {
                m_jmiPeer.setEnabled(fEnabled);
            }
            if (m_jmMenuPeer != null) {
                m_jmMenuPeer.getItem(0).setEnabled(fEnabled);
            }
        }
    }

    /**
    * Return the current enabled state of this MenuSheetItem.
    *
    * @override Never
    */
    public boolean isEnabled() {
        return m_fEnabled;
    }

    /**
    * Mark the item visible or invisible.
    *
    * @override Never
    */
    public void setVisible(boolean fVisible) {
        super.setVisible(fVisible);
        if (!fVisible) {
            synchronized (getPeerLock()) {
                m_jmiPeer = null;
                m_jmMenuPeer = null;
            }
        }
    }

    /**
    * Set the action to perform when this item is selected.
    *
    * @override Never
    *
    * @param aAction the action to perform when this item is selected.
    *
    * @return the previously set action, if any.
    */
    public Action setAction(Action aAction) {
        Action aOld = null;
        synchronized (getActionLock()) {
            aOld = m_aAction;
            m_aAction = aAction;
        }
        return aOld;
    }

    /**
    * Set the mnemonic of this MenuSheetItem.
    *
    * <p>If there is a peer it will reflect the changes immediately.</p>
    *
    * @override Never
    *
    * @param sMnemonic the new mnemonic.
    */
    public void setMnemonic(char cMnemonic) {
        m_cMnemonic = cMnemonic;
        synchronized (getPeerLock()) {
            if (m_jmiPeer != null) {
                m_jmiPeer.setMnemonic(cMnemonic);
            }
            if (m_jmMenuPeer != null) {
                m_jmMenuPeer.getItem(0).setMnemonic(cMnemonic);
            }
        }
    }

    /**
    * Set the accelerator of this MenuSheetItem.
    *
    * <p>If there is a peer it will reflect the changes immediately.</p>
    *
    * @override Never
    *
    * @param ks the new keystroke.
    */
    public void setAccelerator(KeyStroke ks) {
        m_ksKeyStroke = ks;
        synchronized (getPeerLock()) {
            if (m_jmiPeer != null) {
                m_jmiPeer.setAccelerator(ks);
            }
            if (m_jmMenuPeer != null) {
                m_jmMenuPeer.getItem(0).setAccelerator(ks);
            }
        }
    }

    /**
    * Set the default icon of this MenuSheetItem.
    *
    * <p>If there is a peer it will reflect the changes immediately.</p>
    *
    * @override Never
    *
    * @param iiImageIcon the new icon.
    */
    public void setDefaultIcon(ImageIcon iiImageIcon) {
        setIcon(iiImageIcon, DEFAULT_IMAGE);
    }

    /**
    * Set the selected icon of this MenuSheetItem.
    *
    * <p>If there is a peer it will reflect the changes immediately.</p>
    *
    * @override Never
    *
    * @param iiImageIcon the new icon.
    */
    public void setSelectedIcon(ImageIcon iiImageIcon) {
        setIcon(iiImageIcon, SELECTED_IMAGE);
    }

    /**
    * Set the disabled icon of this MenuSheetItem.
    *
    * <p>If there is a peer it will reflect the changes immediately.</p>
    *
    * @override Never
    *
    * @param iiImageIcon the new icon.
    */
    public void setDisabledIcon(ImageIcon iiImageIcon) {
        setIcon(iiImageIcon, DISABLED_IMAGE);
    }

    /**
    * Set the disabled selected icon of this MenuSheetItem.
    *
    * <p>If there is a peer it will reflect the changes immediately.</p>
    *
    * @override Never
    *
    * @param iiImageIcon the new icon.
    */
    public void setDisabledSelectedIcon(ImageIcon iiImageIcon) {
        setIcon(iiImageIcon, DISABLED_SELECTED_IMAGE);
    }

    /**
    * Get the JMenuItem peer of the MenuSheetItem. The JMenuItem peer is a JMenuItem
    * with the same caption as this MenuSheetItem. Selecting this JMenuItem will invoke
    * the action associated to this MenuSheetItem.
    *
    * @override Never
    */
    public JMenuItem getPeer() {
        synchronized (getPeerLock()) {
            if (m_jmiPeer == null) {
                m_jmiPeer = new JMenuItem(getCaption());
                m_jmiPeer.addActionListener(this);
                if (m_cMnemonic != '\0') m_jmiPeer.setMnemonic(m_cMnemonic);
                if (m_ksKeyStroke != null) m_jmiPeer.setAccelerator(m_ksKeyStroke);
                if (m_aiImages != null) {
                    if (m_aiImages[DEFAULT_IMAGE] != null) m_jmiPeer.setIcon(new ImageIcon((Image) m_aiImages[DEFAULT_IMAGE]));
                    if (m_aiImages[SELECTED_IMAGE] != null) m_jmiPeer.setPressedIcon(new ImageIcon((Image) m_aiImages[SELECTED_IMAGE]));
                    if (m_aiImages[DISABLED_IMAGE] != null) m_jmiPeer.setDisabledIcon(new ImageIcon((Image) m_aiImages[DISABLED_IMAGE]));
                    if (m_aiImages[DISABLED_SELECTED_IMAGE] != null) m_jmiPeer.setDisabledSelectedIcon(new ImageIcon((Image) m_aiImages[DISABLED_SELECTED_IMAGE]));
                }
                m_jmiPeer.setEnabled(m_fEnabled);
            }
        }
        return m_jmiPeer;
    }

    /**
    * Return the JMenu peer for this MenuSheetItem. The JMenu peer is a JMenu with the
    * same caption as this MenuSheetItem, containing just one JMenuItem, which is
    * equivalent to the JMenuItem peer of the MenuSheetItem.
    *
    * @override Never
    */
    public JMenu getMenuPeer() {
        synchronized (getPeerLock()) {
            if (m_jmMenuPeer == null) {
                m_jmMenuPeer = new JMenu(getCaption());
                JMenuItem jmi = m_jmMenuPeer.add(getCaption());
                jmi.addActionListener(this);
                if (m_cMnemonic != '\0') jmi.setMnemonic(m_cMnemonic);
                if (m_ksKeyStroke != null) jmi.setAccelerator(m_ksKeyStroke);
                if (m_aiImages != null) {
                    if (m_aiImages[DEFAULT_IMAGE] != null) jmi.setIcon(new ImageIcon((Image) m_aiImages[DEFAULT_IMAGE]));
                    if (m_aiImages[SELECTED_IMAGE] != null) jmi.setPressedIcon(new ImageIcon((Image) m_aiImages[SELECTED_IMAGE]));
                    if (m_aiImages[DISABLED_IMAGE] != null) jmi.setDisabledIcon(new ImageIcon((Image) m_aiImages[DISABLED_IMAGE]));
                    if (m_aiImages[DISABLED_SELECTED_IMAGE] != null) jmi.setDisabledSelectedIcon(new ImageIcon((Image) m_aiImages[DISABLED_SELECTED_IMAGE]));
                }
                jmi.setEnabled(m_fEnabled);
            }
        }
        return m_jmMenuPeer;
    }

    /**
    * Hook method called whenever the user selects the MenuSheetItem. As a default
    * invokes the action currently associated with the MenuSheetItem, handing it the
    * currently attached SalesPoint and SaleProcess.
    *
    * @override Never
    *
    * @see #setAction
    * @see SalesPoint
    * @see SaleProcess
    */
    public void actionPerformed(ActionEvent e) {
        final Action[] aaTemp = new Action[1];
        synchronized (getActionLock()) {
            aaTemp[0] = m_aAction;
        }
        if (aaTemp[0] != null) {
            new Thread("ActionPerfomer: MenuSheetItem: \"" + getCaption() + "\"") {

                public void run() {
                    try {
                        aaTemp[0].doAction(m_pAttached, m_spAttached);
                    } catch (ThreadDeath td) {
                        throw td;
                    } catch (Throwable t) {
                        System.err.println("Exception occured during event dispatching: MenuSheetItem \"" + getCaption() + "\":");
                        t.printStackTrace();
                    }
                }
            }.start();
        }
    }

    /**
    * Get the Mnemonic of this MenuSheetItem.
    *
    * @override Never
    *
    * @return the mnemonic of this MenuSheetItem.
    */
    public char getMnemonic() {
        return m_cMnemonic;
    }

    /**
    * Get the accelerator of this MenuSheetItem.
    *
    * @override Never
    *
    * @return the keystroke associated with the accelerator of this MenuSheetItem.
    */
    public KeyStroke getAccelerator() {
        return m_ksKeyStroke;
    }

    /**
    * Get the default icon of this MenuSheetItem.
    *
    * @override Never
    *
    * @return the default icon of this MenuSheetItem.
    */
    public ImageIcon getDefaultIcon() {
        return (m_aiImages != null) ? new ImageIcon((Image) m_aiImages[DEFAULT_IMAGE]) : null;
    }

    /**
    * Get the selected icon of this MenuSheetItem.
    *
    * @override Never
    *
    * @return the pressed icon of this MenuSheetItem.
    */
    public ImageIcon getSelectedIcon() {
        return (m_aiImages != null) ? new ImageIcon((Image) m_aiImages[SELECTED_IMAGE]) : null;
    }

    /**
    * Get the disabled item of this MenuSheetItem.
    *
    * @override Never
    *
    * @return the disabled icon of this MenuSheetItem.
    */
    public ImageIcon getDisabledIcon() {
        return (m_aiImages != null) ? new ImageIcon((Image) m_aiImages[DISABLED_IMAGE]) : null;
    }

    /**
    * Get the disabled selected item of this MenuSheetItem.
    *
    * @override Never
    *
    * @return the disabled selected icon of this MenuSheetItem.
    */
    public ImageIcon getDisabledSelectedIcon() {
        return (m_aiImages != null) ? new ImageIcon((Image) m_aiImages[DISABLED_SELECTED_IMAGE]) : null;
    }

    private static final int DEFAULT_IMAGE = 0;

    private static final int SELECTED_IMAGE = 1;

    private static final int DISABLED_IMAGE = 2;

    private static final int DISABLED_SELECTED_IMAGE = 3;
}
