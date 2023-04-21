package g1105.android;

import g1105.ps.constant.Constant;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

public class UploadPictureContentFromAndroidAction {

    private String picId;

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getFile(HttpServletRequest request, OutputStream outputStream) throws IOException {
        InputStream inputStream = request.getInputStream();
        org.apache.commons.io.IOUtils.copy(inputStream, outputStream);
        return null;
    }

    public String execute() {
        try {
            HttpServletRequest request = ServletActionContext.getRequest();
            File file = new File(Constant.flickPicDir + picId);
            FileOutputStream outputStream = new FileOutputStream(file);
            getFile(request, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
