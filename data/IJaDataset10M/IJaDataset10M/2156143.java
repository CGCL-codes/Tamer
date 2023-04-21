package edu.ucdavis.genomics.metabolomics.binbase.cluster.job;

import java.io.Serializable;
import java.util.Vector;

/**
 * tells the cluster we want to cache some bin meta informations
 * 
 * @author wohlgemuth
 */
public class CacheBinMetainformationsJob implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    String database;

    Vector<Integer> binId = new Vector<Integer>();

    public CacheBinMetainformationsJob(String database, Vector<Integer> binId) {
        super();
        this.database = database;
        this.binId = binId;
    }

    public CacheBinMetainformationsJob(String database, int binId) {
        super();
        this.database = database;
        this.binId.add(binId);
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Vector<Integer> getBinId() {
        return binId;
    }

    public void setBinId(Vector<Integer> binId) {
        this.binId = binId;
    }

    @Override
    public int hashCode() {
        return this.getDatabase().hashCode() + this.getBinId().hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " - " + this.getDatabase() + " - " + this.getBinId();
    }
}
