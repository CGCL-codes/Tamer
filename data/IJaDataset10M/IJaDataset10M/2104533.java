package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalLookAndFeel;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.UUIDManager;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MModelElement;

/** 
 * Class to display a UML note in a diagram 
 * Since we don't need stereotypes for the note and an
 * empty stereotype textfield causes problems with the
 * note layout, I subclass FigNode instead of FigNodeModelElement.
 */
public class FigNote extends FigNode implements VetoableChangeListener, DelayedVChangeListener, MouseListener, KeyListener, PropertyChangeListener, MElementListener {

    public int x = 0;

    public int y = 0;

    public int width = 80;

    public int height = 60;

    public int gapY = 10;

    protected boolean _readyToEdit = true;

    public static Font LABEL_FONT;

    public static Font ITALIC_LABEL_FONT;

    public final int MARGIN = 2;

    static {
        LABEL_FONT = MetalLookAndFeel.getSubTextFont();
        ITALIC_LABEL_FONT = new Font(LABEL_FONT.getFamily(), Font.ITALIC, LABEL_FONT.getSize());
    }

    private MModelElement _noteOwner = null;

    FigText _text;

    /** UML does not really use ports, so just define one big one so
     *  that users can drag edges to or from any point in the icon. */
    FigRect _bigPort;

    FigPoly _body;

    FigPoly _urCorner;

    public FigNote() {
        _body = new FigPoly(Color.black, Color.white);
        _body.addPoint(x, y);
        _body.addPoint(x + width - 1 - gapY, y);
        _body.addPoint(x + width - 1, y + gapY);
        _body.addPoint(x + width - 1, y + height - 1);
        _body.addPoint(x, y + height - 1);
        _body.addPoint(x, y);
        _body.setFilled(true);
        _body.setLineWidth(1);
        _urCorner = new FigPoly(Color.black, Color.white);
        _urCorner.addPoint(x + width - 1 - gapY, y);
        _urCorner.addPoint(x + width - 1, y + gapY);
        _urCorner.addPoint(x + width - 1 - gapY, y + gapY);
        _urCorner.addPoint(x + width - 1 - gapY, y);
        _urCorner.setFilled(true);
        _urCorner.setLineWidth(1);
        _bigPort = new FigRect(x, y, width, height, null, null);
        _bigPort.setFilled(false);
        _bigPort.setLineWidth(0);
        _text = new FigText(2, 2, width - 2 - gapY, height - 4, true);
        _text.setFont(LABEL_FONT);
        _text.setTextColor(Color.black);
        _text.setMultiLine(true);
        _text.setAllowsTab(false);
        _text.setText(placeString());
        _text.setJustification(FigText.JUSTIFY_LEFT);
        _text.setFilled(false);
        _text.setLineWidth(0);
        addFig(_bigPort);
        addFig(_body);
        addFig(_urCorner);
        addFig(_text);
        setBlinkPorts(false);
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
        updateEdges();
        _readyToEdit = false;
    }

    /**
     * Construct a new note
     *
     * @param gm The graphmodel
     * @param node The underlying MComment node
     */
    public FigNote(GraphModel gm, Object node) {
        this();
        setOwner(node);
        modelChanged();
    }

    /**
     * Create a note for a given model element.
     *
     * @param element The annotated model element.
     */
    public FigNote(MModelElement element) {
        this();
        MComment node = UmlFactory.getFactory().getCore().createComment();
        setOwner(node);
        element.addComment(node);
        if (element instanceof MStateVertex) {
            ProjectBrowser pb = ProjectBrowser.TheInstance;
            if (pb.getTarget() instanceof UMLStateDiagram) {
                StateDiagramGraphModel gm = (StateDiagramGraphModel) (((UMLStateDiagram) pb.getTarget()).getGraphModel());
                node.setNamespace(gm.getNamespace());
            }
        } else {
            node.setNamespace(element.getNamespace());
        }
        storeNote(placeString());
    }

    /**
     * Get the default text for this figure. 
     *   
     * @return The default text for this figure.
     */
    public String placeString() {
        return "new note";
    }

    /**
     * Clone this figure.
     *
     * @return The cloned figure.
     */
    public Object clone() {
        FigNote figClone = (FigNote) super.clone();
        Vector v = figClone.getFigs();
        figClone._bigPort = (FigRect) v.elementAt(0);
        figClone._body = (FigPoly) v.elementAt(1);
        figClone._urCorner = (FigPoly) v.elementAt(2);
        figClone._text = (FigText) v.elementAt(3);
        return figClone;
    }

    /** If the user double clicks on any part of this FigNode, pass it
     *  down to one of the internal Figs.  This allows the user to
     *  initiate direct text editing. */
    public void mouseClicked(MouseEvent me) {
        if (!_readyToEdit) {
            if (getOwner() instanceof MModelElement) {
                MModelElement own = (MModelElement) getOwner();
                storeNote("");
                _readyToEdit = true;
            } else {
                System.out.println("not ready to edit note");
                return;
            }
        }
        if (me.isConsumed()) return;
        if (me.getClickCount() >= 2 && !(me.isPopupTrigger() || me.getModifiers() == InputEvent.BUTTON3_MASK)) {
            if (getOwner() == null) return;
            Fig f = hitFig(new Rectangle(me.getX() - 2, me.getY() - 2, 4, 4));
            if (f instanceof MouseListener) ((MouseListener) f).mouseClicked(me);
        }
        me.consume();
    }

    public void vetoableChange(PropertyChangeEvent pce) {
        Object src = pce.getSource();
        if (src == getOwner()) {
            DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
            SwingUtilities.invokeLater(delayedNotify);
        } else System.out.println("FigNodeModelElement got vetoableChange" + " from non-owner:" + src);
    }

    public void delayedVetoableChange(PropertyChangeEvent pce) {
        Object src = pce.getSource();
        startTrans();
        modelChanged();
        updateBounds();
        endTrans();
    }

    public void propertyChange(PropertyChangeEvent pve) {
        Object src = pve.getSource();
        String pName = pve.getPropertyName();
        if (pName.equals("editing") && Boolean.FALSE.equals(pve.getNewValue())) {
            try {
                startTrans();
                textEdited((FigText) src);
                Rectangle bbox = getBounds();
                Dimension minSize = getMinimumSize();
                bbox.width = Math.max(bbox.width, minSize.width);
                bbox.height = Math.max(bbox.height, minSize.height);
                setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
                endTrans();
            } catch (PropertyVetoException ex) {
                System.out.println("could not parse and use the text you entered");
            }
        } else super.propertyChange(pve);
    }

    public void propertySet(MElementEvent mee) {
        modelChanged();
        damage();
    }

    public void listRoleItemSet(MElementEvent mee) {
        modelChanged();
        damage();
    }

    public void recovered(MElementEvent mee) {
    }

    public void removed(MElementEvent mee) {
        this.delete();
    }

    public void roleAdded(MElementEvent mee) {
        modelChanged();
        damage();
    }

    public void roleRemoved(MElementEvent mee) {
        modelChanged();
        damage();
    }

    public void keyPressed(KeyEvent ke) {
        if (!_readyToEdit) {
            if (getOwner() instanceof MModelElement) {
                storeNote("");
                _readyToEdit = true;
            } else {
                System.out.println("not ready to edit note");
                return;
            }
        }
        if (ke.isConsumed()) return;
        if (getOwner() == null) return;
        _text.keyPressed(ke);
    }

    /** not used, do nothing. */
    public void keyReleased(KeyEvent ke) {
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void setLineColor(Color col) {
        _body.setLineColor(col);
        _urCorner.setLineColor(col);
    }

    public Color getLineColor() {
        return _body.getLineColor();
    }

    public void setFillColor(Color col) {
        _body.setFillColor(col);
        _urCorner.setFillColor(col);
    }

    public Color getFillColor() {
        return _body.getFillColor();
    }

    public void setFilled(boolean f) {
        _text.setFilled(false);
        _body.setFilled(f);
        _urCorner.setFilled(f);
    }

    public boolean getFilled() {
        return _body.getFilled();
    }

    public void setLineWidth(int w) {
        _text.setLineWidth(0);
        _body.setLineWidth(w);
        _urCorner.setLineWidth(w);
    }

    public int getLineWidth() {
        return _body.getLineWidth();
    }

    protected void textEdited(FigText ft) throws PropertyVetoException {
        if (ft == _text) storeNote(ft.getText());
    }

    public void setEnclosingFig(Fig encloser) {
        super.setEnclosingFig(encloser);
    }

    /**
     * Store a note in the associated model element.
     *
     * @param note The note to store.
     */
    public final void storeNote(String note) {
        if (getOwner() != null) ((MModelElement) getOwner()).setName(note);
    }

    /**
     * Retrieve the note from the associated model element.
     *
     * @return The note from the associated model element.
     */
    public final String retrieveNote() {
        return (getOwner() != null) ? ((MModelElement) getOwner()).getName() : null;
    }

    public boolean getUseTrapRect() {
        return true;
    }

    /**
     * Get the minimum size for the note figure.
     *
     * @return The minimum size for the note figure.
     */
    public Dimension getMinimumSize() {
        Dimension textMinimumSize = _text.getMinimumSize();
        return new Dimension(textMinimumSize.width + 4 + gapY, textMinimumSize.height + 4);
    }

    public void setBounds(int x, int y, int w, int h) {
        if (_text == null) return;
        Rectangle oldBounds = getBounds();
        _text.setBounds(x + 2, y + 2, w - 4 - gapY, h - 4);
        _bigPort.setBounds(x, y, w, h);
        Polygon newPoly = new Polygon();
        newPoly.addPoint(x, y);
        newPoly.addPoint(x + w - 1 - gapY, y);
        newPoly.addPoint(x + w - 1, y + gapY);
        newPoly.addPoint(x + w - 1, y + h - 1);
        newPoly.addPoint(x, y + h - 1);
        newPoly.addPoint(x, y);
        _body.setPolygon(newPoly);
        _urCorner.setBounds(x + w - 1 - gapY, y, gapY, gapY);
        calcBounds();
        firePropChange("bounds", oldBounds, getBounds());
    }

    protected void updateBounds() {
        Rectangle bbox = getBounds();
        Dimension minSize = getMinimumSize();
        bbox.width = Math.max(bbox.width, minSize.width);
        bbox.height = Math.max(bbox.height, minSize.height);
        setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
    }

    /**
     * Set the owner for this figure.
     *
     * @param own The new owner of this figure.
     */
    public void setOwner(Object own) {
        Object oldOwner = getOwner();
        super.setOwner(own);
        if (oldOwner instanceof MModelElement) ((MModelElement) oldOwner).removeMElementListener(this);
        if (own instanceof MModelElement) {
            MModelElement me = (MModelElement) own;
            me.removeMElementListener(this);
            me.addMElementListener(this);
            if (me.getUUID() == null) me.setUUID(UUIDManager.SINGLETON.getNewUUID());
        }
        modelChanged();
        _readyToEdit = true;
        updateBounds();
    }

    /**
     * Delete this figure.
     */
    public void delete() {
        MComment commentNode = (MComment) getOwner();
        Collection annotatedElements = commentNode.getAnnotatedElements();
        for (Iterator iter = annotatedElements.iterator(); iter.hasNext(); ) {
            commentNode.removeAnnotatedElement((MModelElement) iter.next());
        }
        super.delete();
    }

    /** 
     * This is called aftern any part of the UML MModelElement has
     * changed. This method automatically updates the note FigText. 
     */
    private final void modelChanged() {
        if (_readyToEdit) {
            String noteStr = retrieveNote();
            if (noteStr != null) _text.setText(noteStr);
        }
    }
}
