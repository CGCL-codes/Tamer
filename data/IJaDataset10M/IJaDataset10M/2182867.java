package com.example;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import com.beanview.swing.SwingBeanViewPanel;

/**
 * @author $Author: wiverson $
 * @version $Revision: 1.1.1.1 $, $Date: 2006/09/19 04:22:27 $
 */
public class CollectionTestView extends JFrame {

    private static final long serialVersionUID = -4203648933760366771L;

    SwingBeanViewPanel<PeoplePicker> viewTestPanel;

    PeoplePicker optionA = new PeoplePicker();

    PeoplePicker optionB = new PeoplePicker();

    public void init() {
        viewTestPanel = new SwingBeanViewPanel<PeoplePicker>();
        setTitle("CollectionTestView Panel");
        viewTestPanel.setDataObject(optionA);
        getContentPane().add(viewTestPanel, java.awt.BorderLayout.CENTER);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        JButton setOptionAButton = new JButton("Option A Picker");
        setOptionAButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setOptionAActionPerformed(evt);
            }
        });
        JButton setOptionBButton = new JButton("Option B Picker");
        setOptionBButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setOptionBActionPerformed(evt);
            }
        });
        JButton updateOptionAButton = new JButton("Update Current Option");
        updateOptionAButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCurrentOptionPerformed(evt);
            }
        });
        JPanel actionPanel = new JPanel();
        FlowLayout actionLayout = new FlowLayout();
        actionLayout.setAlignment(FlowLayout.RIGHT);
        actionLayout.setHgap(5);
        actionPanel.setLayout(actionLayout);
        actionPanel.add(setOptionAButton);
        actionPanel.add(setOptionBButton);
        actionPanel.add(updateOptionAButton);
        this.getContentPane().add(actionPanel, BorderLayout.SOUTH);
        pack();
    }

    public void updateCurrentOptionPerformed(ActionEvent evt) {
        viewTestPanel.updateObjectFromPanel();
        this.repaint();
    }

    public void setOptionBActionPerformed(ActionEvent evt) {
        viewTestPanel.setDataObject(optionB);
        viewTestPanel.updatePanelFromObject();
        this.repaint();
    }

    public void setOptionAActionPerformed(ActionEvent evt) {
        viewTestPanel.setDataObject(optionA);
        viewTestPanel.updatePanelFromObject();
        this.repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                CollectionTestView myFrame = new CollectionTestView();
                myFrame.init();
                myFrame.setVisible(true);
            }
        });
    }

    public SwingBeanViewPanel<PeoplePicker> getViewTestPanel() {
        return viewTestPanel;
    }

    public void setViewTestPanel(SwingBeanViewPanel<PeoplePicker> viewTestPanel) {
        this.viewTestPanel = viewTestPanel;
    }
}
