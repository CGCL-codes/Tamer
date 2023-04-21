package com.safi.asterisk.figures;

import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;

/**
 * @generated NOT
 */
public class ChoiceFigure extends DefaultToolstepFigure {

    public enum SpacerSize {

        EMPTY, SMALL, NORMAL
    }

    ;

    /**
   * @generated
   */
    private Figure itemPanel;

    private static final Dimension SPACER_SIZE = new Dimension(5, 38);

    private static final Dimension SMALL_SPACER_SIZE = new Dimension(5, 19);

    private static final Dimension EMPTY_SPACER_SIZE = new Dimension(5, 0);

    private Figure spacer;

    /**
   * @generated
   */
    public ChoiceFigure() {
        super();
        setBorder(new MarginBorder(2));
    }

    protected void buildLayout() {
        BorderLayout layoutLabelPanel = new BorderLayout();
        layoutLabelPanel.setObserveVisibility(true);
        getRootPane().setLayoutManager(layoutLabelPanel);
        itemPanel = new Figure();
        itemPanel.setMinimumSize(new Dimension(20, 10));
        itemPanel.setBorder(null);
        itemPanel.setOpaque(false);
        itemPanel.setBackgroundColor(null);
        itemPanel.setToolTip(null);
        StackLayout stackLayout = new StackLayout();
        stackLayout.setObserveVisibility(true);
        itemPanel.setLayoutManager(stackLayout);
    }

    /**
   * @generated
   */
    protected void createContents() {
        createToolstepNameLabel();
        toolstepNameLabel.setTextWrap(false);
        Figure f = new Figure();
        FlowLayout fl = new FlowLayout(false);
        fl.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
        fl.setStretchMinorAxis(true);
        f.setLayoutManager(fl);
        f.add(toolstepNameLabel);
        getRootPane().add(f, BorderLayout.TOP);
        getRootPane().add(itemPanel, BorderLayout.RIGHT);
        spacer = new Figure();
        spacer.setOpaque(false);
        spacer.setBackgroundColor(null);
        setSpacerSize(SpacerSize.NORMAL);
        getRootPane().add(spacer, BorderLayout.BOTTOM);
    }

    public void setSpacerSize(SpacerSize size) {
        Dimension sz;
        switch(size) {
            case EMPTY:
                sz = EMPTY_SPACER_SIZE;
                break;
            case SMALL:
                sz = SMALL_SPACER_SIZE;
                break;
            default:
                sz = SPACER_SIZE;
                break;
        }
        spacer.setMinimumSize(sz);
        spacer.setPreferredSize(sz);
    }

    /**
   * @generated
   */
    public WrapLabel getFigureChoiceNameFigure() {
        return getToolstepNameLabel();
    }

    public IFigure getItemPanel() {
        return itemPanel;
    }
}
