package net.sf.jaer.biasgen.VDAC;

import java.util.logging.Logger;
import net.sf.jaer.biasgen.*;
import net.sf.jaer.util.*;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.*;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.JSlider;
import javax.swing.border.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.*;

/**
 * A GUI control component for controlling an IPot.
 * It shows the name of the IPot and provides a slider and text field for entry of the IPot current.
 * @author  tobi
 */
public class VPotSliderTextControl extends JPanel implements Observer, StateEditable {

    static Preferences prefs = Preferences.userNodeForPackage(VPotSliderTextControl.class);

    static Logger log = Logger.getLogger("VPotSliderTextControl");

    VPot pot;

    StateEdit edit = null;

    UndoableEditSupport editSupport = new UndoableEditSupport();

    private static final float TEXT_FIELD_MOUSE_WHEEL_FRACTION = 0.001f;

    static EngineeringFormat engFormat = new EngineeringFormat();

    private long lastMouseWheelMovementTime = 0;

    private final long minDtMsForWheelEditPost = 500;

    static {
        engFormat.setPrecision(3);
    }

    private static final float TEXT_FIELD_KEY_CLICK_FRACTION = 0.001f;

    private boolean addedUndoListener = false;

    private boolean sliderDontProcess = false;

    /**
     * Creates new form VPotSliderTextControl.
     * 
     * 
     */
    public VPotSliderTextControl(VPot pot) {
        this.pot = pot;
        initComponents();
        if (pot != null) {
            sliderDontProcess = true;
            slider.setVisible(true);
            slider.setMaximum(pot.getMaxBitValue());
            slider.setMinimum(0);
            slider.setToolTipText(pot.getTooltipString());
            pot.addObserver(this);
            pot.loadPreferences();
            sliderDontProcess = false;
        }
        updateAppearance();
        allInstances.add(this);
    }

    public String toString() {
        return "VPotGUIControl for pot " + pot.getName();
    }

    void rr() {
        revalidate();
        repaint();
    }

    protected void updateAppearance() {
        if (pot == null) {
            return;
        }
        if (valueTextField.isVisible() != valueEnabled) {
            valueTextField.setVisible(valueEnabled);
            rr();
        }
        slider.setValue(bitValueFromSliderValue(slider));
        valueTextField.setText(engFormat.format(pot.getVoltage()));
    }

    private int sliderValueFromBitValue(JSlider s) {
        double f = (double) s.getValue() / s.getMaximum();
        int v = (int) Math.round(f * pot.getMaxBitValue());
        return v;
    }

    private int bitValueFromSliderValue(JSlider s) {
        int v = (int) Math.round((float) pot.getBitValue() / pot.getMaxBitValue() * s.getMaximum());
        return v;
    }

    /** called when Observable changes (pot changes) */
    public void update(Observable observable, Object obj) {
        if (observable instanceof VPot) {
            slider.setValueIsAdjusting(false);
            updateAppearance();
        }
    }

    private void initComponents() {
        slider = new javax.swing.JSlider();
        valueTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        setFocusable(false);
        addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
        });
        addAncestorListener(new javax.swing.event.AncestorListener() {

            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }

            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                formAncestorAdded(evt);
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        slider.setMajorTickSpacing(100);
        slider.setMaximum(1000);
        slider.setMinorTickSpacing(10);
        slider.setToolTipText("");
        slider.setValue(0);
        slider.setAlignmentX(0.0F);
        slider.setMaximumSize(new java.awt.Dimension(32767, 50));
        slider.setMinimumSize(new java.awt.Dimension(36, 10));
        slider.setPreferredSize(new java.awt.Dimension(150, 10));
        slider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderStateChanged(evt);
            }
        });
        slider.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                sliderMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sliderMouseReleased(evt);
            }
        });
        add(slider);
        valueTextField.setColumns(8);
        valueTextField.setFont(new java.awt.Font("Courier New", 0, 11));
        valueTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        valueTextField.setText("value");
        valueTextField.setToolTipText("Enter voltage here. Up and Down arrows change values.");
        valueTextField.setMaximumSize(new java.awt.Dimension(100, 2147483647));
        valueTextField.setMinimumSize(new java.awt.Dimension(11, 15));
        valueTextField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                valueTextFieldActionPerformed(evt);
            }
        });
        valueTextField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                valueTextFieldFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                valueTextFieldFocusLost(evt);
            }
        });
        valueTextField.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                valueTextFieldKeyPressed(evt);
            }
        });
        valueTextField.addMouseWheelListener(new java.awt.event.MouseWheelListener() {

            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                valueTextFieldMouseWheelMoved(evt);
            }
        });
        add(valueTextField);
        jPanel2.setFocusable(false);
        jPanel2.setMaximumSize(new java.awt.Dimension(0, 32767));
        jPanel2.setMinimumSize(new java.awt.Dimension(0, 20));
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 20));
        jPanel2.setRequestFocusEnabled(false);
        add(jPanel2);
    }

    private void valueTextFieldFocusLost(java.awt.event.FocusEvent evt) {
        endEdit();
        valueTextField.setFont(new java.awt.Font("Courier New", 0, 11));
    }

    private void valueTextFieldFocusGained(java.awt.event.FocusEvent evt) {
        startEdit();
        valueTextField.setFont(new java.awt.Font("Courier New", 1, 11));
    }

    Border selectedBorder = new EtchedBorder(), unselectedBorder = new EmptyBorder(1, 1, 1, 1);

    private void formMouseExited(java.awt.event.MouseEvent evt) {
    }

    private void formMouseEntered(java.awt.event.MouseEvent evt) {
    }

    private void sliderMouseReleased(java.awt.event.MouseEvent evt) {
        endEdit();
    }

    private void sliderMousePressed(java.awt.event.MouseEvent evt) {
        startEdit();
    }

    private void valueTextFieldMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
        int clicks = evt.getWheelRotation();
        float ratio = (clicks * TEXT_FIELD_MOUSE_WHEEL_FRACTION);
        if (pot.getSex() == Pot.Sex.N) {
            ratio = -ratio;
        }
        startEdit();
        pot.changeByFractionOfFullScale(ratio);
        long t = System.currentTimeMillis();
        if (t - lastMouseWheelMovementTime > minDtMsForWheelEditPost) {
            endEdit();
        }
        lastMouseWheelMovementTime = t;
    }

    private void valueTextFieldKeyPressed(java.awt.event.KeyEvent evt) {
        endEdit();
        int code = evt.getKeyCode();
        boolean shift = evt.isShiftDown();
        float byRatio = TEXT_FIELD_KEY_CLICK_FRACTION;
        if (shift) {
            byRatio = TEXT_FIELD_KEY_CLICK_FRACTION * 10;
        }
        if (code == KeyEvent.VK_UP) {
            startEdit();
            pot.changeByFractionOfFullScale(byRatio);
            endEdit();
        } else if (code == KeyEvent.VK_DOWN) {
            startEdit();
            pot.changeByFractionOfFullScale(-byRatio);
            endEdit();
        }
    }

    private void valueTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            float v = engFormat.parseFloat(valueTextField.getText());
            startEdit();
            pot.setVoltage(v);
            endEdit();
        } catch (NumberFormatException e) {
            Toolkit.getDefaultToolkit().beep();
            valueTextField.selectAll();
        }
    }

    /** when slider is moved, event is sent here. The slider is the 'master' of the value in the text field.
     * Slider is linear scale, from pot min to pot max 
     * @param e the ChangeEvent
     */
    private void sliderStateChanged(javax.swing.event.ChangeEvent evt) {
        if (sliderDontProcess) return;
        JSlider s = (JSlider) evt.getSource();
        int v = (int) s.getValue();
        if (v == 0) {
            pot.setBitValue(0);
        } else {
            v = sliderValueFromBitValue(s);
            pot.setBitValue(v);
        }
    }

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {
        if (addedUndoListener) {
            return;
        }
        addedUndoListener = true;
        if (evt.getComponent() instanceof Container) {
            Container anc = (Container) evt.getComponent();
            while (anc != null && anc instanceof Container) {
                if (anc instanceof UndoableEditListener) {
                    editSupport.addUndoableEditListener((UndoableEditListener) anc);
                    break;
                }
                anc = anc.getParent();
            }
        }
    }

    int oldPotValue = 0;

    private void startEdit() {
        if (edit != null) return;
        edit = new MyStateEdit(this, "pot change");
        oldPotValue = pot.getBitValue();
    }

    private void endEdit() {
        if (oldPotValue == pot.getBitValue()) {
            return;
        }
        if (edit != null) {
            edit.end();
            editSupport.postEdit(edit);
            edit = null;
        }
    }

    String STATE_KEY = "pot state";

    public void restoreState(Hashtable<?, ?> hashtable) {
        if (hashtable == null) {
            throw new RuntimeException("null hashtable");
        }
        if (hashtable.get(STATE_KEY) == null) {
            log.warning("pot " + pot + " not in hashtable " + hashtable + " with size=" + hashtable.size());
            return;
        }
        int v = (Integer) hashtable.get(STATE_KEY);
        pot.setBitValue(v);
    }

    public void storeState(Hashtable<Object, Object> hashtable) {
        hashtable.put(STATE_KEY, new Integer(pot.getBitValue()));
    }

    class MyStateEdit extends StateEdit {

        public MyStateEdit(StateEditable o, String s) {
            super(o, s);
        }

        protected void removeRedundantState() {
        }

        ;
    }

    private javax.swing.JPanel jPanel2;

    private javax.swing.JSlider slider;

    private javax.swing.JTextField valueTextField;

    public JSlider getSlider() {
        return this.slider;
    }

    public JTextField getValueTextField() {
        return this.valueTextField;
    }

    public static boolean isVoltageEnabled() {
        return VPotSliderTextControl.valueEnabled;
    }

    public static void setVoltageEnabled(final boolean voltageEnabled) {
        VPotSliderTextControl.valueEnabled = voltageEnabled;
        prefs.putBoolean("VPotGUIControl.voltageEnabled", voltageEnabled);
    }

    public static boolean isSliderEnabled() {
        return VPotSliderTextControl.sliderEnabled;
    }

    public static void setSliderEnabled(final boolean sliderEnabled) {
        VPotSliderTextControl.sliderEnabled = sliderEnabled;
        prefs.putBoolean("VPotGUIControl.sliderEnabled", sliderEnabled);
    }

    static ArrayList<VPotSliderTextControl> allInstances = new ArrayList<VPotSliderTextControl>();

    public static void revalidateAllInstances() {
        for (VPotSliderTextControl c : allInstances) {
            c.updateAppearance();
            c.revalidate();
        }
    }

    static boolean sliderEnabled = prefs.getBoolean("VPotGUIControl.sliderEnabled", true);

    static boolean valueEnabled = prefs.getBoolean("VPotGUIControl.voltageEnabled", true);
}
