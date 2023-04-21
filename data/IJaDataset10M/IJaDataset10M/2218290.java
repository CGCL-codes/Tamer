package reputationreasoners.liar.trustintention;

public class Lev {

    private Double trust;

    private Double distrust;

    private Integer relevance;

    /**
	 * 
	 */
    public Lev(Double trust, Double distrust, Integer relevance) {
        this.trust = trust;
        this.distrust = distrust;
        this.relevance = relevance;
    }

    /**
	 * 
	 */
    public Double getTrust() {
        return this.trust;
    }

    /**
	 * 
	 */
    public Double getDistrust() {
        return this.distrust;
    }

    /**
	 * 
	 */
    public Integer getRelevance() {
        return this.relevance;
    }
}
