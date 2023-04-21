package us.wthr.jdem846.kml;

import java.util.LinkedList;
import java.util.List;
import org.dom4j.Element;

public class LineString extends Geometry {

    private List<Coordinate> coordinates = new LinkedList<Coordinate>();

    private boolean extrude = false;

    private boolean tessellate = false;

    private AltitudeModeEnum altitudeMode = AltitudeModeEnum.CLAMP_TO_GROUND;

    public LineString() {
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public void addCoordinate(Coordinate coordinate) {
        coordinates.add(coordinate);
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public boolean isExtrude() {
        return extrude;
    }

    public void setExtrude(boolean extrude) {
        this.extrude = extrude;
    }

    public boolean isTessellate() {
        return tessellate;
    }

    public void setTessellate(boolean tessellate) {
        this.tessellate = tessellate;
    }

    public AltitudeModeEnum getAltitudeMode() {
        return altitudeMode;
    }

    public void setAltitudeMode(AltitudeModeEnum altitudeMode) {
        this.altitudeMode = altitudeMode;
    }

    protected void loadKmlChildren(Element element) {
        super.loadKmlChildren(element);
        StringBuffer coordsBuffer = new StringBuffer();
        if (coordinates != null) {
            for (Coordinate coordinate : coordinates) {
                coordsBuffer.append(coordinate.toString() + "\r\n");
            }
        }
        Element coordsElement = element.addElement("coordinates");
        coordsElement.addText(coordsBuffer.toString());
        element.addElement("extrude").addText("" + extrude);
        element.addElement("tessellate").addText("" + tessellate);
        if (altitudeMode != null) {
            element.addElement("altitudeMode").addText(altitudeMode.text());
        }
    }

    public void toKml(Element parent) {
        Element element = parent.addElement("LineString");
        loadKmlChildren(element);
    }
}
