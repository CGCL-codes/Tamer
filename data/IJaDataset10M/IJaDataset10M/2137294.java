package org.meta.shell.idebeans.editors.impl;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.syntax.jedit.*;
import org.syntax.jedit.tokenmarker.*;
import jscheme.*;
import org.meta.common.resource.FontResource;
import org.meta.shell.idebeans.editors.IDEEditor;

/**
 * Editor cum IDE for JScheme.
 * The interface class to the scheme interpreter.
 *
 * @author  V.Ganesh
 * @version 2.0 (Part of MeTA v2.0)
 */
public class JSchemeIDE extends JInternalFrame implements IDEEditor {

    protected static final String JSCHEME = "JScheme Console";

    protected JEditTextArea schemeInputArea;

    protected JEditTextArea schemeOutputArea;

    protected JLabel outputLabel, inputLabel;

    protected JPanel outputPanle, inputPanel;

    protected JButton evaluate;

    protected Scheme schemeInterpreter;

    protected InputPort inputPort;

    public JSchemeIDE() {
        super(JSCHEME, true, true, true, true);
        initComponents();
        schemeInterpreter = new Scheme(null);
    }

    /**
     * initilize the components
     */
    protected void initComponents() {
        getContentPane().setLayout(new GridLayout(2, 1));
        FontResource fonts = FontResource.getInstance();
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputLabel = new JLabel("Output area:");
        outputLabel.setFont(fonts.getTaskGroupFont());
        outputLabel.setBorder(BorderFactory.createLineBorder(Color.gray));
        outputPanel.add(outputLabel, BorderLayout.NORTH);
        schemeOutputArea = new JEditTextArea();
        schemeOutputArea.setEditable(false);
        schemeOutputArea.setToolTipText("The scheme interpreter o/p");
        schemeOutputArea.setTokenMarker(new JSchemeTokenMarker());
        schemeOutputArea.setText(";; JSchemeIDE, an IDE for Scheme\n" + ";; For details check http://jscheme.sf.net");
        outputPanel.add(schemeOutputArea, BorderLayout.CENTER);
        inputPanel = new JPanel(new BorderLayout());
        inputLabel = new JLabel("Input area:");
        inputLabel.setFont(fonts.getTaskGroupFont());
        inputLabel.setBorder(BorderFactory.createLineBorder(Color.gray));
        inputPanel.add(inputLabel, BorderLayout.NORTH);
        schemeInputArea = new JEditTextArea();
        schemeInputArea.setTokenMarker(new JSchemeTokenMarker());
        schemeInputArea.setToolTipText("Type your scheme code here");
        inputPanel.add(schemeInputArea, BorderLayout.CENTER);
        evaluate = new JButton("Evaluate!");
        evaluate.setMnemonic('E');
        evaluate.setToolTipText("Click to evaluate the code");
        evaluate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    inputPort = new InputPort(new StringReader(schemeInputArea.getText()));
                    schemeOutputArea.setText(schemeOutputArea.getText() + "\n" + schemeInputArea.getText() + "\n >>> " + schemeInterpreter.eval(inputPort.read()));
                    schemeOutputArea.setCaretPosition(schemeOutputArea.getText().length());
                    schemeInputArea.setText("");
                    schemeInputArea.setCaretPosition(0);
                } catch (Throwable e) {
                    System.err.println("Error during evaluation : " + e.toString());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(JSchemeIDE.this, "Error during evaluation : " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        inputPanel.add(evaluate, BorderLayout.EAST);
        getContentPane().add(outputPanel, BorderLayout.CENTER);
        getContentPane().add(inputPanel, BorderLayout.CENTER);
        setVisible(true);
        grabFocus();
    }

    /**
     * Opens up the specified file in editor -- not implemented
     */
    @Override
    public void open(String fileName) {
        throw new UnsupportedOperationException("Method not implemented!");
    }

    /**
     * Saves the currently loaded file -- not implemented
     */
    @Override
    public void save(String fileName) {
        throw new UnsupportedOperationException("Method not implemented!");
    }

    /**
     * the current implementation always returns false
     */
    @Override
    public boolean isDirty() {
        return false;
    }
}
