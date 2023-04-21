package sosbilens;

import danisman.danismanAtamaABDListesi;
import java.sql.SQLException;
import juri.ogrenciListesiFrame;
import kayitsilme.kayitSilmeDekanlikListesi;
import kayitsilme.kayitSilmeABDListesi;
import kayitsilme.kayitSilOgrenciListesi;
import mezuniyet.mezunDekanListe;
import mezuniyet.mezunAbdListe;
import mezuniyet.mezunOgrenciListesi;
import danisman.danismanDekanlikListesi;
import danisman.degistirme.danismanDegistirmeAbdListe;
import danisman.degistirme.danismanDegistirmeDekanlikListesi;
import eksure.eksureOgrenciListesi;
import ikinciDanisman.ikinciDanismanAtamaABDListesi;
import ikinciDanisman.ikinciDanismanDekanlikListesi;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import tez.tezIzlemeKomitesi.tezIzlemeKomitesiBelirlemeAbdListe;
import tez.tezIzlemeKomitesi.tezIzlemeKomitesiBelirlemeOgrenciListesi;
import tez.tezIzlemeKomitesiDegistirme.tezIzlemeKomitesiDegistirmeAbdListe;
import tez.tezIzlemeKomitesiDegistirme.tezIzlemeKomitesiDegistirmeOgrenciListesi;
import tez.tezKonusuBelirleme.tezKonusuBelirlemeAbdListe;
import tez.tezKonusuBelirleme.tezKonusuBelirlemeDekanlikListesi;
import tez.tezKonusuDegistirme.tezKonusuDegistirmeAbdListe;
import tez.tezKonusuDegistirme.tezKonusuDegistirmeDekanlikListesi;

public class ciktiSecenekFrame extends javax.swing.JFrame {

    private String secenek;

    private int toplantiID;

    private StringBuilder subreport_path;

    private boolean vekil = false;

    public ciktiSecenekFrame() {
        initComponents();
    }

    public ciktiSecenekFrame(int toplantiSayi) {
        initComponents();
        toplantiID = toplantiSayi;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        buton = new javax.swing.JButton();
        imzaYetkiliCombo = new javax.swing.JComboBox();
        vekilCheckBox = new javax.swing.JCheckBox();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jScrollPane1.setName("jScrollPane1");
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("İŞLEMLER");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Yönetim Kurulu Gündemi Ön Sayfa");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Yönetim Kurulu Gündemi Çıktısı");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Y.Lisans Doktora Tez Jürisi Çıktıları");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Tez Jürisi Çıktıları");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Yüksek Lisans Gündem Çıktısı");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Doktora Gündem Çıktısı");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Mezuniyet Çıktıları");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Öğrenci Çıktıları");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Anabilim Dalları Çıktısı");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Dekanlık Çıktısı");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Diğer Çıktılar");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Gündem Çıktısı");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Kayıt Silme Çıktıları");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Öğrenci Çıktıları");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Anabilim Dalları Çıktısı");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Dekanlık Çıktısı");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Diğer Çıktılar");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Gündem Çıktıs");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Danışman Çıktıları");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Danışman Atama Çıktıları");
        javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Öğrenci İşleri Çıktısı");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Dekanlık Çıktıları");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Anabilim Dalları Çıktısı");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Gündem Çıktısı");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Danışman Değiştirme Çıktısı");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Öğrenci İşleri Çıktısı");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Dekanlık Çıktıları");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Anabilim Dalları Çıktısı");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Gündem Çıktısı");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("İkinci Danışman Çıktıları");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Öğrenci İşleri Çıktısı");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Dekanlık Çıktıları");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Anabilim Dalları Çıktısı");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Gündem Çıktısı");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Eksüre Çıktıları");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Öğrenci İşleri Çıktısı");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Anabilim Dalları Çıktısı");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Gündem Çıktısı");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Tez İzleme Komitesi");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Tez İzleme Komitesi Belirleme");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Anabilim Dalı Çıktıları");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Diğer Çıktılar");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Gündem Çıktısı");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Tez İzleme Komitesi Değiştirme");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Anabilim Dalı Çıktıları");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Diğer Çıktılar");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Gündem Çıktısı");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Tez");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Tez Konusu Belirleme");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Öğrenci İşleri Çıktısı");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Dekanlık Çıktıları");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Anabilim Dalları Çıktısı");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Gündem Çıktısı");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Tez Konusu Değiştirme");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Öğrenci İşleri Çıktısı");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Dekanlık Çıktıları");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Anabilim Dalları Çıktısı");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Gündem Çıktısı");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Tez Konusu ve Danışman Değiştirme");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Öğrenci İşleri Çıktısı");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Dekanlık Çıktıları");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Anabilim Dalları Çıktısı");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Gündem Çıktısı");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.setName("jTree1");
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sosbilens.SosBilEnsApp.class).getContext().getResourceMap(ciktiSecenekFrame.class);
        buton.setText(resourceMap.getString("buton.text"));
        buton.setName("buton");
        buton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butonActionPerformed(evt);
            }
        });
        imzaYetkiliCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Prof.Dr.Çınar ÖZEN", "Doç. Dr. Seda G. GÖKMEN", "Yrd. Doç. Dr. M. Arcan TUZCU" }));
        imzaYetkiliCombo.setName("imzaYetkiliCombo");
        vekilCheckBox.setText(resourceMap.getString("vekilCheckBox.text"));
        vekilCheckBox.setName("vekilCheckBox");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(imzaYetkiliCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(vekilCheckBox).addGap(18, 18, 18).addComponent(buton, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(imzaYetkiliCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(vekilCheckBox).addComponent(buton)).addContainerGap()));
        pack();
    }

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        secenek = evt.getPath().toString();
        System.out.print(secenek);
    }

    private void getPath() {
        File directory = new File("");
        String path = directory.getAbsolutePath();
        StringTokenizer st = new StringTokenizer(path, "\\");
        subreport_path = new StringBuilder();
        while (st.hasMoreTokens()) {
            subreport_path.append(st.nextToken() + "\\");
        }
        subreport_path.append("reports\\");
    }

    private void butonActionPerformed(java.awt.event.ActionEvent evt) {
        if (vekilCheckBox.isSelected()) vekil = true; else vekil = false;
        if (secenek.contentEquals("[İŞLEMLER, Yönetim Kurulu Gündemi Çıktısı]")) {
            try {
                String query = "SELECT T.toplantiSayisi,toplantiTarihi,raportor,uye1,uye2,uye3,uye4,uye5,uye6 FROM APP.TOPLANTI T WHERE T.toplantiSayisi=" + toplantiID;
                Baglanti.createConnection();
                ResultSet resultSet = Baglanti.runQuery(query);
                CreateReport report = new CreateReport("toplanti.jrxml");
                report.setDataSource(resultSet);
                HashMap parameters = new HashMap();
                StringBuilder kayitSilQuery = new StringBuilder();
                kayitSilQuery.append("SELECT adi,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli,bolum");
                kayitSilQuery.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                kayitSilQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet kayitSilResult = Baglanti.runQuery(kayitSilQuery.toString());
                JRResultSetDataSource kayitSilDataSource = new JRResultSetDataSource(kayitSilResult);
                parameters.put("kayitSil", kayitSilDataSource);
                StringBuilder juriQuery = new StringBuilder();
                juriQuery.append("SELECT ogrenciNo,ogrenciAdi,anabilimDali,tip,program,sira,danisman,asil2,").append("asil3,asil4,asil5,danismanbolum,asil2bolum,asil3bolum,asil4bolum,asil5bolum,yedek1,yedek2,yedek1bolum,yedek2bolum ").append("FROM APP.OGRENCI  WHERE toplantiSayisi=").append(toplantiID);
                Baglanti.createConnection();
                ResultSet juriResultSet = Baglanti.runQuery(juriQuery.toString());
                JRResultSetDataSource juriDataSource = new JRResultSetDataSource(juriResultSet);
                parameters.put("juriGundem", juriDataSource);
                StringBuilder mezuniyetQuery = new StringBuilder();
                mezuniyetQuery.append("SELECT adi,bolum,statu,abd,tur,ogrenciNo,danisman,M.toplantiSayisi,tarih,kararNo,danismanEmekli");
                mezuniyetQuery.append(" FROM APP.MEZUNIYET M ");
                mezuniyetQuery.append(" WHERE M.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet mezuniyetResultSet = Baglanti.runQuery(mezuniyetQuery.toString());
                JRResultSetDataSource mezuniyetDataSource = new JRResultSetDataSource(mezuniyetResultSet);
                parameters.put("mezuniyet", mezuniyetDataSource);
                StringBuilder danismanAtamaQuery = new StringBuilder();
                danismanAtamaQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde from APP.DANISMAN K,APP.TOPLANTI T");
                danismanAtamaQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                danismanAtamaQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet danismanAtamaResult = Baglanti.runQuery(danismanAtamaQuery.toString());
                JRResultSetDataSource danismanAtamaDataSource = new JRResultSetDataSource(danismanAtamaResult);
                parameters.put("danismanAtama", danismanAtamaDataSource);
                StringBuilder danismanDegistirmeQuery = new StringBuilder();
                danismanDegistirmeQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde,danisman2,dilekceNo,dilekceTarihi from APP.DANISMANDEGISTIRME K,APP.TOPLANTI T");
                danismanDegistirmeQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                danismanDegistirmeQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet danismanDegistirmeResult = Baglanti.runQuery(danismanDegistirmeQuery.toString());
                JRResultSetDataSource danismanDegistirmeDataSource = new JRResultSetDataSource(danismanDegistirmeResult);
                parameters.put("danismanDegistirme", danismanDegistirmeDataSource);
                StringBuffer ekSureQuery = new StringBuffer();
                ekSureQuery.append("SELECT kararNo,adi,abd,tur,ogrenciNo,bolum,ekSureSonu");
                ekSureQuery.append(" FROM APP.EKSURE");
                ekSureQuery.append(" WHERE toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet ekSureResult = Baglanti.runQuery(ekSureQuery.toString());
                JRResultSetDataSource ekSureDataSource = new JRResultSetDataSource(ekSureResult);
                parameters.put("topluKararEkSure", ekSureDataSource);
                StringBuilder tikBelirlemeQuery = new StringBuilder();
                tikBelirlemeQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde,tezkonusu,dilekceNo,dilekceTarihi from APP.TEZKONUSUBELIRLEME K,APP.TOPLANTI T");
                tikBelirlemeQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                tikBelirlemeQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet tikBelirlemeResultSet = Baglanti.runQuery(tikBelirlemeQuery.toString());
                JRResultSetDataSource tikBelirlemeDataSource = new JRResultSetDataSource(tikBelirlemeResultSet);
                parameters.put("tikBelirleme", tikBelirlemeDataSource);
                StringBuilder tezKonusuBelirlemeQuery = new StringBuilder();
                tezKonusuBelirlemeQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde,tezkonusu,dilekceNo,dilekceTarihi from APP.TEZKONUSUBELIRLEME K,APP.TOPLANTI T");
                tezKonusuBelirlemeQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                tezKonusuBelirlemeQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet tezKonusuBelirlemeQueryResult = Baglanti.runQuery(tezKonusuBelirlemeQuery.toString());
                JRResultSetDataSource tezKonusuBelirlemeDataSource = new JRResultSetDataSource(tezKonusuBelirlemeQueryResult);
                parameters.put("tezKonusuBelirleme", tezKonusuBelirlemeDataSource);
                StringBuilder tezKonusuDegistirmeQuery = new StringBuilder();
                tezKonusuDegistirmeQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde,tezkonusu,tezKonusu2,dilekceNo,dilekceTarihi from APP.TEZKONUSUDEGISTIRME K,APP.TOPLANTI T");
                tezKonusuDegistirmeQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                tezKonusuDegistirmeQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet tezKonusuDegistirmeResult = Baglanti.runQuery(tezKonusuDegistirmeQuery.toString());
                JRResultSetDataSource tezKonusuDegistirmeDataSource = new JRResultSetDataSource(tezKonusuDegistirmeResult);
                parameters.put("tezKonusuDegistirme", tezKonusuDegistirmeDataSource);
                parameters.put("SUBREPORT_DIR", subreport_path.toString());
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Hata:" + e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Yönetim Kurulu Gündemi Ön Sayfa]")) {
            try {
                StringBuilder query = new StringBuilder();
                query.append("select uye1 as uye,uye2 as uye,uye3 as uye,uye4 as uye,uye5 as uye,uye6 as uye,");
                query.append("(SELECT enstituSekreteri FROM APP.PERSONEL WHERE id=1) AS uye ");
                query.append("  FROM APP.TOPLANTI  where toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(query.toString());
                CreateReport report = new CreateReport();
                report.setReportFileName("gundemKapak.jrxml");
                report.setDataSource(result);
                report.printReport();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Hata:" + e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Y.Lisans Doktora Tez Jürisi Çıktıları, Tez Jürisi Çıktıları]")) {
            try {
                String imza = imzaYetkiliCombo.getSelectedItem().toString();
                ogrenciListesiFrame fr = new ogrenciListesiFrame(toplantiID, imza, vekil);
                fr.setLocationRelativeTo(rootPane);
                fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                fr.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Y.Lisans Doktora Tez Jürisi Çıktıları, Yüksek Lisans Gündem Çıktısı]")) {
            try {
                StringBuilder query = new StringBuilder();
                query.append("SELECT ogrenciNo,ogrenciAdi,anabilimDali,tip,program,sira,danisman,asil2,").append("asil3,asil4,asil5,danismanbolum,asil2bolum,asil3bolum,asil4bolum,asil5bolum,yedek1,yedek2,yedek1bolum,yedek2bolum ").append("FROM APP.OGRENCI  WHERE toplantiSayisi=").append(toplantiID).append("AND tip LIKE 'Yüksek Lisans'");
                Baglanti.createConnection();
                ResultSet resultSet = Baglanti.runQuery(query.toString());
                CreateReport report = new CreateReport("juriGundem.jrxml");
                report.setDataSource(resultSet);
                report.printReport();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Y.Lisans Doktora Tez Jürisi Çıktıları, Doktora Gündem Çıktısı]")) {
            try {
                StringBuilder query = new StringBuilder();
                query.append("SELECT ogrenciNo,ogrenciAdi,anabilimDali,tip,program,sira,danisman,asil2,").append("asil3,asil4,asil5,danismanbolum,asil2bolum,asil3bolum,asil4bolum,asil5bolum,yedek1,yedek2,yedek1bolum,yedek2bolum ").append("FROM APP.OGRENCI  WHERE toplantiSayisi=").append(toplantiID).append("AND tip LIKE 'Doktora'");
                Baglanti.createConnection();
                ResultSet resultSet = Baglanti.runQuery(query.toString());
                CreateReport report = new CreateReport("juriGundem.jrxml");
                report.setDataSource(resultSet);
                report.printReport();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Mezuniyet Çıktıları, Öğrenci Çıktıları]")) {
            try {
                StringBuilder query = new StringBuilder();
                query.append("SELECT ogrenciNo,adi,danisman,abd  FROM APP.MEZUNIYET ");
                query.append(" WHERE toplantiSayisi=?");
                Baglanti.createConnection();
                PreparedStatement stmt = Baglanti.createPreparedStatement(query.toString());
                stmt.setInt(1, toplantiID);
                ResultSet result = stmt.executeQuery();
                String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                mezunOgrenciListesi fr = new mezunOgrenciListesi(result, toplantiID, imzalayacakKisi, vekil);
                fr.setLocationRelativeTo(rootPane);
                fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                fr.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Mezuniyet Çıktıları, Dekanlık Çıktısı]")) {
            try {
                mezunDekanListe ListeFrame = new mezunDekanListe(toplantiID, imzaYetkiliCombo.getSelectedItem().toString(), vekil);
                ListeFrame.setTitle("Dekanlıkları");
                ListeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ListeFrame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Mezuniyet Çıktıları, Anabilim Dalları Çıktısı]")) {
            try {
                mezunAbdListe ListeFrame = new mezunAbdListe(toplantiID, imzaYetkiliCombo.getSelectedItem().toString(), vekil);
                ListeFrame.setTitle("Dekanlıkları");
                ListeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ListeFrame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Mezuniyet Çıktıları, Öğrenci Çıktıları]")) {
            try {
                String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                mezunOgrenciListesi fr = new mezunOgrenciListesi(toplantiID, imzalayacakKisi);
                fr.setLocationRelativeTo(rootPane);
                fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                fr.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Mezuniyet Çıktıları, Diğer Çıktılar]")) {
            try {
                CreateReport report = new CreateReport("mezunAnaRapor.jrxml");
                HashMap parameters = new HashMap();
                String imzaAtacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                parameters.put("imza", imzaAtacakKisi);
                parameters.put("SUBREPORT_DIR", subreport_path.toString());
                StringBuffer query = new StringBuffer();
                query.append("SELECT DISTINCT T.toplantiSayisi,kararNo,raportor,tarih");
                query.append(" FROM APP.MEZUNIYET K,APP.TOPLANTI T");
                query.append(" WHERE K.toplantiSayisi=T.toplantiSayisi AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet ogrenciResult = Baglanti.runQuery(query.toString());
                report.setDataSource(ogrenciResult);
                StringBuilder ogrenciIsleriListeQuery = new StringBuilder();
                ogrenciIsleriListeQuery.append("SELECT adi,statu,abd,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli,bolum");
                ogrenciIsleriListeQuery.append(" FROM APP.MEZUNIYET K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                ogrenciIsleriListeQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet ogrenciIsleriListeResult = Baglanti.runQuery(ogrenciIsleriListeQuery.toString());
                JRResultSetDataSource ogrenciIsleriListeDataSource = new JRResultSetDataSource(ogrenciIsleriListeResult);
                parameters.put("ogrenciIsleriListe", ogrenciIsleriListeDataSource);
                Baglanti.createConnection();
                ResultSet ogrenciIsleriListeResult2 = Baglanti.runQuery(ogrenciIsleriListeQuery.toString());
                JRResultSetDataSource ogrenciIsleriListeDataSource2 = new JRResultSetDataSource(ogrenciIsleriListeResult2);
                parameters.put("ogrenciIsleriListe2", ogrenciIsleriListeDataSource2);
                StringBuilder rektorlukListeQuery = new StringBuilder();
                rektorlukListeQuery.append("SELECT adi,bolum,statu,abd,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                rektorlukListeQuery.append(" FROM APP.MEZUNIYET K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                rektorlukListeQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet rektorlukListeResult = Baglanti.runQuery(rektorlukListeQuery.toString());
                JRResultSetDataSource rektorlukListeDataSource = new JRResultSetDataSource(rektorlukListeResult);
                parameters.put("rektorlukListe", rektorlukListeDataSource);
                Baglanti.createConnection();
                ResultSet rektorlukListeResult2 = Baglanti.runQuery(rektorlukListeQuery.toString());
                JRResultSetDataSource rektorlukListeDataSource2 = new JRResultSetDataSource(rektorlukListeResult2);
                parameters.put("rektorlukListe2", rektorlukListeDataSource2);
                StringBuilder emniyetQuery = new StringBuilder();
                emniyetQuery.append("SELECT adi,bolum,statu,abd,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                emniyetQuery.append(" FROM APP.MEZUNIYET K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                emniyetQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                emniyetQuery.append(" AND yabanci=1");
                Baglanti.createConnection();
                ResultSet emniyetResult = Baglanti.runQuery(emniyetQuery.toString());
                JRResultSetDataSource emniyetDataSource = new JRResultSetDataSource(emniyetResult);
                if (emniyetResult.next()) {
                    emniyetResult.previous();
                    parameters.put("emniyet", emniyetDataSource);
                    Baglanti.createConnection();
                    ResultSet emniyetResult2 = Baglanti.runQuery(emniyetQuery.toString());
                    JRResultSetDataSource emniyetDataSource2 = new JRResultSetDataSource(emniyetResult2);
                    parameters.put("emniyet2", emniyetDataSource2);
                }
                StringBuilder idariMaliQuery = new StringBuilder();
                idariMaliQuery.append("SELECT adi,bolum,statu,abd,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                idariMaliQuery.append(" FROM APP.MEZUNIYET K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                idariMaliQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                idariMaliQuery.append(" AND danismanEmekli=1");
                Baglanti.createConnection();
                ResultSet idariMaliResult = Baglanti.runQuery(idariMaliQuery.toString());
                JRResultSetDataSource idariMaliDataSource = new JRResultSetDataSource(idariMaliResult);
                if (idariMaliResult.next()) {
                    idariMaliResult.previous();
                    parameters.put("idariMali", idariMaliDataSource);
                    Baglanti.createConnection();
                    ResultSet idariMaliResult2 = Baglanti.runQuery(idariMaliQuery.toString());
                    JRResultSetDataSource idariMaliDataSource2 = new JRResultSetDataSource(idariMaliResult2);
                    parameters.put("idariMali2", idariMaliDataSource2);
                }
                StringBuilder personelQuery = new StringBuilder();
                personelQuery.append("SELECT adi,bolum,statu,abd,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                personelQuery.append(" FROM APP.MEZUNIYET K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                personelQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                personelQuery.append(" AND statu LIKE 'Arş.Gör.'");
                Baglanti.createConnection();
                ResultSet personelResult = Baglanti.runQuery(personelQuery.toString());
                JRResultSetDataSource personelDataSource = new JRResultSetDataSource(personelResult);
                if (personelResult.next()) {
                    personelResult.previous();
                    parameters.put("personel", personelDataSource);
                    Baglanti.createConnection();
                    ResultSet personelResult2 = Baglanti.runQuery(personelQuery.toString());
                    JRResultSetDataSource personelDataSource2 = new JRResultSetDataSource(personelResult2);
                    parameters.put("personel2", personelDataSource2);
                }
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Mezuniyet Çıktıları, Gündem Çıktısı]")) {
            try {
                StringBuilder query = new StringBuilder();
                query.append("SELECT adi,bolum,statu,abd,tur,ogrenciNo,danisman,M.toplantiSayisi,tarih,kararNo,danismanEmekli");
                query.append(" FROM APP.MEZUNIYET M ");
                query.append(" WHERE M.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(query.toString());
                CreateReport report = new CreateReport("mezuniyetGundem.jrxml");
                report.setDataSource(result);
                report.printReport();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Kayıt Silme Çıktıları, Öğrenci Çıktıları]")) {
            try {
                String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                kayitSilOgrenciListesi fr = new kayitSilOgrenciListesi(toplantiID, imzalayacakKisi, vekil);
                fr.setLocationRelativeTo(rootPane);
                fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                fr.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Kayıt Silme Çıktıları, Diğer Çıktılar]")) {
            try {
                CreateReport report = new CreateReport("kayitSilmeAnaRapor.jrxml");
                HashMap parameters = new HashMap();
                String imzaAtacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                parameters.put("imza", imzaAtacakKisi);
                parameters.put("vekil", vekil);
                parameters.put("SUBREPORT_DIR", subreport_path.toString());
                StringBuffer query = new StringBuffer();
                query.append("SELECT DISTINCT T.toplantiSayisi,kararNo,raportor,tarih");
                query.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T");
                query.append(" WHERE K.toplantiSayisi=T.toplantiSayisi AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet ogrenciResult = Baglanti.runQuery(query.toString());
                report.setDataSource(ogrenciResult);
                StringBuilder ogrenciIsleriListeQuery = new StringBuilder();
                ogrenciIsleriListeQuery.append("SELECT adi,bolum,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                ogrenciIsleriListeQuery.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                ogrenciIsleriListeQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet ogrenciIsleriListeResult = Baglanti.runQuery(ogrenciIsleriListeQuery.toString());
                JRResultSetDataSource ogrenciIsleriListeDataSource = new JRResultSetDataSource(ogrenciIsleriListeResult);
                parameters.put("ogrenciIsleriListe", ogrenciIsleriListeDataSource);
                StringBuilder ogrenciIsleriListeQuery2 = new StringBuilder();
                ogrenciIsleriListeQuery2.append("SELECT adi,bolum,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                ogrenciIsleriListeQuery2.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                ogrenciIsleriListeQuery2.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet ogrenciIsleriListeResult2 = Baglanti.runQuery(ogrenciIsleriListeQuery2.toString());
                JRResultSetDataSource ogrenciIsleriListeDataSource2 = new JRResultSetDataSource(ogrenciIsleriListeResult2);
                parameters.put("ogrenciIsleriListe2", ogrenciIsleriListeDataSource2);
                StringBuilder rektorlukListeQuery = new StringBuilder();
                rektorlukListeQuery.append("SELECT adi,bolum,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                rektorlukListeQuery.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                rektorlukListeQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet rektorlukListeResult = Baglanti.runQuery(rektorlukListeQuery.toString());
                JRResultSetDataSource rektorlukListeDataSource = new JRResultSetDataSource(rektorlukListeResult);
                parameters.put("rektorlukListe", rektorlukListeDataSource);
                StringBuilder rektorlukListeQuery2 = new StringBuilder();
                rektorlukListeQuery2.append("SELECT adi,bolum,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                rektorlukListeQuery2.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                rektorlukListeQuery2.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet rektorlukListeResult2 = Baglanti.runQuery(rektorlukListeQuery2.toString());
                JRResultSetDataSource rektorlukListeDataSource2 = new JRResultSetDataSource(rektorlukListeResult2);
                parameters.put("rektorlukListe2", rektorlukListeDataSource2);
                StringBuilder emniyetQuery = new StringBuilder();
                emniyetQuery.append("SELECT adi,bolum,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                emniyetQuery.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                emniyetQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                emniyetQuery.append(" AND yabanci=1");
                Baglanti.createConnection();
                ResultSet emniyetResult = Baglanti.runQuery(emniyetQuery.toString());
                JRResultSetDataSource emniyetDataSource = new JRResultSetDataSource(emniyetResult);
                if (emniyetResult.next()) {
                    emniyetResult.previous();
                    parameters.put("emniyet", emniyetDataSource);
                    parameters.put("emniyetExist", true);
                }
                StringBuilder emniyetQuery2 = new StringBuilder();
                emniyetQuery2.append("SELECT adi,bolum,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                emniyetQuery2.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                emniyetQuery2.append(" AND K.toplantiSayisi=" + toplantiID);
                emniyetQuery2.append(" AND yabanci=1");
                Baglanti.createConnection();
                ResultSet emniyetResult2 = Baglanti.runQuery(emniyetQuery2.toString());
                JRResultSetDataSource emniyetDataSource2 = new JRResultSetDataSource(emniyetResult2);
                if (emniyetResult2.next()) {
                    emniyetResult2.previous();
                    parameters.put("emniyet2", emniyetDataSource2);
                    parameters.put("emniyetExist2", true);
                }
                StringBuilder idariMaliQuery = new StringBuilder();
                idariMaliQuery.append("SELECT adi,bolum,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                idariMaliQuery.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                idariMaliQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                idariMaliQuery.append(" AND danismanEmekli=1");
                Baglanti.createConnection();
                ResultSet idariMaliResult = Baglanti.runQuery(idariMaliQuery.toString());
                JRResultSetDataSource idariMaliDataSource = new JRResultSetDataSource(idariMaliResult);
                if (idariMaliResult.next()) {
                    idariMaliResult.previous();
                    parameters.put("idariMali", idariMaliDataSource);
                    parameters.put("idariMaliExist", true);
                }
                StringBuilder idariMaliQuery2 = new StringBuilder();
                idariMaliQuery2.append("SELECT adi,bolum,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                idariMaliQuery2.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                idariMaliQuery2.append(" AND K.toplantiSayisi=" + toplantiID);
                idariMaliQuery2.append(" AND danismanEmekli=1");
                Baglanti.createConnection();
                ResultSet idariMaliResult2 = Baglanti.runQuery(idariMaliQuery2.toString());
                JRResultSetDataSource idariMaliDataSource2 = new JRResultSetDataSource(idariMaliResult2);
                if (idariMaliResult2.next()) {
                    idariMaliResult2.previous();
                    parameters.put("idariMali2", idariMaliDataSource2);
                    parameters.put("idariMaliExist2", true);
                }
                StringBuilder personelQuery = new StringBuilder();
                personelQuery.append("SELECT adi,bolum,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                personelQuery.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                personelQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                personelQuery.append(" AND statu LIKE 'Arş.Gör.'");
                Baglanti.createConnection();
                ResultSet personelResult = Baglanti.runQuery(personelQuery.toString());
                JRResultSetDataSource personelDataSource = new JRResultSetDataSource(personelResult);
                if (personelResult.next()) {
                    personelResult.previous();
                    parameters.put("personel", personelDataSource);
                    parameters.put("personelExist", true);
                }
                StringBuilder personelQuery2 = new StringBuilder();
                personelQuery2.append("SELECT adi,bolum,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                personelQuery2.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                personelQuery2.append(" AND K.toplantiSayisi=" + toplantiID);
                personelQuery2.append(" AND statu LIKE 'Arş.Gör.'");
                Baglanti.createConnection();
                ResultSet personelResult2 = Baglanti.runQuery(personelQuery2.toString());
                JRResultSetDataSource personelDataSource2 = new JRResultSetDataSource(personelResult2);
                if (personelResult2.next()) {
                    personelResult2.previous();
                    parameters.put("personel2", personelDataSource2);
                    parameters.put("personelExist2", true);
                }
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Kayıt Silme Çıktıları, Anabilim Dalları Çıktısı]")) {
            try {
                kayitSilmeABDListesi abdListeFrame = new kayitSilmeABDListesi(toplantiID, imzaYetkiliCombo.getSelectedItem().toString(), vekil);
                abdListeFrame.setTitle("Anabilim Dalları");
                abdListeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                abdListeFrame.setLocationRelativeTo(rootPane);
                abdListeFrame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Kayıt Silme Çıktıları, Dekanlık Çıktısı]")) {
            try {
                kayitSilmeDekanlikListesi ListeFrame = new kayitSilmeDekanlikListesi(toplantiID, imzaYetkiliCombo.getSelectedItem().toString());
                ListeFrame.setTitle("Dekanlıkları");
                ListeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ListeFrame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Kayıt Silme Çıktıları, Gündem Çıktısı]")) {
            try {
                StringBuilder query = new StringBuilder();
                query.append("SELECT adi,bolum,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,danismanEmekli");
                query.append(" FROM APP.KAYITSIL K ");
                query.append(" WHERE K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(query.toString());
                CreateReport report = new CreateReport("kayitSilGundem.jrxml");
                report.setDataSource(result);
                report.printReport();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Kayıt Silme Çıktıları, Personel İşleri Çıktısı]")) {
            try {
                StringBuilder query = new StringBuilder();
                query.append("SELECT tarih,K.toplantiSayisi,kararNo,raportor");
                query.append(" FROM APP.KAYITSIL K INNER JOIN APP.TOPLANTI T");
                query.append(" ON K.TOPLANTISAYISI=T.TOPLANTISAYISI");
                query.append(" WHERE K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(query.toString());
                String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                CreateReport report = new CreateReport("kayitSilPersonel.jrxml");
                report.setDataSource(result);
                HashMap parameters = new HashMap();
                parameters.put("imza", imzalayacakKisi);
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("SELECT adi,statu,abd,yonetmelikMaddeleri,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli");
                subQuery.append(" FROM APP.KAYITSIL K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                subQuery.append(" AND statu LIKE 'Arş.Gör.'");
                Baglanti.createConnection();
                ResultSet subResult = Baglanti.runQuery(subQuery.toString());
                JRResultSetDataSource subDataSource = new JRResultSetDataSource(subResult);
                parameters.put("subreport_expr", subDataSource);
                parameters.put("SUBREPORT_DIR", GetCurrentDirectory.getPath());
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Eksüre Çıktıları, Öğrenci İşleri Çıktısı]")) {
            try {
                CreateReport report = new CreateReport("eksureOgrenciIsleri.jrxml");
                HashMap parameters = new HashMap();
                String imzaAtacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                parameters.put("imza", imzaAtacakKisi);
                parameters.put("SUBREPORT_DIR", subreport_path.toString());
                StringBuffer query = new StringBuffer();
                query.append("SELECT DISTINCT T.toplantiSayisi,raportor,tarih,kararNo");
                query.append(" FROM APP.EKSURE K,APP.TOPLANTI T");
                query.append(" WHERE K.toplantiSayisi=T.toplantiSayisi AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet ogrenciResult = Baglanti.runQuery(query.toString());
                report.setDataSource(ogrenciResult);
                StringBuilder ogrenciIsleriListeQuery = new StringBuilder();
                ogrenciIsleriListeQuery.append("SELECT adi,bolum,statu,abd,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli,eksuresonu");
                ogrenciIsleriListeQuery.append(" FROM APP.EKSURE K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                ogrenciIsleriListeQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet ogrenciIsleriListeResult = Baglanti.runQuery(ogrenciIsleriListeQuery.toString());
                JRResultSetDataSource ogrenciIsleriListeDataSource = new JRResultSetDataSource(ogrenciIsleriListeResult);
                parameters.put("subreport_expr", ogrenciIsleriListeDataSource);
                StringBuilder ogrenciIsleriListeQuery2 = new StringBuilder();
                ogrenciIsleriListeQuery2.append("SELECT adi,bolum,statu,abd,tur,ogrenciNo,danisman,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli,eksuresonu");
                ogrenciIsleriListeQuery2.append(" FROM APP.EKSURE K,APP.TOPLANTI T WHERE K.toplantiSayisi=T.toplantiSayisi ");
                ogrenciIsleriListeQuery2.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet ogrenciIsleriListeResult2 = Baglanti.runQuery(ogrenciIsleriListeQuery2.toString());
                JRResultSetDataSource ogrenciIsleriListeDataSource2 = new JRResultSetDataSource(ogrenciIsleriListeResult2);
                parameters.put("subreport_expr2", ogrenciIsleriListeDataSource2);
                parameters.put("vekil", vekil);
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Eksüre Çıktıları, Anabilim Dalları Çıktısı]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            eksureOgrenciListesi fr = new eksureOgrenciListesi(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Eksüre Çıktıları, Gündem Çıktısı]")) {
            try {
                CreateReport report = new CreateReport("ekSureGundem.jrxml");
                StringBuffer query = new StringBuffer();
                query.append("SELECT kararNo,adi,abd,tur,ogrenciNo,bolum,ekSureSonu");
                query.append(" FROM APP.EKSURE");
                query.append(" WHERE toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet ogrenciResult = Baglanti.runQuery(query.toString());
                report.setDataSource(ogrenciResult);
                report.printReport();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Danışman Çıktıları, Danışman Atama Çıktıları, Öğrenci İşleri Çıktısı]")) {
            try {
                String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                StringBuffer query = new StringBuffer();
                query.append("SELECT  adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,dilekceNo,dilekceTarihi,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde ");
                query.append(" FROM APP.DANISMAN K,APP.TOPLANTI T");
                query.append(" WHERE K.toplantiSayisi=T.toplantiSayisi AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(query.toString());
                CreateReport report = new CreateReport("danismanAtamaOgrenciIsleri.jrxml");
                report.setDataSource(result);
                HashMap parameters = new HashMap();
                parameters.put("imza", imzalayacakKisi);
                parameters.put("SUBREPORT_DIR", subreport_path.toString());
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("SELECT adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,dilekceNo,dilekceTarihi,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde from APP.DANISMAN K,APP.TOPLANTI T");
                subQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet subResult2 = Baglanti.runQuery(subQuery.toString());
                JRResultSetDataSource subDataSource = new JRResultSetDataSource(subResult2);
                parameters.put("subreport_expr", subDataSource);
                StringBuilder subQuery2 = new StringBuilder();
                subQuery2.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,dilekceNo,dilekceTarihi,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde from APP.DANISMAN K,APP.TOPLANTI T");
                subQuery2.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery2.append(" AND K.toplantiSayisi=" + toplantiID);
                System.out.println();
                System.out.println(subQuery.toString());
                Baglanti.createConnection();
                ResultSet subResult3 = Baglanti.runQuery(subQuery.toString());
                JRResultSetDataSource subDataSource2 = new JRResultSetDataSource(subResult3);
                parameters.put("subreport_expr2", subDataSource2);
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Danışman Çıktıları, Danışman Atama Çıktıları, Dekanlık Çıktıları]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            danismanDekanlikListesi fr = new danismanDekanlikListesi(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Danışman Çıktıları, Danışman Atama Çıktıları, Anabilim Dalları Çıktısı]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            danismanAtamaABDListesi fr = new danismanAtamaABDListesi(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Danışman Çıktıları, Danışman Atama Çıktıları, Gündem Çıktısı]")) {
            try {
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde from APP.DANISMAN K,APP.TOPLANTI T");
                subQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(subQuery.toString());
                CreateReport report = new CreateReport("danismanAtamaGundem.jrxml");
                report.setDataSource(result);
                report.printReport();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Danışman Çıktıları, İkinci Danışman Çıktıları, Öğrenci İşleri Çıktısı]")) {
            try {
                String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                StringBuffer query = new StringBuffer();
                query.append("SELECT  adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,dilekceNo,dilekceTarihi,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde,danisman2,danismanAbd2");
                query.append(" FROM APP.IKINCIDANISMAN K,APP.TOPLANTI T");
                query.append(" WHERE K.toplantiSayisi=T.toplantiSayisi AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(query.toString());
                CreateReport report = new CreateReport("ikinciDanismanAtamaOgrenciIsleri.jrxml");
                report.setDataSource(result);
                HashMap parameters = new HashMap();
                parameters.put("imza", imzalayacakKisi);
                parameters.put("SUBREPORT_DIR", subreport_path.toString());
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("SELECT adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,dilekceNo,dilekceTarihi,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde from APP.DANISMAN K,APP.TOPLANTI T");
                subQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet subResult2 = Baglanti.runQuery(subQuery.toString());
                JRResultSetDataSource subDataSource = new JRResultSetDataSource(subResult2);
                parameters.put("subreport_expr", subDataSource);
                StringBuilder subQuery2 = new StringBuilder();
                subQuery2.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,dilekceNo,dilekceTarihi,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde from APP.DANISMAN K,APP.TOPLANTI T");
                subQuery2.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery2.append(" AND K.toplantiSayisi=" + toplantiID);
                System.out.println();
                System.out.println(subQuery.toString());
                Baglanti.createConnection();
                ResultSet subResult3 = Baglanti.runQuery(subQuery.toString());
                JRResultSetDataSource subDataSource2 = new JRResultSetDataSource(subResult3);
                parameters.put("subreport_expr2", subDataSource2);
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Danışman Çıktıları, İkinci Danışman Çıktıları, Dekanlık Çıktıları]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            ikinciDanismanDekanlikListesi fr = new ikinciDanismanDekanlikListesi(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Danışman Çıktıları, İkinci Danışman Çıktıları, Anabilim Dalları Çıktısı]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            ikinciDanismanAtamaABDListesi fr = new ikinciDanismanAtamaABDListesi(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Danışman Çıktıları, İkinci Danışman Çıktıları, Gündem Çıktısı]")) {
            try {
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde from APP.DANISMAN K,APP.TOPLANTI T");
                subQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(subQuery.toString());
                CreateReport report = new CreateReport("danismanAtamaGundem.jrxml");
                report.setDataSource(result);
                report.printReport();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Danışman Çıktıları, Danışman Değiştirme Çıktısı, Öğrenci İşleri Çıktısı]")) {
            try {
                String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                StringBuffer query = new StringBuffer();
                query.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,dilekceNo,dilekcetarihi,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde,danisman2 from APP.DANISMANDEGISTIRME K,APP.TOPLANTI T");
                query.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                query.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(query.toString());
                CreateReport report = new CreateReport("danismanDegistirme.jrxml");
                report.setDataSource(result);
                HashMap parameters = new HashMap();
                parameters.put("imza", imzalayacakKisi);
                parameters.put("SUBREPORT_DIR", subreport_path.toString());
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,dilekceNo,dilekcetarihi,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde,danisman2 from APP.DANISMANDEGISTIRME K,APP.TOPLANTI T");
                subQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet subResult2 = Baglanti.runQuery(subQuery.toString());
                JRResultSetDataSource subDataSource = new JRResultSetDataSource(subResult2);
                parameters.put("subreport_expr", subDataSource);
                StringBuilder subQuery2 = new StringBuilder();
                subQuery2.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,dilekceNo,dilekcetarihi,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde,danisman2 from APP.DANISMANDEGISTIRME K,APP.TOPLANTI T");
                subQuery2.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery2.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet subResult3 = Baglanti.runQuery(subQuery.toString());
                JRResultSetDataSource subDataSource2 = new JRResultSetDataSource(subResult3);
                parameters.put("subreport_expr2", subDataSource2);
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Danışman Çıktıları, Danışman Değiştirme Çıktısı, Dekanlık Çıktıları]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            danismanDegistirmeDekanlikListesi fr = new danismanDegistirmeDekanlikListesi(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Danışman Çıktıları, Danışman Değiştirme Çıktısı, Anabilim Dalları Çıktısı]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            danismanDegistirmeAbdListe fr = new danismanDegistirmeAbdListe(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Danışman Çıktıları, Danışman Değiştirme Çıktısı, Gündem Çıktısı]")) {
            try {
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde,danisman2 from APP.DANISMANDEGISTIRME K,APP.TOPLANTI T");
                subQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(subQuery.toString());
                CreateReport report = new CreateReport("danismanDegistirmeGundem.jrxml");
                report.setDataSource(result);
                report.printReport();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Tez İzleme Komitesi, Tez İzleme Komitesi Belirleme, Diğer Çıktılar]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            tezIzlemeKomitesiBelirlemeOgrenciListesi form = new tezIzlemeKomitesiBelirlemeOgrenciListesi(toplantiID, imzalayacakKisi, vekil);
            form.setLocationRelativeTo(rootPane);
            form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            form.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Tez İzleme Komitesi, Tez İzleme Komitesi Belirleme, Anabilim Dalı Çıktıları]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            tezIzlemeKomitesiBelirlemeAbdListe form = new tezIzlemeKomitesiBelirlemeAbdListe(toplantiID, imzalayacakKisi, vekil);
            form.setLocationRelativeTo(rootPane);
            form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            form.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Tez İzleme Komitesi, Tez İzleme Komitesi Belirleme, Gündem Çıktısı]")) {
            try {
                CreateReport report = new CreateReport("tezIzlemeKomitesiGundem.jrxml");
                HashMap parameters = new HashMap();
                parameters.put("SUBREPORT_DIR", subreport_path.toString());
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("SELECT adi,bolum,statu,abd,tur,ogrenciNo,danisman,danismanabd,danisman2,danismanabd2,danisman3,danismanabd3,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli,A.FAKULTE AS danismanFakulte ,A2.FAKULTE AS danismanFakulte2 , A3.FAKULTE AS danismanFakulte3,dilekceNo,dilekcetarihi,yonetmelikMadde");
                subQuery.append(" FROM APP.TEZIZLEMEKOMITESI K,APP.TOPLANTI T,APP.ABDALI A,APP.ABDALI A2,APP.ABDALI A3 WHERE K.toplantiSayisi=T.toplantiSayisi ");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID + " AND A.ISIM=danismanAbd AND A2.ISIM=danismanabd2 AND A3.ISIM=danismanAbd3");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet subResult2 = Baglanti.runQuery(subQuery.toString());
                report.setDataSource(subResult2);
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Tez İzleme Komitesi, Tez İzleme Komitesi Değiştirme, Diğer Çıktılar]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            tezIzlemeKomitesiDegistirmeOgrenciListesi form = new tezIzlemeKomitesiDegistirmeOgrenciListesi(toplantiID, imzalayacakKisi, vekil);
            form.setLocationRelativeTo(rootPane);
            form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            form.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Tez İzleme Komitesi, Tez İzleme Komitesi Değiştirme, Anabilim Dalı Çıktıları]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            tezIzlemeKomitesiDegistirmeAbdListe form = new tezIzlemeKomitesiDegistirmeAbdListe(toplantiID, imzalayacakKisi, vekil);
            form.setLocationRelativeTo(rootPane);
            form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            form.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Tez İzleme Komitesi, Tez İzleme Komitesi Değiştirme, Gündem Çıktısı]")) {
            try {
                CreateReport report = new CreateReport("tezIzlemeKomitesiDegistirmeGundem.jrxml");
                HashMap parameters = new HashMap();
                parameters.put("SUBREPORT_DIR", subreport_path.toString());
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("SELECT adi,bolum,statu,abd,tur,ogrenciNo,danisman,danismanabd,danisman2,danismanabd2,danisman3,danismanabd3,K.toplantiSayisi,tarih,kararNo,raportor,danismanEmekli,A.FAKULTE AS danismanFakulte ,A2.FAKULTE AS danismanFakulte2 , A3.FAKULTE AS danismanFakulte3,dilekceNo,dilekcetarihi,yonetmelikMadde");
                subQuery.append(" FROM APP.TEZIZLEMEKOMITESIDEGISTIRME K,APP.TOPLANTI T,APP.ABDALI A,APP.ABDALI A2,APP.ABDALI A3 WHERE K.toplantiSayisi=T.toplantiSayisi ");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID + " AND A.ISIM=danismanAbd AND A2.ISIM=danismanabd2 AND A3.ISIM=danismanAbd3");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet subResult2 = Baglanti.runQuery(subQuery.toString());
                report.setDataSource(subResult2);
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Tez, Tez Konusu Belirleme, Öğrenci İşleri Çıktısı]")) {
            try {
                String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                StringBuffer query = new StringBuffer();
                query.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,danismanEmekli,K.toplantiSayisi,tarih,kararNo,yonetmelikMadde,tezKonusu,dilekceNo,dilekcetarihi from APP.TEZKONUSUBELIRLEME K,APP.TOPLANTI T");
                query.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                query.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(query.toString());
                CreateReport report = new CreateReport("tezKonusuBelirleme.jrxml");
                report.setDataSource(result);
                HashMap parameters = new HashMap();
                parameters.put("imza", imzalayacakKisi);
                parameters.put("SUBREPORT_DIR", subreport_path.toString());
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,danismanEmekli,K.toplantiSayisi,tarih,kararNo,yonetmelikMadde,tezKonusu,dilekceNo,dilekcetarihi from APP.TEZKONUSUBELIRLEME K,APP.TOPLANTI T");
                subQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                System.out.println();
                System.out.println(subQuery.toString());
                Baglanti.createConnection();
                ResultSet subResult2 = Baglanti.runQuery(subQuery.toString());
                JRResultSetDataSource subDataSource = new JRResultSetDataSource(subResult2);
                parameters.put("subreport_expr", subDataSource);
                StringBuilder subQuery2 = new StringBuilder();
                subQuery2.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,danismanEmekli,K.toplantiSayisi,tarih,kararNo,yonetmelikMadde,tezKonusu,dilekceNo,dilekcetarihi from APP.TEZKONUSUBELIRLEME K,APP.TOPLANTI T");
                subQuery2.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery2.append(" AND K.toplantiSayisi=" + toplantiID);
                System.out.println();
                System.out.println(subQuery.toString());
                Baglanti.createConnection();
                ResultSet subResult3 = Baglanti.runQuery(subQuery.toString());
                JRResultSetDataSource subDataSource2 = new JRResultSetDataSource(subResult3);
                parameters.put("subreport_expr2", subDataSource2);
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Tez, Tez Konusu Belirleme, Dekanlık Çıktıları]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            tezKonusuBelirlemeDekanlikListesi fr = new tezKonusuBelirlemeDekanlikListesi(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Tez, Tez Konusu Belirleme, Anabilim Dalları Çıktısı]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            tezKonusuBelirlemeAbdListe fr = new tezKonusuBelirlemeAbdListe(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Tez, Tez Konusu Belirleme, Gündem Çıktısı]")) {
            try {
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde,tezkonusu,dilekceNo,dilekceTarihi from APP.TEZKONUSUBELIRLEME K,APP.TOPLANTI T");
                subQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(subQuery.toString());
                CreateReport report = new CreateReport("tezKonusuBelirlemeGundem.jrxml");
                report.setDataSource(result);
                report.printReport();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Tez, Tez Konusu Değiştirme, Öğrenci İşleri Çıktısı]")) {
            try {
                String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                StringBuffer query = new StringBuffer();
                query.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,danismanEmekli,K.toplantiSayisi,tarih,kararNo,yonetmelikMadde,tezKonusu,dilekceNo,dilekcetarihi,tezkonusu2 from APP.TEZKONUSUDEGISTIRME K,APP.TOPLANTI T");
                query.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                query.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(query.toString());
                CreateReport report = new CreateReport("tezKonusuDegistirme.jrxml");
                report.setDataSource(result);
                HashMap parameters = new HashMap();
                parameters.put("imza", imzalayacakKisi);
                parameters.put("SUBREPORT_DIR", subreport_path.toString());
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,danismanEmekli,K.toplantiSayisi,tarih,kararNo,yonetmelikMadde,tezKonusu,dilekceNo,dilekcetarihi,tezkonusu2 from APP.TEZKONUSUDEGISTIRME K,APP.TOPLANTI T");
                subQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                System.out.println();
                System.out.println(subQuery.toString());
                Baglanti.createConnection();
                ResultSet subResult2 = Baglanti.runQuery(subQuery.toString());
                JRResultSetDataSource subDataSource = new JRResultSetDataSource(subResult2);
                parameters.put("subreport_expr", subDataSource);
                StringBuilder subQuery2 = new StringBuilder();
                subQuery2.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,danismanEmekli,K.toplantiSayisi,tarih,kararNo,yonetmelikMadde,tezKonusu,dilekceNo,tezKonusu2,dilekcetarihi from APP.TEZKONUSUDEGISTIRME K,APP.TOPLANTI T");
                subQuery2.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery2.append(" AND K.toplantiSayisi=" + toplantiID);
                System.out.println();
                System.out.println(subQuery.toString());
                Baglanti.createConnection();
                ResultSet subResult3 = Baglanti.runQuery(subQuery.toString());
                JRResultSetDataSource subDataSource2 = new JRResultSetDataSource(subResult3);
                parameters.put("subreport_expr2", subDataSource2);
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Tez, Tez Konusu Değiştirme, Dekanlık Çıktıları]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            tezKonusuDegistirmeDekanlikListesi fr = new tezKonusuDegistirmeDekanlikListesi(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Tez, Tez Konusu Değiştirme, Anabilim Dalları Çıktısı]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            tezKonusuDegistirmeAbdListe fr = new tezKonusuDegistirmeAbdListe(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Tez, Tez Konusu Değiştirme, Gündem Çıktısı]")) {
            try {
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde,tezkonusu,tezKonusu2,dilekceNo,dilekceTarihi from APP.TEZKONUSUDEGISTIRME K,APP.TOPLANTI T");
                subQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(subQuery.toString());
                CreateReport report = new CreateReport("tezKonusuDegistirmeGundem.jrxml");
                report.setDataSource(result);
                report.printReport();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Tez Konusu ve Danışman Değiştirme, Öğrenci İşleri Çıktısı]")) {
            try {
                String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
                StringBuffer query = new StringBuffer();
                query.append("SELECT  adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,dilekceNo,dilekceTarihi,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde,danisman2,tezKonusu,tezKonusu2");
                query.append(" FROM APP.TEZKONUSUVEDANISMAN K,APP.TOPLANTI T");
                query.append(" WHERE K.toplantiSayisi=T.toplantiSayisi AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(query.toString());
                CreateReport report = new CreateReport("tezKonusuVeDanismanDegistirmeOgrenciIsleri.jrxml");
                report.setDataSource(result);
                HashMap parameters = new HashMap();
                parameters.put("imza", imzalayacakKisi);
                parameters.put("SUBREPORT_DIR", subreport_path.toString());
                report.printReport(parameters);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else if (secenek.contentEquals("[İŞLEMLER, Tez Konusu ve Danışman Değiştirme, Dekanlık Çıktıları]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            tezVeDanisman.tezVeDanismanDegistirmeDekanlikListe fr = new tezVeDanisman.tezVeDanismanDegistirmeDekanlikListe(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Tez Konusu ve Danışman Değiştirme, Anabilim Dalları Çıktısı]")) {
            String imzalayacakKisi = imzaYetkiliCombo.getSelectedItem().toString();
            tezVeDanisman.tezVeDanismanDegistirmeABDListe fr = new tezVeDanisman.tezVeDanismanDegistirmeABDListe(toplantiID, imzalayacakKisi, vekil);
            fr.setLocationRelativeTo(rootPane);
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fr.setVisible(true);
        } else if (secenek.contentEquals("[İŞLEMLER, Tez Konusu ve Danışman Değiştirme, Gündem Çıktısı]")) {
            try {
                StringBuilder subQuery = new StringBuilder();
                subQuery.append("select adi,bolum,statu,abd,tur,ogrenciNo,raportor,danisman,K.toplantiSayisi,tarih,kararNo,danismanEmekli,yonetmelikMadde from APP.DANISMAN K,APP.TOPLANTI T");
                subQuery.append(" WHERE K.toplantiSayisi=T.toplantiSayisi");
                subQuery.append(" AND K.toplantiSayisi=" + toplantiID);
                Baglanti.createConnection();
                ResultSet result = Baglanti.runQuery(subQuery.toString());
                CreateReport report = new CreateReport("danismanAtamaGundem.jrxml");
                report.setDataSource(result);
                report.printReport();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        getPath();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ciktiSecenekFrame().setVisible(true);
            }
        });
    }

    private javax.swing.JButton buton;

    private javax.swing.JComboBox imzaYetkiliCombo;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTree jTree1;

    private javax.swing.JCheckBox vekilCheckBox;
}
