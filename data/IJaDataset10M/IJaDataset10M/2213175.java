package com.muddyhorse.cynch.admin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import com.muddyhorse.cynch.UpdateUtils;
import com.muddyhorse.cynch.manifest.RemoteFileInfo;
import com.muddyhorse.cynch.manifest.RemoteManifest;

public class AdminManifest extends RemoteManifest {

    private static URL FAKE_URL;

    private final File file;

    static {
        try {
            FAKE_URL = new URL("http://totally.fake.com/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            FAKE_URL = null;
        }
    }

    public AdminManifest(File maniFile) {
        super(FAKE_URL, "", false);
        this.file = maniFile;
        load();
    }

    @Override
    protected Map<String, String> getRawManifest() {
        String data = UpdateUtils.getStringFromFile(file.getPath());
        return UpdateUtils.stringToHashtable(data);
    }

    public void save(File saveFile) {
        Map<String, String> output = new TreeMap<String, String>();
        for (RemoteFileInfo rfi : getAllFileInfo().values()) {
            output.put(rfi.getFileID(), rfi.toString());
        }
        UpdateUtils.writeHashtable(saveFile.getPath(), output);
    }
}
