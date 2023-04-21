package org.kalypso.nofdpidss.geodata.wizard.map.addlayer.builder;

import org.eclipse.core.runtime.CoreException;
import org.kalypso.contribs.eclipse.jface.operation.ICoreRunnableWithProgress;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolProject;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.geodata.wizard.map.addlayer.AddMapLayerData;
import org.kalypso.nofdpidss.geodata.wizard.map.addlayer.WizardAddMapLayer;
import org.kalypso.nofdpidss.geodata.wizard.map.addlayer.pages.PageAMLVariantMeasure;

/**
 * @author Dirk Kuch
 */
public class AddLayerBuilderVariant extends AddLayerBuilder {

    private boolean m_added = false;

    public AddLayerBuilderVariant(final WizardAddMapLayer wizard, final AddMapLayerData data) {
        super(wizard, data);
    }

    /**
   * @throws CoreException
   * @see org.kalypso.nofdpidss.ui.view.wizard.map.addlayer.IAddLayerBuilder#addPages()
   */
    public void addPages() throws CoreException {
        if (!m_added) {
            final PoolProject pool = (PoolProject) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eProject);
            getWizard().addPage(new PageAMLVariantMeasure(pool, getData()));
        }
        m_added = true;
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.map.addlayer.IAddLayerBuilder#getPerformFinishWorker()
   */
    public ICoreRunnableWithProgress getPerformFinishWorker() {
        return new AMLFinishWorkerVariant(getData());
    }
}
