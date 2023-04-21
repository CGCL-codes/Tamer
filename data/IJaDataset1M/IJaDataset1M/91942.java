package org.argouml.uml.diagram.activity.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.application.events.ArgoEvent;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigSingleLineText;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML ObjectFlowState in a diagram.<p>
 *
 * The Fig of this modelelement may either represent the following UMLelements:
 * <p>
 * (1) an ObjectFlowState with a Classifier as type, or <p>
 * (2) an ObjectFlowState with a ClassifierInState as type. <p>
 *
 * In both cases (1) and (2), the Fig shows
 * the underlined name of the Classifier,
 * and in the latter case (2), it shows also the names of the states
 * of the ClassifierInState. <p>
 *
 * In the examples in the UML standard, this is written like<pre>
 *      PurchaseOrder
 *       [approved]
 * </pre>
 * i.e. in 2 lines. The first line is underlined,
 * to indicate that it is an instance (object).<p>
 *
 * The fact that the first line is underlined, and the 2nd not, is the
 * reason to implement them in 2 seperate Figs.<p>
 *
 * TODO: Allow stereotypes to be shown.
 *
 * @author mvw
 */
public class FigObjectFlowState extends FigNodeModelElement {

    private static final int PADDING = 8;

    private static final int OFFSET = 10;

    private static final int WIDTH = 70;

    private static final int HEIGHT = 50;

    private NotationProvider notationProviderType;

    private NotationProvider notationProviderState;

    private FigRect cover;

    private FigText state;

    /**
     * Main Constructor FigObjectFlowState (called from file loading).
     */
    public FigObjectFlowState() {
        setBigPort(new FigRect(OFFSET, OFFSET, WIDTH, HEIGHT, Color.cyan, Color.cyan));
        cover = new FigRect(OFFSET, OFFSET, WIDTH, HEIGHT, Color.black, Color.white);
        getNameFig().setUnderline(true);
        getNameFig().setLineWidth(0);
        state = new FigSingleLineText(OFFSET, OFFSET, WIDTH, 21, true);
        addFig(getBigPort());
        addFig(cover);
        addFig(getNameFig());
        addFig(state);
        enableSizeChecking(false);
        setReadyToEdit(false);
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
        ArgoEventPump.addListener(ArgoEvent.ANY_NOTATION_EVENT, this);
    }

    /**
     * Constructor FigObjectFlowState that hooks the Fig into
     * an existing UML model element.
     *
     * @param gm ignored!
     * @param node owner, i.e. the UML element
     */
    public FigObjectFlowState(GraphModel gm, Object node) {
        this();
        setOwner(node);
        enableSizeChecking(true);
    }

    @Override
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        if (Model.getFacade().isAModelElement(own)) {
            notationProviderType = NotationProviderFactory2.getInstance().getNotationProvider(NotationProviderFactory2.TYPE_OBJECTFLOWSTATE_TYPE, own);
            notationProviderState = NotationProviderFactory2.getInstance().getNotationProvider(NotationProviderFactory2.TYPE_OBJECTFLOWSTATE_STATE, own);
        }
    }

    @Override
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        renderingChanged();
        updateListeners(getOwner(), getOwner());
    }

    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
        Set<Object[]> l = new HashSet<Object[]>();
        if (newOwner != null) {
            l.add(new Object[] { newOwner, new String[] { "type", "remove" } });
            Object type = Model.getFacade().getType(newOwner);
            if (Model.getFacade().isAClassifier(type)) {
                if (Model.getFacade().isAClassifierInState(type)) {
                    Object classifier = Model.getFacade().getType(type);
                    l.add(new Object[] { classifier, "name" });
                    l.add(new Object[] { type, "inState" });
                    Collection states = Model.getFacade().getInStates(type);
                    Iterator i = states.iterator();
                    while (i.hasNext()) {
                        l.add(new Object[] { i.next(), "name" });
                    }
                } else {
                    l.add(new Object[] { type, "name" });
                }
            }
        }
        updateElementListeners(l);
    }

    @Override
    public Object clone() {
        FigObjectFlowState figClone = (FigObjectFlowState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigRect) it.next();
        figClone.setNameFig((FigText) it.next());
        figClone.state = (FigText) it.next();
        return figClone;
    }

    @Override
    public void setEnclosingFig(Fig encloser) {
        LayerPerspective layer = (LayerPerspective) getLayer();
        if (layer == null) return;
        super.setEnclosingFig(encloser);
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension tempDim = getNameFig().getMinimumSize();
        int w = tempDim.width + PADDING * 2;
        int h = tempDim.height + PADDING;
        tempDim = state.getMinimumSize();
        w = Math.max(w, tempDim.width + PADDING * 2);
        h = h + PADDING + tempDim.height + PADDING;
        return new Dimension(Math.max(w, WIDTH / 2), Math.max(h, HEIGHT / 2));
    }

    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        Rectangle oldBounds = getBounds();
        Dimension classDim = getNameFig().getMinimumSize();
        Dimension stateDim = state.getMinimumSize();
        int blank = (h - PADDING - classDim.height - stateDim.height) / 2;
        getNameFig().setBounds(x + PADDING, y + blank, w - PADDING * 2, classDim.height);
        state.setBounds(x + PADDING, y + blank + classDim.height + PADDING, w - PADDING * 2, stateDim.height);
        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);
        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    @Override
    public void renderingChanged() {
        updateClassifierText();
        updateStateText();
        updateBounds();
        damage();
    }

    /**
     * Updates the text of the classifier FigText.
     */
    private void updateClassifierText() {
        if (isReadyToEdit()) {
            if (notationProviderType != null) {
                getNameFig().setText(notationProviderType.toString(getOwner(), getNotationArguments()));
            }
        }
    }

    /**
     * Updates the text of the state FigText.
     */
    private void updateStateText() {
        if (isReadyToEdit()) {
            state.setText(notationProviderState.toString(getOwner(), getNotationArguments()));
        }
    }

    @Override
    public void setLineColor(Color col) {
        cover.setLineColor(col);
    }

    @Override
    public Color getLineColor() {
        return cover.getLineColor();
    }

    @Override
    public void setFillColor(Color col) {
        cover.setFillColor(col);
    }

    @Override
    public Color getFillColor() {
        return cover.getFillColor();
    }

    @Override
    public void setFilled(boolean f) {
        cover.setFilled(f);
    }

    @Override
    public boolean isFilled() {
        return cover.isFilled();
    }

    @Override
    public void setLineWidth(int w) {
        cover.setLineWidth(w);
    }

    @Override
    public int getLineWidth() {
        return cover.getLineWidth();
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        if (!isReadyToEdit()) {
            if (Model.getFacade().isAModelElement(getOwner())) {
                updateClassifierText();
                updateStateText();
                setReadyToEdit(true);
            } else {
                return;
            }
        }
        if (ke.isConsumed() || getOwner() == null) {
            return;
        }
        getNameFig().keyTyped(ke);
    }

    @Override
    protected void textEdited(FigText ft) throws PropertyVetoException {
        if (ft == getNameFig()) {
            notationProviderType.parse(getOwner(), ft.getText());
            ft.setText(notationProviderType.toString(getOwner(), null));
        } else if (ft == state) {
            notationProviderState.parse(getOwner(), ft.getText());
            ft.setText(notationProviderState.toString(getOwner(), null));
        }
    }

    @Override
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp(notationProviderType.getParsingHelp());
        }
        if (ft == state) {
            showHelp(notationProviderState.getParsingHelp());
        }
    }

    @Override
    public Selection makeSelection() {
        return new SelectionActionState(this);
    }
}
