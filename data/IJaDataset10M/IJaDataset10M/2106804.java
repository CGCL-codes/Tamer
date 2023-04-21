package com.jigen.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ErrorDetailsDialog extends JDialog {

    public static Dimension DEFAULT_SIZE = new Dimension(400, 250);

    public ErrorDetailsDialog(Throwable e) {
        setTitle("An error has ocurred!");
        setModal(true);
        init(e);
    }

    public ErrorDetailsDialog(JDialog owner, Throwable e) {
        super(owner, "An error has ocurred!", true);
        init(e);
    }

    private void init(Throwable e) {
        setLayout(new BorderLayout());
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        detailsArea.setText(sw.toString());
        detailsArea.setCaretPosition(0);
        detailsArea.setTabSize(4);
        JLabel label = new JLabel("Error details:");
        JScrollPane detailsPane = new JScrollPane(detailsArea);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonPanel.add(okButton);
        label.setBorder(new EmptyBorder(10, 5, 5, 5));
        detailsPane.setBorder(new CompoundBorder(new EmptyBorder(0, 5, 0, 5), detailsPane.getBorder()));
        add(label, BorderLayout.NORTH);
        add(detailsPane);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void openDialog() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - DEFAULT_SIZE.width) / 2;
        int y = (screenSize.height - DEFAULT_SIZE.height) / 2;
        setSize(DEFAULT_SIZE);
        setLocation(x, y);
        setVisible(true);
    }
}
