package pf.alaudes.mastermahjong.util;

import java.util.ResourceBundle;

public abstract class Ressource {

    public static ResourceBundle RESOURCE_JETTY = ResourceBundle.getBundle("jetty");

    public static int getInt(final String cle, final ResourceBundle resourceBundle) {
        return Integer.parseInt(resourceBundle.getString(cle));
    }
}
