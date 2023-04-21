package net.sf.jtreemap.swttreemap;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * This figure creates children lazily.  They are created when this
 * figure is painted.
 * 
 * @author Nigel Westbury
 *
 */
public class TreemapFigure extends Figure {

    private TreemapViewer viewer;

    private Object element;

    public TreemapFigure(TreemapViewer viewer, Object element) {
        this.viewer = viewer;
        this.element = element;
        TreeMapLayout layout = new TreeMapLayout(viewer.getSplitStrategy());
        setLayoutManager(layout);
        setOpaque(true);
    }

    @Override
    public void layout() {
        if (getChildren().isEmpty()) {
            ITreeContentProvider contentProvider = (ITreeContentProvider) viewer.getContentProvider();
            ITreemapLabelProvider labelProvider = (ITreemapLabelProvider) viewer.getLabelProvider();
            for (Object childElement : contentProvider.getChildren(element)) {
                if (contentProvider.hasChildren(childElement)) {
                    Border border = labelProvider.getBorder(childElement);
                    IFigure expandedFigure = new TreemapFigure(viewer, childElement);
                    expandedFigure.setBorder(border);
                    IFigure collapsedFigure = labelProvider.getFigure(childElement);
                    add(new ExpandableFigure(collapsedFigure, expandedFigure), labelProvider.getWeighting(childElement));
                } else {
                    IFigure childFigure = labelProvider.getFigure(childElement);
                    add(childFigure, labelProvider.getWeighting(childElement));
                }
            }
        }
        super.layout();
    }
}
