package barrywei.igosyncdocs.bean;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * 
 * 
 *
 *
 * @author BarryWei
 * @version 1.0, Jul 16, 2010
 * @since JDK1.6
 */
public class IGoImageManager {

    private static IGoImageManager instance;

    private IGoImageManager() {
    }

    public static IGoImageManager getInstance() {
        if (instance == null) instance = new IGoImageManager();
        return instance;
    }

    public Icon getIcon(String iconName) {
        return new ImageIcon(getClass().getResource("/barrywei/igosyncdocs/resource/icon/" + iconName));
    }

    public Image getImage(String imageName) {
        return new ImageIcon(getClass().getResource("/barrywei/igosyncdocs/resource/icon/" + imageName)).getImage();
    }
}
