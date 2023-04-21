package edu.ucsd.ncmir.jibber.ui;

import edu.ucsd.ncmir.asynchronous_event.AsynchronousEvent;
import edu.ucsd.ncmir.jibber.Jibber;
import edu.ucsd.ncmir.jibber.core.Invoker;
import edu.ucsd.ncmir.jibber.events.UpdateEvent;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author  spl
 */
public class ColorSelector extends JFrame {

    private static final long serialVersionUID = 42L;

    /**
     * Creates new form ColorSelector
     */
    public ColorSelector() {
        new SetOverlayColorEvent().send(false);
        new SetMarksColorEvent().send(false);
        this.initComponents();
        this.setTitle(Jibber.BANNER + " Color Selector");
        ColorSelectionModel csm = this.color_chooser.getSelectionModel();
        csm.addChangeListener(new ColorChangeListener(this));
        new Invoker(this);
    }

    private class ColorChangeListener implements ChangeListener {

        private ColorSelector _color_selector;

        ColorChangeListener(ColorSelector color_selector) {
            this._color_selector = color_selector;
        }

        public void stateChanged(ChangeEvent change_event) {
            Color color = this._color_selector.color_chooser.getColor();
            this._color_selector.setColor(color);
        }
    }

    private void setColor(Color color) {
        this._set_color_event.send(color);
        new UpdateEvent().send();
    }

    private SetColorEvent _set_color_event = new SetMarksColorEvent();

    private class SetColorEvent extends AsynchronousEvent {
    }

    private class SetOverlayColorEvent extends SetColorEvent {
    }

    private class SetMarksColorEvent extends SetColorEvent {
    }

    private void initComponents() {
        display_object_type = new javax.swing.ButtonGroup();
        color_chooser = new javax.swing.JColorChooser();
        jPanel1 = new javax.swing.JPanel();
        overlay = new javax.swing.JRadioButton();
        marks = new javax.swing.JRadioButton();
        revert = new javax.swing.JButton();
        reset = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        color_chooser.setFocusable(false);
        color_chooser.setRequestFocusEnabled(false);
        color_chooser.setVerifyInputWhenFocusTarget(false);
        jPanel1.setLayout(new java.awt.GridLayout(1, 6, 5, 0));
        display_object_type.add(overlay);
        overlay.setText("Overlay");
        overlay.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        overlay.setFocusPainted(false);
        overlay.setFocusable(false);
        overlay.setMargin(new java.awt.Insets(0, 0, 0, 0));
        overlay.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overlayActionPerformed(evt);
            }
        });
        jPanel1.add(overlay);
        display_object_type.add(marks);
        marks.setSelected(true);
        marks.setText("Marks");
        marks.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        marks.setFocusPainted(false);
        marks.setFocusable(false);
        marks.setMargin(new java.awt.Insets(0, 0, 0, 0));
        marks.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                marksActionPerformed(evt);
            }
        });
        jPanel1.add(marks);
        revert.setText("Revert");
        revert.setFocusPainted(false);
        revert.setFocusable(false);
        revert.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revertActionPerformed(evt);
            }
        });
        jPanel1.add(revert);
        reset.setText("Default");
        reset.setFocusPainted(false);
        reset.setFocusable(false);
        reset.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });
        jPanel1.add(reset);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, color_chooser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 420, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(color_chooser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    private void marksActionPerformed(java.awt.event.ActionEvent event) {
        this._set_color_event = new SetMarksColorEvent();
    }

    private void overlayActionPerformed(java.awt.event.ActionEvent event) {
        this._set_color_event = new SetOverlayColorEvent();
    }

    private void resetActionPerformed(java.awt.event.ActionEvent event) {
        this._set_color_event.send(null);
    }

    private void revertActionPerformed(java.awt.event.ActionEvent event) {
        this._set_color_event.send(true);
    }

    private class DialogClosedEvent extends AsynchronousEvent {
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        new DialogClosedEvent().send();
    }

    private javax.swing.JColorChooser color_chooser;

    private javax.swing.ButtonGroup display_object_type;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JRadioButton marks;

    private javax.swing.JRadioButton overlay;

    private javax.swing.JButton reset;

    private javax.swing.JButton revert;
}
