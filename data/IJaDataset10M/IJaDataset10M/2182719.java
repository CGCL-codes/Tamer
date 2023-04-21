package org.velogen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.velogen.core.VeloGenException;

/**
 *
 * @author ala
 */
public class VeloUtils {

    private static boolean inited = false;

    private static String TEMPLATES = ".";

    /** Creates a new instance of VeloUtils */
    private VeloUtils() {
    }

    public static Template getTemplate(String name) {
        try {
            return _getTemplate(name);
        } catch (Exception e) {
            throw new VeloGenException(e);
        }
    }

    private static Template _getTemplate(String name) throws Exception {
        Properties p = new Properties();
        p.setProperty(VelocityEngine.RESOURCE_LOADER, "file");
        p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, TEMPLATES);
        p.setProperty("runtime.log.error.stacktrace", "true");
        FileInputStream fis = new FileInputStream(new File(TEMPLATES + "/" + name));
        System.out.println("FIS:" + fis);
        Velocity.init(p);
        return Velocity.getTemplate(name);
    }

    public static String getTemplatesPackage() {
        return TEMPLATES;
    }

    public static void setTemplatesPackage(String p) {
        TEMPLATES = p;
    }
}
