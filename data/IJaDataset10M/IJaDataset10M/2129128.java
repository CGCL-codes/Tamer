package wh2fo.elements.content;

import wh2fo.elements.Element;

public class PreProcessor extends Element {

    public PreProcessor(String params) {
        super("<?xml ", params + "?>", "", "", "");
    }
}
