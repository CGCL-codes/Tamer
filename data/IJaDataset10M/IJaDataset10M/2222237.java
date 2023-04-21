package org.panopticode.java.ant;

import org.apache.tools.ant.types.FileSet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.panopticode.java.rdf.JavaRDFStore;
import org.panopticode.supplement.XMLFileSupplement;
import java.io.File;
import java.io.IOException;

public class XMLFileSupplementDefinition extends SupplementDefinitionBase {

    private static SAXReader saxReader = new SAXReader();

    private FileSet fileSet;

    private String file;

    public void setFile(String file) {
        this.file = file;
    }

    public void addFileSet(FileSet aFileSet) {
        fileSet = aFileSet;
    }

    private Document document(String fileName) throws DocumentException, IOException {
        return saxReader.read(fileSetBaseDir() + File.separator + fileName);
    }

    private String fileSetBaseDir() throws IOException {
        return fileSet.getDirectoryScanner(getProject()).getBasedir().getCanonicalPath();
    }

    private String[] fileSetFileNames() {
        if (file == null) {
            return fileSet.getDirectoryScanner(getProject()).getIncludedFiles();
        } else {
            return new String[] { file };
        }
    }

    private XMLFileSupplement supplement;

    void setSupplement() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        supplement = (XMLFileSupplement) Class.forName(getClassName()).newInstance();
    }

    public XMLFileSupplement getXmlFileSupplement() {
        return supplement;
    }

    @Override
    public void execute(JavaRDFStore store, File workingDir) throws Exception {
        setSupplement();
        if (file == null) {
            for (String fileName : fileSetFileNames()) {
                supplement.loadData(store, workingDir, document(fileName), getArgs());
            }
        } else {
            supplement.loadData(store, workingDir, saxReader.read(file), getArgs());
        }
    }
}
