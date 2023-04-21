package uk.ac.leeds.comp.ui.itemlist.item;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import uk.ac.leeds.comp.ui.action.UIAction;
import uk.ac.leeds.comp.ui.factory.UIControllerFactory;
import uk.ac.leeds.comp.ui.itemlist.item.impl.AbstractItemController;

/**
 * Provides a generic controller for showing an {@link ItemModel} using a
 * {@link SwingItemView}. By default the itemview simply uses a label to present
 * the item. Subclasses can extend this controller to use a different view for
 * more specialised item models.
 * 
 * @author rdenaux
 * 
 * @param <ModelType>
 */
public class GenericSwingItemControllerImpl<ModelType extends ItemModel<?>> extends AbstractItemController<ModelType, SwingItemView<?>> implements SwingItemController<ModelType, SwingItemView<?>> {

    private static final long serialVersionUID = 9035060640273657668L;

    protected final UIControllerFactory cFactory;

    public GenericSwingItemControllerImpl(UIControllerFactory aControllerFactory) {
        cFactory = aControllerFactory;
    }

    public void addKeyboardShortcut(KeyStroke aKeyStroke, UIAction aAction) {
        InputMap im = getView().getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = getView().getActionMap();
        am.put(aAction.toString(), (Action) aAction);
        im.put(aKeyStroke, aAction.toString());
    }

    @Override
    protected SwingItemView<?> createView() {
        return cFactory.createUIComponent(SwingLabelItemView.class);
    }
}
