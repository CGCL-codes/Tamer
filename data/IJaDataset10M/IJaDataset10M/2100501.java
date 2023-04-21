package org.t2framework.t2.contexts.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.t2framework.commons.util.CollectionsUtil;
import org.t2framework.t2.contexts.Multipart;
import org.t2framework.t2.contexts.UploadFile;

public class MultipartImpl implements Multipart {

    protected List<UploadFile> uploadList = CollectionsUtil.newArrayList();

    protected Map<String, UploadFile[]> uploadMap = CollectionsUtil.newHashMap();

    @Override
    public List<UploadFile> getUploadList() {
        return Collections.unmodifiableList(uploadList);
    }

    @Override
    public Map<String, UploadFile[]> getUploadMap() {
        return Collections.unmodifiableMap(uploadMap);
    }

    public void addUploadFile(String key, UploadFile uploadFile) {
        UploadFile[] files = uploadMap.get(key);
        if (files == null) {
            files = new UploadFile[] { uploadFile };
        } else {
            UploadFile[] newFiles = new UploadFile[files.length + 1];
            System.arraycopy(files, 0, newFiles, 0, files.length);
            newFiles[files.length] = uploadFile;
            files = newFiles;
        }
        uploadMap.put(key, files);
        uploadList.add(uploadFile);
    }
}
