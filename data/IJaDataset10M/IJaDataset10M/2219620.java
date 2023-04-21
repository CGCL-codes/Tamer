package com.googlecode.sarasvati.editor.model;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import com.googlecode.sarasvati.editor.dialog.DialogFactory;
import com.googlecode.sarasvati.util.SvUtil;
import com.googlecode.sarasvati.visual.common.NodeDrawConfig;
import com.googlecode.sarasvati.visual.icon.NodeIconType;

public class EditorPreferences {

    private static final EditorPreferences INSTANCE = new EditorPreferences();

    public static EditorPreferences getInstance() {
        return INSTANCE;
    }

    private static final String LIBRARY_PATH_KEY = "libraryPath";

    private static final String RECURSE_LIBRARY_KEY = "recurseLibrary";

    private static final String DEFAULT_NODE_TYPE_KEY = "defaultNodeType";

    private static final String DEFAULT_SELF_ARCS_LABEL_KEY = "defaultSelfArcsLabel";

    private static final String NODE_TYPES_KEY = "nodeTypes";

    private static final String ATTRIBUTES_KEY = "attributes";

    private static final String NODE_TYPE_NAME = "nodeTypeName";

    private static final String NODE_TYPE_ALLOW_CUSTOM = "nodeTypeAllowCustom";

    private static final String NODE_TYPE_ICON = "nodeTypeIcon";

    private static final String NODE_TYPE_ICON_COLOR = "nodeTypeIconColor";

    private static final String NODE_TYPE_ATTR_NAME = "nodeTypeAttrName";

    private static final String NODE_TYPE_ATTR_DEFAULT_VALUE = "nodeTypeAttrDefaultValue";

    private static final String NODE_TYPE_ATTR_USE_CDATA = "nodeTypeAttrUseCDATA";

    protected String libraryPath;

    protected boolean recurseLibrary;

    protected String defaultSelfArcsLabel;

    protected Map<String, EditorNodeType> typesByName = new HashMap<String, EditorNodeType>();

    protected List<EditorNodeType> nodeTypes;

    protected EditorNodeType defaultNodeType;

    protected boolean firstRun = false;

    public void loadPreferences() {
        try {
            loadNodeTypes();
        } catch (BackingStoreException bse) {
            bse.printStackTrace();
            DialogFactory.showError("Failed to load preferences: " + bse.getMessage());
            setNodeTypes(getDefaultNodeTypes());
        } catch (RuntimeException re) {
            re.printStackTrace();
            DialogFactory.showError("Failed to load preferences: " + re.getMessage());
            setNodeTypes(getDefaultNodeTypes());
        }
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        libraryPath = prefs.get(LIBRARY_PATH_KEY, null);
        recurseLibrary = prefs.getBoolean(RECURSE_LIBRARY_KEY, false);
        defaultSelfArcsLabel = SvUtil.nullIfBlank(prefs.get(DEFAULT_SELF_ARCS_LABEL_KEY, null));
        defaultNodeType = getTypeByName(prefs.get(DEFAULT_NODE_TYPE_KEY, "node"));
        if (defaultNodeType == null && nodeTypes != null && !nodeTypes.isEmpty()) {
            defaultNodeType = nodeTypes.get(0);
        }
        reloadLibrary();
    }

    public void importPreferences(final File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            Preferences.importPreferences(is);
            DialogFactory.showInfo("Preferences imported from: " + file.getAbsolutePath());
        } catch (Exception e) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ie) {
                }
            }
            DialogFactory.showError("Failed to import preferences: " + e.getMessage());
        }
        loadPreferences();
    }

    public void exportPreferences(final File file) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            prefs.exportSubtree(os);
            DialogFactory.showInfo("Preferences exported to: " + file.getAbsolutePath());
        } catch (Exception e) {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ie) {
                }
            }
            DialogFactory.showError("Failed to export preferences: " + e.getMessage());
        }
    }

    public void reloadLibrary() {
        if (SvUtil.isBlankOrNull(libraryPath)) {
            Library.getInstance().emptyLibrary();
        } else {
            Library.getInstance().loadFromPath(libraryPath, recurseLibrary);
        }
    }

    public boolean isFirstRun() {
        return firstRun;
    }

    public EditorNodeType getDefaultNodeType() {
        return defaultNodeType;
    }

    public String getLibraryPath() {
        return libraryPath;
    }

    public boolean isRecurseLibrary() {
        return recurseLibrary;
    }

    public String getDefalutSelfArcsLabel() {
        return defaultSelfArcsLabel;
    }

    private void setNodeTypes(final List<EditorNodeType> nodeTypes) {
        this.nodeTypes = nodeTypes;
        typesByName.clear();
        for (EditorNodeType type : nodeTypes) {
            typesByName.put(type.getName(), type);
        }
    }

    public List<EditorNodeType> getNodeTypes() {
        return nodeTypes;
    }

    public EditorNodeType getTypeByName(final String name) {
        return typesByName.get(name);
    }

    public void loadNodeTypes() throws BackingStoreException {
        List<EditorNodeType> newNodeTypes = new LinkedList<EditorNodeType>();
        Preferences baseNode = Preferences.userNodeForPackage(getClass());
        Preferences typesNode = baseNode.node(NODE_TYPES_KEY);
        String[] childrenNames = typesNode.childrenNames();
        if (childrenNames.length == 0) {
            firstRun = true;
            importDefaultNodeTypes();
        } else {
            for (String nodeType : childrenNames) {
                newNodeTypes.add(loadNodeType(typesNode.node(nodeType)));
            }
            setNodeTypes(newNodeTypes);
        }
    }

    public void clearPreferences() throws BackingStoreException {
        Preferences baseNode = Preferences.userNodeForPackage(getClass());
        Preferences parent = baseNode.parent();
        baseNode.removeNode();
        parent.flush();
    }

    public EditorNodeType loadNodeType(final Preferences typeNode) throws BackingStoreException {
        String name = typeNode.get(NODE_TYPE_NAME, "<error loading type name>");
        boolean allowCustom = typeNode.getBoolean(NODE_TYPE_ALLOW_CUSTOM, false);
        String nodeIconTypeName = typeNode.get(NODE_TYPE_ICON, NodeIconType.Oval.toString());
        Color color = new Color(typeNode.getInt(NODE_TYPE_ICON_COLOR, NodeDrawConfig.NODE_BG_ACTIVE.getRGB()));
        List<EditorNodeTypeAttribute> attributes = new LinkedList<EditorNodeTypeAttribute>();
        Preferences attributesNode = typeNode.node(ATTRIBUTES_KEY);
        for (String child : attributesNode.childrenNames()) {
            Preferences attrNode = attributesNode.node(child);
            String attrName = attrNode.get(NODE_TYPE_ATTR_NAME, "<error loading attr>");
            String defaultValue = attrNode.get(NODE_TYPE_ATTR_DEFAULT_VALUE, "");
            boolean useCDATA = attrNode.getBoolean(NODE_TYPE_ATTR_USE_CDATA, false);
            attributes.add(new EditorNodeTypeAttribute(attrName, defaultValue, useCDATA));
        }
        NodeIconType nodeIconType = null;
        try {
            nodeIconType = NodeIconType.valueOf(nodeIconTypeName);
        } catch (IllegalArgumentException iae) {
            nodeIconType = NodeIconType.Oval;
        }
        return new EditorNodeType(name, allowCustom, nodeIconType, color, attributes);
    }

    public List<EditorNodeType> getDefaultNodeTypes() {
        List<EditorNodeType> newNodeTypes = new LinkedList<EditorNodeType>();
        List<EditorNodeTypeAttribute> emptyAttributes = Collections.emptyList();
        newNodeTypes.add(new EditorNodeType("node", true, NodeIconType.Oval, NodeDrawConfig.NODE_BG_ACTIVE, emptyAttributes));
        newNodeTypes.add(new EditorNodeType("wait", true, NodeIconType.Oval, NodeDrawConfig.NODE_BG_ACTIVE, emptyAttributes));
        List<EditorNodeTypeAttribute> attributes = new LinkedList<EditorNodeTypeAttribute>();
        attributes.add(new EditorNodeTypeAttribute("execute", "", true));
        attributes.add(new EditorNodeTypeAttribute("executeType", "js", false));
        attributes.add(new EditorNodeTypeAttribute("backtrack", "", true));
        attributes.add(new EditorNodeTypeAttribute("backtrackType", "js", false));
        newNodeTypes.add(new EditorNodeType("script", false, NodeIconType.Oval, NodeDrawConfig.NODE_BG_ACTIVE, attributes));
        attributes = new LinkedList<EditorNodeTypeAttribute>();
        attributes.add(new EditorNodeTypeAttribute("graphName", "", false));
        newNodeTypes.add(new EditorNodeType("nested", false, NodeIconType.Oval, NodeDrawConfig.NODE_BG_ACTIVE, attributes));
        return newNodeTypes;
    }

    public void importDefaultNodeTypes() throws BackingStoreException {
        saveNodeTypes(getDefaultNodeTypes());
    }

    public void saveNodeTypes(final List<EditorNodeType> newNodeTypes) throws BackingStoreException {
        Preferences baseNode = Preferences.userNodeForPackage(getClass());
        Preferences typesNode = baseNode.node(NODE_TYPES_KEY);
        typesNode.removeNode();
        typesNode = baseNode.node(NODE_TYPES_KEY);
        int count = 1;
        for (EditorNodeType nodeType : newNodeTypes) {
            persistNodeType(typesNode, "nodeType" + count, nodeType);
            count++;
        }
        setNodeTypes(newNodeTypes);
        baseNode.flush();
    }

    private void persistNodeType(final Preferences typesNode, final String nodeName, final EditorNodeType nodeType) {
        Preferences typeNode = typesNode.node(nodeName);
        typeNode.put(NODE_TYPE_NAME, nodeType.getName());
        typeNode.putBoolean(NODE_TYPE_ALLOW_CUSTOM, nodeType.isAllowNonSpecifiedAttributes());
        typeNode.put(NODE_TYPE_ICON, nodeType.getNodeIconType().name());
        typeNode.putInt(NODE_TYPE_ICON_COLOR, nodeType.getIconColor().getRGB());
        int count = 1;
        Preferences attributesNode = typeNode.node(ATTRIBUTES_KEY);
        for (EditorNodeTypeAttribute attr : nodeType.getAttributes()) {
            Preferences attrNode = attributesNode.node("attribute" + count);
            attrNode.put(NODE_TYPE_ATTR_NAME, attr.getName());
            attrNode.put(NODE_TYPE_ATTR_DEFAULT_VALUE, attr.getDefaultValue());
            attrNode.putBoolean(NODE_TYPE_ATTR_USE_CDATA, attr.isUseCDATA());
            count++;
        }
    }

    public void saveGeneralPreferences(final String newLibraryPath, final boolean newRecurseLibrary, final EditorNodeType newDefaultNodeType, final String newDefaultSelfArcsLabel) throws BackingStoreException {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        prefs.put(LIBRARY_PATH_KEY, newLibraryPath);
        prefs.putBoolean(RECURSE_LIBRARY_KEY, newRecurseLibrary);
        prefs.put(DEFAULT_NODE_TYPE_KEY, newDefaultNodeType.getName());
        prefs.put(DEFAULT_SELF_ARCS_LABEL_KEY, SvUtil.blankIfNull(newDefaultSelfArcsLabel));
        prefs.flush();
        this.libraryPath = newLibraryPath;
        this.recurseLibrary = newRecurseLibrary;
        this.defaultNodeType = newDefaultNodeType;
        this.defaultSelfArcsLabel = newDefaultSelfArcsLabel;
        reloadLibrary();
    }
}
