package org.sadhar.sipp.rencanaanggaranpendapatanuanguksksasuransi;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sadhar.sia.common.ClassSession;
import org.sadhar.sia.common.ClassUtility;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

/**
 *
 * @author kris
 */
public class RencanaAnggaranPendapatanUangUktSksAsuransiWnd extends ClassApplicationModule {

    private Combobox cmbboxPosPendapatan;

    private Label lblTahunAnggaran;

    private Listbox lstboxUnitKerja;

    private Label lblProgramStudi;

    private Label lblUKTAsuransiDetailX;

    private Label lblUKTAsuransiDetailY;

    private Label lblJmlAnggaranUKTAsuransiX;

    private Label lblJmlAnggaranUKTAsuransiY;

    private Label lblTotalAnggaranUKTAsuransi;

    private Label lblTotalAnggaranSKS;

    private Label lblSKSDetailX;

    private Label lblSKSDetailY;

    private Label lblJmlAnggaranSKSX;

    private Listbox lstboxUKTAsuransi;

    private Listbox lstboxDetailUKTAsuransiX;

    private Listbox lstboxDetailUKTAsuransiY;

    private Listbox lstboxSKSDetailY;

    private Listbox lstboxSKSDetailX;

    private Label lblJmlAnggaranSKSY;

    private Listbox lstboxSKS;

    private Groupbox groupboxSKSDetail;

    private Textbox txtboxIdPosPendapatan;

    private Textbox txtboxKodeProdi;

    private Textbox txtboxIdPosAnggaranPendapatan;

    private Groupbox groupboxUKTAsuransiDetail;

    private Groupbox groupboxUKTAsuransi;

    private Groupbox groupboxSKS;

    private ClassSession session;

    private DecimalFormat df;

    private RencanaAnggaranPendapatanUangUktSksAsuransiDAO rapuusadao;

    public RencanaAnggaranPendapatanUangUktSksAsuransiWnd() {
        rapuusadao = new RencanaAnggaranPendapatanUangUktSksAsuransiDAOImpl();
        df = new DecimalFormat("#,##0.00");
    }

    public void onCreate() throws Exception {
        cmbboxPosPendapatan = (Combobox) getFellow("cmbboxPosPendapatan");
        lblTahunAnggaran = (Label) getFellow("lblTahunAnggaran");
        lstboxUnitKerja = (Listbox) getFellow("lstboxUnitKerja");
        lstboxUKTAsuransi = (Listbox) getFellow("lstboxUKTAsuransi");
        lstboxSKSDetailY = (Listbox) getFellow("lstboxSKSDetailY");
        lstboxSKS = (Listbox) getFellow("lstboxSKS");
        lstboxSKSDetailX = (Listbox) getFellow("lstboxSKSDetailX");
        groupboxSKSDetail = (Groupbox) getFellow("groupboxSKSDetail");
        lstboxDetailUKTAsuransiX = (Listbox) getFellow("lstboxDetailUKTAsuransiX");
        lstboxDetailUKTAsuransiY = (Listbox) getFellow("lstboxDetailUKTAsuransiY");
        lblProgramStudi = (Label) getFellow("lblProgramStudi");
        lblUKTAsuransiDetailX = (Label) getFellow("lblUKTAsuransiDetailX");
        lblUKTAsuransiDetailY = (Label) getFellow("lblUKTAsuransiDetailY");
        lblJmlAnggaranUKTAsuransiX = (Label) getFellow("lblJmlAnggaranUKTAsuransiX");
        lblJmlAnggaranUKTAsuransiY = (Label) getFellow("lblJmlAnggaranUKTAsuransiY");
        lblJmlAnggaranSKSX = (Label) getFellow("lblJmlAnggaranSKSX");
        lblTotalAnggaranUKTAsuransi = (Label) getFellow("lblTotalAnggaranUKTAsuransi");
        lblTotalAnggaranSKS = (Label) getFellow("lblTotalAnggaranSKS");
        lblSKSDetailX = (Label) getFellow("lblSKSDetailX");
        lblSKSDetailY = (Label) getFellow("lblSKSDetailY");
        lblJmlAnggaranSKSY = (Label) getFellow("lblJmlAnggaranSKSY");
        txtboxIdPosPendapatan = (Textbox) getFellow("txtboxIdPosPendapatan");
        txtboxKodeProdi = (Textbox) getFellow("txtboxKodeProdi");
        groupboxUKTAsuransi = (Groupbox) getFellow("groupboxUKTAsuransi");
        groupboxSKS = (Groupbox) getFellow("groupboxSKS");
        groupboxUKTAsuransiDetail = (Groupbox) getFellow("groupboxUKTAsuransiDetail");
        session = new ClassSession("2009", "");
        lblTahunAnggaran.setValue(session.getThnAnggaran());
        lblUKTAsuransiDetailX.setValue("Januari-Juni " + session.getThnAnggaran());
        lblUKTAsuransiDetailY.setValue("Juli-Desember " + session.getThnAnggaran());
        lblSKSDetailX.setValue("Januari-Juni " + session.getThnAnggaran());
        lblSKSDetailY.setValue("Juli-Desember " + session.getThnAnggaran());
        txtboxIdPosAnggaranPendapatan = (Textbox) getFellow("txtboxIdPosAnggaranPendapatan");
        loadPosPendapatan();
    }

    public void loadPosPendapatan() throws Exception, Exception, Exception {
        cmbboxPosPendapatan.getItems().clear();
        for (Map map : rapuusadao.getPosPendapatan()) {
            Comboitem item = new Comboitem();
            item.setValue(map);
            item.setLabel(map.get("posPendapatan").toString());
            cmbboxPosPendapatan.appendChild(item);
        }
    }

    public void cmbboxPosPendapatanOnSelect() throws Exception {
        clearData();
        Map map = (Map) cmbboxPosPendapatan.getSelectedItem().getValue();
        txtboxIdPosPendapatan.setValue(map.get("idPosPendapatan").toString());
        if (Integer.valueOf(map.get("idPosPendapatan").toString()) == 1 || Integer.valueOf(map.get("idPosPendapatan").toString()) == 4) {
            groupboxSKS.setVisible(false);
            groupboxSKSDetail.setVisible(false);
            groupboxUKTAsuransi.setVisible(true);
            groupboxUKTAsuransiDetail.setVisible(true);
        } else {
            groupboxUKTAsuransi.setVisible(false);
            groupboxSKS.setVisible(true);
            groupboxSKSDetail.setVisible(true);
            groupboxUKTAsuransiDetail.setVisible(false);
        }
        lblProgramStudi.setValue("");
        loadPosPendapatanForListBox();
    }

    public void loadPosPendapatanForListBox() throws Exception {
        lstboxUnitKerja.getItems().clear();
        for (Map m : rapuusadao.getUnitKerja(session.getThnAnggaran(), Integer.valueOf(txtboxIdPosPendapatan.getValue().toString()))) {
            Listitem item = new Listitem();
            item.setValue(m);
            item.appendChild(new Listcell(m.get("Nama_unit_kerja").toString()));
            Listcell listcell = new Listcell();
            Checkbox checkbox = new Checkbox();
            checkbox.setDisabled(true);
            checkbox.setChecked(true);
            listcell.appendChild(checkbox);
            item.appendChild(listcell);
            lstboxUnitKerja.appendChild(item);
        }
    }

    public void lstboxUnitKerjaOnSelect() throws Exception {
        Map map = (Map) lstboxUnitKerja.getSelectedItem().getValue();
        lblProgramStudi.setValue(map.get("Nama_unit_kerja").toString());
        txtboxKodeProdi.setValue(map.get("kodeProdi").toString());
        txtboxIdPosAnggaranPendapatan.setValue(map.get("idPosPendapatanUnit").toString());
        loadPosAnggaranPendapatanUnitUKTAsuransi();
        Integer tahun = Integer.valueOf(session.getThnAnggaran().toString()) - 1;
        if (Integer.valueOf(txtboxIdPosPendapatan.getValue().toString()) == 1 || Integer.valueOf(txtboxIdPosPendapatan.getValue().toString()) == 4) {
            lstboxDetailUKTAsuransiX.getItems().clear();
            int idProsentaseX = 0;
            float totalAnggaranBawah = 0;
            float totalAnggaranAtas = 0;
            try {
                for (Map m : rapuusadao.getDetailProdiUKTAsuransi(txtboxKodeProdi.getValue(), tahun.toString())) {
                    final Listitem item = new Listitem();
                    String tahunAngkatan = m.get("tahunAngkatan").toString();
                    item.appendChild(new Listcell(tahunAngkatan));
                    item.appendChild(new Listcell("Tidak"));
                    final int jumlahMahasiswa = rapuusadao.getJumlahMahasiswa(txtboxKodeProdi.getValue(), tahun.toString(), tahunAngkatan.substring(2, 4));
                    item.appendChild(new Listcell(String.valueOf(jumlahMahasiswa)));
                    final float tarif = rapuusadao.getTarif(Integer.valueOf(txtboxIdPosPendapatan.getValue()), Integer.toString(tahun), tahunAngkatan, txtboxKodeProdi.getValue().toString());
                    item.appendChild(new Listcell(String.valueOf(ClassUtility.formatMoney(tarif))));
                    Listcell cellProsentaseX = new Listcell();
                    final Doublebox txtboxProsentaseX = new Doublebox();
                    txtboxProsentaseX.setId("txtboxProsentaseX" + idProsentaseX);
                    txtboxProsentaseX.setValue(0);
                    txtboxProsentaseX.setStyle("text-align:right");
                    cellProsentaseX.appendChild(txtboxProsentaseX);
                    item.appendChild(cellProsentaseX);
                    Listcell cellJmlAnggaran = new Listcell();
                    final Label lblJmlAnggaran = new Label();
                    try {
                        RencanaAnggaranPendapatanUangUktSksAsuransi uktSksAsuransi = rapuusadao.getProsentase(Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue().toString()), lblUKTAsuransiDetailX.getValue(), tahunAngkatan);
                        txtboxProsentaseX.setValue(uktSksAsuransi.getProsentase());
                        try {
                            float jumlahAnggaran = jumlahMahasiswa * tarif * (Float.parseFloat(txtboxProsentaseX.getValue().toString()) / 100);
                            lblJmlAnggaran.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                            item.setValue(jumlahAnggaran);
                            totalAnggaranAtas += jumlahAnggaran;
                            lblJmlAnggaranUKTAsuransiX.setValue(ClassUtility.formatMoney(totalAnggaranAtas));
                        } catch (NullPointerException e) {
                            txtboxProsentaseX.setValue(0);
                        }
                    } catch (NullPointerException e) {
                        txtboxProsentaseX.setValue(0);
                    }
                    txtboxProsentaseX.addEventListener("onChange", new EventListener() {

                        @Override
                        public void onEvent(Event arg0) throws Exception {
                            try {
                                float prosentase = (float) (txtboxProsentaseX.getValue() / 100);
                                float jumlahAnggaran = jumlahMahasiswa * tarif * prosentase;
                                lblJmlAnggaran.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                                item.setValue(jumlahAnggaran);
                            } catch (NullPointerException e) {
                                txtboxProsentaseX.setValue(0);
                            }
                        }
                    });
                    txtboxProsentaseX.addEventListener("onChange", new EventListener() {

                        @Override
                        public void onEvent(Event arg0) throws Exception {
                            List<Listitem> items = lstboxDetailUKTAsuransiX.getItems();
                            float totals = 0;
                            for (Listitem i : items) {
                                if (i.getValue() != null) {
                                    totals += (Float) i.getValue();
                                }
                            }
                            lblJmlAnggaranUKTAsuransiX.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                        }
                    });
                    cellJmlAnggaran.appendChild(lblJmlAnggaran);
                    item.appendChild(cellJmlAnggaran);
                    idProsentaseX++;
                    lstboxDetailUKTAsuransiX.appendChild(item);
                }
            } catch (Exception ex) {
                Logger.getLogger(RencanaAnggaranPendapatanUangUktSksAsuransiWnd.class.getName()).log(Level.SEVERE, null, ex);
            }
            lstboxDetailUKTAsuransiY.getItems().clear();
            try {
                int id = 1;
                final Listitem listitem = new Listitem();
                listitem.appendChild(new Listcell(session.getThnAnggaran()));
                listitem.appendChild(new Listcell("Ya"));
                Listcell cellJmlMhs = new Listcell();
                final Intbox txtboxJmlMhsAtas = new Intbox();
                txtboxJmlMhsAtas.setId("txtboxJmlMhs");
                txtboxJmlMhsAtas.setValue(0);
                txtboxJmlMhsAtas.setStyle("text-align:right");
                cellJmlMhs.appendChild(txtboxJmlMhsAtas);
                listitem.appendChild(cellJmlMhs);
                Listcell cellTarifAtas = new Listcell();
                final Doublebox txtboxTarifAtas = new Doublebox();
                txtboxTarifAtas.setId("txtboxTarif");
                txtboxTarifAtas.setValue(0);
                txtboxTarifAtas.setStyle("text-align:right");
                cellTarifAtas.appendChild(txtboxTarifAtas);
                listitem.appendChild(cellTarifAtas);
                Listcell cellProsentaseY = new Listcell();
                final Doublebox txtboxProsentaseYAtas = new Doublebox();
                txtboxProsentaseYAtas.setId("txtboxProsentaseY");
                txtboxProsentaseYAtas.setValue(0);
                txtboxProsentaseYAtas.setStyle("text-align:right");
                final Label lblJmlAnggaranAtas = new Label();
                try {
                    RencanaAnggaranPendapatanUangUktSksAsuransi uktSksAsuransi = rapuusadao.getProsentase(Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue().toString()), lblUKTAsuransiDetailY.getValue(), session.getThnAnggaran());
                    txtboxProsentaseYAtas.setValue(uktSksAsuransi.getProsentase());
                    txtboxJmlMhsAtas.setValue(uktSksAsuransi.getJmlMhsDU());
                    txtboxTarifAtas.setValue(uktSksAsuransi.getTarif());
                    try {
                        float jumlahAnggaran = txtboxJmlMhsAtas.getValue() * Float.valueOf(txtboxTarifAtas.getValue().toString()) * (Float.parseFloat(txtboxProsentaseYAtas.getValue().toString()) / 100);
                        lblJmlAnggaranAtas.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                        listitem.setValue(jumlahAnggaran);
                        totalAnggaranBawah += jumlahAnggaran;
                        lblJmlAnggaranUKTAsuransiY.setValue(ClassUtility.formatMoney(totalAnggaranBawah));
                    } catch (NullPointerException e) {
                        txtboxProsentaseYAtas.setValue(0);
                    }
                } catch (NullPointerException e) {
                    txtboxProsentaseYAtas.setValue(0);
                    txtboxTarifAtas.setValue(0);
                    txtboxJmlMhsAtas.setValue(0);
                }
                txtboxJmlMhsAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        try {
                            float prosentase = (float) (txtboxProsentaseYAtas.getValue() / 100);
                            float jumlahAnggaran = Float.valueOf(txtboxJmlMhsAtas.getValue().toString()) * Float.valueOf(txtboxTarifAtas.getValue().toString()) * prosentase;
                            lblJmlAnggaranAtas.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                            listitem.setValue(jumlahAnggaran);
                        } catch (NullPointerException e) {
                            txtboxJmlMhsAtas.setValue(0);
                        }
                    }
                });
                txtboxJmlMhsAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        List<Listitem> items = lstboxDetailUKTAsuransiY.getItems();
                        float totals = 0;
                        for (Listitem i : items) {
                            if (i.getValue() != null) {
                                totals += (Float) i.getValue();
                            }
                        }
                        lblJmlAnggaranUKTAsuransiY.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                    }
                });
                txtboxTarifAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        try {
                            float prosentase = (float) (txtboxProsentaseYAtas.getValue() / 100);
                            float jumlahAnggaran = Float.valueOf(txtboxJmlMhsAtas.getValue().toString()) * Float.valueOf(txtboxTarifAtas.getValue().toString()) * prosentase;
                            lblJmlAnggaranAtas.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                            listitem.setValue(jumlahAnggaran);
                        } catch (NullPointerException e) {
                            txtboxTarifAtas.setValue(0);
                        }
                    }
                });
                txtboxTarifAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        List<Listitem> items = lstboxDetailUKTAsuransiY.getItems();
                        float totals = 0;
                        for (Listitem i : items) {
                            if (i.getValue() != null) {
                                totals += (Float) i.getValue();
                            }
                        }
                        lblJmlAnggaranUKTAsuransiY.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                    }
                });
                txtboxProsentaseYAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        try {
                            float prosentase = (float) (txtboxProsentaseYAtas.getValue() / 100);
                            float jumlahAnggaran = Float.valueOf(txtboxJmlMhsAtas.getValue().toString()) * Float.valueOf(txtboxTarifAtas.getValue().toString()) * prosentase;
                            lblJmlAnggaranAtas.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                            listitem.setValue(jumlahAnggaran);
                        } catch (NullPointerException e) {
                            txtboxProsentaseYAtas.setValue(0);
                        }
                    }
                });
                txtboxProsentaseYAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        List<Listitem> items = lstboxDetailUKTAsuransiY.getItems();
                        float totals = 0;
                        for (Listitem i : items) {
                            if (i.getValue() != null) {
                                totals += (Float) i.getValue();
                            }
                        }
                        lblJmlAnggaranUKTAsuransiY.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                    }
                });
                Listcell cellJmlAnggaranAtas = new Listcell();
                cellJmlAnggaranAtas.appendChild(lblJmlAnggaranAtas);
                cellProsentaseY.appendChild(txtboxProsentaseYAtas);
                listitem.appendChild(cellProsentaseY);
                listitem.appendChild(cellJmlAnggaranAtas);
                lstboxDetailUKTAsuransiY.appendChild(listitem);
                for (Map m : rapuusadao.getDetailProdiUKTAsuransi(txtboxKodeProdi.getValue(), tahun.toString())) {
                    final Listitem item = new Listitem();
                    String tahunAngkatan = m.get("tahunAngkatan").toString();
                    item.appendChild(new Listcell(tahunAngkatan));
                    item.appendChild(new Listcell("Tidak"));
                    final int jumlahMahasiswa = rapuusadao.getJumlahMahasiswa(txtboxKodeProdi.getValue(), tahun.toString(), tahunAngkatan.substring(2, 4));
                    cellJmlMhs = new Listcell();
                    Label lblJmlMhs = new Label();
                    lblJmlMhs.setId("txtboxJmlMhs" + id);
                    lblJmlMhs.setValue(jumlahMahasiswa + "");
                    cellJmlMhs.appendChild(lblJmlMhs);
                    item.appendChild(cellJmlMhs);
                    final float tarif = rapuusadao.getTarif(Integer.valueOf(txtboxIdPosPendapatan.getValue()), Integer.toString(tahun), tahunAngkatan, txtboxKodeProdi.getValue().toString());
                    cellTarifAtas = new Listcell();
                    final Doublebox txtboxTarifBawah = new Doublebox();
                    txtboxTarifBawah.setId("txtboxTarif" + id);
                    txtboxTarifBawah.setValue(tarif);
                    txtboxTarifBawah.setStyle("text-align:right");
                    cellTarifAtas.appendChild(txtboxTarifBawah);
                    item.appendChild(cellTarifAtas);
                    cellProsentaseY = new Listcell();
                    final Doublebox txtboxProsentaseYBawah = new Doublebox();
                    txtboxProsentaseYBawah.setId("txtboxProsentaseY" + id);
                    txtboxProsentaseYBawah.setValue(0);
                    txtboxProsentaseYBawah.setStyle("text-align:right");
                    cellProsentaseY.appendChild(txtboxProsentaseYBawah);
                    item.appendChild(cellProsentaseY);
                    Listcell cellJmlAnggaranBawah = new Listcell();
                    final Label lblJmlAnggaranBawah = new Label();
                    try {
                        RencanaAnggaranPendapatanUangUktSksAsuransi uktSksAsuransi = rapuusadao.getProsentase(Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue().toString()), lblUKTAsuransiDetailY.getValue(), tahunAngkatan);
                        txtboxProsentaseYBawah.setValue(uktSksAsuransi.getProsentase());
                        txtboxTarifBawah.setValue(uktSksAsuransi.getTarif());
                        try {
                            float jumlahAnggaran = jumlahMahasiswa * Float.valueOf(txtboxTarifBawah.getValue().toString()) * (Float.parseFloat(txtboxProsentaseYBawah.getValue().toString()) / 100);
                            lblJmlAnggaranBawah.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                            listitem.setValue(jumlahAnggaran);
                            totalAnggaranBawah += jumlahAnggaran;
                            lblJmlAnggaranUKTAsuransiY.setValue(ClassUtility.formatMoney(totalAnggaranBawah));
                        } catch (NullPointerException e) {
                            txtboxProsentaseYBawah.setValue(0);
                        }
                    } catch (NullPointerException e) {
                        txtboxProsentaseYBawah.setValue(0);
                        txtboxTarifBawah.setValue(tarif);
                    }
                    txtboxTarifBawah.addEventListener("onChange", new EventListener() {

                        @Override
                        public void onEvent(Event arg0) throws Exception {
                            try {
                                float jumlahAnggaran = jumlahMahasiswa * Float.valueOf(txtboxTarifBawah.getValue().toString()) * Float.parseFloat(txtboxProsentaseYBawah.getValue().toString()) / 100;
                                lblJmlAnggaranBawah.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                                item.setValue(jumlahAnggaran);
                            } catch (NullPointerException e) {
                                txtboxTarifBawah.setValue(0);
                            }
                        }
                    });
                    txtboxTarifBawah.addEventListener("onChange", new EventListener() {

                        @Override
                        public void onEvent(Event arg0) throws Exception {
                            List<Listitem> items = lstboxDetailUKTAsuransiY.getItems();
                            float totals = 0;
                            for (Listitem i : items) {
                                if (i.getValue() != null) {
                                    totals += (Float) i.getValue();
                                }
                            }
                            lblJmlAnggaranUKTAsuransiY.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                        }
                    });
                    txtboxProsentaseYBawah.addEventListener("onChange", new EventListener() {

                        @Override
                        public void onEvent(Event arg0) throws Exception {
                            try {
                                float jumlahAnggaran = jumlahMahasiswa * Float.valueOf(txtboxTarifBawah.getValue().toString()) * Float.parseFloat(txtboxProsentaseYBawah.getValue().toString()) / 100;
                                lblJmlAnggaranBawah.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                                item.setValue(jumlahAnggaran);
                            } catch (NullPointerException e) {
                                txtboxProsentaseYBawah.setValue(0);
                            }
                        }
                    });
                    txtboxProsentaseYBawah.addEventListener("onChange", new EventListener() {

                        @Override
                        public void onEvent(Event arg0) throws Exception {
                            List<Listitem> items = lstboxDetailUKTAsuransiY.getItems();
                            float totals = 0;
                            for (Listitem i : items) {
                                if (i.getValue() != null) {
                                    totals += (Float) i.getValue();
                                }
                            }
                            lblJmlAnggaranUKTAsuransiY.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                        }
                    });
                    cellJmlAnggaranBawah.appendChild(lblJmlAnggaranBawah);
                    item.appendChild(cellJmlAnggaranBawah);
                    lstboxDetailUKTAsuransiY.appendChild(item);
                    id++;
                }
            } catch (Exception ex) {
                Logger.getLogger(RencanaAnggaranPendapatanUangUktSksAsuransiWnd.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            lstboxSKSDetailX.getItems().clear();
            int idProsentaseX = 0;
            float totalAnggaranBawah = 0;
            float totalAnggaranAtas = 0;
            try {
                for (Map m : rapuusadao.getDetailProdiSKS(txtboxKodeProdi.getValue(), tahun.toString())) {
                    final Listitem item = new Listitem();
                    String tahunAngkatan = m.get("tahunAngkatan").toString();
                    item.appendChild(new Listcell(tahunAngkatan));
                    item.appendChild(new Listcell("Tidak"));
                    final int jumlahMahasiswa = rapuusadao.getJumlahMahasiswa(txtboxKodeProdi.getValue(), tahun.toString(), tahunAngkatan.substring(2, 4));
                    item.appendChild(new Listcell(String.valueOf(jumlahMahasiswa)));
                    final float tarif = rapuusadao.getTarif(Integer.valueOf(txtboxIdPosPendapatan.getValue()), Integer.toString(tahun), tahunAngkatan, txtboxKodeProdi.getValue().toString());
                    item.appendChild(new Listcell(String.valueOf(ClassUtility.formatMoney(tarif))));
                    final int jumlahSKS = rapuusadao.getJumlahSKS(String.valueOf(tahun), tahunAngkatan.substring(2, 4), txtboxKodeProdi.getValue());
                    item.appendChild(new Listcell(String.valueOf(jumlahSKS)));
                    Listcell cellJmlAnggaran = new Listcell();
                    final Label lblJmlAnggaran = new Label();
                    Listcell cellProsentaseX = new Listcell();
                    final Doublebox txtboxProsentaseSKSX = new Doublebox();
                    txtboxProsentaseSKSX.setId("txtboxProsentaseXSKS" + idProsentaseX);
                    txtboxProsentaseSKSX.setStyle("text-align:right");
                    try {
                        RencanaAnggaranPendapatanUangUktSksAsuransi uktSksAsuransi = rapuusadao.getProsentase(Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue().toString()), lblSKSDetailX.getValue(), tahunAngkatan);
                        txtboxProsentaseSKSX.setValue(uktSksAsuransi.getProsentase());
                        try {
                            float prosentase = (float) (txtboxProsentaseSKSX.getValue() / 100);
                            float jumlahAnggaran = jumlahMahasiswa * tarif * jumlahSKS * prosentase;
                            lblJmlAnggaran.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                            item.setValue(jumlahAnggaran);
                            totalAnggaranAtas += jumlahAnggaran;
                            lblJmlAnggaranSKSX.setValue(String.valueOf(ClassUtility.formatMoney(totalAnggaranAtas)));
                        } catch (NullPointerException e) {
                            txtboxProsentaseSKSX.setValue(0);
                        }
                    } catch (NullPointerException e) {
                        txtboxProsentaseSKSX.setValue(0);
                    }
                    cellProsentaseX.appendChild(txtboxProsentaseSKSX);
                    item.appendChild(cellProsentaseX);
                    txtboxProsentaseSKSX.addEventListener("onChange", new EventListener() {

                        @Override
                        public void onEvent(Event arg0) throws Exception {
                            try {
                                float prosentase = (float) (txtboxProsentaseSKSX.getValue() / 100);
                                float jumlahAnggaran = jumlahMahasiswa * tarif * jumlahSKS * prosentase;
                                lblJmlAnggaran.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                                item.setValue(jumlahAnggaran);
                            } catch (NullPointerException e) {
                                txtboxProsentaseSKSX.setValue(0);
                            }
                        }
                    });
                    txtboxProsentaseSKSX.addEventListener("onChange", new EventListener() {

                        @Override
                        public void onEvent(Event arg0) throws Exception {
                            List<Listitem> items = lstboxSKSDetailX.getItems();
                            float totals = 0;
                            for (Listitem i : items) {
                                if (i.getValue() != null) {
                                    totals += (Float) i.getValue();
                                }
                            }
                            lblJmlAnggaranSKSX.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                        }
                    });
                    cellJmlAnggaran.appendChild(lblJmlAnggaran);
                    item.appendChild(cellJmlAnggaran);
                    lstboxSKSDetailX.appendChild(item);
                    idProsentaseX++;
                }
            } catch (Exception ex) {
                Logger.getLogger(RencanaAnggaranPendapatanUangUktSksAsuransiWnd.class.getName()).log(Level.SEVERE, null, ex);
            }
            lstboxSKSDetailY.getItems().clear();
            try {
                int idProsentaseY = 1;
                int idTarif = 1;
                int idJmlMhs = 1;
                final Listitem listitem = new Listitem();
                listitem.appendChild(new Listcell(session.getThnAnggaran()));
                listitem.appendChild(new Listcell("Ya"));
                Listcell cellJmlMhs = new Listcell();
                final Intbox txtboxJmlMhsAtas = new Intbox();
                txtboxJmlMhsAtas.setId("txtboxJmlMhsSKS");
                txtboxJmlMhsAtas.setValue(0);
                txtboxJmlMhsAtas.setStyle("text-align:right");
                cellJmlMhs.appendChild(txtboxJmlMhsAtas);
                listitem.appendChild(cellJmlMhs);
                Listcell cellTarif = new Listcell();
                final Doublebox txtboxTarifAtas = new Doublebox();
                txtboxTarifAtas.setId("txtboxTarifSKS");
                txtboxTarifAtas.setValue(0);
                txtboxTarifAtas.setStyle("text-align:right");
                cellTarif.appendChild(txtboxTarifAtas);
                listitem.appendChild(cellTarif);
                Listcell cellSKS = new Listcell();
                final Intbox txtboxJmlSKSAtas = new Intbox();
                txtboxJmlSKSAtas.setId("txtboxJmlSKS");
                txtboxJmlSKSAtas.setValue(0);
                txtboxJmlSKSAtas.setStyle("text-align:right");
                cellSKS.appendChild(txtboxJmlSKSAtas);
                listitem.appendChild(cellSKS);
                Listcell cellJmlAnggaranAtas = new Listcell();
                final Label lblJmlAnggaranAtas = new Label();
                Listcell cellProsentaseY = new Listcell();
                final Doublebox txtboxProsentaseSKSYAtas = new Doublebox();
                txtboxProsentaseSKSYAtas.setId("txtboxProsentaseSKSY");
                txtboxProsentaseSKSYAtas.setStyle("text-align:right");
                try {
                    RencanaAnggaranPendapatanUangUktSksAsuransi uktSksAsuransi = rapuusadao.getProsentase(Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue().toString()), lblSKSDetailY.getValue(), session.getThnAnggaran());
                    txtboxProsentaseSKSYAtas.setValue(uktSksAsuransi.getProsentase());
                    txtboxJmlMhsAtas.setValue(uktSksAsuransi.getJmlMhsDU());
                    txtboxJmlSKSAtas.setValue(uktSksAsuransi.getJmlSKS());
                    txtboxTarifAtas.setValue(uktSksAsuransi.getTarif());
                    try {
                        float prosentase = (float) (txtboxProsentaseSKSYAtas.getValue() / 100);
                        float jumlahAnggaran = Float.valueOf(txtboxJmlMhsAtas.getValue().toString()) * Float.valueOf(txtboxTarifAtas.getValue().toString()) * Integer.valueOf(txtboxJmlSKSAtas.getValue().toString()) * prosentase;
                        lblJmlAnggaranAtas.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                        listitem.setValue(jumlahAnggaran);
                        totalAnggaranBawah += jumlahAnggaran;
                    } catch (NullPointerException e) {
                        txtboxJmlMhsAtas.setValue(0);
                    }
                } catch (NullPointerException e) {
                    txtboxProsentaseSKSYAtas.setValue(0);
                    txtboxJmlMhsAtas.setValue(0);
                    txtboxJmlSKSAtas.setValue(0);
                    txtboxTarifAtas.setValue(0);
                }
                cellProsentaseY.appendChild(txtboxProsentaseSKSYAtas);
                listitem.appendChild(cellProsentaseY);
                txtboxJmlMhsAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        try {
                            float prosentase = (float) (txtboxProsentaseSKSYAtas.getValue() / 100);
                            float jumlahAnggaran = Float.valueOf(txtboxJmlMhsAtas.getValue().toString()) * Float.valueOf(txtboxTarifAtas.getValue().toString()) * Integer.valueOf(txtboxJmlSKSAtas.getValue().toString()) * prosentase;
                            lblJmlAnggaranAtas.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                            listitem.setValue(jumlahAnggaran);
                        } catch (NullPointerException e) {
                            txtboxJmlMhsAtas.setValue(0);
                        }
                    }
                });
                txtboxJmlMhsAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        List<Listitem> items = lstboxSKSDetailY.getItems();
                        float totals = 0;
                        for (Listitem i : items) {
                            if (i.getValue() != null) {
                                totals += (Float) i.getValue();
                            }
                        }
                        lblJmlAnggaranSKSY.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                    }
                });
                txtboxTarifAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        try {
                            float prosentase = (float) (txtboxProsentaseSKSYAtas.getValue() / 100);
                            float jumlahAnggaran = Float.valueOf(txtboxJmlMhsAtas.getValue().toString()) * Float.valueOf(txtboxTarifAtas.getValue().toString()) * Integer.valueOf(txtboxJmlSKSAtas.getValue().toString()) * prosentase;
                            lblJmlAnggaranAtas.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                            listitem.setValue(jumlahAnggaran);
                        } catch (NullPointerException e) {
                            txtboxTarifAtas.setValue(0);
                        }
                    }
                });
                txtboxTarifAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        List<Listitem> items = lstboxSKSDetailY.getItems();
                        float totals = 0;
                        for (Listitem i : items) {
                            if (i.getValue() != null) {
                                totals += (Float) i.getValue();
                            }
                        }
                        lblJmlAnggaranSKSY.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                    }
                });
                txtboxJmlSKSAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        try {
                            float prosentase = (float) (txtboxProsentaseSKSYAtas.getValue() / 100);
                            float jumlahAnggaran = Float.valueOf(txtboxJmlMhsAtas.getValue().toString()) * Float.valueOf(txtboxTarifAtas.getValue().toString()) * Integer.valueOf(txtboxJmlSKSAtas.getValue().toString()) * prosentase;
                            lblJmlAnggaranAtas.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                            listitem.setValue(jumlahAnggaran);
                        } catch (NullPointerException e) {
                            txtboxJmlSKSAtas.setValue(0);
                        }
                    }
                });
                txtboxJmlSKSAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        List<Listitem> items = lstboxSKSDetailY.getItems();
                        float totals = 0;
                        for (Listitem i : items) {
                            if (i.getValue() != null) {
                                totals += (Float) i.getValue();
                            }
                        }
                        lblJmlAnggaranSKSY.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                    }
                });
                txtboxProsentaseSKSYAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        try {
                            float prosentase = (float) (txtboxProsentaseSKSYAtas.getValue() / 100);
                            float jumlahAnggaran = Float.valueOf(txtboxJmlMhsAtas.getValue().toString()) * Float.valueOf(txtboxTarifAtas.getValue().toString()) * Integer.valueOf(txtboxJmlSKSAtas.getValue().toString()) * prosentase;
                            lblJmlAnggaranAtas.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                            listitem.setValue(jumlahAnggaran);
                        } catch (NullPointerException e) {
                            txtboxProsentaseSKSYAtas.setValue(0);
                        }
                    }
                });
                txtboxProsentaseSKSYAtas.addEventListener("onChange", new EventListener() {

                    @Override
                    public void onEvent(Event arg0) throws Exception {
                        List<Listitem> items = lstboxSKSDetailY.getItems();
                        float totals = 0;
                        for (Listitem i : items) {
                            if (i.getValue() != null) {
                                totals += (Float) i.getValue();
                            }
                        }
                        lblJmlAnggaranSKSY.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                    }
                });
                cellJmlAnggaranAtas.appendChild(lblJmlAnggaranAtas);
                listitem.appendChild(cellJmlAnggaranAtas);
                lstboxSKSDetailY.appendChild(listitem);
                for (Map m : rapuusadao.getDetailProdiSKS(txtboxKodeProdi.getValue(), tahun.toString())) {
                    final Listitem item = new Listitem();
                    String tahunAngkatan = m.get("tahunAngkatan").toString();
                    item.appendChild(new Listcell(tahunAngkatan));
                    item.appendChild(new Listcell("Tidak"));
                    final int jumlahMahasiswa = rapuusadao.getJumlahMahasiswa(txtboxKodeProdi.getValue(), tahun.toString(), tahunAngkatan.substring(2, 4));
                    item.appendChild(new Listcell(String.valueOf(jumlahMahasiswa)));
                    final float tarif = rapuusadao.getTarif(Integer.valueOf(txtboxIdPosPendapatan.getValue()), Integer.toString(tahun), tahunAngkatan, txtboxKodeProdi.getValue().toString());
                    cellTarif = new Listcell();
                    final Doublebox txtboxTarifBawah = new Doublebox();
                    txtboxTarifBawah.setId("txtboxTarifSKS" + idTarif);
                    txtboxTarifBawah.setValue(tarif);
                    txtboxTarifBawah.setStyle("text-align:right");
                    cellTarif.appendChild(txtboxTarifBawah);
                    item.appendChild(cellTarif);
                    final int jumlahSKS = rapuusadao.getJumlahSKS(String.valueOf(tahun), tahunAngkatan.substring(2, 4), txtboxKodeProdi.getValue());
                    item.appendChild(new Listcell(jumlahSKS + ""));
                    Listcell cellJmlAnggaranBawah = new Listcell();
                    final Label lblJmlAnggaranBawah = new Label();
                    cellProsentaseY = new Listcell();
                    final Doublebox txtboxProsentaseSKSYBawah = new Doublebox();
                    txtboxProsentaseSKSYBawah.setId("txtboxProsentaseSKSY" + idProsentaseY);
                    txtboxProsentaseSKSYBawah.setStyle("text-align:right");
                    try {
                        RencanaAnggaranPendapatanUangUktSksAsuransi uktSksAsuransi = rapuusadao.getProsentase(Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue().toString()), lblSKSDetailY.getValue(), tahunAngkatan);
                        txtboxProsentaseSKSYBawah.setValue(uktSksAsuransi.getProsentase());
                        txtboxTarifBawah.setValue(uktSksAsuransi.getTarif());
                        try {
                            float jumlahAnggaran = jumlahMahasiswa * Float.valueOf(txtboxTarifBawah.getValue().toString()) * jumlahSKS * (Float.parseFloat(txtboxProsentaseSKSYBawah.getValue().toString()) / 100);
                            lblJmlAnggaranBawah.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                            item.setValue(jumlahAnggaran);
                            totalAnggaranBawah += jumlahAnggaran;
                            lblJmlAnggaranSKSY.setValue(ClassUtility.formatMoney(totalAnggaranBawah));
                        } catch (NullPointerException e) {
                            txtboxTarifBawah.setValue(0);
                        }
                    } catch (NullPointerException e) {
                        txtboxProsentaseSKSYBawah.setValue(0);
                    }
                    cellProsentaseY.appendChild(txtboxProsentaseSKSYBawah);
                    item.appendChild(cellProsentaseY);
                    txtboxTarifBawah.addEventListener("onChange", new EventListener() {

                        @Override
                        public void onEvent(Event arg0) throws Exception {
                            try {
                                float jumlahAnggaran = jumlahMahasiswa * Float.valueOf(txtboxTarifBawah.getValue().toString()) * jumlahSKS * (Float.parseFloat(txtboxProsentaseSKSYBawah.getValue().toString()) / 100);
                                lblJmlAnggaranBawah.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                                item.setValue(jumlahAnggaran);
                            } catch (NullPointerException e) {
                                txtboxProsentaseSKSYBawah.setValue(0);
                            }
                        }
                    });
                    txtboxTarifBawah.addEventListener("onChange", new EventListener() {

                        @Override
                        public void onEvent(Event arg0) throws Exception {
                            List<Listitem> items = lstboxSKSDetailY.getItems();
                            float totals = 0;
                            for (Listitem i : items) {
                                if (i.getValue() != null) {
                                    totals += (Float) i.getValue();
                                }
                            }
                            lblJmlAnggaranSKSY.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                        }
                    });
                    txtboxProsentaseSKSYBawah.addEventListener("onChange", new EventListener() {

                        @Override
                        public void onEvent(Event arg0) throws Exception {
                            try {
                                float jumlahAnggaran = jumlahMahasiswa * Float.valueOf(txtboxTarifBawah.getValue().toString()) * jumlahSKS * (Float.parseFloat(txtboxProsentaseSKSYBawah.getValue().toString()) / 100);
                                lblJmlAnggaranBawah.setValue(String.valueOf(ClassUtility.formatMoney(jumlahAnggaran)));
                                item.setValue(jumlahAnggaran);
                            } catch (NullPointerException e) {
                                txtboxProsentaseSKSYBawah.setValue(0);
                            }
                        }
                    });
                    txtboxProsentaseSKSYBawah.addEventListener("onChange", new EventListener() {

                        @Override
                        public void onEvent(Event arg0) throws Exception {
                            List<Listitem> items = lstboxSKSDetailY.getItems();
                            float totals = 0;
                            for (Listitem i : items) {
                                if (i.getValue() != null) {
                                    totals += (Float) i.getValue();
                                }
                            }
                            lblJmlAnggaranSKSY.setValue(String.valueOf(ClassUtility.formatMoney(totals)));
                        }
                    });
                    lstboxSKSDetailY.appendChild(item);
                    cellJmlAnggaranBawah.appendChild(lblJmlAnggaranBawah);
                    item.appendChild(cellJmlAnggaranBawah);
                    idTarif++;
                    idProsentaseY++;
                }
            } catch (Exception ex) {
                Logger.getLogger(RencanaAnggaranPendapatanUangUktSksAsuransiWnd.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void btnSimpanOnClick() throws InterruptedException {
        boolean simpan = false;
        Integer tahun = Integer.valueOf(session.getThnAnggaran().toString()) - 1;
        if (Integer.valueOf(txtboxIdPosPendapatan.getValue().toString()) == 1 || Integer.valueOf(txtboxIdPosPendapatan.getValue().toString()) == 4) {
            boolean isPernahDisimpan = false;
            try {
                int i = 0;
                for (Map m : rapuusadao.getDetailProdiUKTAsuransi(txtboxKodeProdi.getValue(), tahun.toString())) {
                    String tahunAngkatan = m.get("tahunAngkatan").toString();
                    Doublebox doubleboxProsentaseX = (Doublebox) getFellow("txtboxProsentaseX" + i);
                    System.out.println(doubleboxProsentaseX.getValue());
                    RencanaAnggaranPendapatanUangUktSksAsuransi uktSksAsuransi = new RencanaAnggaranPendapatanUangUktSksAsuransi();
                    uktSksAsuransi.setIdPosPendapatanUnit(Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue()));
                    uktSksAsuransi.setPeriode(lblUKTAsuransiDetailX.getValue());
                    uktSksAsuransi.setTahunAngkatan(tahunAngkatan);
                    uktSksAsuransi.setIsAngkatanBaru("N");
                    int jmlMhsDU = rapuusadao.getJumlahMahasiswa(txtboxKodeProdi.getValue(), tahun.toString(), tahunAngkatan.substring(2, 4));
                    uktSksAsuransi.setJmlMhsDU(jmlMhsDU);
                    int jumlahSKS = rapuusadao.getJumlahSKS(String.valueOf(tahun), tahunAngkatan.substring(2, 4), txtboxKodeProdi.getValue());
                    float tarif = 0;
                    try {
                        tarif = rapuusadao.getTarif(Integer.valueOf(txtboxIdPosPendapatan.getValue()), Integer.toString(tahun), tahunAngkatan, txtboxKodeProdi.getValue().toString());
                    } catch (NullPointerException e) {
                    }
                    uktSksAsuransi.setTarif(tarif);
                    uktSksAsuransi.setJmlSKS(jumlahSKS);
                    uktSksAsuransi.setProsentase(Float.valueOf(doubleboxProsentaseX.getValue().toString()));
                    Boolean cekData = rapuusadao.cekData(uktSksAsuransi.getIdPosPendapatanUnit(), uktSksAsuransi.getPeriode(), uktSksAsuransi.getTahunAngkatan());
                    if (!cekData) {
                        rapuusadao.insert(uktSksAsuransi);
                    } else {
                        rapuusadao.update(uktSksAsuransi);
                    }
                    i++;
                }
            } catch (Exception ex) {
                Logger.getLogger(RencanaAnggaranPendapatanUangUktSksAsuransiWnd.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                int i = 1;
                Doublebox doubleboxProsentaseX = (Doublebox) getFellow("txtboxProsentaseY");
                RencanaAnggaranPendapatanUangUktSksAsuransi uktSksAsuransi = new RencanaAnggaranPendapatanUangUktSksAsuransi();
                uktSksAsuransi.setIdPosPendapatanUnit(Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue()));
                uktSksAsuransi.setPeriode(lblUKTAsuransiDetailY.getValue());
                uktSksAsuransi.setTahunAngkatan(session.getThnAnggaran());
                uktSksAsuransi.setIsAngkatanBaru("Y");
                int jumlahSKS = rapuusadao.getJumlahSKS(String.valueOf(tahun), session.getThnAnggaran(), txtboxKodeProdi.getValue());
                Intbox intboxJmlMhs = (Intbox) getFellow("txtboxJmlMhs");
                Doublebox dblboxTarif = (Doublebox) getFellow("txtboxTarif");
                float tarif = 0;
                try {
                    uktSksAsuransi.setJmlMhsDU(intboxJmlMhs.getValue());
                } catch (NullPointerException exception) {
                    uktSksAsuransi.setJmlMhsDU(0);
                }
                try {
                    uktSksAsuransi.setTarif(Float.valueOf(dblboxTarif.getValue().toString()));
                } catch (NullPointerException exception) {
                    uktSksAsuransi.setTarif(tarif);
                }
                uktSksAsuransi.setJmlSKS(jumlahSKS);
                uktSksAsuransi.setProsentase(Float.valueOf(doubleboxProsentaseX.getValue().toString()));
                Boolean cekData = rapuusadao.cekData(uktSksAsuransi.getIdPosPendapatanUnit(), uktSksAsuransi.getPeriode(), uktSksAsuransi.getTahunAngkatan());
                if (!cekData) {
                    rapuusadao.insert(uktSksAsuransi);
                } else {
                    rapuusadao.update(uktSksAsuransi);
                }
                for (Map m : rapuusadao.getDetailProdiUKTAsuransi(txtboxKodeProdi.getValue(), tahun.toString())) {
                    String tahunAngkatan = m.get("tahunAngkatan").toString();
                    doubleboxProsentaseX = (Doublebox) getFellow("txtboxProsentaseY" + i);
                    uktSksAsuransi = new RencanaAnggaranPendapatanUangUktSksAsuransi();
                    uktSksAsuransi.setIdPosPendapatanUnit(Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue()));
                    uktSksAsuransi.setPeriode(lblUKTAsuransiDetailY.getValue());
                    uktSksAsuransi.setTahunAngkatan(tahunAngkatan);
                    uktSksAsuransi.setIsAngkatanBaru("N");
                    Label lblJmlMhs = (Label) getFellow("txtboxJmlMhs" + i);
                    Doublebox txtboxTarif = (Doublebox) getFellow("txtboxTarif" + i);
                    try {
                        uktSksAsuransi.setJmlMhsDU(Integer.valueOf(lblJmlMhs.getValue()));
                    } catch (NullPointerException exception) {
                        uktSksAsuransi.setJmlMhsDU(0);
                    }
                    try {
                        uktSksAsuransi.setTarif(Float.valueOf(txtboxTarif.getValue().toString()));
                    } catch (NullPointerException exception) {
                        uktSksAsuransi.setTarif(0);
                    }
                    jumlahSKS = rapuusadao.getJumlahSKS(String.valueOf(tahun), tahunAngkatan.substring(2, 4), txtboxKodeProdi.getValue());
                    uktSksAsuransi.setJmlSKS(jumlahSKS);
                    uktSksAsuransi.setProsentase(Float.valueOf(doubleboxProsentaseX.getValue().toString()));
                    cekData = rapuusadao.cekData(uktSksAsuransi.getIdPosPendapatanUnit(), uktSksAsuransi.getPeriode(), uktSksAsuransi.getTahunAngkatan());
                    if (!cekData) {
                        rapuusadao.insert(uktSksAsuransi);
                    } else {
                        rapuusadao.update(uktSksAsuransi);
                    }
                    i++;
                }
                clearData();
                Messagebox.show("Data sudah disimpan", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
                loadPosAnggaranPendapatanUnitUKTAsuransi();
            } catch (Exception ex) {
                Logger.getLogger(RencanaAnggaranPendapatanUangUktSksAsuransiWnd.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            boolean isPernahDisimpan = false;
            try {
                int i = 0;
                for (Map m : rapuusadao.getDetailProdiSKS(txtboxKodeProdi.getValue(), tahun.toString())) {
                    String tahunAngkatan = m.get("tahunAngkatan").toString();
                    Doublebox doubleboxProsentaseX = (Doublebox) getFellow("txtboxProsentaseXSKS" + i);
                    RencanaAnggaranPendapatanUangUktSksAsuransi uktSksAsuransi = new RencanaAnggaranPendapatanUangUktSksAsuransi();
                    uktSksAsuransi.setIdPosPendapatanUnit(Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue()));
                    uktSksAsuransi.setPeriode(lblUKTAsuransiDetailX.getValue());
                    uktSksAsuransi.setTahunAngkatan(tahunAngkatan);
                    uktSksAsuransi.setIsAngkatanBaru("N");
                    int jmlMhsDU = rapuusadao.getJumlahMahasiswa(txtboxKodeProdi.getValue(), tahun.toString(), tahunAngkatan.substring(2, 4));
                    uktSksAsuransi.setJmlMhsDU(jmlMhsDU);
                    float tarif = 0;
                    try {
                        tarif = rapuusadao.getTarif(Integer.valueOf(txtboxIdPosPendapatan.getValue()), Integer.toString(tahun), tahunAngkatan, txtboxKodeProdi.getValue().toString());
                    } catch (NullPointerException e) {
                    }
                    uktSksAsuransi.setTarif(tarif);
                    int jumlahSKS = rapuusadao.getJumlahSKS(String.valueOf(tahun), tahunAngkatan.substring(2, 4), txtboxKodeProdi.getValue());
                    uktSksAsuransi.setJmlSKS(jumlahSKS);
                    uktSksAsuransi.setProsentase(Float.valueOf(doubleboxProsentaseX.getValue().toString()));
                    Boolean cekData = rapuusadao.cekData(uktSksAsuransi.getIdPosPendapatanUnit(), uktSksAsuransi.getPeriode(), uktSksAsuransi.getTahunAngkatan());
                    if (!cekData) {
                        rapuusadao.insert(uktSksAsuransi);
                    } else {
                        rapuusadao.update(uktSksAsuransi);
                    }
                    i++;
                }
            } catch (Exception ex) {
                Logger.getLogger(RencanaAnggaranPendapatanUangUktSksAsuransiWnd.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                int i = 1;
                Doublebox doubleboxProsentaseXAtas = (Doublebox) getFellow("txtboxProsentaseSKSY");
                RencanaAnggaranPendapatanUangUktSksAsuransi uktSksAsuransi = new RencanaAnggaranPendapatanUangUktSksAsuransi();
                uktSksAsuransi.setIdPosPendapatanUnit(Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue()));
                uktSksAsuransi.setPeriode(lblUKTAsuransiDetailY.getValue());
                uktSksAsuransi.setTahunAngkatan(session.getThnAnggaran());
                Intbox intboxJmlSKS = (Intbox) getFellow("txtboxJmlSKS");
                uktSksAsuransi.setIsAngkatanBaru("Y");
                Intbox intboxJmlMhsAtas = (Intbox) getFellow("txtboxJmlMhsSKS");
                Doublebox dblboxTarifAtas = (Doublebox) getFellow("txtboxTarifSKS");
                float tarifAtas = 0;
                try {
                    uktSksAsuransi.setJmlMhsDU(intboxJmlMhsAtas.getValue());
                } catch (NullPointerException exception) {
                    uktSksAsuransi.setJmlMhsDU(0);
                }
                try {
                    uktSksAsuransi.setTarif(Float.valueOf(dblboxTarifAtas.getValue().toString()));
                } catch (NullPointerException exception) {
                    uktSksAsuransi.setTarif(tarifAtas);
                }
                uktSksAsuransi.setJmlSKS(intboxJmlSKS.getValue());
                uktSksAsuransi.setProsentase(Float.valueOf(doubleboxProsentaseXAtas.getValue().toString()));
                Boolean cekData = rapuusadao.cekData(uktSksAsuransi.getIdPosPendapatanUnit(), uktSksAsuransi.getPeriode(), uktSksAsuransi.getTahunAngkatan());
                if (!cekData) {
                    rapuusadao.insert(uktSksAsuransi);
                } else {
                    rapuusadao.update(uktSksAsuransi);
                }
                for (Map m : rapuusadao.getDetailProdiUKTAsuransi(txtboxKodeProdi.getValue(), tahun.toString())) {
                    String tahunAngkatan = m.get("tahunAngkatan").toString();
                    Doublebox doubleboxProsentaseX = (Doublebox) getFellow("txtboxProsentaseSKSY" + i);
                    uktSksAsuransi = new RencanaAnggaranPendapatanUangUktSksAsuransi();
                    uktSksAsuransi.setIdPosPendapatanUnit(Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue()));
                    uktSksAsuransi.setPeriode(lblUKTAsuransiDetailY.getValue());
                    uktSksAsuransi.setTahunAngkatan(tahunAngkatan);
                    uktSksAsuransi.setIsAngkatanBaru("N");
                    Doublebox dblboxTarif = (Doublebox) getFellow("txtboxTarifSKS" + i);
                    float tarif = 0;
                    int jumlahMahasiswa = rapuusadao.getJumlahMahasiswa(txtboxKodeProdi.getValue(), tahun.toString(), tahunAngkatan.substring(2, 4));
                    try {
                        uktSksAsuransi.setJmlMhsDU(jumlahMahasiswa);
                    } catch (NullPointerException exception) {
                        uktSksAsuransi.setJmlMhsDU(0);
                    }
                    try {
                        uktSksAsuransi.setTarif(Float.valueOf(dblboxTarif.getValue().toString()));
                    } catch (NullPointerException exception) {
                        uktSksAsuransi.setTarif(tarif);
                    }
                    int jumlahSKS = rapuusadao.getJumlahSKS(String.valueOf(tahun), tahunAngkatan.substring(2, 4), txtboxKodeProdi.getValue());
                    uktSksAsuransi.setJmlSKS(jumlahSKS);
                    uktSksAsuransi.setProsentase(Float.valueOf(doubleboxProsentaseX.getValue().toString()));
                    cekData = rapuusadao.cekData(uktSksAsuransi.getIdPosPendapatanUnit(), uktSksAsuransi.getPeriode(), uktSksAsuransi.getTahunAngkatan());
                    if (!cekData) {
                        rapuusadao.insert(uktSksAsuransi);
                    } else {
                        rapuusadao.update(uktSksAsuransi);
                    }
                    i++;
                }
                Messagebox.show("Data sudah disimpan", "Informasi", Messagebox.OK, Messagebox.INFORMATION);
                clearData();
                loadPosAnggaranPendapatanUnitUKTAsuransi();
            } catch (Exception ex) {
                Logger.getLogger(RencanaAnggaranPendapatanUangUktSksAsuransiWnd.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void btnBatalOnClick() {
        clearData();
    }

    public void clearData() {
        lstboxDetailUKTAsuransiX.getItems().clear();
        lstboxDetailUKTAsuransiY.getItems().clear();
        lstboxSKSDetailX.getItems().clear();
        lstboxSKSDetailY.getItems().clear();
        lstboxSKS.getItems().clear();
        lstboxUKTAsuransi.getItems().clear();
        lblJmlAnggaranSKSX.setValue("");
        lblJmlAnggaranSKSY.setValue("");
        lblJmlAnggaranUKTAsuransiX.setValue("");
        lblJmlAnggaranUKTAsuransiY.setValue("");
        lstboxUnitKerja.setSelectedIndex(-1);
    }

    public void loadPosAnggaranPendapatanUnitUKTAsuransi() throws Exception {
        lstboxUKTAsuransi.getItems().clear();
        lstboxSKS.getItems().clear();
        if (Integer.valueOf(txtboxIdPosPendapatan.getValue().toString()) == 1 || Integer.valueOf(txtboxIdPosPendapatan.getValue().toString()) == 4) {
            Listhead listhead = lstboxUKTAsuransi.getListhead();
            listhead.getChildren().clear();
            lstboxUKTAsuransi.getItems().clear();
            lstboxUKTAsuransi = listhead.getListbox();
            Listheader headerProdi = new Listheader("Prodi", "", "100px");
            Listheader headerPeriode = new Listheader("Periode", "", "100px");
            Listheader headerTahunAngkatan = new Listheader("Tahun Angkatan", "", "80px");
            Listheader headerMahasiswaBaru = new Listheader("Mahasiswa Baru", "", "50px");
            Listheader headerJmlMhsDU = new Listheader("Jml Mahasiswa DU", "", "80px");
            Listheader headerTarif = new Listheader("Tarif", "", "100px");
            Listheader headerProsentase = new Listheader("Prosentase", "", "80px");
            Listheader headerJumlahAnggaran = new Listheader("Jumlah Anggaran", "", "100px");
            Listheader headerAction = new Listheader("", "", "100px");
            listhead.appendChild(headerProdi);
            listhead.appendChild(headerPeriode);
            listhead.appendChild(headerTahunAngkatan);
            listhead.appendChild(headerMahasiswaBaru);
            listhead.appendChild(headerJmlMhsDU);
            headerJmlMhsDU.setAlign("right");
            listhead.appendChild(headerTarif);
            headerTarif.setAlign("right");
            listhead.appendChild(headerProsentase);
            headerProsentase.setAlign("right");
            listhead.appendChild(headerJumlahAnggaran);
            headerJumlahAnggaran.setAlign("right");
            float totalAnggaran = 0;
            for (final Map pendapatanUjianAkhir : rapuusadao.getAnggaranPendapatanUnitUKTAsuransi(session.getThnAnggaran(), Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue().toString()))) {
                Listitem item = new Listitem();
                item.setValue(pendapatanUjianAkhir);
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("Nama_unit_kerja").toString()));
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("periode").toString()));
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("thnAngkatan").toString()));
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("isAngkatanBaru").toString()));
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("jmlMhsDU").toString()));
                item.appendChild(new Listcell(ClassUtility.formatMoney(Float.valueOf(pendapatanUjianAkhir.get("tarif").toString()))));
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("prosentase").toString()));
                float jmlAnggaran = Integer.valueOf(pendapatanUjianAkhir.get("jmlMhsDU").toString()) * Float.valueOf(pendapatanUjianAkhir.get("tarif").toString()) * (Float.valueOf(pendapatanUjianAkhir.get("prosentase").toString()) / 100);
                item.appendChild(new Listcell(ClassUtility.formatMoney(jmlAnggaran)));
                lstboxUKTAsuransi.appendChild(item);
                totalAnggaran += jmlAnggaran;
            }
            lblTotalAnggaranUKTAsuransi.setValue(ClassUtility.formatMoney(totalAnggaran));
        } else {
            Listhead listhead = lstboxUKTAsuransi.getListhead();
            listhead.getChildren().clear();
            lstboxUKTAsuransi.getItems().clear();
            lstboxUKTAsuransi = listhead.getListbox();
            Listheader headerProdi = new Listheader("Prodi", "", "100px");
            Listheader headerPeriode = new Listheader("Periode", "", "100px");
            Listheader headerTahunAngkatan = new Listheader("Tahun Angkatan", "", "80px");
            Listheader headerMahasiswaBaru = new Listheader("Mahasiswa Baru", "", "50px");
            Listheader headerJmlMhsDU = new Listheader("Jml Mahasiswa DU", "", "80px");
            Listheader headerTarif = new Listheader("Tarif", "", "100px");
            Listheader headerProsentase = new Listheader("Prosentase", "", "80px");
            Listheader headerJumlahAnggaran = new Listheader("Jumlah Anggaran", "", "100px");
            Listheader headerAction = new Listheader("", "", "100px");
            listhead.appendChild(headerProdi);
            listhead.appendChild(headerPeriode);
            listhead.appendChild(headerTahunAngkatan);
            listhead.appendChild(headerMahasiswaBaru);
            listhead.appendChild(headerJmlMhsDU);
            listhead.appendChild(headerTarif);
            listhead.appendChild(headerProsentase);
            listhead.appendChild(headerJumlahAnggaran);
            float totalAnggaran = 0;
            for (final Map pendapatanUjianAkhir : rapuusadao.getAnggaranPendapatanUnitUKTAsuransi(session.getThnAnggaran(), Integer.valueOf(txtboxIdPosAnggaranPendapatan.getValue().toString()))) {
                Listitem item = new Listitem();
                item.setValue(pendapatanUjianAkhir);
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("Nama_unit_kerja").toString()));
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("periode").toString()));
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("thnAngkatan").toString()));
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("isAngkatanBaru").toString()));
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("jmlMhsDU").toString()));
                item.appendChild(new Listcell(ClassUtility.formatMoney(Float.valueOf(pendapatanUjianAkhir.get("tarif").toString()))));
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("jmlSKS").toString()));
                item.appendChild(new Listcell(pendapatanUjianAkhir.get("prosentase").toString()));
                float jmlAnggaran = Integer.valueOf(pendapatanUjianAkhir.get("jmlMhsDU").toString()) * Float.valueOf(pendapatanUjianAkhir.get("tarif").toString()) * Integer.valueOf(pendapatanUjianAkhir.get("jmlSKS").toString()) * (Float.valueOf(pendapatanUjianAkhir.get("prosentase").toString()) / 100);
                item.appendChild(new Listcell(ClassUtility.formatMoney(jmlAnggaran)));
                lstboxSKS.appendChild(item);
                totalAnggaran += jmlAnggaran;
            }
            lblTotalAnggaranSKS.setValue(ClassUtility.formatMoney(totalAnggaran));
        }
    }
}
