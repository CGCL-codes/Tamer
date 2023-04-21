package net.sf.vex.layout;

import net.sf.vex.dom.Element;

public interface ElementOrRangeCallback {

    public void onElement(Element child, String displayStyle);

    public void onRange(Element parent, int startOffset, int endOffset);
}
