package net.openchrom.chromatogram.msd.filter.supplier.denoising.ui.modifier;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import net.openchrom.chromatogram.msd.filter.core.ChromatogramFilter;
import net.openchrom.chromatogram.msd.filter.exceptions.ChromatogramSelectionException;
import net.openchrom.chromatogram.msd.filter.exceptions.FilterSettingsException;
import net.openchrom.chromatogram.msd.filter.exceptions.NoFilterAvailableException;
import net.openchrom.chromatogram.msd.filter.result.IChromatogramFilterResult;
import net.openchrom.chromatogram.msd.filter.supplier.denoising.preferences.FilterPreferences;
import net.openchrom.chromatogram.msd.filter.supplier.denoising.result.IDenoisingFilterResult;
import net.openchrom.chromatogram.msd.filter.supplier.denoising.settings.DenoisingFilterSettings;
import net.openchrom.chromatogram.msd.filter.supplier.denoising.settings.IDenoisingFilterSettings;
import net.openchrom.chromatogram.msd.filter.supplier.denoising.ui.internal.preferences.PreferenceSupplier;
import net.openchrom.chromatogram.msd.filter.supplier.denoising.ui.views.InteractiveDenoisingFilterMassSpectrumView;
import net.openchrom.chromatogram.msd.filter.supplier.denoising.ui.views.InteractiveDenoisingFilterMassSpectrumView.SelectionUpdateListener;
import net.openchrom.chromatogram.msd.model.core.modifier.AbstractChromatogramModifier;
import net.openchrom.chromatogram.msd.model.core.support.IChromatogramSelection;
import net.openchrom.chromatogram.msd.model.core.support.IMarkedIons;
import net.openchrom.logging.core.Logger;

public class FilterModifier extends AbstractChromatogramModifier implements IRunnableWithProgress {

    private static final Logger logger = Logger.getLogger(FilterModifier.class);

    private static final String description = "Denoising Filter";

    private static final String FILTER_ID = "net.openchrom.chromatogram.msd.filter.supplier.denoising";

    public FilterModifier(IChromatogramSelection chromatogramSelection) {
        super(chromatogramSelection);
    }

    @Override
    public void execute(IProgressMonitor monitor) {
        IChromatogramSelection chromatogramSelection = getChromatogramSelection();
        IDenoisingFilterSettings chromatogramFilterSettings = new DenoisingFilterSettings();
        IMarkedIons ionsToRemove = chromatogramFilterSettings.getIonsToRemove();
        if (PreferenceSupplier.useChromatogramSpecificIons()) {
            FilterPreferences.setMarkedIons(ionsToRemove, chromatogramSelection.getSelectedIons().getIonsNominal());
        } else {
            FilterPreferences.setMarkedIons(ionsToRemove, PreferenceSupplier.getIonsToRemove());
        }
        IMarkedIons ionsToPreserve = chromatogramFilterSettings.getIonsToPreserve();
        if (PreferenceSupplier.useChromatogramSpecificIons()) {
            FilterPreferences.setMarkedIons(ionsToPreserve, chromatogramSelection.getExcludedIons().getIonsNominal());
        } else {
            FilterPreferences.setMarkedIons(ionsToPreserve, PreferenceSupplier.getIonsToPreserve());
        }
        chromatogramFilterSettings.setAdjustThresholdTransitions(PreferenceSupplier.adjustThresholdTransitions());
        chromatogramFilterSettings.setNumberOfUsedIonsForCoefficient(1);
        chromatogramFilterSettings.setSegmentWidth(PreferenceSupplier.getSegmentWidth());
        try {
            IChromatogramFilterResult result = ChromatogramFilter.applyFilter(chromatogramSelection, chromatogramFilterSettings, FILTER_ID, monitor);
            if (result instanceof IDenoisingFilterResult) {
                final IDenoisingFilterResult denoisingResult = (IDenoisingFilterResult) result;
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        SelectionUpdateListener listener = new InteractiveDenoisingFilterMassSpectrumView.SelectionUpdateListener();
                        listener.update(denoisingResult.getNoiseMassSpectra(), true);
                    }
                });
            }
        } catch (NoFilterAvailableException e) {
            logger.warn(e);
        } catch (ChromatogramSelectionException e) {
            logger.warn(e);
        } catch (FilterSettingsException e) {
            logger.warn(e);
        }
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {
            monitor.beginTask("Denoising Filter", IProgressMonitor.UNKNOWN);
            getChromatogramSelection().getChromatogram().doOperation(this, monitor);
        } finally {
            monitor.done();
        }
    }
}
