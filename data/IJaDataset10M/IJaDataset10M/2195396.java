package com.ozten.font;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.ozten.util.SwingUtil;

/**
 * <p>
 * <b>JFontChooser</b> - aka OzFontChooser a font selection widget. Please note
 * this software is licensed under the LGPL. Please see <a
 * href="./LICENSE.txt">LICENSE.txt</a> distributed with this jar. This font
 * selection dialog is intended to work just like the java.awt.JColorChooser.
 * </p>
 * 
 * <p>
 * JFontChooser provides a pane of controls designed to allow a user to
 * manipulate and select a color.
 * </p>
 * 
 * <p>
 * This class provides three levels of API:<br>
 * 
 * 1. A static convenience method which shows a modal color-chooser dialog and
 * returns the color selected by the user.<br>
 * 2. <b>TODO:</b> A static convenience method for creating a color-chooser
 * dialog where ActionListeners can be specified to be invoked when the user
 * presses one of the dialog buttons.<br>
 * 3. <b>TODO:</b> The ability to create instances of JColorChooser panes
 * directly (within any container). PropertyChange listeners can be added to
 * detect when the current "color" property changes.
 * </p>
 * 
 * This class includes a main method for testing the widget. To run type<br>
 * <br>
 * <code>java -jar JFontChooser</code> <br>
 * <br>
 * to use in your app... put this jar in your classpath and then type<br>
 * <br>
 * <code>import com.ozten.font.JFontChooser;</code> <br>
 * <br>
 * then when you want to get a font from the user..<br>
 * <br>
 * <code>Font f = JFontChooser.showDialog(frame);</code> <br>
 * <br>
 * Please send suggestions, and bug reports to me
 * 
 * @author <a href="mailto:austinking@hotmail.com">Ozten</a> of <a
 *         href="http://www.cruftworks.com/software/">CruftWorks.com</a>
 * @version 0.21 OTHER TODOS and questions... <br>
 *          1 - Do I need to code up a JFontChooser.AccessibleJFontChooser inner
 *          class to implement accessibility support???<br>
 *          2 - implement commented out method public static JDialog
 *          createDialog...
 */
public class JFontChooser extends JComponent {

    private static final long serialVersionUID = 0L;

    /**
     * Creates a font chooser with default font and size.
     */
    public JFontChooser() {
        super();
        doJFCLayout();
        setPreviewText("How does this font fit?");
        setPreviewFont(getAvailableFont());
    }

    /**
     * Create a font chooser with initial Font
     * 
     * @param init -
     *            the initial Font to preview in chooser.
     */
    public JFontChooser(Font init) {
        super();
        setPreviewFont(init);
        setPreviewText("How does this font fit?");
    }

    /**
     * Create a font chooser with initial Font and text in preview window
     * 
     * @param init -
     *            the initial Font to preview in chooser.
     * @param sampleText -
     *            the words to test the font on.
     */
    public JFontChooser(Font init, String sampleText) {
        super();
        setPreviewFont(init);
        setPreviewText(sampleText);
    }

    /**
     * Shows a modal font chooser dialog. If user presses "OK" the dialog is
     * disposed and the currently previewed font is returned. If the user
     * selects "Cancel" then null is returned.
     * 
     * @param component -
     *            the parent component for the Dialog.
     * @param title -
     *            the title of the window such as "Choose Font"
     * @param sampleText -
     *            the words to preview the font with
     * @param initial -
     *            the font to start previewing with. You should pass in the
     *            current font being used for smooth user exp.
     * @return Font you will get back either null if user cancels, or a new Font
     *         that the user has choosen.
     */
    public static Font showDialog(Component component, String title, String sampleText, Font initial) {
        final JFontChooser jfc = new JFontChooser(initial, sampleText);
        jfc.doJFCLayout();
        jfc.setJDialog(JFontChooser.initDialog(component, title, true));
        jfc.getJDialog().getContentPane().add(jfc, BorderLayout.CENTER);
        JPanel buttonsOuter = new JPanel();
        buttonsOuter.setLayout(new BorderLayout());
        JPanel buttons = new JPanel(new GridLayout(0, 1));
        buttons.add(apply = new JButton("OK"));
        apply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                jfc.setPreviewFont(jfc.buildFont());
                if (jfc.getJDialog() != null) {
                    jfc.getJDialog().dispose();
                    jfc.getJDialog().setVisible(false);
                }
            }
        });
        buttons.add(cancel = new JButton("Cancel"));
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                jfc.setPreviewFont(null);
                if (jfc.getJDialog() != null) {
                    jfc.getJDialog().dispose();
                    jfc.getJDialog().setVisible(false);
                }
            }
        });
        buttonsOuter.add(buttons, BorderLayout.NORTH);
        jfc.getJDialog().getContentPane().add(buttonsOuter, BorderLayout.EAST);
        jfc.getJDialog().pack();
        jfc.getJDialog().setResizable(false);
        jfc.getJDialog().setVisible(true);
        return jfc.getPreviewFont();
    }

    private static JDialog initDialog(Component c, String title, boolean modal) {
        JDialog rv = null;
        Frame f = SwingUtil.getFrame(c);
        if (f != null) rv = new JDialog(f, title, modal); else rv = new JDialog();
        return rv;
    }

    /**
     * Useful for grabing any old font
     * 
     * @return Font that is first on in system, plain, 18pt
     */
    public static Font getAvailableFont() {
        Font rv = null;
        String[] sysFonts = JFontChooser.getSystemFonts();
        rv = new Font(sysFonts[0], Font.PLAIN, 18);
        return rv;
    }

    private void doJFCLayout() {
        setLayout(new BorderLayout());
        p = new JPanel(new FlowLayout());
        add(p, BorderLayout.CENTER);
        fontChoices = new JList(fonts);
        boolean initIsValid = false;
        if (getPreviewFont() != null) {
            String[] fontsTemp = JFontChooser.getSystemFonts();
            for (int i = 0; i < fontsTemp.length; i++) {
                if (fontsTemp[i].equalsIgnoreCase(getPreviewFont().getFontName())) {
                    fontChoices.setSelectedIndex(i);
                    initIsValid = true;
                }
            }
        }
        if (!initIsValid) {
            fontChoices.setSelectedIndex(0);
        }
        fontChoices.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent ae) {
                updateGUI();
            }
        });
        fontChoiceScroll = new JScrollPane(fontChoices);
        fontChoiceScroll.setPreferredSize(new Dimension(200, 150));
        p.add(fontChoiceScroll);
        sizeChoices = new JList(getFontSizes());
        initIsValid = false;
        if (getPreviewFont() != null) {
            for (int j = 0; j < getFontSizes().length; j++) {
                if (Integer.parseInt(sizes[j]) == getPreviewFont().getSize()) {
                    sizeChoices.setSelectedIndex(j);
                    initIsValid = true;
                }
            }
        }
        if (!initIsValid) {
            sizeChoices.setSelectedIndex(12);
        }
        sizeChoices.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent ae) {
                updateGUI();
            }
        });
        sizeScroll = new JScrollPane(sizeChoices);
        sizeScroll.setPreferredSize(new Dimension(48, 150));
        p.add(sizeScroll);
        JPanel stylePanel = new JPanel(new GridLayout(1, 2));
        checkBold = new JCheckBox("Bold");
        checkBold.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                updateGUI();
            }
        });
        stylePanel.add(checkBold);
        checkItalic = new JCheckBox("Italic");
        checkItalic.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                updateGUI();
            }
        });
        stylePanel.add(checkItalic);
        JPanel upperRight = new JPanel(new GridLayout(3, 1, 5, 3));
        upperRight.add(stylePanel);
        JPanel status = new JPanel(new GridLayout(2, 1));
        JLabel jl = new JLabel("Current Selection:");
        status.add(jl);
        font = new JLabel();
        font.setForeground(Color.black);
        status.add(font);
        upperRight.add(status);
        p.add(upperRight);
        previewScroll = new JScrollPane(preview);
        previewScroll.setPreferredSize(new Dimension(400, 80));
        add(previewScroll, BorderLayout.SOUTH);
        updateGUI();
    }

    private void updateStatus() {
        Font tmp = buildFont();
        String name;
        if (tmp.getName().length() > 11) name = tmp.getName().substring(0, 12); else name = tmp.getName();
        font.setText(name + " " + tmp.getSize() + "pt.");
    }

    private Font buildFont() {
        Font rv = null;
        String s = (String) sizeChoices.getSelectedValue();
        if (s != null) {
            int i = Integer.parseInt(s);
            int style = Font.PLAIN;
            if (checkBold.isSelected() && checkItalic.isSelected()) {
                style = Font.BOLD;
                style |= Font.ITALIC;
            } else if (checkBold.isSelected()) {
                style = Font.BOLD;
            } else if (checkItalic.isSelected()) {
                style = Font.ITALIC;
            }
            return new Font((String) fontChoices.getSelectedValue(), style, i);
        } else {
            return rv;
        }
    }

    private void updateGUI() {
        Font fnew = buildFont();
        if (fnew != null && getPreviewText() != null) {
            getPreviewText().setFont(fnew);
            if (fnew != null) setPreviewFont(fnew);
            updateStatus();
        }
    }

    private String sizes[] = { "8", "9", "10", "11", "12", "13", "14", "16", "18", "20", "24", "26", "28", "32", "36", "40", "48", "56", "64", "72" };

    private static String[] fonts;

    static {
        try {
            fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        } catch (Throwable t) {
            fonts = Toolkit.getDefaultToolkit().getFontList();
        }
    }

    public static String[] getSystemFonts() {
        return fonts;
    }

    public String[] getFontSizes() {
        return sizes;
    }

    private JDialog jd;

    public JDialog getJDialog() {
        return jd;
    }

    public void setJDialog(JDialog d) {
        jd = d;
    }

    private JPanel p;

    private JLabel font;

    private JList fontChoices, sizeChoices;

    private JScrollPane fontChoiceScroll, previewScroll, sizeScroll;

    private JTextArea preview;

    public JTextArea getPreviewText() {
        return preview;
    }

    public void setPreviewText(String s) {
        preview = new JTextArea(s);
    }

    private static JButton apply, cancel;

    private Font fPreview;

    /**
     * When using JFontChooser as a component, use this to get the currently
     * configured font.
     */
    public Font getPreviewFont() {
        return fPreview;
    }

    public void setPreviewFont(Font f) {
        fPreview = f;
    }

    private JCheckBox checkBold, checkItalic;

    /**
     * This is a convience method for those whom don't want to pass a font in.
     * 
     * @see #showDialog(Component, String, String, Font)
     */
    public static Font showDialog(Component c, String title, String sampleText) {
        Font rv = showDialog(c, title, sampleText, c.getFont());
        return rv;
    }

    /**
     * This is a convience method for those whom don't want to pass a font, and
     * a sample String in.
     * 
     * @see #showDialog(Component, String, String, Font)
     */
    public static Font showDialog(Component c, String title) {
        Font rv = showDialog(c, title, "How about this font?", c.getFont());
        return rv;
    }

    /**
     * This is a convience method for those whom don't want to pass a font, a
     * sample String, and a Dialog title in.
     * 
     * @see #showDialog(Component, String, String, Font)
     */
    public static Font showDialog(Component c) {
        Font rv = showDialog(c, "Choose a font", "How about this font?", c.getFont());
        return rv;
    }

    /**
     * Use this method to test the widget.
     * 
     * @param none
     * @return none
     */
    public static void main(String[] args) {
        final int DEMO_SHOWDIALOG = 666;
        final int DEMO_ASCOMPONENT = 667;
        int test_mode = 666;
        final JFrame test = new JFrame("Testing JFontChooser");
        Container c = test.getContentPane();
        switch(test_mode) {
            case DEMO_SHOWDIALOG:
                final JLabel l = new JLabel("Well now, what font do we have here?");
                c.add(l, BorderLayout.CENTER);
                JButton testButton = new JButton("Test");
                c.add(testButton, BorderLayout.NORTH);
                testButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        Font f = JFontChooser.showDialog(test, "Chooser a font", "Fishing for Bobby Searcher", JFontChooser.getAvailableFont());
                        if (f != null) {
                            l.setFont(f);
                            l.repaint();
                        } else {
                            System.out.println("f was " + f);
                        }
                    }
                });
                JButton quitButton = new JButton("Quit Testing");
                quitButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        test.dispose();
                        test.setVisible(false);
                        System.exit(0);
                    }
                });
                c.add(quitButton, BorderLayout.SOUTH);
                test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                test.setSize(c.getPreferredSize());
                break;
            case DEMO_ASCOMPONENT:
                final JFontChooser jfc = new JFontChooser(JFontChooser.getAvailableFont(), "Testosterone");
                jfc.doJFCLayout();
                c.add(jfc, BorderLayout.CENTER);
                JPanel bottom = new JPanel(new FlowLayout());
                JButton bApply = new JButton("Apply");
                JButton bCancel = new JButton("Cancel");
                bottom.add(bApply);
                bApply.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        System.out.println(jfc.getPreviewFont().toString());
                    }
                });
                bottom.add(bCancel);
                bCancel.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        System.out.println(jfc.getPreviewFont().toString());
                        test.dispose();
                        test.setVisible(false);
                        System.exit(0);
                    }
                });
                c.add(bottom, BorderLayout.SOUTH);
                test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                test.setSize(jfc.getPreferredSize());
                break;
            default:
                break;
        }
        test.setVisible(true);
    }
}
