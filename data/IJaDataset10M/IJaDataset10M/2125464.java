package de.d3web.we.ci4ke.action;

import java.io.IOException;
import java.net.URLDecoder;
import de.d3web.we.ci4ke.build.CIBuildPersistenceHandler;
import de.d3web.we.ci4ke.build.CIBuilder;
import de.d3web.we.ci4ke.handling.CIDashboardRenderer;
import de.knowwe.core.action.AbstractAction;
import de.knowwe.core.action.UserActionContext;

public class CIAction extends AbstractAction {

    @Override
    public void execute(UserActionContext context) throws IOException {
        String task = String.valueOf(context.getParameter("task"));
        String dashboardName = String.valueOf(context.getParameter("id"));
        dashboardName = URLDecoder.decode(dashboardName, "UTF-8");
        String topic = context.getTitle();
        if (task.equals("null") || dashboardName.equals("null")) {
            throw new IOException("CIAction.execute(): Required parameters not set!");
        }
        StringBuffer buffy = new StringBuffer("");
        if (task.equals("executeNewBuild")) {
            CIBuilder builder = new CIBuilder(topic, dashboardName);
            builder.executeBuild();
            buffy.append(CIDashboardRenderer.renderDashboardContents(dashboardName, topic));
        } else if (task.equals("getBuildDetails")) {
            int selectedBuildNumber = Integer.parseInt(context.getParameter("nr"));
            buffy.append(CIDashboardRenderer.renderBuildDetails(dashboardName, topic, selectedBuildNumber));
        } else if (task.equals("refreshBuildList")) {
            int indexFromBack = Integer.parseInt(context.getParameter("indexFromBack"));
            int numberOfBuilds = Integer.parseInt(context.getParameter("numberOfBuilds"));
            CIBuildPersistenceHandler handler = CIBuildPersistenceHandler.getHandler(dashboardName, topic);
            buffy.append(handler.renderBuildList(indexFromBack, numberOfBuilds));
        }
        context.setContentType("text/html; charset=UTF-8");
        context.getWriter().write(buffy.toString());
    }
}
