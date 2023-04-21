package hu.schmidtsoft.map.render.model.io;

import hu.schmidtsoft.map.model.io.CharCollector;
import hu.schmidtsoft.map.model.io.StringEvent;
import hu.schmidtsoft.map.render.model.MRLocationRender;
import hu.schmidtsoft.map.render.model.MRObjectRender;
import hu.schmidtsoft.map.render.model.MRStroke;
import hu.schmidtsoft.map.util.UtilNumber;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationRenderHandler extends ObjectRenderHandler implements StringEvent, IStrokeHolder {

    public LocationRenderHandler(XmlConstsRender x, HashMap<String, String> detailLevelMap) {
        super(x, detailLevelMap);
        elements.put(x.stroke, new StrokeHandler(x));
        elements.put(x.size, new CharCollector(this));
        elements.put(x.label, new CharCollector(new LocationRenderHandlerLabel(this)));
    }

    int size = 1;

    boolean label = false;

    List<MRStroke> strokes = new ArrayList<MRStroke>();

    @Override
    public void setParent(Object parent) {
        super.setParent(parent);
        strokes = new ArrayList<MRStroke>();
        size = 1;
    }

    @Override
    public MRObjectRender createRenderObject() {
        MRLocationRender ret = new MRLocationRender();
        ret.setSize(size);
        ret.setLabel(label);
        ret.setStrokes(strokes.toArray(new MRStroke[0]));
        return ret;
    }

    public void addStroke(MRStroke stroke) {
        strokes.add(stroke);
    }

    public void setString(String s) {
        size = UtilNumber.parseInt(s);
    }
}
