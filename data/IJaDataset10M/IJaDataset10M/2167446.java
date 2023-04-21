package databaseConnect;

import java.util.HashMap;
import java.util.Vector;
import datatypes.HostInformation;

public class ScenarioWizzardRequests {

    public static int getLastRowIDFromHostDescriptionTable() {
        Vector<Vector<String>> temp = AccessConnect.executeSelect("SELECT MAX(ID)FROM HostDescription");
        try {
            if (temp != null) {
                return Integer.parseInt(temp.firstElement().firstElement());
            }
            return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static int getMaxScenarioID() {
        Vector<Vector<String>> temp = AccessConnect.executeSelect("SELECT MAX(ID)FROM ScenarioDescriptions");
        try {
            if (temp != null) {
                return Integer.parseInt(temp.firstElement().firstElement());
            }
            return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void saveScenario(int Scenario_ID, String scen_Name, String scen_subname, String description, int miniworld_width, int miniworld_height, int range, HashMap<Integer, HostInformation> generatedContent, Vector<Vector<String>> actionExecutionList) {
        try {
            AccessConnect.executeQuery("INSERT INTO ScenarioDescriptions VALUES ('" + Scenario_ID + "', '" + scen_Name + "', '" + scen_subname + "', '" + description + "', '" + miniworld_width + "', '" + miniworld_height + "', '" + range + "')");
            for (HostInformation host : generatedContent.values()) {
                AccessConnect.executeQuery("INSERT INTO HostDescription VALUES ('" + host.getID() + "', '" + host.getXPos() + "', '" + host.getYPos() + "')");
                if (host.hasServices()) {
                    for (String service : host.getServices()) {
                        AccessConnect.executeQuery("INSERT INTO HostServices VALUES ('" + host.getID() + "', '" + service + "')");
                    }
                }
                if (host.isActive()) {
                    AccessConnect.executeQuery("INSERT INTO StartConfigurationNodes VALUES ('" + Scenario_ID + "', '" + host.getID() + "', '" + 1 + "', '" + 1 + "')");
                } else {
                    AccessConnect.executeQuery("INSERT INTO StartConfigurationNodes VALUES ('" + Scenario_ID + "', '" + host.getID() + "', '" + 1 + "', '" + 0 + "')");
                }
            }
            ScenarioConfigurationUpdateOrInsertProcessor.setActions(actionExecutionList, Scenario_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
