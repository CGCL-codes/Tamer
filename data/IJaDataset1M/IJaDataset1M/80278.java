package gui;

import files.Scenario;

/**
 * @author kbok
 * Provides a callback for the ScenarioDialog to notify when the used had chosen the
 * Scenario to play.
 */
public interface ScenarioDialogListener {

    public void scenarioChosen(Scenario s);
}
