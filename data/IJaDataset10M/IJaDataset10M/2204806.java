package com.gcapmedia.dab.epg.xml;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.testng.annotations.Test;
import com.gcapmedia.dab.epg.*;

public class ScheduleMarshallerTest {

    @Test
    public void marshall() throws URISyntaxException, UnsupportedEncodingException, MalformedURLException, MarshallException {
        Epg epg = new Epg();
        Schedule schedule = new Schedule();
        epg.setSchedule(schedule);
        Scope scope = new Scope(new DateTime("2001-03-01T00:00:00"), new DateTime("2001-03-02T18:00:00"));
        scope.addService(new ContentId("e1.ce15.c221.0"));
        scope.addService(new ContentId("e1.ce15.c224.0"));
        schedule.setScope(scope);
        Programme programme = new Programme(new ShortCrid(213456));
        schedule.addProgramme(programme);
        programme.setId(new Crid("bbc.co.uk", "4969758988"));
        programme.setRecommendation(Recommendation.YES);
        programme.getNames().addMediumName("Gilles Peterson");
        programme.getNames().addLongName("Gilles Peterson: Worldwide");
        Location location = new Location();
        DateTime programmeTime = new DateTime("2003-12-18T00:00:00");
        location.addTime(new Time(programmeTime, new Period("PT2H"), new DateTime("2003-12-18T00:00:00"), new Period("PT2H")));
        location.addBearer(new Bearer(new ContentId("e1.ce15.c221.0")));
        programme.addLocation(location);
        programme.getMedia().addShortDescription("Gilles Peterson brings you two hours of global beats and the best of cool. Including the Worldwide family. KV5 are live from Maida Value with special guests.");
        programme.addGenre(new Genre(Genre.Scheme.CONTENT, 6, 7, 0, "Rap/Hip Hop/Reggae"));
        programme.addGenre(new Genre(Genre.Scheme.CONTENT, 6, 8, 0, "Electronic/Club/Urban/Dance"));
        programme.addGenre(new Genre(Genre.Scheme.FORMAT, 5, 0, 0, "ARTISTIC PERFORMANCE"));
        programme.addGenre(new Genre(Genre.Scheme.INTENTION, 1, 0, 0, "ENTERTAINMENT"));
        programme.addGenre(new Genre(Genre.Scheme.CONTENT, 6, 9, 0, "World/Traditional/Ethnic/Folk music"));
        Membership membership = new Membership(new ShortCrid(1000));
        membership.setCrid(new Crid("www.bbc.co.uk", "WorldwideGroup"));
        programme.addMembership(membership);
        Link mailLink = new Link(new URL("mailto:gilles.peterson@bbc.co.uk"));
        mailLink.setDescription("Email:");
        programme.addLink(mailLink);
        Link webLink = new Link(new URL("http://www.bbc.co.uk/radio1/urban/peterson"));
        webLink.setDescription("Web:");
        programme.addLink(webLink);
        ProgrammeEvent event = new ProgrammeEvent(new ShortCrid(6353));
        event.setRecommendation(Recommendation.YES);
        event.setId(new Crid("www.bbc.co.uk;dab", "BC81123456a"));
        event.getNames().addShortName("Herbert");
        event.getNames().addMediumName("Herbert Live");
        event.getNames().addLongName("Live session from Herbert");
        Location eventLocation = new Location();
        eventLocation.addRelativeTime(new RelativeTime(programmeTime, new Period("PT45M"), new Period("PT15M")));
        event.addLocation(eventLocation);
        event.getMedia().addShortDescription("Live session from Herbert, recorded at Cargo on 24/2/01");
        programme.addEvent(event);
        EpgXmlMarshaller marshaller = new EpgXmlMarshaller();
        System.out.println(new String(marshaller.marshall(epg)));
    }
}
