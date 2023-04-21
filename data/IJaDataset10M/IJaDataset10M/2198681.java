package com.x3.monitoring.admin;

import com.x3.monitoring.ApplicationContext;
import com.x3.monitoring.dao.PotensiInvestasiDAO;
import com.x3.monitoring.dao.mysql.PotensiInvestasiDAOImpl;
import com.x3.monitoring.entity.PotensiInvestasi;
import java.sql.Connection;
import org.zkforge.fckez.FCKeditor;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author Hendro Steven
 */
public class InputPotensiInvestasiWnd extends ApplicationContext {

    FCKeditor fckPotensi;

    public InputPotensiInvestasiWnd() {
    }

    public void onCreate() throws Exception {
        fckPotensi = (FCKeditor) getFellow("fckPotensi");
        load();
    }

    private void load() throws Exception {
        Connection conn = getConn();
        try {
            PotensiInvestasiDAO dao = new PotensiInvestasiDAOImpl(conn);
            PotensiInvestasi pi = dao.get();
            if (pi != null) {
                fckPotensi.setValue(pi.getText());
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        } finally {
            conn.close();
        }
    }

    public void simpan() throws Exception {
        Connection conn = getConn();
        try {
            if (!fckPotensi.getValue().isEmpty()) {
                PotensiInvestasiDAO dao = new PotensiInvestasiDAOImpl(conn);
                PotensiInvestasi pi = dao.get();
                if (pi != null) {
                    pi.setText(fckPotensi.getValue());
                    dao.update(pi);
                } else {
                    pi = new PotensiInvestasi();
                    pi.setText(fckPotensi.getValue());
                    dao.insert(pi);
                }
                Messagebox.show("Data tersimpan");
                load();
            } else {
                Messagebox.show("Silahkan Input Data");
            }
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        } finally {
            conn.close();
        }
    }

    public void lihat() throws Exception {
        try {
            Window win = (Window) Executions.createComponents("/zul/operator/potensi_investasi.zul", null, null);
            win.doModal();
        } catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
}
