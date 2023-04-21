package foa.properties.groups.sets;

import java.util.*;
import foa.properties.groups.sets.types.*;

/**
 * @author Fabio Giannetti
 * @version 0.0.1
 */
public class Background extends PropertySet {

    private ArrayList propertySets;

    private Hashtable items;

    private String[] paramEnum;

    public Background(Hashtable modifiedAttributes) {
        super("Background");
        propertySets = new ArrayList();
        items = new Hashtable();
        loadTables();
        String name;
        Enumeration e = items.keys();
        Type currentType;
        while (e.hasMoreElements()) {
            name = (String) (e.nextElement());
            if (name.endsWith("color")) {
                currentType = new ColorType(name, (String[]) items.get(name), false);
            } else if (name.indexOf("position") != -1) {
                currentType = new MixedType(name, (String[]) items.get(name));
            } else {
                currentType = new EnumerativeType(name, (String[]) items.get(name));
            }
            if (modifiedAttributes.containsKey(name)) {
                currentType.setStartingValue((String) modifiedAttributes.get(name));
            }
            propertySets.add(currentType);
        }
        this.setProperties(propertySets);
    }

    public void loadTables() {
        items.put("background-attachment", new String[] { "scroll", "fixed" });
        items.put("background-color", new String[] { "transparent", "black", "white", "silver", "lime", "gray", "olive", "green", "yellow", "maroon", "navy", "red", "blue", "purple", "teal", "fuchsia", "aqua", "orange" });
        items.put("background-position-horizontal", new String[] { "left", "center", "right" });
        items.put("background-position-vertical", new String[] { "top", "center", "bottom" });
        items.put("background-repeat", new String[] { "repeat", "repeat-x", "repeat-y", "no-repeat" });
    }
}
