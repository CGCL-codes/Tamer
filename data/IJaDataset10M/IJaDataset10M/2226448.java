package photocard.composants;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JPanel;
import photocard.properties.Config;

public class JPanelListePhoto extends JPanel {

    private static final long serialVersionUID = 1L;

    private Image bordure;

    private Image raccord;

    public JPanelListePhoto() {
        this.setOpaque(true);
        try {
            URL documentBase = new URL("file:///" + System.getProperty("user.dir") + "/");
            String chemin1 = Config.get("repertoire.images") + Config.get("image.bordure");
            String chemin2 = Config.get("repertoire.images") + Config.get("image.bordureAngle");
            this.bordure = Toolkit.getDefaultToolkit().getImage(new URL(documentBase, chemin1));
            this.raccord = Toolkit.getDefaultToolkit().getImage(new URL(documentBase, chemin2));
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement du l'image");
        }
    }

    protected void paintComponent(Graphics g) {
        g.setColor(new Color(0, 0, 0, 76));
        g.fillRect(0, this.raccord.getHeight(this), this.getWidth(), this.getHeight());
        g.fillRect(this.getWidth(), 0, this.getWidth(), this.raccord.getHeight(this));
        g.drawImage(this.raccord, this.getWidth() - this.raccord.getWidth(this), 0, this.raccord.getWidth(this), this.raccord.getHeight(this), this);
        g.drawImage(this.bordure, 0, this.raccord.getHeight(this) - this.bordure.getHeight(this), this.getWidth() - this.raccord.getWidth(this), this.bordure.getHeight(this), this);
    }
}
