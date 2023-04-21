package photospace.web.spring;

import org.springframework.web.multipart.*;

public class UploadCommand implements java.io.Serializable {

    private String path;

    private String newFolderName;

    private MultipartFile file0;

    private MultipartFile file1;

    private MultipartFile file2;

    private MultipartFile file3;

    private MultipartFile file4;

    private MultipartFile file5;

    public MultipartFile[] getFiles() {
        return new MultipartFile[] { file0, file1, file2, file3, file4, file5 };
    }

    public MultipartFile getFile1() {
        return file1;
    }

    public void setFile1(MultipartFile file1) {
        this.file1 = file1;
    }

    public MultipartFile getFile0() {
        return file0;
    }

    public void setFile0(MultipartFile file0) {
        this.file0 = file0;
    }

    public MultipartFile getFile2() {
        return file2;
    }

    public void setFile2(MultipartFile file2) {
        this.file2 = file2;
    }

    public MultipartFile getFile3() {
        return file3;
    }

    public void setFile3(MultipartFile file3) {
        this.file3 = file3;
    }

    public MultipartFile getFile4() {
        return file4;
    }

    public void setFile4(MultipartFile file4) {
        this.file4 = file4;
    }

    public MultipartFile getFile5() {
        return file5;
    }

    public void setFile5(MultipartFile file5) {
        this.file5 = file5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNewFolderName() {
        return newFolderName;
    }

    public void setNewFolderName(String newFolderName) {
        this.newFolderName = newFolderName;
    }
}
