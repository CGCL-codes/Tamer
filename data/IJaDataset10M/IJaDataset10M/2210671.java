package ingenias.editor.events;

import ingenias.editor.cell.*;
import ingenias.editor.entities.*;
import java.awt.*;
import javax.swing.*;
import java.awt.Graphics;
import java.util.Map;
import java.util.Hashtable;
import org.jgraph.graph.*;
import org.jgraph.*;
import org.jgraph.event.*;
import ingenias.editor.ObservableModel;
import java.util.*;

/**
 *
 * Avoids that an entity gets outta of the screen.
 * It does so by setting X to 1 when X<0 and Y to 1 when Y<0
 *
 */
public class KeepLifeLineInsideProtocol implements org.jgraph.event.GraphModelListener {

    public final int COL_SEPARATOR = 5;

    private Object workingObject = null;

    private boolean enabled = true;

    JGraph graph;

    public KeepLifeLineInsideProtocol(JGraph graph) {
        this.graph = graph;
    }

    private LifelineCell locateLifelineCell(Lifeline ll, ingenias.editor.Model model) {
        LifelineCell result = null;
        return (LifelineCell) AUMLDiagramChangesManager.getCellFromUserObject(ll);
    }

    private AUMLPortCell locatePortCell(AUMLPort port, ingenias.editor.Model model) {
        AUMLPortCell result = null;
        return (AUMLPortCell) AUMLDiagramChangesManager.getCellFromUserObject(port);
    }

    private AUMLAlternativeRowCell locateAlternativeRowCell(AUMLAlternativeRow col, ingenias.editor.Model model) {
        AUMLAlternativeRowCell result = null;
        return (AUMLAlternativeRowCell) AUMLDiagramChangesManager.getCellFromUserObject(col);
    }

    private ColumnCell locateColumnCell(Column col, ingenias.editor.Model model) {
        ColumnCell result = null;
        return (ColumnCell) AUMLDiagramChangesManager.getCellFromUserObject(col);
    }

    private ProtocolCell locateProtocolCell(Lifeline ll, ingenias.editor.Model model) {
        ProtocolCell result = null;
        for (int k = 0; k < model.getRootCount() && result == null; k++) {
            if (model.getRootAt(k) instanceof ProtocolCell) {
                Protocol prot = (Protocol) ((ProtocolCell) model.getRootAt(k)).getUserObject();
                Enumeration enumeration = prot.getChildrenElements();
                boolean found = false;
                while (enumeration.hasMoreElements() && !found) {
                    found = enumeration.nextElement().equals(ll);
                }
                if (found) {
                    result = (ProtocolCell) model.getRootAt(k);
                }
            }
        }
        return result;
    }

    private void movedProtocol(ProtocolCell pc, org.jgraph.event.GraphModelEvent gme) {
        Map previousLoc = (Map) gme.getChange().getPreviousAttributes().get(pc);
        Rectangle lastpos = GraphConstants.getBounds(previousLoc).getBounds();
        Rectangle currentpos = GraphConstants.getBounds((Map) gme.getChange().getAttributes().get(pc)).getBounds();
        Protocol prot = (Protocol) pc.getUserObject();
        Enumeration enumeration = prot.getChildrenElements();
        ingenias.editor.Model model = (ingenias.editor.Model) gme.getSource();
        Hashtable modif = new Hashtable();
        while (enumeration.hasMoreElements()) {
            Object nextElem = enumeration.nextElement();
            if (nextElem instanceof Lifeline) {
                Lifeline ll = (Lifeline) nextElem;
                LifelineCell llc = locateLifelineCell(ll, model);
                Map att = llc.getAttributes();
                Rectangle llclastpos = GraphConstants.getBounds(att).getBounds();
                Point newpoint = new Point(llclastpos.x - (currentpos.x - lastpos.x), llclastpos.y - (currentpos.y - lastpos.y));
                Rectangle npos = new Rectangle(newpoint, llclastpos.getSize());
                GraphConstants.setBounds(att, npos);
                modif.put(llc, att);
                this.moveLifeline(llc, newpoint, llclastpos.getLocation(), model);
            }
        }
        model.edit(modif, null, null, null);
    }

    private void movedLifeline(LifelineCell lc, org.jgraph.event.GraphModelEvent gme) {
        ingenias.editor.Model model = (ingenias.editor.Model) gme.getSource();
        Map protocolMap = (Map) this.locateProtocolCell((Lifeline) lc.getUserObject(), (ingenias.editor.Model) gme.getSource()).getAttributes();
        Rectangle lloldpos = GraphConstants.getBounds((Map) gme.getChange().getAttributes().get(lc)).getBounds();
        Rectangle llcurrentpos = GraphConstants.getBounds(lc.getAttributes()).getBounds();
        Rectangle protpos = GraphConstants.getBounds(protocolMap).getBounds();
        if (!protpos.union(llcurrentpos).equals(protpos)) {
            Map m = new Hashtable();
            Map nmap = lc.getAttributes();
            GraphConstants.setBounds(nmap, lloldpos);
            m.put(lc, nmap);
            model.edit(m, null, null, null);
        } else {
            Map m = new Hashtable();
            Map nmap = lc.getAttributes();
            llcurrentpos.y = lloldpos.y;
            GraphConstants.setBounds(nmap, llcurrentpos);
            m.put(lc, nmap);
            model.edit(m, null, null, null);
            this.moveLifeline(lc, llcurrentpos.getLocation(), lloldpos.getLocation(), model);
        }
    }

    private void moveLifeline(LifelineCell lc, Point dest, Point orig, ingenias.editor.Model model) {
        Rectangle bounds = GraphConstants.getBounds(lc.getAttributes()).getBounds();
        Lifeline ll = (Lifeline) lc.getUserObject();
        Enumeration enumeration = ll.getChildrenElements();
        Map elems = new Hashtable();
        int previousy = bounds.y + (bounds.height);
        int despx = dest.x - orig.x;
        int despy = dest.y - orig.y;
        bounds.x = bounds.x + despx;
        bounds.y = bounds.y + despy;
        while (enumeration.hasMoreElements()) {
            Object nextElement = enumeration.nextElement();
            if (nextElement instanceof Column) {
                Column col = (Column) nextElement;
                ColumnCell cc = this.locateColumnCell(col, model);
                Map colmap = cc.getAttributes();
                Rectangle oldpos1 = GraphConstants.getBounds(colmap).getBounds();
                Rectangle npos = oldpos1;
                npos.x = npos.x + despx;
                npos.y = npos.y + despy;
                npos.width = 30;
                Enumeration ports = col.getChildrenElements();
                while (ports.hasMoreElements()) {
                    AUMLPort port = (AUMLPort) ports.nextElement();
                    AUMLPortCell portcell = this.locatePortCell(port, model);
                    if (portcell == null) {
                        System.err.println("could not find a portcell for a cell in " + ll);
                    } else {
                        Map portmap = portcell.getAttributes();
                        Rectangle oldpos = GraphConstants.getBounds(portmap).getBounds();
                        Rectangle nppos = new Rectangle(oldpos.x + despx, oldpos.y + despy, 30, 10);
                        GraphConstants.setBounds(portmap, nppos);
                        elems.put(portcell, portmap);
                    }
                }
                GraphConstants.setBounds(colmap, npos);
                elems.put(cc, colmap);
            }
        }
        model.edit(elems, null, null, null);
    }

    private Rectangle computeAlternativeRowSize(AUMLAlternativeRow ar, ingenias.editor.Model model) {
        Enumeration enumeration = ar.getChildrenElements();
        Rectangle rect = new Rectangle();
        rect.x = Integer.MAX_VALUE;
        rect.y = Integer.MAX_VALUE;
        rect.width = Integer.MIN_VALUE;
        rect.height = Integer.MIN_VALUE;
        while (enumeration.hasMoreElements()) {
            Column col = (Column) enumeration.nextElement();
            ColumnCell cc = this.locateColumnCell(col, model);
            Rectangle ccbounds = GraphConstants.getBounds(cc.getAttributes()).getBounds();
            rect.x = Math.min(ccbounds.x, rect.x);
            rect.y = Math.min(ccbounds.y, rect.y);
            rect.height = Math.max(ccbounds.height, rect.height);
            rect.width = Math.max(ccbounds.x + ccbounds.width, rect.x + rect.width) - rect.x;
        }
        return rect;
    }

    public Vector getLifelines(ingenias.editor.Model model) {
        Vector result = new Vector();
        for (int k = 0; k < model.getRootCount(); k++) {
            if (model.getRootAt(k) instanceof LifelineCell) {
                result.add(((LifelineCell) model.getRootAt(k)).getUserObject());
            }
        }
        return result;
    }

    private Rectangle getMaxContainerSize(AUMLContainer cont) {
        Enumeration enumeration = cont.getChildrenElements();
        DefaultGraphCell dgc1 = AUMLDiagramChangesManager.getCellFromUserObject(cont);
        Rectangle finalSize = new Rectangle(0, 0, 0, 0);
        if (dgc1.getAttributes() != null && GraphConstants.getBounds(dgc1.getAttributes()) != null) {
            finalSize = GraphConstants.getBounds(dgc1.getAttributes()).getBounds();
            while (enumeration.hasMoreElements()) {
                AUMLComponent ac = (AUMLComponent) enumeration.nextElement();
                DefaultGraphCell dgc = (DefaultGraphCell) AUMLDiagramChangesManager.getCellFromUserObject(ac);
                if (dgc != null && dgc.getAttributes() != null && GraphConstants.getBounds(dgc.getAttributes()) != null) {
                    Rectangle ccbounds = null;
                    if (AUMLContainer.class.isAssignableFrom(ac.getClass())) {
                        ccbounds = this.getMaxContainerSize((AUMLContainer) ac);
                    } else {
                        ccbounds = GraphConstants.getBounds(dgc.getAttributes()).getBounds();
                    }
                    finalSize.x = Math.min(ccbounds.x, finalSize.x);
                    finalSize.y = Math.min(ccbounds.y, finalSize.y);
                    finalSize.height = Math.max(ccbounds.y + ccbounds.height, finalSize.y + finalSize.height) - finalSize.y;
                    finalSize.width = Math.max(ccbounds.x + ccbounds.width, finalSize.x + finalSize.width) - finalSize.x;
                }
            }
        }
        return finalSize;
    }

    private Object createCell(Column col, ingenias.editor.Model model) {
        AUMLPort port = ingenias.editor.ObjectManager.getInstance().createAUMLPort(ingenias.editor.Editor.getNewId("port"));
        AUMLPortCell aumlport = new AUMLPortCell(port);
        Map pmap = new Hashtable();
        col.addChildren(port);
        port.setParent(col);
        Hashtable ht = new Hashtable();
        ht.put(aumlport, pmap);
        model.insert(new Object[] { aumlport }, ht, null, null, null);
        return aumlport;
    }

    private void updateProtocol(Protocol prot, ingenias.editor.Model model) {
        DefaultGraphCell dgc = AUMLDiagramChangesManager.getCellFromUserObject(prot);
        Rectangle rect = this.getMaxContainerSize(prot);
        Map map = dgc.getAttributes();
        Hashtable ht = new Hashtable();
        ht.put(dgc, map);
        model.edit(ht, null, null, null);
    }

    public void graphChanged(org.jgraph.event.GraphModelEvent gme) {
        ingenias.editor.IDE.setChanged();
        if (enabled && this.workingObject == null && !(gme.getChange().getInserted() != null && gme.getChange().getInserted().length > 0)) {
            workingObject = "hello";
            Object[] objs = gme.getChange().getChanged();
            Vector movedProtocols = new Vector();
            if (objs != null) {
                for (int k = 0; k < objs.length; k++) {
                    if (objs[k] != null) {
                        if (objs[k] instanceof ProtocolCell && gme.getChange().getPreviousAttributes() != null && gme.getChange().getPreviousAttributes().get(objs[k]) != null) {
                            this.movedProtocol((ProtocolCell) objs[k], gme);
                            movedProtocols.add(objs[k]);
                        }
                        if (objs[k] instanceof LifelineCell && gme.getChange().getPreviousAttributes() != null && gme.getChange().getPreviousAttributes().get(objs[k]) != null) {
                            ProtocolCell pc = this.locateProtocolCell((Lifeline) ((LifelineCell) objs[k]).getUserObject(), (ingenias.editor.Model) gme.getSource());
                            if (!movedProtocols.contains(pc)) {
                                this.movedLifeline((LifelineCell) objs[k], gme);
                            }
                        }
                    }
                }
            }
            workingObject = null;
        }
    }
}
