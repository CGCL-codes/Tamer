package javacream.net;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;
import javacream.swing.Listable;
import javacream.util.Attributes;
import javacream.util.StringVector;

/**
 * Channel
 * 
 * @author Glenn Powell
 *
 */
public class Channel implements Listable, Serializable {

    private static final long serialVersionUID = 1L;

    private static final String NAME = "NAME";

    private static final String USERS = "USERS";

    private Attributes attributes = new Attributes();

    public Channel(String name) {
        attributes.put(NAME, name);
        attributes.put(USERS, new StringVector());
    }

    public String getName() {
        return attributes.getString(NAME, null);
    }

    public void addUser(String user) {
        StringVector users = attributes.get(StringVector.class, USERS, null);
        if (!users.contains(user)) users.add(user);
    }

    public int getUserCount() {
        StringVector users = attributes.get(StringVector.class, USERS, null);
        return users.size();
    }

    public String[] getUsers() {
        StringVector users = attributes.get(StringVector.class, USERS, null);
        return users.toArray(new String[users.size()]);
    }

    public void removeUser(String user) {
        StringVector users = attributes.get(StringVector.class, USERS, null);
        users.remove(user);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public Color getListableBackground(int row, int column, boolean isSelected, boolean hasFocus, boolean isDropTarget, Object argument) {
        return null;
    }

    public Font getListableFont(int row, int column, boolean isSelected, boolean hasFocus, boolean isDropTarget, Object argument) {
        return null;
    }

    public Color getListableForeground(int row, int column, boolean isSelected, boolean hasFocus, boolean isDropTarget, Object argument) {
        return null;
    }

    public String getListableText(int row, int column, boolean isSelected, boolean hasFocus, boolean isDropTarget, Object argument) {
        return getName();
    }

    public boolean isListableEnabled(int row, int column, boolean isSelected, boolean hasFocus, boolean isDropTarget, Object argument) {
        return true;
    }

    public boolean paintListableIcon(int row, int column, boolean isSelected, boolean hasFocus, boolean isDropTarget, Graphics2D g, int width, int height, Object argument) {
        return false;
    }

    public boolean equals(Object object) {
        if (object instanceof Channel) {
            Channel channel = (Channel) object;
            return channel.getName().equals(getName());
        }
        return false;
    }
}
