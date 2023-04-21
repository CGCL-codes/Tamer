package com.cosylab.vdct.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontSelector extends JPanel {

    private static final String nullString = "";

    private static final String exampleText = "The quick brown fox jumps over a lazy dog.";

    private static final String plainStyleString = "Plain";

    private static final String boldStyleString = "Bold";

    private static final String italicStyleString = "Italic";

    private static final String boldAndItalicStyleString = "Bold&Italic";

    FontDisplayPanel displayPanel;

    JComboBox fontsBox, fontStylesBox, fontSizesBox;

    String[] fontStyleLabels = { plainStyleString, boldStyleString, italicStyleString, boldAndItalicStyleString };

    int BOLDITALIC = Font.BOLD | Font.ITALIC;

    int[] fontStyles = { Font.PLAIN, Font.BOLD, Font.ITALIC, BOLDITALIC };

    String[] fontSizeLabels = { "8", "9", "10", "11", "12", "14", "18", "20", "22", "24", "26", "28", "36", "48", "72" };

    public FontSelector() {
        super();
        init();
    }

    public void init() {
        displayPanel = new FontDisplayPanel();
        setLayout(new GridLayout(2, 1));
        JPanel controlPanel = new JPanel();
        add(BorderLayout.NORTH, controlPanel);
        fontsBox = new JComboBox(displayPanel.fontFamilyNames);
        fontsBox.setSelectedItem("Arial");
        fontsBox.addActionListener(new ComboBoxListener());
        fontStylesBox = new JComboBox(fontStyleLabels);
        fontStylesBox.addActionListener(new ComboBoxListener());
        fontSizesBox = new JComboBox(fontSizeLabels);
        fontSizesBox.setSelectedItem("12");
        fontSizesBox.addActionListener(new ComboBoxListener());
        controlPanel.add(fontsBox);
        controlPanel.add(fontStylesBox);
        controlPanel.add(fontSizesBox);
        displayPanel = new FontDisplayPanel();
        JScrollPane scrollPane = new JScrollPane(displayPanel);
        add(BorderLayout.NORTH, controlPanel);
        add(scrollPane);
    }

    class ComboBoxListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JComboBox tempBox = (JComboBox) e.getSource();
            if (tempBox.equals(fontsBox)) {
                displayPanel.fontFamilyName = (String) tempBox.getSelectedItem();
                displayPanel.repaint();
            } else if (tempBox.equals(fontStylesBox)) {
                displayPanel.fontStyle = fontStyles[tempBox.getSelectedIndex()];
                displayPanel.repaint();
            } else if (tempBox.equals(fontSizesBox)) {
                displayPanel.fontSize = Integer.parseInt((String) tempBox.getSelectedItem());
                displayPanel.repaint();
            }
        }
    }

    public class FontDisplayPanel extends JPanel {

        String displayText;

        Font currentFont;

        String fontFamilyName;

        int fontStyle;

        int fontSize;

        GraphicsEnvironment ge;

        String[] fontFamilyNames;

        public FontDisplayPanel() {
            displayText = exampleText;
            fontFamilyName = "Arial";
            fontStyle = Font.PLAIN;
            fontSize = 12;
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            fontFamilyNames = ge.getAvailableFontFamilyNames();
            setBackground(Color.white);
        }

        public void update(Graphics g) {
            g.clearRect(0, 0, getWidth(), getHeight());
            paintComponent(g);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2D = (Graphics2D) g;
            currentFont = new Font(fontFamilyName, fontStyle, fontSize);
            g2D.setFont(currentFont);
            g2D.setColor(getForeground());
            Dimension dimension = getSize();
            if (displayText.compareTo(nullString) == 0) displayText = exampleText;
            g2D.drawString(displayText, 16, dimension.height - 16);
        }

        public void setDisplayText(String parDisplayText) {
            displayText = parDisplayText;
        }
    }

    public void setFont(Font parFont) {
        super.setFont(parFont);
        if (fontsBox != null) fontsBox.setSelectedItem(parFont.getFamily());
        if (fontStylesBox != null) {
            int style = parFont.getStyle();
            if (style == Font.PLAIN) fontStylesBox.setSelectedItem(plainStyleString); else if (style == Font.BOLD) fontStylesBox.setSelectedItem(boldStyleString); else if (style == Font.ITALIC) fontStylesBox.setSelectedItem(italicStyleString); else if (style == BOLDITALIC) fontStylesBox.setSelectedItem(boldAndItalicStyleString);
        }
        if (fontSizesBox != null) fontSizesBox.setSelectedItem(String.valueOf(parFont.getSize()));
        if (displayPanel != null) {
            displayPanel.currentFont = parFont;
            displayPanel.fontFamilyName = parFont.getFamily();
            displayPanel.fontStyle = parFont.getStyle();
            displayPanel.fontSize = parFont.getSize();
        }
    }

    public Font getFont() {
        if (displayPanel != null) return displayPanel.currentFont;
        return super.getFont();
    }

    public FontDisplayPanel getDisplayPanel() {
        return displayPanel;
    }
}
