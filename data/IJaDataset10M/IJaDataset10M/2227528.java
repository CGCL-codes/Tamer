package visad.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.*;
import java.rmi.RemoteException;
import visad.*;

/**
 * A widget that allows users to control aspects of animation (stop/start,
 * step, animation speed and direction).  It is initialized with the state
 * of the AnimationControl for the ScalarMap used in the constructor. Once this
 * widget is constructed, it should be used to control animation instead
 * of using methods in AnimationControl.  Once constructed, changes made
 * using AnimationControl methods will not be reflected in this widget.
 */
public class AnimationWidget extends JPanel implements ActionListener, ChangeListener, ControlListener, ScalarMapListener {

    private static final boolean DEBUG = false;

    private boolean aDir;

    private boolean aAnim;

    private int aMs;

    private JRadioButton forward;

    private JRadioButton reverse;

    private JButton onOff;

    private JButton step;

    private JTextField ms;

    private JSlider TimeSlider;

    private AnimationControl control;

    /**
   * construct an AnimationWidget linked to the Control in smap
   * (which must be to Display.Animation) with auto-detecting ms/frame
   *
   * @param	smap	Display.Animation ScalarMap
   */
    public AnimationWidget(ScalarMap smap) throws VisADException, RemoteException {
        this(smap, -1);
    }

    /**
   * construct an AnimationWidget linked to the Control in smap
   * (which must be to Display.Animation) with specified ms/frame
   *
   * @param     smap    Display.Animation ScalarMap
   * @param     st      animation speed (ms/frame).  If value is negative,
   *                    the default speed set in the Control is used.
   */
    public AnimationWidget(ScalarMap smap, int st) throws VisADException, RemoteException {
        if (!Display.Animation.equals(smap.getDisplayScalar())) {
            throw new DisplayException("AnimationWidget: ScalarMap must " + "be to Display.Animation");
        }
        aAnim = false;
        aDir = true;
        aMs = st;
        JPanel top = new JPanel();
        JPanel bottom = new JPanel();
        JPanel left = new JPanel();
        JPanel right = new JPanel();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        forward = new JRadioButton("Forward", aDir);
        reverse = new JRadioButton("Reverse", !aDir);
        onOff = new JButton("Stop");
        step = new JButton("Step");
        ms = new JTextField("????????");
        Dimension msize = ms.getMaximumSize();
        Dimension psize = ms.getPreferredSize();
        msize.height = psize.height;
        ms.setMaximumSize(msize);
        JLabel msLabel = new JLabel("ms/frame");
        TimeSlider = new JSlider(1, 1, 1);
        Color fore = msLabel.getForeground();
        forward.setForeground(fore);
        reverse.setForeground(fore);
        onOff.setForeground(fore);
        step.setForeground(fore);
        ms.setForeground(fore);
        TimeSlider.setPaintTicks(true);
        onOff.setMaximumSize(step.getMaximumSize());
        ButtonGroup group = new ButtonGroup();
        group.add(forward);
        group.add(reverse);
        left.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        left.setAlignmentY(JPanel.TOP_ALIGNMENT);
        right.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        right.setAlignmentY(JPanel.TOP_ALIGNMENT);
        onOff.setAlignmentX(JButton.CENTER_ALIGNMENT);
        step.setAlignmentX(JButton.CENTER_ALIGNMENT);
        ms.setAlignmentY(JTextField.CENTER_ALIGNMENT);
        msLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        left.add(forward);
        left.add(reverse);
        right.add(onOff);
        right.add(step);
        top.add(left);
        top.add(right);
        add(top);
        bottom.add(ms);
        bottom.add(msLabel);
        add(bottom);
        add(TimeSlider);
        getControlSettings((AnimationControl) smap.getControl());
        if (st > 0) {
            aMs = st;
            if (control != null) control.setStep(aMs);
        }
        fixControlUI();
        if (control != null) control.addControlListener(this);
        smap.addScalarMapListener(this);
        forward.addActionListener(this);
        forward.setActionCommand("forward");
        reverse.addActionListener(this);
        reverse.setActionCommand("reverse");
        onOff.addActionListener(this);
        onOff.setActionCommand("go");
        step.addActionListener(this);
        step.setActionCommand("step");
        ms.addActionListener(this);
        ms.setActionCommand("ms");
        TimeSlider.addChangeListener(this);
    }

    private void getControlSettings(AnimationControl ctl) {
        control = ctl;
        if (control != null) {
            aDir = control.getDirection();
            aAnim = control.getOn();
            aMs = (int) control.getStep();
        }
    }

    private void fixAnimUI() {
        if (aAnim) {
            onOff.setText("Stop");
            step.setEnabled(false);
        } else {
            onOff.setText("Go");
            step.setEnabled(true);
        }
    }

    private void fixDirUI() {
        if (aDir) {
            forward.setSelected(true);
        } else {
            reverse.setSelected(true);
        }
    }

    private void fixSpeedUI() {
        ms.setText(Integer.toString(aMs));
    }

    private void fixSliderUI() {
        int max = 1;
        int cur = 1;
        Set set = null;
        if (control != null) {
            try {
                set = control.getSet();
                if (set != null) {
                    max = set.getLength();
                }
            } catch (VisADException exc) {
                if (DEBUG) exc.printStackTrace();
            }
            cur = control.getCurrent() + 1;
            if (cur < 1) {
                cur = 1;
            } else if (cur > max) {
                cur = max;
            }
        }
        TimeSlider.setMaximum(max);
        TimeSlider.setMinimum(1);
        TimeSlider.setValue(cur);
        int maj;
        if (max < 20) {
            maj = max / 4;
        } else if (max < 30) {
            maj = max / 6;
        } else {
            maj = max / 8;
        }
        TimeSlider.setMajorTickSpacing(maj);
        TimeSlider.setMinorTickSpacing(maj / 4);
        TimeSlider.setPaintLabels(set != null);
        TimeSlider.repaint();
    }

    private void fixControlUI() {
        fixAnimUI();
        fixDirUI();
        fixSpeedUI();
        fixSliderUI();
    }

    /**
   * ActionListener method used with JTextField and JButtons
   */
    public void actionPerformed(ActionEvent e) {
        if (control == null) {
            System.out.println("control == null in AnimationWidget.actionPerformed");
            return;
        }
        String cmd = e.getActionCommand();
        if (cmd.equals("forward")) {
            try {
                control.setDirection(true);
                aDir = true;
            } catch (VisADException exc) {
                if (DEBUG) exc.printStackTrace();
            } catch (RemoteException exc) {
                if (DEBUG) exc.printStackTrace();
            }
        }
        if (cmd.equals("reverse")) {
            try {
                control.setDirection(false);
                aDir = false;
            } catch (VisADException exc) {
                if (DEBUG) exc.printStackTrace();
            } catch (RemoteException exc) {
                if (DEBUG) exc.printStackTrace();
            }
        }
        if (cmd.equals("ms") || (cmd.equals("go") && !aAnim)) {
            int fr = -1;
            try {
                fr = Integer.parseInt(ms.getText());
            } catch (NumberFormatException exc) {
            }
            if (fr > 0) {
                try {
                    control.setStep(fr);
                    aMs = fr;
                    if (aDir) forward.requestFocus(); else reverse.requestFocus();
                } catch (VisADException exc) {
                    if (DEBUG) exc.printStackTrace();
                } catch (RemoteException exc) {
                    if (DEBUG) exc.printStackTrace();
                }
            }
            fixSpeedUI();
        }
        if (cmd.equals("go")) {
            try {
                control.setOn(!aAnim);
                aAnim = !aAnim;
                fixAnimUI();
            } catch (VisADException exc) {
                if (DEBUG) exc.printStackTrace();
            } catch (RemoteException exc) {
                if (DEBUG) exc.printStackTrace();
            }
        }
        if (cmd.equals("step")) {
            try {
                control.takeStep();
            } catch (VisADException exc) {
                if (DEBUG) exc.printStackTrace();
            } catch (RemoteException exc) {
                if (DEBUG) exc.printStackTrace();
            }
        }
    }

    /**
   * ChangeListener method used with JSlider.
   */
    public void stateChanged(ChangeEvent e) {
        try {
            if (control != null) control.setCurrent(TimeSlider.getValue() - 1);
        } catch (VisADException exc) {
            if (DEBUG) exc.printStackTrace();
        } catch (RemoteException exc) {
            if (DEBUG) exc.printStackTrace();
        }
    }

    /**
   * ControlListener method used for programmatically moving JSlider
   */
    public void controlChanged(ControlEvent e) {
        if (control != null) {
            boolean newDir = control.getDirection();
            if (newDir != aDir) {
                aDir = newDir;
                fixDirUI();
            }
            boolean newAnim = control.getOn();
            if (newAnim != aAnim) {
                aAnim = newAnim;
                fixAnimUI();
            }
            int newMs = (int) control.getStep();
            if (newMs != aMs) {
                aMs = newMs;
                fixSpeedUI();
            }
            fixSliderUI();
        }
    }

    /**
   * ScalarMapListener method used to recompute JSlider bounds
   */
    public void mapChanged(ScalarMapEvent e) {
        fixSliderUI();
    }

    /**
   * ScalarMapListener method used to detect new AnimationControl
   */
    public void controlChanged(ScalarMapControlEvent evt) {
        int id = evt.getId();
        if (id == ScalarMapEvent.CONTROL_REMOVED || id == ScalarMapEvent.CONTROL_REPLACED) {
            evt.getControl().removeControlListener(this);
            if (id == ScalarMapEvent.CONTROL_REMOVED) {
                control = null;
            }
        }
        if (id == ScalarMapEvent.CONTROL_REPLACED || id == ScalarMapEvent.CONTROL_ADDED) {
            control = (AnimationControl) (evt.getScalarMap().getControl());
            getControlSettings(control);
            fixControlUI();
            if (control != null) control.addControlListener(this);
        }
    }

    /**
   * Work-around for Swing bug where pack() doesn't display slider labels;
   * actually, it still won't, but window will be the right size
   */
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width, d.height + 18);
    }
}
