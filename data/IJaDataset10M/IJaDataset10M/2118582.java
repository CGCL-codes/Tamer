package meteoric.metaDepth.web.client.visual.canvas;

import java.util.*;
import com.objetdirect.tatami.client.gfx.*;

/**
 * A wrapper for manipulators of visual objects
 * 
 * @author jdelara
 * 
 */
public class VHandler {

    protected HashMap<GraphicObject, VisualArtefact> ht;

    protected MDCanvas canvas;

    protected GraphicObject selected = null;

    protected VisualArtefact selectedDrawing = null;

    protected Selector selector;

    public VHandler(MDCanvas c) {
        ht = new HashMap<GraphicObject, VisualArtefact>();
        canvas = c;
    }

    public VisualArtefact getDrawing(GraphicObject part) {
        return ht.get(part);
    }

    /**
	 * Adds the graphical object and its parts to the Hash map
	 * 
	 * @param d
	 */
    public void add(VisualArtefact d) {
        GraphicObject go = d.getGraphicObject();
        if (!ht.containsKey(go)) {
            ht.put(go, d);
        }
        List<GraphicObject> parts = d.getGraphicObjectParts();
        for (GraphicObject g : parts) {
            if (!ht.containsKey(g)) {
                ht.put(g, d);
            }
        }
    }

    public void swap(GraphicObject old, GraphicObject newgo, VisualArtefact d) {
        if (this.ht.containsKey(old)) {
            this.ht.remove(old);
            this.ht.put(newgo, d);
        }
    }

    public GraphicObject getSelected() {
        return this.selected;
    }

    public Selector getSelector() {
        return this.selector;
    }

    public VisualArtefact getSelectedDrawing() {
        if (this.selected != null) {
            return this.ht.get(this.selected);
        }
        return null;
    }

    public void redraw(GraphicObject o) {
        VisualArtefact d = ht.get(o);
        d.draw(canvas);
    }

    public void select(Drawing d) {
        if (selector != null) selector.undraw();
        this.selectedDrawing = d;
        selectDrawing(d.getPrincipalPart());
    }

    private void selectDrawing(GraphicObject o) {
        this.selector = Selector.instance(canvas, this.selectedDrawing);
        this.selector.draw(this.selectedDrawing);
        this.selected = o;
    }

    public void select(GraphicObject o, boolean drag) {
        if (o != null) {
            System.out.println("Selected = " + selected + " o=" + o + " selector=" + selector);
            if (selected != null) {
                if (selected.equals(o) || (selector != null && selector.hasPart(o))) {
                    if (!drag) {
                        selected = null;
                        selector.undraw();
                        System.out.println("Already selected, erasing selector " + selector);
                        Menu.updateView(null);
                        if (selectedDrawing instanceof Drawing) return;
                    }
                } else {
                    selector.undraw();
                }
            }
            if (!drag) {
                selectedDrawing = ht.get(o);
                System.out.println("Selecting " + selectedDrawing);
                if (selectedDrawing != null) {
                    this.selectDrawing(o);
                }
                Menu.updateView(selectedDrawing);
            }
        } else {
            Menu.updateView(null);
            selector.undraw();
        }
    }

    public void deselect() {
        if (selected != null && selector != null) {
            selected = null;
            selectedDrawing = null;
            Menu.updateView(selectedDrawing);
            selector.undraw();
        }
    }

    public void move(GraphicObject o, int dx, int dy) {
        if (selector.hasPart(o) && selectedDrawing instanceof VModel) {
            selector.translate(dx, dy);
            selector.moveToFront();
        } else {
            boolean moved = selectedDrawing.translate(canvas, dx, dy);
            if (selectedDrawing != null) {
                if (moved) {
                    selector.translate(dx, dy);
                }
                selector.moveToFront();
            } else {
                selector.undraw();
                selector = null;
            }
        }
    }

    public void delete(GraphicObject go) {
        this.ht.remove(go);
    }

    public void updatePosition(GraphicObject o) {
        if (selectedDrawing != null) {
            int finalX = (int) selector.getX();
            int finalY = (int) selector.getY();
            int initialX = (int) selectedDrawing.getX();
            int initialY = (int) selectedDrawing.getY();
            selectedDrawing.translate(canvas, finalX - initialX, finalY - initialY);
        }
    }
}
