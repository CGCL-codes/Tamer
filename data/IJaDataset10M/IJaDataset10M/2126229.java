package org.sadhar.sipp.perekamananggaranbiayagaji;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.java.dev.eval.Expression;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassSession;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sipp.perekamankomponenanggaranbiayagaji.PerekamanKomponenAnggaranBiayaGaji;
import org.sadhar.sipp.perekamankomponenanggaranbiayagaji.PerekamanKomponenAnggaranBiayaGajiDAO;
import org.sadhar.sipp.perekamankomponenanggaranbiayagaji.PerekamanKomponenAnggaranBiayaGajiDAOImpl;
import org.sadhar.sipp.unkerja.UnKerjaDAO;
import org.sadhar.sipp.unkerja.UnKerjaDAOImpl;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author hendro
 */
public class PerekamanAnggaranGajiWnd extends ClassApplicationModule {

    private Label lblTahunAnggaran;

    private Combobox cmbGajiPegawai;

    private Textbox txtboxUnitKerja;

    private Listbox lstboxHasilRumus;

    private Label lblTotalAnggaranKomponen;

    private Listbox lstboxAnggaranBiayaGaji;

    private Label lblTotalAnggaranBiayaGaji;

    private Textbox txtboxKodeUnitKerja;

    private Listbox lstboxTanpaRumus;

    private Button btnSimpan;

    private Button btnHitung;

    private PerekamanAnggaranGajiDAO dao;

    private PerekamanKomponenAnggaranBiayaGajiDAO daoKomponenGaji;

    private ClassSession sesi = new ClassSession("2009", "01002000");

    private Map<String, BigDecimal> variabel;

    private List<PerekamanAnggaranGaji> dataToSave;

    public PerekamanAnggaranGajiWnd() {
        dao = new PerekamanAnggaranGajiDAOImpl();
        daoKomponenGaji = new PerekamanKomponenAnggaranBiayaGajiDAOImpl();
        variabel = new HashMap<String, BigDecimal>();
        dataToSave = new ArrayList<PerekamanAnggaranGaji>();
    }

    public void onCreate() throws Exception {
        lblTahunAnggaran = (Label) getFellow("lblTahunAnggaran");
        cmbGajiPegawai = (Combobox) getFellow("cmbGajiPegawai");
        txtboxUnitKerja = (Textbox) getFellow("txtboxUnitKerja");
        lstboxHasilRumus = (Listbox) getFellow("lstboxHasilRumus");
        lstboxTanpaRumus = (Listbox) getFellow("lstboxTanpaRumus");
        lblTotalAnggaranKomponen = (Label) getFellow("lblTotalAnggaranKomponen");
        lstboxAnggaranBiayaGaji = (Listbox) getFellow("lstboxAnggaranBiayaGaji");
        txtboxKodeUnitKerja = (Textbox) getFellow("txtboxKodeUnitKerja");
        lblTotalAnggaranBiayaGaji = (Label) getFellow("lblTotalAnggaranBiayaGaji");
        btnSimpan = (Button) getFellow("btnSimpan");
        btnHitung = (Button) getFellow("btnHitungKomponen");
        lblTahunAnggaran.setValue(sesi.getThnAnggaran());
        btnHitung.setDisabled(true);
        btnSimpan.setDisabled(true);
        loadKomponenGaji();
        loadAnggaranBiayaGaji();
    }

    public void btnSimpanOnClick() throws Exception {
        if (txtboxKodeUnitKerja.getValue().isEmpty()) {
            Messagebox.show("Silahkan Pilih Unit Kerja", "Pesan Informasi", Messagebox.OK, Messagebox.INFORMATION);
            return;
        }
        for (PerekamanAnggaranGaji pag : dataToSave) {
            if (dao.select(pag.getTahun(), pag.getKodeUnit(), pag.getIdKomponen()) == null) {
                dao.insert(pag);
            } else {
                dao.update(pag);
            }
        }
        clear();
        loadAnggaranBiayaGaji();
    }

    private UnKerjaDAO unDAO = new UnKerjaDAOImpl();

    private void loadAnggaranBiayaGaji() throws Exception {
        lstboxAnggaranBiayaGaji.getItems().clear();
        double totalAnggaran = 0;
        int id = 0;
        for (final PerekamanAnggaranGaji pag : dao.list(sesi.getThnAnggaran())) {
            Listitem item = new Listitem();
            item.setValue(pag);
            item.appendChild(new Listcell(unDAO.select(pag.getKodeUnit()).getNamaUnitKerja()));
            PerekamanKomponenAnggaranBiayaGaji obj = daoKomponenGaji.select(pag.getIdKomponen());
            if (obj.getIsKomponenGaji().equalsIgnoreCase("Y")) {
                item.appendChild(new Listcell(daoKomponenGaji.selectKomponeGaji(obj.getKodeKomponenGaji()).getNmKomponen()));
            } else {
                item.appendChild(new Listcell(obj.getKomponenDiluarGaji()));
            }
            item.appendChild(new Listcell(String.valueOf(pag.getNominalPerBulan())));
            totalAnggaran += Double.valueOf(pag.getNominalPerBulan());
            if (daoKomponenGaji.select(pag.getIdKomponen()).getRumus().length() <= 0) {
                Listcell cellAksi = new Listcell();
                Toolbarbutton btnUbah = new Toolbarbutton();
                btnUbah.setLabel("[ubah]");
                btnUbah.setId("btnUbah" + String.valueOf(id++));
                btnUbah.addEventListener("onClick", new EventListener() {

                    public void onEvent(Event arg0) throws Exception {
                        txtboxKodeUnitKerja.setText(pag.getKodeUnit());
                        loadKomponenTanpaRumus(pag.getKd_stat_edu());
                        hitungKomponenOnClick(pag.getKd_stat_edu());
                        kd_stat_edu = pag.getKd_stat_edu();
                        btnSimpan.setDisabled(true);
                    }
                });
                cellAksi.appendChild(btnUbah);
                item.appendChild(cellAksi);
            } else {
                item.appendChild(new Listcell(""));
            }
            lstboxAnggaranBiayaGaji.appendChild(item);
        }
        lblTotalAnggaranBiayaGaji.setValue(ClassAntiNull.AntiNullDoubleCurrency(totalAnggaran));
    }

    private void clear() throws Exception {
        btnHitung.setDisabled(true);
        btnSimpan.setDisabled(true);
        loadKomponenGaji();
        txtboxUnitKerja.setValue("");
        txtboxKodeUnitKerja.setValue("");
        lstboxTanpaRumus.getItems().clear();
        lstboxHasilRumus.getItems().clear();
        lstboxAnggaranBiayaGaji.getItems().clear();
        lblTotalAnggaranKomponen.setValue("...................");
        lblTotalAnggaranBiayaGaji.setValue("...................");
    }

    public void btnBatalOnClick() throws Exception {
        clear();
        loadAnggaranBiayaGaji();
    }

    public void btnCariUnitKerjaOnClick() throws Exception {
        if (!cmbGajiPegawai.getValue().isEmpty()) {
            final Window win = (Window) Executions.createComponents("/sippzul/perekamananggaranbiayagaji/UnitKerjaPopup.zul", this, null);
            Textbox txtboxKeySearch = (Textbox) win.getFellow("txtboxKeySearch");
            txtboxKeySearch.setValue(txtboxUnitKerja.getValue());
            win.doModal();
            if (!txtboxKodeUnitKerja.getValue().isEmpty()) {
                GajiPegawai gp = (GajiPegawai) cmbGajiPegawai.getSelectedItem().getValue();
                loadKomponenTanpaRumus(gp.getKd_stat_edu());
                hitungKomponenOnClick(gp.getKd_stat_edu());
            }
            btnSimpan.setDisabled(true);
        } else {
            Messagebox.show("Silahakan Pilih Gaji Pegawai", "PesanInformasi", Messagebox.OK, Messagebox.INFORMATION);
            txtboxUnitKerja.setValue("");
            txtboxKodeUnitKerja.setValue("");
            return;
        }
    }

    private void loadKomponenGaji() throws Exception {
        cmbGajiPegawai.getItems().clear();
        for (GajiPegawai gp : dao.listGajiPegawai()) {
            Comboitem item = new Comboitem();
            item.setValue(gp);
            item.setLabel(gp.getNm_stat_edu());
            cmbGajiPegawai.appendChild(item);
        }
    }

    public void cmbGajiPegawaiOnSelect() throws Exception {
        GajiPegawai gp = (GajiPegawai) cmbGajiPegawai.getSelectedItem().getValue();
        kd_stat_edu = gp.getKd_stat_edu();
    }

    private List<PerekamanKomponenAnggaranBiayaGaji> listData = new ArrayList<PerekamanKomponenAnggaranBiayaGaji>();

    private void loadKomponenTanpaRumus(String kodeAdm) throws Exception {
        lstboxTanpaRumus.getItems().clear();
        listData.clear();
        int no = 1;
        int id = 1;
        for (PerekamanKomponenAnggaranBiayaGaji data : daoKomponenGaji.list(sesi.getThnAnggaran(), kodeAdm)) {
            if (data.getRumus().equalsIgnoreCase("")) {
                Listitem item = new Listitem();
                item.setValue(data);
                item.appendChild(new Listcell(String.valueOf(no++)));
                if (data.getIsKomponenGaji().equalsIgnoreCase("Y")) {
                    item.appendChild(new Listcell(daoKomponenGaji.selectKomponeGaji(data.getKodeKomponenGaji()).getNmKomponen()));
                } else {
                    item.appendChild(new Listcell(data.getKomponenDiluarGaji()));
                }
                Listcell cellAnggaran = new Listcell();
                Doublebox txtboxCellAnggaran = new Doublebox();
                txtboxCellAnggaran.setId("txtbox" + String.valueOf(id));
                listData.add(data);
                id++;
                txtboxCellAnggaran.setWidth("130px");
                PerekamanAnggaranGaji pag = dao.select(sesi.getThnAnggaran(), txtboxKodeUnitKerja.getValue(), data.getIdKomponen());
                if (pag != null && pag.getNominalPerBulan() > 0) {
                    txtboxCellAnggaran.setValue(pag.getNominalPerBulan());
                } else {
                    txtboxCellAnggaran.setValue(0);
                }
                cellAnggaran.appendChild(txtboxCellAnggaran);
                item.appendChild(cellAnggaran);
                lstboxTanpaRumus.appendChild(item);
            }
        }
        if (lstboxTanpaRumus.getItems().size() > 0) {
            btnHitung.setDisabled(false);
        }
    }

    private String kd_stat_edu;

    public void btnHitungOnClick() throws Exception {
        hitungKomponenOnClick(kd_stat_edu);
    }

    public void hitungKomponenOnClick(String kd_stat_edu) throws Exception {
        if (lstboxTanpaRumus.getItems().size() > 0) {
            btnSimpan.setDisabled(false);
        }
        int id = 1;
        for (PerekamanKomponenAnggaranBiayaGaji data : listData) {
            Doublebox nilai = (Doublebox) this.getFellow("txtbox" + id);
            variabel.put(data.getAlias(), new BigDecimal(nilai.getValue()));
            id++;
        }
        dataToSave.clear();
        int no = 1;
        lstboxHasilRumus.getItems().clear();
        double totalAnggaran = 0;
        for (PerekamanKomponenAnggaranBiayaGaji data : daoKomponenGaji.list(sesi.getThnAnggaran(), kd_stat_edu)) {
            if (data.getRumus().equalsIgnoreCase("")) {
                Listitem item = new Listitem();
                item.setValue(data);
                item.appendChild(new Listcell(String.valueOf(no++)));
                if (data.getIsKomponenGaji().equalsIgnoreCase("Y")) {
                    item.appendChild(new Listcell(daoKomponenGaji.selectKomponeGaji(data.getKodeKomponenGaji()).getNmKomponen()));
                } else {
                    item.appendChild(new Listcell(data.getKomponenDiluarGaji()));
                }
                item.appendChild(new Listcell(variabel.get(data.getAlias()) + ""));
                PerekamanAnggaranGaji pag = new PerekamanAnggaranGaji();
                pag.setTahun(sesi.getThnAnggaran());
                pag.setKodeUnit(txtboxKodeUnitKerja.getValue());
                pag.setIdKomponen(data.getIdKomponen());
                pag.setNominalPerBulan(Float.valueOf(variabel.get(data.getAlias()).toPlainString()));
                dataToSave.add(pag);
                totalAnggaran += Double.valueOf(variabel.get(data.getAlias()).toPlainString());
                lstboxHasilRumus.appendChild(item);
            } else {
                Listitem item = new Listitem();
                item.setValue(data);
                item.appendChild(new Listcell(String.valueOf(no++)));
                if (data.getIsKomponenGaji().equalsIgnoreCase("Y")) {
                    item.appendChild(new Listcell(daoKomponenGaji.selectKomponeGaji(data.getKodeKomponenGaji()).getNmKomponen()));
                } else {
                    item.appendChild(new Listcell(data.getKomponenDiluarGaji()));
                }
                Expression exp = new Expression(data.getRumus());
                BigDecimal hasil = new BigDecimal(0);
                try {
                    hasil = exp.eval(variabel);
                } catch (Exception ex) {
                    Messagebox.show("Variabel Tidak ditemukan: " + ex.getMessage(), "Pesan Kesalahan", Messagebox.OK, Messagebox.ERROR);
                    lstboxHasilRumus.getItems().clear();
                    return;
                }
                item.appendChild(new Listcell(String.valueOf(hasil)));
                totalAnggaran += Double.valueOf(hasil.toPlainString());
                PerekamanAnggaranGaji pag = new PerekamanAnggaranGaji();
                pag.setTahun(sesi.getThnAnggaran());
                pag.setKodeUnit(txtboxKodeUnitKerja.getValue());
                pag.setIdKomponen(data.getIdKomponen());
                pag.setNominalPerBulan(Float.valueOf(hasil.toPlainString()));
                dataToSave.add(pag);
                lstboxHasilRumus.appendChild(item);
            }
        }
        lblTotalAnggaranKomponen.setValue(ClassAntiNull.AntiNullDoubleCurrency(totalAnggaran));
    }
}
