package edu.psu.citeseerx.domain;

/**
 * Data object transporting Venue statistics
 * @author Isaac Councill
 * @version $Rev: 854 $ $Date: 2009-01-02 12:02:16 -0500 (Fri, 02 Jan 2009) $
 */
public class VenueStat implements Comparable<VenueStat> {

    private String name;

    private float impact;

    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getImpact() {
        return impact;
    }

    public void setImpact(float impact) {
        this.impact = impact;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int compareTo(VenueStat otherStat) {
        if (this.getImpact() > otherStat.getImpact()) return 1;
        if (this.getImpact() < otherStat.getImpact()) return -1;
        return 0;
    }
}
