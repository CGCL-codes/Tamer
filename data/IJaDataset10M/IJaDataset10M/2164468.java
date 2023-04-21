package com.kg.oifilemanager.filemanager.util;

import java.util.HashMap;
import java.util.Map;
import android.webkit.MimeTypeMap;

public class MimeTypes {

    private Map<String, String> mMimeTypes;

    public MimeTypes() {
        mMimeTypes = new HashMap<String, String>();
    }

    public void put(String type, String extension) {
        extension = extension.toLowerCase();
        mMimeTypes.put(type, extension);
    }

    public String getMimeType(String filename) {
        String extension = FileUtils.getExtension(filename);
        if (extension.length() > 0) {
            String webkitMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));
            if (webkitMimeType != null) {
                return webkitMimeType;
            }
        }
        extension = extension.toLowerCase();
        String mimetype = mMimeTypes.get(extension);
        return mimetype;
    }
}
