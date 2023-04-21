package org.edits.processor;

import org.edits.FileTools;
import org.edits.etaf.Rule;

/**
 * @author Milen Kouylekov
 */
public class FileERTarget extends FileWriter<Rule> implements Target<Rule> {

    public FileERTarget(String filename_, boolean overwrite) throws Exception {
        FileTools.checkOutput(filename_, overwrite);
        init(filename_, "rules", "org.edits.etaf");
    }
}
