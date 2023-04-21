package net.narusas.cafelibrary.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class BookAddMethodSelectionPanel extends javax.swing.JPanel {

    private JLabel jLabel1;

    private JLabel jLabel2;

    private JTextField searchTextField;

    private JButton addBookBySearchButton;

    private JButton addBookByWriteButton;

    private JTextArea jLabel4;

    private JLabel jLabel3;

    private JPanel contentsPanel;

    /**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new BookAddMethodSelectionPanel());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public BookAddMethodSelectionPanel() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            GridBagLayout thisLayout = new GridBagLayout();
            this.setPreferredSize(new java.awt.Dimension(290, 300));
            thisLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.1 };
            thisLayout.rowHeights = new int[] { 7, 7, 7, 7 };
            thisLayout.columnWeights = new double[] { 0.0, 0.1, 0.0 };
            thisLayout.columnWidths = new int[] { 7, 7, 7 };
            this.setLayout(new BorderLayout());
            this.setSize(290, 300);
            this.setBorder(BorderFactory.createTitledBorder("추가방법 선택"));
            contentsPanel = new JPanel();
            contentsPanel.setLayout(thisLayout);
            contentsPanel.setBackground(new java.awt.Color(255, 255, 255));
            this.add(contentsPanel, BorderLayout.CENTER);
            {
                jLabel1 = new JLabel();
                contentsPanel.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
                jLabel1.setIcon(new ImageIcon("images/search.png"));
            }
            {
                jLabel2 = new JLabel();
                contentsPanel.add(jLabel2, new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                jLabel2.setText("------------------------------------------------------------");
                jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
            }
            {
                searchTextField = new JTextField();
                contentsPanel.add(searchTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                searchTextField.setText("Title or Author");
            }
            {
                addBookBySearchButton = new JButton();
                contentsPanel.add(addBookBySearchButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
                addBookBySearchButton.setText("제목으로 검색");
                addBookBySearchButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
            }
            {
                jLabel3 = new JLabel();
                contentsPanel.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jLabel3.setIcon(new ImageIcon("images/write.png"));
            }
            {
                jLabel4 = new JTextArea();
                contentsPanel.add(jLabel4, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jLabel4.setText("새로운 책에 대한 정보를 직접 입력합니다.");
                jLabel4.setLineWrap(true);
                jLabel4.setSize(130, 49);
            }
            {
                addBookByWriteButton = new JButton();
                contentsPanel.add(addBookByWriteButton, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
                addBookByWriteButton.setText("상세정보 입력");
                addBookByWriteButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JTextField getSearchTextField() {
        return searchTextField;
    }

    public JButton getAddBookByWriteButton() {
        return addBookByWriteButton;
    }

    public JButton getAddBookBySearchButton() {
        return addBookBySearchButton;
    }
}
