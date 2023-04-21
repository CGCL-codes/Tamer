package com.webstarsltd.common.pim.xvcalendar;

import java.util.Hashtable;
import com.webstarsltd.common.pim.model.PropertySemantics;
import com.webstarsltd.common.pim.model.ParameterSemantics;
import com.webstarsltd.common.pim.model.ValueDayLight;
import com.webstarsltd.common.pim.model.ValuePeriod;
import com.webstarsltd.common.pim.model.ValueText;
import com.webstarsltd.common.pim.model.ValueBool;
import com.webstarsltd.common.pim.model.ValueBinary;
import com.webstarsltd.common.pim.model.ValueDate;
import com.webstarsltd.common.pim.model.ValueDateTime;
import com.webstarsltd.common.pim.model.ValueUtcOffset;
import com.webstarsltd.common.pim.model.ValueFloat;
import com.webstarsltd.common.pim.model.ValueRecur;
import com.webstarsltd.common.pim.model.ValueDuration;
import com.webstarsltd.common.pim.model.ValueInteger;
import com.webstarsltd.common.pim.model.ValueUri;
import com.webstarsltd.common.pim.model.ValueCalAddress;

/**
 * A classe providing the limitations of all properties.
 *
 * @version $Id: XVCalProperties.java,v 1.4 2007/06/18 12:41:01 luigiafassina Exp $
 */
public class XVCalProperties {

    public static final Hashtable table = new Hashtable();

    protected static final ValueDayLight daylight = new ValueDayLight();

    protected static final ValueInteger integer = new ValueInteger();

    protected static final ValueText text = new ValueText();

    protected static final ValueText classProp = new ValueText();

    protected static final ValueText geo = new ValueText();

    protected static final ValueText cmd = new ValueText();

    protected static final ValueText transp = new ValueText();

    protected static final ValueText carLevel = new ValueText();

    protected static final ValueText queryLevel = new ValueText();

    protected static final ValueText query = new ValueText();

    protected static final ValueText method = new ValueText();

    protected static final ValueText status = new ValueText();

    protected static final ValueDuration duration = new ValueDuration();

    protected static final ValueRecur recur = new ValueRecur();

    protected static final ValueUri uri = new ValueUri();

    protected static final ValuePeriod period = new ValuePeriod();

    protected static final ValueFloat floatVal = new ValueFloat();

    protected static final ValueDateTime dateTime = new ValueDateTime();

    protected static final ValueCalAddress calAddress = new ValueCalAddress();

    protected static final ValueUtcOffset utcOffset = new ValueUtcOffset();

    protected static final ValueDate date = new ValueDate();

    protected static final ValueBool bool = new ValueBool();

    protected static final ValueBinary binary = new ValueBinary();

    protected static final PropertySemantics PROP_ACTION = new PropertySemantics("DAYLIGHT", daylight, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_PRODID = new PropertySemantics("PRODID", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_TZ = new PropertySemantics("TZ", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_VERSION = new PropertySemantics("VERSION", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_ATTACH = new PropertySemantics("ATTACH", uri, new ParameterSemantics[] { XVCalParameters.CONTENT_ID, XVCalParameters.URL }, 0, 1, table);

    protected static final PropertySemantics PROP_ATTENDEE = new PropertySemantics("ATTENDEE", calAddress, new ParameterSemantics[] {}, 0, Integer.MAX_VALUE, table);

    protected static final PropertySemantics PROP_AALARM = new PropertySemantics("AALARM", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_PALARM = new PropertySemantics("PALARM", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_DALARM = new PropertySemantics("DALARM", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_MALARM = new PropertySemantics("MALARM", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_CLASS = new PropertySemantics("CLASS", classProp, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_DCREATED = new PropertySemantics("DCREATED", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_COMPLETED = new PropertySemantics("COMPLETED", dateTime, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_DESCRIPTION = new PropertySemantics("DESCRIPTION", text, new ParameterSemantics[] { XVCalParameters.ENCODING }, 0, 1, table);

    protected static final PropertySemantics PROP_DUE = new PropertySemantics("DUE", dateTime, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_DTEND = new PropertySemantics("DTEND", dateTime, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_EXDATE = new PropertySemantics("EXDATE", dateTime, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_DURATION = new PropertySemantics("DURATION", duration, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_RELATED_TO = new PropertySemantics("RELATED-TO", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_RDATE = new PropertySemantics("RDATE", dateTime, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_RESOURCES = new PropertySemantics("RESOURCES", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_RRULE = new PropertySemantics("RRULE", recur, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_SEQUENCE = new PropertySemantics("SEQUENCE", integer, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_LAST_MODIFIED = new PropertySemantics("LAST-MODIFIED", dateTime, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_DTSTART = new PropertySemantics("DTSTART", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_STATUS = new PropertySemantics("STATUS", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_TRANSP = new PropertySemantics("TRANSP", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_LOCATION = new PropertySemantics("LOCATION", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_URL = new PropertySemantics("URL", method, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_SUMMARY = new PropertySemantics("SUMMARY", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_UID = new PropertySemantics("UID", text, new ParameterSemantics[] {}, 0, 1, table);

    protected static final PropertySemantics PROP_ORGANIZER = new PropertySemantics("ORGANIZER", calAddress, new ParameterSemantics[] { XVCalParameters.CN, XVCalParameters.DIR, XVCalParameters.LANGUAGE, XVCalParameters.SENT_BY }, 0, 1, table);

    protected static final PropertySemantics PROP_PRIORITY = new PropertySemantics("PRIORITY", integer, new ParameterSemantics[] {}, 0, 1, table);
}
