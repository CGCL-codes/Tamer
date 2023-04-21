package edu.whitman.halfway.util;

import org.apache.log4j.Logger;
import javax.swing.border.Border;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/** Class to display graphically the contents of a 2D integer matrix.
 * Assumes values in matrix are in the range 0 - 255 inclusive -- a
 * utility method can convert arbitrary integer and double matrices to
 * this format.
 * @deprecated used GridPanel
 */
public class PaintedMatrixPanel extends JPanel {

    static Logger log = Logger.getLogger(PaintedMatrixPanel.class);

    protected MatrixPainter mp;

    public PaintedMatrixPanel() {
        this(false);
    }

    public PaintedMatrixPanel(boolean drawGrid) {
        super(true);
        mp = new MatrixPainter(drawGrid);
    }

    /** Can be used by sublcasses to specify a different subclass of MatrixPainter*/
    protected void setMatrixPainter(MatrixPainter painter) {
        if (painter == null) {
            throw new IllegalArgumentException("Can't have a null painter.");
        }
        mp = painter;
    }

    public void setPalette(Color[] pal) {
        mp.setPalette(pal);
    }

    /** Sets the matrix displayed to the given matrix and returns true
     * if matrix has all entries in [0,255] and is square.  Otherwise
     * returns false, leaves matrix displayed unchanged.*/
    public boolean setMatrix(int[][] a) {
        return mp.setMatrix(a);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        AffineTransform origTrans = ((Graphics2D) g).getTransform();
        mp.paintMatrix((Graphics2D) g, getMatrixRect());
        ((Graphics2D) g).setTransform(origTrans);
    }

    /** The rectangle we draw in */
    protected Rectangle getMatrixRect() {
        Insets insets = getInsets();
        int width = getWidth() - insets.left - insets.right;
        int height = getHeight() - insets.top - insets.bottom;
        return new Rectangle(insets.left, insets.top, width, height);
    }

    public Rectangle rectOfMatrixEntry(int row, int col) {
        return mp.rectOfMatrixEntry(getMatrixRect(), row, col);
    }

    public static void displayFrame(int[][] data) {
        TestFrame frame = new TestFrame(data);
        frame.setVisible(true);
    }

    /** Returns the matrix row corresponding to the given point in the
     * JPanel's coord space, or -1 if it is a point outside of the matrix. */
    public int getRow(Point point) {
        Rectangle rect = getMatrixRect();
        int rp = point.y - rect.y;
        if (rp < 0 || rp > rect.height) return -1;
        int side = mp.calcSide(rect);
        return rp / side;
    }

    /** Returns the matrix column corresponding to the given point in the
     * JPanel's coord space, or -1 if it is a point outside of the matrix. */
    public int getCol(Point point) {
        Rectangle rect = getMatrixRect();
        int cp = point.x - rect.x;
        if (cp < 0 || cp > rect.width) return -1;
        int side = mp.calcSide(rect);
        return cp / side;
    }

    /** @deprecated 
        Gives integer matrix in form need to display given arbitrary
        double matrix.  Call the static method in MatrixPainter instead. */
    public static int[][] normalize(double[][] a) {
        return MatrixPainter.normalize(a);
    }

    public static void main(String[] args) {
        int size = 20;
        double[][] t2 = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                t2[i][j] = Math.sin((i * 10.0) / size + (j * 3.0) / size) + Math.cos((j * 7.0) / size);
            }
        }
        TestFrame frame2 = new TestFrame(PaintedMatrixPanel.normalize(t2));
        frame2.setVisible(true);
    }

    private static class TestFrame extends JFrame {

        public TestFrame(int[][] matrix) {
            PaintedMatrixPanel pmp = new PaintedMatrixPanel(false);
            pmp.setMatrix(matrix);
            Border matte = BorderFactory.createMatteBorder(5, 5, 5, 5, Color.black);
            pmp.setBorder(BorderFactory.createTitledBorder(matte, "Positive Target Velocity"));
            pmp.setPreferredSize(new Dimension(500, 500));
            setSize(500, 500);
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(pmp, BorderLayout.CENTER);
            getContentPane().setBackground(Color.blue);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }
}
