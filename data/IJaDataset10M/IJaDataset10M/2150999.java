package domeinLaag;

public class VliegtuigType {

    private Fabrikant fb;

    private String code;

    private int capaciteitRoken;

    private int capaciteitNietRoken;

    /**
	@param code
	@param capR
	@param capNR
	@param fabr
	 */
    public VliegtuigType(Fabrikant fb, String code, int capR, int capNR) {
        this.fb = fb;
        this.code = code;
        this.capaciteitRoken = capR;
        this.capaciteitNietRoken = capNR;
    }

    /**
	@return java.lang.String
	 */
    public String geefCode() {
        return code;
    }

    public Fabrikant geefFabrikant() {
        return fb;
    }

    /**
	@return int[]
	 */
    public int[] geefCapaciteit() {
        int[] cap = new int[2];
        cap[0] = capaciteitRoken;
        cap[1] = capaciteitNietRoken;
        return cap;
    }
}
