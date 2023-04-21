package jqueryuitest;

import org.zkoss.jquery4j.jqueryui.progressbar.Progressbar;
import org.zkoss.jquery4j.jqueryui.progressbar.events.ChangedEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class ProgressbarController extends GenericForwardComposer {

    Progressbar comp1;

    public void onProgressbarChange$comp1(ChangedEvent e) {
        System.out.println("progressbar value changed : " + e.getValue());
    }
}
