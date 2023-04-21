package org.kalypso.nofdpidss.core.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.common.NofdpIDSSConstants;
import org.kalypso.nofdpidss.core.view.parts.IAbstractMenuPart;
import org.kalypso.nofdpidss.core.view.parts.IAbstractViewPart;

public class WindowManager {

    public enum WINDOW_TYPE {

        eNavigation, eMain, eAdditional
    }

    static final String[] PERPECTIVES = new String[] { NofdpIDSSConstants.NOFDP_PERSPECTIVE_CONFLICT_DETECTION, NofdpIDSSConstants.NOFDP_PERSPECTIVE_CROSS_SECTION_MANAGER, NofdpIDSSConstants.NOFDP_PERSPECTIVE_DEFAULT, NofdpIDSSConstants.NOFDP_PERSPECTIVE_FLOW_NETWORK, NofdpIDSSConstants.NOFDP_PERSPECTIVE_FLOOD_RISK, NofdpIDSSConstants.NOFDP_PERSPECTIVE_MEASURE_CONSTRUCTION, NofdpIDSSConstants.NOFDP_PERSPECTIVE_RATING, NofdpIDSSConstants.NOFDP_PERSPECTIVE_TIME_SERIES_MANAGER, NofdpIDSSConstants.NOFDP_PERSPECTIVE_VALUE_BENEFIT_ANALYSIS, NofdpIDSSConstants.NOFDP_PERSPECTIVE_VARIANT_MANAGER, NofdpIDSSConstants.NOFDP_PERSPECTIVE_INUNDATION_DURATION, NofdpIDSSConstants.NOFDP_PERSPECTIVE_NULL, NofdpIDSSConstants.NOFDP_PERSPECTIVE_HYDRAULIC_COMPUTATION };

    /**
   * key -> perspectiveId<br>
   * value -> list of views in this perspective
   */
    Map<String, List<String>> m_perspectiveViews = new HashMap<String, List<String>>();

    private final Map<WINDOW_TYPE, IAbstractViewPart> m_viewParts = new HashMap<WINDOW_TYPE, IAbstractViewPart>();

    private final NofdpPage m_page = new NofdpPage();

    private void checkPerspectives(final String perspective) {
        final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page == null) return;
        final IPerspectiveDescriptor[] perspectives = page.getOpenPerspectives();
        for (final IPerspectiveDescriptor descriptor : perspectives) {
            final String id = descriptor.getId();
            if (id.equals(perspective)) continue; else if (ArrayUtils.contains(WindowManager.PERPECTIVES, id)) if (descriptor != null) page.closePerspective(descriptor, true, false);
        }
    }

    public IAbstractViewPart getAbstractViewPart(final WINDOW_TYPE id) {
        return m_viewParts.get(id);
    }

    public IAbstractMenuPart getMenuPart() {
        return m_page.getMenuPart();
    }

    public void renameViewHeader(final WINDOW_TYPE viewId, final String headerText) {
        final IAbstractViewPart abstractViewPart = getAbstractViewPart(viewId);
        if (abstractViewPart != null) abstractViewPart.setWindowTitle(headerText);
    }

    public void setAbstractViewPart(final WINDOW_TYPE id, final IAbstractViewPart view) {
        final IAbstractViewPart part = m_viewParts.get(id);
        if (part != null && !part.equals(view)) part.removeWorkspaceListener();
        m_viewParts.put(id, view);
    }

    public void setMenuPart(final IAbstractMenuPart page) {
        m_page.setMenuPart(page);
    }

    /**
   * sets an perspective in given workbench, is this perspective active, perspective won't be set!
   */
    private void setPerspective(final String perspective) {
        final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        final IWorkbenchPage page = window.getActivePage();
        if (page == null) return;
        if (perspective.equals(page.getPerspective().getId())) return;
        final IPerspectiveDescriptor descriptor = page.getWorkbenchWindow().getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(perspective);
        if (descriptor != null) page.setPerspective(descriptor);
    }

    /**
   * open a list of eclipse views param perspective id of perspective
   * 
   * @param viewIds
   *            list of views which will be opened
   * @param optionalViewIds
   *            list of views which are allowed in this context - these views won't be closed
   */
    public Map<String, IViewReference> showView(final INofdpView view) {
        setPerspective(view.getPerspective());
        final List<String> myOptionalViews = new ArrayList<String>();
        for (final String v : view.getOptionalViewIds()) myOptionalViews.add(v);
        final Map<String, IViewReference> activeViews = new HashMap<String, IViewReference>();
        final List<String> viewsAlreadyOpened = new ArrayList<String>();
        final IWorkbenchWindow wWin = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        final IWorkbenchPage[] pages = wWin.getPages();
        for (final IWorkbenchPage page : pages) {
            final List<String> myViews = new ArrayList<String>();
            for (final String v : view.getViewIds()) myViews.add(v);
            final List<IViewReference> viewsToClose = new ArrayList<IViewReference>();
            final IViewReference[] viewReferences = page.getViewReferences();
            for (final IViewReference viewReference : viewReferences) {
                final String id = viewReference.getId();
                if (myViews.contains(id)) {
                    myViews.remove(id);
                    activeViews.put(id, viewReference);
                    viewsAlreadyOpened.add(id);
                } else if (myOptionalViews.contains(id)) {
                    activeViews.put(id, viewReference);
                    continue;
                } else viewsToClose.add(viewReference);
            }
            for (final String viewId : myViews) try {
                page.showView(viewId);
            } catch (final PartInitException e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            }
            for (final IViewReference viewId : viewsToClose) {
                final IViewPart viewPart = viewId.getView(false);
                if (viewPart instanceof IAbstractViewPart) {
                    final IAbstractViewPart vp = (IAbstractViewPart) viewPart;
                    vp.cleanUp();
                }
                page.hideView(viewId);
            }
            final IViewReference[] references = page.getViewReferences();
            for (final IViewReference viewReference : references) activeViews.put(viewReference.getId(), viewReference);
            for (final String viewId : view.getForceUpdateIds()) if (viewsAlreadyOpened.contains(viewId)) {
                final IViewReference reference = activeViews.get(viewId);
                final IViewPart viewPart = reference.getView(false);
                if (viewPart instanceof IAbstractViewPart) {
                    final IAbstractViewPart part = (IAbstractViewPart) viewPart;
                    part.redraw();
                }
            }
        }
        checkPerspectives(view.getPerspective());
        return activeViews;
    }
}
