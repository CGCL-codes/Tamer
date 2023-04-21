package intranetchatv3.df;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

/**
 *
 * @author Philip
 */
public class VariableStore {

    private static volatile VariableStore instance;

    public String[] fonts;

    public String networkID;

    public String netPort;

    public boolean encrypted;

    public Point location;

    public String username;

    public String lf;

    public Font font;

    public Color back;

    public Color system;

    public Color out;

    public Color in;

    public boolean emoticons;

    public static synchronized VariableStore getInstance() {
        if (instance == null) {
            instance = new VariableStore();
        }
        return instance;
    }

    private VariableStore() {
        networkID = "0";
        netPort = "5454";
        encrypted = false;
        location = new Point(200, 200);
        username = "New User";
        lf = "Metal";
        font = new Font("Ariel", Font.PLAIN, 12);
        back = new Color(255, 255, 255);
        system = new Color(212, 6, 130);
        out = new Color(0, 0, 0);
        in = new Color(238, 159, 41);
        emoticons = true;
    }
}
