package net.openchrom.chromatogram.msd.ui.perspective.views;

import net.openchrom.chromatogram.msd.model.core.support.IChromatogramSelection;
import net.openchrom.chromatogram.msd.model.notifier.ISelectionUpdateNotifier;
import net.openchrom.chromatogram.msd.model.notifier.IViewPartUpdater;
import net.openchrom.chromatogram.msd.ui.swt.components.peak.ExtendedPeakUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class PeakView extends ViewPart implements ISelectionUpdateNotifier {

    public static final String ID = "net.openchrom.chromatogram.msd.ui.perspective.views.peakView";

    private IViewPartUpdater viewPartUpdater;

    private ExtendedPeakUI extendedPeakUI;

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout());
        viewPartUpdater = new PeakView.SelectionUpdateListener();
        viewPartUpdater.setParent(this);
        setPartName("Peak");
        extendedPeakUI = new ExtendedPeakUI(parent, SWT.NONE);
        extendedPeakUI.setIncludeBackground(true);
    }

    @Override
    public void setFocus() {
        extendedPeakUI.setFocus();
    }

    @Override
    public void update(IChromatogramSelection chromatogramSelection, boolean forceReload) {
        if (getSite().getPage().isPartVisible(this) && chromatogramSelection != null && chromatogramSelection.getSelectedPeak() != null) {
            extendedPeakUI.update(chromatogramSelection, forceReload);
        }
    }

    public static class SelectionUpdateListener implements ISelectionUpdateNotifier, IViewPartUpdater {

        private static PeakView parentWidget;

        private static IChromatogramSelection selection;

        @Override
        public void setParent(ViewPart parent) {
            if (parent instanceof PeakView) {
                parentWidget = (PeakView) parent;
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
