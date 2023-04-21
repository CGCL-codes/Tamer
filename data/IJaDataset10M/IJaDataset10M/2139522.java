package net.turingcomplete.phosphor.shared;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

/**
 * This is a JDialog subclass that allows the user to select a font, in any
 * style and size, from the list of available fonts on the system.  The 
 * dialog is modal. Display it with show(); this method does not return
 * until the user dismisses the dialog.  When show() returns, call 
 * getSelectedFont() to obtain the user's selection.  If the user clicked the
 * dialog's "Cancel" button, getSelectedFont() will return null.
 **/
public class JFontChooser extends JDialog {

    String family;

    int style;

    int size;

    Font selectedFont;

    Color selectedColor;

    String[] fontFamilies;

    ItemChooser families, styles, sizes;

    JTextArea preview;

    JButton okay, cancel, color;

    JCheckBox chkPortable;

    static final String[] styleNames = new String[] { "Plain", "Italic", "Bold", "Bold Italic" };

    static final String[] portableFontNames = new String[] { "Dialog", "DialogInput", "Monospaced", "Serif", "Sanserif", "Symbol" };

    static final Integer[] styleValues = new Integer[] { new Integer(Font.PLAIN), new Integer(Font.ITALIC), new Integer(Font.BOLD), new Integer(Font.BOLD + Font.ITALIC) };

    static final String[] sizeNames = new String[] { "8", "10", "12", "14", "18", "20", "24", "28", "32", "40", "48", "56", "64", "72" };

    static final String defaultPreviewString = "The quick brown fox jumps over the lazy dog";

    public JFontChooser(Frame owner) {
        this(owner, "Font", false, null, Color.black);
    }

    /** Create a font chooser dialog for the specified frame. */
    public JFontChooser(Frame owner, String title, boolean portableOnly, Font defFont, Color defColor) {
        super(owner, title);
        setModal(true);
        chkPortable = new JCheckBox("Portable fonts only");
        chkPortable.setSelected(portableOnly);
        preview = new JTextArea(defaultPreviewString, 5, 40);
        setFontFamilies();
        family = fontFamilies[0];
        style = Font.PLAIN;
        size = 14;
        selectedFont = new Font(family, style, size);
        if (defFont != null) setSelectedFont(defFont);
        if (defColor != null) setFontColor(defColor);
        families = new ItemChooser("Family", fontFamilies, null, 0, ItemChooser.LIST);
        styles = new ItemChooser("Style", styleNames, styleValues, 0, ItemChooser.LIST);
        sizes = new ItemChooser("Size", sizeNames, null, 4, ItemChooser.LIST);
        setListIndexes();
        families.addItemChooserListener(new ItemChooser.Listener() {

            public void itemChosen(ItemChooser.Event e) {
                setFontFamily((String) e.getSelectedValue());
            }
        });
        styles.addItemChooserListener(new ItemChooser.Listener() {

            public void itemChosen(ItemChooser.Event e) {
                setFontStyle(((Integer) e.getSelectedValue()).intValue());
            }
        });
        sizes.addItemChooserListener(new ItemChooser.Listener() {

            public void itemChosen(ItemChooser.Event e) {
                setFontSize(Integer.parseInt((String) e.getSelectedValue()));
            }
        });
        preview.setFont(selectedFont);
        okay = new JButton("Okay");
        cancel = new JButton("Cancel");
        color = new JButton("Color...");
        okay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                hide();
            }
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectedFont = null;
                selectedColor = null;
                hide();
            }
        });
        color.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setFontColor(JColorChooser.showDialog(getContentPane(), "Font Color", selectedColor));
            }
        });
        chkPortable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setFontFamilies();
                setListIndexes();
            }
        });
        Box choosersBox = Box.createHorizontalBox();
        choosersBox.add(Box.createHorizontalStrut(15));
        choosersBox.add(families);
        choosersBox.add(Box.createHorizontalStrut(15));
        choosersBox.add(styles);
        choosersBox.add(Box.createHorizontalStrut(15));
        choosersBox.add(sizes);
        choosersBox.add(Box.createHorizontalStrut(15));
        choosersBox.add(Box.createGlue());
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createGlue());
        buttonBox.add(chkPortable);
        buttonBox.add(Box.createGlue());
        buttonBox.add(color);
        buttonBox.add(Box.createGlue());
        buttonBox.add(okay);
        buttonBox.add(Box.createGlue());
        buttonBox.add(cancel);
        buttonBox.add(Box.createGlue());
        Container contentPane = getContentPane();
        contentPane.add(new JScrollPane(preview), BorderLayout.CENTER);
        contentPane.add(choosersBox, BorderLayout.NORTH);
        contentPane.add(buttonBox, BorderLayout.SOUTH);
        pack();
        setSize(400, 300);
        GUIHelpers.center(owner, this);
    }

    /**
     * Call this method after show() to obtain the user's selection.  If the
     * user used the "Cancel" button, this will return null
     **/
    public Font getSelectedFont() {
        return selectedFont;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public String getFontFamily() {
        return family;
    }

    public int getFontStyle() {
        return style;
    }

    public int getFontSize() {
        return size;
    }

    public void setFontFamily(String name) {
        family = name;
        changeFont();
    }

    public void setFontStyle(int style) {
        this.style = style;
        changeFont();
    }

    public void setFontSize(int size) {
        this.size = size;
        changeFont();
    }

    public void setFontColor(Color color) {
        selectedColor = color;
        changeFont();
    }

    private void setFontFamilies() {
        if (chkPortable.isSelected()) fontFamilies = portableFontNames; else {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            fontFamilies = env.getAvailableFontFamilyNames();
        }
    }

    class FontComparator implements Comparator {

        public int compare(Object a, Object b) {
            String s1 = (String) a;
            String s2 = (String) b;
            int i = s2.indexOf('.');
            if (i >= 0) {
                s2 = s2.substring(0, i);
            }
            return s1.compareToIgnoreCase(s2);
        }

        public boolean equals(Object o) {
            return false;
        }
    }

    private void setListIndexes() {
        Arrays.sort(fontFamilies);
        int font = Arrays.binarySearch(fontFamilies, selectedFont.getFamily(), new FontComparator());
        Arrays.sort(sizeNames, new Comparator() {

            public int compare(Object a, Object b) {
                String s1 = (String) a;
                String s2 = (String) b;
                return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
            }

            public boolean equals(Object o) {
                return false;
            }
        });
        int size = Arrays.binarySearch(sizeNames, new Integer(selectedFont.getSize()), new Comparator() {

            public int compare(Object a, Object b) {
                String s = (String) a;
                Integer i = (Integer) b;
                return Integer.valueOf(s).compareTo(b);
            }

            public boolean equals(Object o) {
                return false;
            }
        });
        families.setData(fontFamilies);
        if (font <= 0) {
            setFontFamily(fontFamilies[0]);
            families.setSelectedIndex(0);
        } else families.setSelectedIndex(font);
        sizes.setSelectedIndex(size);
        switch(selectedFont.getStyle()) {
            case Font.PLAIN:
                styles.setSelectedIndex(0);
                break;
            case Font.ITALIC:
                styles.setSelectedIndex(1);
                break;
            case Font.BOLD:
                styles.setSelectedIndex(2);
                break;
            case Font.ITALIC | Font.BOLD:
                styles.setSelectedIndex(3);
                break;
            default:
                Assert.that(false, "unknown font style");
        }
    }

    public void setSelectedFont(Font font) {
        if (Arrays.binarySearch(fontFamilies, font.getFamily(), new FontComparator()) >= 0 || Arrays.binarySearch(fontFamilies, font.getName(), new FontComparator()) >= 0) {
            selectedFont = font;
            family = font.getFamily();
            style = font.getStyle();
            size = font.getSize();
            preview.setFont(font);
        }
    }

    protected void changeFont() {
        selectedFont = new Font(family, style, size);
        preview.setForeground(selectedColor);
        preview.setFont(selectedFont);
    }

    public boolean isModal() {
        return true;
    }

    /** This inner class demonstrates the use of FontChooser */
    public static class Demo {

        public static void main(String[] args) {
            final JFrame frame = new JFrame("demo");
            final JButton button = new JButton("Push Me!");
            final JFontChooser chooser = new JFontChooser(frame);
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    chooser.show();
                    Font font = chooser.getSelectedFont();
                    Color color = chooser.getSelectedColor();
                    if (font != null) button.setFont(font);
                    if (color != null) button.setForeground(color);
                }
            });
            frame.getContentPane().add(button);
            frame.setSize(200, 100);
            frame.show();
        }
    }
}
