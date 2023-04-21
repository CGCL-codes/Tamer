package meraner81.jets.shared.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import meraner81.jets.processing.parser.mapparser.ParseException;
import meraner81.jets.processing.parser.mapparser.Q3MapParser;
import meraner81.jets.processing.parser.mapparser.handler.EventMapParserHandler;
import meraner81.jets.processing.parser.mapparser.handler.MapParserEventListener;
import meraner81.jets.shared.model.Brush;
import meraner81.jets.shared.model.Entity;
import meraner81.jets.shared.model.Side;

public class MapExplorer implements MapParserEventListener {

    private Q3MapParser parser;

    private Set<Double> p1s;

    public MapExplorer(String fileName) {
        parser = new Q3MapParser(fileName);
        EventMapParserHandler handler = new EventMapParserHandler();
        handler.addMapParserEventListener(this);
        parser.setHandler(handler);
    }

    public void run() throws ParseException {
        p1s = new HashSet<Double>();
        parser.parseFile();
        Iterator<Double> it = p1s.iterator();
        while (it.hasNext()) {
            Double element = (Double) it.next();
            System.out.println(element);
        }
    }

    public void onFoundBrush(Entity entity, Brush brush) {
        for (int i = 0; i < brush.getSideCount(); i++) {
            Side s = brush.getSideByIndex(i);
            if (s.getP3() != 0) {
                p1s.add(s.getP3());
            }
        }
    }

    public void onFoundEntity(List<Entity> entities, Entity tmp) {
    }

    public static void main(String[] args) throws ParseException {
        String mapPath = "C:\\Programme\\Wolfenstein - Enemy Territory\\etmain\\maps\\";
        String superGoldRush = mapPath + "SuperGoldrush_Final.map";
        MapExplorer explorer = new MapExplorer(superGoldRush);
        explorer.run();
    }
}
