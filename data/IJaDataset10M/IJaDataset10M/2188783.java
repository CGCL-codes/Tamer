package net.cryff.exe;

import java.util.HashMap;
import java.util.Set;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import net.cryff.settings.ConfigLoader;

public class KeyConfigurator extends JFrame {

    ConfigLoader cl = null;

    JComboBox box;

    public KeyConfigurator() {
        super("Cry For Freedom - Key Configurator");
        this.setSize(400, 400);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cl = new ConfigLoader("keys.cnf");
        HashMap<String, String> data = cl.getAll();
        Set<String> keys = data.keySet();
        Object[] key = keys.toArray();
        box = new JComboBox(key);
        box.setBounds(30, 30, 160, 30);
        this.add(box);
        for (int i = 0; i < keys.size(); i++) {
            System.out.println("" + key[i] + " " + KeyEvent.getKeyText(Integer.parseInt("" + data.get(key[i]))));
        }
        this.setVisible(true);
    }

    public static void main(String[] args) {
        KeyConfigurator key = new KeyConfigurator();
    }
}
