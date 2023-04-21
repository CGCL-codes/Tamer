package edu.unibi.agbi.biodwh.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;
import edu.unibi.agbi.biodwh.config.IconLibrary;
import edu.unibi.agbi.biodwh.config.ParserLibrary;
import edu.unibi.agbi.biodwh.config.ResourceLibrary;
import edu.unibi.agbi.biodwh.gui.wizard.WizardProjectData;

public class MonitorPaserListCellRenderer extends JPanel implements ListCellRenderer {

    private static final long serialVersionUID = 1L;

    private JPanel text_panel = new JPanel();

    private JLabel label_parser = new JLabel();

    private JLabel label_database = new JLabel();

    private JLabel label_file = new JLabel();

    private JLabel label_icon = new JLabel();

    private JLabel label_configure = new JLabel();

    private final ImageIcon ICON = IconLibrary.DB_LOADER_ICON;

    private Border emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

    /**
     * Customised cell renderer for this project. This renderer contains an icon an two text label,
     * one label shows the loadername and the databasename and the ohter label shows the directory of the source
     * files.
     * @see javax.swing.ListCellRenderer
     */
    public MonitorPaserListCellRenderer() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        text_panel.setLayout(new MigLayout("fill"));
        text_panel.add(label_parser, "wrap");
        text_panel.add(label_file, " wrap");
        text_panel.add(label_configure);
        Font bold = new Font(this.getFont().getFamily(), Font.BOLD, this.getFont().getSize());
        label_parser.setFont(bold);
        Font italic = new Font(this.getFont().getFamily(), Font.ITALIC, 10);
        label_file.setFont(italic);
        label_file.setForeground(label_file.getForeground().brighter());
        label_configure.setFont(italic);
        label_configure.setForeground(label_configure.getForeground().brighter());
        label_configure.setHorizontalTextPosition(JLabel.LEFT);
        add(label_icon, BorderLayout.WEST);
        add(text_panel, BorderLayout.EAST);
        setAllOpaque(false);
    }

    /**
     * Overwrites the method from superclass and returnes the customised component.
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(JList, Object, int, boolean, boolean)
     * @return Component
     */
    public Component getListCellRendererComponent(JList list, Object obj, int index, boolean isSelected, boolean cellHasFocus) {
        String parserID = (String) obj;
        label_icon.setIcon(ICON);
        label_parser.setText(ParserLibrary.getParser(parserID).getParserName());
        label_file.setText(WizardProjectData.getInstance().getParser(parserID).getSourceDirectory().getAbsolutePath());
        label_configure.setText(ResourceLibrary.getSwingResource("swing.parser.list.renderer.monitor_config"));
        if (WizardProjectData.getInstance().getParser(parserID).hasMonitorConfiguration()) label_configure.setIcon(IconLibrary.SUCCESS_ICON_SMALL); else label_configure.setIcon(IconLibrary.FAIL_ICON_SMALL);
        String tooltip = new String("<html>" + "<b>" + ParserLibrary.getParser(parserID).getParserName() + "</b><br>" + "<b>" + ResourceLibrary.getSwingResource("swing.parser.combobox.renderer.version") + "</b> " + ParserLibrary.getParser(parserID).getVersion() + "<br>" + "<b>" + ResourceLibrary.getSwingResource("swing.parser.combobox.renderer.author") + "</b> " + ParserLibrary.getParser(parserID).getParserAuthor() + "<br>" + "</html>");
        this.setToolTipText(tooltip);
        selection(list, isSelected, cellHasFocus);
        text_panel.updateUI();
        this.updateUI();
        return this;
    }

    /**
     * Sets the border, background, foreground of the (un-)selected component in the list.
     * @param list
     * @param isSelected
     * @param cellHasFocus
     */
    private void selection(JList list, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setForeground(new Color(list.getSelectionForeground().getRGB()));
            setBackground(new Color(list.getSelectionBackground().getRGB()));
        } else {
            setForeground(new Color(list.getBackground().getRGB()));
            setBackground(new Color(list.getBackground().getRGB()));
        }
        if (cellHasFocus) setBorder(BorderFactory.createLineBorder(list.getBackground().darker(), 2)); else setBorder(emptyBorder);
    }

    /**
     * Sets all components of this class opaque.
     * @param isOpaque - true if components should be opaque
     */
    private void setAllOpaque(boolean isOpaque) {
        setOpaque(!isOpaque);
        text_panel.setOpaque(isOpaque);
        label_parser.setOpaque(isOpaque);
        label_database.setOpaque(isOpaque);
        label_file.setOpaque(isOpaque);
        label_configure.setOpaque(isOpaque);
    }
}
