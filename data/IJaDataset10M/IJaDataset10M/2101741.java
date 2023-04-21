package danisman;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import sosbilens.Baglanti;
import sosbilens.ciktiSecenekFrame;

public class danismanGiris extends javax.swing.JFrame {

    private DefaultComboBoxModel programComboModel;

    private DefaultComboBoxModel abdComboModel;

    private int toplantiSayisi;

    private String toplantiTarihi;

    private int kararNo;

    /** Creates new form danismanGiris */
    public danismanGiris() {
        abdComboModel = new DefaultComboBoxModel();
        initComponents();
        programComboModel = new DefaultComboBoxModel();
        programCombo.setModel(programComboModel);
        dekanlik.setEnabled(false);
    }

    public danismanGiris(int toplantiSayisi, String tarih) {
        this.toplantiSayisi = toplantiSayisi;
        abdComboModel = new DefaultComboBoxModel();
        initComponents();
        programComboModel = new DefaultComboBoxModel();
        programCombo.setModel(programComboModel);
        this.toplantiSayisi = toplantiSayisi;
        toplantiTarihi = tarih;
        dekanlik.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        dekanlik = new javax.swing.JComboBox();
        danismanUnvan = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        programCombo = new javax.swing.JComboBox();
        kararNoField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        yabanciRadio = new javax.swing.JRadioButton();
        abdCombo = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        ogrenciNo = new javax.swing.JTextField();
        yonetmelikmadde = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        danisman = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        emekliCheck = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        tarihField = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        ogrenciAdi = new javax.swing.JTextField();
        kaydet = new javax.swing.JButton();
        statuCombo = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        tcRadio = new javax.swing.JRadioButton();
        egitimTuru = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        dilekceTarihiDatePicker = new com.toedter.calendar.JDateChooser();
        jLabel29 = new javax.swing.JLabel();
        dilekceNoField = new javax.swing.JTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jLabel9.setName("jLabel9");
        jLabel2.setName("jLabel2");
        jLabel1.setName("jLabel1");
        jLabel10.setName("jLabel10");
        jLabel5.setName("jLabel5");
        jLabel15.setName("jLabel15");
        jLabel8.setName("jLabel8");
        jLabel3.setName("jLabel3");
        jLabel4.setName("jLabel4");
        jLabel11.setName("jLabel11");
        jLabel13.setName("jLabel13");
        jLabel14.setName("jLabel14");
        dekanlik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "DİL VE TARİH COĞRAFYA", "HUKUK", "İLAHİYAT", "İLETİŞİM", "SİYASAL BİLGİLER" }));
        dekanlik.setEnabled(false);
        dekanlik.setName("dekanlik");
        danismanUnvan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Prof.Dr.", "Doç.Dr.", "Yrd.Doç.Dr." }));
        danismanUnvan.setName("danismanUnvan");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sosbilens.SosBilEnsApp.class).getContext().getResourceMap(danismanGiris.class);
        jLabel16.setFont(resourceMap.getFont("jLabel16.font"));
        jLabel16.setText(resourceMap.getString("jLabel16.text"));
        jLabel16.setName("jLabel16");
        jLabel6.setFont(resourceMap.getFont("jLabel16.font"));
        jLabel6.setText(resourceMap.getString("jLabel6.text"));
        jLabel6.setName("jLabel6");
        programCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        programCombo.setName("programCombo");
        kararNoField.setName("kararNoField");
        jLabel12.setFont(resourceMap.getFont("jLabel12.font"));
        jLabel12.setText(resourceMap.getString("jLabel12.text"));
        jLabel12.setName("jLabel12");
        jLabel7.setFont(resourceMap.getFont("jLabel7.font"));
        jLabel7.setText(resourceMap.getString("jLabel7.text"));
        jLabel7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLabel7.setName("jLabel7");
        yabanciRadio.setText(resourceMap.getString("yabanciRadio.text"));
        yabanciRadio.setName("yabanciRadio");
        abdCombo.setModel(abdComboModel);
        abdCombo.setName("abdCombo");
        abdCombo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abdComboActionPerformed(evt);
            }
        });
        jLabel17.setFont(resourceMap.getFont("jLabel17.font"));
        jLabel17.setText(resourceMap.getString("jLabel17.text"));
        jLabel17.setName("jLabel17");
        jLabel18.setFont(resourceMap.getFont("jLabel18.font"));
        jLabel18.setText(resourceMap.getString("jLabel18.text"));
        jLabel18.setName("jLabel18");
        ogrenciNo.setName("ogrenciNo");
        yonetmelikmadde.setName("yonetmelikmadde");
        jLabel19.setFont(resourceMap.getFont("jLabel19.font"));
        jLabel19.setText(resourceMap.getString("jLabel19.text"));
        jLabel19.setName("jLabel19");
        danisman.setName("danisman");
        danisman.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                danismanActionPerformed(evt);
            }
        });
        danisman.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                danismanKeyTyped(evt);
            }
        });
        jLabel20.setFont(resourceMap.getFont("jLabel20.font"));
        jLabel20.setText(resourceMap.getString("jLabel20.text"));
        jLabel20.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLabel20.setName("jLabel20");
        jLabel21.setBackground(resourceMap.getColor("jLabel21.background"));
        jLabel21.setFont(resourceMap.getFont("jLabel21.font"));
        jLabel21.setText(resourceMap.getString("jLabel21.text"));
        jLabel21.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLabel21.setName("jLabel21");
        jLabel22.setFont(resourceMap.getFont("jLabel22.font"));
        jLabel22.setText(resourceMap.getString("jLabel22.text"));
        jLabel22.setName("jLabel22");
        emekliCheck.setName("emekliCheck");
        jLabel23.setFont(resourceMap.getFont("jLabel23.font"));
        jLabel23.setText(resourceMap.getString("jLabel23.text"));
        jLabel23.setName("jLabel23");
        tarihField.setName("tarihField");
        jLabel24.setFont(resourceMap.getFont("jLabel24.font"));
        jLabel24.setText(resourceMap.getString("jLabel24.text"));
        jLabel24.setName("jLabel24");
        ogrenciAdi.setName("ogrenciAdi");
        kaydet.setText(resourceMap.getString("kaydet.text"));
        kaydet.setName("kaydet");
        kaydet.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kaydetActionPerformed(evt);
            }
        });
        statuCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Burslu", "Arş.Gör.", "Diğer" }));
        statuCombo.setName("statuCombo");
        jLabel25.setFont(resourceMap.getFont("jLabel25.font"));
        jLabel25.setText(resourceMap.getString("jLabel25.text"));
        jLabel25.setName("jLabel25");
        tcRadio.setText(resourceMap.getString("tcRadio.text"));
        tcRadio.setName("tcRadio");
        egitimTuru.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yüksek Lisans", "Doktora" }));
        egitimTuru.setName("egitimTuru");
        egitimTuru.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                egitimTuruActionPerformed(evt);
            }
        });
        jLabel26.setFont(resourceMap.getFont("jLabel26.font"));
        jLabel26.setText(resourceMap.getString("jLabel26.text"));
        jLabel26.setName("jLabel26");
        jLabel27.setFont(resourceMap.getFont("jLabel27.font"));
        jLabel27.setText(resourceMap.getString("jLabel27.text"));
        jLabel27.setName("jLabel27");
        jLabel28.setFont(resourceMap.getFont("jLabel28.font"));
        jLabel28.setText(resourceMap.getString("jLabel28.text"));
        jLabel28.setName("jLabel28");
        dilekceTarihiDatePicker.setDateFormatString(resourceMap.getString("dilekceTarihiDatePicker.dateFormatString"));
        dilekceTarihiDatePicker.setName("dilekceTarihiDatePicker");
        jLabel29.setFont(resourceMap.getFont("jLabel29.font"));
        jLabel29.setText(resourceMap.getString("jLabel29.text"));
        jLabel29.setName("jLabel29");
        dilekceNoField.setText(resourceMap.getString("dilekceNoField.text"));
        dilekceNoField.setName("dilekceNoField");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(22, 22, 22).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel15).addComponent(jLabel3).addComponent(jLabel4).addComponent(jLabel5).addComponent(jLabel2).addComponent(jLabel8).addComponent(jLabel1).addComponent(jLabel11).addComponent(jLabel14).addComponent(jLabel9).addComponent(jLabel10).addComponent(jLabel13))).addGroup(layout.createSequentialGroup().addGap(27, 27, 27).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addGroup(layout.createSequentialGroup().addGap(1, 1, 1).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel16).addComponent(jLabel28).addComponent(jLabel29)).addGap(126, 126, 126).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(dilekceTarihiDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(kararNoField).addComponent(tarihField, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)).addComponent(dilekceNoField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))))).addGap(169, 169, 169)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(23, 23, 23).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel18).addComponent(jLabel25).addComponent(jLabel23)).addGap(97, 97, 97).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(emekliCheck).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addComponent(danismanUnvan, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(danisman, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(dekanlik, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))).addGroup(layout.createSequentialGroup().addGap(163, 163, 163).addComponent(kaydet, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel17).addComponent(jLabel27).addComponent(jLabel19).addComponent(jLabel12).addComponent(jLabel24).addComponent(jLabel26).addComponent(jLabel22).addComponent(jLabel7)).addGap(89, 89, 89).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(yonetmelikmadde, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(tcRadio).addGap(57, 57, 57).addComponent(yabanciRadio)).addComponent(ogrenciNo, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(programCombo, 0, 242, Short.MAX_VALUE).addComponent(ogrenciAdi, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE).addComponent(abdCombo, 0, 242, Short.MAX_VALUE).addComponent(egitimTuru, 0, 242, Short.MAX_VALUE).addComponent(statuCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGap(52, 52, 52))).addGap(23, 23, 23))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel6).addGroup(layout.createSequentialGroup().addComponent(tarihField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(4, 4, 4)).addComponent(jLabel3)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jLabel15)).addGroup(layout.createSequentialGroup().addGap(11, 11, 11).addComponent(jLabel16))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel28).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel29).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 186, Short.MAX_VALUE).addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel2).addGap(31, 31, 31).addComponent(jLabel4).addGap(51, 51, 51).addComponent(jLabel8).addGap(11, 11, 11).addComponent(jLabel11).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel14).addGap(96, 96, 96).addComponent(jLabel9).addGap(26, 26, 26).addComponent(jLabel10).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel13).addGap(76, 76, 76)).addGroup(layout.createSequentialGroup().addComponent(kararNoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(dilekceTarihiDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(dilekceNoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()))).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(148, Short.MAX_VALUE).addComponent(jLabel21).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel27).addComponent(ogrenciNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(ogrenciAdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel24)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel19).addComponent(abdCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(programCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel17)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel12).addComponent(egitimTuru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(11, 11, 11).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(statuCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel26)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tcRadio).addComponent(yabanciRadio).addComponent(jLabel22)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(yonetmelikmadde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel20).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel18).addComponent(danismanUnvan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(danisman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel25).addComponent(dekanlik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel23).addComponent(emekliCheck)).addGap(18, 18, 18).addComponent(kaydet).addGap(30, 30, 30))));
        pack();
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
        danismanUnvan.setSelectedIndex(0);
        yonetmelikmadde.setText("");
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        try {
            tarihField.setText(toplantiTarihi);
            String query = "SELECT isim FROM APP.ABDALI";
            Baglanti.createConnection();
            ResultSet result = Baglanti.runQuery(query);
            while (result.next()) abdComboModel.addElement(result.getObject(1));
            abdComboActionPerformed(null);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            tarihField.setText(toplantiTarihi);
            egitimTuruActionPerformed(null);
            tcRadio.setSelected(true);
        }
    }

    private void abdComboActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String abd = abdCombo.getSelectedItem().toString();
            String query = "SELECT P.isim FROM APP.PROGRAMLAR P WHERE P.abdID=(SELECT id FROM APP.ABDALI WHERE isim LIKE '" + abd + "') ORDER BY P.isim";
            Baglanti.createConnection();
            ResultSet resultSet = Baglanti.runQuery(query);
            programComboModel.removeAllElements();
            while (resultSet.next()) programComboModel.addElement(resultSet.getObject(1));
            programComboModel.addElement("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hata:" + e.getMessage());
        }
    }

    private void danismanActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void danismanKeyTyped(java.awt.event.KeyEvent evt) {
        String value = danisman.getText();
        if (value.length() >= 1) dekanlik.setEnabled(true); else if (value.length() == 0) {
            dekanlik.setSelectedIndex(0);
            dekanlik.setEnabled(false);
        }
    }

    private String getDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dilekceTarihiDatePicker.getDateFormatString());
        String date = dateFormat.format(dilekceTarihiDatePicker.getDate());
        return date;
    }

    private void kaydetActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String ogrenci = ogrenciAdi.getText();
            String anabilimDali = abdCombo.getSelectedItem().toString();
            String bolum = programCombo.getSelectedItem().toString();
            String statu = statuCombo.getSelectedItem().toString();
            String dilekceTarihi = getDateString();
            int dilekceNo = Integer.parseInt(dilekceNoField.getText());
            if (kararNoField.getText().length() == 0) kararNo = 0; else kararNo = Integer.parseInt(kararNoField.getText());
            String yonetmelikMadde = new String();
            yonetmelikMadde = yonetmelikmadde.getText();
            byte isRetired = emekliCheck.isSelected() ? (byte) 1 : (byte) 0;
            byte isForeign = yabanciRadio.isSelected() ? (byte) 1 : (byte) 0;
            String programTipi = egitimTuru.getSelectedItem().toString();
            String tarih = tarihField.getText();
            String dekan = dekanlik.getSelectedItem().toString();
            String danismanAdi = danisman.getText().isEmpty() ? "" : danismanUnvan.getSelectedItem().toString() + danisman.getText();
            StringBuffer query = new StringBuffer();
            query.append("INSERT INTO APP.DANISMAN(adi,statu,abd,tarih,tur,ogrenciNo,danisman,danismanEmekli,yabanci,bolum,yonetmelikMadde,dekanlik,toplantiSayisi,kararNo,dilekceNo,dilekceTarihi)");
            query.append(" VALUES('" + ogrenci + "',");
            query.append("'" + statu + "',");
            query.append("'" + anabilimDali + "',");
            query.append("'" + tarih + "',");
            query.append("'" + programTipi + "',");
            query.append("'" + ogrenciNo.getText() + "',");
            query.append("'" + danismanAdi + "',");
            query.append(isRetired + ",");
            query.append(isForeign + ",");
            query.append("'" + bolum + "',");
            query.append("'" + yonetmelikMadde + "',");
            query.append("'" + dekan + "',");
            query.append(toplantiSayisi + ",");
            query.append(kararNo + ",");
            query.append(dilekceNo + ",");
            query.append("'" + dilekceTarihi + "'");
            query.append(")");
            Baglanti.createConnection();
            Baglanti.insertQuery(query.toString());
            int flag = JOptionPane.showConfirmDialog(this, "Yeni kayıt?", "Kaydedildi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (flag == JOptionPane.YES_OPTION) {
                clearFields();
            } else {
                ciktiSecenekFrame form = new ciktiSecenekFrame(toplantiSayisi);
                form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                form.setTitle("Çıktı Seçenekleri");
                this.setVisible(false);
                form.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Hata:" + e.getMessage());
        }
    }

    private void egitimTuruActionPerformed(java.awt.event.ActionEvent evt) {
        String tur = egitimTuru.getSelectedItem().toString();
        if (tur.contains("Doktora")) yonetmelikmadde.setText("39"); else if (tur.contains("Tezsiz")) yonetmelikmadde.setText("34"); else if (tur.contains("Tezli")) yonetmelikmadde.setText("30");
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new danismanGiris().setVisible(true);
            }
        });
    }

    private javax.swing.JComboBox abdCombo;

    private javax.swing.JTextField danisman;

    private javax.swing.JComboBox danismanUnvan;

    private javax.swing.JComboBox dekanlik;

    private javax.swing.JTextField dilekceNoField;

    private com.toedter.calendar.JDateChooser dilekceTarihiDatePicker;

    private javax.swing.JComboBox egitimTuru;

    private javax.swing.JCheckBox emekliCheck;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel24;

    private javax.swing.JLabel jLabel25;

    private javax.swing.JLabel jLabel26;

    private javax.swing.JLabel jLabel27;

    private javax.swing.JLabel jLabel28;

    private javax.swing.JLabel jLabel29;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JTextField kararNoField;

    private javax.swing.JButton kaydet;

    private javax.swing.JTextField ogrenciAdi;

    private javax.swing.JTextField ogrenciNo;

    private javax.swing.JComboBox programCombo;

    private javax.swing.JComboBox statuCombo;

    private javax.swing.JTextField tarihField;

    private javax.swing.JRadioButton tcRadio;

    private javax.swing.JRadioButton yabanciRadio;

    private javax.swing.JTextField yonetmelikmadde;
}
