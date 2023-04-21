package us.wthr.jdem846.rasterdata;

public class RasterDataLatLongBox {

    private double north;

    private double south;

    private double east;

    private double west;

    private double width;

    private double height;

    public RasterDataLatLongBox(double north, double south, double east, double west) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        width = (east - west);
        height = (north - south);
    }

    public boolean intersects(RasterDataLatLongBox other) {
        if (this.contains(other.getNorth(), other.getWest()) || this.contains(other.getSouth(), other.getWest()) || this.contains(other.getSouth(), other.getEast()) || this.contains(other.getNorth(), other.getEast()) || other.contains(this.getNorth(), this.getWest()) || other.contains(this.getSouth(), this.getWest()) || other.contains(this.getSouth(), this.getEast()) || other.contains(this.getNorth(), this.getEast())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean contains(double latitude, double longitude) {
        if (latitude >= getSouth() && latitude <= getNorth() && longitude >= getWest() && longitude <= getEast()) {
            return true;
        } else {
            return false;
        }
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getNorth() {
        return north;
    }

    public double getSouth() {
        return south;
    }

    public double getEast() {
        return east;
    }

    public double getWest() {
        return west;
    }

    public RasterDataLatLongBox copy() {
        return new RasterDataLatLongBox(north, south, east, west);
    }
}
