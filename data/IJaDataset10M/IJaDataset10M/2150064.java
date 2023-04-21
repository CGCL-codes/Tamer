package com.sistemask.sc.gui.frame.image.internalFrame.chooserZoomer;

import com.sistemask.sc.core.manager.image.ImageManager;
import com.sistemask.sc.gui.frame.image.internalFrame.InternalFrameAbstract;
import com.sistemask.sc.gui.util.FrameUtil;
import com.sistemask.sc.gui.util.GuiUtil;
import java.awt.Image;
import java.awt.Point;
import javax.swing.SpinnerNumberModel;

public class ChooserZoomerJInternalFrame extends InternalFrameAbstract {

    public static ChooserZoomerJInternalFrame instance = null;

    /**
     * Metodo para obtener el singleton
     * @return
     */
    public static ChooserZoomerJInternalFrame getInstance() {
        if (instance == null) {
            instance = new ChooserZoomerJInternalFrame();
        }
        return instance;
    }

    /**
     * Constructor
     */
    private ChooserZoomerJInternalFrame() {
        initComponents();
        FrameUtil.setFrameIcon(this, FrameUtil.CHOOSERZOOMER_IMG);
    }

    public void cleanImage() {
        imagePanel.setImage(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel = new javax.swing.JPanel();
        jButtonZoomIn = new javax.swing.JButton();
        jButtonZoomOut = new javax.swing.JButton();
        jButtonReset = new javax.swing.JButton();
        jLabelGrid = new javax.swing.JLabel();
        jSpinnerGrid = new javax.swing.JSpinner();
        jToggleButtonGrid = new javax.swing.JToggleButton();
        imagePanel = new com.sistemask.sc.gui.frame.image.internalFrame.chooserZoomer.ImagePanelChooserZoomer();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Zoom:");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {

            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }

            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        jButtonZoomIn.setText("+");
        jButtonZoomIn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZoomInActionPerformed(evt);
            }
        });
        jPanel.add(jButtonZoomIn);
        jButtonZoomOut.setText("-");
        jButtonZoomOut.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZoomOutActionPerformed(evt);
            }
        });
        jPanel.add(jButtonZoomOut);
        jButtonReset.setLabel("Reset");
        jButtonReset.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetActionPerformed(evt);
            }
        });
        jPanel.add(jButtonReset);
        jLabelGrid.setText("Grid:");
        jPanel.add(jLabelGrid);
        jSpinnerGrid.setModel(new javax.swing.SpinnerNumberModel(155, 1, 300, 1));
        jSpinnerGrid.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerGridStateChanged(evt);
            }
        });
        jPanel.add(jSpinnerGrid);
        jToggleButtonGrid.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sistemask/sc/gui/resources/image/misc/grid.png")));
        jToggleButtonGrid.setPreferredSize(new java.awt.Dimension(35, 35));
        jToggleButtonGrid.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonGridActionPerformed(evt);
            }
        });
        jPanel.add(jToggleButtonGrid);
        getContentPane().add(jPanel, java.awt.BorderLayout.PAGE_END);
        imagePanel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {

            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                imagePanelMouseWheelMoved(evt);
            }
        });
        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 312, Short.MAX_VALUE));
        imagePanelLayout.setVerticalGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 237, Short.MAX_VALUE));
        getContentPane().add(imagePanel, java.awt.BorderLayout.CENTER);
        pack();
    }

    private void jButtonZoomInActionPerformed(java.awt.event.ActionEvent evt) {
        imagePanel.zoomIn();
    }

    private void jButtonZoomOutActionPerformed(java.awt.event.ActionEvent evt) {
        imagePanel.zoomOut();
    }

    private void jButtonResetActionPerformed(java.awt.event.ActionEvent evt) {
        imagePanel.reset();
        imagePanel.repaint();
    }

    private void imagePanelMouseReleased(java.awt.event.MouseEvent evt) {
        imagePanel.resetMouseDrag();
    }

    private void imagePanelMouseDragged(java.awt.event.MouseEvent evt) {
        int x = evt.getX();
        int y = evt.getY();
        imagePanel.mouseDragPosition(x, y);
    }

    private void imagePanelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
        if (evt.getWheelRotation() < 0) {
            imagePanel.zoomIn();
        } else {
            imagePanel.zoomOut();
        }
    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        if (getClosingCallBack() == null) {
            this.setVisible(false);
        } else {
            getClosingCallBack().exec(this);
        }
    }

    private void jToggleButtonGridActionPerformed(java.awt.event.ActionEvent evt) {
        if (!jToggleButtonGrid.isSelected()) {
            imagePanel.hideGrid();
        } else {
            SpinnerNumberModel _model = (SpinnerNumberModel) jSpinnerGrid.getModel();
            int _value = _model.getNumber().intValue();
            imagePanel.drawGrid(_value);
        }
        imagePanel.repaint();
    }

    private void jSpinnerGridStateChanged(javax.swing.event.ChangeEvent evt) {
        if (jToggleButtonGrid.isSelected()) {
            SpinnerNumberModel _model = (SpinnerNumberModel) jSpinnerGrid.getModel();
            int _value = _model.getNumber().intValue();
            imagePanel.drawGrid(_value);
            imagePanel.repaint();
        }
    }

    /**
     * Metodo que hace el corte de la imagen grandota y
     * la muestra en el imagepanel
     * @param img
     * @param point
     * @param zoom
     */
    public void setImagePoint(Image img, Point point, float zoom) {
        Image _img = ImageManager.getImageFrame(GuiUtil.getCenterPosOfFrame(point, zoom), img);
        imagePanel.setImage(_img);
    }

    /**
     * Metodo que fija la imagen a mostrar en el imagepanel
     * @param img
     */
    public void setImage(Image img) {
        if (img != null) imagePanel.setImage(img);
    }

    /**
     * Refresca la instancia
     */
    public void refresh() {
        super.setImage(null);
    }

    /**
     * Respawmea la instancia
     */
    public void restart() {
        instance = new ChooserZoomerJInternalFrame();
    }

    private com.sistemask.sc.gui.frame.image.internalFrame.chooserZoomer.ImagePanelChooserZoomer imagePanel;

    private javax.swing.JButton jButtonReset;

    private javax.swing.JButton jButtonZoomIn;

    private javax.swing.JButton jButtonZoomOut;

    private javax.swing.JLabel jLabelGrid;

    private javax.swing.JPanel jPanel;

    private javax.swing.JSpinner jSpinnerGrid;

    private javax.swing.JToggleButton jToggleButtonGrid;
}
