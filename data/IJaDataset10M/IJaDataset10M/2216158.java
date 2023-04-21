package rat.document;

import java.util.ArrayList;
import java.util.List;

public class MockDocumentAnalyser implements IDocumentAnalyser {

    public List matches = new ArrayList();

    public void analyse(IDocument document) throws RatDocumentAnalysisException {
        matches.add(document);
    }
}
