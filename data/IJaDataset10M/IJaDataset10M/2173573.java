package org.kablink.teaming.remoting.ws.service.ical;

public interface IcalService {

    public void ical_uploadCalendarEntriesWithXML(String accessToken, long folderId, String iCalDataAsXML);
}
