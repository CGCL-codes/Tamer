package org.jimcat.gui.histogram.image;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.ObjectUtils;
import org.jimcat.gui.ImageControl;
import org.jimcat.gui.ViewControl;
import org.jimcat.gui.ViewFilterListener;
import org.jimcat.gui.histogram.HistogramModel;
import org.jimcat.gui.histogram.HistogramModelEvent;
import org.jimcat.gui.histogram.HistogramModelPath;
import org.jimcat.gui.histogram.HistogramModel.ScaleMark;
import org.jimcat.model.Image;
import org.jimcat.model.filter.metadata.PictureTakenFilter;
import org.jimcat.model.filter.metadata.PictureTakenFilter.Type;
import org.jimcat.model.libraries.ImageLibrary;
import org.jimcat.model.notification.BeanChangeEvent;
import org.jimcat.model.notification.BeanProperty;
import org.jimcat.model.notification.CollectionListener;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This Dimension is used within the ImageHistogram widget to display images
 * date taken spreading. It also controlles date taken filter functions.
 * 
 * $Id$
 * 
 * @author Herbert
 */
public class DateTakenDimension extends Dimension {

    /**
	 * constants for resolution - DAYS
	 */
    public static final int DAYS = 0;

    /**
	 * constants for resolution - WEEKS
	 */
    public static final int WEEKS = 1;

    /**
	 * constants for resolution - MONTHS
	 */
    public static final int MONTHS = 2;

    /**
	 * amoung of resolutions supported
	 */
    private static final int RESOLUTION_COUNT = 3;

    /**
	 * the format used to print day labels
	 */
    private static final DateTimeFormatter DAY_LABEL_FORMATTER = DateTimeFormat.forPattern("dd. MMM yy");

    /**
	 * the format used to print week labels
	 */
    private static final DateTimeFormatter WEEK_LABEL_FORMATTER = DateTimeFormat.forPattern("w / yy");

    /**
	 * the format used to print month labels
	 */
    private static final DateTimeFormatter MONTH_LABEL_FORMATTER = DateTimeFormat.forPattern("MMM yy");

    /**
	 * the view control of this jimcat instance
	 */
    private ViewControl control;

    /**
	 * the value array
	 */
    private int values[][];

    /**
	 * a vector containing max values
	 */
    private int maxValues[];

    /**
	 * the minimum date - used for labeling and calculations (resolution
	 * dependend)
	 */
    private DateTime minDate[];

    /**
	 * the lower limit set - null if not set
	 */
    private DateTime lowerLimit;

    /**
	 * the higher limit set - null if not set
	 */
    private DateTime higherLimit;

    /**
	 * the number of this dimension
	 */
    private int dimensionNumber;

    /**
	 * constructs a new dimension contained in the given ImageHistogram.
	 * 
	 * @param histogram
	 * @param viewControl
	 * @param imageControl
	 * @param number
	 */
    public DateTakenDimension(ImageHistogram histogram, ViewControl viewControl, ImageControl imageControl, int number) {
        super(histogram);
        control = viewControl;
        control.addViewFilterListener(new FilterListener());
        dimensionNumber = number;
        ImageLibrary library = imageControl.getLibrary();
        library.addListener(new ImageLibraryListener());
        reloadValues(library);
    }

    /**
	 * this methode will reload statistical information from the library
	 * 
	 * @param library
	 */
    private void reloadValues(ImageLibrary library) {
        List<Image> images = new ArrayList<Image>(library.getAll());
        int i = 0;
        DateTime min = null;
        DateTime max = null;
        while (min == null && i < images.size()) {
            min = getDateTaken(images.get(i));
            max = min;
            i++;
        }
        if (min == null || max == null) {
            values = new int[RESOLUTION_COUNT][0];
            maxValues = new int[RESOLUTION_COUNT];
            minDate = null;
            fireStructureChangedEvent();
            return;
        }
        for (; i < images.size(); i++) {
            Image img = images.get(i);
            DateTime date = getDateTaken(img);
            if (date != null && min.isAfter(date)) {
                min = date;
            }
            if (date != null && max.isBefore(date)) {
                max = date;
            }
        }
        DateTime dayRef = new DateTime(2000, 1, 1, 0, 0, 0, 0);
        DateTime weekRef = new DateTime(2000, 1, 3, 0, 0, 0, 0);
        DateTime monthRef = dayRef;
        minDate = new DateTime[RESOLUTION_COUNT];
        minDate[DAYS] = dayRef.plusDays(Days.daysBetween(dayRef, min).getDays());
        minDate[WEEKS] = weekRef.plusWeeks(Weeks.weeksBetween(weekRef, min).getWeeks());
        minDate[MONTHS] = monthRef.plusMonths(Months.monthsBetween(monthRef, min).getMonths());
        values = new int[RESOLUTION_COUNT][];
        values[DAYS] = new int[Days.daysBetween(minDate[DAYS], max).getDays() + 1];
        values[WEEKS] = new int[Weeks.weeksBetween(minDate[WEEKS], max).getWeeks() + 1];
        values[MONTHS] = new int[Months.monthsBetween(minDate[MONTHS], max).getMonths() + 1];
        for (Image img : images) {
            DateTime date = getDateTaken(img);
            if (date != null) {
                values[DAYS][Days.daysBetween(minDate[DAYS], date).getDays()]++;
                values[WEEKS][Weeks.weeksBetween(minDate[WEEKS], date).getWeeks()]++;
                values[MONTHS][Months.monthsBetween(minDate[MONTHS], date).getMonths()]++;
            }
        }
        maxValues = new int[RESOLUTION_COUNT];
        for (int j = 0; j < RESOLUTION_COUNT; j++) {
            maxValues[j] = values[j][0];
            for (int k = 1; k < values[j].length; k++) {
                if (maxValues[j] < values[j][k]) {
                    maxValues[j] = values[j][k];
                }
            }
        }
        fireStructureChangedEvent();
    }

    /**
	 * exctract date taken out of given image
	 * 
	 * @param img
	 * @return the date taken
	 */
    private DateTime getDateTaken(Image img) {
        if (img.getExifMetadata() != null) {
            return img.getExifMetadata().getDateTaken();
        }
        return null;
    }

    /**
	 * converte indizes from one resolution to another
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#convertIndex(int, int, int)
	 */
    @Override
    public int convertIndex(int fromResolution, int toResolution, int index) {
        if (fromResolution == toResolution) {
            return index;
        }
        DateTime date = indexToDate(fromResolution, index);
        int result = dateToIndex(toResolution, date);
        return Math.max(Math.min(result, getBucketCount(toResolution)), 0);
    }

    /**
	 * count buckets of resolution
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#getBucketCount(int)
	 */
    @Override
    public int getBucketCount(int resolution) {
        return values[resolution].length;
    }

    /**
	 * get resolution count
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#getCountResolutions()
	 */
    @Override
    public int getCountResolutions() {
        return RESOLUTION_COUNT;
    }

    /**
	 * get index of higher limit for given resolution
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#getHigherLimiter(int)
	 */
    @Override
    public int getHigherLimiter(int resolution) {
        if (higherLimit == null) {
            return HistogramModel.NO_LIMIT;
        }
        return dateToIndex(resolution, higherLimit);
    }

    /**
	 * get initial index for resolution 0
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#getInitialIndex()
	 */
    @Override
    public int getInitialIndex() {
        return getBucketCount(0);
    }

    /**
	 * get label for mark ...
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#getLabelForMark(int, int)
	 */
    @Override
    public String getLabelForMark(int resolution, int index) {
        switch(resolution) {
            case DAYS:
                return getDayLabel(index);
            case WEEKS:
                return getWeekLabel(index);
            case MONTHS:
                return getMonthLabel(index);
        }
        return null;
    }

    /**
	 * get a label text for given index
	 * 
	 * @param index
	 * @return label text for given index
	 */
    private String getDayLabel(int index) {
        DateTime date = indexToDate(DAYS, index);
        int day = date.getDayOfMonth();
        if (day == 1 || day == 15) {
            return DAY_LABEL_FORMATTER.print(date);
        }
        return null;
    }

    /**
	 * get label text for given index
	 * 
	 * @param index
	 * @return the label text for the given index
	 */
    private String getWeekLabel(int index) {
        DateTime date = indexToDate(WEEKS, index);
        int week = date.getWeekOfWeekyear();
        if (week == 1 || week % 10 == 0) {
            return WEEK_LABEL_FORMATTER.print(date);
        }
        return null;
    }

    /**
	 * get label text for given index
	 * 
	 * @param index
	 * @return the label text for given index
	 */
    private String getMonthLabel(int index) {
        DateTime date = indexToDate(MONTHS, index);
        int month = date.getMonthOfYear();
        if (month % 3 == 0) {
            return MONTH_LABEL_FORMATTER.print(date);
        }
        return null;
    }

    /**
	 * get lower limit using given resolution
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#getLowerLimiter(int)
	 */
    @Override
    public int getLowerLimiter(int resolution) {
        if (lowerLimit == null) {
            return HistogramModel.NO_LIMIT;
        }
        return dateToIndex(resolution, lowerLimit);
    }

    /**
	 * get mark for index
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#getMarkFor(int, int)
	 */
    @Override
    public ScaleMark getMarkFor(int resolution, int index) {
        ScaleMark result = ScaleMark.SMALL;
        DateTime date = indexToDate(resolution, index);
        if (date == null) {
            return ScaleMark.NONE;
        }
        if (resolution == DAYS) {
            int day = date.getDayOfMonth();
            if (day == 1 || day == 15) {
                result = ScaleMark.LABEL;
            }
        } else if (resolution == WEEKS) {
            int week = date.getWeekOfWeekyear();
            if (week == 1 || week % 10 == 0) {
                result = ScaleMark.LABEL;
            }
        } else if (resolution == MONTHS) {
            int month = date.getMonthOfYear();
            if (month % 3 == 0) {
                result = ScaleMark.LABEL;
            }
        }
        return result;
    }

    /**
	 * get static name
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#getName()
	 */
    @Override
    public String getName() {
        return "Date Taken";
    }

    /**
	 * get static names for resolutions
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#getNameFor(int)
	 */
    @Override
    public String getNameFor(int resolution) {
        switch(resolution) {
            case DAYS:
                return "Days";
            case WEEKS:
                return "Weeks";
            default:
                return "Months";
        }
    }

    /**
	 * get value for given index
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#getValueAt(int, int)
	 */
    @Override
    public float getValueAt(int resolution, int index) throws IllegalArgumentException {
        return getAbsoluteValueAt(resolution, index) / (float) maxValues[resolution];
    }

    /**
	 * get absolute value at given index
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#getAbsoluteValueAt(int,
	 *      int)
	 */
    @Override
    public int getAbsoluteValueAt(int resolution, int index) throws IllegalArgumentException {
        try {
            return values[resolution][index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("unsupported resolution: " + resolution + " / index: " + index);
        }
    }

    /**
	 * set higher limit
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#setHigherLimiter(int, int)
	 */
    @Override
    public void setHigherLimiter(int resolution, int index) throws IllegalArgumentException {
        DateTime newLimit = null;
        if (index != HistogramModel.NO_LIMIT) {
            newLimit = indexToDate(resolution, index);
        }
        if (!ObjectUtils.equals(higherLimit, newLimit)) {
            higherLimit = newLimit;
            PictureTakenFilter filter = null;
            if (newLimit != null) {
                filter = new PictureTakenFilter(Type.BEFORE, newLimit);
            }
            control.setPictureTakenBefore(filter);
        }
    }

    /**
	 * set lower limit
	 * 
	 * @see org.jimcat.gui.histogram.image.Dimension#setLowerLimiter(int, int)
	 */
    @Override
    public void setLowerLimiter(int resolution, int index) throws IllegalArgumentException {
        DateTime newLimit = null;
        if (index != HistogramModel.NO_LIMIT) {
            newLimit = indexToDate(resolution, index);
        }
        if (!ObjectUtils.equals(lowerLimit, newLimit)) {
            lowerLimit = newLimit;
            PictureTakenFilter filter = null;
            if (newLimit != null) {
                filter = new PictureTakenFilter(Type.AFTER, newLimit);
            }
            control.setPictureTakenAfter(filter);
        }
    }

    /**
	 * converte a given resolution / index pair to a date time
	 * 
	 * @param resolution
	 * @param index
	 * @return the converted date
	 */
    private DateTime indexToDate(int resolution, int index) {
        if (minDate == null) {
            return null;
        }
        DateTime date = null;
        switch(resolution) {
            case DAYS:
                date = minDate[DAYS].plusDays(index);
                break;
            case WEEKS:
                date = minDate[WEEKS].plusWeeks(index);
                break;
            default:
                date = minDate[MONTHS].plusMonths(index);
                break;
        }
        return date;
    }

    /**
	 * converte a given resolution / date pair to a index
	 * 
	 * @param resolution
	 * @param date
	 * @return the converted index
	 */
    private int dateToIndex(int resolution, DateTime date) {
        if (date == null || minDate == null) {
            return 0;
        }
        switch(resolution) {
            case DAYS:
                return Days.daysBetween(minDate[DAYS], date).getDays();
            case WEEKS:
                return Weeks.weeksBetween(minDate[WEEKS], date).getWeeks();
            default:
                return Months.monthsBetween(minDate[MONTHS], date).getMonths();
        }
    }

    /**
	 * small class listening to filter changes
	 */
    private class FilterListener implements ViewFilterListener {

        /**
		 * react on filter changes
		 * 
		 * @see org.jimcat.gui.ViewFilterListener#filterChanges(org.jimcat.gui.ViewControl)
		 */
        public void filterChanges(ViewControl viewControl) {
            PictureTakenFilter after = viewControl.getPictureTakenAfter();
            PictureTakenFilter before = viewControl.getPictureTakenBefore();
            DateTime lower = null;
            if (after != null) {
                lower = after.getLimitDate();
            }
            DateTime higher = null;
            if (before != null) {
                higher = before.getLimitDate();
            }
            if (!ObjectUtils.equals(lower, lowerLimit)) {
                lowerLimit = lower;
                HistogramModelPath path = new HistogramModelPath(dimensionNumber, 0, 0);
                HistogramModelEvent event = new HistogramModelEvent(null, path, false);
                fireLimitChangedEvent(event);
            }
            if (!ObjectUtils.equals(higher, higherLimit)) {
                higherLimit = higher;
                HistogramModelPath path = new HistogramModelPath(dimensionNumber, 0, 0);
                HistogramModelEvent event = new HistogramModelEvent(null, path, true);
                fireLimitChangedEvent(event);
            }
        }
    }

    /**
	 * the listener observing images states
	 */
    private class ImageLibraryListener implements CollectionListener<Image, ImageLibrary> {

        /**
		 * @param collection
		 * @see org.jimcat.model.notification.CollectionListener#basementChanged(org.jimcat.model.notification.ObservableCollection)
		 */
        public void basementChanged(ImageLibrary collection) {
            reloadValues(collection);
        }

        /**
		 * react on added elements
		 * 
		 * @param collection
		 * @param elements
		 * 
		 * @see org.jimcat.model.notification.CollectionListener#elementsAdded(org.jimcat.model.notification.ObservableCollection,
		 *      java.util.Set)
		 */
        public void elementsAdded(ImageLibrary collection, Set<Image> elements) {
            Set<HistogramModelPath> paths = new HashSet<HistogramModelPath>();
            for (Image element : elements) {
                DateTime date = getDateTaken(element);
                if (date == null) {
                    continue;
                }
                for (int res = 0; res < values.length; res++) {
                    int index = dateToIndex(res, date);
                    if (index < 0 || index >= values[res].length) {
                        reloadValues(collection);
                        return;
                    }
                    values[res][index]++;
                    int newValue = values[res][index];
                    if (newValue > maxValues[res]) {
                        maxValues[res] = newValue;
                    }
                    paths.add(new HistogramModelPath(dimensionNumber, res, index));
                }
            }
            HistogramModelEvent event = new HistogramModelEvent(getHistogram(), paths);
            fireValueChangedEvent(event);
        }

        /**
		 * react on removed elements
		 * 
		 * @param collection
		 * @param elements
		 * 
		 * @see org.jimcat.model.notification.CollectionListener#elementsRemoved(org.jimcat.model.notification.ObservableCollection,
		 *      java.util.Set)
		 */
        public void elementsRemoved(ImageLibrary collection, Set<Image> elements) {
            Set<HistogramModelPath> paths = new HashSet<HistogramModelPath>();
            for (Image element : elements) {
                DateTime date = getDateTaken(element);
                if (date == null) {
                    continue;
                }
                for (int res = 0; res < values.length; res++) {
                    int index = dateToIndex(res, date);
                    int oldValue = values[res][index];
                    values[res][index]--;
                    if (oldValue == maxValues[res]) {
                        int max = values[res][0];
                        for (int i = 1; i < values[res].length; i++) {
                            int cur = values[res][i];
                            if (cur > max) {
                                max = cur;
                            }
                        }
                        maxValues[res] = max;
                    }
                    if (values[res][0] == 0 || values[res][getBucketCount(res) - 1] == 0) {
                        reloadValues(collection);
                        return;
                    }
                    paths.add(new HistogramModelPath(dimensionNumber, res, index));
                }
            }
            HistogramModelEvent event = new HistogramModelEvent(getHistogram(), paths);
            fireValueChangedEvent(event);
        }

        /**
		 * react on updates
		 * 
		 * @param collection
		 * @param events
		 * 
		 * @see org.jimcat.model.notification.CollectionListener#elementsUpdated(org.jimcat.model.notification.ObservableCollection,
		 *      java.util.List)
		 */
        public void elementsUpdated(ImageLibrary collection, List<BeanChangeEvent<Image>> events) {
            for (BeanChangeEvent<Image> event : events) {
                if (event.getProperty() == BeanProperty.IMAGE_EXIF_META) {
                    reloadValues(collection);
                    return;
                }
            }
        }
    }
}
