package org.processmining.analysis.redesign.ui.petri;

import java.awt.Color;
import org.deckfour.gantzgraf.model.GGNode;
import org.deckfour.gantzgraf.painter.GGNodeShapePainter;
import org.processmining.framework.models.petrinet.Place;

/**
 * @author Christian W. Guenther (christian@deckfour.org)
 * 
 */
public class PetriNetPlace extends GGNode {

    /**
	 * Drawing the place
	 */
    private static Color background = new Color(250, 250, 250);

    private static Color border = new Color(20, 20, 20);

    private static Color text = new Color(10, 10, 10);

    private static GGNodeShapePainter painter = new PetriNetPlacePainter(background, border, text);

    private Place original;

    public PetriNetPlace(Place original) {
        super(new String[] { "" }, painter);
        painter.setBorderWidth(2f);
        this.original = original;
    }

    public Place original() {
        return original;
    }

    public Object clone() {
        PetriNetPlace clone = (PetriNetPlace) super.clone();
        clone.original = original;
        return clone;
    }
}
