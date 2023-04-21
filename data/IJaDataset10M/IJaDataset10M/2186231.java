package org.libreplan.business.calendars.daos;

import org.libreplan.business.calendars.entities.CalendarAvailability;
import org.libreplan.business.common.daos.IntegrationEntityDAO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * DAO for {@link CalendarAvailability}
 *
 * @author Susana Montes Pedreira <smontes@wirelessgalicia.com>
 */
@Repository
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CalendarAvailabilityDAO extends IntegrationEntityDAO<CalendarAvailability> implements ICalendarAvailabilityDAO {
}
