package kayitsilme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.JdbcRowSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import sosbilens.Baglanti;
import sosbilens.ciktiSecenekFrame;

public class kayitSilGuncelle extends JFrame {

    /** Creates new form kayitSilGuncelle */
    private DefaultComboBoxModel programComboModel;

    private DefaultComboBoxModel abdComboModel;

    private DefaultComboBoxModel dekanlikModel;

    private int toplantiSayisi;

    private String toplantiTarihi;

    private ResultSet result;

    private int kararNo;

    private JdbcRowSet rowSet;

    public kayitSilGuncelle() {
        abdComboModel = new DefaultComboBoxModel();
        dekanlikModel = new DefaultComboBoxModel();
        initComponents();
        programComboModel = new DefaultComboBoxModel();
        programCombo.setModel(programComboModel);
        dekanlik.setModel(dekanlikModel);
        kaydet.setVisible(false);
        dekanlik.setEnabled(false);
    }

    public kayitSilGuncelle(int toplantiSayisi) {
        this.toplantiSayisi = toplantiSayisi;
        abdComboModel = new DefaultComboBoxModel();
        initComponents();
        programComboModel = new DefaultComboBoxModel();
        programCombo.setModel(programComboModel);
        dekanlikModel = new DefaultComboBoxModel();
        dekanlik.setModel(dekanlikModel);
        this.toplantiSayisi = toplantiSayisi;
        kaydet.setVisible(false);
        dekanlik.setEnabled(false);
    }

    public kayitSilGuncelle(int toplantiSayisi, String tarih) {
        this.toplantiSayisi = toplantiSayisi;
        abdComboModel = new DefaultComboBoxModel();
        initComponents();
        programComboModel = new DefaultComboBoxModel();
        programCombo.setModel(programComboModel);
        dekanlikModel = new DefaultComboBoxModel();
        dekanlik.setModel(dekanlikModel);
        this.toplantiSayisi = toplantiSayisi;
        kaydet.setVisible(false);
        dekanlik.setEnabled(false);
        toplantiTarihi = tarih;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel8 = new javax.swing.JLabel();
        emekliCheck = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        yabanciRadio = new javax.swing.JRadioButton();
        egitimTuru = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tcRadio = new javax.swing.JRadioButton();
        programCombo = new javax.swing.JComboBox();
        abdCombo = new javax.swing.JComboBox();
        cb27j = new javax.swing.JCheckBox();
        cb27i = new javax.swing.JCheckBox();
        cb27ih = new javax.swing.JCheckBox();
        tarihField = new javax.swing.JTextField();
        cb27h = new javax.swing.JCheckBox();
        danisman = new javax.swing.JTextField();
        cb27gh = new javax.swing.JCheckBox();
        cb27f = new javax.swing.JCheckBox();
        cb27g = new javax.swing.JCheckBox();
        ogrenciNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cb27b = new javax.swing.JCheckBox();
        guncelle = new javax.swing.JButton();
        cb27c = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        cb27ch = new javax.swing.JCheckBox();
        cb27d = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cb27e = new javax.swing.JCheckBox();
        statuCombo = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        ogrenciAdi = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cb27k = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cb27a = new javax.swing.JCheckBox();
        kendiIstegiCheckBox = new javax.swing.JCheckBox();
        yeni = new javax.swing.JButton();
        kaydet = new javax.swing.JButton();
        danismanUnvan = new javax.swing.JComboBox();
        geriButonu = new javax.swing.JButton();
        ileriButonu = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        kararNoField = new javax.swing.JTextField();
        dekanlik = new javax.swing.JComboBox();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sosbilens.SosBilEnsApp.class).getContext().getResourceMap(kayitSilGuncelle.class);
        jLabel8.setFont(resourceMap.getFont("jLabel8.font"));
        jLabel8.setText(resourceMap.getString("jLabel8.text"));
        jLabel8.setName("jLabel8");
        emekliCheck.setName("emekliCheck");
        jLabel3.setFont(resourceMap.getFont("jLabel3.font"));
        jLabel3.setText(resourceMap.getString("jLabel3.text"));
        jLabel3.setName("jLabel3");
        jLabel13.setFont(resourceMap.getFont("jLabel13.font"));
        jLabel13.setText(resourceMap.getString("jLabel13.text"));
        jLabel13.setName("jLabel13");
        jLabel5.setFont(resourceMap.getFont("jLabel5.font"));
        jLabel5.setText(resourceMap.getString("jLabel5.text"));
        jLabel5.setName("jLabel5");
        buttonGroup1.add(yabanciRadio);
        yabanciRadio.setText(resourceMap.getString("yabanciRadio.text"));
        yabanciRadio.setName("yabanciRadio");
        egitimTuru.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tezli Yüksek Lisans", "Tezsiz Yüksek Lisans", "Doktora", "Birleşik Doktora", "2.Öğretim" }));
        egitimTuru.setName("egitimTuru");
        egitimTuru.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                egitimTuruActionPerformed(evt);
            }
        });
        jLabel14.setFont(resourceMap.getFont("jLabel14.font"));
        jLabel14.setText(resourceMap.getString("jLabel14.text"));
        jLabel14.setName("jLabel14");
        jLabel4.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel4.setText(resourceMap.getString("jLabel4.text"));
        jLabel4.setName("jLabel4");
        buttonGroup1.add(tcRadio);
        tcRadio.setText(resourceMap.getString("tcRadio.text"));
        tcRadio.setName("tcRadio");
        programCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        programCombo.setName("programCombo");
        abdCombo.setModel(abdComboModel);
        abdCombo.setName("abdCombo");
        abdCombo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abdComboActionPerformed(evt);
            }
        });
        cb27j.setText(resourceMap.getString("cb27j.text"));
        cb27j.setName("cb27j");
        cb27i.setText(resourceMap.getString("cb27i.text"));
        cb27i.setName("cb27i");
        cb27ih.setText(resourceMap.getString("cb27ih.text"));
        cb27ih.setName("cb27ih");
        tarihField.setName("tarihField");
        cb27h.setText(resourceMap.getString("cb27h.text"));
        cb27h.setName("cb27h");
        danisman.setName("danisman");
        danisman.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                danismanKeyTyped(evt);
            }
        });
        cb27gh.setText(resourceMap.getString("cb27gh.text"));
        cb27gh.setName("cb27gh");
        cb27f.setText(resourceMap.getString("cb27f.text"));
        cb27f.setName("cb27f");
        cb27g.setText(resourceMap.getString("cb27g.text"));
        cb27g.setName("cb27g");
        ogrenciNo.setName("ogrenciNo");
        jLabel6.setFont(resourceMap.getFont("jLabel6.font"));
        jLabel6.setText(resourceMap.getString("jLabel6.text"));
        jLabel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLabel6.setName("jLabel6");
        cb27b.setText(resourceMap.getString("cb27b.text"));
        cb27b.setName("cb27b");
        guncelle.setText(resourceMap.getString("guncelle.text"));
        guncelle.setName("guncelle");
        guncelle.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guncelleActionPerformed(evt);
            }
        });
        cb27c.setText(resourceMap.getString("cb27c.text"));
        cb27c.setName("cb27c");
        jLabel2.setFont(resourceMap.getFont("jLabel2.font"));
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        cb27ch.setText(resourceMap.getString("cb27ch.text"));
        cb27ch.setName("cb27ch");
        cb27d.setText(resourceMap.getString("cb27d.text"));
        cb27d.setName("cb27d");
        jLabel1.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        jLabel11.setFont(resourceMap.getFont("jLabel11.font"));
        jLabel11.setText(resourceMap.getString("jLabel11.text"));
        jLabel11.setName("jLabel11");
        cb27e.setText(resourceMap.getString("cb27e.text"));
        cb27e.setName("cb27e");
        statuCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Burslu", "Arş.Gör.", "Diğer" }));
        statuCombo.setName("statuCombo");
        jLabel10.setFont(resourceMap.getFont("jLabel10.font"));
        jLabel10.setText(resourceMap.getString("jLabel10.text"));
        jLabel10.setName("jLabel10");
        ogrenciAdi.setName("ogrenciAdi");
        jLabel9.setFont(resourceMap.getFont("jLabel9.font"));
        jLabel9.setText(resourceMap.getString("jLabel9.text"));
        jLabel9.setName("jLabel9");
        cb27k.setText(resourceMap.getString("cb27k.text"));
        cb27k.setName("cb27k");
        jLabel12.setFont(resourceMap.getFont("jLabel12.font"));
        jLabel12.setText(resourceMap.getString("jLabel12.text"));
        jLabel12.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLabel12.setName("jLabel12");
        jLabel7.setFont(resourceMap.getFont("jLabel7.font"));
        jLabel7.setText(resourceMap.getString("jLabel7.text"));
        jLabel7.setName("jLabel7");
        cb27a.setText(resourceMap.getString("cb27a.text"));
        cb27a.setName("cb27a");
        kendiIstegiCheckBox.setText(resourceMap.getString("kendiIstegiCheckBox.text"));
        kendiIstegiCheckBox.setName("kendiIstegiCheckBox");
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
        danismanUnvan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Prof.Dr.", "Doç.Dr.", "Yrd.Doç.Dr." }));
        danismanUnvan.setName("danismanUnvan");
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
        jLabel15.setFont(resourceMap.getFont("jLabel15.font"));
        jLabel15.setText(resourceMap.getString("jLabel15.text"));
        jLabel15.setName("jLabel15");
        kararNoField.setName("kararNoField");
        dekanlik.setEnabled(false);
        dekanlik.setName("dekanlik");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel9).addComponent(jLabel10).addComponent(yeni, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(6, 6, 6).addComponent(geriButonu, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(guncelle, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(ileriButonu, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(21, 21, 21).addComponent(kaydet, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(emekliCheck).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(dekanlik, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(danismanUnvan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(danisman, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))))).addComponent(jLabel13).addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel12).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel4).addComponent(jLabel5).addComponent(jLabel2).addComponent(jLabel8).addComponent(jLabel1).addComponent(jLabel11).addComponent(jLabel14).addComponent(jLabel7).addComponent(jLabel3)).addGap(89, 89, 89).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(cb27a).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cb27b).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cb27c).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cb27ch)).addGroup(layout.createSequentialGroup().addComponent(cb27d).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cb27e).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cb27f)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(cb27j).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(cb27k)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(cb27gh).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cb27h))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(cb27ih).addGap(2, 2, 2).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cb27g).addComponent(cb27i))).addComponent(kendiIstegiCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))).addGroup(layout.createSequentialGroup().addComponent(tcRadio).addGap(57, 57, 57).addComponent(yabanciRadio)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(ogrenciNo, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(programCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(ogrenciAdi).addComponent(abdCombo, 0, 236, Short.MAX_VALUE).addComponent(egitimTuru, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(statuCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(kararNoField, javax.swing.GroupLayout.Alignment.LEADING).addComponent(tarihField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))))).addContainerGap()).addComponent(jLabel15))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(26, 26, 26).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel3).addComponent(tarihField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(22, 22, 22).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel15).addComponent(kararNoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(15, 15, 15).addComponent(jLabel12).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel5).addComponent(ogrenciNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(ogrenciAdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addComponent(abdCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(programCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel8).addComponent(egitimTuru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(11, 11, 11).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(statuCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel11)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tcRadio).addComponent(yabanciRadio).addComponent(jLabel14)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cb27a).addComponent(cb27b).addComponent(cb27c).addComponent(cb27ch)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cb27d).addComponent(cb27e).addComponent(cb27f)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cb27gh).addComponent(cb27h).addComponent(cb27ih))).addGroup(layout.createSequentialGroup().addComponent(cb27g, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cb27i)))).addGroup(layout.createSequentialGroup().addGap(35, 35, 35).addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cb27j, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cb27k, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(kendiIstegiCheckBox)).addGap(38, 38, 38).addComponent(jLabel6).addGap(22, 22, 22).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel9).addComponent(danismanUnvan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(danisman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel10).addComponent(dekanlik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel13).addComponent(emekliCheck)).addGap(12, 12, 12).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(yeni).addComponent(geriButonu).addComponent(guncelle).addComponent(ileriButonu).addComponent(kaydet)).addContainerGap(15, Short.MAX_VALUE)));
        pack();
    }

    private void egitimTuruActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void abdComboActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String abd = abdCombo.getSelectedItem().toString();
            String query = "SELECT P.isim FROM APP.PROGRAMLAR P WHERE P.abdID=(SELECT id FROM APP.ABDALI WHERE isim LIKE '" + abd + "') ORDER BY P.isim";
            Baglanti.createConnection();
            ResultSet resultSet = Baglanti.runQuery(query);
            programComboModel.removeAllElements();
            while (resultSet.next()) programComboModel.addElement(resultSet.getObject(1));
            programComboModel.addElement(" ");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hata:" + e.getMessage());
        }
    }

    private void guncelleActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (kararNoField.getText().length() == 0) kararNo = 0; else kararNo = Integer.parseInt(kararNoField.getText());
            String ogrenci = ogrenciAdi.getText();
            String anabilimDali = abdCombo.getSelectedItem().toString();
            String bolum = programCombo.getSelectedItem().toString();
            String statu = statuCombo.getSelectedItem().toString();
            StringBuffer yonetmelik = new StringBuffer();
            int ilkYonetmelik = 0;
            byte isRetired = emekliCheck.isSelected() ? (byte) 1 : (byte) 0;
            byte isForeign = yabanciRadio.isSelected() ? (byte) 1 : (byte) 0;
            if (cb27a.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/a");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/a");
            }
            if (cb27b.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/b");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/b");
            }
            if (cb27c.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/c");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/c");
            }
            if (cb27ch.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/ç");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/ç");
            }
            if (cb27d.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/d");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/d");
            }
            if (cb27e.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/e");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/e");
            }
            if (cb27f.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/f");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/f");
            }
            if (cb27g.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/g");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/g");
            }
            if (cb27gh.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/ğ");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/ğ");
            }
            if (cb27h.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/h");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/h");
            }
            if (cb27ih.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/ı");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/ı");
            }
            if (cb27i.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/i");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/i");
            }
            if (cb27j.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/j");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/j");
            }
            if (cb27k.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/k");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/k");
            }
            if (kendiIstegiCheckBox.isSelected()) yonetmelik.replace(0, yonetmelik.length(), "Kendi İsteği");
            String tarih = tarihField.getText();
            String dekan = dekanlik.getSelectedItem().toString();
            String danismanAdi = danisman.getText().isEmpty() ? "" : danismanUnvan.getSelectedItem().toString() + danisman.getText();
            String programTipi = "";
            if (egitimTuru.getSelectedItem().toString().contentEquals("Tezli Yüksek Lisans")) programTipi = "YL"; else if (egitimTuru.getSelectedItem().toString().contentEquals("Tezsiz Yüksek Lisans")) programTipi = "Tezsiz YL"; else if (egitimTuru.getSelectedItem().toString().contentEquals("2.Öğretim Tezsiz YL")) programTipi = "2.Öğr.Tezsiz YL"; else if (egitimTuru.getSelectedItem().toString().contentEquals("Doktora")) programTipi = "DR"; else if (egitimTuru.getSelectedItem().toString().contentEquals("Birleşik Doktora")) programTipi = "Birleşik DR";
            StringBuilder query = new StringBuilder();
            query.append("UPDATE APP.KAYITSIL SET ");
            query.append("adi='" + ogrenci);
            query.append("',statu='" + statu);
            query.append("',abd='" + anabilimDali);
            query.append("',tarih='" + tarih);
            query.append("',tur='" + programTipi);
            query.append("',ogrenciNo='" + ogrenciNo.getText());
            query.append("',danisman='" + danismanAdi);
            query.append("',danismanEmekli=" + isRetired);
            query.append(",yabanci=" + isForeign);
            query.append(",bolum='" + bolum);
            query.append("',kararNo=" + kararNo);
            query.append(",yonetmelikMaddeleri='" + yonetmelik);
            query.append("',dekanlik='" + dekan + "' WHERE toplantiSayisi=" + toplantiSayisi);
            query.append(" AND ogrenciNo LIKE '" + ogrenciNo.getText() + "'");
            Baglanti.createConnection();
            Baglanti.insertQuery(query.toString());
            JOptionPane.showMessageDialog(null, "Güncelleme işlemi yapıldı");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Hata: " + e.getMessage());
        }
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        try {
            String[] list = { " ", "DİL VE TARİH COĞRAFYA", "HUKUK", "İLAHİYAT", "İLETİŞİM", "SİYASAL BİLGİLER" };
            for (String e : list) dekanlikModel.addElement(e);
            StringBuffer query = new StringBuffer();
            query.append("SELECT * FROM APP.KAYITSIL");
            query.append(" WHERE toplantiSayisi=" + toplantiSayisi);
            Baglanti.createConnection();
            result = Baglanti.runQuery(query.toString());
            if (result.next()) veriGoster(); else {
                yeni.doClick();
            }
            tcRadio.setSelected(true);
            tarihField.setText(toplantiTarihi);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, "Hata:" + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Hata:" + e.getMessage());
        }
    }

    private void kaydetActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (kararNoField.getText().length() == 0) kararNo = 0; else kararNo = Integer.parseInt(kararNoField.getText());
            String ogrenci = ogrenciAdi.getText();
            String anabilimDali = abdCombo.getSelectedItem().toString();
            String bolum = programCombo.getSelectedItem().toString();
            String statu = statuCombo.getSelectedItem().toString();
            StringBuffer yonetmelik = new StringBuffer();
            int ilkYonetmelik = 0;
            byte isRetired = emekliCheck.isSelected() ? (byte) 1 : (byte) 0;
            byte isForeign = yabanciRadio.isSelected() ? (byte) 1 : (byte) 0;
            if (cb27a.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/a");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/a");
            }
            if (cb27b.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/b");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/b");
            }
            if (cb27c.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/c");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/c");
            }
            if (cb27ch.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/ç");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/ç");
            }
            if (cb27d.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/d");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/d");
            }
            if (cb27e.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/e");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/e");
            }
            if (cb27f.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/f");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/f");
            }
            if (cb27g.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/g");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/g");
            }
            if (cb27gh.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/ğ");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/ğ");
            }
            if (cb27h.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/h");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/h");
            }
            if (cb27ih.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/ı");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/ı");
            }
            if (cb27i.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/i");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/i");
            }
            if (cb27j.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/j");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/j");
            }
            if (cb27k.isSelected()) {
                if (ilkYonetmelik == 0) {
                    yonetmelik.append("27/k");
                    ilkYonetmelik = 1;
                } else yonetmelik.append(",27/k");
            }
            if (kendiIstegiCheckBox.isSelected()) yonetmelik.replace(0, yonetmelik.length(), "Kendi İsteği");
            String tarih = tarihField.getText();
            String dekan = dekanlik.getSelectedItem().toString();
            String danismanAdi = danisman.getText().isEmpty() ? "" : danismanUnvan.getSelectedItem().toString() + danisman.getText();
            String programTipi = "";
            if (egitimTuru.getSelectedItem().toString().contentEquals("Tezli Yüksek Lisans")) programTipi = "YL"; else if (egitimTuru.getSelectedItem().toString().contentEquals("Tezsiz Yüksek Lisans")) programTipi = "Tezsiz YL"; else if (egitimTuru.getSelectedItem().toString().contentEquals("2.Öğretim Tezsiz YL")) programTipi = "2.Öğr.Tezsiz YL"; else if (egitimTuru.getSelectedItem().toString().contentEquals("Doktora")) programTipi = "DR"; else if (egitimTuru.getSelectedItem().toString().contentEquals("Birleşik Doktora")) programTipi = "Birleşik DR";
            StringBuffer query = new StringBuffer();
            query.append("INSERT INTO APP.KAYITSIL(adi,statu,abd,tarih,tur,ogrenciNo,danisman,danismanEmekli,yabanci,bolum,yonetmelikMaddeleri,dekanlik,toplantiSayisi,kararNo)");
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
            query.append("'" + yonetmelik + "',");
            query.append("'" + dekan + "',");
            query.append(toplantiSayisi + ",");
            query.append(kararNo + ")");
            Baglanti.createConnection();
            Baglanti.insertQuery(query.toString());
            int flag = JOptionPane.showConfirmDialog(this, "Yeni Kayıt?", "Kaydedildi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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

    private void yeniActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            guncelle.setVisible(false);
            kaydet.setVisible(true);
            ileriButonu.setVisible(false);
            geriButonu.setVisible(false);
            String query = "SELECT isim FROM APP.ABDALI";
            Baglanti.createConnection();
            ResultSet abResult = Baglanti.runQuery(query);
            while (abResult.next()) {
                abdComboModel.addElement(abResult.getObject(1));
            }
            abdComboModel.addElement(" ");
            abdComboActionPerformed(null);
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
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

    private void danismanKeyTyped(java.awt.event.KeyEvent evt) {
        String value = danisman.getText();
        if (value.length() >= 1) dekanlik.setEnabled(true); else if (value.length() == 0) {
            dekanlik.setSelectedIndex(0);
            dekanlik.setEnabled(false);
        }
    }

    private int kararNoHesapla() throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT MAX(kararNo) FROM APP.KAYITSIL");
        query.append(" WHERE toplantiSayisi=" + toplantiSayisi);
        Baglanti.createConnection();
        ResultSet rs = Baglanti.runQuery(query.toString());
        if (rs.next()) {
            int no = rs.getInt(1);
            return no;
        }
        int juriKararNo;
        query.delete(0, query.length());
        query.append("SELECT MAX(SIRA) FROM APP.OGRENCI");
        query.append(" WHERE toplantiSayisi=" + toplantiSayisi);
        Baglanti.createConnection();
        ResultSet rs2 = Baglanti.runQuery(query.toString());
        if (rs2.next()) juriKararNo = rs2.getInt(1); else juriKararNo = 0;
        return (juriKararNo + 1);
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
        cb27a.setSelected(false);
        cb27b.setSelected(false);
        cb27c.setSelected(false);
        cb27ch.setSelected(false);
        cb27d.setSelected(false);
        cb27e.setSelected(false);
        cb27f.setSelected(false);
        cb27g.setSelected(false);
        cb27gh.setSelected(false);
        cb27ih.setSelected(false);
        cb27h.setSelected(false);
        cb27i.setSelected(false);
        cb27j.setSelected(false);
        cb27k.setSelected(false);
        kendiIstegiCheckBox.setSelected(false);
    }

    private void veriGoster() {
        try {
            danisman.setText("");
            ogrenciAdi.setText(result.getString("adi"));
            ogrenciNo.setText(result.getString("ogrenciNo"));
            kararNoField.setText(result.getString("kararNo"));
            String tarih = result.getString("tarih");
            String yonetmelik = result.getString("yonetmelikMaddeleri");
            String abd = result.getString("abd");
            tarihField.setText(tarih);
            String programTipi = result.getString("tur");
            if (programTipi.contentEquals("Tezli YL")) {
                egitimTuru.setSelectedIndex(0);
            } else if (programTipi.contentEquals("Tezsiz YL")) {
                egitimTuru.setSelectedIndex(1);
            } else if (programTipi.contentEquals("DR")) {
                egitimTuru.setSelectedIndex(2);
            } else if (programTipi.contentEquals("Birleşik DR")) {
                egitimTuru.setSelectedIndex(3);
            } else if (programTipi.contentEquals("2.Öğr.Tezsiz YL")) {
                egitimTuru.setSelectedIndex(4);
            }
            String statu = result.getString("statu");
            if (statu.contains("Burslu")) {
                statuCombo.setSelectedIndex(0);
            } else if (statu.contains("Arş.")) {
                statuCombo.setSelectedIndex(1);
            } else {
                statuCombo.setSelectedIndex(2);
            }
            String program = result.getString("bolum");
            String query = "SELECT isim FROM APP.ABDALI";
            Baglanti.createConnection();
            ResultSet abResult = Baglanti.runQuery(query);
            while (abResult.next()) abdComboModel.addElement(abResult.getObject(1));
            abdComboModel.addElement(" ");
            abdComboActionPerformed(null);
            int abdIndex = abdComboModel.getIndexOf(abd);
            abdCombo.setSelectedIndex(abdIndex);
            int programIndex = programComboModel.getIndexOf(program);
            programCombo.setSelectedIndex(programIndex);
            if (result.getInt("yabanci") == 1) {
                yabanciRadio.setSelected(true);
            } else {
                tcRadio.setSelected(true);
            }
            StringTokenizer yonetmelikTok = new StringTokenizer(yonetmelik);
            while (yonetmelikTok.hasMoreTokens()) {
                String s = yonetmelikTok.nextToken(",");
                if (s.contains("Kendi İsteği")) {
                    kendiIstegiCheckBox.setSelected(true);
                } else if (s.contains("a")) {
                    cb27a.setSelected(true);
                } else if (s.contains("b")) {
                    cb27b.setSelected(true);
                } else if (s.contains("c")) {
                    cb27c.setSelected(true);
                } else if (s.contains("ç")) {
                    cb27ch.setSelected(true);
                } else if (s.contains("d")) {
                    cb27d.setSelected(true);
                } else if (s.contains("e")) {
                    cb27e.setSelected(true);
                } else if (s.contains("f")) {
                    cb27f.setSelected(true);
                } else if (s.contains("27/g")) {
                    cb27g.setSelected(true);
                } else if (s.contains("27/ğ")) {
                    cb27gh.setSelected(true);
                } else if (s.contains("h")) {
                    cb27h.setSelected(true);
                } else if (s.contains("ı")) {
                    cb27ih.setSelected(true);
                } else if (s.contains("i")) {
                    cb27i.setSelected(true);
                } else if (s.contains("j")) {
                    cb27j.setSelected(true);
                } else if (s.contains("k")) {
                    cb27k.setSelected(true);
                }
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
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new kayitSilGuncelle().setVisible(true);
            }
        });
    }

    private javax.swing.JComboBox abdCombo;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JCheckBox cb27a;

    private javax.swing.JCheckBox cb27b;

    private javax.swing.JCheckBox cb27c;

    private javax.swing.JCheckBox cb27ch;

    private javax.swing.JCheckBox cb27d;

    private javax.swing.JCheckBox cb27e;

    private javax.swing.JCheckBox cb27f;

    private javax.swing.JCheckBox cb27g;

    private javax.swing.JCheckBox cb27gh;

    private javax.swing.JCheckBox cb27h;

    private javax.swing.JCheckBox cb27i;

    private javax.swing.JCheckBox cb27ih;

    private javax.swing.JCheckBox cb27j;

    private javax.swing.JCheckBox cb27k;

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

    private javax.swing.JTextField kararNoField;

    private javax.swing.JButton kaydet;

    private javax.swing.JCheckBox kendiIstegiCheckBox;

    private javax.swing.JTextField ogrenciAdi;

    private javax.swing.JTextField ogrenciNo;

    private javax.swing.JComboBox programCombo;

    private javax.swing.JComboBox statuCombo;

    private javax.swing.JTextField tarihField;

    private javax.swing.JRadioButton tcRadio;

    private javax.swing.JRadioButton yabanciRadio;

    private javax.swing.JButton yeni;
}
