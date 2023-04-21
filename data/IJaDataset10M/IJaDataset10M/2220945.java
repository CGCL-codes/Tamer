package net.mogray.infinitypfm.core.data;

import java.sql.SQLException;
import net.mogray.infinitypfm.core.conf.MM;

public class InfinityUpdates {

    public InfinityUpdates() {
        super();
    }

    public void ProcessUpdates() throws SQLException {
        String sVersion = null;
        try {
            sVersion = (String) MM.sqlMap.queryForObject("getAppVersion", null);
        } catch (SQLException se) {
            int code = se.getErrorCode();
            if (code == -22) {
                sVersion = "0.0.1";
            } else {
                return;
            }
        }
        if (sVersion.equals("0.0.1")) {
            ApplyVersion002();
            ApplyVersion010();
            ApplyVersion030();
        } else if (sVersion.equals("0.0.2")) {
            ApplyVersion010();
            ApplyVersion030();
        } else if (sVersion.equals("0.1.0")) {
            ApplyVersion030();
        } else if (sVersion.equals("0.2.0")) {
            ApplyVersion030();
        } else if (sVersion.equals("0.2.1")) {
            ApplyVersion030();
        } else if (sVersion.equals("0.3.0")) {
        }
        DataHandler handler = new DataHandler();
        handler.ProcessRecurringTransaction();
    }

    private void ApplyVersion002() throws SQLException {
        MM.sqlMap.insert("v0_0_2_a", null);
        MM.sqlMap.insert("v0_0_2_b", null);
        MM.sqlMap.insert("v0_0_2_c", null);
    }

    private void ApplyVersion010() throws SQLException {
        MM.sqlMap.update("v0_1_0_a", null);
    }

    private void ApplyVersion030() throws SQLException {
        MM.sqlMap.update("v0_3_0_a", null);
        MM.sqlMap.update("v0_3_0_b", null);
        MM.sqlMap.update("v0_3_0_c", null);
        MM.sqlMap.update("v0_3_0_d", null);
        MM.sqlMap.update("v0_3_0_e", null);
        MM.sqlMap.update("v0_3_0_f", null);
    }

    private void ApplyVersion035() throws SQLException {
        MM.sqlMap.update("v0_3_5_a", null);
        MM.sqlMap.update("v0_3_5_b", null);
        MM.sqlMap.update("v0_3_5_c", null);
    }
}
