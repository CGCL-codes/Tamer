package com.x3.monitoring.admin;

import com.x3.monitoring.ApplicationContextWindow;
import com.x3.monitoring.dao.BidangUsahaDAO;
import com.x3.monitoring.dao.mysql.BidangUsahaDAOImpl;
import com.x3.monitoring.entity.BidangUsaha;
import java.sql.Connection;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

/**
 *
 * @author Hendro Steven
 */
public class DaftarBidangUsahaPencarianWnd extends ApplicationContextWindow {

    Textbox keyword;

    Listbox lstBidangUsaha;

    public DaftarBidangUsahaPencarianWnd() {
    }

    public void onCreate() throws Exception {
        keyword = (Textbox) getFellow("keyword");
        lstBidangUsaha = (Listbox) getFellow("lstBidangUsaha");
        loadBidangUsaha();
    }

    private void loadBidangUsaha() throws Exception {
        Connection conn = getConn();
        try {
            BidangUsahaDAO dao = new BidangUsahaDAOImpl(conn);
            lstBidangUsaha.getItems().clear();
            int no = 1;
            for (BidangUsaha bu : dao.gets(1)) {
                Listitem item = new Listitem();
                item.setValue(bu);
                item.appendChild(new Listcell(bu.getKbli()));
                item.appendChild(new Listcell(bu.getJenis()));
                lstBidangUsaha.appendChild(item);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        } finally {
            conn.close();
        }
    }

    public void loadBidangUsahaKey() throws Exception {
        Connection conn = getConn();
        try {
            BidangUsahaDAO dao = new BidangUsahaDAOImpl(conn);
            lstBidangUsaha.getItems().clear();
            int no = 1;
            for (BidangUsaha bu : dao.gets(keyword.getValue())) {
                Listitem item = new Listitem();
                item.setValue(bu);
                item.appendChild(new Listcell(bu.getKbli()));
                item.appendChild(new Listcell(bu.getJenis()));
                lstBidangUsaha.appendChild(item);
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        } finally {
            conn.close();
        }
    }

    public void onSelectLstBox() throws Exception {
        try {
            BidangUsaha bu = (BidangUsaha) lstBidangUsaha.getSelectedItem().getValue();
            Div w = (Div) this.getParent();
            Textbox txtBidangUsaha = (Textbox) w.getFellow("txtBidangUsaha");
            Intbox txtIdBidangUsaha = (Intbox) w.getFellow("txtIdBidangUsaha");
            txtIdBidangUsaha.setValue(bu.getId());
            txtBidangUsaha.setValue(bu.getJenis());
            this.detach();
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
