package apollo.dataadapter.analysis;

import java.io.InputStream;
import apollo.datamodel.CurationSet;
import apollo.analysis.filter.AnalysisInput;

public interface AnalysisParserI {

    public String load(CurationSet curation, boolean new_curation, InputStream data_stream, AnalysisInput input);
}
