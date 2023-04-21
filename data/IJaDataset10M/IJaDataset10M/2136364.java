package org.fest.swing.script;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import static java.awt.BorderLayout.CENTER;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingUtilities.invokeLater;

/**
 * This class is used to test running Abbot scripts using TestNG. Scripts are stored in the folder "costello".
 * <p>
 * Obtained from the <a href="http://java.sun.com/docs/books/tutorial/uiswing/learn/example3.html">Java Tutorial</a>.
 * </p>
 */
public class CelsiusConverter extends JFrame {

    private static final long serialVersionUID = 1L;

    private class ConvertActionListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            try {
                int fahrenheit = (int) ((Double.parseDouble(celsiusTextInput.getText())) * 1.8 + 32);
                fahrenheitLabel.setText(fahrenheit + " Fahrenheit");
            } catch (NumberFormatException e) {
                fahrenheitLabel.setText("Unable to convert text to Fahrenheit");
            }
        }
    }

    private JPanel converterPanel;

    private JTextField celsiusTextInput;

    private JLabel celsiusLabel, fahrenheitLabel;

    private JButton convertButton;

    public CelsiusConverter() {
        init();
        beVisible();
    }

    private void init() {
        setTitle("Convert Celsius to Fahrenheit");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(120, 40));
        addComponents();
        getRootPane().setDefaultButton(convertButton);
        getContentPane().add(converterPanel, CENTER);
    }

    private void beVisible() {
        pack();
        setVisible(true);
    }

    private void addComponents() {
        initComponents();
        converterPanel = new JPanel(new GridLayout(2, 2));
        converterPanel.add(celsiusTextInput);
        converterPanel.add(celsiusLabel);
        converterPanel.add(convertButton);
        converterPanel.add(fahrenheitLabel);
    }

    private void initComponents() {
        initCelsiusInput();
        initCelsiusLabel();
        initConvertButton();
        initFahrenheitLabel();
    }

    private void initCelsiusInput() {
        celsiusTextInput = new JTextField(2);
        celsiusTextInput.setName("celsiusTextInput");
    }

    private void initCelsiusLabel() {
        celsiusLabel = new JLabel("Celsius", LEFT);
        celsiusLabel.setName("celsiusLabel");
        celsiusLabel.setBorder(labelBorder());
    }

    private void initConvertButton() {
        convertButton = new JButton("Convert");
        convertButton.setName("convertButton");
        convertButton.addActionListener(new ConvertActionListener());
    }

    private void initFahrenheitLabel() {
        fahrenheitLabel = new JLabel("Fahrenheit", LEFT);
        fahrenheitLabel.setName("fahrenheitLabel");
        fahrenheitLabel.setBorder(labelBorder());
    }

    private Border labelBorder() {
        return createEmptyBorder(5, 5, 5, 5);
    }

    private static void createAndShowGUI() {
        setDefaultLookAndFeelDecorated(true);
        new CelsiusConverter();
    }

    public static void main(String[] args) {
        invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }
}
