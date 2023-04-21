package common;

import info.clearthought.layout.TableLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class CustomPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = -5451187966144594250L;

    private Image image = null;

    public CustomPanel() {
        super();
        initGUI();
        setOpaque(false);
    }

    public CustomPanel(String img) {
        this();
        image = new ImageIcon(getClass().getResource(img)).getImage();
    }

    private void initGUI() {
        try {
            this.setSize(new Dimension(800, 600));
            TableLayout thisLayout = new TableLayout(new double[][] { { TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL }, { TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL } });
            thisLayout.setHGap(10);
            thisLayout.setVGap(2);
            this.setLayout(thisLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if (image != null) g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        super.paintComponent(g);
    }

    public void setImage(String img) {
        image = new ImageIcon(getClass().getResource(img)).getImage();
        this.repaint();
    }
}
