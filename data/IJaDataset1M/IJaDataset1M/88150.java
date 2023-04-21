package net.confex.schema.figures;

import java.util.List;
import net.confex.schema.model.StateContainer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class SimpleContainerFigure extends RoundedRectangle implements IModelElementFigure {

    private static final int MIN_CORNER_SIZE = 10;

    private static final int MAX_CORNER_SIZE = 24;

    private static final int LIMIT_FOR_CHANGE_CORNER_SIZE = 50;

    public static final int SELECTION_LINE_WIDTH = 4;

    public static final int NORMAL_LINE_WIDTH = 1;

    private boolean selected;

    private Label tooltip_label = new Label("");

    private EditableLabel nameLabel;

    private IFigure pane;

    ScrollPane scrollpane;

    private Color name_back_color = new Color(Display.getCurrent(), 236, 255, 255);

    public IFigure getContentsPane() {
        return pane;
    }

    public void validate() {
        super.validate();
    }

    public SimpleContainerFigure(EditableLabel name) {
        this(name, null);
    }

    public SimpleContainerFigure(EditableLabel name, List colums) {
        RoundedRectangleBorder border = new RoundedRectangleBorder();
        setBorder(border);
        setOutline(false);
        corner = new Dimension(MIN_CORNER_SIZE, MIN_CORNER_SIZE);
        setCornerDimensions(corner);
        nameLabel = name;
        nameLabel.setBackgroundColor(name_back_color);
        nameLabel.setOpaque(false);
        pane = new FreeformLayer();
        pane.setOpaque(true);
        pane.setLayoutManager(new FreeformLayout());
        scrollpane = new ScrollPane();
        scrollpane.setViewport(new FreeformViewport());
        scrollpane.setContents(pane);
        scrollpane.setPreferredSize(-1, 50);
        ToolbarLayout layout = new ToolbarLayout();
        layout.setVertical(true);
        layout.setStretchMinorAxis(true);
        layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
        setLayoutManager(layout);
        add(nameLabel);
        add(scrollpane);
        super.setBackgroundColor(name_back_color);
        setOpaque(true);
    }

    /**
	 * Sets the dimensions of each corner. This will form the radii of the arcs which form the
	 * corners.
	 *
	 * @param d the dimensions of the corner
	 */
    public void setCornerDimensions(Dimension d) {
        super.setCornerDimensions(d);
        if (getBorder() instanceof RoundedRectangleBorder) {
            ((RoundedRectangleBorder) getBorder()).setCornerDimensions(d);
        }
    }

    public void setForegroundColor(Color fore_color) {
        if (pane != null) pane.setForegroundColor(fore_color);
        if (getBorder() instanceof LineBorder) {
            ((LineBorder) getBorder()).setColor(fore_color);
            ((LineBorder) getBorder()).setWidth(1);
        } else {
            super.setForegroundColor(fore_color);
            setLineWidth(1);
        }
    }

    public void setBackgroundColor(Color back_color) {
        pane.setBackgroundColor(back_color);
        scrollpane.setBackgroundColor(back_color);
    }

    public void modifyState(String state) {
        if (state.equals(StateContainer.COMPACT_STATE)) {
            this.setVisible(true);
            scrollpane.setVisible(false);
            pane.setVisible(false);
            IFigure parent = this.getParent();
            if (parent != null) {
                parent.remove(this);
                parent.add(this);
            }
        } else if (state.equals(StateContainer.STANDART_STATE)) {
            this.setVisible(true);
            scrollpane.setVisible(true);
            pane.setVisible(true);
            putOnTop();
        }
    }

    /**
	 * ������ ������ �������
	 */
    protected void putOnTop() {
        IFigure parent = this.getParent();
        if (parent != null) {
            parent.remove(this);
            parent.add(this);
        }
    }

    public void setText(String text) {
        nameLabel.setText(text);
    }

    public String getText() {
        return nameLabel.getText();
    }

    /**
	 * @return returns the label used to edit the name
	 */
    public EditableLabel getNameLabel() {
        return nameLabel;
    }

    /**
	 * Returns <code>true</code> if this Figure uses local coordinates. This means its
	 * children are placed relative to this Figure's top-left corner.
	 * @return <code>true</code> if this Figure uses local coordinates
	 * @since 2.0
	 */
    protected boolean useLocalCoordinates() {
        return true;
    }

    public void paintFigure(Graphics graphics) {
        calcCornerSize();
        int h = getBounds().height;
        scrollpane.setPreferredSize(-1, h - nameLabel.getBounds().height - this.corner.height / 2);
        super.paintFigure(graphics);
    }

    /**
	 * �������������� ������ ���������� ����� � ����������� �� �������
	 * ������.
	 */
    private void calcCornerSize() {
        int min_size = (getBounds().width < getBounds().height) ? getBounds().width : getBounds().height;
        if (min_size < LIMIT_FOR_CHANGE_CORNER_SIZE) {
            if (corner.height != MIN_CORNER_SIZE) {
                corner.height = MIN_CORNER_SIZE;
                corner.width = MIN_CORNER_SIZE;
                setCornerDimensions(corner);
            }
        } else {
            if (corner.height != MAX_CORNER_SIZE) {
                corner.height = MAX_CORNER_SIZE;
                corner.width = MAX_CORNER_SIZE;
                setCornerDimensions(corner);
            }
        }
    }

    public Rectangle getSelectionRectangle() {
        return this.getBounds();
    }

    /**
	 * @see IFigure#setConstraint(IFigure, Object)
	 */
    public void setConstraint(IFigure child, Object constraint) {
        if (child.getParent() != this) throw new IllegalArgumentException("Figure must be a child");
        if (this.getLayoutManager() != null) this.getLayoutManager().setConstraint(child, constraint);
        revalidate();
    }

    /**
	 * Sets the selection state of this SimpleActivityLabel
	 * 
	 * @param b
	 *            true will cause the label to appear selected
	 */
    public void setSelected(boolean isSelected) {
        selected = isSelected;
        if (getBorder() instanceof LineBorder) {
            LineBorder lineBorder = (LineBorder) getBorder();
            if (isSelected) {
                lineBorder.setWidth(SELECTION_LINE_WIDTH);
            } else {
                lineBorder.setWidth(NORMAL_LINE_WIDTH);
            }
        } else {
            if (isSelected) {
                setLineWidth(SELECTION_LINE_WIDTH);
            } else {
                setLineWidth(NORMAL_LINE_WIDTH);
            }
        }
        repaint();
    }

    public String getToolTipText() {
        return tooltip_label.getText();
    }

    public void setToolTipText(String txt) {
        tooltip_label.setText(txt);
    }
}
