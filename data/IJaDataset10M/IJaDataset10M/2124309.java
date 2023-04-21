package fitlibrary.specify.domain;

import java.util.HashMap;
import java.util.Map;
import fitlibrary.object.DomainFixtured;
import fitlibrary.specify.eg.Colour;

@SuppressWarnings("rawtypes")
public class Maps implements DomainFixtured {

    private Map aColourMap = new HashMap();

    private Map aStringMap = new HashMap();

    @SuppressWarnings("unchecked")
    public Maps() {
        aColourMap.put(Colour.RED, Colour.GREEN);
        aColourMap.put(Colour.YELLOW, Colour.BLUE);
    }

    public Map getAStringMap() {
        return aStringMap;
    }

    public void setAStringMap(Map stringMap) {
        aStringMap = stringMap;
    }

    public Map getAColourMap() {
        return aColourMap;
    }

    public void setAColourMap(Map colourMap) {
        aColourMap = colourMap;
    }
}
