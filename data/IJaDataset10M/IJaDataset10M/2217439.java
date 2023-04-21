package org.jowidgets.api.widgets.descriptor.setup;

import java.util.Date;
import org.jowidgets.common.widgets.descriptor.setup.ICalendarSetupCommon;

public interface ICalendarSetup extends IComponentSetup, ICalendarSetupCommon {

    Date getDate();
}
