package org.fpdev.apps.cart;

import java.io.File;
import org.fpdev.apps.cart.network.CartNetwork;

/**
 *
 * @author demory
 */
public class EditorFactory {

    private Cartographer cart_;

    private int currentID_;

    public EditorFactory(Cartographer cart) {
        cart_ = cart;
        currentID_ = 1;
    }

    public Editor createBlankEditor() {
        return new Editor(currentID_++, new CartDocument(), new CartActionHistory(cart_));
    }

    public Editor createEditorFromFile(File file) {
        CartDocument doc = new CartDocument();
        doc.readXMLFile(file);
        Editor ed = new Editor(currentID_++, doc, new CartActionHistory(cart_));
        return ed;
    }
}
