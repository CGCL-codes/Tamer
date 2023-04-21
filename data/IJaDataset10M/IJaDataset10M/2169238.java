package org.sadhar.sipp.laporananggaranpendapatanuangkemahasiswaan;

import java.util.Map;
import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author kris
 */
public class LaporanAnggaranPendapatanUangKemahasiswaanUnitKerjaPopupWnd extends ClassApplicationModule {

    private LaporanAnggaranPendapatanUangKemahasiswaanDAO pendapatanUangKemahasiswaanDAO;

    private Listbox lstboxUnitKerja;

    private Textbox txtboxUnitKerja;

    private Textbox txtboxIdUnitKerja;

    public LaporanAnggaranPendapatanUangKemahasiswaanUnitKerjaPopupWnd() {
        pendapatanUangKemahasiswaanDAO = new LaporanAnggaranPendapatanUangKemahasiswaanDAOImpl();
    }

    public void onCreate() throws Exception {
        Window window = (Window) this.getRoot().getFellow("contentDiv").getFirstChild();
        lstboxUnitKerja = (Listbox) getFellow("lstboxUnitKerja");
        txtboxIdUnitKerja = (Textbox) window.getFellow("txtboxIdUnitKerja");
        txtboxUnitKerja = (Textbox) window.getFellow("txtboxUnitKerja");
        load();
    }

    public void load() throws Exception {
        lstboxUnitKerja.getItems().clear();
        Integer no = 1;
        for (Map map : pendapatanUangKemahasiswaanDAO.getAllUnitKerja()) {
            Listitem item = new Listitem();
            item.setValue(map);
            item.appendChild(new Listcell(String.valueOf(no++)));
            item.appendChild(new Listcell(map.get("Nama_unit_kerja").toString()));
            lstboxUnitKerja.appendChild(item);
        }
    }

    public void lstboxUnitKerjaOnSelect() {
        Map id = (Map) lstboxUnitKerja.getSelectedItem().getValue();
        txtboxIdUnitKerja.setValue(id.get("Kd_unit_kerja").toString());
        Map nama = (Map) lstboxUnitKerja.getSelectedItem().getValue();
        txtboxUnitKerja.setValue(nama.get("Nama_unit_kerja").toString());
        this.detach();
    }
}
