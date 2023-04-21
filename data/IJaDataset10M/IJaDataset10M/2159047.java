package com.peusoft.ptcollect.core.service.persistance;

import com.peusoft.ptcollect.core.persistance.domain.Project;
import com.peusoft.ptcollect.core.persistance.domain.User;
import com.peusoft.ptcollect.core.persistance.domain.WorkDay;
import com.peusoft.ptcollect.core.persistance.domain.WorkDayTimeRecord;
import java.util.Calendar;
import java.util.List;

/**
 * Defines the functionality of the work time service.
 * 
 * @author Yauheni Prykhodzka
 * @version 1.0
 * @since 1.0
 *
 */
public interface WorkTimeService extends DataAccessService {

    /**
     * Looks for work time records for the period.
     * @param year year
     * @param user user
     * @return list with work time records for the year
     */
    List<WorkDayTimeRecord> getWorkTimeRecordsForYear(int year, User user);

    /**
     * Looks for work time records for the period.
     * @param year year
     * @param project project, if null looks for all projects
     * @param user user
     * @return list with work time records for the year
     */
    List<WorkDayTimeRecord> getWorkTimeRecordsForYear(int year, Project project, User user);

    /**
     * Looks for work time records for the period.
     * @param month month begins with 1
     * @param year year
     * @param user user
     * @return list with work time records for the month
     */
    List<WorkDayTimeRecord> getWorkTimeRecordsForMonth(int month, int year, User user);

    /**
     * Looks for work time records for the period.
     * @param month month begins with 1
     * @param year year
     * @param project project, if null looks for all projects
     * @param user user
     * @return list with work time records for the month
     */
    List<WorkDayTimeRecord> getWorkTimeRecordsForMonth(int month, int year, Project project, User user);

    /**
     * Looks for work time records for the period.
     * @param week week of the year
     * @param year year
     * @param user user
     * @return list with work time records
     */
    List<WorkDayTimeRecord> getWorkTimeRecordsForWeek(int week, int year, User user);

    /**
     * Looks for work time records for the period.
     * @param week week of the year
     * @param year year
     * @param project project, if null looks for all projects
     * @param user user
     * @return list with work time records
     */
    List<WorkDayTimeRecord> getWorkTimeRecordsForWeek(int week, int year, Project project, User user);

    /**
     * Looks for work time records for the period.
     * @param begin begin of the period
     * @param end end of the period
     * @param user user
     * @return list with work time records from the period
     */
    List<WorkDayTimeRecord> getWorkTimeRecordsForPeriod(Calendar begin, Calendar end, User user);

    /**
     * Looks for work time records for the period.
     * @param begin begin of the period
     * @param end end of the period
     * @param project project, if null looks for all projects
     * @param user user
     * @return list with work time records from the period
     */
    List<WorkDayTimeRecord> getWorkTimeRecordsForPeriod(Calendar begin, Calendar end, Project project, User user);

    /**
     * Looks for work days for the period.
     * @param year year
     * @param user user
     * @return list with work days for the year
     */
    List<WorkDay> getWorkDaysForYear(int year, User user);

    /**
     * Looks for work days for the period.
     * @param month month begins with 1
     * @param year year
     * @param user user
     * @return list with work days for the month
     */
    List<WorkDay> getWorkDaysForMonth(int month, int year, User user);

    /**
     * Looks for work days for the period.
     * @param week week
     * @param year year
     * @param user user
     * @return list with work days for the week
     */
    List<WorkDay> getWorkDaysForWeek(int week, int year, User user);

    /**
     * Looks for work days for the period.
     * @param begin begin of the period
     * @param end end of the period
     * @param user user
     * @return list with work days for the week
     */
    List<WorkDay> getWorkDaysForPeriod(Calendar begin, Calendar end, User user);

    /**
     * Looks for work days for the period.
     * Creates missed work days that are missed in this period.
     * New created days will not be saved.
     * @param year year
     * @param user user
     * @return list with work days for the year
     */
    List<WorkDay> getOrCreateWorkDaysForYear(int year, User user);

    /**
     * Looks for work days for the period.
     * Creates missed work days that are missed in this period.
     * New created days will not be saved.
     * @param month month begins with 1
     * @param year year
     * @param user user
     * @return list with work days for the month
     */
    List<WorkDay> getOrCreateWorkDaysForMonth(int month, int year, User user);

    /**
     * Looks for work days for the period.
     * Creates missed work days that are missed in this period.
     * New created days will not be saved.
     * @param week week
     * @param year year
     * @param user user
     * @return list with work days for the week
     */
    List<WorkDay> getOrCreateWorkDaysForWeek(int week, int year, User user);

    /**
     * Returns work days for the specified period.
     * Creates missed work days that are missed in this period.
     * New created days will not be saved.
     * @param begin period's begin
     * @param end period's end
     * @param user user
     * @return a list of all work days for the specified period
     */
    List<WorkDay> getOrCreateWorkDaysForPeriod(Calendar begin, Calendar end, User user);
}
