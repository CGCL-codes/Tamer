package org.jcvi.assembly.analysis.issue;

import org.jcvi.assembly.analysis.DefaultAnalysisIssue;
import org.jcvi.assembly.contig.QualityClassRegion;

public class QualityClassContigMapIssue extends DefaultAnalysisIssue {

    public QualityClassContigMapIssue(QualityClassRegion qualityClassRegion) {
        super(Severity.MEDIUM, qualityClassRegion.toString());
    }
}
