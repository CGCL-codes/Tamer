package com.amd.aparapi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import com.amd.aparapi.Diff.DiffResult;

public class SwingDiff {

    JFrame frame;

    public SwingDiff(DiffResult result) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            frame = new JFrame("SwingDiff");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel panel = new JPanel() {

                @Override
                public void paint(Graphics g) {
                    super.paint(g);
                    g.drawRect(10, 10, 100, 100);
                }
            };
            panel.setLayout(new BorderLayout());
            StyleContext sc = new StyleContext();
            final Style rootStyle = sc.addStyle("Root", null);
            rootStyle.addAttribute(StyleConstants.Foreground, Color.black);
            rootStyle.addAttribute(StyleConstants.FontSize, new Integer(12));
            rootStyle.addAttribute(StyleConstants.FontFamily, "serif");
            rootStyle.addAttribute(StyleConstants.Bold, new Boolean(false));
            final Style heading1Style = sc.addStyle("Heading1", rootStyle);
            heading1Style.addAttribute(StyleConstants.Foreground, Color.blue);
            final Style heading2Style = sc.addStyle("Heading2", rootStyle);
            heading2Style.addAttribute(StyleConstants.Foreground, Color.red);
            heading2Style.addAttribute(StyleConstants.Background, Color.green);
            final DefaultStyledDocument lhsdoc = new DefaultStyledDocument(sc);
            JTextPane lhs = new JTextPane(lhsdoc);
            lhsdoc.insertString(0, arrayToString(result.getLhs()), null);
            lhsdoc.setParagraphAttributes(4, 1, heading2Style, false);
            lhsdoc.setParagraphAttributes(20, 5, heading1Style, false);
            lhs.setPreferredSize(new Dimension(800, 800));
            final DefaultStyledDocument rhsdoc = new DefaultStyledDocument(sc);
            JTextPane rhs = new JTextPane(rhsdoc);
            rhsdoc.insertString(0, arrayToString(result.getRhs()), null);
            rhsdoc.setParagraphAttributes(4, 1, heading2Style, false);
            rhsdoc.setParagraphAttributes(20, 5, heading1Style, false);
            rhs.setPreferredSize(new Dimension(800, 800));
            panel.add(new JScrollPane(lhs), BorderLayout.WEST);
            panel.add(new JScrollPane(rhs), BorderLayout.EAST);
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setVisible(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String[] lhs = getFileContents("expected.c");
        String[] rhs = getFileContents("actual.c");
        DiffResult result = Diff.diff(lhs, rhs);
        System.out.println(result);
        SwingDiff swingDiff = new SwingDiff(result);
    }

    private static String arrayToString(String[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : array) {
            stringBuilder.append(line).append("\n");
        }
        return (stringBuilder.toString().trim());
    }

    private static String[] getFileContents(String string) {
        String[] content = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(string)));
            List<String> lines = new ArrayList<String>();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                lines.add(line);
            }
            reader.close();
            content = lines.toArray(new String[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (content);
    }

    private static String getFileContent(String string) {
        String content = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(string)));
            StringBuilder sb = new StringBuilder();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                sb.append(line).append("\n");
            }
            reader.close();
            content = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (content);
    }
}
