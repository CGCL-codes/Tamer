package de.fraunhofer.isst.axbench.views.metrics;

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import de.fraunhofer.isst.axbench.operations.checker.solver.utilities.AxlVariabilitySolverResultData;

/**
 * @brief View label provider for the "AxlVarSolverSpassConsistency"
 *
 * @author smann
 * @version 0.9.0
 * @since 0.9.0
 */
public class AxlVarSolverSpassConsistency_ViewLabelProvider extends AxlVarSolverAbstract_ViewLabelProvider {

    /**
     * @brief Specifics; the rest is done by the superclass
     * @param editor
     */
    public AxlVarSolverSpassConsistency_ViewLabelProvider(AxlVarSolverAbstract_View theView) {
        super(theView);
    }

    /**
     * @brief Specifics; the rest is done by the superclass
     */
    @Override
    public String getColumnText(Object element, int column) {
        if (element instanceof AxlVariabilitySolverResultData) {
            AxlVariabilitySolverResultData axlMetricsData = (AxlVariabilitySolverResultData) element;
            switch(column) {
                case 0:
                    return axlMetricsData.getAxlElement().getIdentifier();
                case 1:
                    return String.valueOf(axlMetricsData.isConsistent());
                case 2:
                    return null;
                case 3:
                    return null;
                case 4:
                    return String.valueOf(axlMetricsData.getSolverInformation());
                case 5:
                    return axlMetricsData.getInformation();
                case 6:
                    return null;
                case 7:
                    return null;
                case 8:
                    return null;
                case 9:
                    return axlMetricsData.getSpassAnswer().toString();
                default:
                    break;
            }
        }
        return super.getColumnText(element, column);
    }

    /**
     * @brief Specifics; the rest is done by the superclass
     */
    @Override
    public Image getImage(Object oElement) {
        return super.getImage(oElement);
    }

    /**
     * @brief Specifics; the rest is done by the superclass
     */
    @Override
    public String getToolTipText(Object element) {
        return super.getToolTipText(element);
    }

    /**
     * @brief Set specific layout (colors, fonts) for specific rows in the view(non-Javadoc)
     * @see de.fraunhofer.isst.axbench.views.metrics.AxlVarSolverAbstract_ViewLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
     */
    @Override
    public void update(ViewerCell cell) {
        super.update(cell);
    }
}
