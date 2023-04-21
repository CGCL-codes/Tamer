package fitlibraryGeneric.specify.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fitlibrary.collection.map.ListOfMapsTraverse;
import fitlibrary.collection.map.SubsetMapTraverse;
import fitlibrary.object.DomainFixtured;
import fitlibrary.specify.eg.Colour;

public class GenericMaps implements DomainFixtured {

    private Map<Colour, Colour> aMap = new HashMap<Colour, Colour>();

    private Map<Colour, Map<Colour, Colour>> aMapOfMaps = new HashMap<Colour, Map<Colour, Colour>>();

    public GenericMaps() {
        aMap.put(Colour.GREEN, Colour.BLUE);
        aMap.put(Colour.BLACK, Colour.YELLOW);
        cyclicMaps(Colour.RED, Colour.GREEN);
        cyclicMaps(Colour.WHITE, Colour.BLACK);
        cyclicMaps(Colour.BLUE, Colour.YELLOW);
    }

    private void cyclicMaps(Colour colour1, Colour colour2) {
        Map<Colour, Colour> map1 = new HashMap<Colour, Colour>();
        map1.put(colour2, colour1);
        aMapOfMaps.put(colour1, map1);
    }

    public Map<Colour, Map<Colour, Colour>> getAMapOfMaps() {
        return aMapOfMaps;
    }

    public void setAMapOfMaps(Map<Colour, Map<Colour, Colour>> mapOfMaps) {
        aMapOfMaps = mapOfMaps;
    }

    public Map<Colour, Colour> getAMap() {
        return aMap;
    }

    public void setAMap(Map<Colour, Colour> map) {
        aMap = map;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public SubsetMapTraverse aSubsetMap() {
        return new SubsetMapTraverse(new HashMap(aMap));
    }

    public ListOfMapsTraverse aListOfMaps() {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("color", Colour.RED);
        map.put("count", 1);
        maps.add(map);
        map = new HashMap<String, Object>();
        map.put("color", Colour.GREEN);
        map.put("count", 2);
        maps.add(map);
        map = new HashMap<String, Object>();
        map.put("color", Colour.YELLOW);
        map.put("count", "three");
        maps.add(map);
        return new ListOfMapsTraverse(maps);
    }

    public ListOfMapsTraverse aListOfMapsWithEmpty() {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("color", Colour.RED);
        map.put("count", 1);
        maps.add(map);
        map = new HashMap<String, Object>();
        map.put("count", 2);
        maps.add(map);
        return new ListOfMapsTraverse(maps);
    }
}
