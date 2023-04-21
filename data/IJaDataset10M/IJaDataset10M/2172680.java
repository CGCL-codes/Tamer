package toolkit.levelEditor.gui;

import game.managers.BlipManager;
import game.resourceObjects.BlipResource;
import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.metal.MetalIconFactory;
import toolkit.levelEditor.tools.BlipTool;
import toolkit.levelEditor.tools.PolyTool;
import toolkit.shared.Resource;

public class ToolsPanel extends JPanel {

    DefaultListModel listModel = new DefaultListModel();

    private final List<ActionListener> listeners = new ArrayList<ActionListener>();

    public ToolsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JButton(new PolyTool("Collision")));
        final JPanel mainPanel = new JPanel();
        {
            final BoxLayout box = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
            mainPanel.setLayout(box);
            for (final String handle : Resource.getBlipHandles()) {
                final BlipResource blipSrc = BlipManager.getInst().getBlipResource(handle);
                final JButton bt = new JButton(new BlipTool(blipSrc));
                bt.setMargin(new Insets(0, 0, 0, 0));
                final Image im = BlipManager.getInst().getIcon(blipSrc).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                bt.setIcon(new ImageIcon(im));
                mainPanel.add(bt);
            }
        }
        add(new JScrollPane(mainPanel));
    }

    public void addActionListener(final ActionListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }
}

class IconListRenderer extends DefaultListCellRenderer {

    private Map<Object, Icon> icons = null;

    public IconListRenderer(final Map<Object, Icon> icons) {
        this.icons = icons;
    }

    @Override
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        final JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        final Icon icon = icons.get(value);
        label.setIcon(icon);
        return label;
    }

    public static void main(final String[] args) {
        final Map<Object, Icon> icons = new HashMap<Object, Icon>();
        icons.put("details", MetalIconFactory.getFileChooserDetailViewIcon());
        icons.put("folder", MetalIconFactory.getTreeFolderIcon());
        icons.put("computer", MetalIconFactory.getTreeComputerIcon());
        final JFrame frame = new JFrame("Icon List");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final JList list = new JList(new Object[] { "details", "computer", "folder", "computer" });
        list.setCellRenderer(new IconListRenderer(icons));
        frame.add(list);
        frame.pack();
        frame.setVisible(true);
    }
}
