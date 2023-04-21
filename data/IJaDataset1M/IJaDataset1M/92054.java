package eu.pisolutions.ocelot.document.xobject.form;

import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.tree.NameTree;
import eu.pisolutions.ocelot.object.PdfObject;

public class FormXObjectRegistry extends NameTree<FormXObject> {

    public enum Factory implements DocumentNodeFactory<FormXObjectRegistry> {

        INSTANCE;

        public FormXObjectRegistry createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            return new FormXObjectRegistry(indirect);
        }
    }

    public FormXObjectRegistry() {
        super();
    }

    protected FormXObjectRegistry(boolean indirect) {
        super(indirect);
    }

    @Override
    protected final DocumentNodeFactory<FormXObject> getValueFactory() {
        return FormXObject.Factory.INSTANCE;
    }
}
