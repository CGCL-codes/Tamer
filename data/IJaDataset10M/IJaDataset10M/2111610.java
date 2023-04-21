package com.jmonkey.office.lexi.support;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import com.jmonkey.office.lexi.support.editors.OfficeStyleConstants;

/**
 * This class implements a Dialog for selecting page style attributes.
 */
public final class PageChooser extends AttributeChooser implements ActionListener {

    private JTextField m_pageHeight, m_pageWidth;

    private boolean m_outcome;

    /**
   * @param owner the owner frame for the chooser.
   * @param initial the initial paragraph attributes
   */
    public PageChooser(JFrame owner, AttributeSet initial) {
        super(owner, "Page Chooser");
        setSize(700, 500);
        init(owner, initial);
    }

    /**
   * Handle the actions associated with the 'ok' and the 'cancel' buttons
   */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("can-button")) {
            m_outcome = false;
            dispose();
        } else if (command.equals("ok-button")) {
            m_outcome = true;
            dispose();
        }
    }

    public boolean getOutcome() {
        return m_outcome;
    }

    /**
   * Sets us up with the panels for this style chooser
   */
    private void init(Component c, AttributeSet initial) {
        JPanel main = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel propertyPanel = new JPanel();
        main.setLayout(new BorderLayout());
        buttonPanel.setLayout(new FlowLayout());
        propertyPanel.setLayout(new GridLayout(2, 2));
        m_pageHeight = new JTextField();
        m_pageWidth = new JTextField();
        propertyPanel.add(m_pageHeight);
        propertyPanel.add(new JLabel("Page height"));
        propertyPanel.add(m_pageWidth);
        propertyPanel.add(new JLabel("Page width"));
        JButton okButton = new JButton("OK");
        okButton.setActionCommand("ok-button");
        okButton.addActionListener(this);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("can-button");
        cancelButton.addActionListener(this);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        main.add(propertyPanel, BorderLayout.CENTER);
        main.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(main);
        pack();
        setLocationRelativeTo(c);
    }

    public AttributeSet getAttributes() {
        MutableAttributeSet res = new SimpleAttributeSet();
        if (isValidMeasure(m_pageHeight)) {
            OfficeStyleConstants.setPageHeight(res, getMeasure(m_pageHeight));
        }
        if (isValidMeasure(m_pageWidth)) {
            OfficeStyleConstants.setPageWidth(res, getMeasure(m_pageWidth));
        }
        return res;
    }
}
