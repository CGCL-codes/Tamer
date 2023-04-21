package edu.nps.moves.kmleditor.palette.items;

import edu.nps.moves.kmleditor.palette.BaseCustomizer;
import edu.nps.moves.kmleditor.types.panels.StyleMapTypePanel;
import java.util.List;
import java.util.Vector;
import org.jdom.Element;

/**
 * AbstractGeometry.java
 * Created on Feb 11, 2010
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey / jmbailey@nps.edu
 * @version $Id$
 */
public class StyleMap extends AbstractStyleSelector {

    public Vector<Pair> pairs;

    public StyleMap() {
        super();
    }

    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return StyleMapTypePanel.class;
    }

    @Override
    public void initializeEmpty() {
        super.initializeEmpty();
        pairs = new Vector<Pair>();
    }

    @Override
    public void initializeFromJdom(Element mom) {
        initializeEmpty();
        super.initializeFromJdom(mom);
        List lis = mom.getChildren();
        for (Object o : lis) {
            Pair p = new Pair(getKmlDataObject());
            p.initializeFromJdom((Element) o);
            pairs.add(p);
        }
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
    }

    @Override
    public void updateContent(ContentWriter cw) {
        Element mom = getJdomElement();
        mom.removeContent();
        if (cw == null) cw = new ContentWriter(mom);
        super.updateContent(cw);
        for (Pair p : pairs) cw.handleComplexElement(p.getElementName(), true, p);
        cw.finalizeContent();
    }

    /**
   * This method is found through reflection and called base class methods are trying to determin whether to
   * include the element in the serialization.  If you wanted to here, you could check for the pairs
   * vector being empty, but I'm going to say "never delete it" because it may contain XML comments.
   * The user can delete manually if desired.
   * @return whether the element is empty and/or should be deleted.
   */
    public boolean isEmpty() {
        return false;
    }
}
