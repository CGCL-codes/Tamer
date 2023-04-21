package oopex.openjpa2.jpa2x.relationships.model;

public class AddressDetails {

    private long id;

    private String description;

    private double floorArea;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(double floorArea) {
        this.floorArea = floorArea;
    }

    public String toString() {
        return String.format("AddressDetails(%d): %s, floor-area: %.2f", new Long(id), description, new Double(floorArea));
    }
}
