package uk.co.thirstybear.hectorj;

import java.util.Observable;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class HudsonMonitor extends Observable implements PropertyChangeListener {

    private HudsonScraper hudsonScraper = new HudsonScraper();

    private HudsonState oldState;

    private static Logger logger = Logger.getLogger("uk.co.thirstybear.hectorj.HudsonMonitor");

    private final Pattern findProjectDetail = Pattern.compile("<title>([ a-zA-Z_0-9\\-]*)\\s#(\\d+)\\s*\\((\\w*)\\)</title>");

    public void checkState() {
        logger.log(Level.INFO, "Checking state");
        String rawRssLatestData = hudsonScraper.fetchRssLatestData();
        String rawRssAllData = hudsonScraper.fetchRssAllData();
        HudsonState currentState = parseDataToStateObject(rawRssLatestData, rawRssAllData);
        if (oldState == null || !currentState.equals(oldState) || currentState.hasError()) {
            setChanged();
            oldState = currentState;
        }
        notifyObservers(currentState);
        if (currentState.isBuildOk()) {
            logger.log(Level.INFO, "Build state is OK");
        } else {
            logger.log(Level.INFO, "Build state is FAIL");
        }
    }

    private HudsonState parseDataToStateObject(String rawRssLatestData, String rawRssAllData) {
        HudsonState state = new HudsonState();
        state.setUrl(HectorPreferences.getInstance().getHudsonUrl());
        marshalToProjects(rawRssLatestData, rawRssAllData, state);
        if (rawRssLatestData.equals("")) {
            state.setError(true);
        }
        return state;
    }

    private void marshalToProjects(String rawRssLatestData, String rawRssAllData, HudsonState state) {
        Map<String, Project> projects = new HashMap<String, Project>();
        Matcher m = findProjectDetail.matcher(rawRssLatestData);
        while (m.find()) {
            String projectname = m.group(1);
            int projectVersion = Integer.parseInt(m.group(2));
            ProjectState projectState = ProjectState.getState(m.group(3));
            BuildInfo previousBuildVersion = null;
            if (projectState == ProjectState.ABORTED || projectState == ProjectState.BUILDING) {
                previousBuildVersion = mineHistoryForLastPassFail(projectname, rawRssAllData);
            }
            Project project = projects.get(projectname);
            if (project != null) {
                BuildInfo buildInfo = new BuildInfo(projectname, projectVersion, projectState);
                project.addItemToBuildHistory(buildInfo);
            } else {
                project = new Project(projectname, projectVersion, projectState);
                projects.put(projectname, project);
                state.add(project);
            }
            if (previousBuildVersion != null) {
                project.addItemToBuildHistory(previousBuildVersion);
            }
        }
    }

    private BuildInfo mineHistoryForLastPassFail(String projectname, String rawRssAllData) {
        Pattern p = Pattern.compile("<title>" + projectname + "\\s#(\\d+)\\s*\\((\\w*)\\)</title>");
        Matcher m = p.matcher(rawRssAllData);
        BuildInfo lastPassFail = null;
        while (m.find()) {
            int version = Integer.parseInt(m.group(1));
            ProjectState state = ProjectState.getState(m.group(2));
            if (state == ProjectState.PASS || state == ProjectState.FAIL) {
                lastPassFail = new BuildInfo(projectname, version, state);
                break;
            }
        }
        return lastPassFail;
    }

    public void setHudsonScraper(HudsonScraper newHudsonScraper) {
        hudsonScraper = newHudsonScraper;
    }

    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        checkState();
    }
}
