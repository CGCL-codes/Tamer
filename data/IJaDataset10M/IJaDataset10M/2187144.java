package forms.editor.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import forms.FileHelper;
import forms.editor.Picture;

public class BlobEditor extends DefaultPropertyEditor<Blob> {

    JLabel l = new JLabel();

    private Blob blob;

    int w = 50;

    int h = 50;

    public BlobEditor() {
        l = new JLabel();
        l.addMouseListener(new LoadFileListener());
    }

    @Override
    public void setPropertie(PropertyDescriptor propertie) {
        Method read = propertie.getReadMethod();
        if (read != null && read.isAnnotationPresent(Picture.class)) {
            Picture p = read.getAnnotation(Picture.class);
            w = p.width();
            h = p.heigth();
            if (blob == null) {
                drawEmpty();
            }
        }
    }

    private class LoadFileListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    setBlob(FileHelper.createBlob(f));
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override
    public Component getCustomEditor() {
        return l;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Blob) {
            try {
                Blob b = (Blob) value;
                setBlob(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            drawEmpty();
        }
    }

    private void setBlob(Blob b) throws IOException, SQLException {
        if (b != null) {
            this.blob = b;
            BufferedImage image = ImageIO.read(b.getBinaryStream());
            Image scal = image.getScaledInstance(w, h, BufferedImage.SCALE_FAST);
            l.setIcon(new ImageIcon(scal));
        } else {
            drawEmpty();
        }
    }

    private void drawEmpty() {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);
        g.drawRect(0, 0, w - 1, h - 1);
        l.setIcon(new ImageIcon(image));
    }

    @Override
    public Object getValue() {
        return blob;
    }
}
