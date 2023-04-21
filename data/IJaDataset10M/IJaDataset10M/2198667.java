package D_Material;

import java.io.Serializable;
import java.util.*;

/**
 * @author Studio
 * @version 1.0
 * @created 15-12-2009 13:11:16
 */
public class Store implements Serializable {

    private int id;

    protected String capacity;

    protected String technicalParametrs;

    protected Map<Integer, Integer> materials;

    public Store() {
    }

    public Store(String capacity, String technicalParametrs) {
        this.capacity = capacity;
        this.technicalParametrs = technicalParametrs;
        this.materials = new HashMap<Integer, Integer>();
    }

    public String getCapacity() {
        return capacity;
    }

    public String getTechnicalParametrs() {
        return technicalParametrs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Store other = (Store) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    public int getId() {
        return id;
    }

    public Map<Integer, Integer> getMaterials() {
        return new HashMap(materials);
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMaterials(Map<Integer, Integer> materials) {
        this.materials = materials;
    }

    public void setTechnicalParametrs(String technicalParametrs) {
        this.technicalParametrs = technicalParametrs;
    }
}
