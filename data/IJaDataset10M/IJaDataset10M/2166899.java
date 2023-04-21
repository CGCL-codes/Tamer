package org.iascf.itmm.client.selection.ui.available;

import org.iascf.itmm.client.application.controller.ClientModelController;
import org.iascf.itmm.client.application.controller.PresentationController;
import org.iascf.itmm.client.application.controller.PresentationController.PRESENTATION_TYPE;
import org.iascf.itmm.client.mvc.AppEvent;
import org.iascf.itmm.client.mvc.AppEvent.APP_EVENT;
import org.iascf.itmm.client.selection.ui.ATreeWidget;
import org.iascf.itmm.client.selection.ui.toolbar.ISearchListener;
import org.iascf.itmm.client.selection.ui.tree.ModuleTree;
import org.iascf.itmm.client.selection.ui.tree.SubModuleTree;

/**
 * AvailabeTreeWidget shows a tree of selectable modules and/or submodules, according to 
 * the state of the <code>PresentationController</code>.
 * <p>
 * AvailableTreeWidget might be searched for a certain element, therefore it implements
 * <code>ISearchListener</code>
 * 
 * 
 * @author Lukas Pruschke (lpruschke@iasb.org)
 */
public class AvailableTreeWidget extends ATreeWidget implements ISearchListener {

    private AAvailableTreeBuilder builder;

    public AvailableTreeWidget(String header) {
        super(header);
    }

    private void init() {
        setTree();
        getTree().addTreeListener(ClientModelController.getInstance());
        if (PresentationController.getInstance().getPresentationType() == PRESENTATION_TYPE.MODULE) {
            builder = new AvailableModuleTreeBuilder((ModuleTree) getTree());
        } else {
            builder = new AvailableSubModuleTreeBuilder((SubModuleTree) getTree());
        }
        builder.reRender();
    }

    @Override
    public void handleEvent(AppEvent e) {
        APP_EVENT event = e.getType();
        Object data = e.getData();
        switch(event) {
            case TAXONOMY_CHANGED:
                init();
                break;
            case USER_TYPE_EXPERT:
                builder.reRender();
                break;
            case USER_TYPE_NORMAL:
                break;
            case PRESENTATION_TYPE_MODULES:
            case PRESENTATION_TYPE_SUBMODULES:
                init();
                break;
        }
    }

    public void applySearch(String search) {
        builder.filter(search);
    }

    public void clearSearch() {
        builder.unFilter();
    }
}
