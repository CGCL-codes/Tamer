package eu.pisolutions.ocelot.document.tree;

import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentReadingContextHelper;
import eu.pisolutions.ocelot.document.value.StringNode;
import eu.pisolutions.ocelot.document.value.StringNode.Factory;
import eu.pisolutions.ocelot.object.ContainerPdfObjectElement;
import eu.pisolutions.ocelot.object.PdfNameObject;

public final class TestNodeTree extends DocumentNodeTree<String, StringNode> {

    public TestNodeTree() {
        super();
    }

    @Override
    protected PdfNameObject getEntriesKey() {
        return new PdfNameObject("entries");
    }

    @Override
    protected DocumentNodeFactory<StringNode> getValueFactory() {
        return Factory.INSTANCE;
    }

    @Override
    protected String readKey(ContainerPdfObjectElement element) {
        return DocumentReadingContextHelper.asElementOfType(PdfNameObject.class, element).getString();
    }

    @Override
    protected ContainerPdfObjectElement createElement(String key) {
        return new PdfNameObject(key);
    }
}
