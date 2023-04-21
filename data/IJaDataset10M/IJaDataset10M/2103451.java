package seksen.gui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class LineNumberPanel extends JPanel {

    JTextArea text_area;

    JScrollPane scrollPane;

    public LineNumberPanel() {
        super();
        setPreferredSize(new Dimension(30, 30));
        setMinimumSize(new Dimension(30, 30));
        text_area = new JTextArea() {

            public void paint(Graphics g) {
                super.paint(g);
                LineNumberPanel.this.repaint();
            }
        };
        scrollPane = new JScrollPane(text_area);
    }

    public void paint(Graphics g) {
        super.paint(g);
        int start = text_area.viewToModel(scrollPane.getViewport().getViewPosition());
        int end = text_area.viewToModel(new Point(scrollPane.getViewport().getViewPosition().x + text_area.getWidth(), scrollPane.getViewport().getViewPosition().y + text_area.getHeight()));
        Document doc = text_area.getDocument();
        int startline = doc.getDefaultRootElement().getElementIndex(start) + 1;
        int endline = doc.getDefaultRootElement().getElementIndex(end) + 1;
        FontMetrics fm = g.getFontMetrics(text_area.getFont());
        int fontHeight = fm.getHeight();
        int fontDesc = fm.getDescent();
        int starting_y = -1;
        try {
            starting_y = text_area.modelToView(start).y - scrollPane.getViewport().getViewPosition().y + fontHeight - fontDesc;
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        for (int line = startline, y = starting_y; line <= endline; y += fontHeight, line++) {
            g.drawString(Integer.toString(line), 0, y);
        }
        int width = fm.stringWidth(Integer.toString(endline)) + 10;
        setPreferredSize(new Dimension(width, getHeight()));
    }
}
