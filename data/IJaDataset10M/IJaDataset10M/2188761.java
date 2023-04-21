package bbj;

import bbj.graphicsobjects.*;
import bbj.virtualobjects.*;
import colorcombo.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.event.*;
import java.util.*;

/**
 *
 * @author Lemon
 */
public final class PreferenceWindow extends JDialog implements ItemListener {

    JPanel paneGrid;

    JCheckBox gridDisplay;

    JLabel gridColorLabel;

    JLabel gridFactorLabel;

    JPanel flowPanel1;

    JPanel flowPanel2;

    JPanel flowPanel3;

    ColorComboBean colorCombo;

    JCheckBox qualityAntialiasing;

    JCheckBox qualityTextAntialiasing;

    JCheckBox qualityInterpolation;

    JComboBox comboRenderQuality;

    JComboBox comboAlpha;

    Scene scene;

    JSpinner gridFactorSpinner;

    Vector gridColors;

    public PreferenceWindow() {
    }

    public PreferenceWindow(Frame f, String title, boolean isModal) {
        super(f, title, isModal);
        this.setResizable(false);
        scene = BBJ.app.getScene();
        setSize(new Dimension(500, 200));
        gridColors = new Vector();
        gridColors.add(new Color(128, 128, 128, 64));
        gridColors.add(new Color(204, 255, 243));
        gridColors.add(new Color(204, 241, 255));
        gridColors.add(new Color(204, 216, 255));
        gridColors.add(new Color(216, 255, 204));
        gridColors.add(new Color(204, 255, 218));
        gridColors.add(new Color(143, 255, 229));
        gridColors.add(new Color(241, 255, 204));
        gridColors.add(new Color(255, 243, 204));
        gridColors.add(new Color(255, 218, 204));
        fillGUIContent();
    }

    public void fillGUIContent() {
        JTabbedPane tabbedPane = new JTabbedPane();
        JComponent panel1 = makeDrawPrefs();
        tabbedPane.addTab("Отрисовка", panel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.setFont(BBJ.tabTitleFont);
        this.add(tabbedPane);
    }

    protected JComponent makeDrawPrefs() {
        JPanel drawPrefsPanel = new JPanel(false);
        drawPrefsPanel.setSize(getSize());
        TitledBorder border = new TitledBorder(BorderFactory.createRaisedBevelBorder(), "Сетка", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        border.setTitleFont(BBJ.borderTitleFont);
        JPanel gridPrefsPanel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(gridPrefsPanel, BoxLayout.Y_AXIS);
        gridPrefsPanel.setLayout(boxlayout);
        gridPrefsPanel.setBorder(border);
        gridDisplay = new JCheckBox("Отображать сетку");
        FlowLayout flow1 = new FlowLayout();
        flow1.addLayoutComponent("gridDisplay", gridDisplay);
        gridDisplay.setSelected(scene.getIsGrid());
        gridDisplay.setFont(BBJ.commonArial);
        gridDisplay.addItemListener(this);
        gridPrefsPanel.add(gridDisplay);
        SpinnerModel model = new SpinnerNumberModel((int) (scene.getGridFactor() / SceneItem.m_SR), 10, 30, 1);
        gridFactorSpinner = new JSpinner(model);
        gridFactorSpinner.setPreferredSize(new Dimension(50, 22));
        ChangeListener gridFactorListener = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int curVal = Integer.parseInt(gridFactorSpinner.getModel().getValue().toString());
                scene.setGridFactor((int) (curVal * SceneItem.m_SR));
            }
        };
        gridFactorSpinner.addChangeListener(gridFactorListener);
        gridFactorLabel = new JLabel("Размер ячейки");
        flowPanel2 = new JPanel();
        FlowLayout flow2 = new FlowLayout();
        flowPanel2.setLayout(flow2);
        flow2.addLayoutComponent("gridFactorLabel", gridFactorLabel);
        flow2.addLayoutComponent("gridFactorSpinner", gridFactorSpinner);
        flowPanel2.add(gridFactorLabel);
        flowPanel2.add(gridFactorSpinner);
        gridPrefsPanel.add(flowPanel2);
        colorCombo = new ColorComboBean(gridColors);
        colorCombo.setSelectedItem(scene.getGridColor());
        gridPrefsPanel.add(colorCombo);
        colorCombo.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                Color color = (Color) e.getItem();
                scene.setGridColor(color);
            }
        });
        gridColorLabel = new JLabel("Цвет сетки   ");
        flowPanel3 = new JPanel();
        FlowLayout flow3 = new FlowLayout();
        flowPanel3.setLayout(flow3);
        flow3.addLayoutComponent("gridColorLabel", gridColorLabel);
        flow3.addLayoutComponent("colorCombo", colorCombo);
        flowPanel3.add(gridColorLabel);
        flowPanel3.add(colorCombo);
        gridPrefsPanel.add(flowPanel3);
        drawPrefsPanel.add(gridPrefsPanel);
        JPanel qualityPrefsPanel = new JPanel();
        BoxLayout qltLayout = new BoxLayout(qualityPrefsPanel, BoxLayout.Y_AXIS);
        qualityPrefsPanel.setLayout(qltLayout);
        TitledBorder borderQuality = new TitledBorder(BorderFactory.createRaisedBevelBorder(), "Качество отрисовки", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        border.setTitleFont(BBJ.borderTitleFont);
        qualityPrefsPanel.setBorder(borderQuality);
        borderQuality.setTitleFont(BBJ.borderTitleFont);
        qualityAntialiasing = new JCheckBox("Улучшенное сглаживание");
        qualityAntialiasing.addItemListener(this);
        qualityAntialiasing.setSelected(BBJ.qltAntialiasing == RenderingHints.VALUE_ANTIALIAS_OFF ? false : true);
        qualityAntialiasing.setFont(BBJ.commonArial);
        qualityPrefsPanel.add(qualityAntialiasing);
        qualityTextAntialiasing = new JCheckBox("Улучшенное сглаживание текста");
        qualityTextAntialiasing.addItemListener(this);
        qualityTextAntialiasing.setSelected(BBJ.qltTextAntialiasing1 == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT ? false : true);
        qualityTextAntialiasing.setFont(BBJ.commonArial);
        qualityPrefsPanel.add(qualityTextAntialiasing);
        qualityInterpolation = new JCheckBox("Улучшенная интерполяция");
        qualityInterpolation.addItemListener(this);
        qualityInterpolation.setSelected(BBJ.qltInterpolation == RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR ? false : true);
        qualityInterpolation.setFont(BBJ.commonArial);
        qualityPrefsPanel.add(qualityInterpolation);
        drawPrefsPanel.add(qualityPrefsPanel);
        return drawPrefsPanel;
    }

    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        if (source == gridDisplay && e.getStateChange() == ItemEvent.DESELECTED) {
            scene.setIsGrid(false);
            gridFactorLabel.setEnabled(false);
            gridFactorSpinner.setEnabled(false);
            gridColorLabel.setEnabled(false);
            colorCombo.setEnabled(false);
        } else if (source == gridDisplay && e.getStateChange() == ItemEvent.SELECTED) {
            scene.setIsGrid(true);
            gridFactorLabel.setEnabled(true);
            gridFactorSpinner.setEnabled(true);
            gridColorLabel.setEnabled(true);
            colorCombo.setEnabled(true);
        }
        if (source == qualityAntialiasing && e.getStateChange() == ItemEvent.DESELECTED) {
            BBJ.qltAntialiasing = RenderingHints.VALUE_ANTIALIAS_OFF;
        } else if (source == qualityAntialiasing && e.getStateChange() == ItemEvent.SELECTED) {
            BBJ.qltAntialiasing = RenderingHints.VALUE_ANTIALIAS_ON;
        }
        if (source == qualityTextAntialiasing && e.getStateChange() == ItemEvent.DESELECTED) {
            BBJ.qltTextAntialiasing1 = RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
            BBJ.qltTextAntialiasing2 = null;
            BBJ.qltTextAntialiasing3 = null;
            BBJ.qltTextAntialiasing4 = null;
        } else if (source == qualityTextAntialiasing && e.getStateChange() == ItemEvent.SELECTED) {
            BBJ.qltTextAntialiasing1 = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
            BBJ.qltTextAntialiasing3 = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
        }
        if (source == qualityInterpolation && e.getStateChange() == ItemEvent.DESELECTED) {
            BBJ.qltInterpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        } else if (source == qualityInterpolation && e.getStateChange() == ItemEvent.SELECTED) {
            BBJ.qltInterpolation = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
        }
        BBJ.app.mainFrame.repaint();
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = BBJ.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
