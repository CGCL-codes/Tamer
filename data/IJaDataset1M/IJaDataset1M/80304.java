package shieh.pnn.wm;

import shieh.pnn.core.*;

public class LayerInput extends Layer {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7337987476253161162L;

    public static final String NEW_ITEM_SIGNAL_ID = "NEW_ITEM";

    public static final String NAME_INPUT_LAYER = "Input";

    private Signal NEW_ITEM = null;

    public LayerInput(Network model, ItemSet itemSet) {
        super(model, NAME_INPUT_LAYER, itemSet);
        ProcessorHost host = ProcessorHost.getInstance();
        NEW_ITEM = host.registerSignal(NEW_ITEM_SIGNAL_ID);
    }

    /**
   * Process an input item to the memory layer
   * @param item The input item
   */
    public void onInputItem(Item item) {
        Unit u = findUnit(item);
        if (u == null) return;
        activateUnit(u);
        NEW_ITEM.fire();
    }
}
