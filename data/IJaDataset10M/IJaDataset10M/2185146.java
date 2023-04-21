package org.opu.db_vdumper.actions.db;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.SQLWarning;
import javax.swing.JOptionPane;
import org.opu.db_vdumper.actions.AbstractAction;
import org.opu.db_vdumper.ui.TextResource;
import org.opu.db_vdumper.util.DbManager;
import org.opu.db_vdumper.util.Logger;
import org.opu.db_vdumper.util.QueryHandler;

/**
 *
 * @author yura
 */
public class ExecuteQueryAction extends DbAction {

    public static String MNAME = "ExecuteQuery";

    private TextResource textResource;

    public ExecuteQueryAction(DbManager dbManager, TextResource textResource) {
        super(dbManager);
        this.textResource = textResource;
    }

    public ExecuteQueryAction(DbManager dbManager) {
        this(dbManager, null);
    }

    public ExecuteQueryAction() {
        this(null);
    }

    @Override
    public String getName() {
        return MNAME;
    }

    @Override
    public void action(ActionEvent e) {
        if (dbManager != null && dbManager.isConnected()) {
            QueryHandler queryHandler = dbManager.getQueryHandler();
            if (queryHandler == null) {
                errorNoConnect();
                return;
            }
            String query = textResource.getText();
            boolean sendQuery = queryHandler.sendQuery(query);
            String[] headers = null;
            Object data;
            SQLWarning warning;
            if (sendQuery) {
                headers = dbManager.headers();
                int r = dbManager.getRows();
                int c = dbManager.getColumns();
                if (r <= 0 || c <= 0) {
                    data = new String[1][headers.length];
                } else {
                    data = new String[r][c];
                    String arr[][] = (String[][]) data;
                    for (int i = 0; i < r; i++) {
                        for (int j = 0; j < c; j++) {
                            arr[i][j] = dbManager.getString(i + 1, j + 1);
                        }
                    }
                }
                warning = dbManager.getWarning();
            } else {
                SQLException ex = queryHandler.getException();
                data = boundle.getString(MNAME + ".Title.NoRes");
                warning = new SQLWarning((String) data, ex);
                if (ex != null) {
                    while (ex != null) {
                        warning.setNextWarning(new SQLWarning(ex.getLocalizedMessage(), ex));
                        ex = ex.getNextException();
                    }
                }
                warning.setNextWarning(dbManager.getWarning());
            }
            textResource.setResult(query, headers, data, warning);
        } else {
            errorNoConnect();
        }
    }

    public TextResource getTextResource() {
        return textResource;
    }

    public void setTextResource(TextResource textResource) {
        this.textResource = textResource;
    }

    protected void errorSendQuery(String str) {
        Logger.getInstance().info(this, "Can't send query");
        if (str == null) {
            str = "";
        } else {
            str = ":\n" + str;
        }
        try {
            JOptionPane.showMessageDialog(null, boundle.getString(MNAME + ".Text.SendQuery") + str, boundle.getString(MNAME + ".Title.SendQuery"), JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException ex) {
            Logger.getInstance().error(this, ex);
        }
    }
}
