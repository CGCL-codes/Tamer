package com.dotmarketing.viewtools;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.context.ViewContext;
import org.apache.velocity.tools.view.tools.ViewTool;
import com.dotmarketing.beans.Host;
import com.dotmarketing.portlets.files.factories.FileFactory;
import com.dotmarketing.portlets.files.model.File;
import com.dotmarketing.portlets.folders.factories.FolderFactory;
import com.dotmarketing.portlets.folders.model.Folder;
import com.dotmarketing.util.Config;

public class VideoGalleryWebApi implements ViewTool {

    Context ctx;

    private HttpServletRequest request;

    public void init(Object obj) {
        ViewContext context = (ViewContext) obj;
        ctx = context.getVelocityContext();
        this.request = context.getRequest();
    }

    public List<File> getVideoGalleryByPath(String folderPath, Host host) {
        return getVideoGalleryByPath(folderPath, host.getInode());
    }

    public List<File> getVideoGalleryByPath(String folderPath, long hostId) {
        folderPath = (folderPath == null) ? "" : folderPath;
        folderPath = folderPath.trim().endsWith("/") ? folderPath.trim() : folderPath.trim() + "/";
        Folder folder = FolderFactory.getFolderByPath(folderPath, hostId);
        List<File> filesList = FileFactory.getFileChildrenByCondition(folder, "deleted=" + Config.getStringProperty("DB_FALSE_BOOLEAN"));
        List<File> videoList = new ArrayList<File>();
        for (File file : filesList) {
            String ext = file.getExtension();
            if (ext.toLowerCase().endsWith("flv")) videoList.add(file);
        }
        return videoList;
    }

    public List<File> getVideoImages(String videoURI, long hostId) {
        String imageURI = videoURI.substring(0, videoURI.length() - 4) + ".jpg";
        File img = FileFactory.getFileByURI(imageURI, hostId, true);
        List<File> videoList = new ArrayList<File>();
        videoList.add(img);
        return videoList;
    }
}
