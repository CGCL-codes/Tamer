package com.apocalyptech.minecraft.xray.dialog;

import com.apocalyptech.minecraft.xray.XRay;
import com.apocalyptech.minecraft.xray.MinecraftLevel;
import com.apocalyptech.minecraft.xray.FirstPersonCameraController;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Properties;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.JSeparator;
import javax.swing.JComponent;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.AbstractAction;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 */
public class JumpDialog extends JFrame {

    private static final long serialVersionUID = -670931768263974900L;

    private static final int FRAMEWIDTH = 420;

    private static final int FRAMEHEIGHT = 220;

    private JSpinner xSpinner;

    private JSpinner zSpinner;

    private SpinnerNumberModel xSpinnerModel;

    private SpinnerNumberModel zSpinnerModel;

    private ButtonGroup positionSelectGroup;

    private JRadioButton positionButton;

    private JRadioButton chunkButton;

    private JButton runButton;

    private JButton exitButton;

    private JButton syncButton;

    private GridBagLayout gridBagLayoutManager;

    private JPanel basicPanel;

    private static XRay xray_obj;

    private static boolean dialog_showing = false;

    private static JumpDialog jump_dialog;

    public static boolean selectedChunk;

    public static int selectedX;

    public static int selectedZ;

    public static Image iconImage;

    /***
	 * Centers this dialog on the screen
	 */
    private void centerDialogOnScreen() {
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension screenSize = t.getScreenSize();
        int x = (screenSize.width / 2) - (this.getWidth() / 2);
        int y = (screenSize.height / 2) - (this.getHeight() / 2);
        gridBagLayoutManager = new GridBagLayout();
        this.setLocation(x, y);
        this.setAlwaysOnTop(true);
    }

    /***
	 * Layouts all the controls and labels on the dialog using a gridbaglayout
	 */
    private void layoutControlsOnDialog() {
        basicPanel = new JPanel();
        this.getContentPane().setLayout(gridBagLayoutManager);
        basicPanel.setLayout(gridBagLayoutManager);
        GridBagConstraints c = new GridBagConstraints();
        JLabel xLabel = new JLabel("X Position: ");
        JLabel zLabel = new JLabel("Z Position: ");
        float flabel = 0.1f;
        float flist = 1.9f;
        int current_grid_y = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.weighty = .1f;
        positionSelectGroup = new ButtonGroup();
        positionButton = new JRadioButton("Jump to position");
        positionButton.setSelected(true);
        chunkButton = new JRadioButton("Jump to chunk");
        positionSelectGroup.add(positionButton);
        positionSelectGroup.add(chunkButton);
        c.insets = new Insets(5, 15, 5, 5);
        c.gridx = 0;
        c.gridwidth = 3;
        current_grid_y++;
        c.gridy = current_grid_y;
        addComponent(basicPanel, positionButton, c);
        current_grid_y++;
        c.gridy = current_grid_y;
        addComponent(basicPanel, chunkButton, c);
        current_grid_y++;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1.0f;
        c.gridx = 0;
        c.gridy = current_grid_y;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        addComponent(basicPanel, Box.createVerticalStrut(5), c);
        addComponent(basicPanel, new JSeparator(SwingConstants.HORIZONTAL), c);
        addComponent(basicPanel, Box.createVerticalStrut(5), c);
        current_grid_y++;
        c.gridx = 2;
        c.gridy = current_grid_y;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.weightx = 0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        addComponent(basicPanel, syncButton, c);
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = flabel;
        c.gridx = 0;
        c.gridy = current_grid_y;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        addComponent(basicPanel, xLabel, c);
        xSpinnerModel = new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        xSpinner = new JSpinner(xSpinnerModel);
        c.weightx = flist;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = current_grid_y;
        addComponent(basicPanel, xSpinner, c);
        current_grid_y++;
        c.weightx = flabel;
        c.gridx = 0;
        c.gridy = current_grid_y;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        addComponent(basicPanel, zLabel, c);
        zSpinnerModel = new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        zSpinner = new JSpinner(zSpinnerModel);
        c.weightx = flist;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = current_grid_y;
        addComponent(basicPanel, zSpinner, c);
        c.weightx = 1.0f;
        c.weighty = .1f;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        addComponent(this.getContentPane(), basicPanel, c);
        c.insets = new Insets(5, 15, 5, 15);
        c.gridwidth = 1;
        c.weightx = flabel;
        c.weighty = 1.0f;
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        addComponent(this.getContentPane(), exitButton, c);
        c.weightx = flist;
        c.weighty = 1.0f;
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        addComponent(this.getContentPane(), runButton, c);
    }

    /***
	 * Adds a component to the container and updates the constraints for that component
	 * @param root The contiainer to add the component to
	 * @param comp The component to add to the container
	 * @param constraints The constraints which affect the component
	 */
    private void addComponent(Container root, Component comp, GridBagConstraints constraints) {
        gridBagLayoutManager.setConstraints(comp, constraints);
        root.add(comp);
    }

    /***
	 * Builds the Go and Exit Buttons and attaches the actions to them
	 */
    private void buildButtons() {
        JRootPane rootPane = this.getRootPane();
        runButton = new JButton("Jump");
        runButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialogOK();
            }
        });
        KeyStroke enterStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterStroke, "ENTER");
        rootPane.getActionMap().put("ENTER", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                dialogOK();
            }
        });
        exitButton = new JButton("Cancel");
        exitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialogCancel();
            }
        });
        KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, "ESCAPE");
        rootPane.getActionMap().put("ESCAPE", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                dialogCancel();
            }
        });
        syncButton = new JButton("Sync to Camera");
        syncButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                syncToCamera();
            }
        });
    }

    /**
	 * Synchronizes our spinbuttons to the current camera position.
	 */
    private void syncToCamera() {
        FirstPersonCameraController camera = JumpDialog.xray_obj.getCamera();
        int cam_x = -(int) camera.getPosition().x;
        int cam_z = -(int) camera.getPosition().z;
        if (chunkButton.isSelected()) {
            cam_x = MinecraftLevel.getChunkX(cam_x);
            cam_z = MinecraftLevel.getChunkZ(cam_z);
        }
        xSpinnerModel.setValue(cam_x);
        zSpinnerModel.setValue(cam_z);
    }

    /**
	 * Actions to perform if the "Jump" button is hit, or otherwise triggered.
	 */
    private void dialogOK() {
        setSelectedValues();
        setVisible(false);
        dispose();
        JumpDialog.xray_obj.jump_dialog_trigger = true;
        JumpDialog.dialog_showing = false;
        synchronized (JumpDialog.this) {
            JumpDialog.this.notify();
        }
    }

    /**
	 * Actions to perform if the "Cancel" button is hit, or otherwise triggered.
	 */
    private void dialogCancel() {
        setVisible(false);
        dispose();
        JumpDialog.dialog_showing = false;
        synchronized (JumpDialog.this) {
            JumpDialog.this.notify();
        }
    }

    /***
	 * Sets the selected values to the static properties of this resolution dialog
	 */
    private void setSelectedValues() {
        JumpDialog.selectedX = this.xSpinnerModel.getNumber().intValue();
        JumpDialog.selectedZ = this.zSpinnerModel.getNumber().intValue();
        JumpDialog.selectedChunk = this.chunkButton.isSelected();
    }

    /***
	 * Creates a new JumpDialog
	 * @param windowName the title of the dialog
	 */
    protected JumpDialog(String windowName) {
        super(windowName);
        if (JumpDialog.iconImage != null) this.setIconImage(JumpDialog.iconImage);
        this.setSize(FRAMEWIDTH, FRAMEHEIGHT);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                dialogCancel();
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }
        });
        this.setMinimumSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
        centerDialogOnScreen();
        buildButtons();
        layoutControlsOnDialog();
        validate();
        this.setVisible(true);
    }

    /***
	 * Pops up the dialog window
	 * @param windowName the title of the dialog
	 */
    public static void presentDialog(String windowName, XRay xray_obj) {
        if (!dialog_showing) {
            JumpDialog.xray_obj = xray_obj;
            JumpDialog.dialog_showing = true;
            JumpDialog.jump_dialog = new JumpDialog(windowName);
        }
    }

    public static void closeDialog() {
        if (JumpDialog.dialog_showing && JumpDialog.jump_dialog != null) {
            JumpDialog.jump_dialog.dialogCancel();
        }
    }
}
