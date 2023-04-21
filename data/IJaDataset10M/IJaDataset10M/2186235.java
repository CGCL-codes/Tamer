package spaghetti;

import javax.swing.*;
import java.awt.*;

/**
 * @author Ulrich Staudinger, us@die-horde.de
 * 
 * built for Claudio Erba
 *
 * This source code is licensed under the GPL. 
 * First version on: 12:25:17 PM Dec 16, 2003
 */
public class User extends JPanel {

    public String nick, fullname, imageurl, description;

    public User(String nick) {
        this.nick = nick;
        setLayout(new BorderLayout());
        add(new JLabel(nick), BorderLayout.CENTER);
    }
}
