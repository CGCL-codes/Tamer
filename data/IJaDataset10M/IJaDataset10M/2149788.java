package net.sourceforge.umljgraph.vertex;

import org.jgraph.graph.CellViewRenderer;

/**
 * The vertex view for <code>UMLUseCaseCell</code>.
 *
 * @author Jack Hong
 * @author Michael Rumpf
 */
public class UMLUseCaseView extends UMLVertexView {

    private static final long serialVersionUID = 1L;

    /**
     * The renderer for <code>UMLUseCaseView</code>.
     */
    private static final UMLUseCaseRenderer RENDERER = new UMLUseCaseRenderer();

    /**
     * Creates an instance of <code>UMLUseCaseView</code>.
     *
     * @param cell The cell associated with this view.
     */
    public UMLUseCaseView(Object cell) {
        super(cell);
    }

    /**
     * Returns a reference of the renderer.
     *
     * @returns a reference of the renderer.
     *
     * @see org.jgraph.graph.AbstractCellView#getRenderer()
     */
    public CellViewRenderer getRenderer() {
        return RENDERER;
    }
}
