package org.kalypso.nofdpidss.measure.construction.create.builders.measures;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.eclipse.jface.wizard.IWizardPage;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.core.KalypsoCorePlugin;
import org.kalypso.gmlschema.IGMLSchema;
import org.kalypso.gmlschema.feature.IFeatureType;
import org.kalypso.gmlschema.property.IPropertyType;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.commands.AtomarAddFeatureCommand;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.implementation.MeasureForestDevelopmentHandler;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolProject;
import org.kalypso.nofdpidss.core.base.measures.MCUtils.MEASURE;
import org.kalypso.nofdpidss.core.common.utils.gml.McGmlUtils;
import org.kalypso.nofdpidss.measure.construction.create.MeasureBuilder;
import org.kalypso.nofdpidss.measure.construction.create.builders.drawing.DrawHandlerForestDevelopment;
import org.kalypso.nofdpidss.measure.construction.create.builders.drawing.IDrawHandler;
import org.kalypso.nofdpidss.measure.construction.create.interfaces.IMeasureConstructionCreationHandler;
import org.kalypso.ogc.gml.selection.IFeatureSelectionManager;
import org.kalypsodeegree.model.feature.FeatureList;

/**
 * @author Dirk Kuch
 */
public class MCBuilderForestDevelopment extends MeasureBuilder {

    public MCBuilderForestDevelopment(final PoolProject pool, final IMeasureConstructionCreationHandler handler) {
        super(pool, handler);
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.MeasureBuilder#addDrawHandlers()
   */
    @Override
    public void addDrawHandlers() {
        final IDrawHandler dh = new DrawHandlerForestDevelopment(this, getHandler().getMapPanel(), getPool(), getMeasure(), getDoneListener());
        addDrawHandler(dh);
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.IMeasureBuilder#getNextPageAction(org.eclipse.jface.wizard.IWizardPage)
   */
    public void getNextPageAction(final IWizardPage nextPage) {
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.IMeasureBuilder#getPages()
   */
    public void getPages() {
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.IMeasureBuilder#getSelectedMeasure()
   */
    public MEASURE getSelectedMeasure() {
        return MEASURE.eAraForestDevelopment;
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.MeasureBuilder#mkeFeature()
   */
    @Override
    public void mkeFeature() {
        final IGMLSchema schema = getPool().getWorkspace().getGMLSchema();
        final QName qType = getSelectedMeasure().getFeatureType();
        final IFeatureType ft = schema.getFeatureType(qType);
        final IFeatureType substitutionGroupFT = ft.getSubstitutionGroupFT();
        final FeatureList parent = McGmlUtils.findParentFeature(getPool(), substitutionGroupFT);
        final IFeatureSelectionManager selectionManager = KalypsoCorePlugin.getDefault().getSelectionManager();
        final Map<IPropertyType, Object> properties = new HashMap<IPropertyType, Object>();
        final IPropertyType dt = ft.getProperty(GmlConstants.QN_MEASURE_MEASURE_DIGI_TOOL);
        properties.put(dt, "polygone");
        final AtomarAddFeatureCommand cmd = new AtomarAddFeatureCommand(getPool().getWorkspace(), ft, parent.getParentFeature(), parent.getParentFeatureTypeProperty(), -1, properties, selectionManager);
        try {
            getPool().getWorkspace().postCommand(cmd);
        } catch (final Exception e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            throw new IllegalStateException(e.getMessage());
        }
        final MeasureForestDevelopmentHandler measure = new MeasureForestDevelopmentHandler(getPool().getModel(), cmd.getNewFeature());
        setMeasure(measure);
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.MeasureBuilder#postProcessing()
   */
    @Override
    public boolean postProcessing() {
        return true;
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.create.builders.MeasureBuilder#preInit()
   */
    @Override
    public void preInit() {
    }
}
