package jmodnews.modules.gui.faces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class StandaloneFacesViewer {

    private JPanel facesPanel;

    private JFrame frame;

    private JTextArea textbox;

    private StandaloneFacesViewer() {
        frame = new JFrame("Standalone Faces Viewer (c) 2005 Michael Schierl");
        Container cpane = frame.getContentPane();
        cpane.setLayout(new BorderLayout());
        facesPanel = new JPanel(new BorderLayout());
        facesPanel.setPreferredSize(new Dimension(60, 60));
        textbox = new JTextArea(10, 80);
        textbox.setFont(new Font("Monospaced", Font.PLAIN, 10));
        textbox.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                updateFaces();
            }

            public void removeUpdate(DocumentEvent e) {
                updateFaces();
            }

            public void insertUpdate(DocumentEvent e) {
                updateFaces();
            }
        });
        cpane.add(BorderLayout.NORTH, facesPanel);
        cpane.add(BorderLayout.CENTER, new JScrollPane(textbox));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    protected void updateFaces() {
        facesPanel.removeAll();
        String line = textbox.getText();
        if (line.length() < 20) return;
        String linePrefix = line.substring(0, 15).toLowerCase();
        Icon ic;
        if (linePrefix.startsWith("x-face:")) {
            ic = new XFaceIcon(line.substring(7), Color.BLACK, Color.WHITE, 2, Color.RED);
        } else if (linePrefix.startsWith("face:")) {
            ic = new PNGFaceIcon(line.substring(5), 2, Color.BLACK);
        } else if (linePrefix.startsWith("x-png-face:")) {
            ic = new PNGFaceIcon(line.substring(11), 2, Color.BLACK);
        } else {
            ic = null;
        }
        if (ic != null) {
            facesPanel.add(BorderLayout.EAST, new JLabel(ic));
        }
        facesPanel.invalidate();
        frame.getContentPane().invalidate();
        frame.getContentPane().validate();
        frame.repaint();
    }

    public static void main(String[] args) {
        new StandaloneFacesViewer();
    }
}
