package org.sadhar.sipp.perekamanitembiaya;

import org.sadhar.sia.framework.ClassApplicationModule;
import org.sadhar.sipp.posbiaya.PosBiaya;
import org.sadhar.sipp.posbiaya.PosBiayaDAO;
import org.sadhar.sipp.posbiaya.PosBiayaDAOImpl;
import org.sadhar.sipp.satuan.SatuanDAO;
import org.sadhar.sipp.satuan.SatuanDAOImpl;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Space;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 *
 * @author hendro
 */
public class PosBiayaPopupWnd extends ClassApplicationModule {

    private Textbox txtboxKeySearch;

    private Listbox lstboxPosBiaya;

    private PosBiayaDAO posBiayaDAO;

    private PerekamanItemBiayaDAO itemBiayaDAO;

    private SatuanDAO satuanDAO;

    public PosBiayaPopupWnd() {
        posBiayaDAO = new PosBiayaDAOImpl();
        itemBiayaDAO = new PerekamanItemBiayaDAOImpl();
        satuanDAO = new SatuanDAOImpl();
    }

    public void onCreate() throws Exception {
        lstboxPosBiaya = (Listbox) getFellow("lstboxPosBiaya");
        txtboxKeySearch = (Textbox) getFellow("txtboxKeySearch");
        load();
    }

    public void load() throws Exception {
        lstboxPosBiaya.getItems().clear();
        int no = 1;
        for (PosBiaya pos : posBiayaDAO.list(txtboxKeySearch.getValue().toString())) {
            if (pos.getNamaTableItem().equalsIgnoreCase("itemBiaya")) {
                Listitem item = new Listitem();
                item.setValue(pos);
                item.appendChild(new Listcell("" + no++));
                item.appendChild(new Listcell(pos.getNamaPosBiaya().toString()));
                lstboxPosBiaya.appendChild(item);
            }
        }
    }

    public void lstboxPosBiayaOnSelect() throws Exception {
        Window window = (Window) this.getRoot().getFellow("contentDiv").getFirstChild();
        Label lblPosBiaya = (Label) window.getFellow("lblPosBiaya");
        Textbox txtboxKodePosBiaya = (Textbox) window.getFellow("txtboxKodePosBiaya");
        PosBiaya pos = (PosBiaya) lstboxPosBiaya.getSelectedItem().getValue();
        lblPosBiaya.setValue(pos.getNamaPosBiaya());
        txtboxKodePosBiaya.setValue(pos.getKodePosBiaya() + "");
        loadItemBiayaPerPosBiaya(pos.getKodePosBiaya());
        this.detach();
    }

    public void loadItemBiayaPerPosBiaya(int posBiaya) throws Exception {
        Window window = (Window) this.getRoot().getFellow("contentDiv").getFirstChild();
        Listbox lstboxItemBiaya = (Listbox) window.getFellow("lstboxItemBiaya");
        final Button btnBatal = (Button) window.getFellow("btnBatal");
        final Label lblPosBiaya = (Label) window.getFellow("lblPosBiaya");
        final Textbox txtboxKodePosBiaya = (Textbox) window.getFellow("txtboxKodePosBiaya");
        final Textbox txtboxNamaItemBiaya = (Textbox) window.getFellow("txtboxNamaItemBiaya");
        final Textbox txtboxKodeItemBiaya = (Textbox) window.getFellow("txtboxKodeItemBiaya");
        final Textbox txtboxCariSatuanBarang = (Textbox) window.getFellow("txtboxCariSatuanBarang");
        final Textbox txtboxKodeSatuanBarang = (Textbox) window.getFellow("txtboxKodeSatuanBarang");
        final Textbox txtboxCariPosBiaya = (Textbox) window.getFellow("txtboxCariPosBiaya");
        final Button btnCariPosBiaya = (Button) window.getFellow("btnCariPosBiaya");
        final Button btnCariSatuan = (Button) window.getFellow("btnCariSatuan");
        lstboxItemBiaya.getItems().clear();
        int no = 1;
        for (final PerekamanItemBiaya itemBiaya : itemBiayaDAO.listItemBiayaPerPos(posBiaya)) {
            Listitem item = new Listitem();
            item.setValue(itemBiaya);
            item.appendChild(new Listcell(posBiayaDAO.select(itemBiaya.getKodePosBiaya()).getNamaPosBiaya()));
            item.appendChild(new Listcell(itemBiaya.getNamaItem()));
            Listcell cellAksi = new Listcell();
            Toolbarbutton btnUbah = new Toolbarbutton();
            btnUbah.setLabel("[ubah]");
            btnUbah.setId("btnUbah" + (no++));
            btnUbah.addEventListener("onClick", new EventListener() {

                public void onEvent(Event arg0) throws Exception {
                    txtboxCariPosBiaya.setDisabled(false);
                    txtboxNamaItemBiaya.setDisabled(false);
                    txtboxCariSatuanBarang.setDisabled(false);
                    btnCariPosBiaya.setDisabled(false);
                    btnCariSatuan.setDisabled(false);
                    lblPosBiaya.setValue(posBiayaDAO.select(itemBiaya.getKodePosBiaya()).getNamaPosBiaya());
                    txtboxKodePosBiaya.setValue(itemBiaya.getKodePosBiaya() + "");
                    txtboxNamaItemBiaya.setValue(itemBiaya.getNamaItem());
                    txtboxKodeItemBiaya.setValue(itemBiaya.getKodeItem() + "");
                    txtboxCariSatuanBarang.setValue(satuanDAO.select(itemBiaya.getKodeSatuan()).getSatuan());
                    txtboxKodeSatuanBarang.setValue(itemBiaya.getKodeSatuan() + "");
                }
            });
            final Toolbarbutton btnHapus = new Toolbarbutton();
            btnHapus.setLabel("[hapus]");
            btnHapus.setId("btnHapus" + (no++));
            btnHapus.addEventListener("onClick", new EventListener() {

                public void onEvent(Event arg0) throws Exception {
                    if (Messagebox.show("Apakah data Item Biaya: " + itemBiaya.getNamaItem() + " akan dihapus?", "Konfirmasi", Messagebox.YES | Messagebox.NO, null) == Messagebox.YES) {
                        try {
                            itemBiayaDAO.delete(itemBiaya);
                        } catch (Exception ex) {
                            Messagebox.show("Gagal menghapus data: " + ex.getMessage());
                        }
                        btnHapus.addForward(null, btnBatal, "onClick");
                    }
                }
            });
            cellAksi.appendChild(btnUbah);
            cellAksi.appendChild(new Space());
            cellAksi.appendChild(btnHapus);
            item.appendChild(cellAksi);
            lstboxItemBiaya.appendChild(item);
        }
    }
}
