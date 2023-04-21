package tk.bot;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class BotFloodProtection extends BotBasicInterpreter implements BotCustomCommand, ActionListener {

    BotUtilities u = new BotUtilities();

    JLabel label = new JLabel("Prevents your bot from flooding battle.net");

    JLabel def = new JLabel("This button clears the command queue");

    JButton clear = new JButton("Clear");

    static boolean enabled = false;

    TKBot bot;

    public BotFloodProtection(TKBot bot) {
        this.bot = bot;
    }

    public void commandPerformed(CommandEvent e) {
    }

    public JPanel getConfig() {
        JPanel temp = new JPanel();
        temp.setLayout(new GridLayout(3, 1));
        clear.addActionListener(this);
        temp.add(label);
        temp.add(def);
        temp.add(clear);
        return temp;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(clear)) {
            bot.clearBuffer();
        }
    }

    public boolean equals(Object obj) {
        return (obj instanceof BotFloodProtection);
    }

    public String toString() {
        return "FloodProtection";
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        bot.setFloodProtection(enabled);
    }

    public boolean getEnabled() {
        return bot.getFloodProtection();
    }

    public void install(TKBot bot) {
        if (TKBot.DEBUG > 3) System.out.println("Installing Flood Protection");
    }

    public void remove(TKBot bot) {
    }

    public boolean isInstalled(TKBot bot) {
        return true;
    }
}
