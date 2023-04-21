package com.ma_la.myRunning.comparator;

import com.ma_la.myRunning.dto.TrainingAnalysisDto;
import java.util.*;

/**
 *
 * @author <a href="mailto:mail@myrunning.de">Martin Lang</a>
 */
public class TrainingAnalysisDtoComparator extends AbstractComparator {

    /**
	 * Mode for comparing <code>TrainingAnalysisDto</code> objects by periode.
	 */
    public static final String PERIOD = "period";

    /**
	 * Mode for comparing <code>TrainingAnalysisDto</code> objects by duration.
	 */
    public static final String DURATION = "duration";

    /**
	 * Mode for comparing <code>TrainingAnalysisDto</code> objects by quantity.
	 */
    public static final String QUANTITY = "quantity";

    /**
	 * Mode for comparing <code>TrainingAnalysisDto</code> objects by kilometres.
	 */
    public static final String KILOMETRES = "kilometres";

    /**
	 * Mode for comparing <code>TrainingAnalysisDto</code> objects by average speed.
	 */
    public static final String AVERAGE_SPEED = "averageSpeed";

    /**
	 * Creates a new instance of <code>TrainingAnalysisDtoComparator</code> that standard
	 * compares <code>TrainingAnalysisDto</code> objects by its <code>LAST_NAME</code>.
	 */
    public TrainingAnalysisDtoComparator() {
        this(PERIOD);
    }

    /**
	 * Creates a new instance of <code>TrainingAnalysisDtoComparator</code> that compares
	 * <code>TrainingAnalysisDto</code> objects by the given property.
	 *
	 * @param	property the property by which two <code>TrainingAnalysisDto</code>
	 * objects should be compared.
	 */
    public TrainingAnalysisDtoComparator(final String property) {
        setCompareProperties(new String[] { PERIOD, DURATION, QUANTITY, KILOMETRES, AVERAGE_SPEED });
        setCompareProperty(property);
        setSortingDirection(SORT_ASCENDING);
    }

    /**
	 * @see	java.util.Comparator#compare(Object,Object)
	 */
    public int compare(final Object o1, final Object o2) {
        final TrainingAnalysisDto trainingAnalysisDto1, trainingAnalysisDto2;
        if (o1 instanceof TrainingAnalysisDto) {
            trainingAnalysisDto1 = (TrainingAnalysisDto) o1;
        } else {
            throw new ClassCastException("ClassCastException: o1 is not an instance of " + TrainingAnalysisDto.class + ", its an instance of " + o1.getClass());
        }
        if (o2 instanceof TrainingAnalysisDto) {
            trainingAnalysisDto2 = (TrainingAnalysisDto) o2;
        } else {
            throw new ClassCastException("ClassCastException: o2 is not an instance of " + TrainingAnalysisDto.class + ", its an instance of " + o2.getClass());
        }
        String ser1 = null;
        String ser2 = null;
        if (PERIOD.equals(getCompareProperty())) {
            Long l1 = trainingAnalysisDto1.getPeriodSort();
            Long l2 = trainingAnalysisDto2.getPeriodSort();
            return super.compare(l1, l2);
        } else if (DURATION.equals(getCompareProperty())) {
            Double d1 = trainingAnalysisDto1.getDuration();
            Double d2 = trainingAnalysisDto2.getDuration();
            return super.compare(d1, d2);
        } else if (QUANTITY.equals(getCompareProperty())) {
            Long l1 = trainingAnalysisDto1.getQuantity();
            Long l2 = trainingAnalysisDto2.getQuantity();
            return super.compare(l1, l2);
        } else if (KILOMETRES.equals(getCompareProperty())) {
            Double d1 = trainingAnalysisDto1.getKilometres();
            Double d2 = trainingAnalysisDto2.getKilometres();
            return super.compare(d1, d2);
        } else if (AVERAGE_SPEED.equals(getCompareProperty())) {
            Double d1 = trainingAnalysisDto1.getAverageSpeed();
            Double d2 = trainingAnalysisDto2.getAverageSpeed();
            return super.compare(d1, d2);
        }
        return super.compare(ser1, ser2);
    }
}
