package bomberman.img;

import bomberman.DB;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author eirikb
 */
public class ViewAll {

    public static void main(String[] args) {
        DB db = DB.load();
        db.getIdb().generateImages();
        db.getIdb().resize(200);
        ViewAll.viewAll(db.getIdb());
    }

    public static void viewAll(ImageDB idb) {
        Iterator<Entry<String, ImgCharacter>> c = idb.getCharacter().entrySet().iterator();
        while (c.hasNext()) {
            Entry<String, ImgCharacter> c2 = c.next();
            int count = 0;
            for (ImageIcon i : c2.getValue().getDown()) {
                JOptionPane.showMessageDialog(null, i, "Character - " + c2.getKey() + " - Down - " + count++, 1);
            }
            count = 0;
            for (ImageIcon i : c2.getValue().getLeft()) {
                JOptionPane.showMessageDialog(null, i, "Character - " + c2.getKey() + " - Left - " + count++, 1);
            }
            count = 0;
            for (ImageIcon i : c2.getValue().getRight()) {
                JOptionPane.showMessageDialog(null, i, "Character - " + c2.getKey() + " - Right - " + count++, 1);
            }
            count = 0;
            for (ImageIcon i : c2.getValue().getUp()) {
                JOptionPane.showMessageDialog(null, i, "Character - " + c2.getKey() + " - Up - " + count++, 1);
            }
        }
        Iterator<Entry<String, ImgFire>> f = idb.getFire().entrySet().iterator();
        while (f.hasNext()) {
            Entry<String, ImgFire> f2 = f.next();
            int count = 0;
            for (ImageIcon i : f2.getValue().getCore()) {
                JOptionPane.showMessageDialog(null, i, "Fire - " + f2.getKey() + " - Core - " + count++, 1);
            }
            count = 0;
            for (ImageIcon i : f2.getValue().getDown()) {
                JOptionPane.showMessageDialog(null, i, "Fire - " + f2.getKey() + " - Down - " + count++, 1);
            }
            count = 0;
            for (ImageIcon i : f2.getValue().getLeft()) {
                JOptionPane.showMessageDialog(null, i, "Fire - " + f2.getKey() + " - Left - " + count++, 1);
            }
            count = 0;
            for (ImageIcon i : f2.getValue().getRight()) {
                JOptionPane.showMessageDialog(null, i, "Fire - " + f2.getKey() + " - Right - " + count++, 1);
            }
            count = 0;
            for (ImageIcon i : f2.getValue().getUp()) {
                JOptionPane.showMessageDialog(null, i, "Fire - " + f2.getKey() + " - Up - " + count++, 1);
            }
            count = 0;
            for (ImageIcon i : f2.getValue().getSide1()) {
                JOptionPane.showMessageDialog(null, i, "Fire - " + f2.getKey() + " - Side1 - " + count++, 1);
            }
            count = 0;
            for (ImageIcon i : f2.getValue().getSide2()) {
                JOptionPane.showMessageDialog(null, i, "Fire - " + f2.getKey() + " - Side2 - " + count++, 1);
            }
        }
        Iterator<Entry<String, ArrayList<ImageIcon>>> b = idb.getBomb().entrySet().iterator();
        while (b.hasNext()) {
            Entry<String, ArrayList<ImageIcon>> b2 = b.next();
            int count = 0;
            for (ImageIcon i : b2.getValue()) {
                JOptionPane.showMessageDialog(null, i, "Bomb - " + b2.getKey() + " - " + count++, 1);
            }
        }
        Iterator<Entry<String, ArrayList<ImageIcon>>> brick = idb.getBrick().entrySet().iterator();
        while (brick.hasNext()) {
            Entry<String, ArrayList<ImageIcon>> brick2 = brick.next();
            int count = 0;
            for (ImageIcon i : brick2.getValue()) {
                JOptionPane.showMessageDialog(null, i, "Brick - " + brick2.getKey() + " - " + count++, 1);
            }
        }
        Iterator<Entry<String, ImageIcon>> box = idb.getBox().entrySet().iterator();
        while (box.hasNext()) {
            Entry<String, ImageIcon> box2 = box.next();
            JOptionPane.showMessageDialog(null, box2.getValue(), "Box - " + box2.getKey(), 1);
        }
        Iterator<Entry<String, ImageIcon>> p = idb.getPowerup().entrySet().iterator();
        while (p.hasNext()) {
            Entry<String, ImageIcon> p2 = p.next();
            JOptionPane.showMessageDialog(null, p2.getValue(), "Powerup - " + p2.getKey(), 1);
        }
    }
}
