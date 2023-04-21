package edu.ucdavis.genomics.metabolomics.binbase.cluster.profiler.report;

import java.util.Date;
import java.util.List;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.profiler.taks.ProfilingObject;

public interface AnalyzeProfiling {

    List<ProfilingObject> getByDateList(Date begin, Date end);
}
