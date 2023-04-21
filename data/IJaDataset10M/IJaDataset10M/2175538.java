package ispyb.client.help;

import java.io.Serializable;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
* @struts.form name="sendReportForm"
*/
public class SendReportForm extends ActionForm implements Serializable {

    static final long serialVersionUID = 0;

    private FormFile uploadedFile;

    private String fileFullPath = null;

    public FormFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(FormFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getFileFullPath() {
        return fileFullPath;
    }

    public void setFileFullPath(String fileFullPath) {
        this.fileFullPath = fileFullPath;
    }
}
