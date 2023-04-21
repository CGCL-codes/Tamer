package edu.nps.moves.kmleditor.palette.items;

import edu.nps.moves.kmleditor.KmlDataObject;
import static edu.nps.moves.kmleditor.KmlConstants.*;
import edu.nps.moves.kmleditor.palette.BaseCustomizer;
import edu.nps.moves.kmleditor.types.panels.AliasTypePanel;
import org.jdom.Element;

/**
 * Alias.java
 * Created on Mar 26, 2010
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey / jmbailey@nps.edu
 * @version $Id$
 */
public class Alias extends AbstractObject {

    public String sourceHref;

    public String targetHref;

    public String sourceHrefDefault = "";

    public String targetHrefDefault = "";

    public Alias() {
        super();
    }

    public Alias(KmlDataObject kobj) {
        super(kobj);
        initializeEmpty();
    }

    @Override
    public void initializeEmpty() {
        super.initializeEmpty();
        sourceHref = sourceHrefDefault;
        targetHref = targetHrefDefault;
    }

    @Override
    public void initializeFromJdom(Element mom) {
        initializeEmpty();
        super.initializeFromJdom(mom);
        Element e = null;
        e = mom.getChild("sourceHref", KML_NSO);
        if (e != null) sourceHref = e.getText();
        e = mom.getChild("targetHref", KML_NSO);
        if (e != null) targetHref = e.getText();
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
    }

    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return AliasTypePanel.class;
    }

    @Override
    public void updateContent(ContentWriter cw) {
        if (cw == null) cw = new ContentWriter(getJdomElement());
        super.updateContent(cw);
        cw.handleSimpleElement("sourceHref", (!sourceHref.equals(sourceHrefDefault)), sourceHref);
        cw.handleSimpleElement("targetHref", (!targetHref.equals(targetHrefDefault)), targetHref);
        cw.finalizeContent();
    }
}
