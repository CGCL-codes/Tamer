package pulsarhunter.datatypes;

import java.util.Comparator;
import pulsarhunter.datatypes.BasicSearchResult;
import pulsarhunter.datatypes.PeriodSearchResultGroup.SortField;

public class SearchResultComparator<R extends BasicSearchResult> implements Comparator<R> {

    private SortField sortField;

    public SearchResultComparator(SortField sortField) {
        this.sortField = sortField;
    }

    public int compare(R o1, R o2) {
        int rv;
        switch(sortField) {
            case PERIOD:
                return (int) (1000000 * (o1.getPeriod() - o2.getPeriod()));
            case DM:
                return (int) (1000000 * (o1.getDM() - o2.getDM()));
            case SPECTRAL_SNR:
                rv = (int) (1000000 * (o1.getSpectralSignalToNoise() - o2.getSpectralSignalToNoise()));
                if (rv == 0) {
                    return (int) (1000000 * (o1.getReconstructedSignalToNoise() - o2.getReconstructedSignalToNoise()));
                } else return rv;
            case RECONSTRUCTED_SNR:
                rv = (int) (1000000 * (o1.getReconstructedSignalToNoise() - o2.getReconstructedSignalToNoise()));
                if (rv == 0) {
                    return (int) (1000000 * (o1.getSpectralSignalToNoise() - o2.getSpectralSignalToNoise()));
                } else return rv;
            case FOLD_SNR:
                return (int) (1000000 * (o1.getFoldSignalToNoise() - o2.getFoldSignalToNoise()));
            case PDOT:
                return (int) (1000000 * (o1.getAccn() - o2.getAccn()));
            case PDDOT:
                return (int) (1000000 * (o1.getJerk() - o2.getJerk()));
            case DM_PDOT_PDDOT:
                if (o1.getDM() == o2.getDM()) {
                    if (o1.getAccn() == o2.getAccn()) {
                        if (o1.getJerk() == o2.getJerk()) {
                            return 0;
                        } else return (int) (1000000 * (o1.getJerk() - o2.getJerk()));
                    } else return (int) (1000000 * (o1.getAccn() - o2.getAccn()));
                } else return (int) (1000000 * (o1.getDM() - o2.getDM()));
            default:
                return 0;
        }
    }
}
