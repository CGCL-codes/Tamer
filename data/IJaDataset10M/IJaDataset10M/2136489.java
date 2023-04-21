package org.openweblearning.owl.client.actions;

import org.openweblearning.owl.client.OWL;
import org.openweblearning.owl.client.message.AddCollectionIn;
import org.openweblearning.owl.client.message.AddCollectionOut;

/**
 * The Class AddCollectionAction.
 */
public class AddCollectionAction extends RetryAction<AddCollectionOut> {

    /** The input. */
    protected AddCollectionIn input;

    /**
	 * Instantiates a new adds the collection action.
	 * 
	 * @param in the in
	 * @param callback the callback
	 */
    public AddCollectionAction(AddCollectionIn in, ActionCallback<AddCollectionOut> callback) {
        input = in;
        actionCallback = callback;
    }

    @Override
    public void attempt() {
        OWL.OWL_SERVICE.addCollection(input, this);
    }
}
