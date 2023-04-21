package com.ryanm.config.serial.imp;

import java.io.File;
import com.ryanm.config.serial.ConfiguratorCodec;
import com.ryanm.util.io.FileUtils;

/**
 * @author s9902505
 */
public class FileCodec implements ConfiguratorCodec<File> {

    private static final File CURRENT_DIR = new File(System.getProperty("user.dir"));

    @Override
    public String encode(File value) {
        if (value == null) {
            return "";
        }
        if (value.isAbsolute()) {
            return value.getAbsolutePath();
        } else {
            return FileUtils.getRelativePath(CURRENT_DIR, value).getPath();
        }
    }

    @Override
    public File decode(String encoded, Class type) {
        assert type.equals(getType());
        if (encoded.length() == 0) {
            return null;
        }
        return new File(encoded);
    }

    @Override
    public Class getType() {
        return File.class;
    }

    @Override
    public String getDescription() {
        return "A file path, starting with \"/\" for an absolute path";
    }
}
