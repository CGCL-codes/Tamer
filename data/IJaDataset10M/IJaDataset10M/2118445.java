package interfaces.loading;

import interfaces.InterfaceWindow;
import org.fenggui.Display;
import scenario.MatchScenario;

public class LoadingWindow extends InterfaceWindow {

    protected MatchScenario scenario;

    public LoadingWindow(Display display, MatchScenario scenario) {
        super(display, false, "");
        this.scenario = scenario;
    }
}
