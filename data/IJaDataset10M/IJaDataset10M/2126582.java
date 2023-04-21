package editor;

import io.WeaponWriter;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.*;
import sgEngine.EngineConstants;
import world.shot.weapon.Weapon;

/**
 * a basic unit editor
 * @author Jack
 *
 */
public class BasicWeaponEditor extends JFrame {

    JFrame f;

    JTextField name;

    JTextField shot;

    JTextField range;

    JTextField reload;

    public BasicWeaponEditor() {
        super("Basic Weapon Editor");
        f = this;
        setSize(200, 200);
        add(createGeneralPanel());
        setJMenuBar(createMenuBar());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new BasicWeaponEditor();
    }

    private JMenuBar createMenuBar() {
        JMenuBar b = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("saving weapon...");
                    String dir = System.getProperty("user.dir") + System.getProperty("file.separator") + "weapons" + System.getProperty("file.separator");
                    System.out.println("dir = " + dir);
                    File f = new File(dir + name.getText() + ".weapon");
                    System.out.println("file name = " + f.getName());
                    FileOutputStream fos = new FileOutputStream(f);
                    DataOutputStream dos = new DataOutputStream(fos);
                    WeaponWriter.writeWeapon(dos, name.getText(), shot.getText(), Double.parseDouble(range.getText()), Integer.parseInt(reload.getText()));
                    System.out.println("done!");
                } catch (Exception a) {
                    a.printStackTrace();
                    System.out.println("failed to save");
                }
            }
        });
        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    Weapon w = EngineConstants.weaponFactory.makeWeapon(name.getText());
                    shot.setText(w.getShot().getName());
                    range.setText("" + w.getRange());
                    reload.setText("" + w.getReload());
                } catch (NullPointerException a) {
                    System.out.println("incorrect name, weapon load failed");
                }
            }
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file.add(load);
        file.add(save);
        file.addSeparator();
        file.add(exit);
        b.add(file);
        return b;
    }

    private JPanel createGeneralPanel() {
        JPanel p = createPanel();
        name = new JTextField(15);
        shot = new JTextField(15);
        range = new JTextField(15);
        reload = new JTextField(15);
        p.add(new JLabel("Name:"));
        p.add(name);
        p.add(new JLabel("Shot Type:"));
        p.add(shot);
        p.add(new JLabel("Range:"));
        p.add(range);
        p.add(new JLabel("Reload Iterations:"));
        p.add(reload);
        return p;
    }

    private JPanel createPanel() {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }
}
