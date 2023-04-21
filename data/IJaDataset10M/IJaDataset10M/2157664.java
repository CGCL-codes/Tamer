package onepoint.project.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import onepoint.log.XLog;
import onepoint.log.XLogFactory;
import onepoint.xml.XContext;
import onepoint.xml.XNodeHandler;

public class OpModuleHandler implements XNodeHandler {

    public static final String MODULE = "module";

    private static final XLog logger = XLogFactory.getLogger(OpModuleHandler.class);

    private static final String CLASS = "class";

    private static final String NAME = "name";

    private static final String VERSION = "version";

    private static final String CAPTION = "caption";

    private static final String EXTENDS = "extends";

    private static final String DEPENDS = "depends";

    public Object newNode(XContext context, String name, HashMap attributes) {
        OpModule module = null;
        Object value = attributes.get(CLASS);
        if ((value != null) && (value instanceof String)) {
            try {
                module = (OpModule) (Class.forName((String) value).newInstance());
            } catch (Exception e) {
                logger.error("OpModuleHandler.newNode", e);
            }
        }
        value = attributes.get(NAME);
        if ((value != null) && (value instanceof String)) {
            module.setName((String) value);
        }
        value = attributes.get(VERSION);
        if ((value != null) && (value instanceof String)) {
            module.setVersion((String) value);
        }
        value = attributes.get(CAPTION);
        if ((value != null) && (value instanceof String)) {
            module.setCaption((String) value);
        }
        value = attributes.get(EXTENDS);
        if ((value != null) && (value instanceof String)) {
            module.setExtendedModule((String) value);
        }
        value = attributes.get(DEPENDS);
        if ((value != null) && (value instanceof String)) {
            StringTokenizer tokenizer = new StringTokenizer((String) value, ",");
            Set<String> dependencies = new HashSet<String>();
            while (tokenizer.hasMoreTokens()) {
                dependencies.add(tokenizer.nextToken().trim());
            }
            module.setDependencies(dependencies);
        }
        return module;
    }

    public void addChildNode(XContext context, Object node, String child_name, Object child) {
        if (child_name == OpPrototypeFilesHandler.PROTOTYPE_FILES) {
            ((OpModule) node).setPrototypeFiles((ArrayList) child);
        } else if (child_name == OpServiceFilesHandler.SERVICE_FILES) {
            ((OpModule) node).setServiceFiles((ArrayList) child);
        } else if (child_name == OpLanguageKitPathsHandler.LANGUAGE_KIT_PATHS) {
            ((OpModule) node).setLanguageKitPaths((ArrayList) child);
        } else if (child_name == OpToolsHandler.TOOLS) {
            ((OpModule) node).setTools((ArrayList) child);
        } else if (child_name == OpToolGroupsHandler.TOOL_GROUPS) {
            ((OpModule) node).setToolGroups((ArrayList) child);
        }
    }

    public void addNodeContent(XContext context, Object node, String content) {
    }

    public void nodeFinished(XContext context, String name, Object node, Object parent) {
    }
}
