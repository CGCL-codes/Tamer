package org.vikamine.gui.subgroup.visualization.circles;

import java.awt.Color;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import org.vikamine.app.VIKAMINE;
import org.vikamine.gui.subgroup.visualization.SGComponent;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.util.XMLUtils;

/**
 * @author Tobias Vogele
 */
public class CircleSGComponent extends SGComponent {

    /**
     * 
     */
    private static final long serialVersionUID = 2766781211121638075L;

    private CircleVisualizationComponent circle;

    public CircleSGComponent() {
        super();
    }

    public CircleSGComponent(SG sg) {
        this();
        getCircle().setSubgroup(sg);
        update();
    }

    public CircleSGComponent(double positives, double populationTargetShare) {
        this();
        getCircle().setPositives(positives, populationTargetShare);
        update();
    }

    @Override
    public SG getSubgroup() {
        return getCircle().getSubgroup();
    }

    @Override
    protected JComponent getVisualisationComponent() {
        return getCircle();
    }

    @Override
    protected boolean isSelected() {
        return getCircle().isSelected();
    }

    public CircleVisualizationComponent getCircle() {
        if (circle == null) {
            circle = new CircleVisualizationComponent();
        }
        return circle;
    }

    @Override
    protected LayoutManager createLayoutManager() {
        return new CircleSGLayout();
    }

    @Override
    protected String verbalizeSG() {
        if (getSubgroup() == null) {
            return XMLUtils.convertToHTMLCompliantText(VIKAMINE.I18N.getString("vikamine.visualisation.allInstances"));
        }
        return super.verbalizeSG();
    }

    @Override
    public void update() {
        boolean select = isSelected();
        Color c = select ? selectionColor : noselectionColor;
        getLabel().setBackground(c);
        getLabel().setText(verbalizeSG());
        repaint();
    }
}
