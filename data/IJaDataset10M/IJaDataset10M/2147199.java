package fr.crnan.videso3d.formats.plns;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.CategoryLabelEntity;
import org.jfree.chart.plot.CategoryPlot;
import fr.crnan.videso3d.DatabaseManager.Type;
import fr.crnan.videso3d.ihm.ContextPanel;
import fr.crnan.videso3d.stpv.StpvController;

/**
 * 
 * @author Bruno Spyckerelle
 * @version 0.1.0
 */
public class PLNSChartMouseListener implements ChartMouseListener {

    private ContextPanel context;

    public PLNSChartMouseListener(ContextPanel context) {
        this.context = context;
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent evt) {
        if (evt.getEntity() instanceof CategoryItemEntity) {
            CategoryItemEntity entity = (CategoryItemEntity) evt.getEntity();
            if (entity.getColumnKey() instanceof String && ((String) entity.getColumnKey()).matches("C.")) {
                this.context.showInfo(Type.STPV, StpvController.CATEGORIE_CODE, (String) entity.getColumnKey());
            } else if (entity.getColumnKey() instanceof Integer && evt.getChart().getPlot() instanceof CategoryPlot && ((CategoryPlot) evt.getChart().getPlot()).getDomainAxis().getLabel().equals("LP")) {
                this.context.showInfo(Type.STPV, StpvController.LIAISON_PRIVILEGIEE, ((Integer) entity.getColumnKey()).toString());
            }
        } else if (evt.getEntity() instanceof CategoryLabelEntity) {
            CategoryLabelEntity entity = (CategoryLabelEntity) evt.getEntity();
            if (entity.getKey() instanceof String && ((String) entity.getKey()).matches("C.")) {
                this.context.showInfo(Type.STPV, StpvController.CATEGORIE_CODE, (String) entity.getKey());
            } else if (entity.getKey() instanceof Integer && evt.getChart().getPlot() instanceof CategoryPlot && ((CategoryPlot) evt.getChart().getPlot()).getDomainAxis().getLabel().equals("LP")) {
                this.context.showInfo(Type.STPV, StpvController.LIAISON_PRIVILEGIEE, ((Integer) entity.getKey()).toString());
            }
        }
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent evt) {
    }
}
