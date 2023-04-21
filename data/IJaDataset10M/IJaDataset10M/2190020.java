package org.omnidoc;

import java.io.File;
import org.omnidoc.formula.FormulaTransformer;
import org.omnidoc.hightlight.SourceCodeColouringTransformer;
import org.omnidoc.process.DocumentProcessor;
import org.omnidoc.process.DocumentReaderSource;
import org.omnidoc.process.FolderTarget;
import org.omnidoc.uml.YmlUmlTransformer;
import org.omnidoc.xml.XSLTransfomer;

public class ArticleTransformer {

    private File configFile;

    private File dataFile;

    public ArticleTransformer() {
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    public File transform() {
        String fileName = dataFile.getName().substring(0, dataFile.getName().lastIndexOf("."));
        File result = new File(dataFile.getParentFile().getAbsolutePath() + File.separator + fileName + ".html");
        DocumentProcessor.createProcessor().add(new FormulaTransformer()).add(new SourceCodeColouringTransformer()).add(new YmlUmlTransformer()).add(XSLTransfomer.forFile(configFile)).process(DocumentReaderSource.forReader(new XMLFileDocumentReader(dataFile)), FolderTarget.forForder(dataFile.getParentFile()));
        return result;
    }
}
