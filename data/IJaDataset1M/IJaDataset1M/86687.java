package harvestmars;

/**
 *
 * @author toshiba
 */
public class Stuff {

    protected int Plantable;

    protected int Plant;

    protected int Plough;

    protected int Seeded;

    protected int Watered;

    protected int Border;

    protected int Orang;

    protected int Kurcaci;

    protected int Harvested;

    protected int PlantDead;

    public Stuff() {
        Plantable = 0;
        Plant = 0;
        Plough = 0;
        Seeded = 0;
        Watered = 0;
        Orang = 0;
        Kurcaci = 0;
        Harvested = 0;
        PlantDead = 0;
    }

    public int getPlant() {
        return Plant;
    }

    public void setPlant(int Plant) {
        this.Plant = Plant;
    }

    public int getBorder() {
        return Border;
    }

    public void setBorder(int Border) {
        this.Border = Border;
    }

    public int getHarvested() {
        return Harvested;
    }

    public void setHarvested(int Harvested) {
        this.Harvested = Harvested;
    }

    public int getKurcaci() {
        return Kurcaci;
    }

    public void setKurcaci(int Kurcaci) {
        this.Kurcaci = Kurcaci;
    }

    public int getOrang() {
        return Orang;
    }

    public void setOrang(int Orang) {
        this.Orang = Orang;
    }

    public int getPlantDead() {
        return PlantDead;
    }

    public void setPlantDead(int PlantDead) {
        this.PlantDead = PlantDead;
    }

    public int getPlantable() {
        return Plantable;
    }

    public void setPlantable(int Plantable) {
        this.Plantable = Plantable;
    }

    public int getPlough() {
        return Plough;
    }

    public void setPlough(int Plough) {
        this.Plough = Plough;
    }

    public int getSeeded() {
        return Seeded;
    }

    public void setSeeded(int Seeded) {
        this.Seeded = Seeded;
    }

    public int getWatered() {
        return Watered;
    }

    public void setWatered(int Watered) {
        this.Watered = Watered;
    }
}
