package com.mapbased.sfw.store;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import com.google.common.io.Files;
import com.mapbased.sfw.util.logging.ESLogger;
import com.mapbased.sfw.util.logging.Loggers;

public class FSSiteStore extends SiteStore {

    private static ESLogger log = Loggers.getLogger(FSSiteStore.class);

    public static final String ROOT = "sitestore";

    private final String root;

    public FSSiteStore(String siteId) {
        super(siteId);
        root = ROOT + "/" + this.siteId + "/";
    }

    @Override
    public void delete(String path, PathDataType type) {
        new File(this.mkFilePath(path, type)).delete();
    }

    private String mkFilePath(String path, PathDataType type) {
        return root + type.name() + path;
    }

    @Override
    public List<PathData> getAllPathData() {
        List<PathData> l = new java.util.ArrayList<PathData>();
        for (PathDataType p : PathDataType.values()) {
            File f = new File(root + p.name());
            if (!f.exists()) {
                f.mkdirs();
            }
            this.getChildren(f, l, "/", p);
        }
        return l;
    }

    private void getChildren(File r, List<PathData> list, String path, PathDataType type) {
        File[] ff = r.listFiles();
        for (File f : ff) {
            if (f.isDirectory()) {
                this.getChildren(f, list, path + f.getName() + "/", type);
            } else {
                String key = path + f.getName();
                PathData kv = new PathData();
                kv.type = type;
                kv.path = key;
                byte[] v = readFile(f);
                kv.lastModified = f.lastModified();
                if (v == null) {
                    continue;
                }
                if (v.length != 0) {
                    kv.value = v;
                    list.add(kv);
                }
            }
        }
    }

    private byte[] readFile(File f) {
        try {
            return Files.toByteArray(f);
        } catch (IOException e) {
            log.error("Error while read file:{}", e, f);
        }
        return null;
    }

    @Override
    public void put(PathData kv) {
        File f = new File(this.mkFilePath(kv.path, kv.type));
        f.getParentFile().mkdirs();
        try {
            Files.write(kv.value, f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }

    @Override
    public boolean exist() {
        return new File(ROOT + "/" + siteId).exists();
    }

    @Override
    public byte[] getData(String path, PathDataType type) {
        return readFile(new File(this.mkFilePath(path, type)));
    }
}
