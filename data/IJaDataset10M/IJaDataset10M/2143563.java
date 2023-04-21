package edu.ucdavis.genomics.metabolomics.binbase.cluster.job;

import java.io.Serializable;

/**
 * updates all the meta data
 * 
 * @author wohlgemuth
 * 
 */
public class UpdateMetaDataJob implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UpdateMetaDataJob) {
            UpdateMetaDataJob job = (UpdateMetaDataJob) obj;
            return job.getClass().getName().equals(this.getClass().getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
