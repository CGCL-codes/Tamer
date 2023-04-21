package net.ar.guia.plugins.swing;

import java.awt.*;
import java.util.*;
import java.util.List;
import net.ar.guia.own.interfaces.*;
import net.ar.guia.ui.*;

public class GuiaToSwingRenderingContributionsContainer extends DefaultRenderingContributionContainer {

    public void doContribution(VisualComponent aComponent, Component aSwingComponent) {
        List theProps = new Vector();
        theProps.add(0, new VisitableSwingComponentWrapper(aSwingComponent));
        theComponentProps.put(aComponent, theProps);
    }

    public Component getSwingComponent(VisualComponent aComponent) {
        return ((VisitableSwingComponentWrapper) ((List) theComponentProps.get(aComponent)).get(0)).getSwingComponent();
    }
}
