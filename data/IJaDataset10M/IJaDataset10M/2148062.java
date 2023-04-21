package ca.ubc.icapture.genapha.forms;

import icapture.SQLMgr;
import icapture.beans.DB.GenotypingRun;
import java.util.ArrayList;

/**
 *
 * @author btripp
 */
public class ExportRawGenotypesForm extends AnalysisFilesForm {

    private ArrayList<GenotypingRun> genotypingRunList;

    private String[] selectedGenotypingRun;

    private String chromosomes;

    private String snps;

    private String format;

    public ExportRawGenotypesForm() {
        setGenotypingRunList(SQLMgr.getGenotypingRuns());
        setPhenotypes(SQLMgr.getPhenotypes());
    }

    /**
     * @return the genotypingRunList
     */
    public ArrayList<GenotypingRun> getGenotypingRunList() {
        return genotypingRunList;
    }

    /**
     * @param genotypingRunList the genotypingRunList to set
     */
    public void setGenotypingRunList(ArrayList<GenotypingRun> genotypingRunList) {
        this.genotypingRunList = genotypingRunList;
    }

    /**
     * @return the selectedGenotypingRun
     */
    public String[] getSelectedGenotypingRun() {
        return selectedGenotypingRun;
    }

    /**
     * @param selectedGenotypingRun the selectedGenotypingRun to set
     */
    public void setSelectedGenotypingRun(String[] selectedGenotypingRun) {
        this.selectedGenotypingRun = selectedGenotypingRun;
    }

    /**
     * @return the chromosomes
     */
    public String getChromosomes() {
        return chromosomes;
    }

    /**
     * @param chromosomes the chromosomes to set
     */
    public void setChromosomes(String chromosomes) {
        this.chromosomes = chromosomes;
    }

    /**
     * @return the snps
     */
    public String getSnps() {
        return snps;
    }

    /**
     * @param snps the snps to set
     */
    public void setSnps(String snps) {
        this.snps = snps;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }
}
