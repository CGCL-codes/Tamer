package vpfarm.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class RenderResult implements Serializable {

    public RenderResult(int jobid, String path, int num) throws IOException {
        this.jobID = jobid;
        this.number = num;
        File file = new File(path);
        int l = (int) file.length();
        this.imageFileByteData = new byte[l];
        FileInputStream f_in = new FileInputStream(file);
        f_in.read(this.imageFileByteData);
        f_in.close();
    }

    int jobID = 0;

    int number = 0;

    byte[] imageFileByteData = null;

    public void saveToFile(String path) throws IOException {
        FileOutputStream out = new FileOutputStream(path);
        out.write(imageFileByteData);
        out.close();
    }

    public int getFrameNumber() {
        return this.number;
    }

    public int getJobID() {
        return jobID;
    }
}
