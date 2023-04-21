package org.rapla.entities.configuration;

import java.util.Collection;
import java.util.Date;
import org.rapla.entities.RaplaObject;
import org.rapla.entities.RaplaType;
import org.rapla.entities.dynamictype.ClassificationFilter;
import org.rapla.entities.storage.Mementable;

/**
 *
 * @author ckohlhaas
 * @version 1.00.00
 * @since 2.03.00
 */
public interface CalendarModelConfiguration extends RaplaObject, Mementable<CalendarModelConfiguration> {

    public static final RaplaType TYPE = new RaplaType(CalendarModelConfiguration.class, "calendar");

    public static final String CONFIG_ENTRY = "org.rapla.DefaultSelection";

    public Date getStartDate();

    public Date getEndDate();

    public Date getSelectedDate();

    public String getTitle();

    public String getView();

    public Collection getSelected();

    public RaplaMap getSelectedMap();

    public RaplaMap getOptionMap();

    public ClassificationFilter[] getFilter();

    public boolean isDefaultEventTypes();

    public boolean isDefaultResourceTypes();
}
