package org.spantus.work.ui.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.spantus.logger.Logger;
import org.spantus.work.ui.dto.FeatureReader;
import org.spantus.work.ui.dto.SpantusWorkInfo;
import org.spantus.work.ui.dto.SpantusWorkProjectInfo;

public class FileWorkInfoManager extends AbstractWorkInfoManager {

    Logger log = Logger.getLogger(getClass());

    public static final String FILE_NAME = ".spntconfig";

    public static final String WORKING_DIR = "workingDir";

    public static final String CONFIG = "config";

    @SuppressWarnings("unchecked")
    public SpantusWorkInfo openWorkInfo() {
        SpantusWorkInfo info = null;
        Map<String, Object> attr = null;
        try {
            FileInputStream in = new FileInputStream(FILE_NAME);
            ObjectInputStream s = new ObjectInputStream(in);
            attr = (Map<String, Object>) s.readObject();
        } catch (IOException e) {
            log.debug("Problem with loading " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (attr == null) {
            log.debug("Config file not read. Create new one.");
            info = new SpantusWorkInfo();
            info.getProject().setWorkingDir(new File("."));
        } else {
            info = new SpantusWorkInfo();
            info.getProject().setWorkingDir((File) attr.get(WORKING_DIR));
            info.getProject().setFeatureReader(((FeatureReader) attr.get(CONFIG)));
            log.debug("Config file read correctly. info: " + info.getProject().getWorkingDir());
        }
        return info;
    }

    public void saveWorkInfo(SpantusWorkInfo info) {
        try {
            Map<String, Object> attr = new HashMap<String, Object>();
            FileOutputStream f = new FileOutputStream(FILE_NAME);
            ObjectOutputStream s = new ObjectOutputStream(f);
            attr.put(WORKING_DIR, info.getProject().getWorkingDir());
            attr.put(CONFIG, info.getProject().getFeatureReader());
            s.writeObject(attr);
            s.flush();
            log.debug("Config file is saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SpantusWorkProjectInfo openProject(String path) {
        throw new RuntimeException("Not impl");
    }

    public void saveProject(SpantusWorkProjectInfo project, String path) {
        throw new RuntimeException("Not impl");
    }
}
