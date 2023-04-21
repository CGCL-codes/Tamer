package dsc.netgame;

public abstract class HostWorldComponent implements java.io.Serializable {

    protected HostWorld hostWorld;

    protected Location location;

    protected int id;

    protected int owner;

    protected int scanRange;

    protected String name;

    public HostWorldComponent(HostWorld hw, int owner, Location location) {
        hostWorld = hw;
        this.location = (Location) location.clone();
        this.owner = owner;
    }

    public void doRound() {
    }

    public abstract void putMessage(WorldComponentMessage m);

    public abstract WorldComponent getWorldComponent(int player);

    public int getScanRange() {
        return scanRange;
    }

    public double getScanRangeDivisor() {
        return (double) 1.0;
    }

    public Location getLocation() {
        return (Location) location.clone();
    }

    public void setLocation(Location l) {
        location = (Location) l.clone();
    }

    public int getId() {
        return id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int player) {
        owner = player;
    }

    void setId(int i) {
        id = i;
    }

    public void setName(String n) {
        name = n;
    }

    public boolean isVisible() {
        return false;
    }

    public String getName() {
        return name;
    }

    public int getRadius() {
        return 0;
    }
}
