package net.playbesiege.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ConfigDialog extends JFrame implements ActionListener {

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
    }

    JTextField w = new JTextField("1280");

    JTextField h = new JTextField("720");

    JTextField colorDepth = new JTextField("32");

    JTextField frequency = new JTextField("0");

    JCheckBox fullScreen = new JCheckBox();

    JCheckBox niceSky = new JCheckBox();

    JCheckBox sound = new JCheckBox();

    JCheckBox welcomeDialog = new JCheckBox();

    JButton save = new JButton("Save");

    public ConfigDialog() {
        super("Besiege Configuration");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Container cp = getContentPane();
        cp.setLayout(new MigLayout());
        cp.add(new JLabel("width"));
        cp.add(w, "wrap, w 100::");
        w.setName("width");
        cp.add(new JLabel("height"));
        cp.add(h, "wrap, w 100::");
        h.setName("height");
        cp.add(new JLabel("colorDepth"));
        cp.add(colorDepth, "wrap, w 100::");
        colorDepth.setName("colorDepth");
        cp.add(new JLabel("frequency"));
        cp.add(frequency, "wrap, w 100::");
        frequency.setName("frequency");
        cp.add(new JLabel("fullScreen"));
        cp.add(fullScreen, "wrap, w 100::");
        fullScreen.setName("fullScreen");
        cp.add(new JLabel("niceSky"));
        cp.add(niceSky, "wrap, w 100::");
        niceSky.setName("niceSky");
        cp.add(new JLabel("sound"));
        cp.add(sound, "wrap, w 100::");
        sound.setName("sound");
        cp.add(new JLabel("welcomeDialog"));
        cp.add(welcomeDialog, "wrap, w 100::");
        welcomeDialog.setName("welcomeDialog");
        save.addActionListener(this);
        cp.add(save, "span 2, align center");
        try {
            URL jarLocation = getJarLocation();
            File f = new File(jarLocation.toURI());
            File folder = f.getParentFile();
            File f2 = new File(folder, "Besiege.cfg");
            FileInputStream fis = new FileInputStream(f2);
            Properties p = new Properties();
            p.load(fis);
            fis.close();
            Component[] components = getContentPane().getComponents();
            for (int i = 0; i < components.length; i++) {
                Component component = components[i];
                if (component instanceof JTextField) {
                    JTextField tf = (JTextField) component;
                    String v = p.getProperty(component.getName());
                    tf.setText(v);
                }
                if (component instanceof JCheckBox) {
                    JCheckBox cb = (JCheckBox) component;
                    String v = p.getProperty(component.getName());
                    cb.setSelected(v.equals("true") ? true : false);
                }
            }
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        ConfigDialog cd = new ConfigDialog();
        cd.pack();
        cd.setLocationRelativeTo(null);
        cd.setVisible(true);
        System.out.println(getJarLocation());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            URL jarLocation = getJarLocation();
            File f = new File(jarLocation.toURI());
            File folder = f.getParentFile();
            File f2 = new File(folder, "Besiege.cfg");
            FileOutputStream fos = new FileOutputStream(f2);
            Properties p = new Properties();
            Component[] components = getContentPane().getComponents();
            for (int i = 0; i < components.length; i++) {
                Component component = components[i];
                if (component instanceof JTextField) {
                    JTextField tf = (JTextField) component;
                    p.setProperty(component.getName(), tf.getText());
                }
                if (component instanceof JCheckBox) {
                    JCheckBox cb = (JCheckBox) component;
                    p.setProperty(component.getName(), cb.isSelected() ? "true" : "false");
                }
            }
            p.store(fos, null);
            fos.close();
            this.dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    public static URL getJarLocation() {
        return ConfigDialog.class.getProtectionDomain().getCodeSource().getLocation();
    }
}
