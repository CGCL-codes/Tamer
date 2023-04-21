package dsrmapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;
import javax.swing.JComponent;

public abstract class Icon extends JComponent {

    private static final long serialVersionUID = 1L;

    public static final int ICON_SIZE = 25;

    public short cmd;

    private Image img;

    private MapEditorMain main;

    public Icon(MapEditorMain m, short _cmd, String image_filename, MouseListener ml) {
        main = m;
        cmd = _cmd;
        img = MapEditorMain.GetImage(image_filename);
        this.setSize(ICON_SIZE, ICON_SIZE);
        this.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        this.addMouseListener(ml);
    }

    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, ICON_SIZE, ICON_SIZE, this);
        if (main.selected_icon == this) {
            g.setColor(Color.yellow);
            g.drawRect(0, 0, ICON_SIZE - 1, ICON_SIZE - 1);
        }
    }
}
