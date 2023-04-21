package edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin;

/**
 * @swt multi ="true"
 * @hibernate.class table = "BIN_RATIO" dynamic-insert = "true" dynamic-update =
 *                  "true"
 * 
 */
public class BinIonFilter implements Comparable {

    private Integer id;

    private Bin bin;

    private Integer primaeryIon;

    private Integer secondaeryIon;

    private Double ratio;

    /**
	 * @hibernate.id column = "`id`" generator-class = "native"
	 * @hibernate.generator-param name = "sequence" value = "HIBERNATE_SEQUENCE"
	 *                            returns the id belonging to this refrence
	 * @return
	 */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * @swt.variable visible="false" name="Bin" searchable="true"
	 * @swt.modify canModify="false"
	 * @hibernate.many-to-one cascade = "none" column = "bin_id" class =
	 *                        "edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Bin"
	 *                        returns the bin, belonging to this refrence
	 * @return
	 */
    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    /**
	 * @swt.variable visible="true" name="primaery ion" searchable="true"
	 * @swt.modify canModify="true"
	 * @hibernate.property column = "`main_ion`" insert = "false" update =
	 *                     "false"
	 * 
	 * @return
	 */
    public Integer getPrimaeryIon() {
        return primaeryIon;
    }

    public void setPrimaeryIon(Integer primaeryIon) {
        this.primaeryIon = primaeryIon;
    }

    /**
	 * @swt.variable visible="true" name="secondaery ion" searchable="true"
	 * @swt.modify canModify="true"
	 * @hibernate.property column = "`secondaery_ion`" insert = "false" update =
	 *                     "false"
	 * 
	 * @return
	 */
    public Integer getSecondaeryIon() {
        return secondaeryIon;
    }

    public void setSecondaeryIon(Integer secondaeryIon) {
        this.secondaeryIon = secondaeryIon;
    }

    /**
	 * @swt.variable visible="true" name="ratio" searchable="true"
	 * @swt.modify canModify="true"
	 * @hibernate.property column = "`ratio`" insert = "false" update =
	 *                     "false"
	 * 
	 * @return
	 */
    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public int compareTo(Object arg0) {
        return getId().compareTo((((BinIonFilter) arg0).getId()));
    }
}
