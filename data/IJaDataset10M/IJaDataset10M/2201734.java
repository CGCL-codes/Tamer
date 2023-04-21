package jshomeorg.simplytrain.gui.panels;

import java.text.*;
import javax.swing.*;
import jshomeorg.event.*;
import jshomeorg.simplytrain.*;
import jshomeorg.simplytrain.editor.EditorActionEvent;
import jshomeorg.simplytrain.editor.EditorEvent;
import jshomeorg.simplytrain.gui.*;
import jshomeorg.simplytrain.service.track;
import javax.swing.JFormattedTextField.AbstractFormatter;
import jshomeorg.simplytrain.service.trackGroup;

/**
 *
 * @author  js
 */
public class mainpanel_edittrackgroup extends javax.swing.JPanel {

    private class FormattedTextFieldVerifier extends InputVerifier {

        public boolean verify(JComponent input) {
            if (input instanceof JFormattedTextField) {
                JFormattedTextField ftf = (JFormattedTextField) input;
                AbstractFormatter formatter = ftf.getFormatter();
                if (formatter != null) {
                    String text = ftf.getText();
                    try {
                        formatter.stringToValue(text);
                        return true;
                    } catch (ParseException pe) {
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean shouldYieldFocus(JComponent input) {
            return verify(input);
        }
    }

    ;

    trackGroup workingTrack = null;

    /** Creates new form mainpanel_file */
    public mainpanel_edittrackgroup() {
        initComponents();
        dataCollector.collector.editorEventListeners.addListener(new AbstractListener() {

            public void action(AbstractEvent e) {
                trackEvent((EditorEvent) e);
            }
        });
        level_Slider.setMinimum(track.MINLEVEL);
        level_Slider.setMaximum(track.MAXLEVEL);
        level_Slider.setValue(0);
    }

    private void initComponents() {
        clone_Button = new javax.swing.JButton();
        del_Button = new javax.swing.JButton();
        clearMark_Button = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        level_Slider = new javax.swing.JSlider();
        level_Label = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        touchedT_CB = new javax.swing.JCheckBox();
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        clone_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jshomeorg/simplytrain/gui/resources/cp_paste.png")));
        clone_Button.setText("duplizieren");
        clone_Button.setActionCommand("clone");
        clone_Button.setEnabled(false);
        clone_Button.setMargin(new java.awt.Insets(2, 5, 2, 5));
        clone_Button.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonActionPerformed(evt);
            }
        });
        add(clone_Button);
        del_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jshomeorg/simplytrain/gui/resources/delete.png")));
        del_Button.setText("löschen");
        del_Button.setActionCommand("del");
        del_Button.setEnabled(false);
        del_Button.setMargin(new java.awt.Insets(2, 5, 2, 5));
        del_Button.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonActionPerformed(evt);
            }
        });
        add(del_Button);
        clearMark_Button.setText("Markerung löschen");
        clearMark_Button.setActionCommand("clearselection");
        clearMark_Button.setEnabled(false);
        clearMark_Button.setMargin(new java.awt.Insets(2, 5, 2, 5));
        clearMark_Button.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonActionPerformed(evt);
            }
        });
        add(clearMark_Button);
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setMaximumSize(new java.awt.Dimension(5, 32767));
        jSeparator1.setMinimumSize(new java.awt.Dimension(5, 25));
        jSeparator1.setPreferredSize(new java.awt.Dimension(5, 25));
        add(jSeparator1);
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        level_Slider.setMajorTickSpacing(1);
        level_Slider.setMaximum(10);
        level_Slider.setMinorTickSpacing(1);
        level_Slider.setSnapToTicks(true);
        level_Slider.setToolTipText("Höhe");
        level_Slider.setValue(0);
        level_Slider.setPreferredSize(new java.awt.Dimension(100, 24));
        level_Slider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                level_SliderStateChanged(evt);
            }
        });
        jPanel1.add(level_Slider);
        level_Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        level_Label.setLabelFor(level_Slider);
        level_Label.setMaximumSize(new java.awt.Dimension(40, 24));
        level_Label.setMinimumSize(new java.awt.Dimension(20, 10));
        level_Label.setPreferredSize(new java.awt.Dimension(20, 23));
        jPanel1.add(level_Label);
        add(jPanel1);
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setMaximumSize(new java.awt.Dimension(5, 32767));
        jSeparator2.setMinimumSize(new java.awt.Dimension(5, 25));
        jSeparator2.setPreferredSize(new java.awt.Dimension(5, 25));
        add(jSeparator2);
        touchedT_CB.setText("auch Teilgleise");
        touchedT_CB.setActionCommand("touchedT");
        touchedT_CB.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        touchedT_CB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        touchedT_CB.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBActionPerformed(evt);
            }
        });
        add(touchedT_CB);
    }

    private void CBActionPerformed(java.awt.event.ActionEvent evt) {
        dataCollector.collector.thegame.runAction(new EditorActionEvent(evt.getActionCommand(), ((JCheckBox) evt.getSource()).isSelected()));
    }

    private void level_SliderStateChanged(javax.swing.event.ChangeEvent evt) {
        level_Label.setText("" + level_Slider.getValue());
        dataCollector.collector.thegame.runAction(new EditorActionEvent<Integer>("level", level_Slider.getValue()));
    }

    private void ButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dataCollector.collector.thegame.runAction(new EditorActionEvent(evt.getActionCommand()));
    }

    private javax.swing.JButton clearMark_Button;

    private javax.swing.JButton clone_Button;

    private javax.swing.JButton del_Button;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JLabel level_Label;

    private javax.swing.JSlider level_Slider;

    private javax.swing.JCheckBox touchedT_CB;

    void trackEvent(EditorEvent e) {
        workingTrack = e.getTrackGroup();
        switch(e.getType()) {
            case EditorEvent.TRACKGROUP_UNSELECTED:
                clone_Button.setEnabled(false);
                del_Button.setEnabled(false);
                clearMark_Button.setEnabled(false);
                break;
            case EditorEvent.TRACKGROUP_SELECTED:
                clone_Button.setEnabled(true);
                del_Button.setEnabled(true);
                clearMark_Button.setEnabled(false);
                break;
            case EditorEvent.TRACKGROUP_AREASELECTED:
                clone_Button.setEnabled(true);
                del_Button.setEnabled(true);
                clearMark_Button.setEnabled(true);
                break;
        }
    }
}
