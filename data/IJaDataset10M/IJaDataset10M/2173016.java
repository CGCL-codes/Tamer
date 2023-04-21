package com.lars_albrecht.foldergen.core.generator.worker;

import java.io.File;
import java.util.HashMap;
import com.lars_albrecht.foldergen.helper.Utilities;
import com.lars_albrecht.foldergen.plugin.classes.FolderGenPlugin;
import com.lars_albrecht.foldergen.plugin.interfaces.IFolderGenPlugin;

/**
 * @author lalbrecht
 * 
 */
public class DeleteWorker extends FolderGenPlugin {

    public DeleteWorker() {
        this.infoMap.put(IFolderGenPlugin.INFO_TITLE, "DeleteWorker");
        this.infoMap.put(IFolderGenPlugin.INFO_FILEMARKER, "");
        this.infoMap.put(IFolderGenPlugin.INFO_INFOMARKER, "");
    }

    @Override
    public HashMap<String, Object> doWork(final HashMap<String, Object> workerMap) {
        File rootFolder = (File) workerMap.get("rootFolder");
        String name = (String) workerMap.get("name");
        File f = new File(rootFolder + File.separator + name);
        if (f.exists()) {
            Utilities.delete(f);
        }
        return null;
    }

    @Override
    public HashMap<String, String> getAdditionlInfo(final HashMap<Integer, String> basicInfo) {
        return null;
    }

    @Override
    public String getItemTitle(final HashMap<Integer, String> basicInfo) {
        return null;
    }

    @Override
    public Integer getPluginType() {
        return IFolderGenPlugin.PLUGINTYPE_CONFEXTENSION_FILE;
    }

    @Override
    public String replaceContent(final String content) {
        return null;
    }
}
