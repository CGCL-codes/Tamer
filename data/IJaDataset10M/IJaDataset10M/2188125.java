package org.jtools.util.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface DOMWriter {

    public static class TextElement implements DOMWriter {

        private String value = null;

        public TextElement() {
        }

        public TextElement(String value) {
            this.value = value;
        }

        @Override
        public void write(Document document, Element element) {
            XMLUtils.writeText(document, element, value);
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    void write(Document document, Element element);
}
