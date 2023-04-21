package org.blueoxygen.cimande.gx.window;

import java.util.ArrayList;
import org.blueoxygen.cimande.gx.entity.GxWindow;

public class ListWindow extends WindowForm {

    public String execute() {
        setWindows((ArrayList<GxWindow>) manager.findAllSorted(GxWindow.class, "description"));
        modelMap.put("windows", getWindows());
        return SUCCESS;
    }
}
