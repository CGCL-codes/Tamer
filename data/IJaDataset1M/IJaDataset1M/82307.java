package tk.bot;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class BotCustomHello extends BotBasicInterpreter implements BotCustomCommand, ActionListener {

    static String[] greetings;

    Vector loader = new Vector();

    BotUtilities u = new BotUtilities();

    JList list;

    JTextField field;

    BotDatabaseInterface db;

    TKBot bot;

    int i = 0;

    static boolean enabled = false;

    public BotCustomHello(TKBot bot) {
        this.bot = bot;
        db = (BotDatabaseInterface) bot.dbManager.getInterface("commands");
        greetings = u.parse(db.getProperty("customhello", "Hello %name%"), ",");
        for (int j = 0; j < greetings.length; j++) {
            loader.addElement(greetings[j]);
        }
    }

    public void commandPerformed(CommandEvent e) {
        if (enabled) {
            BotCommandType type = e.getType();
            String command = e.getCommand();
            if (type instanceof BotJoinType) {
                if (i == greetings.length) i = 0;
                BotUser user = u.getUserFromType(command);
                String greeting = u.replaceVars(greetings[i], user.getHashtable(bot.getUserTable()));
                type.getBot().addCommand(greeting);
                type.getBot().appendLine("#" + greeting);
                i++;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Add") || e.getSource().equals(field)) {
            loader.add(field.getText());
            refresh();
        }
        if (command.equals("Remove")) {
            loader.remove(list.getSelectedValue());
            refresh();
        }
    }

    void refresh() {
        greetings = new String[loader.size()];
        i = 0;
        loader.copyInto(greetings);
        list.setListData(loader);
        list.repaint();
        String temp = greetings[0];
        for (int j = 1; j < greetings.length; j++) {
            temp += "," + greetings[j];
        }
        db.setProperty("customhello", temp);
        db.store();
    }

    public JPanel getConfig() {
        JPanel temp = new JPanel();
        list = new JList(greetings);
        JScrollPane scroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        field = new JTextField(10);
        field.addActionListener(this);
        JButton add = new JButton("Add");
        add.addActionListener(this);
        JButton remove = new JButton("Remove");
        remove.addActionListener(this);
        temp.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 4;
        c.fill = c.BOTH;
        temp.add(scroller, c);
        c.gridx = 2;
        c.gridheight = 1;
        temp.add(field, c);
        c.gridy = 2;
        temp.add(add, c);
        c.gridy = 3;
        temp.add(remove, c);
        return temp;
    }

    public boolean equals(Object obj) {
        return (obj instanceof BotCustomHello);
    }

    public String toString() {
        return "HelloCommand";
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void install(TKBot bot) {
        if (TKBot.DEBUG > 3) System.out.println("Installing Custom Hello");
        (new BotJoinType(bot)).addBotCommandInterpreter(this);
    }

    public void remove(TKBot bot) {
        (new BotJoinType(bot)).removeBotCommandInterpreter(this);
    }

    public boolean isInstalled(TKBot bot) {
        return (new BotJoinType(bot)).containsInterpreter(this);
    }
}
