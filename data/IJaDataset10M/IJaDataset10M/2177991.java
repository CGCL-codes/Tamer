package org.kalypso.nofdpidss.evaluation.ranking.view;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.IModelEventDelegate;
import org.kalypso.nofdpidss.core.base.MyJob;
import org.kalypso.nofdpidss.core.base.MyModelEventListener;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.core.view.WindowManager.WINDOW_TYPE;
import org.kalypso.nofdpidss.core.view.parts.IAbstractMenuPart;
import org.kalypso.nofdpidss.core.view.parts.IAbstractViewPart;
import org.kalypso.nofdpidss.evaluation.i18n.Messages;
import org.kalypso.nofdpidss.evaluation.ranking.view.ui.RanVariantSectionBuilder;
import org.kalypsodeegree.model.feature.event.FeatureStructureChangeModellEvent;
import org.kalypsodeegree.model.feature.event.ModellEvent;

/**
 * @author Dirk Kuch
 */
public class RankingEditPage {

    private final Composite m_parent;

    private final IAbstractMenuPart m_part;

    public RankingEditPage(final IAbstractMenuPart part, final Composite parent) {
        m_part = part;
        m_parent = parent;
        final IAbstractViewPart viewPart = NofdpCorePlugin.getWindowManager().getAbstractViewPart(WINDOW_TYPE.eMain);
        final MyBasePool pool = NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eProject);
        final IModelEventDelegate delegate = new IModelEventDelegate() {

            public MyJob handleModelEvent(final ModellEvent modellEvent) {
                if (modellEvent instanceof FeatureStructureChangeModellEvent) return new MyJob(Messages.RanEditPage_0) {

                    @Override
                    public IStatus runInUIThread(IProgressMonitor monitor) {
                        viewPart.redraw();
                        return Status.OK_STATUS;
                    }
                };
                return null;
            }
        };
        viewPart.addWorkspaceListener(pool.getWorkspace(), new MyModelEventListener(delegate));
    }

    public void generateBody() {
        if (m_parent.isDisposed()) return;
        final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
        new RanVariantSectionBuilder(m_part, toolkit, m_parent);
        m_parent.layout();
    }
}
