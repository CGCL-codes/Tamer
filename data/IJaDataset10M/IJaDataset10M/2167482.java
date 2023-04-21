package com.gorillalogic.flex.fileupload;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.gorillalogic.config.Module;

/**
 * Class for uploading model files from flex application
 * @author jonr
 *
 */
public class ModelUploadServlet extends GenericFileUploadServlet {

    private static String lastResult = "";

    public static String getLastResult() {
        return lastResult;
    }

    public static void clearLastResult() {
        lastResult = "";
    }

    protected void doFileItem(FileItem fileItem, String basepath) {
        synchronized (this) {
            lastResult = fileItem.getName();
        }
        System.out.println("=!!!!+!!!!!!!!!!++++++!+!++!++!++!");
        System.out.println(lastResult);
        System.out.println("=!!!!+!!!!!!!!!!++++++!+!++!++!++!");
    }
}
