package org.kalypso.nofdpidss.measure.construction.create.builders.drawing;

import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureExcavationWorks;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolProject;
import org.kalypso.nofdpidss.measure.construction.base.MyEvent;
import org.kalypso.nofdpidss.measure.construction.create.builders.IMeasureBuilder;
import org.kalypso.nofdpidss.measure.construction.create.builders.geometry.MyPolygonGeometryBuilder;
import org.kalypso.nofdpidss.measure.construction.create.widgets.MethodWidgetHelper;
import org.kalypso.nofdpidss.measure.construction.create.widgets.MyDoubleClickWidget;
import org.kalypso.nofdpidss.measure.construction.i18n.Messages;
import org.kalypso.ogc.gml.FeatureUtils;
import org.kalypso.ogc.gml.map.IMapPanel;
import org.kalypso.ogc.gml.map.widgets.builders.IGeometryBuilder;
import org.kalypso.ogc.gml.widgets.IWidgetManager;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.geometry.GM_Object;

/**
 * @author Dirk Kuch
 */
public class DrawHandlerExcavationWorks extends AbstractDrawHandler implements IDrawHandler {

    public DrawHandlerExcavationWorks(final IMeasureBuilder builder, final IMapPanel mapPanel, final PoolProject pool, final Feature feature, final Runnable doneListener) {
        super(builder, mapPanel, pool, feature, doneListener);
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.drawing.IDrawHandler#getGeometryBuilder()
   */
    public IGeometryBuilder getGeometryBuilder() {
        final String targetCrs = getMapPanel().getMapModell().getCoordinatesSystem();
        final Object geometry = getFeature().getProperty(IMeasureExcavationWorks.QN_GEOMETRY);
        getFeature().setProperty(IMeasureExcavationWorks.QN_GEOMETRY, null);
        return new MyPolygonGeometryBuilder(targetCrs, this, geometry);
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.drawing.ITooltipProvider#getTooltip()
   */
    public String[] getTooltip() {
        if (getLastEvent() == null) return new String[] {}; else if (MyEvent.Init.equals(getLastEvent().event)) return new String[] { Messages.DrawHandlerExcavationWorks_1, Messages.DrawHandlerExcavationWorks_2 }; else if (MyEvent.Left == (getLastEvent().event & MyEvent.Left)) return new String[] { Messages.DrawHandlerExcavationWorks_3, Messages.DrawHandlerExcavationWorks_4 };
        return new String[] {};
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.drawing.IDrawHandler#process()
   */
    public void process() {
        final MyDoubleClickWidget widget = new MyDoubleClickWidget(this);
        widget.init();
        final IWidgetManager widgetManager = getMapPanel().getWidgetManager();
        widgetManager.setActualWidget(widget);
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.drawing.IDrawHandler#resetGeometry()
   */
    public void resetGeometry() {
        MethodWidgetHelper.resetGeometry(getFeature(), IMeasureExcavationWorks.QN_GEOMETRY);
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.drawing.IDrawHandler#setGeometry(org.kalypsodeegree.model.geometry.GM_Object)
   */
    public boolean setGeometry(final GM_Object gmo) throws Exception {
        FeatureUtils.updateProperty(getPool().getWorkspace(), getFeature(), IMeasureExcavationWorks.QN_GEOMETRY, gmo);
        getFeature().invalidEnvelope();
        return true;
    }
}
