package swing;

import javax.swing.JPanel;
import modele.PhotoModele;
import java.awt.Graphics;
import java.io.File;

class PanelPhotoPreview extends JPanel {

    private static final long serialVersionUID = 1L;

    private PhotoModele p;

    public static final File iconFile = new File("res/icon.png");

    public PanelPhotoPreview() {
        super();
        this.p = new PhotoModele(iconFile);
    }

    public PanelPhotoPreview(PhotoModele p) {
        super();
        this.p = p;
    }

    public void updatePanelPreview(PhotoModele p) {
        this.p = p;
        Graphics g = this.getGraphics();
        g.clearRect(0, 0, this.getParent().getHeight(), this.getParent().getWidth());
        g.drawImage(p.miniature, 20, 20, p.largeurMini, p.hauteurMini, this);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(p.miniature, 20, 20, p.largeurMini, p.hauteurMini, this);
    }
}
