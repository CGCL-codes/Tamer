package it.southdown.avana.analysis;

import it.southdown.avana.alignscan.Region;

public interface Analysis {

    public void updateRegions(Region[] regions);

    public String getSummary();

    public AnalysisReport getAnalysisReport();

    public AnalysisReport getVariantsReport();
}
