package jemu.system.cpc;

import jemu.ui.Switches;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.border.*;

public class CPCPrinter extends WindowAdapter implements WindowListener, ActionListener {

    JFileChooser saveFileChooser = new JFileChooser();

    protected GridBagConstraints gbcConstraints = null;

    static boolean visible = false;

    public static JFrame typeconsole;

    public static String autotext;

    public static JTextArea textArea;

    public JButton button2 = new JButton("  Clear output  ");

    public JButton button3 = new JButton("    Copy text    ");

    public JButton button4 = new JButton(" Save Output ");

    public JButton smaller = new JButton("  Textsize -  ");

    public JButton bigger = new JButton("  Textsize +  ");

    public JButton FontB = new JButton(" Select Font + ");

    public static JButton Online = new JButton(" Online ");

    public static int textSize = 12;

    public static String FontName1 = "Monospaced";

    public static String FontName2 = "Courier New";

    public static String FontName3 = "Trebuchet MS";

    public static String FontName4 = "Verdana";

    public static String FontName5 = "OCR A Std";

    public static String FontName6 = "Old English Text MT";

    public static String FontName7 = "Arial";

    public static String FontName8 = "Arial Narrow";

    public static String FontName9 = "Arial Bold";

    public static int fontc = 1;

    public static String FontName = FontName1;

    public CPCPrinter() {
        typeconsole = new JFrame("JavaCPC Printer");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension((int) (screenSize.width / 2), (int) (screenSize.height / 2));
        int x = (frameSize.width / 2 + 110);
        int y = (frameSize.height / 2 + 60);
        typeconsole.setBounds(x, y, 640, 480);
        typeconsole.setAlwaysOnTop(true);
        textArea = new JTextArea();
        textArea.setEditable(true);
        textArea.setAutoscrolls(true);
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        textArea.setCaretColor(Color.RED);
        textArea.setSelectedTextColor(Color.GRAY);
        textArea.setSelectionColor(Color.ORANGE);
        textArea.setBorder(new BevelBorder(BevelBorder.LOWERED));
        button2.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        button2.setForeground(Color.LIGHT_GRAY);
        button2.setBackground(Color.DARK_GRAY);
        button2.setBorder(new BevelBorder(BevelBorder.RAISED));
        button3.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        button3.setForeground(Color.LIGHT_GRAY);
        button3.setBackground(Color.DARK_GRAY);
        button3.setBorder(new BevelBorder(BevelBorder.RAISED));
        button4.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        button4.setForeground(Color.LIGHT_GRAY);
        button4.setBackground(Color.DARK_GRAY);
        button4.setBorder(new BevelBorder(BevelBorder.RAISED));
        smaller.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        smaller.setForeground(Color.LIGHT_GRAY);
        smaller.setBackground(Color.DARK_GRAY);
        smaller.setBorder(new BevelBorder(BevelBorder.RAISED));
        bigger.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        bigger.setForeground(Color.LIGHT_GRAY);
        bigger.setBackground(Color.DARK_GRAY);
        bigger.setBorder(new BevelBorder(BevelBorder.RAISED));
        FontB.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        FontB.setForeground(Color.LIGHT_GRAY);
        FontB.setBackground(Color.DARK_GRAY);
        FontB.setBorder(new BevelBorder(BevelBorder.RAISED));
        Online.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        Online.setBorder(new BevelBorder(BevelBorder.RAISED));
        OnlineButton();
        typeconsole.getContentPane().setLayout(new GridBagLayout());
        typeconsole.add(button2, getGridBagConstraints(2, 2, 1.0, 0.0, 1, GridBagConstraints.BOTH));
        typeconsole.add(button3, getGridBagConstraints(3, 2, 1.0, 0.0, 1, GridBagConstraints.BOTH));
        typeconsole.add(button4, getGridBagConstraints(4, 2, 1.0, 0.0, 1, GridBagConstraints.BOTH));
        typeconsole.add(smaller, getGridBagConstraints(5, 2, 1.0, 0.0, 1, GridBagConstraints.BOTH));
        typeconsole.add(bigger, getGridBagConstraints(6, 2, 1.0, 0.0, 1, GridBagConstraints.BOTH));
        typeconsole.add(FontB, getGridBagConstraints(7, 2, 1.0, 0.0, 1, GridBagConstraints.BOTH));
        typeconsole.add(Online, getGridBagConstraints(8, 2, 1.0, 0.0, 1, GridBagConstraints.BOTH));
        typeconsole.add(new JScrollPane(textArea), getGridBagConstraints(1, 1, 1.0, 1.0, 8, GridBagConstraints.BOTH));
        typeconsole.setVisible(false);
        typeconsole.addWindowListener(this);
        button2.addActionListener(this);
        button2.setFocusable(false);
        button3.addActionListener(this);
        button3.setFocusable(false);
        button4.addActionListener(this);
        button4.setFocusable(false);
        smaller.addActionListener(this);
        smaller.setFocusable(false);
        bigger.addActionListener(this);
        bigger.setFocusable(false);
        FontB.addActionListener(this);
        FontB.setFocusable(false);
        Online.addActionListener(this);
        Online.setFocusable(false);
    }

    private GridBagConstraints getGridBagConstraints(int x, int y, double weightx, double weighty, int width, int fill) {
        if (this.gbcConstraints == null) {
            this.gbcConstraints = new GridBagConstraints();
        }
        this.gbcConstraints.gridx = x;
        this.gbcConstraints.gridy = y;
        this.gbcConstraints.weightx = weightx;
        this.gbcConstraints.weighty = weighty;
        this.gbcConstraints.gridwidth = width;
        this.gbcConstraints.fill = fill;
        return this.gbcConstraints;
    }

    public synchronized void windowClosing(WindowEvent evt) {
        typeconsole.setVisible(false);
        visible = false;
    }

    public static String dump = "";

    public static void DumpText(int value) {
        dump += ("" + (char) value);
        if ((char) value == 13) {
            if (!visible && jemu.ui.JEMU.iframe == null) {
                OnlineButton();
                typeconsole.setVisible(true);
                visible = true;
            }
            if (jemu.ui.JEMU.iframe != null) {
                if (!jemu.ui.Desktop.printframe.isVisible()) jemu.ui.Desktop.printframe.setVisible(true);
            }
            if (Switches.printEffect) {
                textArea.setFont(new Font(FontName, 0, 3));
                textSize = 3;
            } else {
                textArea.setFont(new Font(FontName, 0, textSize));
            }
            textArea.append(dump);
            jemu.ui.Desktop.printout.append(dump);
            textArea.select(2000000000, 2000000000);
            jemu.ui.Desktop.printout.select(2000000000, 2000000000);
            dump = "";
        }
    }

    public synchronized void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == button2) {
            ClearPrinter();
        }
        if (evt.getSource() == smaller) {
            Smaller();
        }
        if (evt.getSource() == bigger) {
            Bigger();
        }
        if (evt.getSource() == FontB) {
            FontSelector();
        }
        if (evt.getSource() == Online) {
            PrinterCheck();
        }
        if (evt.getSource() == button3) {
            Copy();
        }
        if (evt.getSource() == button4) {
            saveFile();
        }
    }

    public static void MakeVisible() {
        typeconsole.setVisible(true);
        visible = true;
    }

    public static void MakeInvisible() {
        typeconsole.setVisible(false);
        visible = false;
    }

    public static void Smaller() {
        if (textSize >= 2) {
            textSize--;
        }
        textArea.setFont(new Font(FontName, 0, textSize));
    }

    public static void Bigger() {
        if (textSize <= 150) {
            textSize++;
        }
        textArea.setFont(new Font(FontName, 0, textSize));
    }

    public static void FontSelector() {
        if (fontc <= 9) {
            fontc++;
        } else {
            fontc = 1;
        }
        if (fontc == 1) {
            FontName = FontName1;
        }
        if (fontc == 2) {
            FontName = FontName2;
        }
        if (fontc == 3) {
            FontName = FontName3;
        }
        if (fontc == 4) {
            FontName = FontName4;
        }
        if (fontc == 5) {
            FontName = FontName5;
        }
        if (fontc == 6) {
            FontName = FontName6;
        }
        if (fontc == 7) {
            FontName = FontName7;
        }
        if (fontc == 8) {
            FontName = FontName8;
        }
        if (fontc == 9) {
            FontName = FontName9;
        }
        textArea.setFont(new Font(FontName, 0, textSize));
    }

    public static void PrinterCheck() {
        if (Switches.Printer) {
            Switches.Printer = false;
        } else {
            Switches.Printer = true;
        }
        OnlineButton();
    }

    public static void OnlineButton() {
        if (Switches.Printer) {
            Online.setForeground(Color.WHITE);
            Online.setBackground(Color.GREEN);
        } else {
            Online.setForeground(Color.BLACK);
            Online.setBackground(Color.RED);
        }
    }

    public static void ClearPrinter() {
        textArea.setText("");
    }

    public static void Copy() {
        if (!textArea.getText().equals("")) {
            textArea.selectAll();
            textArea.copy();
            textArea.paste();
        }
    }

    public static void setFont() {
        textArea.setFont(new Font(FontName, 0, textSize));
    }

    public void saveFile() {
        FileDialog filedia = new FileDialog((Frame) typeconsole, "Save ASCII...", FileDialog.SAVE);
        filedia.setFile("*.txt: *.asm; *.bas");
        filedia.setVisible(true);
        String filename = filedia.getFile();
        if (filename != null) {
            filename = filedia.getDirectory() + filedia.getFile();
            String savename = filename;
            if (!savename.toLowerCase().endsWith(".txt")) {
                if (!savename.toLowerCase().endsWith(".asm")) {
                    if (!savename.toLowerCase().endsWith(".bas")) {
                        savename = savename + ".txt";
                    }
                }
            }
            File file = new File(savename);
            String gettext = textArea.getText();
            try {
                FileWriter fw = new FileWriter(file);
                fw.write(gettext);
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        filedia.dispose();
    }
}
