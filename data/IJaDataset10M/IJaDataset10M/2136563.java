package net.nan21.lib.dc;

import javax.servlet.http.HttpServletResponse;
import net.nan21.lib.HttpRequest;
import net.nan21.lib.HttpSession;
import net.nan21.lib.DbManager;

public interface IDataControl {

    public void init(HttpRequest request, HttpServletResponse response, HttpSession session, DbManager dbm) throws Exception;

    public void doQuery() throws Exception;

    public void doInsert() throws Exception;

    public void doUpdate() throws Exception;

    public void doDelete() throws Exception;

    public void fetchRecord() throws Exception;

    public void doExport() throws Exception;

    public void initNewRecord() throws Exception;

    public void doCustomAction(String action) throws Exception;
}
