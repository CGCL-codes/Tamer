package net.sf.depcon.event.ui.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.sf.depcon.model.event.Event;
import net.sf.depcon.model.event.EventPackage;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.widgets.compositetable.viewers.IMonthCalendarContentProvider;
import com.ibm.icu.util.Calendar;

public class ObservableMonthCalenderContentProvider2 implements IMonthCalendarContentProvider {

    private Map<Date, ArrayList<Object>> dateToEventMap;

    private IObservableMap endDateMap;

    private IObservableList inputList;

    IListChangeListener listListener = new IListChangeListener() {

        public void handleListChange(ListChangeEvent e) {
            for (ListDiffEntry entry : e.diff.getDifferences()) {
                if (entry.isAddition()) {
                    writableSet.add(entry.getElement());
                }
            }
        }
    };

    private IMapChangeListener mapListener = new IMapChangeListener() {

        @SuppressWarnings("unchecked")
        public void handleMapChange(MapChangeEvent event) {
            Set changedKeys = event.diff.getChangedKeys();
            for (Object key : changedKeys) {
                Event changedEvent = (Event) key;
                Date newDate = (Date) event.diff.getNewValue(key);
                if (newDate == null) {
                    for (Date date : dateToEventMap.keySet()) {
                        dateToEventMap.get(date).remove(changedEvent);
                    }
                    if (changedEvent.getStartDate() != null) {
                        dateToEventMap.get(transformDate(changedEvent.getStartDate())).add(changedEvent);
                    }
                } else if (newDate.equals(changedEvent.getStartDate())) {
                    Date oldStart = (Date) event.diff.getOldValue(key);
                    if (oldStart == null) {
                        dateToEventMap.get(transformDate(newDate)).add(changedEvent);
                    } else if (changedEvent.getEndDate() == null) {
                        dateToEventMap.get(transformDate(oldStart)).remove(changedEvent);
                        dateToEventMap.get(transformDate(newDate)).add(changedEvent);
                    } else if (compareDates(oldStart, newDate) > 0) {
                        for (Date date : dateToEventMap.keySet()) {
                            if (compareDates(date, oldStart) <= 0 && compareDates(date, newDate) > 0) {
                                dateToEventMap.get(date).remove(changedEvent);
                            }
                        }
                    } else if (compareDates(oldStart, newDate) < 0) {
                        for (Date date : dateToEventMap.keySet()) {
                            if (compareDates(date, newDate) <= 0 && compareDates(date, oldStart) > 0) {
                                dateToEventMap.get(date).add(changedEvent);
                            }
                        }
                    }
                } else {
                    Date oldEnd = (Date) event.diff.getOldValue(key);
                    if (oldEnd == null) {
                        for (Date date : dateToEventMap.keySet()) {
                            if (compareDates(changedEvent.getStartDate(), date) > 0 && compareDates(date, newDate) >= 0) {
                                dateToEventMap.get(date).add(changedEvent);
                            }
                        }
                    } else if (compareDates(oldEnd, newDate) > 0) {
                        for (Date date : dateToEventMap.keySet()) {
                            if (compareDates(date, oldEnd) < 0 && compareDates(date, newDate) >= 0) {
                                dateToEventMap.get(date).add(changedEvent);
                            }
                        }
                    } else if (compareDates(oldEnd, newDate) < 0) {
                        for (Date date : dateToEventMap.keySet()) {
                            if (compareDates(date, newDate) < 0 && compareDates(date, oldEnd) >= 0) {
                                dateToEventMap.get(date).remove(changedEvent);
                            }
                        }
                    }
                }
            }
            viewer.refresh();
        }
    };

    private Date transformDate(Date newDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(newDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private IObservableMap startDateMap;

    private Viewer viewer;

    private WritableSet writableSet;

    public ObservableMonthCalenderContentProvider2() {
        inputList = new WritableList();
        dateToEventMap = new HashMap<Date, ArrayList<Object>>();
    }

    protected int compareDates(Date date1, Date date2) {
        if (date1 == null && date2 != null) {
            return 1;
        } else if (date1 != null && date2 == null) {
            return -1;
        } else if (date1 == null && date2 == null) {
            return 0;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int sameYear = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
        if (sameYear == 0) {
            int sameMonth = cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
            if (sameMonth == 0) {
                return cal2.get(Calendar.DATE) - cal1.get(Calendar.DATE);
            }
            return sameMonth;
        }
        return sameYear;
    }

    public void dispose() {
        if (startDateMap != null) startDateMap.removeMapChangeListener(mapListener);
        if (endDateMap != null) endDateMap.removeMapChangeListener(mapListener);
        if (inputList != null) inputList.removeListChangeListener(listListener);
        inputList = null;
        writableSet = null;
    }

    protected boolean eventOnDate(Event event, Date date) {
        return event.getStartDate() != null && ((event.getEndDate() == null && compareDates(event.getStartDate(), date) == 0) || (event.getEndDate() != null && compareDates(event.getStartDate(), date) >= 0 && compareDates(date, event.getEndDate()) >= 0));
    }

    public Object[] getElements(Date date, Object inputElement) {
        ArrayList<Object> result = dateToEventMap.get(date);
        if (result == null) {
            result = new ArrayList<Object>();
            for (Object element : inputList) {
                if (element instanceof Event) {
                    Event event = (Event) element;
                    if (eventOnDate(event, date)) {
                        result.add(event);
                    }
                }
            }
            dateToEventMap.put(date, result);
        }
        return result.toArray();
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.viewer = viewer;
        dateToEventMap = new HashMap<Date, ArrayList<Object>>();
        inputList.removeListChangeListener(listListener);
        inputList = null;
        if (startDateMap != null) startDateMap.removeMapChangeListener(mapListener);
        if (endDateMap != null) endDateMap.removeMapChangeListener(mapListener);
        if (newInput != null) {
            inputList = (IObservableList) newInput;
            inputList.addListChangeListener(listListener);
            writableSet = new WritableSet(inputList, Event.class);
            endDateMap = EMFObservables.observeMap(writableSet, EventPackage.eINSTANCE.getEvent_EndDate());
            startDateMap = EMFObservables.observeMap(writableSet, EventPackage.eINSTANCE.getEvent_StartDate());
            startDateMap.addMapChangeListener(mapListener);
            endDateMap.addMapChangeListener(mapListener);
        }
    }
}
