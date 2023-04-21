package com.intellij.uiDesigner.lw;

import com.intellij.uiDesigner.compiler.UnexpectedFormElementException;
import org.jdom.Element;
import java.awt.*;

/**
 * @author Anton Katilin
 * @author Vladimir Kondratyev
 */
public final class LwSplitPane extends LwContainer {

    public static final String POSITION_LEFT = "left";

    public static final String POSITION_RIGHT = "right";

    public LwSplitPane(String className) {
        super(className);
    }

    protected LayoutManager createInitialLayout() {
        return null;
    }

    public void read(final Element element, final PropertiesProvider provider) throws Exception {
        readNoLayout(element, provider);
    }

    protected void readConstraintsForChild(final Element element, final LwComponent component) {
        final Element constraintsElement = LwXmlReader.getRequiredChild(element, "constraints");
        final Element splitterChild = LwXmlReader.getRequiredChild(constraintsElement, "splitpane");
        final String position = LwXmlReader.getRequiredString(splitterChild, "position");
        if ("left".equals(position)) {
            component.setCustomLayoutConstraints(POSITION_LEFT);
        } else if ("right".equals(position)) {
            component.setCustomLayoutConstraints(POSITION_RIGHT);
        } else {
            throw new UnexpectedFormElementException("unexpected position: " + position);
        }
    }
}
