package desperateDoctors;

import java.util.HashMap;
import java.awt.*;
import desperateDoctors.userInterface.UserInterface2;
import java.io.Serializable;

/**
 * Classe associant des Images avec des String, et lan�ant intelligement le t�l�chargement des
 * images qui ne sont pas encore charg�e.
 * des op�rations avec.
 * @author Nad�ge Barrage
 * @author Jean-Fran�ois Geyelin
 * @author Francis Lim
 * @version 1.0
 * @since 1.0
 */
public class ImageHashMap {

    private static transient HashMap<String, Image> images;

    private static UserInterface2 ui;

    public static void init() {
        ui = null;
        images = new HashMap<String, Image>();
    }

    public static void dispose() {
        images = null;
        ui = null;
    }

    public static void setUi(UserInterface2 ui_) {
        ui = ui_;
    }

    public static Image getImage(String s) {
        if (ui == null) return null;
        Image image = images.get(s);
        if (image == null) {
            image = ui.loadImage("graphics/" + s);
            images.put(s, image);
        }
        return image;
    }
}
