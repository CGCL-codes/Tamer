package es.devel.opentrats.booking.gui;

import es.devel.opentrats.booking.service.business.EnvironmentService;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author  Fran Serrano
 */
public class frmOpenTratsBooking extends javax.swing.JFrame {

    private static final long serialVersionUID = -2020163527694698466L;

    protected JLabel lblStatus;

    /** Creates new form frmOpenTratsBooking */
    public frmOpenTratsBooking() {
        initComponents();
        lblStatus = new JLabel("Estado: ");
        lblStatus.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        lblStatus.setBorder(BorderFactory.createEtchedBorder());
        lblStatus.setFocusable(false);
        lblStatus.setSize(100, 14);
        lblStatus.setToolTipText("Estado de la aplicacion");
        lblStatus.setLocation(0, this.getHeight() - lblStatus.getHeight());
        this.add(lblStatus);
        this.setTitle(EnvironmentService.getInstance().getVersion());
    }

    protected void setStatus(String status) {
        this.lblStatus.setText("Estado: " + status);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 300, Short.MAX_VALUE));
        pack();
    }

    private void formComponentResized(java.awt.event.ComponentEvent evt) {
    }
}
