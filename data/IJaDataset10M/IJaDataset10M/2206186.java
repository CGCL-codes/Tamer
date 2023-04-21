package de.ios.framework.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import de.ios.framework.gui.ViewTools;

public class JMessageDialog extends JDialog implements ActionListener, KeyListener, WindowListener {

    /**
   * Flag zur Aktivierung der automatischen Button-Text-Ergaenzung
   * um die ausloesende Taste (default: Button1-Button3 => F1-F3)
   */
    public static boolean addKeyToButtonLabel = false;

    /**
   * Die Tasten:
   */
    protected int key1 = KeyEvent.VK_F1;

    protected int key2 = KeyEvent.VK_F2;

    protected int key3 = KeyEvent.VK_F3;

    protected String key1Text = "[F1]";

    protected String key2Text = "[F2]";

    protected String key3Text = "[F3]";

    /**
   * @deprecated Use constructor with View or Frame-Argument.
   * Konstruktor.
   */
    public JMessageDialog() {
        super(new JFrame());
    }

    /**
   * Konstructor mit Angabe des Parent-Frames
   */
    public JMessageDialog(Frame f) {
        super((f == null) ? new JFrame() : f);
    }

    /**
   * @deprecated Use constructor with View or Frame-Argument.
   * <i>Use JMessageDialog( Frame).setLabelMode( useLabels )</i>.
   */
    public JMessageDialog(boolean useLabels) {
        super(new JFrame());
        useMultiLabels = useLabels;
    }

    /**
   * @deprecated Use constructor with View or Frame-Argument.
   * Konstruktur mit Flag und Size fuer TextArea.
   * Die Meldung wird mit Labels oder mit einer Textarea angezeigt.
   */
    public JMessageDialog(boolean useLabels, int rows, int cols) {
        super(new JFrame());
        useMultiLabels = useLabels;
        trows = rows;
        tcols = cols;
    }

    /**
   * Konstruktor, der sofort eine Alert-Box ohne Buttons aufmacht
   */
    public JMessageDialog(Frame parent, String _title, String _message) {
        super(parent);
        alert0NoWait(_title, _message);
    }

    /**
   * @deprecated Use constructor with View or Frame-Argument.
   * Konstruktor, der sofort eine Alert-Box ohne Buttons aufmacht
   */
    public JMessageDialog(String _title, String _message) {
        super(new JFrame());
        alert0NoWait(_title, _message);
    }

    /**
   * Konstruktor, der sofort eine 1-Button-Alert-Box aufmacht
   */
    public JMessageDialog(Frame parent, String _title, String _message, String _button) {
        super(parent);
        alert1NoWait(_title, _message, _button);
    }

    /**
   * @deprecated Use constructor with View or Frame-Argument.
   * Konstruktor, der sofort eine 1-Button-Alert-Box aufmacht
   */
    public JMessageDialog(String _title, String _message, String _button) {
        super(new JFrame());
        alert1NoWait(_title, _message, _button);
    }

    /**
   * @deprecated Use constructor with View or Frame-Argument.
   * Set the keys woth <i>defineKeys</i>.
   * Konstruktor, der sofort eine 1-Button-Alert-Box aufmacht.
   * @param key Virtual key-code for selecting the button (see java.awt.event.KeyEvent for valid values!).
   */
    public JMessageDialog(String _title, String _message, String _button, int key, String keyText) {
        super(new JFrame());
        defineKeys(key, keyText, 0, null, 0, null);
        alert1NoWait(_title, _message, _button);
    }

    public int alert0NoWait(String title, String message) {
        waitF = false;
        b1 = null;
        b2 = null;
        b3 = null;
        return build_message(title, message, false);
    }

    public int alert1NoWait(String title, String message, String button1) {
        waitF = false;
        b1 = createButton(extendButtonLabel(button1, key1Text));
        b2 = null;
        b3 = null;
        return build_message(title, message, false);
    }

    public int alert1(String title, String message, String button1) {
        waitF = true;
        b1 = createButton(extendButtonLabel(button1, key1Text));
        b2 = null;
        b3 = null;
        return build_message(title, message, true);
    }

    /**
   * Zu einer aelternen Version kompatibler Aufruf.
   * Wird bei einer der naechsten Revisionen entfernt.
   */
    public int alert1(JFrame parent, String title, String message, String button1) {
        return alert1(title, message, button1);
    }

    /**
   * Dialogbox mit zwei Antwortbuttons
   */
    public int alert2(String title, String message, String button1, String button2) {
        waitF = true;
        b1 = createButton(extendButtonLabel(button1, key1Text));
        b2 = createButton(extendButtonLabel(button2, key2Text));
        b3 = null;
        return build_message(title, message, true);
    }

    /**
   * Dialogbox mit drei Antwortbuttons
   */
    public int alert3(String title, String message, String button1, String button2, String button3) {
        waitF = true;
        b1 = createButton(extendButtonLabel(button1, key1Text));
        b2 = createButton(extendButtonLabel(button2, key2Text));
        b3 = createButton(extendButtonLabel(button3, key3Text));
        return build_message(title, message, true);
    }

    /**
   * Dialogbox mit Antwortbuttons
   */
    public int alert(String title, String message, String[] buttons) {
        waitF = true;
        b1 = (buttons.length > 0 && buttons[0] != null) ? createButton(buttons[0]) : null;
        b2 = (buttons.length > 1 && buttons[1] != null) ? createButton(buttons[1]) : null;
        b3 = (buttons.length > 2 && buttons[2] != null) ? createButton(buttons[2]) : null;
        return build_message(title, message, true);
    }

    public int alertNoWait(String title, String message, String[] buttons) {
        waitF = false;
        b1 = (buttons.length > 0 && buttons[0] != null) ? createButton(buttons[0]) : null;
        b2 = (buttons.length > 1 && buttons[1] != null) ? createButton(buttons[1]) : null;
        b3 = (buttons.length > 2 && buttons[2] != null) ? createButton(buttons[2]) : null;
        return build_message(title, message, false);
    }

    /**
   * If true, a message can be splittet in more then one label.
   */
    public boolean useMultiLabels = true;

    /** Size of the TextArea (if used). */
    public int trows = -1;

    public int tcols = -1;

    /**
   * Redefine the Keys activating the Buttons
   */
    public JMessageDialog defineKeys(int[] k, String[] text) {
        if (k != null) {
            key1 = (k.length > 0) ? k[0] : 0;
            key2 = (k.length > 1) ? k[1] : 0;
            key3 = (k.length > 2) ? k[2] : 0;
        }
        if (text != null) {
            key1Text = (text.length > 0) ? text[0] : null;
            key2Text = (text.length > 1) ? text[1] : null;
            key3Text = (text.length > 2) ? text[2] : null;
        }
        return this;
    }

    /**
   * @decprecated Use defineKeys( int [], String[] ).
   * Redefine the Keys activating the Buttons
   */
    public JMessageDialog defineKeys(int k1, String k1Text, int k2, String k2Text, int k3, String k3Text) {
        key1 = k1;
        key2 = k2;
        key3 = k3;
        key1Text = k1Text;
        key2Text = k2Text;
        key3Text = k3Text;
        return this;
    }

    /**
   * Erweiterung des Button-Labels um die ausloesende Taste (soweit aktiviert):
   */
    protected String extendButtonLabel(String label, String keyText) {
        int c = 0, p = -1;
        if (addKeyToButtonLabel && (keyText != null)) {
            for (c = 0; (c < label.length()) && (p < 0); c++) if (label.charAt(c) != ' ') p = c;
            if (p < 0) p = label.length() / 2;
            return (label.substring(0, p) + keyText + ' ' + label.substring(p));
        } else return label;
    }

    /**
   * set a new Message
   */
    public void setMessage(String m) {
        StringTokenizer s;
        int i, i2;
        String tmp, tmp2;
        if (useMultiLabels) {
            s = new StringTokenizer(m, "\n");
            for (i = 0; i < v.size(); i++) {
                tmp = s.nextToken();
                tmp2 = "";
                while (true) {
                    i2 = tmp.indexOf('\t');
                    if (i2 < 0) break;
                    if (i2 > 0) tmp2 = tmp2 + tmp.substring(0, i2);
                    tmp2 = tmp2 + " ";
                    while ((tmp2.length() % 8) != 0) tmp2 = tmp2 + " ";
                    tmp = tmp.substring(i2 + 1);
                }
                tmp2 = tmp2 + tmp;
                if (debug) System.out.println(tmp2);
                ((JLabel) v.elementAt(i)).setText(" " + tmp2);
            }
        } else ((JTextArea) v.elementAt(0)).setText(m);
        pack();
        repaint();
    }

    /**
   * Get the message text (only when useMultiLables is disabled)
   *
   * @return the message text
   */
    public String getMessage() {
        if (!useMultiLabels) {
            return (String) ((JTextArea) v.elementAt(0)).getText();
        } else return null;
    }

    /**
   * Set the label-display-mode.
   * @param useLabels If true, lables are used to display themessage, 
   *                  otherwise a textArea is used.
   */
    public JMessageDialog setLabelMode(boolean useLabels) {
        useMultiLabels = useLabels;
        return this;
    }

    /**
   * Get the label-display-mode.
   * @return true if the dialog display the message using labels.
   */
    public boolean getLabelMode() {
        return useMultiLabels;
    }

    /**
   * Set the width and heigth of the displaying text-area.
   * This take only effect if the the Label-mode is off.
   */
    public JMessageDialog setTextSize(int rows, int cols) {
        trows = rows;
        tcols = cols;
        return this;
    }

    /**
   * Aufbau der Textlabel und Aufruf des Dialogfensters.
   */
    protected int build_message(String title, String message, boolean mode) {
        setModal(mode);
        setTitle(title);
        int i, n;
        JLabel l;
        JTextArea t;
        answer = -1;
        getContentPane().setLayout(new BorderLayout());
        msg = new JPanel();
        v = new Vector();
        if (useMultiLabels) {
            n = new StringTokenizer(message, "\n").countTokens();
            msg.setLayout(new GridLayout(n + 2, 1));
            msg.add(new JLabel(" "));
            for (i = 0; i < n; i++) {
                l = new JLabel();
                v.addElement(l);
                msg.add(l);
            }
        } else {
            msg.setLayout(new BorderLayout());
            if ((trows == -1) || (tcols == -1)) t = new JTextArea(); else t = new JTextArea(trows, tcols);
            v.addElement(t);
            msg.add(new JScrollPane(t), BorderLayout.CENTER);
        }
        getContentPane().add(msg, BorderLayout.CENTER);
        buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
        if (b1 != null) {
            buttons.add(b1);
        }
        if (b2 != null) buttons.add(b2);
        if (b3 != null) buttons.add(b3);
        getContentPane().add(buttons, BorderLayout.SOUTH);
        setMessage(message);
        addListeners();
        ViewTools.centerAbove(getParent(), this);
        setVisible(true);
        if (!waitF) {
            return 0;
        }
        while (isShowing()) {
            try {
                Thread.yield();
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
        removeListeners();
        return answer;
    }

    /**
   * Add the Dialog as listener to all buttons.
   */
    protected void addListeners() {
        JButton ba[] = { b1, b2, b3 };
        addWindowListener(this);
        for (int i = 0; i < ba.length; i++) {
            if (ba[i] != null) {
                ba[i].addKeyListener(this);
                ba[i].addActionListener(this);
            }
        }
        if (v != null) {
            Component c;
            Enumeration e = v.elements();
            while (e.hasMoreElements()) {
                c = (Component) e.nextElement();
                if (c.isEnabled() && c.isFocusTraversable()) c.addKeyListener(this);
            }
        }
    }

    /**
   * Remove the Dialog as listener from all buttons.
   */
    protected void removeListeners() {
        JButton ba[] = { b1, b2, b3 };
        removeWindowListener(this);
        for (int i = 0; i < ba.length; i++) {
            if (ba[i] != null) {
                ba[i].removeKeyListener(this);
                ba[i].removeActionListener(this);
            }
        }
    }

    /**
   * Close the Window. 
   * Redefine this methode if the dialog should not be closed on buttons.
   */
    public void closeWindow() {
        this.setVisible(false);
        this.dispose();
    }

    /**
   * Implements WindowListener.
   * Uses to set the focus to the first button.
   */
    public void windowActivated(WindowEvent evt) {
        if (isShowing()) {
            Component c = getFocusOwner();
            if (c != null) if (!(c.isEnabled() && c.isFocusTraversable())) c = null;
            if (c == null) {
                if (b1 != null) {
                    b1.requestFocus();
                } else if (b2 != null) b2.requestFocus(); else if (b3 != null) b3.requestFocus();
            }
        }
    }

    /**
   * Implements WindowListener.
   */
    public void windowClosed(WindowEvent evt) {
    }

    /**
   * Implements WindowListener.
   */
    public void windowClosing(WindowEvent evt) {
        answer = -1;
        closeWindow();
    }

    /**
   * Implements WindowListener.
   */
    public void windowDeactivated(WindowEvent evt) {
    }

    /**
   * Implements WindowListener.
   */
    public void windowDeiconified(WindowEvent evt) {
    }

    /**
   * Implements WindowListener.
   */
    public void windowIconified(WindowEvent evt) {
    }

    /**
   * Implements WindowListener.
   */
    public void windowOpened(WindowEvent evt) {
    }

    /**
   * Implements ActionListener
   */
    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        boolean handled = false;
        if (src == b1) {
            if (debug) {
                System.out.println("Button1");
            }
            answer = 1;
            handled = true;
        } else if (src == b2) {
            if (debug) {
                System.out.println("Button2");
            }
            answer = 2;
            handled = true;
        } else if (src == b3) {
            if (debug) {
                System.out.println("Button3");
            }
            answer = 3;
            handled = true;
        } else if (debug) {
            System.out.println("WARNUNG: Ein unbekannter Event ist aufgetreten!");
        }
        if (handled) closeWindow();
    }

    /**
   * Implements KeyListener
   */
    public void keyReleased(KeyEvent evt) {
    }

    static char ENTER = '\n';

    /**
   * Implements KeyListener
   */
    public void keyTyped(KeyEvent evt) {
        Object src = evt.getSource();
        char key = evt.getKeyChar();
        boolean handled = false;
        if (debug) System.out.println("KEY_PRESS: " + evt);
        if ((src == b1) && (key == ENTER)) {
            if (debug) System.out.println("Button1");
            answer = 1;
            handled = true;
        } else if ((src == b2) && (key == ENTER)) {
            if (debug) {
                System.out.println("Button2");
            }
            answer = 2;
            handled = true;
        } else if ((src == b3) && (key == ENTER)) {
            if (debug) {
                System.out.println("Button3");
            }
            answer = 3;
            handled = true;
        }
        if (handled) closeWindow();
    }

    /**
   * Implements KeyListener
   */
    public void keyPressed(KeyEvent evt) {
        Object src = evt.getSource();
        int key = evt.getKeyCode();
        boolean handled = false;
        if ((b1 != null) && (key == key1)) {
            if (debug) System.out.println("Button1");
            answer = 1;
            handled = true;
        } else if ((b2 != null) && (key == key2)) {
            if (debug) System.out.println("Button2");
            answer = 2;
            handled = true;
        } else if ((b3 != null) && (key == key3)) {
            if (debug) System.out.println("Button3");
            answer = 3;
            handled = true;
        }
        if (handled) closeWindow();
    }

    /**
   * Variabeln der Klasse
   */
    Vector v = null;

    /**
   * The last selected button.
   */
    public int answer = 0;

    JPanel msg;

    JButton b1 = null, b2 = null, b3 = null;

    boolean waitF = false;

    JPanel buttons;

    /**
   * Flag, das interne Debugging-Ausgaben aktiviert
   */
    static final boolean debug = false;

    /**
   *
   */
    protected JButton createButton(String label) {
        return new JGenButton(label);
    }
}
