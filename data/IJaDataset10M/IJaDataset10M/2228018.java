package wand.filterChannel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.*;
import wand.ChannelFrame;

public class FilterFader extends JPanel {

    private Insets inset = new Insets(1, 1, 1, 1);

    public JSlider transparencySlider;

    private float alpha;

    public JCheckBox opaqueCheck;

    private int sliderValue;

    public FilterFader() {
        setBackground(Color.white);
        makeButtons();
        setLayout(new GridLayout(1, 2));
        add(transparencySlider);
        add(opaqueCheck);
        transparencySlider.setBackground(Color.lightGray);
    }

    public void setOpaqueCheck(boolean state) {
        opaqueCheck.setSelected(!state);
        opaqueCheck.doClick();
    }

    private void makeButtons() {
        transparencySlider = new javax.swing.JSlider();
        transparencySlider.setMaximum(100);
        transparencySlider.setMinimum(0);
        transparencySlider.setValue(100);
        transparencySlider.setOrientation(javax.swing.JSlider.VERTICAL);
        transparencySlider.setToolTipText("transparency");
        transparencySlider.setDoubleBuffered(true);
        transparencySlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                transparencySliderStateChanged(evt);
                if (transparencySlider.getValue() == transparencySlider.getMinimum() || transparencySlider.getValue() == transparencySlider.getMaximum()) transparencySlider.setBackground(Color.lightGray); else transparencySlider.setBackground(Color.red);
                restoreFocus();
            }
        });
        opaqueCheck = new JCheckBox("BG fill", true);
        opaqueCheck.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opaqueCheckActionPerformed(evt);
            }
        });
    }

    private void transparencySliderStateChanged(javax.swing.event.ChangeEvent evt) {
        sliderValue = transparencySlider.getValue();
        alpha = sliderValue * 1.0f / transparencySlider.getMaximum();
        ChannelFrame.filterPanel.getChannelDisplay().setAlpha(alpha, false);
        ChannelFrame.filterPanel.getOutputDisplayPanel().setAlpha(alpha, true);
        ChannelFrame.filterPanel.getOutputPreviewPanel().setAlpha(alpha, true);
    }

    private void opaqueCheckActionPerformed(java.awt.event.ActionEvent evt) {
        ChannelFrame.filterPanel.getChannelDisplay().setOpaque(opaqueCheck.isSelected());
        ChannelFrame.filterPanel.getOutputDisplayPanel().setOpaque(opaqueCheck.isSelected());
        ChannelFrame.filterPanel.getOutputPreviewPanel().setOpaque(opaqueCheck.isSelected());
        ChannelFrame.filterPanel.getChannelDisplay().refreshAlpha(false);
        ChannelFrame.filterPanel.getOutputDisplayPanel().refreshAlpha(true);
        ChannelFrame.filterPanel.getOutputPreviewPanel().refreshAlpha(true);
    }

    private void restoreFocus() {
        ChannelFrame.enginePanel.requestFocus();
    }
}
