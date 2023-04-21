package uk.ac.bolton.archimate.editor.diagram.figures;

import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * Abstract Figure with Label
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractLabelFigure extends AbstractDiagramModelObjectFigure {

    private Label fLabel;

    public AbstractLabelFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
    }

    @Override
    protected void setUI() {
        setLayoutManager(new DelegatingLayout());
        Locator labelLocator = new Locator() {

            public void relocate(IFigure target) {
                Rectangle bounds = calculateTextControlBounds();
                if (bounds != null) {
                    translateFromParent(bounds);
                    target.setBounds(bounds);
                }
            }
        };
        add(getLabel(), labelLocator);
    }

    public void refreshVisuals() {
        setText();
        setFont();
        setFillColor();
        setFontColor();
    }

    protected void setText() {
        String text = StringUtils.safeString(getDiagramModelObject().getName());
        getLabel().setText(text);
    }

    public Label getLabel() {
        if (fLabel == null) {
            fLabel = new Label("");
        }
        return fLabel;
    }

    @Override
    public IFigure getTextControl() {
        return getLabel();
    }

    /**
     * Calculate the Text Control Bounds or null if none.
     * The Default is to delegate to the Figure Delegate.
     */
    protected Rectangle calculateTextControlBounds() {
        if (getFigureDelegate() != null) {
            return getFigureDelegate().calculateTextControlBounds();
        }
        return null;
    }
}
