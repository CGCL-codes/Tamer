package nickyb.fqb;

import nickyb.fqb.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import javax.swing.JLabel;

public class DialogCount extends ConfirmDialog {

    JLabel lbl;

    DesktopEntity item;

    public DialogCount(String title, DesktopEntity item) {
        super(item.getQueryBuilder(), title, 320, 120);
        this.item = item;
        this.getContentPane().add(lbl = new JLabel("wait..."));
    }

    protected void onRunning() {
        try {
            Statement stmt = item.getQueryBuilder().getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + item.querytoken.toString());
            if (rs.next()) {
                int records = rs.getInt(1);
                lbl.setText("table contains " + NumberFormat.getInstance().format(records) + " records, view content?");
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            lbl.setText(sqle.getMessage());
        }
    }

    protected boolean onConfirm() {
        return item.continueWithPreview = true;
    }
}
