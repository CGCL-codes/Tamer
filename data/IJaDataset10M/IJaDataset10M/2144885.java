package net.sf.convergia.client.tools.games.risk;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class RiskBoard extends JPanel {

    private JButton nextButton;

    private BufferedImage boardImage;

    private ArrayList<Polygon> polygons = new ArrayList<Polygon>();

    private ArrayList<Color> colors = new ArrayList<Color>();

    private ArrayList<Boolean> fills = new ArrayList<Boolean>();

    private ArrayList<Stroke> strokes = new ArrayList<Stroke>();

    public synchronized void addPolygon(Polygon polygon, Color color, boolean fill, Stroke stroke) {
        polygons.add(polygon);
        colors.add(color);
        fills.add(fill);
        if (stroke == null) stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        strokes.add(stroke);
        repaint();
    }

    public synchronized void clearPolygons() {
        polygons.clear();
        colors.clear();
        fills.clear();
        strokes.clear();
        repaint();
    }

    /**
	 * shows a test board in a frame.
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test risk board");
        frame.setSize(500, 300);
        JPanel panel = new JPanel();
        panel.add(new RiskBoard());
        frame.getContentPane().add(new JScrollPane(panel));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.show();
    }

    public RiskBoard() {
        super();
        setLayout(new BorderLayout());
        nextButton = new JButton("Next");
        nextButton.setFocusable(false);
        JPanel lowerPanel = new JPanel();
        add(lowerPanel, BorderLayout.SOUTH);
        lowerPanel.setLayout(new BorderLayout());
        lowerPanel.setOpaque(false);
        lowerPanel.add(nextButton, BorderLayout.EAST);
        try {
            boardImage = ImageIO.read(getClass().getResourceAsStream("worldmap.jpg"));
        } catch (IOException e) {
            throw new RuntimeException("TODO auto generated on Feb 15, 2008 : " + e.getClass().getName() + " - " + e.getMessage(), e);
        }
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public Dimension getPreferredSize() {
        return new Dimension(boardImage.getWidth(), boardImage.getHeight());
    }

    public Dimension getMinimumSize() {
        return new Dimension(boardImage.getWidth(), boardImage.getHeight());
    }

    public synchronized void paintComponent(Graphics g7) {
        if (!(g7 instanceof Graphics2D)) throw new RuntimeException("RiskBoard requires a Graphics2D to function.");
        Graphics2D g = (Graphics2D) g7;
        g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        g.drawImage(boardImage, 0, 0, null);
        for (int i = 0; i < polygons.size() && i < colors.size() && i < fills.size(); i++) {
            g.setColor(colors.get(i));
            g.setStroke(strokes.get(i));
            if (fills.get(i)) g.fillPolygon(polygons.get(i)); else g.drawPolygon(polygons.get(i));
        }
    }
}
