package jasci.ui.event;

import jasci.ui.text.Document;
import jasci.ui.text.Element;

public interface DocumentEvent {

    public static final int INSERT_STRING = 0;

    public static final int REMOVE_STRING = 1;

    public int getType();

    public int getStartOffset();

    public int getEndOffset();

    public Document getDocument();

    public interface ElementChange {

        public int getIndex();

        public Element[] getAddedChildren();

        public Element[] getRemovedChildren();

        public Element getElement();
    }

    public ElementChange getChange(Element e);
}
