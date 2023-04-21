package co.edu.unal.ungrid.services.client.applet.atlas.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import co.edu.unal.ungrid.services.client.util.ColorIcon;
import co.edu.unal.ungrid.services.client.util.GroupLayout;
import co.edu.unal.ungrid.services.client.util.LayoutStyle;
import co.edu.unal.ungrid.services.client.util.OptionsDlg;

public class AtlasAppOptionsDlg extends OptionsDlg {

    public static final long serialVersionUID = 1L;

    public AtlasAppOptionsDlg() {
        super();
    }

    @Override
    protected void createComponents() {
        super.createComponents();
        jTabbedPane = new JTabbedPane();
        jColorPanel = new JPanel();
        jBackgColorLabel = new JLabel();
        jBackgColorButton = new JButton();
        jGridColorLabel = new JLabel();
        jGridColorButton = new JButton();
        jCurveColorLabel = new JLabel();
        jCurveColorButton = new JButton();
        jSelCurveColorLabel = new JLabel();
        jSelCurveColorButton = new JButton();
        jCtrlPtColorLabel = new JLabel();
        jCtrlPtColorButton = new JButton();
        jSelCtrlPtColorLabel = new JLabel();
        jSelCtrlPtColorButton = new JButton();
        jDirsPanel = new JPanel();
        jLocalDirLabel = new JLabel();
        jImagesDirLabel = new JLabel();
        jLocalDirTextField = new JTextField();
        jImagesDirTextField = new JTextField();
        jLocalDirButton = new JButton();
        jImagesDirButton = new JButton();
        jOptionsPanel = new JPanel();
        jHelp = new JTextPane() {

            public static final long serialVersionUID = 1L;

            public Dimension getPreferredSize() {
                return new Dimension(0, 80);
            }
        };
        jHelp.setEditable(false);
        jHelp.setForeground(new Color(80, 80, 80));
        jHelp.setFont(new Font("Arial Narrow", 0, 13));
        jHelp.setAutoscrolls(false);
        jHelp.setBorder(BorderFactory.createTitledBorder(""));
        jHelp.setFocusable(false);
        jHelp.setVerifyInputWhenFocusTarget(false);
        jHelp.getAccessibleContext().setAccessibleParent(null);
        jBackgColorLabel.setText("Background");
        jBackgColorButton.setMargin(new Insets(4, 14, 4, 14));
        jGridColorLabel.setText("Grid");
        jGridColorButton.setMargin(new Insets(4, 14, 4, 14));
        jCurveColorLabel.setText("Curves");
        jCurveColorButton.setMargin(new Insets(4, 14, 4, 14));
        jSelCurveColorLabel.setText("Selected");
        jSelCurveColorButton.setMargin(new Insets(4, 14, 4, 14));
        jCtrlPtColorLabel.setText("Control Points");
        jCtrlPtColorButton.setMargin(new Insets(4, 14, 4, 14));
        jSelCtrlPtColorLabel.setText("Selected");
        jSelCtrlPtColorButton.setMargin(new Insets(4, 14, 4, 14));
        GroupLayout jColorPanelLayout = new GroupLayout(jColorPanel);
        jColorPanel.setLayout(jColorPanelLayout);
        jColorPanelLayout.setHorizontalGroup(jColorPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(jColorPanelLayout.createSequentialGroup().addContainerGap().addGroup(jColorPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(jColorPanelLayout.createSequentialGroup().addGroup(jColorPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER, false).addComponent(jCtrlPtColorLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jGridColorLabel, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE).addComponent(jCurveColorLabel, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)).addGap(20, 20, 20)).addGroup(jColorPanelLayout.createSequentialGroup().addComponent(jBackgColorLabel, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))).addGroup(jColorPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(jColorPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(jCurveColorButton).addComponent(jBackgColorButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jGridColorButton)).addComponent(jCtrlPtColorButton)).addGap(30, 30, 30).addGroup(jColorPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(jSelCurveColorLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jSelCtrlPtColorLabel)).addGap(12, 12, 12).addGroup(jColorPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(jSelCurveColorButton, GroupLayout.Alignment.TRAILING).addComponent(jSelCtrlPtColorButton, GroupLayout.Alignment.TRAILING)).addGap(202, 202, 202)));
        jColorPanelLayout.setVerticalGroup(jColorPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(jColorPanelLayout.createSequentialGroup().addGap(20, 20, 20).addGroup(jColorPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(jColorPanelLayout.createSequentialGroup().addComponent(jBackgColorButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jGridColorButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jCurveColorButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jCtrlPtColorButton)).addGroup(jColorPanelLayout.createSequentialGroup().addComponent(jBackgColorLabel).addGap(20, 20, 20).addComponent(jGridColorLabel).addGap(20, 20, 20).addComponent(jCurveColorLabel).addGap(20, 20, 20).addComponent(jCtrlPtColorLabel)).addGroup(jColorPanelLayout.createSequentialGroup().addGap(74, 74, 74).addGroup(jColorPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(jColorPanelLayout.createSequentialGroup().addComponent(jSelCurveColorButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jSelCtrlPtColorButton)).addGroup(jColorPanelLayout.createSequentialGroup().addComponent(jSelCurveColorLabel).addGap(20, 20, 20).addComponent(jSelCtrlPtColorLabel))))).addContainerGap(246, Short.MAX_VALUE)));
        jTabbedPane.addTab("Colors", jColorPanel);
        jLocalDirLabel.setText("Local Documents");
        jLocalDirButton.setText("Browse...");
        jImagesDirLabel.setText("Local Images");
        jImagesDirButton.setText("Browse...");
        GroupLayout jDirsPanelLayout = new GroupLayout(jDirsPanel);
        jDirsPanel.setLayout(jDirsPanelLayout);
        jDirsPanelLayout.setHorizontalGroup(jDirsPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(jDirsPanelLayout.createSequentialGroup().addContainerGap().addGroup(jDirsPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(jImagesDirLabel, GroupLayout.Alignment.CENTER, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLocalDirLabel, GroupLayout.Alignment.CENTER, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(12, 12, 12).addGroup(jDirsPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(jImagesDirTextField, GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE).addComponent(jLocalDirTextField, GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jDirsPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER, false).addComponent(jImagesDirButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLocalDirButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        jDirsPanelLayout.setVerticalGroup(jDirsPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(jDirsPanelLayout.createSequentialGroup().addGap(20, 20, 20).addGroup(jDirsPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(jLocalDirLabel).addComponent(jLocalDirButton).addComponent(jLocalDirTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jDirsPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(jImagesDirLabel).addComponent(jImagesDirTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jImagesDirButton)).addContainerGap(301, Short.MAX_VALUE)));
        jTabbedPane.addTab("Directories", jDirsPanel);
        jOptionsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        jOptionsPanel.setLayout(new BorderLayout());
        JPanel jOptionsPanelNorth = new JPanel(new GridLayout(5, 2, 20, 8));
        jCopyLayerCurves = new JCheckBox("Copy Slice Segments");
        jOptionsPanelNorth.add(jCopyLayerCurves);
        jOptionsPanel.add(jOptionsPanelNorth, BorderLayout.NORTH);
        jOptionsPanel.add(jHelp, BorderLayout.SOUTH);
        jTabbedPane.addTab("Options", jOptionsPanel);
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jTabbedPane, GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jTabbedPane, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE).addContainerGap()));
    }

    @Override
    public AtlasAppOptions getOptions() {
        return (AtlasAppOptions) super.getOptions();
    }

    protected void addActionListeners() {
        super.addActionListeners();
        final AtlasAppOptions options = getOptions();
        jTabbedPane.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent ke) {
                if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
                    approveSelection();
                } else if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    cancelSelection();
                }
            }
        });
        jBackgColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Color clr = JColorChooser.showDialog(AtlasAppOptionsDlg.this, "Background Color", options.getColor(AtlasAppOptions.BACKG_CLR, Color.BLACK));
                if (clr != null) {
                    options.setColor(AtlasAppOptions.BACKG_CLR, clr);
                    ColorIcon ci = (ColorIcon) jBackgColorButton.getIcon();
                    ci.setColor(clr);
                }
            }
        });
        jGridColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Color clr = JColorChooser.showDialog(AtlasAppOptionsDlg.this, "Grid Color", options.getColor(AtlasAppOptions.GRID_CLR, Color.GRAY));
                if (clr != null) {
                    options.setColor(AtlasAppOptions.GRID_CLR, clr);
                    ColorIcon ci = (ColorIcon) jGridColorButton.getIcon();
                    ci.setColor(clr);
                }
            }
        });
        jCurveColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Color clr = JColorChooser.showDialog(AtlasAppOptionsDlg.this, "Curves Color", options.getColor(AtlasAppOptions.CURVE_CLR, Color.GREEN));
                if (clr != null) {
                    options.setColor(AtlasAppOptions.CURVE_CLR, clr);
                    ColorIcon ci = (ColorIcon) jCurveColorButton.getIcon();
                    ci.setColor(clr);
                }
            }
        });
        jSelCurveColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Color clr = JColorChooser.showDialog(AtlasAppOptionsDlg.this, "Selected Curve Color", options.getColor(AtlasAppOptions.CURVE_SEL_CLR, Color.RED));
                if (clr != null) {
                    options.setColor(AtlasAppOptions.CURVE_SEL_CLR, clr);
                    ColorIcon ci = (ColorIcon) jSelCurveColorButton.getIcon();
                    ci.setColor(clr);
                }
            }
        });
        jCtrlPtColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Color clr = JColorChooser.showDialog(AtlasAppOptionsDlg.this, "Control Point Color", options.getColor(AtlasAppOptions.CPOINT_CLR, Color.GREEN));
                if (clr != null) {
                    options.setColor(AtlasAppOptions.CPOINT_CLR, clr);
                    ColorIcon ci = (ColorIcon) jCtrlPtColorButton.getIcon();
                    ci.setColor(clr);
                }
            }
        });
        jSelCtrlPtColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Color clr = JColorChooser.showDialog(AtlasAppOptionsDlg.this, "Selected Control Point Color", options.getColor(AtlasAppOptions.CPOINT_SEL_CLR, Color.RED));
                if (clr != null) {
                    options.setColor(AtlasAppOptions.CPOINT_SEL_CLR, clr);
                    ColorIcon ci = (ColorIcon) jSelCtrlPtColorButton.getIcon();
                    ci.setColor(clr);
                }
            }
        });
        jLocalDirButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                File f = readDirectoryName("Default Documents Folder", jLocalDirTextField.getText());
                if (f != null) {
                    jLocalDirTextField.setText(f.getAbsolutePath());
                }
            }
        });
        jImagesDirButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                File f = readDirectoryName("Default Images Folder", jImagesDirTextField.getText());
                if (f != null) {
                    jImagesDirTextField.setText(f.getAbsolutePath());
                }
            }
        });
        jCopyLayerCurves.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (jCopyLayerCurves.isSelected()) {
                    showHelp(AUTO_COPY_CURVES_ON);
                } else {
                    showHelp(AUTO_COPY_CURVES_OFF);
                }
            }
        });
        jCopyLayerCurves.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent me) {
                if (jCopyLayerCurves.isSelected()) {
                    showHelp(AUTO_COPY_CURVES_ON);
                } else {
                    showHelp(AUTO_COPY_CURVES_OFF);
                }
            }

            public void mouseExited(MouseEvent me) {
                showHelp(DEFAULT_MSG);
            }
        });
    }

    protected void showHelp(final String msg) {
        jHelp.setText(msg);
    }

    @Override
    protected void approveSelection() {
        AtlasAppOptions options = getOptions();
        options.setDocsDir(jLocalDirTextField.getText());
        options.setImgsDir(jImagesDirTextField.getText());
        super.approveSelection();
    }

    @Override
    protected void initComponents() {
        AtlasAppOptions options = getOptions();
        assert options != null;
        jBackgColorButton.setIcon(new ColorIcon(options.getColor(AtlasAppOptions.BACKG_CLR, Color.BLACK), 54, 11));
        jGridColorButton.setIcon(new ColorIcon(options.getColor(AtlasAppOptions.GRID_CLR, Color.GRAY), 54, 11));
        jCurveColorButton.setIcon(new ColorIcon(options.getColor(AtlasAppOptions.CURVE_CLR, Color.GREEN), 54, 11));
        jSelCurveColorButton.setIcon(new ColorIcon(options.getColor(AtlasAppOptions.CURVE_SEL_CLR, Color.GREEN), 54, 11));
        jCtrlPtColorButton.setIcon(new ColorIcon(options.getColor(AtlasAppOptions.CPOINT_CLR, Color.RED), 54, 11));
        jSelCtrlPtColorButton.setIcon(new ColorIcon(options.getColor(AtlasAppOptions.CPOINT_SEL_CLR, Color.RED), 54, 11));
        jLocalDirTextField.setText(options.getDocsDir());
        jImagesDirTextField.setText(options.getImgsDir());
    }

    public static void main(String[] args) {
        AtlasAppOptionsDlg dlg = new AtlasAppOptionsDlg();
        System.out.println(dlg.showDialog("Options Dlg Test", new AtlasAppOptions()));
    }

    protected JButton jBackgColorButton;

    protected JButton jGridColorButton;

    protected JButton jCurveColorButton;

    protected JButton jSelCurveColorButton;

    protected JButton jCtrlPtColorButton;

    protected JButton jSelCtrlPtColorButton;

    protected JButton jLocalDirButton;

    protected JButton jImagesDirButton;

    protected JLabel jBackgColorLabel;

    protected JLabel jGridColorLabel;

    protected JLabel jCurveColorLabel;

    protected JLabel jSelCurveColorLabel;

    protected JLabel jCtrlPtColorLabel;

    protected JLabel jSelCtrlPtColorLabel;

    protected JLabel jLocalDirLabel;

    protected JLabel jImagesDirLabel;

    protected JPanel jColorPanel;

    protected JPanel jDirsPanel;

    protected JPanel jOptionsPanel;

    protected JTabbedPane jTabbedPane;

    protected JTextField jLocalDirTextField;

    protected JTextField jImagesDirTextField;

    protected JCheckBox jCopyLayerCurves;

    protected JEditorPane jHelp;

    protected static final String DEFAULT_MSG = "";

    protected static final String AUTO_COPY_CURVES_OFF = "Do not duplicate current slice segments when moving to next/previous slice.";

    protected static final String AUTO_COPY_CURVES_ON = "Duplicate current slice segments when moving to next/previous slice.";
}
