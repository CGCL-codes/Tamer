package net.openchrom.chromatogram.msd.ui.perspective.views;

import net.openchrom.chromatogram.msd.model.core.support.IChromatogramSelection;
import net.openchrom.chromatogram.msd.model.notifier.ISelectionUpdateNotifier;
import net.openchrom.chromatogram.msd.model.notifier.IViewPartUpdater;
import net.openchrom.chromatogram.msd.ui.swt.components.chromatogram.SelectedExactIonChromatogramUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * @author eselmeister
 */
public class SelectedExactIonChromtogramView extends ViewPart implements ISelectionUpdateNotifier {

    public static final String ID = "net.openchrom.chromatogram.msd.ui.perspective.views.selectedExactIonChromtogramView";

    private SelectedExactIonChromatogramUI selectedExactIonChromatogramUI;

    private IViewPartUpdater viewPartUpdater;

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout());
        viewPartUpdater = new SelectedExactIonChromtogramView.SelectionUpdateListener();
        viewPartUpdater.setParent(this);
        setPartName("Selected Exact ion Chromatogram");
        selectedExactIonChromatogramUI = new SelectedExactIonChromatogramUI(parent, SWT.NONE);
    }

    @Override
    public void setFocus() {
        selectedExactIonChromatogramUI.setFocus();
    }

    @Override
    public void update(IChromatogramSelection chromatogramSelection, boolean forceReload) {
        if (getSite().getPage().isPartVisible(this) && chromatogramSelection != null) {
            selectedExactIonChromatogramUI.update(chromatogramSelection, forceReload);
        }
    }

    public static class SelectionUpdateListener implements ISelectionUpdateNotifier, IViewPartUpdater {

        private static SelectedExactIonChromtogramView parentWidget;

        private static IChromatogramSelection selection;

        @Override
        public void setParent(ViewPart parent) {
            if (parent instanceof SelectedExactIonChromtogramView) {
                parentWidget = (SelectedExactIonChromtogramView) parent;
            }
        }

        @Override
        public void update(IChromatogramSelection chromatogramSelection, boolean forceReload) {
            selection = chromatogramSelection;
            if (parentWidget != null) {
                parentWidget.update(chromatogramSelection, forceReload);
            }
        }

        @Override
        public IChromatogramSelection getChromatogramSelection() {
            return selection;
        }
    }
}
