package net.sf.mzmine.modules.peaklistmethods.alignment.path.functions;

import java.util.Comparator;
import net.sf.mzmine.data.PeakListRow;

public class AlignmentSorterFactory {

    public static enum SORT_MODE {

        name {

            public String toString() {
                return "name";
            }
        }
        , peaks {

            public String toString() {
                return "number of peaks";
            }
        }
        , rt {

            public String toString() {
                return "RT";
            }
        }
        , none {

            public String toString() {
                return "nothing";
            }
        }
        ;

        public abstract String toString();
    }

    public static Comparator<PeakListRow> getComparator(final SORT_MODE mode) {
        return getComparator(mode, true);
    }

    /**
         * Return a comparator that <b>is</b> inconsistent with equals.
         * @param mode
         * @param ascending
         * @return
         */
    public static Comparator<PeakListRow> getComparator(final SORT_MODE mode, final boolean ascending) {
        switch(mode) {
            case name:
                return getNameComparator(ascending);
            case peaks:
                return getPeakCountComparator(ascending);
            case rt:
                return getDoubleValComparator(ascending, mode);
            default:
                return nullComparator();
        }
    }

    private static Comparator<PeakListRow> getNameComparator(final boolean ascending) {
        return new Comparator<PeakListRow>() {

            public int compare(PeakListRow o1, PeakListRow o2) {
                int comparison = 0;
                comparison = o1.getPreferredPeakIdentity().getName().compareToIgnoreCase(o2.getPreferredPeakIdentity().getName());
                return ascending ? comparison : -comparison;
            }
        };
    }

    private static Comparator<PeakListRow> getPeakCountComparator(final boolean ascending) {
        return new Comparator<PeakListRow>() {

            public int compare(PeakListRow o1, PeakListRow o2) {
                int comp = (Integer) o1.getNumberOfPeaks() - (Integer) o2.getNumberOfPeaks();
                return ascending ? comp : -comp;
            }
        };
    }

    private static Comparator<PeakListRow> getDoubleValComparator(final boolean ascending, final SORT_MODE mode) {
        return new Comparator<PeakListRow>() {

            public int compare(PeakListRow o1, PeakListRow o2) {
                int comparison = 0;
                double val1 = 0.0;
                double val2 = 0.0;
                switch(mode) {
                    case rt:
                        val1 = (Double) o1.getAverageRT();
                        break;
                }
                if (val1 < val2) {
                    comparison = -1;
                }
                if (val1 > val2) {
                    comparison = 1;
                }
                return ascending ? comparison : -comparison;
            }
        };
    }

    private static Comparator<PeakListRow> nullComparator() {
        return new Comparator<PeakListRow>() {

            public int compare(PeakListRow o1, PeakListRow o2) {
                return 0;
            }
        };
    }
}
