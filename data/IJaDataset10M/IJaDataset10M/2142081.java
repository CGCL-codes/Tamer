package googlechartwrapper.data;

/**
 * 
 * @author steffan
 *
 */
public class VennDiagramData {

    private int circleSizeA;

    private int circleSizeB;

    private int circleSizeC;

    private int areaAB;

    private int areaAC;

    private int areaBC;

    private int areaABC;

    /**
	 * @param areaAB the area of A intersecting B
	 * @param areaABC the area of A intersecting B intersecting C
	 * @param areaAC the area of A intersecting C
	 * @param areaBC the area of B intersecting C
	 * @param circleSizeA specify the relative size
	 * @param circleSizeB specify the relative size
	 * @param circleSizeC specify the relative size
	 */
    public VennDiagramData(int circleSizeA, int circleSizeB, int circleSizeC, int areaAB, int areaAC, int areaBC, int areaABC) {
        this.areaAB = areaAB;
        this.areaABC = areaABC;
        this.areaAC = areaAC;
        this.areaBC = areaBC;
        this.circleSizeA = circleSizeA;
        this.circleSizeB = circleSizeB;
        this.circleSizeC = circleSizeC;
    }

    /**
	 * @return the circleSizeA
	 */
    public int getCircleSizeA() {
        return circleSizeA;
    }

    /**
	 * @param circleSizeA the circleSizeA to set
	 */
    public void setCircleSizeA(int circleSizeA) {
        this.circleSizeA = circleSizeA;
    }

    /**
	 * @return the circleSizeB
	 */
    public int getCircleSizeB() {
        return circleSizeB;
    }

    /**
	 * @param circleSizeB the circleSizeB to set
	 */
    public void setCircleSizeB(int circleSizeB) {
        this.circleSizeB = circleSizeB;
    }

    /**
	 * @return the circleSizeC
	 */
    public int getCircleSizeC() {
        return circleSizeC;
    }

    /**
	 * @param circleSizeC the circleSizeC to set
	 */
    public void setCircleSizeC(int circleSizeC) {
        this.circleSizeC = circleSizeC;
    }

    /**
	 * @return the areaAB
	 */
    public int getAreaAB() {
        return areaAB;
    }

    /**
	 * @param areaAB the areaAB to set
	 */
    public void setAreaAB(int areaAB) {
        this.areaAB = areaAB;
    }

    /**
	 * @return the areaAC
	 */
    public int getAreaAC() {
        return areaAC;
    }

    /**
	 * @param areaAC the areaAC to set
	 */
    public void setAreaAC(int areaAC) {
        this.areaAC = areaAC;
    }

    /**
	 * @return the areaBC
	 */
    public int getAreaBC() {
        return areaBC;
    }

    /**
	 * @param areaBC the areaBC to set
	 */
    public void setAreaBC(int areaBC) {
        this.areaBC = areaBC;
    }

    /**
	 * @return the areaABC
	 */
    public int getAreaABC() {
        return areaABC;
    }

    /**
	 * @param areaABC the areaABC to set
	 */
    public void setAreaABC(int areaABC) {
        this.areaABC = areaABC;
    }
}
