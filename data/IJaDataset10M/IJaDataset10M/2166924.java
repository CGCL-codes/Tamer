package lpp.citytrans.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.maps.GeoPoint;
import lpp.citytrans.geo.MapPoint;

public class Stopover implements Comparable<Stopover> {

    private final String id;

    private final GeoPoint mapPoint;

    private String name;

    private String description;

    private Map<String, Line> lines;

    private Set<Stopover> neighbours;

    public Stopover(String id, GeoPoint mapPoint, String name, String description) {
        this.id = id;
        this.mapPoint = mapPoint;
        this.name = name;
        this.description = description;
        lines = new HashMap<String, Line>();
    }

    public GeoPoint getLocation() {
        return this.mapPoint;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void AddLine(Line line) {
        if (line == null) {
            throw new NullPointerException("line can not be null");
        }
        String lineId = line.getLineId();
        lines.put(lineId, line);
    }

    public int compareTo(Stopover another) {
        return getName().compareTo(another.getName());
    }
}
