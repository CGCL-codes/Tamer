package tk.bot;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class BotCustomBan extends BotBasicInterpreter implements BotCustomCommand, ActionListener {

    BotUtilities u = new BotUtilities();

    JTextField field;

    BotDatabaseInterface db;

    String ban;

    static boolean enabled = false;

    TKBot bot;

    public BotCustomBan(TKBot bot) {
        this.bot = bot;
        db = (BotDatabaseInterface) bot.dbManager.getInterface("commands");
        ban = db.getProperty("customban", "/ban %name%");
    }

    public void commandPerformed(CommandEvent e) {
        if (enabled) {
            BotCommandType type = e.getType();
            String command = e.getCommand();
            if (type instanceof BotUserType) {
                BotUser user = u.getUserFromType(command);
                String message = u.replaceVars(ban, user.getHashtable(bot.getUserTable()));
                type.getBot().addCommand(message);
                type.getBot().appendLine("#" + message);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (e.getSource().equals(field)) {
            ban = field.getText();
            refresh();
        }
    }

    void refresh() {
        db.setProperty("customban", ban);
        db.store();
    }

    public JPanel getConfig() {
        JPanel temp = new JPanel();
        field = new JTextField(10);
        field.setText(ban);
        field.addActionListener(this);
        temp.add(field);
        return temp;
    }

    public boolean equals(Object obj) {
        return (obj instanceof BotCustomBan);
    }

    public String toString() {
        return "BanCommand";
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void install(TKBot bot) {
        if (TKBot.DEBUG > 3) System.out.println("Installing Custom Ban");
        (new BotJoinType(bot)).addBotCommandInterpreter(this);
    }

    public void remove(TKBot bot) {
        (new BotJoinType(bot)).removeBotCommandInterpreter(this);
    }

    public boolean isInstalled(TKBot bot) {
        return (new BotJoinType(bot)).containsInterpreter(this);
    }
}
