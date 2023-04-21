package br.com.nix.gui.views.propertiesview;

import java.beans.PropertyChangeEvent;
import br.com.nix.events.ModelChangedEvent;
import br.com.nix.events.ModelSelectionEvent;
import br.com.nix.model.Model;
import com.adamtaft.eb.EventBusService;
import com.adamtaft.eb.EventHandler;
import com.l2fprod.common.propertysheet.PropertySheet;

public class MapPropertiesView extends PropertiesView<Model> {

    private static final long serialVersionUID = -4055633929338658855L;

    public MapPropertiesView() {
        super("Prop. Exibição");
        propPanel.setDescriptionVisible(false);
        propPanel.setMode(PropertySheet.VIEW_AS_FLAT_LIST);
    }

    @EventHandler
    public synchronized void onModelSelection(final ModelSelectionEvent event) {
        setSelection(event.getSelection());
    }

    @EventHandler
    public void onModelChanged(ModelChangedEvent event) {
        loadSelection();
    }

    @Override
    protected void onPropertyChange(Object selection, PropertyChangeEvent event) {
        EventBusService.publish(new ModelChangedEvent());
    }
}
