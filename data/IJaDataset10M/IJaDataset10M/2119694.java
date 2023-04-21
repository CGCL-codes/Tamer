package org.javagame.mapeditor;

import java.awt.Dimension;
import java.util.ArrayList;

/**
 *
 * @author  Leonardo
 */
public class MapEditorPanel extends javax.swing.JPanel {

    private TilePanel tilePanel;

    private MapPanel mapPanel;

    private LayerPanel layerPanel;

    public MapEditorPanel(String nomeArqXML) {
        ResourceManager rm = ResourceManager.getInstance();
        boolean carregado = rm.load(nomeArqXML);
        if (carregado) {
            initComponents();
            mapPanel = new MapPanel();
            area1.setViewportView(mapPanel);
            atualizaMapa();
            tilePanel = new TilePanel();
            area3.add(tilePanel);
            layerPanel = new LayerPanel();
            area2.add(layerPanel);
            area1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        }
    }

    private void atualizaMapa() {
        if (mapPanel != null) {
            mapPanel.atualiza();
        }
    }

    private void initComponents() {
        area1 = new javax.swing.JScrollPane();
        area2 = new javax.swing.JPanel();
        area3 = new javax.swing.JPanel();
        setLayout(new java.awt.BorderLayout());
        area1.addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentShown(java.awt.event.ComponentEvent evt) {
                area1ComponentShown(evt);
            }
        });
        add(area1, java.awt.BorderLayout.CENTER);
        area2.setLayout(new java.awt.BorderLayout());
        area2.setBackground(new java.awt.Color(255, 255, 204));
        area2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        area2.setMinimumSize(new java.awt.Dimension(150, 500));
        area2.setPreferredSize(new java.awt.Dimension(170, 500));
        add(area2, java.awt.BorderLayout.EAST);
        area3.setLayout(new java.awt.BorderLayout());
        area3.setPreferredSize(new java.awt.Dimension(0, 80));
        add(area3, java.awt.BorderLayout.SOUTH);
    }

    private void area1ComponentShown(java.awt.event.ComponentEvent evt) {
        atualizaMapa();
    }

    private javax.swing.JScrollPane area1;

    private javax.swing.JPanel area2;

    private javax.swing.JPanel area3;
}
