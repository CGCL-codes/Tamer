package org.sadhar.sipp.laporananggaranpendapatanuangformulirmahasiswabaru;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sipp.anggaranpendapatanformulirpmb.AnggaranPendapatanFormulirPMBDAO;
import org.sadhar.sipp.anggaranpendapatanformulirpmb.AnggaranPendapatanFormulirPMBDAOImpl;
import org.sadhar.sipp.gelombang.Gelombang;
import org.sadhar.sipp.gelombang.GelombangDAO;
import org.sadhar.sipp.gelombang.GelombangDAOImpl;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Radio;

/**
 * 
 * @author Yohan Hardinugroho
 */
public class LaporanAnggaranPendapatanUangFormulirMahasiswaBaruWND extends ClassApplicationModule {

    private Button buttonHideExport;

    private Radio rdSemua;

    private Combobox cmbboxTertentu;

    private Combobox cmbboxTahunMulai;

    private Combobox cmbboxTahunSampai;

    private Radio rdTahun;

    private Listbox lstboxAnggaranPendapatanUnit;

    private Label lblTotalAnggaran;

    private GelombangDAO gelombangDAO;

    private AnggaranPendapatanFormulirPMBDAO anggaranPendapatanFormulirDAO;

    private List<Gelombang> cmbboxGelombangData;

    private List<Map> data;

    private Jasperreport report;

    private Listbox lstboxformat;

    private Panel panelReport;

    public LaporanAnggaranPendapatanUangFormulirMahasiswaBaruWND() {
        data = new ArrayList<Map>();
        gelombangDAO = new GelombangDAOImpl();
        anggaranPendapatanFormulirDAO = new AnggaranPendapatanFormulirPMBDAOImpl();
    }

    public void load() {
        loadCmbboxTertentu();
        loadLstboxAnggaranPendapatanUnitByTahun();
        loadcmbboxTahun();
    }

    public void loadcmbboxTahun() {
        try {
            cmbboxTahunMulai.getItems().clear();
            cmbboxTahunSampai.getItems().clear();
            List<String> dataCmbbox = anggaranPendapatanFormulirDAO.listTahun();
            for (String s : dataCmbbox) {
                cmbboxTahunMulai.appendChild(new Comboitem(s));
                cmbboxTahunSampai.appendChild(new Comboitem(s));
            }
            cmbboxTahunMulai.setValue(dataCmbbox.get(0));
            cmbboxTahunSampai.setValue(dataCmbbox.get(dataCmbbox.size() - 1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadCmbboxTertentu() {
        try {
            cmbboxGelombangData = gelombangDAO.list();
            cmbboxTertentu.getItems().clear();
            for (Gelombang gb : cmbboxGelombangData) {
                Comboitem item = new Comboitem(gb.getNm_gelombang());
                cmbboxTertentu.appendChild(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rdSemuaOnCheck() throws Exception {
        cmbboxTertentu.setDisabled(true);
        if (rdTahun.isChecked()) {
            data = anggaranPendapatanFormulirDAO.listDetails("", cmbboxTahunMulai.getValue(), cmbboxTahunSampai.getValue(), true);
            layouting(data, true);
        } else {
            data = anggaranPendapatanFormulirDAO.listDetails("", cmbboxTahunMulai.getValue(), cmbboxTahunSampai.getValue(), false);
            layouting(data, false);
        }
    }

    public void layouting(List<Map> data, boolean byTahun) {
        if (byTahun) {
            DecimalFormat df = new DecimalFormat("#,##0.0");
            double totals = 0.0;
            Listhead lh = lstboxAnggaranPendapatanUnit.getListhead();
            lh.getChildren().clear();
            Listheader tahunHead = new Listheader("Tahun", "", "50px");
            lh.appendChild(tahunHead);
            Listheader gelHead = new Listheader("Gelombang", "", "100px");
            lh.appendChild(gelHead);
            Listheader jmlHead = new Listheader("Jumlah Formulir yang Terjual", "", "200px");
            jmlHead.setAlign("right");
            lh.appendChild(jmlHead);
            Listheader hargaHead = new Listheader("Harga", "", "120px");
            hargaHead.setAlign("right");
            lh.appendChild(hargaHead);
            Listheader totalHead = new Listheader("Total", "", "120px");
            totalHead.setAlign("right");
            lh.appendChild(totalHead);
            lstboxAnggaranPendapatanUnit.appendChild(lh);
            List<Map> listMap;
            try {
                listMap = data;
                lstboxAnggaranPendapatanUnit.getItems().clear();
                for (Map m : listMap) {
                    Listitem item = new Listitem();
                    item.setValue(m);
                    item.appendChild(new Listcell(ClassAntiNull.AntiNullString(m.get("tahun"))));
                    item.appendChild(new Listcell(ClassAntiNull.AntiNullString(m.get("nm_gelombang"))));
                    item.appendChild(new Listcell(ClassAntiNull.AntiNullIntString(m.get("jumlahFormulirYangTerjual"))));
                    item.appendChild(new Listcell(ClassAntiNull.AntiNullDoubleString(m.get("harga"))));
                    double total = ClassAntiNull.AntiNullInt(m.get("jumlahFormulirYangTerjual")) * ClassAntiNull.AntiNullDouble(m.get("harga"));
                    totals += total;
                    item.appendChild(new Listcell(df.format(total)));
                    lstboxAnggaranPendapatanUnit.appendChild(item);
                }
                lblTotalAnggaran.setValue(df.format(totals));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DecimalFormat df = new DecimalFormat("#,##0.0");
            double totals = 0.0;
            Listhead lh = lstboxAnggaranPendapatanUnit.getListhead();
            lh.getChildren().clear();
            Listheader gelHead = new Listheader("Gelombang", "", "100px");
            lh.appendChild(gelHead);
            Listheader tahunHead = new Listheader("Tahun", "", "50px");
            lh.appendChild(tahunHead);
            Listheader jmlHead = new Listheader("Jumlah Formulir yang Terjual", "", "200px");
            jmlHead.setAlign("right");
            lh.appendChild(jmlHead);
            Listheader hargaHead = new Listheader("Harga", "", "120px");
            hargaHead.setAlign("right");
            lh.appendChild(hargaHead);
            Listheader totalHead = new Listheader("Total", "", "120px");
            totalHead.setAlign("right");
            lh.appendChild(totalHead);
            lstboxAnggaranPendapatanUnit.appendChild(lh);
            List<Map> listMap;
            try {
                listMap = data;
                lstboxAnggaranPendapatanUnit.getItems().clear();
                for (Map m : listMap) {
                    Listitem item = new Listitem();
                    item.setValue(m);
                    item.appendChild(new Listcell(ClassAntiNull.AntiNullString(m.get("nm_gelombang"))));
                    item.appendChild(new Listcell(ClassAntiNull.AntiNullString(m.get("tahun"))));
                    item.appendChild(new Listcell(ClassAntiNull.AntiNullIntString(m.get("jumlahFormulirYangTerjual"))));
                    item.appendChild(new Listcell(ClassAntiNull.AntiNullDoubleString(m.get("harga"))));
                    double total = ClassAntiNull.AntiNullInt(m.get("jumlahFormulirYangTerjual")) * ClassAntiNull.AntiNullDouble(m.get("harga"));
                    totals += total;
                    item.appendChild(new Listcell(df.format(total)));
                    lstboxAnggaranPendapatanUnit.appendChild(item);
                }
                lblTotalAnggaran.setValue(df.format(totals));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cmbboxTertentuOnChange() throws Exception {
        int selectedIndex = cmbboxTertentu.getSelectedIndex();
        Gelombang glb = cmbboxGelombangData.get(selectedIndex);
        if (rdTahun.isChecked()) {
            data = anggaranPendapatanFormulirDAO.listDetails(glb.getKd_gelombang(), cmbboxTahunMulai.getValue(), cmbboxTahunSampai.getValue(), true);
            layouting(data, true);
        } else {
            data = anggaranPendapatanFormulirDAO.listDetails(glb.getKd_gelombang(), cmbboxTahunMulai.getValue(), cmbboxTahunSampai.getValue(), false);
            layouting(data, false);
        }
    }

    public void rdTertentuOnCheck() throws Exception {
        cmbboxTertentu.setDisabled(false);
        loadCmbboxTertentu();
    }

    public void onCreate() throws Exception {
        rdSemua = (Radio) getFellow("rdSemua");
        cmbboxTertentu = (Combobox) getFellow("cmbboxTertentu");
        cmbboxTahunMulai = (Combobox) getFellow("cmbboxTahunMulai");
        cmbboxTahunSampai = (Combobox) getFellow("cmbboxTahunSampai");
        rdTahun = (Radio) getFellow("rdTahun");
        lstboxAnggaranPendapatanUnit = (Listbox) getFellow("lstboxAnggaranPendapatanUnit");
        lblTotalAnggaran = (Label) getFellow("lblTotalAnggaran");
        report = (Jasperreport) getFellow("report");
        lstboxformat = (Listbox) getFellow("lstboxformat");
        panelReport = (Panel) getFellow("panelReport");
        buttonHideExport = (Button) getFellow("buttonHideExport");
        load();
    }

    public void loadLstboxAnggaranPendapatanUnitByTahun() {
        try {
            String kodeGel = "";
            if (!rdSemua.isChecked()) {
                int selectedIndex = cmbboxTertentu.getSelectedIndex();
                if (selectedIndex >= 0) {
                    Gelombang glb = cmbboxGelombangData.get(selectedIndex);
                    kodeGel = glb.getKd_gelombang();
                }
            }
            data = anggaranPendapatanFormulirDAO.listDetails(kodeGel, cmbboxTahunMulai.getValue(), cmbboxTahunSampai.getValue(), true);
            layouting(data, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listBoxOnClick() {
    }

    public void export() throws Exception {
        String kodeGel = "";
        if (!rdSemua.isChecked()) {
            int selectedIndex = cmbboxTertentu.getSelectedIndex();
            if (selectedIndex >= 0) {
                Gelombang glb = cmbboxGelombangData.get(selectedIndex);
                kodeGel = glb.getKd_gelombang();
            }
        }
        report.setType(lstboxformat.getSelectedItem().getValue().toString());
        String sql = "";
        if (rdTahun.isChecked()) {
            report.setSrc("reports/laporananggaranpendapatanuangformulirmahasiswabaru/LaporanFormulirByTahun.jasper");
            sql = anggaranPendapatanFormulirDAO.listDetailSql(kodeGel, cmbboxTahunMulai.getValue(), cmbboxTahunSampai.getValue(), true);
        } else {
            report.setSrc("reports/laporananggaranpendapatanuangformulirmahasiswabaru/LaporanFormulirByGelombang.jasper");
            sql = anggaranPendapatanFormulirDAO.listDetailSql(kodeGel, cmbboxTahunMulai.getValue(), cmbboxTahunSampai.getValue(), false);
        }
        Map m = new HashMap();
        m.put("tahunMulai", cmbboxTahunMulai.getValue());
        m.put("tahunSampai", cmbboxTahunSampai.getValue());
        report.setParameters(m);
        ResultSetTableModel rstm = new ResultSetTableModel(ClassConnection.getJdbc().getDataSource().getConnection(), sql);
        report.setDatasource(new JRTableModelDataSource(rstm));
        showExport();
    }

    public void showExport() {
        panelReport.setVisible(true);
        panelReport.setOpen(true);
        buttonHideExport.setVisible(true);
    }

    public void hideExport() {
        panelReport.setVisible(false);
        buttonHideExport.setVisible(false);
    }

    public void cmbboxTahunOnChange() {
        try {
            String kodeGel = "";
            if (!rdSemua.isChecked()) {
                int selectedIndex = cmbboxTertentu.getSelectedIndex();
                if (selectedIndex >= 0) {
                    Gelombang glb = cmbboxGelombangData.get(selectedIndex);
                    kodeGel = glb.getKd_gelombang();
                }
            }
            if (rdTahun.isChecked()) {
                data = anggaranPendapatanFormulirDAO.listDetails(kodeGel, cmbboxTahunMulai.getValue(), cmbboxTahunSampai.getValue(), true);
                layouting(data, true);
            } else {
                data = anggaranPendapatanFormulirDAO.listDetails(kodeGel, cmbboxTahunMulai.getValue(), cmbboxTahunSampai.getValue(), false);
                layouting(data, false);
            }
            if (cmbboxTahunMulai.getValue().compareTo(cmbboxTahunSampai.getValue()) > 0) {
                Messagebox.show("Tahun Sampai Tidak Boleh lebih kecil dari tahun mulai", "Pesan Kesalahan", Messagebox.OK, null);
                cmbboxTahunSampai.setFocus(true);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadLstboxAnggaranPendapatanUnitByGelombang() {
        try {
            String kodeGel = "";
            if (!rdSemua.isChecked()) {
                int selectedIndex = cmbboxTertentu.getSelectedIndex();
                if (selectedIndex >= 0) {
                    Gelombang glb = cmbboxGelombangData.get(selectedIndex);
                    kodeGel = glb.getKd_gelombang();
                }
            }
            data = anggaranPendapatanFormulirDAO.listDetails(kodeGel, cmbboxTahunMulai.getValue(), cmbboxTahunSampai.getValue(), false);
            layouting(data, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
