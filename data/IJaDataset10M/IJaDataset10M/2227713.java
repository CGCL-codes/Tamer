package openpixel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import javax.swing.JPanel;

public class PaletteViewer extends JPanel {

    private static final long serialVersionUID = 1L;

    protected int gridSize;

    private BufferedImage image = null;

    private int numX;

    private int numY;

    /**
	 * This is the default constructor
	 */
    public PaletteViewer() {
        super();
        initialize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            IndexColorModel icm = (IndexColorModel) image.getColorModel();
            int ms = icm.getMapSize();
            int x = 0;
            int y = 0;
            for (int i = 0; i < ms; i++) {
                g.setColor(new Color(icm.getRed(i), icm.getGreen(i), icm.getBlue(i)));
                g.fillRect(x * gridSize, y * gridSize, gridSize, gridSize);
                x++;
                if (x >= numX) {
                    y++;
                    x = 0;
                }
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(300, 200);
        gridSize = 12;
        numX = getWidth() / gridSize;
        numY = 256 / numX;
        setPreferredSize(new Dimension(gridSize * numX, gridSize * numY));
        this.addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent e) {
                numX = getWidth() / gridSize;
                numY = 256 / numX;
                setPreferredSize(new Dimension(gridSize * numX, gridSize * numY));
                repaint();
            }
        });
        this.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                gridSize -= e.getWheelRotation();
                if (gridSize < 4) gridSize = 4;
                numX = getWidth() / gridSize;
                numY = 256 / numX;
                Dimension d = new Dimension(gridSize * numX, gridSize * numY);
                setPreferredSize(d);
                setSize(getSize());
                getParent().repaint();
            }
        });
    }
}
