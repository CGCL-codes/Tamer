package org.rapla.gui.toolkit;

import javax.swing.ActionMap;

public interface WizardPanel extends RaplaWidget {

    String NEXT = "next";

    String ABORT = "abort";

    String PREV = "prev";

    String FINISH = "finish";

    public ActionMap getActionMap();

    public String getHelp();

    public String getDefaultAction();
}
