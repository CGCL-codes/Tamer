package pl.kwiecienm.jcomet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import pl.kwiecienm.jcomet.model.AnalysisResult;
import pl.kwiecienm.jcomet.model.TextFile;

/**
 * @author kwiecienm
 */
public final class FileAnalyzer {

    /** */
    private List<LineAnalyzer> _analyzers;

    /** */
    public FileAnalyzer() {
        this._analyzers = new ArrayList<LineAnalyzer>();
    }

    /**
     * @param inTextFile
     * @return
     */
    public AnalysisResult analyse(File inFile) {
        AnalysisResult analysisResult = new AnalysisResult();
        TextFile textFile = new TextFile(inFile);
        getFileDescriptionFromFile(analysisResult, inFile);
        for (LineAnalyzer analyzer : this._analyzers) {
            analyzer.preAnalyse();
            for (String line : textFile.getData()) {
                analyzer.analyse(line);
            }
            analyzer.postAnalyse();
            analyzer.mergeAnalysisResult(analysisResult);
        }
        return analysisResult;
    }

    /**
     * @param index
     * @param inAnalyzer
     */
    public void addAnalyzer(LineAnalyzer inAnalyzer) {
        this._analyzers.add(inAnalyzer);
    }

    /**
     * @param inAnaRes
     * @param inFile
     */
    private void getFileDescriptionFromFile(AnalysisResult inAnaRes, File inFile) {
        inAnaRes.put(TextFile.FILE_NAME, inFile.getName());
        try {
            inAnaRes.put(TextFile.FILE_FULL_PATH, inFile.getCanonicalPath());
        } catch (IOException e) {
        }
    }
}
