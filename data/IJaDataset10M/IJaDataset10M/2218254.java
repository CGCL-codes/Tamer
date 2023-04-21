package bitWave.utilities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;
import bitWave.geometry.Line;
import bitWave.geometry.Mesh;
import bitWave.geometry.Vertex;
import bitWave.linAlg.Vec4;

public class ViewPanel extends JPanel {

    static final long serialVersionUID = 0;

    private Mesh m_mesh;

    private int m_w;

    private int m_h;

    private int m_mapw;

    private int m_maph;

    byte[] m_edges;

    /**
     * Displays the given mesh.
     */
    public static void display(byte[] edges, int mapWidth, int mapHeight, Mesh m, int width, int height) {
        if (m != null) {
            System.out.println("Number of lines: " + m.getLineCount());
            System.out.println("Number of vertices: " + m.getVertexCount());
        }
        ViewPanel gp = new ViewPanel(edges, mapWidth, mapHeight, m, width, height);
        JFrame j = new JFrame();
        j.setTitle("Mesh View");
        j.setSize(width, height);
        j.getContentPane().add(gp);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);
    }

    ViewPanel(byte[] edges, int mapWidth, int mapHeight, Mesh m, int width, int height) {
        this.m_edges = edges;
        this.m_mesh = m;
        this.m_w = width;
        this.m_h = height;
        this.m_mapw = mapWidth;
        this.m_maph = mapHeight;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.RED);
        if (this.m_edges != null) {
            for (int y = 0; y < this.m_maph; y++) {
                for (int x = 0; x < this.m_mapw; x++) {
                    if (this.m_edges[y * this.m_mapw + x] == 1) g2.drawLine(x, y, x, y);
                }
            }
        }
        g2.setColor(Color.BLACK);
        if (this.m_mesh != null) {
            for (int i = 0; i < this.m_mesh.getLineCount(); i++) {
                Line l = this.m_mesh.getLineByIndex(i);
                int n = l.getVertexCount();
                if (n > 1) {
                    int[] x = new int[n];
                    int[] y = new int[n];
                    Vec4 pos;
                    for (int p = 0; p < n; p++) {
                        Vertex v = l.getVertexByIndex(p);
                        pos = v.getPosition();
                        x[p] = (int) (pos.x() * (double) this.m_w);
                        y[p] = (int) (pos.y() * (double) this.m_h);
                    }
                    g2.setColor(new Color((float) (Math.random() * 0.7 + 0.25), (float) (Math.random() * 0.7 + 0.25), (float) (Math.random() * 0.7 + 0.25), (float) 0.6));
                    for (int p = 1; p < n; p++) {
                        g2.drawLine(x[p - 1], y[p - 1], x[p], y[p]);
                    }
                }
            }
        }
    }
}
