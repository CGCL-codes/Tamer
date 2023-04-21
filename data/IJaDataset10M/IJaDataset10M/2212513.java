package com.nullfish.app.jfd2.config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import org.jdom.JDOMException;
import com.l2fprod.common.swing.JFontChooser;
import com.nullfish.app.jfd2.JFD;
import com.nullfish.app.jfd2.dialog.components.DialogComboBox;
import com.nullfish.app.jfd2.resource.JFDResource;
import com.nullfish.lib.tablelayout.BgImagePainter;
import com.nullfish.lib.tablelayout.HtmlTablePanel;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;

public class ViewConfigPanel extends JPanel implements ConfigPanel {

    private VFile configDir;

    private ConfigFrame frame;

    public static final String APPEARANCE_LAYOUT = "classpath:///resources/option_layout_apearance_tab.xml";

    private HtmlTablePanel appearancePanel;

    private JLabel fontLabel = new JLabel(JFDResource.LABELS.getString("font"));

    private JLabel fontSampleLabel = new JLabel(JFDResource.LABELS.getString("font_sample"));

    private JButton fontButton = new JButton(JFDResource.LABELS.getString("modify"));

    private JLabel dialogFontLabel = new JLabel(JFDResource.LABELS.getString("dialog_font"));

    private JLabel dialogFontSampleLabel = new JLabel(JFDResource.LABELS.getString("font_sample"));

    private JButton dialogFontButton = new JButton(JFDResource.LABELS.getString("modify"));

    private JLabel colorLabel = new JLabel(JFDResource.LABELS.getString("colors"));

    private ColorConfig fileColorConfig = new ColorConfig("default_label_color", "file", Color.WHITE);

    private ColorConfig readOnlyColorConfig = new ColorConfig("read_only_color", "read_only", Color.YELLOW);

    private ColorConfig subDirectoryColorConfig = new ColorConfig("sub_directory_color", "sub_directory", Color.CYAN);

    private ColorConfig messageColorConfig = new ColorConfig("message_color", "message", Color.YELLOW);

    private ColorConfig bgColorConfig = new ColorConfig("background_color", "back_ground", Color.BLACK);

    private ColorConfig gridColorConfig = new ColorConfig("grid_color", "grid", Color.WHITE);

    private ColorConfig currentDirColorConfig = new ColorConfig("current_directory_color", "current_directory", Color.WHITE);

    private ColorConfig parentDirColorConfig = new ColorConfig("parent_directory_color", "parent_directory", Color.WHITE);

    private ColorConfig linkColorConfig = new ColorConfig("link_color", "link", Color.GREEN);

    private ColorConfig listColorConfig = new ColorConfig("list_color", "list", Color.MAGENTA);

    private ColorConfig inactiveColorConfig = new ColorConfig("grid_color_no_focus", "inactive", Color.GRAY);

    private ColorConfig tagColorConfig = new ColorConfig("tag_color", "tag", Color.YELLOW);

    private ColorConfig textViewerColorConfig = new ColorConfig("text_viewer_color", "text_viewer", Color.WHITE);

    private PathConfig bgImageConfig = new PathConfig("bg_image", "bg_image", JFileChooser.FILES_ONLY);

    private DialogComboBox bgImageCombo = new DialogComboBox(frame);

    private JSlider transparencySlider = new JSlider(0, 100);

    private JLabel alignLabel = new JLabel(JFDResource.LABELS.getString("bg_image_align"));

    private JLabel transparencyLabel = new JLabel(JFDResource.LABELS.getString("transparency"));

    private JCheckBox doubleLineCheckBox = new JCheckBox(JFDResource.LABELS.getString("double_line_grid"));

    private JLabel insetLabel = new JLabel(JFDResource.LABELS.getString("row_inset"));

    private JSpinner insetSpinner = new JSpinner(new SpinnerNumberModel(0, -10, 10, 1));

    private JCheckBox cursorRequresFocusCheckBox = new JCheckBox(JFDResource.LABELS.getString("cursor_requires_focus"));

    private JCheckBox changeColorWhenNoFocusCheckBox = new JCheckBox(JFDResource.LABELS.getString("change_color_with_focus"));

    private JLabel lofLabel = new JLabel(JFDResource.LABELS.getString("look_and_feel"));

    private DialogComboBox lofCombo = new DialogComboBox(frame);

    private JLabel minColumnWidthLabel = new JLabel(JFDResource.LABELS.getString("min_column_width"));

    private JSpinner minColumnWidthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 20));

    public ViewConfigPanel(ConfigFrame frame) {
        super(new BorderLayout());
        this.frame = frame;
        try {
            initGui();
            layoutComponent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initGui() throws Exception {
        bgImageConfig.setEscapesQuate(false);
        appearancePanel = new HtmlTablePanel(VFS.getInstance().getFile(APPEARANCE_LAYOUT).getInputStream());
        add(appearancePanel);
        fontButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Font newFont = JFontChooser.showDialog(ViewConfigPanel.this, "jFD2", fontSampleLabel.getFont());
                if (newFont != null) {
                    fontSampleLabel.setFont(newFont);
                }
            }
        });
        dialogFontButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Font newFont = JFontChooser.showDialog(ViewConfigPanel.this, "jFD2", dialogFontSampleLabel.getFont());
                if (newFont != null) {
                    dialogFontSampleLabel.setFont(newFont);
                }
            }
        });
        bgImageCombo.setRenderer(new BgImageAlignComboBoxRenderer());
        bgImageCombo.addItem("tile");
        bgImageCombo.addItem("center");
        bgImageCombo.addItem("fill");
        UIManager.LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
        lofCombo.setRenderer(new DefaultListCellRenderer() {

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return super.getListCellRendererComponent(list, value != null ? ((UIManager.LookAndFeelInfo) value).getName() : JFDResource.LABELS.getString("platform_native"), index, isSelected, cellHasFocus);
            }
        });
        lofCombo.addItem(null);
        for (int i = 0; i < lookAndFeels.length; i++) {
            lofCombo.addItem(lookAndFeels[i]);
        }
        minColumnWidthSpinner.setEditor(new JSpinner.NumberEditor(minColumnWidthSpinner, "#,#00"));
    }

    public void layoutComponent() {
        appearancePanel.addComponent(fontLabel, "font_label");
        appearancePanel.addComponent(fontSampleLabel, "font_sample");
        appearancePanel.addComponent(fontButton, "font_button");
        appearancePanel.addComponent(dialogFontLabel, "dialog_font_label");
        appearancePanel.addComponent(dialogFontSampleLabel, "dialog_font_sample");
        appearancePanel.addComponent(dialogFontButton, "dialog_font_button");
        appearancePanel.addComponent(colorLabel, "color_label");
        appearancePanel.addComponent(fileColorConfig, "color_file");
        appearancePanel.addComponent(readOnlyColorConfig, "color_readonly");
        appearancePanel.addComponent(subDirectoryColorConfig, "color_dir");
        appearancePanel.addComponent(messageColorConfig, "color_message");
        appearancePanel.addComponent(bgColorConfig, "color_bg");
        appearancePanel.addComponent(gridColorConfig, "color_grid");
        appearancePanel.addComponent(currentDirColorConfig, "color_current");
        appearancePanel.addComponent(parentDirColorConfig, "color_parent");
        appearancePanel.addComponent(linkColorConfig, "color_link");
        appearancePanel.addComponent(listColorConfig, "color_list");
        appearancePanel.addComponent(inactiveColorConfig, "color_inactive");
        appearancePanel.addComponent(tagColorConfig, "color_tag");
        appearancePanel.addComponent(textViewerColorConfig, "color_text_viewer");
        appearancePanel.addComponent(doubleLineCheckBox, "double_line_grid");
        appearancePanel.addComponent(cursorRequresFocusCheckBox, "hides_cursor");
        appearancePanel.addComponent(changeColorWhenNoFocusCheckBox, "changes_color");
        appearancePanel.addComponent(insetLabel, "line_inset_label");
        appearancePanel.addComponent(insetSpinner, "line_inset_input");
        appearancePanel.addComponent(bgImageConfig, "bg_image");
        appearancePanel.addComponent(alignLabel, "bg_image_align_label");
        appearancePanel.addComponent(bgImageCombo, "bg_image_align");
        appearancePanel.addComponent(transparencyLabel, "bg_image_transparency_label");
        appearancePanel.addComponent(transparencySlider, "bg_image_transparency");
        appearancePanel.addComponent(lofLabel, "look_and_feel_label");
        appearancePanel.addComponent(lofCombo, "look_and_feel_combo");
        appearancePanel.addComponent(minColumnWidthLabel, "min_column_width_label");
        appearancePanel.addComponent(minColumnWidthSpinner, "min_column_width_spinner");
    }

    /**
	 * 設定を読み込む
	 * @param configDir
	 * @throws JDOMException
	 * @throws IOException
	 * @throws VFSException
	 */
    public void loadPreference(VFile configDir) throws JDOMException, IOException, VFSException {
        this.configDir = configDir;
        Configuration commonConfig = Configuration.getInstance(configDir.getChild(JFD.COMMON_PARAM_FILE));
        fontSampleLabel.setFont((Font) commonConfig.getParam("label_font", new Font("Monospaced", Font.PLAIN, 12)));
        dialogFontSampleLabel.setFont((Font) commonConfig.getParam("dialog_font", new Font("SansSerif", Font.PLAIN, 12)));
        fileColorConfig.setConfiguration(commonConfig);
        readOnlyColorConfig.setConfiguration(commonConfig);
        subDirectoryColorConfig.setConfiguration(commonConfig);
        messageColorConfig.setConfiguration(commonConfig);
        bgColorConfig.setConfiguration(commonConfig);
        gridColorConfig.setConfiguration(commonConfig);
        currentDirColorConfig.setConfiguration(commonConfig);
        parentDirColorConfig.setConfiguration(commonConfig);
        linkColorConfig.setConfiguration(commonConfig);
        listColorConfig.setConfiguration(commonConfig);
        inactiveColorConfig.setConfiguration(commonConfig);
        tagColorConfig.setConfiguration(commonConfig);
        textViewerColorConfig.setConfiguration(commonConfig);
        doubleLineCheckBox.setSelected(((Boolean) commonConfig.getParam("double_line", Boolean.TRUE)).booleanValue());
        cursorRequresFocusCheckBox.setSelected(((Boolean) commonConfig.getParam("hides_cursor", Boolean.TRUE)).booleanValue());
        changeColorWhenNoFocusCheckBox.setSelected(((Boolean) commonConfig.getParam("change_color_with_focus", Boolean.TRUE)).booleanValue());
        insetSpinner.setValue(commonConfig.getParam("row-inset", new Integer(0)));
        bgImageConfig.setConfiguration(commonConfig);
        transparencySlider.setValue(((Integer) commonConfig.getParam("bg_image_transparency", new Integer(100))).intValue());
        String bgAlign = (String) commonConfig.getParam("bg_image_align", BgImagePainter.DEFAULT.getName());
        bgAlign = bgAlign != null ? bgAlign : BgImagePainter.DEFAULT.getName();
        bgImageCombo.setSelectedItem(bgAlign);
        String lofClassName = (String) commonConfig.getParam("look_and_feel", null);
        lofCombo.setSelectedItem(null);
        for (int i = 0; i < lofCombo.getItemCount(); i++) {
            UIManager.LookAndFeelInfo lof = (UIManager.LookAndFeelInfo) lofCombo.getItemAt(i);
            if (lof != null && lof.getClassName().equals(lofClassName)) {
                lofCombo.setSelectedItem(lof);
            }
        }
        minColumnWidthSpinner.setValue((Integer) commonConfig.getParam("min_column_width", Integer.valueOf(300)));
    }

    /***
	 * 入力を設定に反映する。
	 * @throws JDOMException
	 * @throws IOException
	 * @throws VFSException
	 */
    public void apply() throws JDOMException, IOException, VFSException {
        Configuration commonConfig = Configuration.getInstance(configDir.getChild(JFD.COMMON_PARAM_FILE));
        commonConfig.setParam("label_font", fontSampleLabel.getFont());
        commonConfig.setParam("dialog_font", dialogFontSampleLabel.getFont());
        fileColorConfig.apply(commonConfig);
        readOnlyColorConfig.apply(commonConfig);
        subDirectoryColorConfig.apply(commonConfig);
        messageColorConfig.apply(commonConfig);
        bgColorConfig.apply(commonConfig);
        gridColorConfig.apply(commonConfig);
        currentDirColorConfig.apply(commonConfig);
        parentDirColorConfig.apply(commonConfig);
        linkColorConfig.apply(commonConfig);
        listColorConfig.apply(commonConfig);
        inactiveColorConfig.apply(commonConfig);
        tagColorConfig.apply(commonConfig);
        textViewerColorConfig.apply(commonConfig);
        commonConfig.setParam("double_line", new Boolean(doubleLineCheckBox.isSelected()));
        commonConfig.setParam("hides_cursor", new Boolean(cursorRequresFocusCheckBox.isSelected()));
        commonConfig.setParam("change_color_with_focus", new Boolean(changeColorWhenNoFocusCheckBox.isSelected()));
        commonConfig.setParam("row-inset", insetSpinner.getValue());
        bgImageConfig.apply(commonConfig);
        commonConfig.setParam("bg_image_align", bgImageCombo.getSelectedItem());
        commonConfig.setParam("bg_image_transparency", new Integer(transparencySlider.getValue()));
        UIManager.LookAndFeelInfo lof = (UIManager.LookAndFeelInfo) lofCombo.getSelectedItem();
        commonConfig.setParam("look_and_feel", lof != null ? lof.getClassName() : null);
        commonConfig.setParam("min_column_width", minColumnWidthSpinner.getValue());
    }

    public String getTitle() {
        return JFDResource.LABELS.getString("appearance");
    }
}
