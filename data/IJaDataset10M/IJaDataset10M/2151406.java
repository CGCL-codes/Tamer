package org.dbe.composer.wfengine.bpel.impl.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.xml.namespace.QName;
import org.dbe.composer.wfengine.SdlException;
import org.dbe.composer.wfengine.bpel.SdlBusinessProcessException;
import org.dbe.composer.wfengine.bpel.impl.ISdlBusinessProcessEngineInternal;
import org.dbe.composer.wfengine.bpel.impl.queue.SdlAlarm;
import org.dbe.composer.wfengine.util.SdlUtil;

/**
 * Provides filtering capability for the in-memory alarm manager listing.
 */
public class AlarmFilterManager {

    /** Default comparator for sorting the list of alarms. */
    private static final SdlAlarmComparator SORTER = new SdlAlarmComparator();

    /**
     * Returns a filtered list of alarms.
     * @param aEngine the bpel engine.
     * @param aFilter Determines the selection criteria.
     * @param aAlarms All of the available alarms on the queue.
     * @return The filtered results.
     */
    public static AlarmListResult filter(ISdlBusinessProcessEngineInternal aEngine, AlarmFilter aFilter, List aAlarms) {
        List matches = new ArrayList();
        int totalRows = 0;
        if (aAlarms != null && !aAlarms.isEmpty()) {
            SdlAlarm[] recs = (SdlAlarm[]) aAlarms.toArray(new SdlAlarm[aAlarms.size()]);
            for (int i = aFilter.getListStart(); i < recs.length; i++) {
                SdlAlarm alarm = recs[i];
                try {
                    QName processQName = aEngine.getProcessManager().getProcessQName(alarm.getProcessId());
                    AlarmExt alarmExt = new AlarmExt(alarm.getProcessId(), alarm.getPathId(), alarm.getDeadline(), processQName);
                    if (accepts(aFilter, alarmExt)) {
                        totalRows++;
                        if (aFilter.isWithinRange(totalRows)) {
                            matches.add(alarmExt);
                        }
                    }
                } catch (SdlBusinessProcessException e) {
                    SdlException.logError(e, "Error filtering alarm for process " + alarm.getProcessId());
                }
            }
        }
        if (!matches.isEmpty()) {
            sort(matches);
        }
        return new AlarmListResult(totalRows, matches);
    }

    /**
     * Returns true if the message alarm meets the filter criteria.
     * @param aFilter The selection criteria.
     * @param aAlarm A queued message alarm.
     */
    protected static boolean accepts(AlarmFilter aFilter, AlarmExt aAlarm) {
        return isPIDMatch(aFilter, aAlarm) && isProcessNameMatch(aFilter, aAlarm) && isDeadlineMatch(aFilter, aAlarm);
    }

    /**
     * Match that the alarm matches the deadline between dates if filled in.
     * @param aFilter The filter to match the start and end between from
     * @param aAlarm The alarm to test.
     * @return True if match of deadline with filter.
     */
    private static boolean isDeadlineMatch(AlarmFilter aFilter, AlarmExt aAlarm) {
        if (aFilter.getAlarmFilterStart() != null) {
            if (aAlarm.getDeadline().getTime() < aFilter.getAlarmFilterStart().getTime()) {
                return false;
            }
        }
        if (aFilter.getAlarmFilterEnd() != null) {
            if (aAlarm.getDeadline().getTime() > aFilter.getAlarmFilterEnd().getTime()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Match that the alarm matches the proess name in filter if filled in.
     * @param aFilter The filter to match the process name
     * @param aAlarm The alarm to test.
     * @return True if the process name matches the passed name.
     */
    private static boolean isProcessNameMatch(AlarmFilter aFilter, AlarmExt aAlarm) {
        if (aFilter.getProcessName() != null) {
            if (!SdlUtil.isNullOrEmpty(aFilter.getProcessName().getLocalPart())) return SdlUtil.compareObjects(aFilter.getProcessName().getLocalPart(), aAlarm.getProcessName());
            if (!SdlUtil.isNullOrEmpty(aFilter.getProcessName().getNamespaceURI())) return SdlUtil.compareObjects(aFilter.getProcessName().getNamespaceURI(), aAlarm.getProcessQName().getNamespaceURI());
        }
        return true;
    }

    /**
     * Returns true if there is no process id specified in the filter or
     * if the process id in the filter mathes the receive's process id.
     * @param aFilter The selection criteria.
     * @param aAlarm A queued message alarm.
     */
    static boolean isPIDMatch(AlarmFilter aFilter, AlarmExt aAlarm) {
        if (!aFilter.isNullProcessId()) {
            return aFilter.getProcessId() == aAlarm.getProcessId();
        } else {
            return true;
        }
    }

    /**
     * Sorts the matching queued alarm.
     * @param aMatches
     */
    protected static void sort(List aMatches) {
        Collections.sort(aMatches, SORTER);
    }

    /**
     * Comparator impl compares on process id.
     */
    protected static class SdlAlarmComparator implements Comparator {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object a1, Object a2) {
            SdlAlarm alarmOne = (SdlAlarm) a1;
            SdlAlarm alarmTwo = (SdlAlarm) a2;
            int match = (int) (alarmOne.getProcessId() - alarmTwo.getProcessId());
            return match;
        }
    }
}
