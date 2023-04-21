package org.fmi.bioinformatics.exec.hopping;

import java.util.Properties;
import org.fmi.bioinformatics.format.Fasta;
import org.fmi.bioinformatics.system.XmlFactory;
import org.fmi.bioinformatics.system.config.ProcessConfiguration;
import org.fmi.bioinformatics.system.io.PropertiesManager;

/**
 * @author mhaimel  -  Diploma Thesis
 *
 */
public class BlastHopperConfig {

    private Integer hopper = null;

    private Integer hopperForProbes = null;

    private Integer minScore = null;

    private Integer maxScore = null;

    private Integer sequLimit = null;

    private Integer threshold = null;

    private Properties species = null;

    private Properties defaultProperties = null;

    private Fasta fasta = null;

    private ProcessConfiguration blastExecConf = null;

    private String blastTmpDir = null;

    public BlastHopperConfig() {
        init();
    }

    public BlastHopperConfig(BlastHopperConfig bhc) {
        super();
        this.hopper = bhc.getHopper();
        this.minScore = bhc.getMinScore();
        this.maxScore = bhc.getMaxScore();
        this.threshold = bhc.getThreshold();
        this.species = bhc.getSpecies();
        this.fasta = bhc.getFasta();
        this.blastExecConf = bhc.getBlastExecConf();
        this.hopperForProbes = bhc.getHopperForProbes();
    }

    private void init() {
        this.defaultProperties = PropertiesManager.getInstance().getDefaultProperties();
        String tmp = this.defaultProperties.getProperty("hopper.max");
        this.hopper = new Integer(null != tmp ? tmp : "3");
        tmp = this.defaultProperties.getProperty("hopper.limit.sequ.max");
        this.sequLimit = new Integer(null != tmp ? tmp : "200");
        tmp = this.defaultProperties.getProperty("score.range.min");
        this.minScore = new Integer(null != tmp ? tmp : "10");
        tmp = this.defaultProperties.getProperty("score.range.max");
        this.maxScore = new Integer(null != tmp ? tmp : "80");
        tmp = this.defaultProperties.getProperty("score.threshold");
        this.threshold = new Integer(null != tmp ? tmp : "5");
        tmp = this.defaultProperties.getProperty("blast.temp");
        this.blastTmpDir = null != tmp ? tmp.trim() : System.getProperty("java.io.tmpdir");
        tmp = this.defaultProperties.getProperty("hopper.max.probes");
        this.hopperForProbes = null != tmp ? new Integer(tmp.trim()) : this.hopper;
        this.species = PropertiesManager.getInstance().getSpeciesProperties();
        this.species = null == species ? new Properties() : species;
        this.fasta = new Fasta("", "", "");
        this.blastExecConf = (ProcessConfiguration) XmlFactory.getInstance().loadFromProperty(ProcessConfiguration.class, XmlFactory.BLAST_HOPPING);
    }

    public ProcessConfiguration getBlastExecConf() {
        return blastExecConf;
    }

    public void setBlastExecConf(ProcessConfiguration blastExecConf) {
        this.blastExecConf = blastExecConf;
    }

    public Fasta getFasta() {
        return fasta;
    }

    public void setFasta(Fasta fasta) {
        this.fasta = fasta;
    }

    public Integer getHopper() {
        return hopper;
    }

    public void setHopper(Integer hopper) {
        this.hopper = hopper;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public Integer getMinScore() {
        return minScore;
    }

    public void setMinScore(Integer minScore) {
        this.minScore = minScore;
    }

    public Properties getSpecies() {
        return species;
    }

    public void setSpecies(Properties species) {
        this.species = species;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public String getBlastTmpDir() {
        return blastTmpDir;
    }

    public void setBlastTmpDir(String blastTmpDir) {
        this.blastTmpDir = blastTmpDir;
    }

    public ProcessConfiguration blastExecCopy() {
        return new ProcessConfiguration(this.blastExecConf);
    }

    public Properties getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(Properties defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public Integer getHopperForProbes() {
        return hopperForProbes;
    }

    public void setHopperForProbes(Integer hopperForProbes) {
        this.hopperForProbes = hopperForProbes;
    }

    public Integer getSequLimit() {
        return sequLimit;
    }

    public void setSequLimit(Integer sequLimit) {
        this.sequLimit = sequLimit;
    }
}
