package reputationreasoners.liar.reputation;

public class ReputationValue {

    public static final Double UNKNOWN = new Double(-10000);

    private Double value;

    private Integer reliability;

    /**
	 * 
	 */
    public ReputationValue(Double value, Integer reliability) {
        this.reliability = reliability;
        if (reliability < 0) {
            this.value = UNKNOWN;
        } else {
            this.value = Math.min(1.0, Math.max(-1.0, value));
        }
    }

    public Double getValue() {
        return value;
    }

    public Integer getReliability() {
        return reliability;
    }
}
