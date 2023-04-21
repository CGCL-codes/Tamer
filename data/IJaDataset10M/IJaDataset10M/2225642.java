package org.dinnermate.gui.layout.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.WindowConstants;
import javax.swing.JFrame;
import org.dinnermate.gui.BasicPanel;
import org.dinnermate.gui.layout.controller.EditorConstants;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class OptionPanel extends javax.swing.JPanel {

    /**
	 * SerialID to separate file from JPanel
	 */
    private static final long serialVersionUID = -1470559099217647780L;

    /** Sets state to seat. */
    private JButton seatButton;

    /** Sets state to table */
    private JButton tableButton;

    /** Sets state to floor */
    private JButton floorButton;

    /** Saves design. */
    private JButton saveButton;

    /** Launches load screen. */
    private JButton loadButton;

    /** The default width of the option pane. */
    public static final int DEFAULT_OPTION_WIDTH = 100;

    public OptionPanel() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            GridLayout thisLayout = new GridLayout(6, 1);
            thisLayout.setHgap(5);
            thisLayout.setVgap(5);
            thisLayout.setColumns(1);
            this.setLayout(thisLayout);
            this.setPreferredSize(new Dimension(EditorConstants.DEFAULT_OPTION_WIDTH, BasicPanel.DEFAULT_HEIGHT));
            {
                seatButton = new JButton();
                this.add(seatButton);
                seatButton.setText("SEAT");
                seatButton.setBackground(EditorConstants.SEAT_COLOR);
                seatButton.setSize(100, 50);
            }
            {
                tableButton = new JButton();
                this.add(tableButton);
                tableButton.setText("TABLE");
                tableButton.setBackground(EditorConstants.TABLE_COLOR);
                tableButton.setSize(100, 50);
            }
            {
                floorButton = new JButton();
                this.add(floorButton);
                floorButton.setText("FLOOR");
                floorButton.setBackground(EditorConstants.FLOOR_COLOR);
                floorButton.setSize(100, 50);
            }
            {
                saveButton = new JButton();
                this.add(saveButton);
                saveButton.setText("SAVE");
                saveButton.setSize(100, 50);
            }
            {
                loadButton = new JButton();
                this.add(loadButton);
                loadButton.setText("LOAD");
                loadButton.setSize(100, 50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Adds ActionListener for seatbutton.
	 */
    public void addSeatButtonListener(ActionListener al) {
        seatButton.addActionListener(al);
    }

    /**
	 * Adds ActionListener for floorbutton.
	 */
    public void addFloorButtonListener(ActionListener al) {
        floorButton.addActionListener(al);
    }

    /**
	 * Adds ActionListener for seatbutton.
	 */
    public void addTableButtonListener(ActionListener al) {
        tableButton.addActionListener(al);
    }

    /**
	 * Adds ActionListener for loadbutton.
	 */
    public void addLoadButtonListener(ActionListener al) {
        loadButton.addActionListener(al);
    }

    /**
	 * Adds ActionListener for savebutton.
	 */
    public void addSaveButtonListener(ActionListener al) {
        saveButton.addActionListener(al);
    }

    /**
	 * Enables all other buttons and disables parameter button.
	 * 
	 * @param floorButton the floor button
	 * @param seatButton the seat button
	 * @param tableButton the table button
	 */
    public void disableButtons(boolean floorButton, boolean seatButton, boolean tableButton) {
        this.floorButton.setEnabled(floorButton);
        this.seatButton.setEnabled(seatButton);
        this.tableButton.setEnabled(tableButton);
    }
}
