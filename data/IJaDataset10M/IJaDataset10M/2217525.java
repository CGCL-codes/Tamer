package ch.ethz.inf.vs.wot.autowot.ui.resourcedisplay;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import ch.ethz.inf.vs.wot.autowot.commons.Constants;
import ch.ethz.inf.vs.wot.autowot.core.AutoWoT;
import ch.ethz.inf.vs.wot.autowot.project.resources.AbstractResourceItem;
import ch.ethz.inf.vs.wot.autowot.project.resources.ResourceItem;
import ch.ethz.inf.vs.wot.autowot.ui.ViewStyle;
import ch.ethz.inf.vs.wot.autowot.ui.views.UserInterface;

/**
 * Listener reacting to clicks and selections in the Pretty and
 * XML mode
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */
public class ResourceDisplaySelectionListener implements SelectionListener {

    protected AutoWoT application = null;

    protected UserInterface mainUI = null;

    public ResourceDisplaySelectionListener(AutoWoT application, UserInterface mainUI) {
        this.application = application;
        this.mainUI = mainUI;
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
    }

    @Override
    public void widgetSelected(SelectionEvent arg0) {
        if (arg0.item == null) {
            return;
        }
        Integer selectionIndex = mainUI.resourceDisplay.getSelectionIndex();
        if (selectionIndex != 0) {
            if (mainUI.resourceDisplay.getViewStyle() == ViewStyle.XML) {
                String selectedCategory = mainUI.resourceDisplay.getItem(selectionIndex - 1).getText().trim();
                AbstractResourceItem selection = null;
                if (selectedCategory.startsWith(Constants.startOfXMLElement(Constants.RESOURCE_CHILD_OPEN))) {
                    ResourceItem currentRes = application.getCurrentResource().asResourceItem();
                    selection = currentRes.getChild(mainUI.resourceDisplay.getItem(mainUI.resourceDisplay.getSelectionIndex()).getText().trim());
                } else if (selectedCategory.equalsIgnoreCase(Constants.RESOURCE_GETTER_OPEN)) {
                    ResourceItem currentRes = application.getCurrentResource().asResourceItem();
                    selection = currentRes.getGetter();
                } else if (selectedCategory.equalsIgnoreCase(Constants.RESOURCE_POSTER_OPEN)) {
                    ResourceItem currentRes = application.getCurrentResource().asResourceItem();
                    selection = currentRes.getPoster();
                } else if (selectedCategory.equalsIgnoreCase(Constants.RESOURCE_PUTTER_OPEN)) {
                    ResourceItem currentRes = application.getCurrentResource().asResourceItem();
                    selection = currentRes.getPutter();
                } else if (selectedCategory.equalsIgnoreCase(Constants.RESOURCE_DELETER_OPEN)) {
                    ResourceItem currentRes = application.getCurrentResource().asResourceItem();
                    selection = currentRes.getDeleter();
                }
                if (selection != null) {
                    application.setCurrentResource(selection);
                    mainUI.resourceDisplay.printCurrentResourceInfo();
                }
            } else if (mainUI.resourceDisplay.getViewStyle() == ViewStyle.PRETTY) {
                String selectedCategory = mainUI.resourceDisplay.getItem(selectionIndex).getText().trim();
                AbstractResourceItem selection = null;
                if (selectedCategory.startsWith("Getter")) {
                    ResourceItem currentRes = application.getCurrentResource().asResourceItem();
                    selection = currentRes.getGetter();
                } else if (selectedCategory.startsWith("Poster")) {
                    ResourceItem currentRes = application.getCurrentResource().asResourceItem();
                    selection = currentRes.getPoster();
                } else if (selectedCategory.startsWith("Putter")) {
                    ResourceItem currentRes = application.getCurrentResource().asResourceItem();
                    selection = currentRes.getPutter();
                } else if (selectedCategory.startsWith("Deleter")) {
                    ResourceItem currentRes = application.getCurrentResource().asResourceItem();
                    selection = currentRes.getDeleter();
                } else if (application.getCurrentResource().getIsResource()) {
                    String resourceName = null;
                    if (selectedCategory.contains(":")) resourceName = selectedCategory.substring(0, selectedCategory.indexOf(":")); else resourceName = selectedCategory;
                    if (application.getCurrentResource().asResourceItem().getChildNames().contains(resourceName)) {
                        ResourceItem currentRes = application.getCurrentResource().asResourceItem();
                        selection = currentRes.getChild(resourceName);
                    }
                }
                if (selection != null) {
                    application.setCurrentResource(selection);
                    mainUI.refresh();
                }
            }
        } else {
            application.ascend();
        }
    }
}
