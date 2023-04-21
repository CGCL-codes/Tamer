package org.sadhar.sipp.jenisbarang;

import org.sadhar.sia.framework.ClassApplicationModule;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author hendro
 */
public class JenisBarangPopupWnd extends ClassApplicationModule {

    private Textbox txtboxKodeKlasifikasi;

    private Textbox txtboxKeySearch;

    private Listbox lstboxJenisBarang;

    private JenisBarangDAO jenisBarangDAO;

    public JenisBarangPopupWnd() {
        jenisBarangDAO = new JenisBarangDAOImpl();
    }

    public void onCreate() throws Exception {
        txtboxKodeKlasifikasi = (Textbox) getFellow("txtboxKodeKlasifikasi");
        txtboxKeySearch = (Textbox) getFellow("txtboxKeySearch");
        lstboxJenisBarang = (Listbox) getFellow("lstboxJenisBarang");
        loadJenisBarang();
    }

    private void loadJenisBarang() throws Exception {
        lstboxJenisBarang.getItems().clear();
        for (JenisBarang jenis : jenisBarangDAO.list(Integer.valueOf(txtboxKodeKlasifikasi.getValue()), txtboxKeySearch.getValue())) {
            Listitem item = new Listitem();
            item.setValue(jenis);
            item.appendChild(new Listcell(jenis.getJenisBarang()));
            lstboxJenisBarang.appendChild(item);
        }
    }

    public void lstboxJenisBarangOnSelect() throws Exception {
        JenisBarang jenis = (JenisBarang) lstboxJenisBarang.getSelectedItem().getValue();
        Window window = (Window) this.getRoot().getFellow("contentDiv").getFirstChild();
        Textbox txtboxIdJenisBarang = (Textbox) window.getFellow("txtboxIdJenisBarang");
        Textbox txtboxCariJenis = (Textbox) window.getFellow("txtboxCariJenis");
        txtboxIdJenisBarang.setValue(jenis.getIdJenisBarang() + "");
        txtboxCariJenis.setValue(jenis.getJenisBarang());
        this.detach();
    }
}
