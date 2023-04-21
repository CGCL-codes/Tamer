package rat.document.impl;

import java.io.File;

public class DocumentImplUtils {

    public static final String toName(File file) {
        String path = file.getPath();
        String normalisedPath = path.replace('\\', '/');
        return normalisedPath;
    }
}
