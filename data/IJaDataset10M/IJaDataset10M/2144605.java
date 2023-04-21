package objectif.lyon.designer.data.reader;

import java.util.Map;
import org.w3c.dom.Node;
import objectif.lyon.data.SimpleRoute;
import objectif.lyon.data.TypeRoute;
import objectif.lyon.designer.gui.component.SegmentObject;

public class SegmentObjectReader implements ElementReader<SegmentObject> {

    @Override
    public SegmentObject read(Node node, Map<String, String> properties) {
        int x = Integer.valueOf(properties.get("x"));
        int y = Integer.valueOf(properties.get("y"));
        double angle = Double.valueOf(properties.get("angle"));
        SimpleRoute route = new SimpleRoute(TypeRoute.NORMAL, null, null);
        return new SegmentObject(route, x, y, angle);
    }
}
