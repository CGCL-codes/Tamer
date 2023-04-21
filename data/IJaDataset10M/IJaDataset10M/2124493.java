package ee.ut.logic;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import ee.ut.logic.*;
import ee.ut.logic.gui.*;

/**
 * Startingpoint of the applet.
 * @author Janno Liivak
 * @version 1.8
 */
public class Main extends Applet {

    /** applets layout */
    public GridBagLayout appletsLayout;

    /** constraints of applets layout */
    public GridBagConstraints appletsConstraints;

    /** titlebar */
    public Label headingLabel;

    /** status of the system */
    public Label statusLabel;

    /** choose task to perform */
    public Choice taskChoice;

    /** you can input text in this thing */
    public TextArea inputArea;

    /** output area */
    public TextArea outputArea;

    /** button for computing */
    public Button buttonCompute;

    /** button for clearing the fields */
    public Button buttonClear;

    /** button for displaying information about project participants */
    public Button buttonAbout;

    /**
     * Construct the applet
     * Giving values to non-static variables
     */
    public Main() {
        appletsLayout = new GridBagLayout();
        appletsConstraints = new GridBagConstraints();
        headingLabel = new Label();
        statusLabel = new Label();
        taskChoice = new Choice();
        inputArea = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
        inputArea.setText("Sisesta valem");
        outputArea = new TextArea();
        buttonCompute = new Button();
        buttonClear = new Button();
        buttonAbout = new Button();
    }

    /** Initialize the applet*/
    public void init() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Component initialization
     * @throws Exception when something bad happens
     */
    private void jbInit() throws Exception {
        this.setBackground(new Color(192, 220, 192));
        this.setLayout(appletsLayout);
        appletsConstraints.gridx = 0;
        appletsConstraints.gridy = 0;
        appletsConstraints.gridwidth = 2;
        appletsConstraints.gridheight = 1;
        appletsConstraints.weightx = 0.0;
        appletsConstraints.weighty = 0.0;
        appletsConstraints.anchor = GridBagConstraints.CENTER;
        appletsConstraints.fill = GridBagConstraints.HORIZONTAL;
        appletsConstraints.insets = new Insets(0, 10, 10, 10);
        appletsConstraints.ipadx = 3;
        appletsConstraints.ipady = 3;
        headingLabel.setAlignment(Label.CENTER);
        headingLabel.setFont(new java.awt.Font("Monospaced", 1, 18));
        headingLabel.setText("Logic CAI 1.1");
        this.add(headingLabel, appletsConstraints);
        appletsConstraints.gridy = 2;
        appletsConstraints.gridwidth = 1;
        appletsConstraints.gridheight = 1;
        appletsConstraints.anchor = GridBagConstraints.WEST;
        appletsConstraints.insets = new Insets(0, 10, 10, 10);
        appletsConstraints.ipadx = 0;
        appletsConstraints.ipady = 0;
        inputArea.setColumns(40);
        inputArea.setRows(3);
        inputArea.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 12));
        inputArea.addKeyListener(new InputKeyListener(inputArea, buttonClear));
        this.add(inputArea, appletsConstraints);
        appletsConstraints.gridx = 1;
        appletsConstraints.anchor = GridBagConstraints.NORTHEAST;
        appletsConstraints.fill = GridBagConstraints.NONE;
        appletsConstraints.insets = new Insets(0, 0, 10, 10);
        taskChoice.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 12));
        taskChoice.addItem("-- Vali funktsioon --");
        taskChoice.addItem("T�ielik t�ev��rtustabel");
        taskChoice.addItem("T�ev��rtuste veerg");
        taskChoice.addItem("Valemi liik");
        taskChoice.addItem("T�ielik disjunktiivne normaalkuju");
        taskChoice.addItem("T�ielik konjunktiivne normaalkuju");
        taskChoice.addItem("S�ntaksipuu");
        taskChoice.addItem("Valemi infiks-kuju");
        taskChoice.addItem("Valemi prefiks-kuju");
        taskChoice.addItem("Valemi postfiks-kuju");
        taskChoice.addItem("Optimiseeritud valem");
        taskChoice.addKeyListener(new ChoiceKeyListener(taskChoice, inputArea));
        this.add(taskChoice, appletsConstraints);
        appletsConstraints.gridx = 0;
        appletsConstraints.gridy = 3;
        appletsConstraints.gridheight = 3;
        appletsConstraints.anchor = GridBagConstraints.WEST;
        appletsConstraints.insets = new Insets(0, 10, 10, 10);
        outputArea.setColumns(60);
        outputArea.setRows(12);
        outputArea.setEditable(false);
        outputArea.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 12));
        outputArea.addKeyListener(new OutputKeyListener(outputArea, taskChoice));
        this.add(outputArea, appletsConstraints);
        appletsConstraints.gridx = 1;
        appletsConstraints.gridy = 3;
        appletsConstraints.gridheight = 1;
        appletsConstraints.anchor = GridBagConstraints.SOUTH;
        appletsConstraints.fill = GridBagConstraints.HORIZONTAL;
        appletsConstraints.insets = new Insets(150, 80, 0, 80);
        buttonCompute.setFont(new java.awt.Font("Dialog", 1, 12));
        buttonCompute.setLabel("Arvuta");
        buttonCompute.addActionListener(new ComputeButtonListener(this, inputArea, outputArea, taskChoice, statusLabel));
        buttonCompute.addKeyListener(new ComputeKeyListener(buttonCompute, outputArea));
        this.add(buttonCompute, appletsConstraints);
        appletsConstraints.gridy = 4;
        appletsConstraints.anchor = GridBagConstraints.NORTH;
        appletsConstraints.insets = new Insets(5, 80, 0, 80);
        buttonClear.setFont(new java.awt.Font("Dialog", 1, 12));
        buttonClear.setLabel("Puhasta");
        buttonClear.addActionListener(new ClearButtonListener(this, inputArea, outputArea));
        buttonClear.addKeyListener(new ClearKeyListener(buttonClear, buttonCompute));
        this.add(buttonClear, appletsConstraints);
        appletsConstraints.gridy = 5;
        appletsConstraints.insets = new Insets(5, 80, 10, 80);
        buttonAbout.setFont(new java.awt.Font("Dialog", 1, 12));
        buttonAbout.setLabel("Tegijad");
        buttonAbout.addActionListener(new AboutButtonListener());
        buttonAbout.addKeyListener(new AboutKeyListener(buttonAbout, buttonClear));
        this.add(buttonAbout, appletsConstraints);
        appletsConstraints.gridx = 0;
        appletsConstraints.gridy = 6;
        appletsConstraints.gridwidth = 2;
        appletsConstraints.anchor = GridBagConstraints.WEST;
        appletsConstraints.insets = new Insets(10, 10, 0, 10);
        appletsConstraints.ipadx = 3;
        appletsConstraints.ipady = 3;
        statusLabel.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 12));
        statusLabel.setBackground(new Color(192, 210, 192));
        statusLabel.setText("Valmis");
        this.add(statusLabel, appletsConstraints);
    }

    /**
     * Start the applet
     */
    public void start() {
    }

    /**
     * Stop the applet
     */
    public void stop() {
    }

    /**
     * Destroy the applet
     */
    public void destroy() {
    }
}
