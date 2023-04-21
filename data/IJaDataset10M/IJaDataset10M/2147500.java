package ostf.test.client.ftp;

import ostf.test.client.ActionResult;
import ostf.test.data.array.SimpleDataArray;

public class FtpActionResult extends ActionResult {

    private String[] subFolders = new String[0];

    private String[] files = new String[0];

    public String[] getFiles() {
        return files;
    }

    public SimpleDataArray getFileArray() {
        return new SimpleDataArray(files);
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public String[] getSubFolders() {
        return subFolders;
    }

    public SimpleDataArray getSubFolderArray() {
        return new SimpleDataArray(subFolders);
    }

    public void setSubFolders(String[] subFolders) {
        this.subFolders = subFolders;
    }
}
