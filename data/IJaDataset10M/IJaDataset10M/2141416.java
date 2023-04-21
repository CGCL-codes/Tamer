package gqtitypes;

public class AreaMapEntry {

    private String mappedValue;

    private String shape;

    private String coords;

    private Boolean isnull;

    public AreaMapEntry(String mValue, String shpe, String cords, Boolean isnll) {
        mappedValue = mValue;
        shape = shpe;
        coords = cords;
        isnull = isnll;
    }

    public String getMappedValue() {
        return mappedValue;
    }

    public String getShape() {
        return shape;
    }

    public String getCoords() {
        return coords;
    }

    public Boolean isnull() {
        return isnull;
    }
}
