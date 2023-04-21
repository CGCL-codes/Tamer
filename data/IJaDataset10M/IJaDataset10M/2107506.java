package jk.spider.mod.plugin.images;

import java.io.File;
import jk.spider.api.event.SpiderEvent;
import jk.spider.api.event.resource.ResourceFetchedEvent;
import jk.spider.core.SpiderController;
import jk.spider.core.event.CoreEvent;
import jk.spider.mod.plugin.Plugin;
import jk.spider.model.Folder;
import jk.spider.model.ImgInfo;
import jk.spider.model.Resource;
import org.apache.log4j.Logger;

public class ImagesPlugin implements Plugin {

    protected static final Logger log = Logger.getLogger(ImagesPlugin.class);

    public String getDescription() {
        return "A JK_Spider plugin that allows the disk-file of web images";
    }

    public String getName() {
        return "Images diskWriter JK_Spider plugin";
    }

    public String getVersion() {
        return "v1.0";
    }

    public void initialize() {
        log.info(PLUGIN_OUT + this.getName());
        log.info(PLUGIN_OUT + this.getDescription());
        log.info(PLUGIN_OUT + this.getVersion());
    }

    public void notifyEvent(SpiderController controller, SpiderEvent event) {
        if (event instanceof ResourceFetchedEvent) {
            Resource tRes = new Resource();
            tRes = event.getResource();
            if (!isWriteFile(tRes)) return;
            String filePath = "";
            filePath = controller.getOutputFile() + "images" + File.separator + tRes.getPaId() + File.separator;
            String suffix = tRes.getUrl().substring(tRes.getUrl().lastIndexOf("."), tRes.getUrl().length());
            String fileName = tRes.getRId() + suffix;
            File outputFile = new File(filePath, fileName);
            ensureFolders(filePath);
            writeFile(outputFile, tRes.getBytes());
            Folder folder = new Folder();
            folder.setRid(tRes.getRId());
            folder.setFName(outputFile.getPath());
            folder.setFType(tRes.getType());
            controller.getStorage().getFolderDAO().createFolder(folder);
            ImgInfo imgInfo = tRes.getImgInfo(controller.getStorage(), tRes.getRId());
            imgInfo.setImgPath(outputFile.getPath());
            log.info("[IMAGE] | RID [" + tRes.getRId() + "] | TYPE [" + tRes.getUrl() + "] | FILE [" + outputFile.getPath() + "]");
        }
    }

    protected boolean isWriteFile(Resource res) {
        return res.getType().equals(CoreEvent.COREEVENT_IMGDETAIL);
    }

    protected void writeFile(File outputFile, byte[] bytes) {
    }

    protected void ensureFolders(String path) {
        File folderPath = new File(path);
        if (!folderPath.exists()) folderPath.mkdirs();
    }

    public void shutdown() {
    }
}
