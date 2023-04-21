package org.kalypso.nofdpidss.ui.view.common.navigation;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.MyColors;
import org.kalypso.nofdpidss.ui.NofdpUiPlugin;

/**
 * @author Dirk Kuch
 */
public class NavigationSite {

    private Composite m_parent = null;

    protected Composite m_sashForm = null;

    protected int[] m_arrWeights = null;

    public void draw(final Composite parent) throws CoreException {
        m_parent = parent;
        if (parent.isDisposed()) return;
        final FormToolkit toolkit = NofdpUiPlugin.getToolkit();
        toolkit.adapt(m_parent);
        final FillLayout fillLayout = new FillLayout();
        fillLayout.marginWidth = 0;
        if (NofdpCorePlugin.getProjectManager().getActiveProject() == null) {
            m_sashForm = toolkit.createComposite(m_parent);
            m_sashForm.setLayout(fillLayout);
            m_sashForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
            NavigationPane.drawPane(m_sashForm);
        } else {
            m_sashForm = new SashForm(m_parent, SWT.VERTICAL);
            m_sashForm.setLayout(fillLayout);
            m_sashForm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
            toolkit.adapt(m_sashForm);
            final Composite compNav = toolkit.createComposite(m_sashForm);
            compNav.setLayout(fillLayout);
            NavigationPane.drawPane(compNav);
            final Composite compNavMenu = toolkit.createComposite(m_sashForm);
            compNavMenu.setLayout(fillLayout);
            compNavMenu.layout();
            FabricationNavMenu.getNavMenuBuilder(compNavMenu);
            if (m_parent.isDisposed()) return;
            if (m_arrWeights == null || m_arrWeights.length != 2) {
                ((SashForm) m_sashForm).setWeights(new int[] { 45, 55 });
                ((SashForm) m_sashForm).setBackground(MyColors.cSashForm);
            } else {
                ((SashForm) m_sashForm).setWeights(m_arrWeights);
                ((SashForm) m_sashForm).setBackground(MyColors.cSashForm);
            }
            compNavMenu.addControlListener(new ControlListener() {

                public void controlMoved(final ControlEvent arg0) {
                }

                public void controlResized(final ControlEvent arg0) {
                    if (m_sashForm instanceof SashForm) {
                        final SashForm mySash = (SashForm) m_sashForm;
                        m_arrWeights = mySash.getWeights();
                    }
                }
            });
        }
    }

    public void resetSashWeights() {
        m_arrWeights = null;
    }
}
