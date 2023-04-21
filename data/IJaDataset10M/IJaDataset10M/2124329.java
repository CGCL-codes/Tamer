package org.libreplan.business.calendars.daos;

import java.util.List;
import org.libreplan.business.calendars.entities.CalendarExceptionType;
import org.libreplan.business.common.daos.IIntegrationEntityDAO;
import org.libreplan.business.common.exceptions.InstanceNotFoundException;

/**
 * Contract for {@link CalendarExceptionTypeDAO}
 *
 * @author Manuel Rego Casasnovas <mrego@igalia.com>
 * @author Diego Pino Garcia <dpino@igalia.com>
 */
public interface ICalendarExceptionTypeDAO extends IIntegrationEntityDAO<CalendarExceptionType> {

    boolean existsByName(CalendarExceptionType type);

    List<CalendarExceptionType> getAll();

    boolean hasCalendarExceptions(CalendarExceptionType type);

    boolean existsByNameAnotherTransaction(String name);

    CalendarExceptionType findUniqueByName(String name) throws InstanceNotFoundException;

    boolean existsByName(String name);

    CalendarExceptionType findUniqueByNameAnotherTransaction(String name) throws InstanceNotFoundException;
}
