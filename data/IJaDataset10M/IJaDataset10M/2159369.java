package org.pojosoft.lms.web.gwt.client.menu;

import org.pojosoft.ria.gwt.client.ui.OutlineContainer;
import org.pojosoft.ria.gwt.client.ui.wizard.WizardComposite;

/**
 * Launches the Learning Assignment wizard when the Learning Assignment menu item is clicked.
 *
 * @author POJO Software
 */
public class LearningAssignmentMenuCommand extends AbstractMenuCommand {

    public void execute() {
        super.execute();
        WizardComposite composite = new WizardComposite("LearningAssignment");
        OutlineContainer outlineContainer = new OutlineContainer();
        outlineContainer.setContent(composite);
        moduleContainer.setContent(outlineContainer);
    }
}
