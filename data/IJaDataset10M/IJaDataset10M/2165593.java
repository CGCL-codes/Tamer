package org.identifylife.harvest.model;

/**
 * @author mike
 *
 */
public class Taxon {

    private Long id;

    private String uuid;

    private String name;

    private Integer rank = 0;

    private String lsid;

    /**
   * @return the id
   */
    public Long getId() {
        return id;
    }

    /**
   * @param id the id to set
   */
    public void setId(Long id) {
        this.id = id;
    }

    /**
   * @return the uuid
   */
    public String getUuid() {
        return uuid;
    }

    /**
   * @param uuid the uuid to set
   */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
   * @return the name
   */
    public String getName() {
        return name;
    }

    /**
   * @param name the name to set
   */
    public void setName(String name) {
        this.name = name;
    }

    /**
   * @return the rank
   */
    public Integer getRank() {
        return rank;
    }

    /**
   * @param rank the rank to set
   */
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    /**
   * @return the lsid
   */
    public String getLsid() {
        return lsid;
    }

    /**
   * @param lsid the lsid to set
   */
    public void setLsid(String lsid) {
        this.lsid = lsid;
    }
}
