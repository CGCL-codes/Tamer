package mezuniyet;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;
import sosbilens.Baglanti;
import sosbilens.ciktiSecenekFrame;

public class mezuniyetGuncelleme extends javax.swing.JFrame {

    private DefaultComboBoxModel programComboModel;

    private DefaultComboBoxModel abdComboModel;

    private DefaultComboBoxModel dekanlikModel;

    private int toplantiSayisi;

    private String toplantiTarihi;

    private ResultSet result;

    private int kararNo;

    public mezuniyetGuncelleme() {
        abdComboModel = new DefaultComboBoxModel();
        dekanlikModel = new DefaultComboBoxModel();
        dekanlik.setModel(dekanlikModel);
        initComponents();
        programComboModel = new DefaultComboBoxModel();
        programCombo.setModel(programComboModel);
        dekanlik.setEnabled(false);
        this.setTitle("Mezuniyet Güncelleme");
    }

    public mezuniyetGuncelleme(int toplantiSayisi) {
        this.toplantiSayisi = toplantiSayisi;
        abdComboModel = new DefaultComboBoxModel();
        initComponents();
        dekanlikModel = new DefaultComboBoxModel();
        dekanlik.setModel(dekanlikModel);
        programComboModel = new DefaultComboBoxModel();
        programCombo.setModel(programComboModel);
        dekanlik.setEnabled(false);
        this.setTitle("Mezuniyet Güncelleme");
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        yabanci = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        emekliCheck = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        ogrenciAdi = new javax.swing.JTextField();
        yabanciRadio = new javax.swing.JRadioButton();
        tcRadio = new javax.swing.JRadioButton();
        abdCombo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        programCombo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        egitimTuru = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        ogrenciNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        statuCombo = new javax.swing.JComboBox();
        guncelle = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tarihField = new javax.swing.JTextField();
        danisman = new javax.swing.JTextField();
        yeni = new javax.swing.JButton();
        kaydet = new javax.swing.JButton();
        geriButonu = new javax.swing.JButton();
        ileriButonu = new javax.swing.JButton();
        danismanUnvan = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        kararNoField = new javax.swing.JTextField();
        dekanlik = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jPopupMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPopupMenu1.setName("jPopupMenu1");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sosbilens.SosBilEnsApp.class).getContext().getResourceMap(mezuniyetGuncelleme.class);
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text"));
        jMenuItem1.setName("jMenuItem1");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(resourceMap.getString("Form.title"));
        setName("Form");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        emekliCheck.setName("emekliCheck");
        jLabel14.setFont(resourceMap.getFont("jLabel14.font"));
        jLabel14.setText(resourceMap.getString("jLabel14.text"));
        jLabel14.setName("jLabel14");
        jLabel2.setFont(resourceMap.getFont("jLabel2.font"));
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        jLabel1.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        ogrenciAdi.setName("ogrenciAdi");
        yabanci.add(yabanciRadio);
        yabanciRadio.setText(resourceMap.getString("yabanciRadio.text"));
        yabanciRadio.setName("yabanciRadio");
        yabanci.add(tcRadio);
        tcRadio.setText(resourceMap.getString("tcRadio.text"));
        tcRadio.setName("tcRadio");
        abdCombo.setModel(abdComboModel);
        abdCombo.setName("abdCombo");
        abdCombo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abdComboActionPerformed(evt);
            }
        });
        jLabel4.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel4.setText(resourceMap.getString("jLabel4.text"));
        jLabel4.setName("jLabel4");
        programCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        programCombo.setName("programCombo");
        jLabel3.setFont(resourceMap.getFont("jLabel3.font"));
        jLabel3.setText(resourceMap.getString("jLabel3.text"));
        jLabel3.setName("jLabel3");
        jLabel8.setFont(resourceMap.getFont("jLabel8.font"));
        jLabel8.setText(resourceMap.getString("jLabel8.text"));
        jLabel8.setName("jLabel8");
        egitimTuru.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tezli Yüksek Lisans", "Tezsiz Yüksek Lisans", "Doktora", "Birleşik Doktora", "2.Öğretim Tezsiz YL" }));
        egitimTuru.setName("egitimTuru");
        egitimTuru.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                egitimTuruActionPerformed(evt);
            }
        });
        jLabel5.setFont(resourceMap.getFont("jLabel5.font"));
        jLabel5.setText(resourceMap.getString("jLabel5.text"));
        jLabel5.setName("jLabel5");
        jLabel9.setFont(resourceMap.getFont("jLabel9.font"));
        jLabel9.setText(resourceMap.getString("jLabel9.text"));
        jLabel9.setName("jLabel9");
        ogrenciNo.setName("ogrenciNo");
        jLabel6.setFont(resourceMap.getFont("jLabel6.font"));
        jLabel6.setText(resourceMap.getString("jLabel6.text"));
        jLabel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLabel6.setName("jLabel6");
        jLabel13.setFont(resourceMap.getFont("jLabel13.font"));
        jLabel13.setText(resourceMap.getString("jLabel13.text"));
        jLabel13.setName("jLabel13");
        jLabel10.setFont(resourceMap.getFont("jLabel10.font"));
        jLabel10.setText(resourceMap.getString("jLabel10.text"));
        jLabel10.setName("jLabel10");
        statuCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Burslu", "Arş.Gör.", "Diğer" }));
        statuCombo.setName("statuCombo");
        guncelle.setText(resourceMap.getString("guncelle.text"));
        guncelle.setName("guncelle");
        guncelle.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guncelleActionPerformed(evt);
            }
        });
        jLabel12.setFont(resourceMap.getFont("jLabel12.font"));
        jLabel12.setText(resourceMap.getString("jLabel12.text"));
        jLabel12.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLabel12.setName("jLabel12");
        jLabel11.setFont(resourceMap.getFont("jLabel11.font"));
        jLabel11.setText(resourceMap.getString("jLabel11.text"));
        jLabel11.setName("jLabel11");
        tarihField.setComponentPopupMenu(jPopupMenu1);
        tarihField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        tarihField.setName("tarihField");
        danisman.setName("danisman");
        danisman.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                danismanKeyTyped(evt);
            }
        });
        yeni.setText(resourceMap.getString("yeni.text"));
        yeni.setName("yeni");
        yeni.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yeniActionPerformed(evt);
            }
        });
        kaydet.setText(resourceMap.getString("kaydet.text"));
        kaydet.setName("kaydet");
        kaydet.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kaydetActionPerformed(evt);
            }
        });
        geriButonu.setText(resourceMap.getString("geriButonu.text"));
        geriButonu.setName("geriButonu");
        geriButonu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                geriButonuActionPerformed(evt);
            }
        });
        ileriButonu.setText(resourceMap.getString("ileriButonu.text"));
        ileriButonu.setName("ileriButonu");
        ileriButonu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ileriButonuActionPerformed(evt);
            }
        });
        danismanUnvan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Prof.Dr.", "Doç.Dr.", "Yrd.Doç.Dr." }));
        danismanUnvan.setName("danismanUnvan");
        jLabel15.setName("jLabel15");
        kararNoField.setName("kararNoField");
        dekanlik.setEnabled(false);
        dekanlik.setName("dekanlik");
        jLabel7.setFont(resourceMap.getFont("jLabel7.font"));
        jLabel7.setText(resourceMap.getString("jLabel7.text"));
        jLabel7.setName("jLabel7");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(9, 9, 9).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel13).addComponent(jLabel9).addComponent(jLabel10)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(danismanUnvan, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(danisman, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(dekanlik, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(emekliCheck)).addGap(97, 97, 97)).addGroup(layout.createSequentialGroup().addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE).addGap(74, 74, 74)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel12).addComponent(jLabel4).addComponent(jLabel5).addComponent(jLabel2).addComponent(jLabel8).addComponent(jLabel1).addComponent(jLabel11).addComponent(jLabel14)).addGap(51, 51, 51).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(tcRadio).addGap(57, 57, 57).addComponent(yabanciRadio)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(programCombo, 0, 236, Short.MAX_VALUE).addComponent(ogrenciAdi, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE).addComponent(abdCombo, 0, 236, Short.MAX_VALUE).addComponent(egitimTuru, 0, 236, Short.MAX_VALUE).addComponent(statuCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(ogrenciNo, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(kararNoField, javax.swing.GroupLayout.Alignment.LEADING).addComponent(tarihField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)))).addGap(104, 104, 104))).addGap(77, 77, 77)).addComponent(jLabel3).addComponent(jLabel15).addComponent(jLabel7)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(yeni).addGap(18, 18, 18).addComponent(geriButonu, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(154, 154, 154).addComponent(ileriButonu, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(kaydet)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(19, 19, 19).addComponent(guncelle, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(75, 75, 75)))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(tarihField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel15).addComponent(jLabel7)).addGap(18, 18, 18).addComponent(jLabel12)).addGroup(layout.createSequentialGroup().addGap(8, 8, 8).addComponent(kararNoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(15, 15, 15).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel2).addGap(15, 15, 15).addComponent(jLabel4).addGap(18, 18, 18).addComponent(jLabel8).addGap(11, 11, 11).addComponent(jLabel11).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel14)).addGroup(layout.createSequentialGroup().addComponent(ogrenciNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(ogrenciAdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(abdCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(programCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(egitimTuru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(11, 11, 11).addComponent(statuCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tcRadio).addComponent(yabanciRadio)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel6).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel13).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(danismanUnvan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(danisman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel9)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(dekanlik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel10)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(emekliCheck))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(yeni).addComponent(geriButonu).addComponent(guncelle).addComponent(ileriButonu).addComponent(kaydet)).addGap(30, 30, 30)));
        pack();
    }

    private void abdComboActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String abd = abdCombo.getSelectedItem().toString();
            String query = "SELECT P.isim FROM APP.PROGRAMLAR P WHERE P.abdID=(SELECT id FROM APP.ABDALI WHERE isim LIKE '" + abd + "') ORDER BY P.isim";
            Baglanti.createConnection();
            ResultSet resultSet = Baglanti.runQuery(query);
            programComboModel.removeAllElements();
            while (resultSet.next()) {
                programComboModel.addElement(resultSet.getObject(1));
            }
            programComboModel.addElement(" ");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hata:" + e.getMessage());
        }
    }

    private void egitimTuruActionPerformed(java.awt.event.ActionEvent evt) {
        String tur = egitimTuru.getSelectedItem().toString();
    }

    private void guncelleActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String ogrenci = ogrenciAdi.getText();
            String anabilimDali = abdCombo.getSelectedItem().toString();
            String bolum = programCombo.getSelectedItem().toString();
            String statu = statuCombo.getSelectedItem().toString();
            int isRetired = emekliCheck.isSelected() ? 1 : 0;
            int isForeign = yabanciRadio.isSelected() ? 1 : 0;
            String programTipi = "";
            if (egitimTuru.getSelectedItem().toString().contentEquals("Tezli Yüksek Lisans")) programTipi = "YL"; else if (egitimTuru.getSelectedItem().toString().contentEquals("Tezsiz Yüksek Lisans")) programTipi = "Tezsiz YL"; else if (egitimTuru.getSelectedItem().toString().contentEquals("2.Öğretim Tezsiz YL")) programTipi = "2.Öğr.Tezsiz YL"; else if (egitimTuru.getSelectedItem().toString().contentEquals("Doktora")) programTipi = "DR"; else if (egitimTuru.getSelectedItem().toString().contentEquals("Bütünleşik Doktora")) programTipi = "Bütünleşik DR";
            if (kararNoField.getText().length() == 0) kararNo = 0; else kararNo = Integer.parseInt(kararNoField.getText());
            String tarih = tarihField.getText();
            String dekan = dekanlik.getSelectedItem().toString();
            StringBuffer query = new StringBuffer();
            query.append("UPDATE APP.MEZUNIYET SET ");
            query.append("adi='" + ogrenci);
            query.append("',statu='" + statu);
            query.append("',abd='" + anabilimDali);
            query.append("',tarih='" + tarih);
            query.append("',tur='" + programTipi);
            if (!danisman.getText().equals("")) {
                query.append("',danisman='" + danismanUnvan.getSelectedItem().toString() + danisman.getText() + "',");
            } else {
                query.append("',danisman='',");
            }
            query.append("danismanEmekli=" + isRetired);
            query.append(",dekanlik='" + dekan);
            query.append("',yabanci=" + isForeign);
            query.append(",kararNo=" + kararNo);
            query.append(",bolum='" + bolum);
            query.append("' WHERE toplantiSayisi=" + toplantiSayisi + " AND ogrenciNo='" + ogrenciNo.getText() + "'");
            System.out.println(query);
            Baglanti.createConnection();
            Baglanti.insertQuery(query.toString());
            JOptionPane.showMessageDialog(null, "Güncelleme işlemi yapıldı");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "sasds" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        try {
            String[] list = { " ", "DİL VE TARİH COĞRAFYA", "HUKUK", "İLAHİYAT", "İLETİŞİM", "SİYASAL BİLGİLER" };
            for (String e : list) dekanlikModel.addElement(e);
            kaydet.setVisible(false);
            Baglanti.createConnection();
            ResultSet abd = Baglanti.runQuery("SELECT isim FROM APP.ABDALI ORDER BY isim");
            while (abd.next()) abdComboModel.addElement(abd.getString(1));
            StringBuffer query = new StringBuffer();
            query.append("SELECT * FROM APP.MEZUNIYET");
            query.append(" WHERE toplantiSayisi=" + toplantiSayisi);
            Baglanti.createConnection();
            result = Baglanti.runQuery(query.toString());
            if (result.next()) veriGoster(); else {
                StringBuffer sql = new StringBuffer();
                sql.append("SELECT * FROM APP.TOPLANTI WHERE toplantiSayisi=" + toplantiSayisi);
                Baglanti.createConnection();
                result = Baglanti.runQuery(sql.toString());
                result.next();
                tarihField.setText(result.getString("toplantiTarihi"));
                guncelle.setVisible(false);
                kaydet.setVisible(true);
                yeni.setVisible(false);
                ileriButonu.setVisible(false);
                geriButonu.setVisible(false);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "HATA:" + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Hata:" + e.getMessage());
        }
    }

    private void yeniActionPerformed(java.awt.event.ActionEvent evt) {
        guncelle.setVisible(false);
        kaydet.setVisible(true);
        clearFields();
    }

    private void kaydetActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String ogrenci = ogrenciAdi.getText();
            String anabilimDali = abdCombo.getSelectedItem().toString();
            String bolum = programCombo.getSelectedItem().toString();
            String statu = statuCombo.getSelectedItem().toString();
            if (kararNoField.getText().length() == 0) kararNo = 0; else kararNo = Integer.parseInt(kararNoField.getText());
            int isRetired = emekliCheck.isSelected() ? 1 : 0;
            int isForeign = yabanciRadio.isSelected() ? 1 : 0;
            String programTipi = "";
            if (egitimTuru.getSelectedItem().toString().contentEquals("Tezli Yüksek Lisans")) programTipi = "YL"; else if (egitimTuru.getSelectedItem().toString().contentEquals("Tezsiz Yüksek Lisans")) programTipi = "Tezsiz YL"; else if (egitimTuru.getSelectedItem().toString().contentEquals("2.Öğretim Tezsiz YL")) programTipi = "2.Öğr.Tezsiz YL"; else if (egitimTuru.getSelectedItem().toString().contentEquals("Doktora")) programTipi = "DR"; else if (egitimTuru.getSelectedItem().toString().contentEquals("Birleşik Doktora")) programTipi = "Birleşik DR";
            String dekan = dekanlik.getSelectedItem().toString();
            StringBuffer query = new StringBuffer();
            query.append("INSERT INTO APP.MEZUNIYET(adi,statu,abd,tarih,tur,ogrenciNo,danisman,danismanEmekli,dekanlik,yabanci,toplantiSayisi,bolum,kararNo)");
            query.append(" VALUES('" + ogrenci + "',");
            query.append("'" + statu + "',");
            query.append("'" + anabilimDali + "',");
            query.append("'" + tarihField.getText() + "',");
            query.append("'" + programTipi + "',");
            query.append("'" + ogrenciNo.getText() + "',");
            if (!danisman.getText().equals("")) {
                query.append("'" + danismanUnvan.getSelectedItem().toString() + danisman.getText() + "',");
            } else {
                query.append("'',");
            }
            query.append(isRetired + ",");
            query.append("'" + dekan + "',");
            query.append(isForeign + ",");
            query.append("" + toplantiSayisi + ",");
            query.append("'" + bolum + "',");
            query.append(kararNo + ")");
            System.out.println(query.toString());
            Baglanti.createConnection();
            Baglanti.insertQuery(query.toString());
            int flag = JOptionPane.showConfirmDialog(this, "Yeni kayıt girmek istiyor musunuz?", "Kaydedildi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (flag == JOptionPane.YES_OPTION) {
                clearFields();
            } else {
                ciktiSecenekFrame outputForm = new ciktiSecenekFrame(toplantiSayisi);
                outputForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                outputForm.setTitle("Çıktı Seçenekleri");
                this.setVisible(false);
                outputForm.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, " asdad" + e.getMessage());
        }
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void geriButonuActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (result.previous()) {
                clearFields();
                veriGoster();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Başka kayıt yok");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Başka kayıt yok");
        }
    }

    private void ileriButonuActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (result.next()) {
                clearFields();
                veriGoster();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Başka kayıt yok");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Başka kayıt yok");
        }
    }

    private void danismanKeyTyped(java.awt.event.KeyEvent evt) {
        String value = danisman.getText();
        if (value.length() >= 1) dekanlik.setEnabled(true); else if (value.length() == 0) {
            dekanlik.setSelectedIndex(0);
            dekanlik.setEnabled(false);
        }
    }

    private void veriGoster() {
        try {
            ogrenciAdi.setText(result.getString("adi"));
            ogrenciNo.setText(result.getString("ogrenciNo"));
            kararNoField.setText(result.getString("kararNo"));
            String tarih = result.getString("tarih");
            String abd = result.getString("abd");
            tarihField.setText(tarih);
            String programTipi = result.getString("tur");
            if (programTipi.contentEquals("YL")) egitimTuru.setSelectedIndex(0); else if (programTipi.contentEquals("Tezsiz YL")) egitimTuru.setSelectedIndex(1); else if (programTipi.contentEquals("2.Öğr.Tezsiz YL")) egitimTuru.setSelectedIndex(4); else if (programTipi.contentEquals("DR")) egitimTuru.setSelectedIndex(2); else if (programTipi.contentEquals("Birleşik DR")) egitimTuru.setSelectedIndex(3);
            String program = result.getString("bolum");
            String query = "SELECT isim FROM APP.ABDALI";
            Baglanti.createConnection();
            ResultSet abResult = Baglanti.runQuery(query);
            while (abResult.next()) abdComboModel.addElement(abResult.getObject(1));
            abdComboActionPerformed(null);
            int abdIndex = abdComboModel.getIndexOf(abd);
            abdCombo.setSelectedIndex(abdIndex);
            int programIndex = programComboModel.getIndexOf(program);
            programCombo.setSelectedIndex(programIndex);
            String statu = result.getString("statu");
            if (statu.contains("Burslu")) {
                statuCombo.setSelectedIndex(0);
            } else if (statu.contains("Arş.")) {
                statuCombo.setSelectedIndex(1);
            } else {
                statuCombo.setSelectedIndex(2);
            }
            if (result.getInt("yabanci") == 1) {
                yabanciRadio.setSelected(true);
            } else {
                tcRadio.setSelected(true);
            }
            String danismanResult = result.getString("danisman");
            if (danismanResult.contains("Prof.Dr.")) {
                danismanUnvan.setSelectedIndex(0);
                danisman.setText(danismanResult.substring(8));
            } else if (danismanResult.contains("Yrd.Doç.Dr.")) {
                danismanUnvan.setSelectedIndex(2);
                danisman.setText(danismanResult.substring(11));
            } else if (danismanResult.contains("Doç.Dr.")) {
                danismanUnvan.setSelectedIndex(1);
                danisman.setText(danismanResult.substring(7));
            }
            if (danisman.getText().length() > 0) dekanlik.setEnabled(true); else dekanlik.setEnabled(false);
            String tempDekan = result.getString("dekanlik");
            int dekanlikIndex = dekanlikModel.getIndexOf(tempDekan);
            dekanlik.setSelectedIndex(dekanlikIndex);
            if (result.getInt("danismanEmekli") == 1) {
                emekliCheck.setSelected(true);
            } else {
                emekliCheck.setSelected(false);
            }
        } catch (Exception e) {
            try {
                JOptionPane.showMessageDialog(null, "Hiç kayıt yok" + e.getMessage());
                StringBuffer sql = new StringBuffer();
                sql.append("SELECT * FROM TOPLANTI WHERE toplantiSayisi=" + toplantiSayisi);
                Baglanti.createConnection();
                result = Baglanti.runQuery(sql.toString());
                result.next();
                tarihField.setText(result.getString("toplantiTarihi"));
            } catch (Exception es) {
                JOptionPane.showMessageDialog(null, es.getMessage());
            }
        }
    }

    private void clearFields() {
        ogrenciAdi.setText("");
        ogrenciNo.setText("");
        abdCombo.setSelectedIndex(0);
        programCombo.setSelectedIndex(0);
        danisman.setText("");
        dekanlik.setSelectedIndex(0);
        dekanlik.setEnabled(false);
        emekliCheck.setSelected(false);
        tcRadio.setSelected(true);
        yabanciRadio.setSelected(false);
        statuCombo.setSelectedIndex(0);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new mezuniyetGuncelleme().setVisible(true);
            }
        });
    }

    @Action
    public void Yapıştır() {
    }

    private javax.swing.JComboBox abdCombo;

    private javax.swing.JTextField danisman;

    private javax.swing.JComboBox danismanUnvan;

    private javax.swing.JComboBox dekanlik;

    private javax.swing.JComboBox egitimTuru;

    private javax.swing.JCheckBox emekliCheck;

    private javax.swing.JButton geriButonu;

    private javax.swing.JButton guncelle;

    private javax.swing.JButton ileriButonu;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JPopupMenu jPopupMenu1;

    private javax.swing.JTextField kararNoField;

    private javax.swing.JButton kaydet;

    private javax.swing.JTextField ogrenciAdi;

    private javax.swing.JTextField ogrenciNo;

    private javax.swing.JComboBox programCombo;

    private javax.swing.JComboBox statuCombo;

    private javax.swing.JTextField tarihField;

    private javax.swing.JRadioButton tcRadio;

    private javax.swing.ButtonGroup yabanci;

    private javax.swing.JRadioButton yabanciRadio;

    private javax.swing.JButton yeni;
}
