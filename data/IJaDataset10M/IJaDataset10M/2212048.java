package dbo;

/**
 *
 * @author bi
 */
public class Deployment {

    private int id;

    public Player player;

    public Squad squad;

    private int healthTotal;

    private int sizeTotal;

    public Deployment(int id) {
        this.id = id;
    }

    public Deployment(int id, Player player, Squad squad) {
        this.id = id;
        this.player = player;
        this.squad = squad;
        this.healthTotal = squad.getHealth() * squad.getSize();
        this.sizeTotal = squad.getSize();
    }

    public Deployment(int id, Player player, Squad squad, int healthTotal, int sizeTotal) {
        this.id = id;
        this.player = player;
        this.squad = squad;
        this.healthTotal = healthTotal;
        this.sizeTotal = sizeTotal;
    }

    public Deployment() {
        this.player = null;
        this.squad = null;
        this.healthTotal = 0;
        this.sizeTotal = 0;
    }

    /**
     * @return the healthTotal
     */
    public int getHealthTotal() {
        return healthTotal;
    }

    /**
     * @param healthTotal the healthTotal to set
     */
    public void setHealthTotal(int healthTotal) {
        this.healthTotal = healthTotal;
    }

    /**
     * @return the sizeTotal
     */
    public int getSizeTotal() {
        return sizeTotal;
    }

    /**
     * @param sizeTotal the sizeTotal to set
     */
    public void setSizeTotal(int sizeTotal) {
        this.sizeTotal = sizeTotal;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass())) {
            if (this.hashCode() == obj.hashCode()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        return hash;
    }
}
