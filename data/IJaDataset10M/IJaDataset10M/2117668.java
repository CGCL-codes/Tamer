package OpenDatcom;

/**
 * The main class contains all the Datcom Data. This should probably be split into
 * smaller, more manageable classes but for now it serves it's purpose. There is
 * a single instance of this class declared during program execution that serves
 * as a global variable.
 * @author -B-
 */
public class instanceData {

    String aggrateData = null;

    String aggrateDataWithComments = null;

    PlanformData hwPlanform = new PlanformData();

    PlanformData htPlanform = new PlanformData();

    String flightHeader;

    String machs;

    String altitudes;

    String aoas;

    String airfoil;

    double weight;

    ;

    double loop = 1;

    double nMach = 0;

    double nAlt = 0;

    double nAOA = 0;

    double stMach;

    double tsMach;

    double gamma;

    double XCG, ZCG;

    double XW, ZW;

    double XH, ZH;

    double XVF, ZVF;

    double XV, ZV;

    double ALIW, ALIH;

    double xValues[];

    double radii[];

    double sectionCount;

    boolean isValid;

    public instanceData() {
        xValues = new double[20];
        radii = new double[20];
    }

    public void refresh() {
        nMach = 0;
        nAlt = 0;
        nAOA = 0;
        if ((!machs.isEmpty()) && (!altitudes.isEmpty()) && (!aoas.isEmpty())) {
            String dummy = machs;
            nMach = dummy.split(",").length;
            dummy = altitudes;
            nAlt = dummy.split(",").length;
            dummy = aoas;
            nAOA = dummy.split(",").length;
        }
        createFLTCON();
        createSYNTH();
        createPlanform("WGPLNF", hwPlanform);
        createPlanform("HTPLNF", htPlanform);
        createBODY();
    }

    /**
     * Checks all numerical values against their upper and lower limits that the
     * datcom allows. This function attempts to prevent I/O errors or conditions
     * the DATCOM does not allow. Prints any errors found to the debug console.
     * 
     * @return true if data is valid.
     */
    private boolean validateData() {
        boolean isValid = true;
        isValid = testBounds(stMach, Double.MAX_VALUE, 0);
        return isValid;
    }

    /**
     * Tests the given value against its upper and lower bounds allowed by DATCOM.
     * Returns true if the value is within the given region, false otherwise.
     * @param value The value to test
     * @param upperBound The upper limit the value can be
     * @param lowerBound The lower limit the value can be
     * @return
     */
    private boolean testBounds(double value, double upperBound, double lowerBound) {
        if (value <= upperBound && value >= lowerBound) {
            return true;
        }
        return false;
    }

    /**
     * Creates and formats the FLTCON data. The data is added to the aggrateData
     * string.
     */
    private void createFLTCON() {
        aggrateData = "CASEID TEST TEST\n\n";
        safeAdd("#Start of Flight Conditions");
        safeAdd("flightHeader");
        safeAdd("$FLTCON");
        if (nMach == 0 || nAOA == 0 || nAlt == 0) {
            safeAdd("$");
            safeAdd("# End of Flight Conditions\n");
            return;
        }
        ;
        safeAdd("NMACH=", nMach);
        safeAdd("MACH(1)= ", machs);
        safeAdd("NALPHA=", nAOA);
        safeAdd("ALSCHD(1)= ", aoas);
        safeAdd("NALT=", nAlt);
        safeAdd("ALT(1)= ", altitudes);
        safeAdd("WT= ", weight);
        safeAdd("LOOP= ", loop);
        safeAdd("$");
        safeAdd("# End of Flight Conditions\n");
    }

    private void createPlanform(String NAMELIST, PlanformData sc) {
        safeAdd("# Start of " + NAMELIST + " Parameters");
        safeAdd("$" + NAMELIST);
        safeAdd("CHRDTP=", sc.CHRDTP);
        safeAdd("SSPNOP=", sc.SSPNOP);
        safeAdd("SSPNE=", sc.SSPNE);
        safeAdd("SSPN=", sc.SSPN);
        safeAdd("CHRDBP=", sc.CHRDBP);
        safeAdd("CHRDR=", sc.CHRDR);
        safeAdd("SAVSI=", sc.SAVSI);
        safeAdd("CHSTAT=", sc.CHSTAT);
        safeAdd("TWISTA=", sc.TWISTA);
        safeAdd("DHDADI=", sc.DHDADI);
        safeAdd("TYPE=", sc.TYPE);
        safeAdd("$");
        safeAdd("# End of " + NAMELIST + " Parameters\n");
    }

    /**
     * Creates and formats the SYNTH data. The data is added to the aggrateData
     * string.
     */
    private void createSYNTH() {
        safeAdd("# Start of Synthesis Parameters");
        safeAdd("$SYNTHS");
        safeAdd("XCG=", XCG);
        safeAdd("ZCG=", ZCG);
        safeAdd("XW=", XW);
        safeAdd("ZW=", ZW);
        safeAdd("ALIH=", ALIH);
        safeAdd("ALIW=", ALIW);
        safeAdd("XH=", XH);
        safeAdd("ZH=", ZH);
        safeAdd("XV=", XV);
        safeAdd("ZV=", ZV);
        safeAdd("$");
        safeAdd("# End of Synthesis Parameters\n");
    }

    /**
     * Creates and formats the BODY data. The data is added to the aggrateData
     * string.
     */
    private void createBODY() {
        String temp = "";
        safeAdd("# Start of Body Parameters");
        safeAdd("$BODY");
        if (xValues.length == 0 || radii.length == 0) {
            safeAdd("$");
            safeAdd("# End of Body Parameters\n");
            return;
        }
        safeAdd("NX=", sectionCount);
        temp = "X(1)=\t";
        for (int i = 0; i < xValues.length; i++) {
            temp += xValues[i] + ", ";
        }
        safeAdd(temp);
        temp = "R(1)=\t";
        for (int i = 0; i < xValues.length; i++) {
            temp += radii[i] + ", ";
        }
        temp = temp.substring(0, temp.length() - 2);
        safeAdd(temp + "$");
        safeAdd("# End of Body Parameters\n");
    }

    /**
     * All the safeAdd functions take the input data and format  it the following
     * way: <  Header \t Data, \n >. The input data is checked for error conditions
     * (empty string or NaN double) and rejected if invalid. If valid, it is
     * appended to the end of the aggragateData string.
     * @param Header
     * @param Data
     */
    private void safeAdd(String Header, double Data) {
        if (Double.isNaN(Data)) {
            return;
        }
        aggrateData += Header + "\t" + Data + ",\n";
    }

    /**
     * All the safeAdd functions take the input data and format  it the following
     * way: <  Data, \n >. The input data is checked for error conditions
     * (empty string or NaN double) and rejected if invalid. If valid, it is
     * appended to the end of the aggragateData string.
     * @param Data
     */
    private void safeAdd(String Data) {
        if (Data.isEmpty()) {
            return;
        }
        aggrateData += Data + "\n";
    }

    /**
     * All the safeAdd functions take the input data and format  it the following
     * way: <  Header \t Data, \n >. The input data is checked for error conditions
     * (empty string or NaN double) and rejected if invalid. If valid, it is
     * appended to the end of the aggragateData string.
     * @param Header
     * @param Data
     */
    private void safeAdd(String Header, String Data) {
        if (Data.isEmpty()) {
            return;
        }
        aggrateData += Header + "\t" + Data + ",\n";
    }

    public void setALIH(double ALIH) {
        this.ALIH = ALIH;
    }

    public void setALIW(double ALIW) {
        this.ALIW = ALIW;
    }

    public void setXCG(double XCG) {
        this.XCG = XCG;
    }

    public void setXH(double XH) {
        this.XH = XH;
    }

    public void setXV(double XV) {
        this.XV = XV;
    }

    public void setXVF(double XVF) {
        this.XVF = XVF;
    }

    public void setXW(double XW) {
        this.XW = XW;
    }

    public void setZCG(double ZCG) {
        this.ZCG = ZCG;
    }

    public void setZH(double ZH) {
        this.ZH = ZH;
    }

    public void setZV(double ZV) {
        this.ZV = ZV;
    }

    public void setZVF(double ZVF) {
        this.ZVF = ZVF;
    }

    public void setZW(double ZW) {
        this.ZW = ZW;
    }

    public void setAirfoil(String airfoil) {
        this.airfoil = airfoil;
    }

    public void setAltitudes(String altitudes) {
        this.altitudes = altitudes;
    }

    public void setAoas(String aoas) {
        this.aoas = aoas;
    }

    public void setFlightHeader(String flightHeader) {
        this.flightHeader = flightHeader;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public void setMachs(String machs) {
        this.machs = machs;
    }

    public void setRadii(double[] radii) {
        this.radii = radii;
    }

    public void setSectionCount(double sectionCount) {
        this.sectionCount = sectionCount;
    }

    public void setStMach(double stMach) {
        this.stMach = stMach;
    }

    public void setTsMach(double tsMach) {
        this.tsMach = tsMach;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setxValues(double[] xValues) {
        this.xValues = xValues;
    }

    public void setHtPlanform(PlanformData htPlanform) {
        this.htPlanform = htPlanform;
    }

    public void setHwPlanform(PlanformData hwPlanform) {
        this.hwPlanform = hwPlanform;
    }

    public String getData() {
        refresh();
        return aggrateData;
    }
}
