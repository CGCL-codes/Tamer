package jset.project.safeguards;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jset.model.gui.GUI;
import jset.model.gui.GUIElement;
import jset.project.Project;

public class GUIStructureIDSafeguard implements ISafeguard {

    private static GUIStructureIDSafeguard instance;

    private String message;

    private GUIStructureIDSafeguard() {
    }

    public static synchronized GUIStructureIDSafeguard getInstance() {
        if (instance == null) {
            instance = new GUIStructureIDSafeguard();
        }
        return instance;
    }

    @Override
    public int run(Object[] parameters) {
        if (parameters == null || parameters.length != 1 || parameters[0] instanceof Project == false) {
            throw new IllegalArgumentException("Illegal arguments for safeguard");
        }
        return runIDSafeguard(((Project) parameters[0]).getGui());
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int runIDSafeguard(GUI guiStructure) {
        List<GUIElement> flatGUI = new ArrayList<GUIElement>(guiStructure.flatMap().values());
        Set<String> duplicateIDs = new HashSet<String>();
        Set<String> idSet = new HashSet<String>();
        for (GUIElement ct : flatGUI) {
            if (idSet.contains(ct.id)) {
                duplicateIDs.add(ct.id);
            } else {
                idSet.add(ct.id);
            }
        }
        StringBuilder messageBuilder = new StringBuilder();
        for (String id : duplicateIDs) {
            messageBuilder.append("Duplicate widget ID: " + id + "\n");
        }
        if (duplicateIDs.size() == 0) {
            message = "No duplicate widget IDs.";
            return ISafeguard.SUCCESS;
        }
        message = messageBuilder.toString();
        return ISafeguard.FATAL_ERROR;
    }
}
