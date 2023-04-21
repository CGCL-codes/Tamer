package eu.fbk.hlt.edits.processor;

import java.util.ArrayList;
import java.util.List;
import eu.fbk.hlt.common.EDITSException;
import eu.fbk.hlt.common.FileTools;
import eu.fbk.hlt.edits.etaf.EntailmentPair;

/**
 * @author Milen Kouylekov
 */
public class FileEPSource extends FileReader<EntailmentPair> implements Source<EntailmentPair> {

    private List<String> files;

    public FileEPSource(String filename_) throws EDITSException {
        FileTools.checkInput(filename_);
        files = new ArrayList<String>();
        files.add(filename_);
        init(filename_, "entailment-corpus", "eu.fbk.hlt.edits.etaf");
    }

    @Override
    public List<String> getFiles() {
        return files;
    }
}
