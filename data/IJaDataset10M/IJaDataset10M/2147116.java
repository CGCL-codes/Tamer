package sale;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import sale.stdforms.*;
import sale.events.*;

/**
  * A JFrame that can display Form- and MenuSheets.
  *
  * <p>You can use this frame to pop up messages and dialogs in extra windows, while
  * maintaining consistency with the rest of the GUI by using the familiar FormSheet
  * look'n'feel.</p>
  *
  * <p>The frame will display one {@link FormSheet}, and, by default, will close when the FormSheet
  * is closed. Closing the frame using the systems menu or any other OS dependent gesture
  * will result in a call to {@link FormSheet#cancel()} on the FormSheet.</p>
  *
  * <p>Also, the frame may display a {@link MenuSheet}. It can therefore be used wherever a Display
  * can be used.</p>
  *
  * <p><strong>Attention:</strong> This class is not meant to be serialized.</p>
  *
  * @author Steffen Zschaler
  * @version 2.0 25/05/1999
  * @since v2.0
  */
public class JDisplayDialog extends JDialog implements Display, FormSheetContainer {

    /**
    * Object used to block {@link #setFormSheet} when the FormSheet demands it.
    */
    private transient Object m_oWaiter;

    /**
    * Return the object used to block {@link #setFormSheet} when the FormSheet demands it.
    */
    private Object getWaiter() {
        if (m_oWaiter == null) {
            m_oWaiter = new Object();
        }
        return m_oWaiter;
    }

    /**
    * The currently displaying component.
    */
    private transient JComponent m_jcmpComponent;

    /**
    * The currently displaying button bar panel.
    */
    private transient JPanel m_jpButtonBar;

    /**
    * The current FormSheet.
    */
    private transient FormSheet m_fsCurrent;

    /**
    * The current MenuSheet.
    */
    private transient MenuSheet m_msCurrent;

    /**
    * The list of listeners.
    */
    protected transient EventListenerList m_ellListeners = new EventListenerList();

    /**
    * JDisplayDialogs are not meant to be serialized!
    */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        throw new NotSerializableException("JDisplayDialog");
    }

    /**
    * Create a new JDisplayDialog.
    */
    public JDisplayDialog() {
        super();
        getContentPane().setLayout(new java.awt.BorderLayout());
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                if (m_fsCurrent != null) {
                    m_fsCurrent.cancel();
                }
            }
        });
    }

    /**
    * Create a new JDisplayDialog with the given owner.
    *
    * @param jfOwner the JFrame owning the display dialog.
    */
    public JDisplayDialog(JFrame jfOwner) {
        super(jfOwner);
        getContentPane().setLayout(new java.awt.BorderLayout());
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                if (m_fsCurrent != null) {
                    m_fsCurrent.cancel();
                }
            }
        });
    }

    /**
    * Close a FormSheet.
    *
    * <p>If a FormSheet is closed, by default, the JDisplayDialog containing it is also closed. You can,
    * however, alter this behavior by overriding {@link #formSheetClosed}.</p>
    *
    * @override Never Instead override {@link #formSheetClosed}.
    *
    * @param fs the FormSheet to be closed.
    */
    public void closeFormSheet(FormSheet fs) {
        boolean fExplicit = true;
        fs.detachDisplay();
        if (m_fsCurrent == fs) {
            m_fsCurrent = null;
        } else {
            fExplicit = false;
        }
        formSheetClosed();
        synchronized (getWaiter()) {
            getWaiter().notifyAll();
        }
        fireFormSheetRemoved(fs, fExplicit);
    }

    /**
    * Hook method called when the FormSheet was closed.
    *
    * @override Sometimes The default implementation closes the JDisplayDialog.
    */
    protected void formSheetClosed() {
        setVisible(false);
        dispose();
    }

    /**
    * In addition to disposing of the peer resources, remove the FormSheet and the
    * MenuSheet.
    *
    * @override Never
    */
    public void dispose() {
        try {
            setFormSheet(null);
        } catch (InterruptedException e) {
        }
        setMenuSheet(null);
        super.dispose();
    }

    /**
    * Notification event informing about a change of a FormSheet's caption.
    *
    * @override Never
    *
    * @param fs the FormSheet whose caption changed.
    * @param sNewCaption the new caption of the FormSheet.
    */
    public void onFormSheetCaptionChanged(FormSheet fs, String sNewCaption) {
        setTitle(sNewCaption);
    }

    /**
    * Notification event informing about a change of a FormSheet's component.
    *
    * @override Never
    *
    * @param fs the FormSheet whose component changed.
    * @param jcmpNew the new component of the FormSheet.
    */
    public void onFormSheetComponentChanged(FormSheet fs, JComponent jcmpNew) {
        synchronized (fs.getComponentLock()) {
            getContentPane().remove(m_jcmpComponent);
            m_jcmpComponent = fs.getComponent();
            if (m_jcmpComponent != null) {
                getContentPane().add(m_jcmpComponent, "Center");
            }
            pack();
        }
    }

    /**
    * Notification event informing that a button was added to the FormSheet's button bar.
    *
    * @override Never
    *
    * @param fs the FormSheet whose button bar changed.
    * @param fb the button that was added to the FormSheet.
    */
    public void onFormSheetButtonAdded(FormSheet fs, FormSheet.FormButton fb) {
        synchronized (fs.getButtonsLock()) {
            m_jpButtonBar.add(fb.getPeer());
            pack();
        }
    }

    /**
    * Notification event informing that a button was removed from the FormSheet's button bar.
    *
    * @override Never
    *
    * @param fs the FormSheet whose button bar changed.
    * @param fb the button that was removed from the FormSheet.
    */
    public void onFormSheetButtonRemoved(FormSheet fs, FormSheet.FormButton fb) {
        synchronized (fs.getButtonsLock()) {
            m_jpButtonBar.remove(fb.getPeer());
            pack();
        }
    }

    /**
    * Notification event informing that all buttons were removed from a FormSheet's button bar.
    *
    * @override Never
    *
    * @param fs the FormSheet whose button bar was cleared.
    */
    public void onFormSheetButtonsCleared(FormSheet fs) {
        synchronized (fs.getButtonsLock()) {
            m_jpButtonBar.removeAll();
            pack();
        }
    }

    /**
    * Set and display a FormSheet.
    *
    * <p>If {@link FormSheet#waitResponse fs.waitResponse()} returns true,
    * <code>setFormSheet()</code> blocks, until the FormSheet is closed by a matching
    * call to {@link #closeFormSheet}.</p>
    *
    * @override Never
    *
    * @param fs the FormSheet to be displayed.
    *
    * @exception InterruptedException if an interrupt occurs while waiting for the
    * FormSheet to be closed.
    */
    public void setFormSheet(FormSheet fs) throws InterruptedException {
        if (m_fsCurrent != null) {
            FormSheet fsTemp = m_fsCurrent;
            if (fs != null) {
                m_fsCurrent = null;
            }
            fsTemp.cancel();
        }
        getContentPane().removeAll();
        if (fs != null) {
            synchronized (fs.getComponentLock()) {
                synchronized (fs.getButtonsLock()) {
                    setTitle(fs.getCaption());
                    fs.attach(this);
                    m_fsCurrent = fs;
                    m_jcmpComponent = fs.getComponent();
                    if (m_jcmpComponent != null) {
                        getContentPane().add(m_jcmpComponent, "Center");
                    }
                    m_jpButtonBar = new JPanel(false);
                    fs.fillBtnPanel(m_jpButtonBar);
                    getContentPane().add(m_jpButtonBar, "South");
                    pack();
                }
            }
            fireFormSheetSet(fs);
            if (fs.waitResponse()) {
                synchronized (getWaiter()) {
                    while (fs.getDisplay() == this) {
                        getWaiter().wait();
                    }
                }
            }
        }
    }

    /**
    * Close the current FormSheet.
    *
    * @override Never
    */
    public void closeFormSheet() {
        if (m_fsCurrent != null) {
            closeFormSheet(m_fsCurrent);
        }
    }

    /**
    * Open a fresh {@link JDisplayDialog} and display the FormSheet in it.
    *
    * @override Never
    *
    * @exception InterruptedException if an interrupt occured while waiting for the
    * FormSheet to be closed.
    */
    public void popUpFormSheet(FormSheet fs) throws InterruptedException {
        JDisplayDialog jdd = new JDisplayDialog();
        jdd.setVisible(true);
        try {
            jdd.setFormSheet(fs);
        } catch (InterruptedException e) {
            if (fs.getDisplay() == jdd) {
                fs.cancel();
            }
            throw e;
        }
    }

    /**
    * Remove any old MenuSheet and display the new one.
    *
    * @override Never
    *
    * @param ms the MenuSheet to be displayed.
    */
    public void setMenuSheet(MenuSheet ms) {
        if (m_msCurrent != null) {
            m_msCurrent.setVisible(false);
        }
        m_msCurrent = ms;
        if (m_msCurrent != null) {
            m_msCurrent.setVisible(true);
            setJMenuBar(ms.getMenuBar());
        } else {
            setJMenuBar(null);
        }
        pack();
    }

    /**
    * Return true to indicate this is a useable display.
    *
    * @override Never
    */
    public boolean isUseableDisplay() {
        return true;
    }

    /**
    * Add a listener to receive notification on the JDisplayDialog's FormSheet.
    *
    * @override Never
    */
    public void addFormSheetListener(FormSheetListener fsl) {
        m_ellListeners.add(FormSheetListener.class, fsl);
    }

    /**
    * Remove a listener to receive notification on the JDisplayDialog's FormSheet.
    *
    * @override Never
    */
    public void removeFormSheetListener(FormSheetListener fsl) {
        m_ellListeners.remove(FormSheetListener.class, fsl);
    }

    /**
    * Fire an event to all {@link sale.events.FormSheetListener FormSheetListeners} indicating that
    * a {@link FormSheet} was set on this display. As FormSheet setting is always explicit, no
    * extra parameter is necessary.
    *
    * @override Never
    *
    * @param fs the FormSheet that was set
    */
    protected void fireFormSheetSet(FormSheet fs) {
        FormSheetEvent e = null;
        Object[] listeners = m_ellListeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FormSheetListener.class) {
                if (e == null) e = new FormSheetEvent(this, fs, true);
                ((FormSheetListener) listeners[i + 1]).formSheetSet(e);
            }
        }
    }

    /**
    * Fire an event to all {@link sale.events.FormSheetListener FormSheetListeners} indicating that
    * a {@link FormSheet} was removed from this display.
    *
    * @override Never
    *
    * @param fs the FormSheet that was set
    * @param fExplicit true, if the FormSheet was closed explicitly, i.e. either by a call to one of
    * the <code>closeFormSheet</code> methods or by <code>setFormSheet (null)</code>.
    *
    * @see #closeFormSheet()
    * @see #closeFormSheet(FormSheet)
    * @see #setFormSheet
    */
    protected void fireFormSheetRemoved(FormSheet fs, boolean fExplicit) {
        FormSheetEvent e = null;
        Object[] listeners = m_ellListeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FormSheetListener.class) {
                if (e == null) e = new FormSheetEvent(this, fs, fExplicit);
                ((FormSheetListener) listeners[i + 1]).formSheetRemoved(e);
            }
        }
    }

    /**
    * JDisplayDialog test suite.
    */
    public static void main(java.lang.String[] args) {
        final JDisplayDialog jdd = new JDisplayDialog();
        jdd.show();
        MenuSheet ms = new MenuSheet("Main");
        ms.add(new MenuSheetItem("Quit", new Action() {

            public void doAction(SaleProcess p, SalesPoint sp) {
                jdd.dispose();
            }
        }));
        jdd.setMenuSheet(ms);
        final MsgForm mfTest = new sale.stdforms.MsgForm("Testmessage", "Dies ist ein Test des JFormSheetFrames.\n\n" + "Wir verwenden dazu ein veraendertes MsgForm.");
        mfTest.addButton("Toggle Caption", 1, new Action() {

            public void doAction(SaleProcess p, SalesPoint sp) {
                if (mfTest.getCaption().equals("Testmessage")) {
                    mfTest.setCaption("Geaendert !");
                } else {
                    mfTest.setCaption("Testmessage");
                }
            }
        });
        mfTest.addButton("Add button", 2, new Action() {

            public void doAction(SaleProcess p, SalesPoint sp) {
                mfTest.addButton("Tester", 700, null);
                mfTest.getButton(2).setEnabled(false);
                mfTest.getButton(3).setEnabled(true);
            }
        });
        mfTest.addButton("Remove button", 3, new Action() {

            public void doAction(SaleProcess p, SalesPoint sp) {
                mfTest.removeButton(700);
                mfTest.getButton(2).setEnabled(true);
                mfTest.getButton(3).setEnabled(false);
            }
        });
        mfTest.getButton(3).setEnabled(false);
        final JComponent[] ajcmp = new JComponent[1];
        ajcmp[0] = new JLabel("Tester");
        mfTest.addButton("Toggle Component", 4, new Action() {

            public void doAction(SaleProcess p, SalesPoint sp) {
                ajcmp[0] = mfTest.setComponent(ajcmp[0]);
            }
        });
        try {
            jdd.setFormSheet(mfTest);
        } catch (InterruptedException e) {
        }
        System.err.println("FormSheet was " + ((mfTest.isCancelled()) ? ("cancelled.") : ("closed normally.")));
        System.exit(0);
    }
}
