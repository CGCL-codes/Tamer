package RootP;

import java.awt.Container;
import java.awt.Image;
import java.awt.Polygon;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JPanel;
import GameThings.Figure;
import GameThings.Player;

public class Level extends JPanel {

    public static Container root;

    ArrayList<Polygon> collidableList = new ArrayList();

    ArrayList<Figure> figureList = new ArrayList();

    Player player;

    PlayerGUI playerGui;

    public EventLoop eventLoop;

    public static Image[] getPicArray(String[] URLs) {
        Image[] returnArr = new Image[URLs.length];
        for (int i = 0; i < returnArr.length; i++) returnArr[i] = RootFrame.getPic(URLs[i]);
        return returnArr;
    }

    public static Image getPic(String URL) {
        return RootFrame.getPic(URL);
    }

    public void lost() {
        root.remove(this);
        root.add(new LostScreen());
        root.validate();
    }
}
