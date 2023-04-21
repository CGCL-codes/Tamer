package net.sf.clairv.index.resource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.sf.clairv.index.builder.DocumentBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Identifies document type simply by inspecting the file extensions.
 *
 * @author qiuyin
 *
 */
public class ExtensionFileHandler implements FileHandler {

    private static final Log log = LogFactory.getLog(ExtensionFileHandler.class);

    private Map builders;

    protected Properties extensionMappings;

    public ExtensionFileHandler() {
        builders = new HashMap();
    }

    public void setExtensionMappings(Properties extensionMappings) {
        this.extensionMappings = extensionMappings;
    }

    public DocumentBuilder handle(File file) {
        if (extensionMappings == null) {
            throw new IllegalStateException("extensionMappings cannot be null");
        }
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        if (dot > 0) {
            String ext = name.substring(dot + 1);
            String clazz = extensionMappings.getProperty(ext);
            if (clazz != null) {
                DocumentBuilder db = (DocumentBuilder) builders.get(clazz);
                if (db == null) {
                    try {
                        db = (DocumentBuilder) Class.forName(clazz).newInstance();
                        return db;
                    } catch (Exception e) {
                        log.error("Cannot create instance of " + clazz, e);
                    }
                }
            }
        }
        return null;
    }
}
