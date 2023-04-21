package aplikasi.com.gui;

import aplikasi.com.entity.komik;
import aplikasi.com.implement.implemKomik;
import aplikasi.com.interfac.interKomik;
import aplikasi.com.koneksiDatabase.DatabaseUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Henfryandie
 */
public class fromTambahKomik extends javax.swing.JInternalFrame {

    private int id = 0;

    private List<komik> rekam = new ArrayList<komik>();

    private interKomik dataKomik;

    /** Creates new form fromTambahKomik */
    public fromTambahKomik() {
        initComponents();
        dataKomik = new implemKomik();
        this.statusAwal();
        tabelKomik.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                btnSimpan.setText("Update");
                int row = tabelKomik.getSelectedRow();
                if (row != -1) {
                    isiTeks();
                }
            }
        });
    }

    void isiTeks() {
        komik k = rekam.get(tabelKomik.getSelectedRow());
        txtNomorKomik.setText(String.valueOf(k.getIdKomik()));
        txtJudulKomik.setText(k.getJudulKomik());
        txtPengarang.setText(k.getPengarang());
        txtTarifSewa.setText(String.valueOf(k.getTarifSewa()));
    }

    void loadAllKomik() {
        try {
            rekam = dataKomik.getAll();
        } catch (SQLException ex) {
            Logger.getLogger(fromTambahKomik.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void isiTabel() {
        Object data[][] = new Object[rekam.size()][4];
        int x = 0;
        for (komik k : rekam) {
            data[x][0] = k.getIdKomik();
            data[x][1] = k.getJudulKomik();
            data[x][2] = k.getPengarang();
            data[x][3] = k.getTarifSewa();
            x++;
        }
        String judul[] = { "Id Komik", "Judul", "Pengarang", "Tarif Sewa" };
        tabelKomik.setModel(new DefaultTableModel(data, judul));
        jScrollPane1.setViewportView(tabelKomik);
    }

    void kosongkanTeks() {
        txtJudulKomik.setText("");
        txtPengarang.setText("");
        txtTarifSewa.setText("");
    }

    int cariID() {
        try {
            String sql = "select idKomik from komik order by idKomik desc";
            Statement st = DatabaseUtilities.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                id = rs.getInt("idKomik");
            }
        } catch (SQLException ex) {
            Logger.getLogger(formTambahPelanggan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ++id;
    }

    void statusAwal() {
        this.loadAllKomik();
        this.isiTabel();
        this.kosongkanTeks();
        btnSimpan.setText("Simpan");
        txtNomorKomik.setText(String.valueOf(cariID()));
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNomorKomik = new javax.swing.JTextField();
        txtJudulKomik = new javax.swing.JTextField();
        txtPengarang = new javax.swing.JTextField();
        txtTarifSewa = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelKomik = new javax.swing.JTable();
        setClosable(true);
        setIconifiable(true);
        setTitle("Form Tambah Komik");
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Isi Data Komik"));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 14));
        jLabel1.setText("Nomor Komik");
        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14));
        jLabel2.setText("Judul Komik");
        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14));
        jLabel3.setText("Pengarang");
        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14));
        jLabel4.setText("Tarif Sewa");
        txtNomorKomik.setEditable(false);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jLabel2).addComponent(jLabel3).addComponent(jLabel4)).addGap(10, 10, 10).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtJudulKomik, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE).addComponent(txtPengarang, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtNomorKomik, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(198, Short.MAX_VALUE)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(txtTarifSewa, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()))));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(txtNomorKomik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(txtJudulKomik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(txtPengarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(txtTarifSewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(148, 148, 148).addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(135, Short.MAX_VALUE)));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnHapus).addComponent(btnSimpan).addComponent(btnReset)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        tabelKomik.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane1.setViewportView(tabelKomik);
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 596, Short.MAX_VALUE).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 140, Short.MAX_VALUE).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(17, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 616, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 347, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (btnSimpan.getText().equals("Update")) {
                JOptionPane.showMessageDialog(this, "Belum Bisa Update", "Informasi", JOptionPane.ERROR_MESSAGE);
            } else if (btnSimpan.getText().equals("Simpan")) {
                if (txtJudulKomik.getText().isEmpty() || txtPengarang.getText().isEmpty() || txtTarifSewa.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Isi Data Secara Lengkap", "Informasi", JOptionPane.ERROR_MESSAGE);
                } else {
                    komik k = new komik();
                    k.setJudulKomik(txtJudulKomik.getText());
                    k.setPengarang(txtPengarang.getText());
                    k.setTarifSewa(Integer.valueOf(txtTarifSewa.getText()));
                    dataKomik.insertKar(k);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(formTambahPelanggan.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.statusAwal();
    }

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (txtJudulKomik.getText().isEmpty() || txtPengarang.getText().isEmpty() || txtTarifSewa.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Klik data pada tabel terlebih dahulu", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            } else {
                if (JOptionPane.showConfirmDialog(this, "Apakah Anda yakin untuk menghapus Daftar komik ini", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    komik k = rekam.get(tabelKomik.getSelectedRow());
                    dataKomik.delete(k.getJudulKomik());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(fromTambahKomik.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.statusAwal();
    }

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {
        this.statusAwal();
    }

    private javax.swing.JButton btnHapus;

    private javax.swing.JButton btnReset;

    private javax.swing.JButton btnSimpan;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTable tabelKomik;

    private javax.swing.JTextField txtJudulKomik;

    private javax.swing.JTextField txtNomorKomik;

    private javax.swing.JTextField txtPengarang;

    private javax.swing.JTextField txtTarifSewa;
}
