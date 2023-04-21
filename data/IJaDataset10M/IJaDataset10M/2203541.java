package edu.psu.citeseerx.ingestion;

import edu.psu.citeseerx.ingestion.ws.BpelClient;
import edu.psu.citeseerx.messaging.messages.NewRecordToIngestMsg;
import edu.psu.citeseerx.dao2.logic.CSXDAO;
import edu.psu.citeseerx.domain.Document;
import edu.psu.citeseerx.domain.DocumentFileInfo;
import edu.psu.citeseerx.utility.*;
import java.io.*;

/**
 * Runnable job implementation for submitting new records to the ingestion
 * pipeline and storing results upon success.
 * 
 * @author Isaac Councill
 *
 */
public class IngestWorker implements Runnable {

    private final NewRecordToIngestMsg msg;

    private final BpelClient bpelClient;

    private final CSXDAO csxdao;

    private final IngestionManager manager;

    /**
     * Sets up pointers to needed resources.
     * @param msg
     * @param bpelClient
     * @param csxdao
     * @param manager
     */
    public IngestWorker(NewRecordToIngestMsg msg, BpelClient bpelClient, CSXDAO csxdao, IngestionManager manager) {
        this.msg = msg;
        this.bpelClient = bpelClient;
        this.csxdao = csxdao;
        this.manager = manager;
    }

    private String sep = System.getProperty("file.separator");

    public void run() {
    }

    protected String copyToTmp(String crawlerPath, String tmpPath) throws IOException {
        File fromFile = new File(crawlerPath);
        tmpPath = tmpPath + sep + fromFile.getName();
        File toFile = new File(tmpPath);
        FileUtils.copy(fromFile, toFile);
        return FileUtils.makeRelative(tmpPath, manager.getRepositoryPath());
    }

    protected void copyToRepository(edu.psu.citeseerx.domain.Document doc) throws IOException {
        String repPath = manager.getRepositoryPath();
        String doi = doc.getDatum(Document.DOI_KEY, Document.UNENCODED);
        String dir = FileNamingUtils.getDirectoryFromDOI(doi);
        String repID = manager.getRepositoryID();
        String fullDestDir = repPath + sep + dir;
        DocumentFileInfo finfo = doc.getFileInfo();
        String srcPath = "fixme";
        String fullSrcPath = repPath + sep + srcPath;
        String[] extensions = { FileUtils.getExtension(fullSrcPath), ".txt", ".body", ".cite" };
        for (String ext : extensions) {
            String src = FileUtils.changeExtension(fullSrcPath, ext);
            String dest = fullDestDir + doi + ext;
            File srcFile = new File(src);
            File destFile = new File(dest);
            FileUtils.copy(srcFile, destFile);
            if (ext.equals(".txt")) {
            }
        }
        finfo.setDatum(DocumentFileInfo.REP_ID_KEY, repID);
    }
}
