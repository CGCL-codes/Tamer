package org.kalypso.nofdpidss.evaluation.cost.widgets;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IAssessmentTemplate;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ICostCriterionDefinition;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ICriterionDefinition;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IVariant;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolProject;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.core.common.utils.gml.ATGmlUtils;
import org.kalypso.nofdpidss.core.common.utils.gml.VMGmlUtils;
import org.kalypso.nofdpidss.core.view.parts.IAbstractMenuPart;
import org.kalypso.nofdpidss.core.view.widgets.MySection;
import org.kalypso.nofdpidss.evaluation.i18n.Messages;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class CETableBuilder {

    private static final String SECTION_COST_EFFECTIVENESS = "sCostEffectiveness";

    private final IAbstractMenuPart m_part;

    private final FormToolkit m_toolkit;

    private final Composite m_parent;

    private Composite m_body;

    public CETableBuilder(final IAbstractMenuPart part, final FormToolkit toolkit, final Composite parent) {
        m_part = part;
        m_toolkit = toolkit;
        m_parent = parent;
    }

    public void draw() {
        final PoolProject pool = (PoolProject) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eProject);
        final IAssessmentTemplate lastAT = ATGmlUtils.getLastUsedAssessmentTemplate(pool);
        final ICriterionDefinition[] definitions = ATGmlUtils.getInATUsedCriterionDef(lastAT);
        if (definitions == null) return;
        if (m_body != null) {
            if (!m_body.isDisposed()) m_body.dispose();
            m_body = null;
            m_parent.redraw();
            m_parent.layout();
        }
        m_body = m_toolkit.createComposite(m_parent);
        m_body.setLayout(new GridLayout());
        m_body.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        int costCount = 0;
        for (final ICriterionDefinition definition : definitions) {
            if (definition instanceof ICostCriterionDefinition) {
                getCriterionCard(definition);
                costCount++;
            }
        }
        if (costCount == 0) drawEmptyPage(m_parent);
    }

    private void drawEmptyPage(final Composite sectionBody) {
        m_toolkit.createLabel(sectionBody, Messages.CETableBuilder_1);
    }

    private void getCriterionCard(final Feature fCrDef) {
        final String sName = ATGmlUtils.getFeatureNameAssessment(fCrDef);
        final MySection section = new MySection(m_part, m_toolkit, m_body, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE, true);
        final Composite myBody = section.setup(Messages.CETableBuilder_2 + " " + sName, new GridData(GridData.FILL, GridData.FILL, true, false), new GridData(GridData.FILL, GridData.FILL, true, true), CETableBuilder.SECTION_COST_EFFECTIVENESS + fCrDef.getId());
        myBody.setLayout(new GridLayout());
        final PoolProject pool = (PoolProject) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eProject);
        final IVariant[] lstVariants = VMGmlUtils.getListOfVariants(pool);
        new CEVariantTableBuilder(fCrDef, lstVariants, m_toolkit, myBody);
        section.getSection().layout();
        myBody.layout();
        m_body.layout();
    }
}
