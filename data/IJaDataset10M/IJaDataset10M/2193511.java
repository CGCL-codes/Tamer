package com.nullfish.app.jfd2.config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import com.l2fprod.common.swing.renderer.DefaultCellRenderer;
import com.nullfish.app.jfd2.JFD;
import com.nullfish.app.jfd2.resource.JFDResource;
import com.nullfish.lib.tablelayout.HtmlTablePanel;
import com.nullfish.lib.ui.OneKeyButton;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;

public class View2ConfigPanel extends JPanel implements ConfigPanel {

    private JLabel colorLabel = new JLabel(JFDResource.LABELS.getString("extension_color"));

    private ColorMapTableModel tableModel = new ColorMapTableModel();

    private JTable colorTable = new JTable(tableModel);

    private JScrollPane tableScroll = new JScrollPane(colorTable);

    private OneKeyButton addColorButton = new OneKeyButton(new AddColorAction(), KeyStroke.getKeyStroke(KeyEvent.VK_A, 0));

    private OneKeyButton removeColorButton = new OneKeyButton(new RemoveColorAction(), KeyStroke.getKeyStroke(KeyEvent.VK_A, 0));

    private VFile configDir;

    public static final String LAYOUT = "classpath:///resources/option_layout_apearance2_tab.xml";

    public View2ConfigPanel() {
        super(new BorderLayout());
        try {
            initGui();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGui() throws Exception {
        HtmlTablePanel panel = new HtmlTablePanel(VFS.getInstance().getFile(LAYOUT).getInputStream());
        panel.layoutByMemberName(this);
        this.add(panel);
        colorTable.getColumnModel().getColumn(1).setCellRenderer(new ColorRenderer(true));
        colorTable.getColumnModel().getColumn(1).setCellEditor(new ColorCellEditor());
        colorTable.setRowSelectionAllowed(true);
        colorTable.setColumnSelectionAllowed(false);
        colorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        colorTable.getTableHeader().setPreferredSize(new Dimension(0, 0));
    }

    public void apply() throws Exception {
        Configuration commonConfig = Configuration.getInstance(configDir.getChild(JFD.COMMON_PARAM_FILE));
        commonConfig.setParam("color_map", tableModel.getColorMap());
    }

    public String getTitle() {
        return JFDResource.LABELS.getString("appearance") + "2";
    }

    public void loadPreference(VFile configDir) throws Exception {
        this.configDir = configDir;
        Configuration commonConfig = Configuration.getInstance(configDir.getChild(JFD.COMMON_PARAM_FILE));
        tableModel.setColorMap((Map) commonConfig.getParam("color_map", new HashMap()));
    }

    private class AddColorAction extends AbstractAction {

        public AddColorAction() {
            super("+");
        }

        public void actionPerformed(ActionEvent e) {
            String answer = JOptionPane.showInputDialog(JFDResource.MESSAGES.getString("input_extension"));
            if (answer == null || answer.length() == 0) {
                return;
            }
            answer = answer.toLowerCase();
            Color color = JColorChooser.showDialog(View2ConfigPanel.this, "", Color.WHITE);
            if (color == null) {
                return;
            }
            tableModel.add(answer, color);
        }
    }

    private class RemoveColorAction extends AbstractAction {

        public RemoveColorAction() {
            super("-");
        }

        public void actionPerformed(ActionEvent e) {
            int index = colorTable.getSelectedRow();
            if (index < 0) {
                return;
            }
            tableModel.remove(index);
        }
    }
}
