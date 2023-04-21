package vehikel.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Thomas Jourdan
 *
 */
public class TemplateReader {

    public InputStream getTemplate(String templateCathegory, String targetName, String templateTail, String flavor) {
        String absolutePath = VehikelPreference.getVehicleSpaceFolder() + templateCathegory + "/" + targetName;
        if (flavor != null && flavor.length() > 0) {
            absolutePath += flavor;
        }
        InputStream in = null;
        try {
            in = new FileInputStream(absolutePath + "/" + templateTail);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return in;
    }

    public InputStream getTemplate(String templateCathegory, String targetName, String templateTail) {
        return getTemplate(templateCathegory, targetName, templateTail, null);
    }
}
