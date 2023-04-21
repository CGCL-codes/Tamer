package net.sf.amemailchecker.gui.settings;

import net.sf.amemailchecker.app.extension.ExtensionInfo;
import net.sf.amemailchecker.app.extension.ExtensionSettings;
import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtensionsPanelModel {

    private List<ExtensionInfo> extensions;

    private Map<String, ExtensionInfo> extensionsMap;

    private DefaultListModel extensionsListModel;

    private Map<String, JPanel> extUiPanels;

    public ExtensionsPanelModel(List<ExtensionInfo> extensions) {
        this.extensions = extensions;
        init();
    }

    public void init() {
        extensionsMap = new HashMap<String, ExtensionInfo>();
        extUiPanels = new HashMap<String, JPanel>();
        extensionsListModel = new DefaultListModel();
        for (int i = 0; i < extensions.size(); i++) {
            ExtensionInfo extension = extensions.get(i);
            if (!extension.isSmooth()) {
                extensionsListModel.addElement(extension.getName());
                extensionsMap.put(extension.getName(), extension);
            }
        }
    }

    public DefaultListModel getExtensionsListModel() {
        return extensionsListModel;
    }

    public ExtensionInfo getExtensionInfo(String name) {
        return extensionsMap.get(name);
    }

    public JPanel getExtensionUI(String name, ExtensionModelChangeListener listener) {
        JPanel result = extUiPanels.get(name);
        if (result == null) {
            ExtensionSettings extSettings = extensionsMap.get(name).getExtension().settings();
            if (extSettings != null) {
                result = extSettings.settingsUI();
                result.addPropertyChangeListener(listener);
                extUiPanels.put(name, result);
            }
        }
        return result;
    }

    public int size() {
        return extensionsMap.size();
    }
}
