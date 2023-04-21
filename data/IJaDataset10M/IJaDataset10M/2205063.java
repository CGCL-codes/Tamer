package com.billing.client;

import javax.swing.ImageIcon;

/**
 *
 * @author javamaniac  < mrt.itnewbies@gmail.com >
 */
public class PanelWarnet extends javax.swing.JPanel {

    static final int FAILED_STAT = 1;

    static final int NORMAL_STAT = 2;

    static final int WAITING_STAT = 3;

    final ImageIcon ICON_FAILED;

    final ImageIcon ICON_NORMAL;

    final ImageIcon ICON_WAITING;

    public PanelWarnet() {
        initComponents();
        ICON_FAILED = new ImageIcon(getClass().getResource("/images/icon_loading.gif"));
        ICON_NORMAL = new ImageIcon(getClass().getResource("/images/connect_normal.png"));
        ICON_WAITING = new ImageIcon(getClass().getResource("/images/connect_waiting.png"));
    }

    private void initComponents() {
        lblIcon = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblNmWarnet = new javax.swing.JLabel();
        lblAlamat = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lblIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/connect_normal.png")));
        lblStatus.setText("Connected to server ...");
        lblNmWarnet.setFont(new java.awt.Font("Dialog", 1, 18));
        lblNmWarnet.setText("warNETPOS Kediri");
        lblAlamat.setText("Jln. Mayjend Sungkono Nomor 32 Kediri");
        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        lblTitle.setFont(new java.awt.Font("Dialog", 1, 18));
        lblTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cyber-billing-logo.png")));
        lblTitle.setText("Cyber-Billing");
        lblTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblTitle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(7, 7, 7)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblNmWarnet).addGroup(layout.createSequentialGroup().addComponent(lblIcon).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblStatus).addComponent(lblAlamat)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(lblNmWarnet).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addComponent(lblAlamat).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE).addComponent(lblStatus)).addComponent(lblIcon))));
    }

    /**
     * method setWarnet ini berfungsi untuk menampilkan nama warnet dan 
     * alamat warnet jika koneksi telah berhasil dilakukan dengan server.
     *
     * @param warnet : nama warnet yang ingin ditampilkan.
     * @param warnetAddrs : alamat warnet yang ingin ditampilkan
     */
    public void setWarnet(final String warnet, final String warnetAddrs) {
        lblNmWarnet.setText(warnet);
        lblAlamat.setText(warnetAddrs);
    }

    /**
     * method setStatusConnection ini berfungsi untuk menampilkan status koneksi
     * pada server billing. Ada 3 macam mode koneksi disini yaitu :
     * <pre>
     *  1. KONEKSI FAILED (ditandai dng ERR_NUMBER = 1) :
     *      - Koneksi failed ini akan terjadi jika proses koneksi ke server terputus
     *        atau ketika user dengan status member salah dalam proses login.
     *  2. KONEKSI NORMAL (ditandai dng ERR_NUMBER = 2) :
     *      - Koneksi normal ini terjadi jika proses koneksi ke server billing telah 
     *        sukses dilakukan.
     *  3. KONEKSI WAITING (ditandai dng ERR_NUMBER = 3) :
     *      - Koneksi waiting ini terjadi jika proses koneksi ke server billing masih
     *        belum sukses dilakukan, tetapi koneksi tidak dalam kondisi terputus.
     * </pre>
     *
     * @param status : keterangan status yang ingin ditampilkan
     * @param ERR_NUMBER : error number dari proses koneksi.
     */
    public void setStatusConnection(final String status, final int ERR_NUMBER) {
        if (ERR_NUMBER == FAILED_STAT) {
            lblStatus.setText(status);
            lblIcon.setIcon(ICON_FAILED);
        } else if (ERR_NUMBER == NORMAL_STAT) {
            lblStatus.setText(status);
            lblIcon.setIcon(ICON_NORMAL);
        } else if (ERR_NUMBER == WAITING_STAT) {
            lblStatus.setText(status);
            lblIcon.setIcon(ICON_WAITING);
        }
    }

    private javax.swing.JPanel jPanel1;

    private javax.swing.JLabel lblAlamat;

    private javax.swing.JLabel lblIcon;

    private javax.swing.JLabel lblNmWarnet;

    private javax.swing.JLabel lblStatus;

    private javax.swing.JLabel lblTitle;
}
