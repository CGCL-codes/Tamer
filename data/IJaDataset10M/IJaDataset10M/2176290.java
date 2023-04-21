package nskhan.utilities;

import java.io.File;
import java.util.ArrayList;
import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_tag;

@xml_tag("BeginSketch")
public class StrokeFile extends ElementState {

    @xml_collection("Stroke")
    ArrayList<Stroke> strokes = null;

    private static int autoId = 0;

    public String filename = null;

    public StrokeFile() {
        this.strokes = new ArrayList<Stroke>();
        filename = "TvS-" + Helper.paddingString(Integer.toString(autoId++), 3, '0', true);
        Stroke.autoId = 1;
    }

    public void addStroke(Stroke stroke) {
        this.strokes.add(stroke);
    }

    public Stroke getStroke(int index) {
        return strokes.get(index);
    }

    public void saveStrokeFile() {
        try {
            File f = new File("./xml files/" + this.filename + ".xml");
            writePrettyXML(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveStrokeFile(String path) {
        try {
            File f = new File(path);
            writePrettyXML(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readStrokeFile(String path) {
    }

    public ArrayList<Stroke> getStrokes() {
        return strokes;
    }

    public Stroke getStrokeById(int strokeId) {
        for (Stroke s : strokes) {
            if (s.getId() == strokeId) return s;
        }
        return null;
    }
}
