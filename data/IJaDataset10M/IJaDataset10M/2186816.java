package pck_tap.Userinterface.ObjectLibrary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import pck_tap.alg.Util;

public class LabelSpinner extends JPanel implements ComponentListener, PropertyChangeListener {

    private JLabel jLabelprompt = new JLabel();

    private JSpinner jSpinner = new JSpinner();

    private JLabel jLabelReadOnly = new JLabel();

    private String label;

    private int width;

    private String jComboBox1_oldValue;

    private boolean enabled;

    private boolean changed = false;

    private SpinnerNumberModel snmodel;

    private String displayformat = "0.00";

    private Color ReadOnly_background = new Color(237, 237, 237);

    private Boolean debug = false;

    public LabelSpinner(String p_label, int p_width, String p_selected, boolean p_isEnabled, String toolTipText, Integer value, Integer min, Integer max, Integer step) {
        try {
            snmodel = new SpinnerNumberModel(value, min, max, step);
            this.jLabelReadOnly.setText(value.toString());
            jSpinner.setModel(snmodel);
            this.label = p_label;
            this.width = p_width;
            this.enabled = p_isEnabled;
            this.jLabelprompt.setToolTipText("<html>" + toolTipText.replace(Util.CRLF, "<br>") + "</html>");
            jComboBox1_oldValue = p_selected;
            EditMode(p_isEnabled);
            this.changed = false;
            jbInit();
            ((JSpinner.DefaultEditor) jSpinner.getEditor()).getTextField().addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent p_evt) {
                    firePropertyChange("", null, null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBorder(Boolean b) {
        if (!b) {
            this.jLabelReadOnly.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        } else {
            jLabelReadOnly.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        }
    }

    public LabelSpinner(String label, Boolean required, int width, Boolean enabled, String toolTipText, Double minimum, Double maximum, Double stepSize, int pMinimumFractionDigits) {
        snmodel = new SpinnerNumberModel(minimum, minimum, maximum, stepSize);
        jSpinner = new JSpinner(snmodel);
        this.doRequired(required);
        this.label = label;
        this.width = width;
        this.enabled = enabled;
        this.jLabelprompt.setToolTipText("<html>" + toolTipText.replace(Util.CRLF, "<br>") + "</html>");
        EditMode(enabled);
        this.changed = false;
        try {
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor) jSpinner.getEditor();
            DecimalFormat format = editor.getFormat();
            format.setMinimumFractionDigits(pMinimumFractionDigits);
            displayformat = "0." + Util.lpad("", pMinimumFractionDigits, "0");
            editor.getTextField().setHorizontalAlignment(SwingConstants.RIGHT);
            jbInit();
            ((JSpinner.DefaultEditor) jSpinner.getEditor()).getTextField().addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent p_evt) {
                    if (!p_evt.getPropertyName().equalsIgnoreCase("ancestor")) {
                        firePropertyChange(p_evt.getPropertyName(), p_evt.getOldValue(), p_evt.getNewValue());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LabelSpinner(String label, Boolean required, int width, Boolean enabled, String toolTipText, Double minimum, Double maximum, Double stepSize) {
        snmodel = new SpinnerNumberModel(minimum, minimum, maximum, stepSize);
        jSpinner = new JSpinner(snmodel);
        this.doRequired(required);
        this.label = label;
        this.width = width;
        this.enabled = enabled;
        this.jLabelprompt.setToolTipText("<html>" + toolTipText.replace(Util.CRLF, "<br>") + "</html>");
        EditMode(enabled);
        this.changed = false;
        try {
            jbInit();
            ((JSpinner.DefaultEditor) jSpinner.getEditor()).getTextField().addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent p_evt) {
                    if (!p_evt.getPropertyName().equalsIgnoreCase("ancestor")) {
                        firePropertyChange(p_evt.getPropertyName(), p_evt.getOldValue(), p_evt.getNewValue());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LabelSpinner(String pLabelText, int pWidth, boolean pEnabled, String pToolTipText) {
        try {
            this.label = pLabelText;
            this.width = pWidth;
            this.enabled = pEnabled;
            this.jLabelprompt.setToolTipText("<html>" + pToolTipText.replace(Util.CRLF, "<br>") + "</html>");
            EditMode(pEnabled);
            this.changed = false;
            jbInit();
            ((JSpinner.DefaultEditor) jSpinner.getEditor()).getTextField().addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent p_evt) {
                    if (!p_evt.getPropertyName().equalsIgnoreCase("ancestor")) {
                        firePropertyChange(p_evt.getPropertyName(), p_evt.getOldValue(), p_evt.getNewValue());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LabelSpinner(String pLabelText, int pWidth, String pSelected, boolean pIsEnabled, String pToolTipText, double pValue, double pMinValue, double pMaxValue, double pStepSize, int pMinimumFractionDigits) {
        try {
            this.jLabelReadOnly.setText(Double.toString(pValue));
            jSpinner.setModel(snmodel);
            this.label = pLabelText;
            this.width = pWidth;
            this.enabled = pIsEnabled;
            this.jLabelprompt.setToolTipText("<html>" + pToolTipText.replace(Util.CRLF, "<br>") + "</html>");
            jComboBox1_oldValue = pSelected;
            EditMode(pIsEnabled);
            this.changed = false;
            SpinnerNumberModel model = new SpinnerNumberModel(pValue, pMinValue, pMaxValue, pStepSize);
            jSpinner = new JSpinner(model);
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor) jSpinner.getEditor();
            DecimalFormat format = editor.getFormat();
            format.setMinimumFractionDigits(pMinimumFractionDigits);
            editor.getTextField().setHorizontalAlignment(SwingConstants.RIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void pl(String s) {
        if (debug) {
            System.out.println(this.getClass().getName() + s);
        }
    }

    public void Init(double pValue, double pMinValue, double pMaxValue, double pStepSize, int pMinimumFractionDigits) {
        this.Init(pValue, pMinValue, pMaxValue, pStepSize, pMinimumFractionDigits, false);
    }

    public void Init(double pValue, double pMinValue, double pMaxValue, double pStepSize, int pMinimumFractionDigits, Boolean required) {
        try {
            this.jLabelReadOnly.setText(Double.toString(pValue));
            snmodel = new SpinnerNumberModel(pValue, pMinValue, pMaxValue, pStepSize);
            jSpinner = new JSpinner(snmodel);
            this.doRequired(required);
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor) jSpinner.getEditor();
            DecimalFormat format = editor.getFormat();
            format.setMinimumFractionDigits(pMinimumFractionDigits);
            displayformat = "0." + Util.lpad("", pMinimumFractionDigits, "0");
            editor.getTextField().setHorizontalAlignment(SwingConstants.RIGHT);
            jbInit();
            ((JSpinner.DefaultEditor) jSpinner.getEditor()).getTextField().addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent p_evt) {
                    if (!p_evt.getPropertyName().equalsIgnoreCase("ancestor")) {
                        firePropertyChange(p_evt.getPropertyName(), p_evt.getOldValue(), p_evt.getNewValue());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void EditMode(boolean p_isEnabled) {
        if (p_isEnabled) {
            this.jSpinner.setEnabled(true);
            jSpinner.setFont(new Font(this.getFont().getFontName(), 0, this.getFont().getSize()));
            this.jSpinner.setVisible(true);
            this.jLabelReadOnly.setVisible(false);
        } else {
            jLabelReadOnly.setBackground(this.ReadOnly_background);
            this.jLabelprompt.setBackground(ReadOnly_background);
            this.jLabelReadOnly.setVisible(true);
            this.jSpinner.setVisible(false);
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(400, 22));
        this.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                this_componentResized(e);
            }
        });
        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                this_mousePressed(e);
            }
        });
        this.jLabelprompt.setText(label);
        jLabelprompt.setBounds(new Rectangle(0, 0, width, 20));
        jLabelprompt.setHorizontalTextPosition(SwingConstants.CENTER);
        jLabelprompt.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jLabelprompt.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                jLabelprompt_mousePressed(e);
            }

            public void mouseReleased(MouseEvent e) {
                jLabelprompt_mouseReleased(e);
            }
        });
        jSpinner.setBounds(new Rectangle(185, 0, 215, 20));
        jSpinner.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        jSpinner.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                if (!e.getPropertyName().equalsIgnoreCase("ancestor")) {
                    pl("propertyChange : " + jLabelprompt.getText() + " " + e.getPropertyName());
                    kSpinner_propertyChange(e);
                }
            }
        });
        jSpinner.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                jSpinner_mousePressed(e);
            }
        });
        jLabelReadOnly.setBounds(new Rectangle(185, 0, 215, 20));
        jLabelReadOnly.setHorizontalTextPosition(SwingConstants.CENTER);
        jLabelReadOnly.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabelReadOnly.setOpaque(true);
        jLabelReadOnly.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.add(jSpinner, null);
        this.add(jLabelReadOnly, null);
        this.add(jLabelprompt, null);
    }

    public void componentResized(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
        this.jLabelprompt.setBounds(0, 0, this.width, this.getHeight());
        this.jSpinner.setBounds(this.width, 0, this.getWidth() - this.width, this.getHeight());
        this.jLabelReadOnly.setBounds(this.width, 0, this.getWidth() - this.width, this.getHeight());
    }

    public void componentHidden(ComponentEvent e) {
    }

    private void this_componentResized(ComponentEvent e) {
        e = null;
        this.jLabelprompt.setBounds(0, 0, this.width, this.getHeight());
        this.jSpinner.setBounds(this.width, 0, this.getWidth() - this.width, this.getHeight());
        this.jLabelReadOnly.setBounds(this.width, 0, this.getWidth() - this.width, this.getHeight());
    }

    public void setEnabled(boolean p_enabled) {
        this.enabled = p_enabled;
        EditMode(p_enabled);
    }

    public boolean isChanged() {
        return changed;
    }

    public void setWidth(int p_width) {
        this.width = p_width;
    }

    public void setText(Double p_text) {
        try {
            try {
                snmodel.setValue(p_text);
            } catch (NullPointerException e) {
            }
            try {
                FormatString(p_text);
            } catch (Exception e) {
            }
            changed = true;
        } catch (Exception e) {
        }
    }

    public Double getText() {
        try {
            return snmodel.getNumber().doubleValue();
        } catch (Exception e) {
            return Util.Double0();
        }
    }

    public Object getSelectedItem() {
        return getText();
    }

    public void propertyChange(PropertyChangeEvent p_evt) {
    }

    public String getDisplayformat() {
        return displayformat;
    }

    public void setDisplayformat(String p_Displayformat) {
        this.displayformat = p_Displayformat;
    }

    private void kSpinner_propertyChange(PropertyChangeEvent e) {
        try {
            if (e.getOldValue().toString().equalsIgnoreCase("PRESSED HOT")) {
                FormatString((Double) this.jSpinner.getValue());
                this.firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
            }
        } catch (Exception ef) {
        }
    }

    public void FormatString(Double p_text) {
        if (p_text != null) {
            DecimalFormat df = new DecimalFormat(displayformat);
            try {
                this.jLabelReadOnly.setText(df.format(p_text));
            } catch (Exception e) {
            }
        }
    }

    private void kSpinner_stateChanged(ChangeEvent e) {
    }

    public void setReadOnly_background(Color ReadOnly_background) {
        this.ReadOnly_background = ReadOnly_background;
    }

    public Color getReadOnly_background() {
        return ReadOnly_background;
    }

    private void doRequired(boolean required) {
        if (required) {
            JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) jSpinner.getEditor();
            editor.getTextField().setBackground(Theme.RequiredColor());
            jLabelReadOnly.setBackground(Theme.RequiredColor());
        } else {
            this.jSpinner.setBackground(Theme.NormalColor());
            jLabelReadOnly.setBackground(Theme.NormalColor());
            JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) jSpinner.getEditor();
            editor.getTextField().setBackground(Theme.NormalColor());
        }
    }

    private void jSpinner_propertyChange(PropertyChangeEvent e) {
        if (!e.getPropertyName().equalsIgnoreCase("ancestor")) {
            FormatString((Double) this.jSpinner.getValue());
        }
    }

    private void jSpinner_mousePressed(MouseEvent e) {
        this.firePropertyChange(e.getClass().getName(), e, e);
    }

    private void this_mousePressed(MouseEvent e) {
        this.firePropertyChange(e.getClass().getName(), e, e);
    }

    private void jLabelprompt_mousePressed(MouseEvent e) {
        this.firePropertyChange("mousePressed", e, e);
    }

    private void jLabelprompt_mouseReleased(MouseEvent e) {
        this.firePropertyChange("mouseReleased", e, e);
    }

    private void bgcolor(Color color) {
        this.jLabelReadOnly.setBackground(color);
    }

    public void flash() {
        ChangeTheStatusBarAfterXSeconds da = new ChangeTheStatusBarAfterXSeconds(this.toString(), 1);
        da.done();
        da.init();
    }

    private class ChangeTheStatusBarAfterXSeconds extends Thread {

        int value;

        int wait;

        private boolean threadDone = false;

        public ChangeTheStatusBarAfterXSeconds(String str, int wait) {
            super(str);
            value = 0;
            this.wait = wait;
            start();
        }

        public void init() {
            threadDone = false;
        }

        public void done() {
            threadDone = true;
        }

        public void run() {
            try {
                while (!threadDone && value < 5) {
                    try {
                        Thread.sleep(this.wait * 1000 / 4);
                        bgcolor(Theme.FlashColor());
                        Thread.sleep(this.wait * 1000 / 4);
                        bgcolor(ReadOnly_background);
                        value++;
                    } catch (InterruptedException e) {
                        e = null;
                    }
                }
            } catch (Exception ex) {
                ex = null;
            }
            System.out.println("Exit from thread: " + getName());
        }
    }
}
