package onepoint.project.module;

import onepoint.xml.XContext;
import onepoint.xml.XNodeHandler;
import java.util.HashMap;

public class OpModuleFileHandler implements XNodeHandler {

    public static final String MODULE_FILE = "module-file";

    public static final String FILE_NAME = "file-name";

    public Object newNode(XContext context, String name, HashMap attributes) {
        OpModuleFile module_file = new OpModuleFile();
        Object value = attributes.get(FILE_NAME);
        if ((value != null) && (value instanceof String)) {
            module_file.setFileName((String) value);
        }
        return module_file;
    }

    public void addChildNode(XContext context, Object node, String child_name, Object child) {
    }

    public void addNodeContent(XContext context, Object node, String content) {
    }

    public void nodeFinished(XContext context, String name, Object node, Object parent) {
    }
}
