package ces.coffice.webmail.ui.form;

import org.apache.struts.action.*;

public class NewFolderForm extends ActionForm {

    private int boxid;

    private String boxname;

    private String foldername;

    private String boxPath;

    public int getBoxid() {
        return boxid;
    }

    public void setBoxid(int boxid) {
        this.boxid = boxid;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public String getBoxname() {
        return boxname;
    }

    public void setBoxname(String boxname) {
        this.boxname = boxname;
    }

    public String getBoxPath() {
        return boxPath;
    }

    public void setBoxPath(String boxPath) {
        this.boxPath = boxPath;
    }
}
