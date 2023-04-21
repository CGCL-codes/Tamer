package neon.objects;

import java.util.ArrayList;
import java.util.Collection;
import org.jdom2.Element;

public class Crafts {

    private static ArrayList<Element> prints = new ArrayList<Element>();

    public static void addBlueprints(Collection<Element> things) {
        prints.addAll(things);
    }

    public static Collection<Element> getBlueprints() {
        return prints;
    }
}
