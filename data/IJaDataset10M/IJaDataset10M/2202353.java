package onepoint.project.module;

import onepoint.xml.XContext;
import onepoint.xml.XNodeHandler;
import java.util.HashMap;

public class OpLanguageKitFileHandler implements XNodeHandler {

    public static final String LANGUAGE_KIT_FILE = "language-kit-file";

    public static final String FILE_NAME = "file-name";

    public Object newNode(XContext context, String name, HashMap attributes) {
        OpLanguageKitFile language_kit_file = new OpLanguageKitFile();
        Object value = attributes.get(FILE_NAME);
        if ((value != null) && (value instanceof String)) language_kit_file.setFileName((String) value);
        return language_kit_file;
    }

    public void addChildNode(XContext context, Object node, String child_name, Object child) {
    }

    public void addNodeContent(XContext context, Object node, String content) {
    }

    public void nodeFinished(XContext context, String name, Object node, Object parent) {
    }
}
