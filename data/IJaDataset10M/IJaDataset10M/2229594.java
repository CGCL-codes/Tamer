package fr.fg.server.action;

import java.util.Map;
import fr.fg.server.core.ReportTools;
import fr.fg.server.data.DataAccess;
import fr.fg.server.data.IllegalOperationException;
import fr.fg.server.data.Player;
import fr.fg.server.data.Report;
import fr.fg.server.servlet.Action;
import fr.fg.server.servlet.Session;

public class GetReportByHash extends Action {

    @Override
    protected String execute(Player player, Map<String, Object> params, Session session) throws Exception {
        Report report = DataAccess.getReportByHash((String) params.get("hash"));
        if (report == null) throw new IllegalOperationException("Rapport de combat inexistant.");
        return ReportTools.getReport(null, player, report).toString();
    }
}
