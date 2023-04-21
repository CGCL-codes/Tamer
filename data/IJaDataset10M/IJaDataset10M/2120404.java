package uk.ac.cam.caret.minibix.qtibank.impl.vcrud.item;

import java.io.IOException;
import java.io.InputStream;
import uk.ac.cam.caret.minibix.archive.api.Item;
import uk.ac.cam.caret.minibix.archive.api.ItemFactory;

public class SlurpedByteArrayItemFactory implements ItemFactory {

    private static final String NAME = "uk.ac.cam.caret.minibix.qtibank.impl.SlurpedByteArrayItem";

    public Item create(InputStream is) throws IOException {
        return new SlurpedByteArrayItem(is);
    }

    public String getName() {
        return NAME;
    }
}
