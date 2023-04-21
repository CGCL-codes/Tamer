package com.hadeslee.yoyoplayer.setting;

import com.hadeslee.yoyoplayer.lyric.LyricPanel;
import com.hadeslee.yoyoplayer.util.Config;
import com.hadeslee.yoyoplayer.util.FontChooser;
import com.hadeslee.yoyoplayer.util.Util;
import com.sun.jna.examples.WindowUtils;
import java.awt.Color;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author  hadeslee
 */
public class LyricSettingPanel extends javax.swing.JPanel implements Initable {

    private static Logger log = Logger.getLogger(LyricSettingPanel.class.getName());

    private Color foregroundColor, backgroundColor, hilightColor;

    private Font font;

    public static final int width = 15;

    public static final int height = 15;

    /** Creates new form LyricSettingPanel */
    public LyricSettingPanel() {
        initComponents();
        init();
    }

    private void initComponents() {
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        scrollMode = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        alignMode = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        hSpace = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        vSpace = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        lyricShadow = new javax.swing.JCheckBox();
        karaoke = new javax.swing.JCheckBox();
        trans = new javax.swing.JCheckBox();
        borderTrans = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        foreground = new javax.swing.JButton();
        hilight = new javax.swing.JButton();
        background = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        autoLoadLyric = new javax.swing.JCheckBox();
        cutBlankChars = new javax.swing.JCheckBox();
        autoHide = new javax.swing.JCheckBox();
        autoResize = new javax.swing.JCheckBox();
        resizeV = new javax.swing.JCheckBox();
        mouseDragEnable = new javax.swing.JCheckBox();
        anti = new javax.swing.JCheckBox();
        scrollAdjust = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        refreshInterval = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        topShow = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        setDoubleBuffered(false);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(Config.getResource("LyricSettingPanel.view")));
        jLabel1.setText(Config.getResource("LyricSettingPanel.scrollMode"));
        scrollMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "水平模式", "垂直模式" }));
        jLabel2.setText(Config.getResource("LyricSettingPanel.alignMode"));
        alignMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "左对齐", "中间对齐", "右对齐" }));
        jLabel3.setText(Config.getResource("LyricSettingPanel.HDistance"));
        jLabel4.setText(Config.getResource("LyricSettingPanel.pix"));
        jLabel5.setText(Config.getResource("LyricSettingPanel.VDistance"));
        jLabel6.setText(Config.getResource("LyricSettingPanel.pix"));
        lyricShadow.setText(Config.getResource("LyricSettingPanel.shadowLyric"));
        karaoke.setText(Config.getResource("LyricSettingPanel.karaoke"));
        trans.setText(Config.getResource("LyricSettingPanel.transBG"));
        trans.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transActionPerformed(evt);
            }
        });
        borderTrans.setText(Config.getResource("LyricSettingPanel.borderTrans"));
        jLabel7.setText(Config.getResource("LyricSettingPanel.color"));
        foreground.setText(Config.getResource("LyricSettingPanel.normal"));
        foreground.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foregroundActionPerformed(evt);
            }
        });
        hilight.setText(Config.getResource("LyricSettingPanel.hilight"));
        hilight.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hilightActionPerformed(evt);
            }
        });
        background.setText(Config.getResource("LyricSettingPanel.bg"));
        background.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backgroundActionPerformed(evt);
            }
        });
        jLabel8.setText(Config.getResource("LyricSettingPanel.font"));
        jButton4.setText(Config.getResource("LyricSettingPanel.changeFont"));
        jButton4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(scrollMode, 0, 136, Short.MAX_VALUE)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(hSpace, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel4).addGap(4, 4, 4))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(lyricShadow).addGap(103, 103, 103))).addGroup(jPanel1Layout.createSequentialGroup().addComponent(trans).addGap(79, 79, 79))).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(borderTrans).addComponent(karaoke).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(alignMode, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(vSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel6)))).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(foreground).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(hilight).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(background).addGap(30, 30, 30).addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton4))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(alignMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2).addComponent(scrollMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jLabel5).addComponent(vSpace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6).addComponent(jLabel4).addComponent(hSpace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lyricShadow).addComponent(karaoke)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(trans).addComponent(borderTrans)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(foreground).addComponent(hilight).addComponent(background).addComponent(jLabel8).addComponent(jButton4)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(Config.getResource("LyricSettingPanel.option")));
        autoLoadLyric.setText(Config.getResource("LyricSettingPanel.autoLoadLyric"));
        cutBlankChars.setText(Config.getResource("LyricSettingPanel.cutBlankChars"));
        autoHide.setText(Config.getResource("LyricSettingPanel.autoHide"));
        autoResize.setText(Config.getResource("LyricSettingPanel.autoResize"));
        resizeV.setText(Config.getResource("LyricSettingPanel.onlyResizeWhenV"));
        mouseDragEnable.setText(Config.getResource("LyricSettingPanel.canDragToLocation"));
        anti.setText(Config.getResource("LyricSettingPanel.textAnti"));
        scrollAdjust.setText(Config.getResource("LyricSettingPanel.mouseWheelToAdjust"));
        jLabel9.setText(Config.getResource("LyricSettingPanel.refreshTime"));
        refreshInterval.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(50), Integer.valueOf(0), null, Integer.valueOf(1)));
        jLabel10.setText(Config.getResource("LyricSettingPanel.ms"));
        topShow.setText(Config.getResource("LyricSettingPanel.showOnTop"));
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(autoLoadLyric).addComponent(autoResize).addComponent(mouseDragEnable).addComponent(scrollAdjust).addComponent(autoHide)).addGap(25, 25, 25).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(topShow).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel9).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(refreshInterval, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel10)).addComponent(anti).addComponent(resizeV).addComponent(cutBlankChars)).addContainerGap(19, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(autoLoadLyric).addComponent(cutBlankChars)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(autoHide).addComponent(topShow)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(autoResize).addComponent(resizeV)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(mouseDragEnable).addComponent(anti)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(scrollAdjust).addComponent(jLabel9).addComponent(refreshInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel10))));
        jButton9.setText(Config.getResource("save"));
        jButton9.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton9);
        jButton10.setText(Config.getResource("reset"));
        jButton10.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton10);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()).addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
    }

    private void transActionPerformed(java.awt.event.ActionEvent evt) {
        borderTrans.setEnabled(trans.isSelected());
    }

    private void foregroundActionPerformed(java.awt.event.ActionEvent evt) {
        Color c = JColorChooser.showDialog(this, Config.getResource("LyricSettingPanel.foreground"), foregroundColor);
        if (c != null) {
            foregroundColor = c;
            foreground.setIcon(Util.createColorIcon(c, width, height));
        }
    }

    private void hilightActionPerformed(java.awt.event.ActionEvent evt) {
        Color c = JColorChooser.showDialog(this, Config.getResource("LyricSettingPanel.selectHilight"), hilightColor);
        if (c != null) {
            hilightColor = c;
            hilight.setIcon(Util.createColorIcon(c, width, height));
        }
    }

    private void backgroundActionPerformed(java.awt.event.ActionEvent evt) {
        Color c = JColorChooser.showDialog(this, Config.getResource("LyricSettingPanel.selectBG"), backgroundColor);
        if (c != null) {
            backgroundColor = c;
            background.setIcon(Util.createColorIcon(c, width, height));
        }
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        Font f = FontChooser.showDialog(null, Config.getResource("LyricSettingPanel.selectFont"), font);
        if (f != null) {
            font = f;
        }
    }

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {
        doSave();
    }

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {
        init();
    }

    public void init() {
        Config config = Config.getConfig();
        scrollMode.setSelectedItem(getStringOfScrollMode(config.getLpState()));
        alignMode.setSelectedItem(getStringOfAlignMode(config.getLyricAlignMode()));
        hSpace.setValue(config.getH_SPACE());
        vSpace.setValue(config.getV_SPACE());
        lyricShadow.setSelected(config.isLyricShadow());
        karaoke.setSelected(config.isKaraoke());
        if (config.isLinux()) {
            trans.setEnabled(false);
            borderTrans.setEnabled(false);
        } else {
            trans.setSelected(config.isTransparency());
            borderTrans.setSelected(!config.isShowLrcBorder());
            borderTrans.setEnabled(config.isTransparency());
        }
        foregroundColor = config.getLyricForeground();
        backgroundColor = config.getLyricBackground();
        hilightColor = config.getLyricHilight();
        font = config.getLyricFont();
        foreground.setIcon(Util.createColorIcon(foregroundColor, width, height));
        background.setIcon(Util.createColorIcon(backgroundColor, width, height));
        hilight.setIcon(Util.createColorIcon(hilightColor, width, height));
        autoLoadLyric.setSelected(config.isAutoLoadLyric());
        cutBlankChars.setSelected(config.isCutBlankChars());
        autoHide.setSelected(config.isHideWhenNoLyric());
        topShow.setSelected(config.isLyricTopShow());
        autoResize.setSelected(config.isAutoResize());
        resizeV.setSelected(config.isOnlyResizeWhenVerticalMode());
        mouseDragEnable.setSelected(config.isMouseDragToSeekEnabled());
        anti.setSelected(config.isAntiAliasing());
        scrollAdjust.setSelected(config.isMouseScrollAjustTime());
        refreshInterval.setValue(config.getRefreshInterval());
    }

    private String getStringOfScrollMode(int mode) {
        switch(mode) {
            case LyricPanel.H:
                return "水平模式";
            case LyricPanel.V:
                return "垂直模式";
        }
        return "垂直模式";
    }

    private String getStringOfAlignMode(int mode) {
        switch(mode) {
            case Config.LYRIC_CENTER_ALIGN:
                return "中间对齐";
            case Config.LYRIC_LEFT_ALIGN:
                return "左对齐";
            case Config.LYRIC_RIGHT_ALIGN:
                return "右对齐";
        }
        return "中间对齐";
    }

    private int getIntOfAlignMode(String s) {
        if (s.equals("中间对齐")) {
            return Config.LYRIC_CENTER_ALIGN;
        } else if (s.equals("左对齐")) {
            return Config.LYRIC_LEFT_ALIGN;
        } else if (s.equals("右对齐")) {
            return Config.LYRIC_RIGHT_ALIGN;
        } else {
            return Config.LYRIC_CENTER_ALIGN;
        }
    }

    private int getIntOfScrollMode(String s) {
        if (s.equals("水平模式")) {
            return LyricPanel.H;
        } else if (s.equals("垂直模式")) {
            return LyricPanel.V;
        } else {
            return LyricPanel.V;
        }
    }

    private void doSave() {
        Config config = Config.getConfig();
        try {
            config.setH_SPACE(Integer.parseInt(hSpace.getValue().toString()));
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(this, Config.getResource("LyricSettingPanel.HDistanceNotNumber"));
            hSpace.requestFocus();
            return;
        }
        try {
            config.setV_SPACE(Integer.parseInt(vSpace.getValue().toString()));
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(this, Config.getResource("LyricSettingPanel.VDistanceNotNumber"));
            vSpace.requestFocus();
            return;
        }
        try {
            int i = Integer.parseInt(refreshInterval.getValue().toString());
            if (i < 0) {
                JOptionPane.showMessageDialog(this, Config.getResource("LyricSettingPanel.lessthan0"));
                refreshInterval.requestFocus();
                return;
            }
            config.setRefreshInterval(i);
        } catch (NumberFormatException numberFormatException) {
            refreshInterval.requestFocus();
            return;
        }
        config.setLpState(getIntOfScrollMode(scrollMode.getSelectedItem().toString()));
        config.setLyricAlignMode(getIntOfAlignMode(alignMode.getSelectedItem().toString()));
        config.setLyricShadow(lyricShadow.isSelected());
        config.setKaraoke(karaoke.isSelected());
        config.setLyricForeground(foregroundColor);
        config.setLyricBackground(backgroundColor);
        config.setLyricHilight(hilightColor);
        config.setLyricFont(font);
        config.setAutoLoadLyric(autoLoadLyric.isSelected());
        config.setCutBlankChars(cutBlankChars.isSelected());
        config.setHideWhenNoLyric(autoHide.isSelected());
        config.setLyricTopShow(topShow.isSelected());
        config.setAutoResize(autoResize.isSelected());
        config.setOnlyResizeWhenVerticalMode(resizeV.isSelected());
        config.setMouseDragToSeekEnabled(mouseDragEnable.isSelected());
        config.setAntiAliasing(anti.isSelected());
        config.setMouseScrollAjustTime(scrollAdjust.isSelected());
        config.setShowLrcBorder(!borderTrans.isSelected());
        if (trans.isSelected() != config.isTransparency()) {
            config.setTransparency(trans.isSelected());
            if (trans.isSelected()) {
                config.getPlayer().getLyricUI().setBorderEnabled(config.isShowLrcBorder());
                JDialog jd = config.getPlayer().getLoader().changeLrcDialog();
                WindowUtils.setWindowTransparent(jd, true);
                config.getPlayer().getLyricUI().start();
            } else {
                config.getPlayer().getLyricUI().setBorderEnabled(true);
                WindowUtils.setWindowTransparent(config.getLrcWindow(), false);
            }
        }
        if (config.isTransparency()) {
            config.getPlayer().getLyricUI().setBorderEnabled(config.isShowLrcBorder());
        }
        config.getPlayer().getLyricUI().getLyricPanel().setResized(!autoResize.isSelected());
        if (config.isAutoCloseDialogWhenSave()) {
            config.getOptionDialog().setVisible(false);
        }
    }

    private javax.swing.JComboBox alignMode;

    private javax.swing.JCheckBox anti;

    private javax.swing.JCheckBox autoHide;

    private javax.swing.JCheckBox autoLoadLyric;

    private javax.swing.JCheckBox autoResize;

    private javax.swing.JButton background;

    private javax.swing.JCheckBox borderTrans;

    private javax.swing.JCheckBox cutBlankChars;

    private javax.swing.JButton foreground;

    private javax.swing.JSpinner hSpace;

    private javax.swing.JButton hilight;

    private javax.swing.JButton jButton10;

    private javax.swing.JButton jButton4;

    private javax.swing.JButton jButton9;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JCheckBox karaoke;

    private javax.swing.JCheckBox lyricShadow;

    private javax.swing.JCheckBox mouseDragEnable;

    private javax.swing.JSpinner refreshInterval;

    private javax.swing.JCheckBox resizeV;

    private javax.swing.JCheckBox scrollAdjust;

    private javax.swing.JComboBox scrollMode;

    private javax.swing.JCheckBox topShow;

    private javax.swing.JCheckBox trans;

    private javax.swing.JSpinner vSpace;
}
