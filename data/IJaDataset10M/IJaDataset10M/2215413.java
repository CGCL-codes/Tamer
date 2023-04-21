package si.mk.k3.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class K3Panel {

    private String m_name;

    private int m_layout;

    public static final String BOX_X_LAYOUT = "boxX";

    public static final String BOX_Y_LAYOUT = "boxY";

    private List<K3GUIEditor> m_editors;

    public K3Panel(String name, String layout) {
        if (m_name == null) {
            throw new IllegalArgumentException("Name must not be 'null' for GUI panel - it has to be specified!");
        }
        m_name = name;
        if (layout == null) {
            m_layout = BoxLayout.Y_AXIS;
        } else {
            if (layout.equals(BOX_X_LAYOUT)) {
                m_layout = BoxLayout.X_AXIS;
            } else if (layout.equals(BOX_Y_LAYOUT)) {
                m_layout = BoxLayout.Y_AXIS;
            } else {
                throw new IllegalArgumentException("Unknown layout specified for GUI panel: '" + layout + "'");
            }
        }
        m_editors = new ArrayList<K3GUIEditor>();
    }

    public void add(K3GUIEditor editor) {
        m_editors.add(editor);
    }

    public JPanel createVisible() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, m_layout));
        for (K3GUIEditor editor : m_editors) {
            panel.add(editor.createVisible());
        }
        return panel;
    }

    public List<String> getParameterNames() {
        List<String> names = new ArrayList<String>();
        for (K3GUIEditor editor : m_editors) {
            names.add(editor.getParameterName());
        }
        return names;
    }

    public Map<String, K3GUIEditor> getEditorsMap() {
        Map<String, K3GUIEditor> editors = new TreeMap<String, K3GUIEditor>();
        for (K3GUIEditor editor : m_editors) {
            editors.put(editor.getParameterName(), editor);
        }
        return editors;
    }
}
