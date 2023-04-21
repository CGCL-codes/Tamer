package org.paquitosoft.namtia.view.actions;

import java.lang.Thread.State;
import org.paquitosoft.namtia.view.ViewController;
import org.paquitosoft.namtia.view.facade.ViewFacade;
import org.paquitosoft.namtia.view.forms.CoverSelectorWindow;

/**
 *
 * @author paquitosoft
 */
public class FindCoversViewAction extends Thread {

    private static FindCoversViewAction instance;

    private String searchString;

    /** Creates a new instance of FindCoversViewAction */
    private FindCoversViewAction(String searchString) {
        this.searchString = searchString;
    }

    public static FindCoversViewAction getInstance(String searchString) {
        if (instance != null && instance.isAlive()) {
            instance = null;
        } else if (instance == null || instance.getState().equals(State.TERMINATED)) {
            instance = new FindCoversViewAction(searchString);
        }
        return instance;
    }

    public void run() {
        CoverSelectorWindow window = ViewController.getInstance().getCoverSelectorWindow();
        window.changeMainPanelBorderTitle("(searching covers...)");
        window.getAcceptButton().setEnabled(false);
        window.getPreviousButton().setEnabled(false);
        window.getNextButton().setEnabled(false);
        window.getCovers().addAll(new ViewFacade().findCovers(this.searchString));
        window.getAcceptButton().setEnabled(true);
        window.getPreviousButton().setEnabled(true);
        window.getNextButton().setEnabled(true);
        window.changeMainPanelBorderTitle("Cover");
    }
}
