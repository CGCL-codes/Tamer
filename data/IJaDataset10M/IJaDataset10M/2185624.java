package com.bnpparibas.tools.ant.logic.select;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

/**
 * @author V_Thoule
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SelectFrame extends JDialog implements ActionListener {

    private javax.swing.JPanel ivjContentPane = null;

    private javax.swing.JPanel ivjJPanel = null;

    private javax.swing.JButton ivjJButtonOk = null;

    private javax.swing.JButton ivjJButtonCancel = null;

    private javax.swing.JPanel ivjJPanel1 = null;

    private javax.swing.JRadioButton[] ivjJRadioButton = null;

    private javax.swing.JPanel ivjContentPane1 = null;

    private javax.swing.JPanel ivjContentPane12 = null;

    private javax.swing.ButtonGroup group = null;

    private ChoiceDef m_Choices = null;

    private int defaultParam = 0;

    private boolean cancelled;

    /**
	 * This method initializes 
	 * 
	 */
    public SelectFrame(ChoiceDef Choices) {
        super(new JFrame(), true);
        m_Choices = Choices;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setContentPane(getIvjContentPane());
        setLocation(100, 100);
        setSize(338, m_Choices.Choices.size() * 30 + 50);
        initConnection();
        setVisible(true);
    }

    private void initConnection() {
        getIvjJButtonOk().addActionListener(this);
        getIvjJButtonCancel().addActionListener(this);
        this.addWindowListener(new WindowAdapter() {

            /**
			 * @see java.awt.event.WindowAdapter#windowClosing(WindowEvent)
			 */
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                cancelled = true;
            }
        });
    }

    /**
	 * This method initializes ivjContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private javax.swing.JPanel getIvjContentPane() {
        if (ivjContentPane == null) {
            ivjContentPane = new javax.swing.JPanel();
            java.awt.BorderLayout layBorderLayout_1 = new java.awt.BorderLayout();
            ivjContentPane.setLayout(layBorderLayout_1);
            ivjContentPane.add(getIvjJPanel(), java.awt.BorderLayout.SOUTH);
            ivjContentPane.add(getIvjJPanelButton(), java.awt.BorderLayout.CENTER);
        }
        return ivjContentPane;
    }

    /**
	 * This method initializes ivjJPanel
	 * 
	 * @return javax.swing.JPanel
	 */
    private javax.swing.JPanel getIvjJPanel() {
        if (ivjJPanel == null) {
            ivjJPanel = new javax.swing.JPanel();
            ivjJPanel.add(getIvjJButtonOk(), null);
            ivjJPanel.add(getIvjJButtonCancel(), null);
        }
        return ivjJPanel;
    }

    /**
	 * This method initializes ivjJButton
	 * 
	 * @return javax.swing.JButton
	 */
    private javax.swing.JButton getIvjJButtonOk() {
        if (ivjJButtonOk == null) {
            ivjJButtonOk = new javax.swing.JButton();
            ivjJButtonOk.setText("Ok");
        }
        return ivjJButtonOk;
    }

    /**
	 * This method initializes ivjJButton1
	 * 
	 * @return javax.swing.JButton
	 */
    private javax.swing.JButton getIvjJButtonCancel() {
        if (ivjJButtonCancel == null) {
            ivjJButtonCancel = new javax.swing.JButton();
            ivjJButtonCancel.setText("Cancel");
        }
        return ivjJButtonCancel;
    }

    /**
	 * This method initializes ivjJPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
    private javax.swing.JPanel getIvjJPanelButton() {
        if (ivjJPanel1 == null) {
            ivjJPanel1 = new javax.swing.JPanel();
            java.awt.GridLayout layGridLayout_6 = new java.awt.GridLayout();
            layGridLayout_6.setRows(m_Choices.Choices.size());
            ivjJPanel1.setLayout(layGridLayout_6);
            group = new ButtonGroup();
            int i = 0;
            ivjJRadioButton = new javax.swing.JRadioButton[m_Choices.Choices.size()];
            for (Iterator iter = m_Choices.Choices.iterator(); iter.hasNext(); ) {
                Choice aChoice = (Choice) iter.next();
                ivjJPanel1.add(getNewJRadioButton(i, aChoice), null);
                if (this.defaultParam == i) {
                    ivjJRadioButton[i].setSelected(true);
                }
                if (m_Choices.isExclusif) {
                    group.add(ivjJRadioButton[i]);
                }
                i++;
            }
        }
        return ivjJPanel1;
    }

    /**
	 * This method initializes ivjJRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
    public javax.swing.JRadioButton[] getJRadioButtons() {
        return ivjJRadioButton;
    }

    private javax.swing.JRadioButton getNewJRadioButton(int i, Choice aChoice) {
        ivjJRadioButton[i] = new javax.swing.JRadioButton();
        ivjJRadioButton[i].setText(aChoice.Text);
        return ivjJRadioButton[i];
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getIvjJButtonOk()) {
        } else if (e.getSource() == getIvjJButtonCancel()) {
            System.out.println("SelectFrame cancelled");
            cancelled = true;
        }
        setVisible(false);
    }

    /**
	 * This method initializes ivjContentPane12
	 * 
	 * @return javax.swing.JPanel
	 */
    private javax.swing.JPanel getIvjContentPane12() {
        if (ivjContentPane12 == null) {
            ivjContentPane12 = new javax.swing.JPanel();
            java.awt.BorderLayout layBorderLayout_8 = new java.awt.BorderLayout();
            ivjContentPane12.setLayout(layBorderLayout_8);
        }
        return ivjContentPane12;
    }

    public static void main(String[] args) {
        ChoiceDef cd = new ChoiceDef();
        cd.AddChoice("Essai 0", "0");
        cd.AddChoice("Essai 1", "1");
        cd.IsExclusif(true);
        SelectFrame f = new SelectFrame(cd);
        System.exit(0);
        System.out.println("Apr�s");
    }

    /**
	 * Returns the cancelled.
	 * @return boolean
	 */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
	 * Sets the defaultParam.
	 * @param defaultParam The defaultParam to set
	 */
    public void setDefaultParam(int defaultParam) {
        this.defaultParam = defaultParam;
    }
}
