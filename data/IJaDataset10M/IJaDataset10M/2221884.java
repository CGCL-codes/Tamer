package de.wiedmann.part;

/**
 * @author Walter Wiedmann
 * @version 0.1.0 Create / Erstellt: 02.09.2008
 * 
 */
public class AnalogPart extends Part {

    private String testLabel;

    private Double testLo;

    private Double testHi;

    private Double partLo;

    private Double partHi;

    private Double partValue;

    private Double partTol;

    private Double loTol;

    private Double hiTol;

    private Boolean parallel = false;

    private String parallelName;

    private AnalogPart parallelPart;

    private Double goodLsl;

    private Double goodUsl;

    private Double cp;

    private Double cpl;

    private Double cpu;

    private Double cpk;

    private int sampleSize;

    static final Double WELL_TOL = 0.05;

    static final Double GOOD_TOL = 0.1;

    static final Double FAIR_TOL = 0.25;

    static final Double TARGET_TOL = 0.03;

    public static final Double TARGET_CPK = 1.0;

    /**
	 * @param articleNumber
	 * @param description
	 */
    public AnalogPart(String articleNumber, String description, Double partLo, Double partHi) throws IllegalArgumentException {
        super(articleNumber, description);
        if (partLo >= partHi) {
            throw new IllegalArgumentException("partLo >= partHi");
        } else {
            this.partLo = partLo;
            this.partHi = partHi;
            setPartValueAndTolerance();
        }
        this.testLo = null;
        this.testHi = null;
    }

    private Double getValue(Double lo, Double hi) {
        return (hi + lo) / 2;
    }

    private Double getTolerance(Double value, Double difference) {
        return 1.0 / value * (value - difference);
    }

    private void setPartValueAndTolerance() {
        partValue = getValue(partLo, partHi);
        partTol = getTolerance(partValue, partLo);
    }

    /**
	 * 
	 * @param testLo => Lo limit
	 * @param testHi => Hi limit
	 * @param testLabel => name of the Label 
	 */
    public void setTestLimits(Double testLo, Double testHi, String testLabel) {
        if (testLo >= testHi) {
            throw new IllegalArgumentException("testLo >= testHi");
        }
        this.testLo = testLo;
        loTol = ((1.0 / partValue) * testLo) - 1.0;
        this.testHi = testHi;
        hiTol = ((1.0 / partValue) * testHi) - 1.0;
        this.testLabel = testLabel;
    }

    /**
	 * @return testHi
	 */
    public Double getTestHi() {
        return testHi;
    }

    /**
	 * @return testLo
	 */
    public Double getTestLo() {
        return testLo;
    }

    @Override
    public String toString() {
        return super.toString() + " => Lo=" + partLo.toString() + " Hi=" + partHi.toString() + " => Val=" + partValue.toString() + " Tol=" + new Double(partTol * 100).toString() + "%";
    }

    @Override
    public int getTestStatus() {
        if (testLo == null || testHi == null) {
            if (parallel) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if ((loTol > -partTol - WELL_TOL) && (hiTol < partTol + WELL_TOL)) {
                return 3;
            } else {
                if ((loTol > -partTol - GOOD_TOL) && (hiTol < partTol + GOOD_TOL)) {
                    return 2;
                } else {
                    if ((loTol > -partTol - FAIR_TOL) && (hiTol < partTol + FAIR_TOL)) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }

    @Override
    public String translateTestStatus(int testStatus) {
        switch(testStatus) {
            case -1:
                if (parallel) {
                    return "Part is parallel to " + parallelName + "!";
                } else {
                    return "Part was not found in Genrad logfile!";
                }
            case 0:
                if (parallel) {
                    return "Part is parallel to " + parallelName + "!";
                } else {
                    return "Tolerance is too big. Part is poorly tested!";
                }
            case 1:
                return "Tolerance to something big. Part is sufficient tested!";
            case 2:
                return "Tolerance good. Part is good tested!";
            case 3:
                return "Tolerance well. Part ist well tested!";
            default:
                return "Unknown test status";
        }
    }

    /**
	 * @return hiTol => hi tolerance of the test
	 */
    public Double getHiTol() {
        return hiTol;
    }

    /**
	 * @return loTol => lo tolerance of the test
	 */
    public Double getLoTol() {
        return loTol;
    }

    /**
	 * @return partTol => part tolerance
	 */
    public Double getPartTol() {
        return partTol;
    }

    /**
	 * @return partValue => part value
	 */
    public Double getPartValue() {
        return partValue;
    }

    /**
	 * 
	 * @return TargetTo
	 */
    public Double getTargetLo() {
        return (partValue) * (1 - partTol - TARGET_TOL);
    }

    /**
	 * 
	 * @return TargetHi
	 */
    public Double getTargetHi() {
        return (partValue) * (1 + partTol + TARGET_TOL);
    }

    @Override
    public String getType() {
        return "AnalogPart";
    }

    /**
	 * @return cp
	 */
    public Double getCp() {
        return cp;
    }

    /**
	 * @param cp The cp to set. 
	 */
    public void setCp(Double cp) {
        this.cp = cp;
    }

    /**
	 * @return cpk
	 */
    public Double getCpk() {
        return Math.min(cpl, cpu);
    }

    /**
	 * @return sampleSize
	 */
    public int getSampleSize() {
        return sampleSize;
    }

    /**
	 * @param sampleSize The sampleSize to set. 
	 */
    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    /**
	 * @return partHi
	 */
    public Double getPartHi() {
        return partHi;
    }

    /**
	 * @return partLo
	 */
    public Double getPartLo() {
        return partLo;
    }

    /**
	 * @return testLabel
	 */
    public String getTestLabel() {
        return testLabel;
    }

    /**
	 * @return lsl
	 */
    public Double getCpl() {
        return cpl;
    }

    /**
	 * @param lsl The lsl to set. 
	 */
    public void setCpl(Double lsl) {
        this.cpl = lsl;
    }

    /**
	 * @return usl
	 */
    public Double getCpu() {
        return cpu;
    }

    /**
	 * @param usl The usl to set. 
	 */
    public void setCpu(Double usl) {
        this.cpu = usl;
    }

    /**
	 * @return goodLsl
	 */
    public Double getGoodLsl() {
        return goodLsl;
    }

    /**
	 * @param goodLsl The goodLsl to set. 
	 */
    public void setGoodLsl(Double goodLsl) {
        this.goodLsl = goodLsl;
    }

    /**
	 * @return goodUsl
	 */
    public Double getGoodUsl() {
        return goodUsl;
    }

    /**
	 * @param goodUsl The goodUsl to set. 
	 */
    public void setGoodUsl(Double goodUsl) {
        this.goodUsl = goodUsl;
    }

    /**
	 * @param parallelPart
	 */
    public void setParallelPart(String parallelName, AnalogPart parallelPart) {
        parallel = true;
        this.parallelName = parallelName;
        this.parallelPart = parallelPart;
    }

    ;

    /**
	 * @return
	 */
    public String getParallelName() {
        return parallelName;
    }

    /**
	 * @return
	 */
    public Part getParallelPart() {
        return parallelPart;
    }

    /**
	 * @return
	 */
    public Boolean isParallel() {
        return parallel;
    }

    /**
	 * @return the fAIR_TOL
	 */
    public static Double getFAIR_TOL() {
        return FAIR_TOL;
    }

    /**
	 * @return the GOOD_TOL
	 */
    public static Double getGOOD_TOL() {
        return GOOD_TOL;
    }

    /**
	 * @return the gOOD_TOL
	 */
    public static Double getWELL_TOL() {
        return WELL_TOL;
    }

    /**
	 * @return the tARGET_CPK
	 */
    public static Double getTARGET_CPK() {
        return TARGET_CPK;
    }

    /**
	 * @return the tARGET_TOL
	 */
    public static Double getTARGET_TOL() {
        return TARGET_TOL;
    }
}
