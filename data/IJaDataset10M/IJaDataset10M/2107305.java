package com.google.wireless.gdata.calendar.serializer.xml;

import com.google.wireless.gdata.calendar.data.EventEntry;
import com.google.wireless.gdata.calendar.data.When;
import com.google.wireless.gdata.calendar.data.Reminder;
import com.google.wireless.gdata.calendar.data.Who;
import com.google.wireless.gdata.data.StringUtils;
import com.google.wireless.gdata.parser.xml.XmlGDataParser;
import com.google.wireless.gdata.parser.xml.XmlParserFactory;
import com.google.wireless.gdata.parser.ParseException;
import com.google.wireless.gdata.serializer.xml.XmlEntryGDataSerializer;
import org.xmlpull.v1.XmlSerializer;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class XmlEventEntryGDataSerializer extends XmlEntryGDataSerializer {

    public static final String NAMESPACE_GCAL = "gCal";

    public static final String NAMESPACE_GCAL_URI = "http://schemas.google.com/gCal/2005";

    public XmlEventEntryGDataSerializer(XmlParserFactory factory, EventEntry entry) {
        super(factory, entry);
    }

    protected EventEntry getEventEntry() {
        return (EventEntry) getEntry();
    }

    protected void declareExtraEntryNamespaces(XmlSerializer serializer) throws IOException {
        serializer.setPrefix(NAMESPACE_GCAL, NAMESPACE_GCAL_URI);
    }

    protected void serializeExtraEntryContents(XmlSerializer serializer, int format) throws IOException, ParseException {
        EventEntry entry = getEventEntry();
        serializeEventStatus(serializer, entry.getStatus());
        serializeTransparency(serializer, entry.getTransparency());
        serializeVisibility(serializer, entry.getVisibility());
        if (entry.getSendEventNotifications()) {
            serializer.startTag(NAMESPACE_GCAL_URI, "sendEventNotifications");
            serializer.attribute(null, "value", "true");
            serializer.endTag(NAMESPACE_GCAL_URI, "sendEventNotifications");
        }
        Enumeration attendees = entry.getAttendees().elements();
        while (attendees.hasMoreElements()) {
            Who attendee = (Who) attendees.nextElement();
            serializeWho(serializer, entry, attendee);
        }
        serializeRecurrence(serializer, entry.getRecurrence());
        if (entry.getRecurrence() != null) {
            if (entry.getReminders() != null) {
                Enumeration reminders = entry.getReminders().elements();
                while (reminders.hasMoreElements()) {
                    Reminder reminder = (Reminder) reminders.nextElement();
                    serializeReminder(serializer, reminder);
                }
            }
        } else {
            Enumeration whens = entry.getWhens().elements();
            while (whens.hasMoreElements()) {
                When when = (When) whens.nextElement();
                serializeWhen(serializer, entry, when);
            }
        }
        serializeOriginalEvent(serializer, entry.getOriginalEventId(), entry.getOriginalEventStartTime());
        serializeWhere(serializer, entry.getWhere());
        serializeCommentsUri(serializer, entry.getCommentsUri());
        Hashtable extendedProperties = entry.getExtendedProperties();
        if (extendedProperties != null) {
            Enumeration propertyNames = extendedProperties.keys();
            while (propertyNames.hasMoreElements()) {
                String propertyName = (String) propertyNames.nextElement();
                String propertyValue = (String) extendedProperties.get(propertyName);
                serializeExtendedProperty(serializer, propertyName, propertyValue);
            }
        }
        serializeQuickAdd(serializer, entry.isQuickAdd());
    }

    private static void serializeEventStatus(XmlSerializer serializer, byte status) throws IOException {
        String statusString;
        switch(status) {
            case EventEntry.STATUS_TENTATIVE:
                statusString = "http://schemas.google.com/g/2005#event.tentative";
                break;
            case EventEntry.STATUS_CANCELED:
                statusString = "http://schemas.google.com/g/2005#event.canceled";
                break;
            case EventEntry.STATUS_CONFIRMED:
                statusString = "http://schemas.google.com/g/2005#event.confirmed";
                break;
            default:
                statusString = "http://schemas.google.com/g/2005#event.tentative";
        }
        serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "eventStatus");
        serializer.attribute(null, "value", statusString);
        serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "eventStatus");
    }

    private static void serializeRecurrence(XmlSerializer serializer, String recurrence) throws IOException {
        if (StringUtils.isEmpty(recurrence)) {
            return;
        }
        serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "recurrence");
        serializer.text(recurrence);
        serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "recurrence");
    }

    private static void serializeTransparency(XmlSerializer serializer, byte transparency) throws IOException {
        String transparencyString;
        switch(transparency) {
            case EventEntry.TRANSPARENCY_OPAQUE:
                transparencyString = "http://schemas.google.com/g/2005#event.opaque";
                break;
            case EventEntry.TRANSPARENCY_TRANSPARENT:
                transparencyString = "http://schemas.google.com/g/2005#event.transparent";
                break;
            default:
                transparencyString = "http://schemas.google.com/g/2005#event.transparent";
        }
        serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "transparency");
        serializer.attribute(null, "value", transparencyString);
        serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "transparency");
    }

    private static void serializeVisibility(XmlSerializer serializer, byte visibility) throws IOException {
        String visibilityString;
        switch(visibility) {
            case EventEntry.VISIBILITY_DEFAULT:
                visibilityString = "http://schemas.google.com/g/2005#event.default";
                break;
            case EventEntry.VISIBILITY_CONFIDENTIAL:
                visibilityString = "http://schemas.google.com/g/2005#event.confidential";
                break;
            case EventEntry.VISIBILITY_PRIVATE:
                visibilityString = "http://schemas.google.com/g/2005#event.private";
                break;
            case EventEntry.VISIBILITY_PUBLIC:
                visibilityString = "http://schemas.google.com/g/2005#event.public";
                break;
            default:
                visibilityString = "http://schemas.google.com/g/2005#event.default";
        }
        serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "visibility");
        serializer.attribute(null, "value", visibilityString);
        serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "visibility");
    }

    private static void serializeSendEventNotifications(XmlSerializer serializer, boolean sendEventNotifications) throws IOException {
        serializer.startTag(NAMESPACE_GCAL_URI, "sendEventNotifications");
        serializer.attribute(null, "value", sendEventNotifications ? "true" : "false");
        serializer.endTag(NAMESPACE_GCAL_URI, "sendEventNotifications");
    }

    private static void serializeGuestsCanModify(XmlSerializer serializer, boolean guestsCanModify) throws IOException {
        serializer.startTag(NAMESPACE_GCAL_URI, "guestsCanModify");
        serializer.attribute(null, "value", guestsCanModify ? "true" : "false");
        serializer.endTag(NAMESPACE_GCAL_URI, "guestsCanModify");
    }

    private static void serializeGuestsCanInviteOthers(XmlSerializer serializer, boolean guestsCanInviteOthers) throws IOException {
        serializer.startTag(NAMESPACE_GCAL_URI, "guestsCanInviteOthers");
        serializer.attribute(null, "value", guestsCanInviteOthers ? "true" : "false");
        serializer.endTag(NAMESPACE_GCAL_URI, "guestsCanInviteOthers");
    }

    private static void serializeGuestsCanSeeGuests(XmlSerializer serializer, boolean guestsCanSeeGuests) throws IOException {
        serializer.startTag(NAMESPACE_GCAL_URI, "guestsCanSeeGuests");
        serializer.attribute(null, "value", guestsCanSeeGuests ? "true" : "false");
        serializer.endTag(NAMESPACE_GCAL_URI, "guestsCanSeeGuests");
    }

    private static void serializeWho(XmlSerializer serializer, EventEntry entry, Who who) throws IOException, ParseException {
        serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "who");
        String email = who.getEmail();
        if (!StringUtils.isEmpty(email)) {
            serializer.attribute(null, "email", email);
        }
        String value = who.getValue();
        if (!StringUtils.isEmpty(value)) {
            serializer.attribute(null, "valueString", value);
        }
        String rel = null;
        switch(who.getRelationship()) {
            case Who.RELATIONSHIP_NONE:
                break;
            case Who.RELATIONSHIP_ATTENDEE:
                rel = "http://schemas.google.com/g/2005#event.attendee";
                break;
            case Who.RELATIONSHIP_ORGANIZER:
                rel = "http://schemas.google.com/g/2005#event.organizer";
                break;
            case Who.RELATIONSHIP_PERFORMER:
                rel = "http://schemas.google.com/g/2005#event.performer";
                break;
            case Who.RELATIONSHIP_SPEAKER:
                rel = "http://schemas.google.com/g/2005#event.speaker";
                break;
            default:
                throw new ParseException("Unexpected rel: " + who.getRelationship());
        }
        if (!StringUtils.isEmpty(rel)) {
            serializer.attribute(null, "rel", rel);
        }
        String status = null;
        switch(who.getStatus()) {
            case Who.STATUS_NONE:
                break;
            case Who.STATUS_ACCEPTED:
                status = "http://schemas.google.com/g/2005#event.accepted";
                break;
            case Who.STATUS_DECLINED:
                status = "http://schemas.google.com/g/2005#event.declined";
                break;
            case Who.STATUS_INVITED:
                status = "http://schemas.google.com/g/2005#event.invited";
                break;
            case Who.STATUS_TENTATIVE:
                status = "http://schemas.google.com/g/2005#event.tentative";
                break;
            default:
                throw new ParseException("Unexpected status: " + who.getStatus());
        }
        if (!StringUtils.isEmpty(status)) {
            serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "attendeeStatus");
            serializer.attribute(null, "value", status);
            serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "attendeeStatus");
        }
        String type = null;
        switch(who.getType()) {
            case Who.TYPE_NONE:
                break;
            case Who.TYPE_REQUIRED:
                type = "http://schemas.google.com/g/2005#event.required";
                break;
            case Who.TYPE_OPTIONAL:
                type = "http://schemas.google.com/g/2005#event.optional";
                break;
            default:
                throw new ParseException("Unexpected type: " + who.getType());
        }
        if (!StringUtils.isEmpty(type)) {
            serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "attendeeType");
            serializer.attribute(null, "value", type);
            serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "attendeeType");
        }
        serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "who");
    }

    private static void serializeWhen(XmlSerializer serializer, EventEntry entry, When when) throws IOException {
        String startTime = when.getStartTime();
        String endTime = when.getEndTime();
        if (StringUtils.isEmpty(when.getStartTime())) {
            return;
        }
        serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "when");
        serializer.attribute(null, "startTime", startTime);
        if (!StringUtils.isEmpty(endTime)) {
            serializer.attribute(null, "endTime", endTime);
        }
        if (entry.getReminders() != null) {
            Enumeration reminders = entry.getReminders().elements();
            while (reminders.hasMoreElements()) {
                Reminder reminder = (Reminder) reminders.nextElement();
                serializeReminder(serializer, reminder);
            }
        }
        serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "when");
    }

    private static void serializeReminder(XmlSerializer serializer, Reminder reminder) throws IOException {
        serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "reminder");
        byte method = reminder.getMethod();
        String methodStr = null;
        switch(method) {
            case Reminder.METHOD_ALERT:
                methodStr = "alert";
                break;
            case Reminder.METHOD_EMAIL:
                methodStr = "email";
                break;
            case Reminder.METHOD_SMS:
                methodStr = "sms";
                break;
        }
        if (methodStr != null) {
            serializer.attribute(null, "method", methodStr);
        }
        int minutes = reminder.getMinutes();
        if (minutes != Reminder.MINUTES_DEFAULT) {
            serializer.attribute(null, "minutes", Integer.toString(minutes));
        }
        serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "reminder");
    }

    private static void serializeOriginalEvent(XmlSerializer serializer, String originalEventId, String originalEventTime) throws IOException {
        if (StringUtils.isEmpty(originalEventId) || StringUtils.isEmpty(originalEventTime)) {
            return;
        }
        serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "originalEvent");
        int index = originalEventId.lastIndexOf("/");
        if (index != -1) {
            String id = originalEventId.substring(index + 1);
            if (!StringUtils.isEmpty(id)) {
                serializer.attribute(null, "id", id);
            }
        }
        serializer.attribute(null, "href", originalEventId);
        serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "when");
        serializer.attribute(null, "startTime", originalEventTime);
        serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "when");
        serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "originalEvent");
    }

    private static void serializeWhere(XmlSerializer serializer, String where) throws IOException {
        if (StringUtils.isEmpty(where)) {
            return;
        }
        serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "where");
        serializer.attribute(null, "valueString", where);
        serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "where");
    }

    private static void serializeCommentsUri(XmlSerializer serializer, String commentsUri) throws IOException {
        if (commentsUri == null) {
            return;
        }
        serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "feedLink");
        serializer.attribute(null, "href", commentsUri);
        serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "feedLink");
    }

    private static void serializeExtendedProperty(XmlSerializer serializer, String name, String value) throws IOException {
        serializer.startTag(XmlGDataParser.NAMESPACE_GD_URI, "extendedProperty");
        serializer.attribute(null, "name", name);
        serializer.attribute(null, "value", value);
        serializer.endTag(XmlGDataParser.NAMESPACE_GD_URI, "extendedProperty");
    }

    private static void serializeQuickAdd(XmlSerializer serializer, boolean quickAdd) throws IOException {
        if (quickAdd) {
            serializer.startTag(NAMESPACE_GCAL, "quickadd");
            serializer.attribute(null, "value", "true");
            serializer.endTag(NAMESPACE_GCAL, "quickadd");
        }
    }
}
