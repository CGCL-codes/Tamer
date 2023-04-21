package gui.form;

import java.io.File;
import javax.swing.SwingWorker;
import reportingModule.ReportingModule;

/**
 *
 * @author Compaq
 */
public class ReportWorker extends SwingWorker<ReportingModule, String> {

    File projectFolder;

    String selectedDocumentPath;

    ReportData repdata;

    ReportingModule rp = new ReportingModule();

    boolean deletefolder;

    public ReportWorker(String aselectedDocumentPath, ReportData arepdata, File aprojectFolder, boolean deletefolder) {
        this.repdata = arepdata;
        this.selectedDocumentPath = aselectedDocumentPath;
        this.projectFolder = aprojectFolder;
        this.deletefolder = deletefolder;
    }

    @Override
    protected ReportingModule doInBackground() throws Exception {
        System.out.println("Generating Results Please Wait...............");
        rp.setDocument(selectedDocumentPath);
        rp.setTemp(repdata.getFolder());
        rp.setUrl(repdata.getUrlList());
        rp.setMap(repdata.getFileUrlMap());
        rp.setData(projectFolder, deletefolder);
        System.err.print("end");
        return rp;
    }
}
