package plot.image;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class PictureFrame extends JPanel {

    private static final long serialVersionUID = 1L;

    private Picture picture;

    public PictureFrame(String filename) {
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        setLayout(null);
        picture = new Picture(filename);
        Dimension dim = picture.getPrefferedSize();
        picture.setBounds(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
        add(picture);
    }

    public Picture getPicture() {
        return picture;
    }
}
