package se.kth.cid.conzilla.edit;

import se.kth.cid.util.*;
import se.kth.cid.component.*;
import se.kth.cid.identity.*;
import se.kth.cid.conceptmap.*;
import se.kth.cid.conzilla.util.*;
import se.kth.cid.conzilla.component.*;
import se.kth.cid.conzilla.map.*;
import se.kth.cid.conzilla.metadata.*;
import se.kth.cid.conzilla.controller.*;
import se.kth.cid.library.*;
import se.kth.cid.conzilla.tool.*;
import se.kth.cid.conzilla.history.*;
import se.kth.cid.neuron.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/** 
 *  @author Matthias Palm�r
 *  @version $Revision: 350 $
 */
public class InsertClipboardContentMapTool extends MapTool {

    public InsertClipboardContentMapTool(String name, MapController cont) {
        super(name, Tool.ACTION, cont);
    }

    protected boolean updateImpl() {
        ClipboardLibrary cl = controller.getConzillaKit().getConzillaEnvironment().getRootLibrary().getClipboardLibrary();
        if (mapEvent.hitType != mapEvent.HIT_NONE && mapObject.getNeuron() != null && cl.getComponent() != null && mapObject.getNeuron().isEditable()) return true;
        return false;
    }

    public void activateImpl() {
        ClipboardLibrary cl = controller.getConzillaKit().getConzillaEnvironment().getRootLibrary().getClipboardLibrary();
        if (cl.getComponent() == null) return;
        URI base = URIClassifier.parseValidURI(mapObject.getNeuron().getURI());
        URI absoluteURI = URIClassifier.parseValidURI(cl.getComponent().getURI());
        String relativeURI;
        try {
            relativeURI = base.makeRelative(absoluteURI, false);
        } catch (MalformedURIException me) {
            relativeURI = absoluteURI.toString();
        }
        if (mapObject.getNeuron().isEditable()) {
            MetaData.Relation relation = new MetaData.Relation(new MetaData.LangString(null, "content"), null, relativeURI);
            MetaDataUtils.addObject(mapObject.getNeuron().getMetaData(), "relation", relation);
        }
        ComponentEdit cEditor = controller.getConzillaKit().getComponentEdit();
        cEditor.editComponent(mapObject.getNeuron(), false);
    }
}
