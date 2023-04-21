package edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample;

import java.util.Collection;

/**
 * provides us with meta data about samples
 * @author wohlgemuth
 * @swt
 * @author wohlgemuth
 * @hibernate.class table = "META_KEY" dynamic-insert = "false" dynamic-update =
 *                  "false"
 * @hibernate.cache usage = "read-only"
 */
public class MetaKey implements Comparable {

    private Integer id;

    private String key;

    private Collection sampleInfo;

    /**
	 * @hibernate.id column = "`key_id`" generator-class = "native"
	 * @hibernate.generator-param name = "sequence" value = "LINK_ID"
	 */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * @hibernate.property column = "`key`" insert = "true" update =
	 *                     "true" not-null = "true"
	 * 
	 * @swt.variable visible="true" name="Key" searchable="true"
	 * @swt.modify canModify="false"
	 * @return
	 */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
	 * 
	 * @hibernate.set lazy="true" cascade = "none" inverse = "true"
	 * @hibernate.collection-one-to-many class =
	 *                                   "edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.SampleInfo"
	 * @hibernate.collection-key column = "`key_id`"
	 * 
	 */
    public Collection getSampleInfo() {
        return sampleInfo;
    }

    public void setSampleInfo(Collection sampleInfo) {
        this.sampleInfo = sampleInfo;
    }

    public int compareTo(Object arg0) {
        return this.getId().compareTo(((MetaKey) arg0).getId());
    }

    public String toString() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MetaKey) {
            return (((MetaKey) obj).getId().equals(this.getId()));
        }
        return false;
    }
}
