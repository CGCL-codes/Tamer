package br.unb.syntainia.gui.graphicinteraction.jgraph.rendering;

import br.unb.syntainia.gui.graphicinteraction.jgraph.Constants;
import br.unb.syntainia.gui.graphicinteraction.jgraph.rendering.TitleCellView.TitleCellRenderer;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;

/**
 *
 * @author luizaugusto
 */
public class GeneGroupCellView extends VertexView implements Constants {

    private static final long serialVersionUID = -4669815772148910055L;

    public static transient GeneGroupCellRenderer geneGroupRenderer;

    private GeneGroupCell geneGroupCell;

    private boolean selectingGenes;

    static {
        try {
            geneGroupRenderer = new GeneGroupCellRenderer();
        } catch (Error e) {
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void updateGroupBounds() {
        @SuppressWarnings("rawtypes") LinkedList result = new LinkedList();
        for (CellView child : getChildViews()) if (includeInGroupBounds(child)) {
            result.add(child);
            if (child instanceof TitleCellView) {
                TitleCell titleCell = ((TitleCellView) child).getTitleCell();
                TitleCellRenderer component = (TitleCellRenderer) ((TitleCellView) child).getRenderer();
                int titleWidth = component.getFontMetrics(GENES_GROUP_FONT).stringWidth(titleCell.getTitle());
                Rectangle2D rect = child.getBounds();
                child.getBounds().setFrame(rect.getX(), rect.getY(), titleWidth, rect.getHeight());
            }
        }
        CellView[] children = new CellView[result.size()];
        result.toArray(children);
        Rectangle2D r = getBounds(children);
        int groupBorder = GraphConstants.getInset(getAllAttributes());
        if (r != null) {
            r.setFrame(r.getX() - groupBorder, r.getY() - groupBorder, r.getWidth() + 2 * groupBorder, r.getHeight() + 2 * groupBorder);
            r.setFrame(r.getX() - 5, r.getY() - 5, r.getWidth() + 10, r.getHeight() + 10);
        }
        groupBounds = r;
    }

    public GeneGroupCellView(GeneGroupCell cell) {
        super(cell);
        this.geneGroupCell = cell;
    }

    @Override
    public CellViewRenderer getRenderer() {
        return geneGroupRenderer;
    }

    /**
     * @return the geneGroupCell
     */
    public GeneGroupCell getGeneGroupCell() {
        return geneGroupCell;
    }

    /**
     * @param geneGroupCell the geneGroupCell to set
     */
    public void setGeneGroupCell(GeneGroupCell geneGroupCell) {
        this.geneGroupCell = geneGroupCell;
    }

    /**
     * @return the selectingGenes
     */
    public boolean isSelectingGenes() {
        return selectingGenes;
    }

    /**
     * @param selectingGenes the selectingGenes to set
     */
    public void setSelectingGenes(boolean selectingGenes) {
        this.selectingGenes = selectingGenes;
    }

    public static class GeneGroupCellRenderer extends AbstractCellRenderer<GeneGroupCellView> {

        private static final long serialVersionUID = 5204279228756722390L;

        @Override
        public void paint(Graphics g) {
            Graphics2D graphics = (Graphics2D) g;
            if (isSelected()) {
                if (isPreview()) graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, MOVEMENT_ALPHA_VALUE));
                graphics.setColor(SELECTION_COLOR);
                graphics.fillRoundRect(0, 0, (int) getView().getBounds().getWidth(), (int) getView().getBounds().getHeight(), ROUND_RECT_CORNER, ROUND_RECT_CORNER);
            }
        }
    }
}
