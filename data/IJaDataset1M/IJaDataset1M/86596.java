package com.qasystems.qstudio.java.integration.eclipse.actions;

import com.qasystems.international.MessageResource;
import com.qasystems.qstudio.java.browser.EditionBrowser;
import com.qasystems.qstudio.java.gui.RuleIdentifier;
import com.qasystems.qstudio.java.gui.observation.ObservationPaneController;
import com.qasystems.qstudio.java.io.Observation;
import org.eclipse.jface.action.Action;

/**
 * This class implement the RulesGuideAction necessary for
 * the right menu click on the observation table.
 */
public class RulesGuideAction extends Action {

    private ObservationPaneController controller;

    /**
   * Creates a new RulesGuideAction object.
   *
   * @param controller DOCUMENT ME!
   */
    public RulesGuideAction(ObservationPaneController controller) {
        this.controller = controller;
        setText(MessageResource.getClientInstance().getString("MENU_022"));
    }

    /**
   * DOCUMENT ME!
   */
    public void run() {
        final Observation observation = controller.getSelectedObservation();
        if (observation != null) {
            final RuleIdentifier ruleId = observation.getIdentifier();
            new EditionBrowser().openRuleGuide(ruleId);
        } else {
            new EditionBrowser().openRuleGuide();
        }
    }
}
