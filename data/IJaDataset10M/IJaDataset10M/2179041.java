package gov.sns.apps.diagnostics.corede.corede;

import java.util.*;

public interface ControllerCommand {

    public String run(ArrayList<String> args);

    public String getHelp();

    public String getName();
}
