package uk.co.ordnancesurvey.rabbitgui.parserproblem.list.impl;

import uk.ac.leeds.comp.ui.factory.UIControllerFactory;
import uk.ac.leeds.comp.ui.itemlist.SwingItemListControllerImpl;
import uk.ac.leeds.comp.ui.itemlist.SwingItemListView;
import uk.co.ordnancesurvey.rabbitgui.parserproblem.item.RbtParserProblemItemModel;
import uk.co.ordnancesurvey.rabbitgui.parserproblem.item.SwingRbtParserProblemItemController;
import uk.co.ordnancesurvey.rabbitgui.parserproblem.list.RbtParserProblemListModel;
import com.google.inject.Inject;

/**
 * Handles the relation between a list of rabbit parser problems and its view.
 * 
 * @author rdenaux
 * 
 */
public class SwingRbtParserProblemListControllerImpl extends SwingItemListControllerImpl<RbtParserProblemListModel, SwingItemListView, SwingRbtParserProblemItemController, RbtParserProblemItemModel> implements SwingRbtParserProblemListController {

    private static final long serialVersionUID = 3499265877405464081L;

    private final UIControllerFactory controllerFactory;

    @Inject
    public SwingRbtParserProblemListControllerImpl(UIControllerFactory aControllerFactory) {
        controllerFactory = aControllerFactory;
    }

    @Override
    protected SwingRbtParserProblemItemController createItemControllerBasedOnItemModel(RbtParserProblemItemModel aItemModel) {
        return controllerFactory.createController(SwingRbtParserProblemItemController.class);
    }

    @Override
    protected SwingItemListView createView() {
        return controllerFactory.createNamedUIComponent(SwingItemListView.class, "WithScrollPane");
    }
}
