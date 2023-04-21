package cn.jsprun.vo.logs;

public class FilesVo {

    private String filename;

    private String filebyte;

    private String modifyDate;

    private String status;

    public String getFilebyte() {
        return filebyte;
    }

    public void setFilebyte(String filebyte) {
        this.filebyte = filebyte;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
