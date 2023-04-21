package phonebook.entry;

import javax.swing.ImageIcon;
import javax.swing.Icon;
import common.NoSuchLocationException;
import common.Configuration;

/**
 * <p>Title: Location </p>
 * <p>Description: This class describes the location a person is calling from</p>
 * @author Ynon
 * @version 1.0
 */
public class Location {

    private String m_name;

    private ImageIcon m_icon;

    private int m_index;

    private static Location[] s_AvailableLocations = { new Location("Home", Configuration.getInstance().getIconsDir() + "/home.png", 0), new Location("Work", Configuration.getInstance().getIconsDir() + "/work.png", 1), new Location("Mobile", Configuration.getInstance().getIconsDir() + "/mobile.png", 2) };

    public static Location getLocationForString(String location) throws NoSuchLocationException {
        for (int i = 0; i < s_AvailableLocations.length; i++) {
            if (s_AvailableLocations[i].getName().equals(location)) {
                return s_AvailableLocations[i];
            }
        }
        throw new NoSuchLocationException(location);
    }

    public static Location getHomeLocation() {
        return s_AvailableLocations[0];
    }

    public static Location getWorkLocation() {
        return s_AvailableLocations[1];
    }

    public static Location getMobileLocation() {
        return s_AvailableLocations[2];
    }

    public static Location[] getAvailableLocations() {
        return s_AvailableLocations;
    }

    public Icon getImage() {
        return m_icon;
    }

    public String getName() {
        return m_name;
    }

    public String toString() {
        return Configuration.messages.getString("Loc" + getName());
    }

    public int getIndex() {
        return m_index;
    }

    private Location(String name, String imagefile, int index) {
        m_name = name;
        m_icon = new ImageIcon(imagefile);
        m_index = index;
    }
}
