package pl.edu.wat.wcy.jit.utils;

import java.util.ArrayList;
import pl.edu.wat.wcy.jit.model.BpmnApplication;
import pl.edu.wat.wcy.jit.model.StartActivity;
import pl.edu.wat.wcy.jit.view.bpmn.Element;

public class DeskitFacade {

    private static final long DEFAULT_SIMULATION_DUARATION = 50000;

    public static void startSimulation(ArrayList<Element> startsList, long simulationDuration) {
        ArrayList<StartActivity> startPoints = ModelConverter.ConvertGuiModelToSimModel(startsList);
        BpmnApplication simulation = new BpmnApplication(startPoints, simulationDuration);
    }

    public static void startSimulation(ArrayList<Element> startsList) {
        ArrayList<StartActivity> startPoints = ModelConverter.ConvertGuiModelToSimModel(startsList);
        BpmnApplication simulation = new BpmnApplication(startPoints, DEFAULT_SIMULATION_DUARATION);
    }
}
