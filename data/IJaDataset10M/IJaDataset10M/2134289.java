package net.narusas.cafelibrary.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class AddBookPanel extends javax.swing.JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8558082842112136142L;

    private JLabel jLabel1;

    private JTextField titleSearchTextField;

    private JLabel jLabel2;

    private JTextArea jTextArea1;

    private JButton writeBookButton;

    /**
	 * Auto-generated main method to display this JPanel inside a new JFrame.
	 */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new AddBookPanel());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public AddBookPanel() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            this.setPreferredSize(new java.awt.Dimension(285, 579));
            GridBagLayout thisLayout = new GridBagLayout();
            thisLayout.rowWeights = new double[] { 0.0, 0.0, 0.1, 0.1 };
            thisLayout.rowHeights = new int[] { 47, 48, 7, 7 };
            thisLayout.columnWeights = new double[] { 0.0, 0.0, 0.1 };
            thisLayout.columnWidths = new int[] { 37, 171, 7 };
            this.setLayout(thisLayout);
            this.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
            this.setSize(320, 579);
            {
                jLabel1 = new JLabel();
                this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jLabel1.setIcon(new ImageIcon("images/search.png"));
                jLabel1.setPreferredSize(new Dimension(32, 32));
            }
            {
                titleSearchTextField = new JTextField();
                this.add(titleSearchTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                titleSearchTextField.setText("Title");
            }
            {
                jLabel2 = new JLabel();
                this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jLabel2.setIcon(new ImageIcon("images/write.png"));
                jLabel2.setPreferredSize(new Dimension(32, 32));
            }
            {
                writeBookButton = new JButton();
                this.add(writeBookButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
                writeBookButton.setText("Enter");
            }
            {
                jTextArea1 = new JTextArea();
                this.add(jTextArea1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                jTextArea1.setText("Enter Detail for this new item by hand");
                jTextArea1.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
                jTextArea1.setEditable(false);
                jTextArea1.setMargin(new java.awt.Insets(2, 5, 2, 1));
                jTextArea1.setLineWrap(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JTextField getTitleSearchTextField() {
        return titleSearchTextField;
    }

    public JButton getWriteBookButton() {
        return writeBookButton;
    }
}
