package edu.xtec.adapter.test.drawactivity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import edu.xtec.adapter.Adapter;
import edu.xtec.adapter.AdapterFactory;
import edu.xtec.adapter.ExitListener;

/**
 * Example applet that uses an {@link edu.xtec.adapter.Adapter}.
 * The applet has a design mode where it is possible to specify a description and
 * a number of squares, circles and triangles.
 * In non-design mode, a number of squares, circles and triangles are plot 
 * and can be moved with the mouse.
 * This example stores and retrieves the state from the adapter.
 */
public class MainApplet extends JApplet implements ExitListener, ActionListener {

    private Adapter adapter;

    private int square = 3;

    private int circle = 3;

    private int triangle = 3;

    private String description = "";

    private DrawPanel dpanel;

    private boolean first = true;

    private boolean design = false;

    private JTextField tfSquare;

    private JTextField tfCircle;

    private JTextField tfTriangle;

    private JButton btnUpdate;

    private JTextArea taDescription;

    /**
   * Initializes the applet: creates user interface components, the adapter, and
   * reads and imposes the state.
   */
    public void init() {
        adapter = AdapterFactory.getAdapter(this);
        adapter.addExitListener(this);
        dpanel = new DrawPanel();
        dpanel.init();
        this.add(dpanel, BorderLayout.CENTER);
        loadInitialState();
    }

    /**
   * Loads and sets the state of the applet from the adapter.
   */
    private void loadInitialState() {
        if (adapter.getState() != null) {
            try {
                setState(adapter.getState());
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        design = adapter.getUserMode().equals("design");
        if (design) {
            first = true;
            initDesign();
        }
        if (!design && first) {
            dpanel.generateFigures(description.length() == 0 ? "Move the figures and enjoy!" : description, this.getSize(), square, circle, triangle);
            first = false;
        }
    }

    /**
   * Initializes the design mode. Called only in design mode.
   */
    private void initDesign() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.lightGray);
        taDescription = new JTextArea(3, 30);
        JScrollPane spane = new JScrollPane(taDescription);
        panel.setLayout(new BorderLayout());
        panel.add(spane, BorderLayout.CENTER);
        JPanel upanel = new JPanel();
        upanel.add(new JLabel("Squares"));
        upanel.add(tfSquare = new JTextField(3));
        upanel.add(new JLabel("Circles"));
        upanel.add(tfCircle = new JTextField(3));
        upanel.add(new JLabel("Triangles"));
        upanel.add(tfTriangle = new JTextField(3));
        upanel.add(btnUpdate = new JButton("Preview"));
        btnUpdate.addActionListener(this);
        panel.add(upanel, BorderLayout.SOUTH);
        this.add(panel, BorderLayout.NORTH);
        exchange(true);
    }

    /**
   * Exchanges the values from/to the user interface components to/from the
   * class fields.
   * @param get
   */
    private void exchange(boolean get) {
        if (design) {
            square = exchangeInt(get, tfSquare, square);
            circle = exchangeInt(get, tfCircle, circle);
            triangle = exchangeInt(get, tfTriangle, triangle);
            description = exchange(get, taDescription, description);
        }
    }

    /**
   * Exchange for integers.
   * @param get
   * @param tf
   * @param value
   * @return
   */
    private int exchangeInt(boolean get, JTextComponent tf, int value) {
        if (get) {
            tf.setText("" + value);
        } else {
            value = Integer.parseInt(tf.getText());
        }
        return value;
    }

    /**
   * Exchange for strings.
   * @param get
   * @param tf
   * @param value
   * @return
   */
    private String exchange(boolean get, JTextComponent tf, String value) {
        if (get) {
            tf.setText("" + value);
        } else {
            value = tf.getText();
        }
        return value;
    }

    /**
   * Callback function called by the adapter when exiting.
   */
    public void onExit() {
        exchange(false);
        adapter.setState(getState());
    }

    /**
   * Event handler.
   * @param e
   */
    public void actionPerformed(ActionEvent e) {
        exchange(false);
        dpanel.clearFigures();
        dpanel.generateFigures(description, dpanel.getSize(), square, circle, triangle);
        repaint();
    }

    /**
   * Sets the state of the applet.
   * @param str
   */
    private void setState(String str) {
        String ss[] = str.split("@@@@");
        if (ss.length > 0) {
            String ss2[] = ss[0].split("@@");
            description = ss2[0];
            square = Integer.parseInt(ss2[1]);
            circle = Integer.parseInt(ss2[2]);
            triangle = Integer.parseInt(ss2[3]);
            first = Boolean.valueOf(ss2[4]).booleanValue();
        }
        if (ss.length > 1) {
            dpanel.clearFigures();
            dpanel.loadFigures(ss[1]);
            dpanel.description = description;
        }
    }

    /**
   * Gets the state of the applet.
   * @return the state.
   */
    private String getState() {
        String str;
        str = "" + description + "@@" + square + "@@" + circle + "@@" + triangle + "@@" + first + "@@@@";
        if (!design) str += dpanel.saveState();
        return str;
    }

    /**
   * Callback function called, potentially, by the LMS, Javascript or any other
   * mecanism. The adapter contract requires that the applet implements this 
   * method.
   * @return
   */
    public String doExit() {
        return adapter.doExit();
    }

    public Adapter getAdapter() {
        return adapter;
    }
}
