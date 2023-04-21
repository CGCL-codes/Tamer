package org.ai.pattern.gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import org.ai.pattern.UmbralFinder;

/**
 *
 * @author  yannart
 */
public class HistogramaDialog extends javax.swing.JDialog {

    private HistogramaPanel histograma;

    private MainFrame parent;

    private long[] values;

    /** Creates new form HistogramaDialog */
    public HistogramaDialog(MainFrame parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
        histograma = new HistogramaPanel();
        jPanelHistogramaData.add(histograma);
    }

    public void setValues(long[] values, long maxnum, int umbral) {
        this.values = values;
        histograma.setValues(values, maxnum);
        setUmbral(umbral);
    }

    private void setUmbral(int umbral) {
        jSliderUmbral.setValue(umbral);
        jSpinnerUmbral.setValue(umbral);
        histograma.setUmbral(umbral);
    }

    private void initComponents() {
        jPanelHistogramaData = new javax.swing.JPanel();
        jSliderUmbral = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jButtonDetectarUmbral = new javax.swing.JButton();
        jButtonAceptar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jSpinnerUmbral = new javax.swing.JSpinner();
        jSeparator1 = new javax.swing.JSeparator();
        setTitle("Histograma");
        setResizable(false);
        jPanelHistogramaData.setMinimumSize(new java.awt.Dimension(255, 90));
        jPanelHistogramaData.setPreferredSize(new java.awt.Dimension(255, 100));
        jPanelHistogramaData.setLayout(new java.awt.BorderLayout());
        jSliderUmbral.setMajorTickSpacing(25);
        jSliderUmbral.setMaximum(255);
        jSliderUmbral.setMinorTickSpacing(5);
        jSliderUmbral.setPaintLabels(true);
        jSliderUmbral.setPaintTicks(true);
        jSliderUmbral.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderUmbralStateChanged(evt);
            }
        });
        jLabel1.setText("Umbral:");
        jButtonDetectarUmbral.setText("Detectar Umbral");
        jButtonDetectarUmbral.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonDetectarUmbralMouseClicked(evt);
            }
        });
        jButtonAceptar.setText("Aceptar Umbral");
        jButtonAceptar.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAceptarMouseClicked(evt);
            }
        });
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonCancelarMouseClicked(evt);
            }
        });
        jSpinnerUmbral.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));
        jSpinnerUmbral.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerUmbralStateChanged(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSpinnerUmbral, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 232, Short.MAX_VALUE).addComponent(jButtonDetectarUmbral).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jButtonCancelar).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButtonAceptar).addContainerGap()).addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE))).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanelHistogramaData, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE).addContainerGap()).addComponent(jSliderUmbral, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanelHistogramaData, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSliderUmbral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(3, 3, 3).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonDetectarUmbral).addComponent(jSpinnerUmbral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addGap(9, 9, 9).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonAceptar).addComponent(jButtonCancelar)).addContainerGap()));
        pack();
    }

    private void jButtonCancelarMouseClicked(java.awt.event.MouseEvent evt) {
        this.setVisible(false);
    }

    private void jButtonAceptarMouseClicked(java.awt.event.MouseEvent evt) {
        parent.setUmbralActual((Integer) jSpinnerUmbral.getValue());
        this.setVisible(false);
    }

    private void jButtonDetectarUmbralMouseClicked(java.awt.event.MouseEvent evt) {
        int umbral = UmbralFinder.getUmbral(values, UmbralFinder.ISODATA_ALGORITHM);
        setUmbral(umbral);
    }

    private void jSpinnerUmbralStateChanged(javax.swing.event.ChangeEvent evt) {
        JSpinner source = (JSpinner) evt.getSource();
        int valor = (Integer) source.getValue();
        jSliderUmbral.setValue(valor);
        histograma.setUmbral(valor);
    }

    private void jSliderUmbralStateChanged(javax.swing.event.ChangeEvent evt) {
        JSlider source = (JSlider) evt.getSource();
        int valor = source.getValue();
        jSpinnerUmbral.setValue(valor);
        histograma.setUmbral(valor);
    }

    private javax.swing.JButton jButtonAceptar;

    private javax.swing.JButton jButtonCancelar;

    private javax.swing.JButton jButtonDetectarUmbral;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanelHistogramaData;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSlider jSliderUmbral;

    private javax.swing.JSpinner jSpinnerUmbral;
}

class HistogramaPanel extends JPanel {

    private long[] values;

    private long maxnum;

    private int umbral;

    public void setValues(long[] values, long maxnum) {
        this.values = values;
        this.maxnum = maxnum;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = this.getWidth();
        int h = this.getHeight();
        int xpos;
        int ypos;
        int xumbral;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.BLACK);
        for (int i = 0; i < values.length; i++) {
            xpos = i * 2;
            ypos = (int) (h - (values[i] * h / maxnum));
            g.setColor(Color.BLACK);
            g.drawLine(xpos, h, xpos, ypos);
            g.drawLine(xpos + 1, h, xpos + 1, ypos);
        }
        g.setColor(Color.LIGHT_GRAY);
        xumbral = umbral * 2;
        g.drawLine(xumbral, 0, xumbral, h);
        g.drawLine(xumbral + 1, 0, xumbral + 1, h);
    }

    public void setUmbral(int umbral) {
        this.umbral = umbral;
        repaint();
    }
}
