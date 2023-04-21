package com.gcapmedia.dab.epg.xml;

import java.io.*;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.gcapmedia.dab.epg.*;

/**
 * Marshalls an EPG to/from XML
 */
public class XmlMarshaller implements Marshaller {

    /**
	 * Serial version
	 */
    private static final long serialVersionUID = 3823353646420716647L;

    /**
	 * @see com.gcapmedia.dab.epg.Marshaller#marshall(com.gcapmedia.dab.epg.Epg)
	 */
    public byte[] marshall(Epg epg) throws MarshallException {
        byte[] bytes = null;
        Document doc = buildDocument(epg);
        try {
            Source source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setAttribute("indent-number", new Integer(4));
            Transformer transformer = factory.newTransformer();
            StreamResult result = new StreamResult(writer);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, result);
            String markup = writer.getBuffer().toString();
            bytes = markup.getBytes();
        } catch (Exception e) {
            throw new MarshallException("Error marshalling XML data", e);
        }
        return bytes;
    }

    /**
	 * Marshalls an EPG to an XML string adherent to the ISO/IEC 10646 character
	 * set and using UTF-8 encoding.
	 * @throws UnsupportedEncodingException 
	 * @throws MarshallException 
	 */
    public String marshallToXmlString(Epg epg) throws UnsupportedEncodingException, MarshallException {
        return new String(marshall(epg), "ISO-8859-2");
    }

    /**
	 * @see com.gcapmedia.dab.epg.Marshaller#unmarshall(byte[])
	 */
    public Epg unmarshall(byte[] bytes) {
        return null;
    }

    /**
	 * Unmarshall a document located at the specified URL. This should use the
	 * ISO/IEC 10646 character set and be encoded in UTF-8.
	 * @throws IOException 
	 */
    public Epg unmarshallFromUrl(URL url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String tmp = null;
        StringBuilder buffer = new StringBuilder();
        while ((tmp = reader.readLine()) != null) {
            buffer.append(tmp);
        }
        return unmarshall(buffer.toString().getBytes());
    }

    /**
	 * Unmarshall an EPG from a file. This should use the
	 * ISO/IEC 10646 character set and be encoded in UTF-8.
	 * @throws IOException 
	 */
    public Epg unmarshallFromFile(File file) throws IOException {
        return unmarshallFromUrl(file.toURL());
    }

    /**
	 * @param epg
	 * @return
	 */
    private Document buildDocument(Epg epg) {
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        Document doc = builder.newDocument();
        Element epgElement = doc.createElement("epg");
        doc.appendChild(epgElement);
        epgElement.setAttribute("system", epg.getSystemType().name());
        if (epg.getSchedule() != null) {
            Schedule schedule = epg.getSchedule();
            Element scheduleElement = buildSchedule(schedule, epgElement, doc);
            epgElement.appendChild(scheduleElement);
        }
        return doc;
    }

    /**
	 * builds the <schedule> element
	 */
    private Element buildSchedule(Schedule schedule, Element parent, Document doc) {
        Element scheduleElement = doc.createElement("schedule");
        if (schedule.getVersion() > 0) {
            scheduleElement.setAttribute("version", "" + schedule.getVersion());
        }
        if (schedule.getCreated() != null) {
            scheduleElement.setAttribute("creationTime", schedule.getCreated().toString());
        }
        if (schedule.getOriginator() != null) {
            scheduleElement.setAttribute("originator", schedule.getOriginator());
        }
        if (schedule.getScope() != null) {
            Scope scope = schedule.getScope();
            Element scopeElement = buildScope(scope, scheduleElement, doc);
            scheduleElement.appendChild(scopeElement);
        }
        for (Programme programme : schedule.getProgrammes()) {
            Element programmeElement = buildProgramme(programme, scheduleElement, doc);
            scheduleElement.appendChild(programmeElement);
        }
        return scheduleElement;
    }

    /**
	 * builds a <programme> element
	 */
    private Element buildProgramme(Programme programme, Element scheduleElement, Document doc) {
        Element programmeElement = doc.createElement("programme");
        programmeElement.setAttribute("shortId", "" + programme.getShortId());
        if (programme.getId() != null) {
            programmeElement.setAttribute("id", programme.getId().toString());
        }
        if (programme.getRecommendation() != null && programme.getRecommendation() != Recommendation.NO) {
            programmeElement.setAttribute("recommendation", programme.getRecommendation().value());
        }
        if (programme.getBroadcastFlag() != null && programme.getBroadcastFlag() != BroadcastFlag.ON_AIR) {
            programmeElement.setAttribute("broadcast", programme.getBroadcastFlag().value());
        }
        NameGroup names = programme.getNames();
        for (ShortName shortName : names.getShortNames()) {
            Element shortNameElement = doc.createElement("shortName");
            shortNameElement.setAttribute("xml:lang", shortName.getLanguage());
            shortNameElement.setTextContent(shortName.getText());
            programmeElement.appendChild(shortNameElement);
        }
        for (MediumName mediumName : names.getMediumNames()) {
            Element mediumNameElement = doc.createElement("mediumName");
            mediumNameElement.setAttribute("xml:lang", mediumName.getLanguage());
            mediumNameElement.setTextContent(mediumName.getText());
            programmeElement.appendChild(mediumNameElement);
        }
        for (LongName longName : names.getLongNames()) {
            Element longNameElement = doc.createElement("longName");
            longNameElement.setAttribute("xml:lang", longName.getLanguage());
            longNameElement.setTextContent(longName.getText());
            programmeElement.appendChild(longNameElement);
        }
        for (Location location : programme.getLocations()) {
            Element locationElement = buildLocation(location, programmeElement, doc);
            programmeElement.appendChild(locationElement);
        }
        MediaGroup media = programme.getMedia();
        Element mediaElement = buildMedia(media, doc);
        programmeElement.appendChild(mediaElement);
        for (Genre genre : programme.getGenres()) {
            Element genreElement = buildGenre(genre, programmeElement, doc);
            programmeElement.appendChild(genreElement);
        }
        for (Membership membership : programme.getMemberships()) {
            Element membershipElement = buildMembership(membership, programmeElement, doc);
            programmeElement.appendChild(membershipElement);
        }
        for (Link link : programme.getLinks()) {
            Element linkElement = buildLink(link, programmeElement, doc);
            programmeElement.appendChild(linkElement);
        }
        if (programme.getKeywords() != null) {
            Element keywordsElement = buildKeywords(programme.getKeywords(), programmeElement, doc);
            programmeElement.appendChild(keywordsElement);
        }
        for (ProgrammeEvent event : programme.getEvents()) {
            Element eventElement = buildEvent(event, programmeElement, doc);
            programmeElement.appendChild(eventElement);
        }
        return programmeElement;
    }

    /**
	 * builds the <programmeEvent> element
	 */
    private Element buildEvent(ProgrammeEvent event, Element programmeElement, Document doc) {
        Element eventElement = doc.createElement("programmeEvent");
        eventElement.setAttribute("shortId", "" + event.getShortId());
        if (event.getId() != null) {
            programmeElement.setAttribute("id", event.getId().toString());
        }
        if (event.getRecommendation() != null && event.getRecommendation() != Recommendation.NO) {
            programmeElement.setAttribute("recommendation", event.getRecommendation().value());
        }
        if (event.getBroadcastFlag() != null && event.getBroadcastFlag() != BroadcastFlag.ON_AIR) {
            programmeElement.setAttribute("broadcast", event.getBroadcastFlag().value());
        }
        NameGroup names = event.getNames();
        for (ShortName shortName : names.getShortNames()) {
            Element shortNameElement = doc.createElement("shortName");
            shortNameElement.setAttribute("xml:lang", shortName.getLanguage());
            shortNameElement.setTextContent(shortName.getText());
            eventElement.appendChild(shortNameElement);
        }
        for (MediumName mediumName : names.getMediumNames()) {
            Element mediumNameElement = doc.createElement("mediumName");
            mediumNameElement.setAttribute("xml:lang", mediumName.getLanguage());
            mediumNameElement.setTextContent(mediumName.getText());
            eventElement.appendChild(mediumNameElement);
        }
        for (LongName longName : names.getLongNames()) {
            Element longNameElement = doc.createElement("longName");
            longNameElement.setAttribute("xml:lang", longName.getLanguage());
            longNameElement.setTextContent(longName.getText());
            eventElement.appendChild(longNameElement);
        }
        for (Location location : event.getLocations()) {
            Element locationElement = buildLocation(location, programmeElement, doc);
            eventElement.appendChild(locationElement);
        }
        MediaGroup media = event.getMedia();
        Element mediaElement = buildMedia(media, doc);
        eventElement.appendChild(mediaElement);
        for (Genre genre : event.getGenres()) {
            Element genreElement = buildGenre(genre, programmeElement, doc);
            eventElement.appendChild(genreElement);
        }
        for (Membership membership : event.getMemberships()) {
            Element membershipElement = buildMembership(membership, programmeElement, doc);
            eventElement.appendChild(membershipElement);
        }
        for (Link link : event.getLinks()) {
            Element linkElement = buildLink(link, programmeElement, doc);
            eventElement.appendChild(linkElement);
        }
        return eventElement;
    }

    /**
	 * builds the <keywords> element
	 */
    private Element buildKeywords(Keywords keywords, Element programmeElement, Document doc) {
        Element keywordsElement = doc.createElement("keywords");
        keywordsElement.setAttribute("xml:lang", keywords.getLanguage());
        String words = keywords.getKeywords().toString();
        words = StringUtils.removeStart(words, "[");
        words = StringUtils.removeEnd(words, "]");
        CDATASection data = doc.createCDATASection(words);
        keywordsElement.appendChild(data);
        return keywordsElement;
    }

    /**
	 * builds the <link> element
	 */
    private Element buildLink(Link link, Element programmeElement, Document doc) {
        Element linkElement = doc.createElement("link");
        linkElement.setAttribute("url", link.getUrl().toString());
        if (link.getMimeType() != null) {
            linkElement.setAttribute("mimeType", link.getMimeType());
        }
        if (link.getDescription() != null) {
            linkElement.setAttribute("description", link.getDescription());
        }
        if (link.getLanguage() != null) {
            linkElement.setAttribute("xml:lang", link.getLanguage());
        }
        if (link.getExpiryTime() != null) {
            linkElement.setAttribute("expiryTime", link.getExpiryTime().toString());
        }
        return linkElement;
    }

    /**
	 * builds the <memberOf> element
	 */
    private Element buildMembership(Membership membership, Element programmeElement, Document doc) {
        Element memberOfElement = doc.createElement("memberOf");
        memberOfElement.setAttribute("shortId", "" + membership.getShortId());
        if (membership.getCrid() != null) {
            memberOfElement.setAttribute("id", membership.getCrid().toString());
        }
        if (membership.getIndex() > 0) {
            memberOfElement.setAttribute("shortId", "" + membership.getIndex());
        }
        return memberOfElement;
    }

    /**
	 * builds the <genre> element
	 */
    private Element buildGenre(Genre genre, Element programmeElement, Document doc) {
        Element genreElement = doc.createElement("genre");
        genreElement.setAttribute("href", genre.toString());
        if (genre.getType() != null && genre.getType() != Genre.Type.MAIN) {
            genreElement.setAttribute("type", genre.getType().toString());
        }
        if (genre.getDefinition() != null) {
            Element nameElement = doc.createElement("doc");
            CDATASection data = doc.createCDATASection(genre.getDefinition());
            nameElement.appendChild(data);
            genreElement.appendChild(nameElement);
        }
        return genreElement;
    }

    /**
	 * builds the <mediaDescription> element
	 */
    private Element buildMedia(MediaGroup media, Document doc) {
        Element mediaElement = doc.createElement("mediaDescription");
        for (ShortDescription shortDescription : media.getShortDescriptions()) {
            Element shortDescriptionElement = doc.createElement("shortDescription");
            shortDescriptionElement.setAttribute("xml:lang", shortDescription.getLanguage());
            CDATASection data = doc.createCDATASection(shortDescription.getText());
            shortDescriptionElement.appendChild(data);
            mediaElement.appendChild(shortDescriptionElement);
        }
        for (LongDescription longDescription : media.getLongDescriptions()) {
            Element longDescriptionElement = doc.createElement("longDescription");
            longDescriptionElement.setAttribute("xml:lang", longDescription.getLanguage());
            CDATASection data = doc.createCDATASection(longDescription.getText());
            longDescriptionElement.appendChild(data);
            mediaElement.appendChild(longDescriptionElement);
        }
        for (Multimedia multimedia : media.getMultimedia()) {
            Element multimediaElement = doc.createElement("multimedia");
            multimediaElement.setAttribute("url", multimedia.getUrl().toString());
            if (multimedia.getType() != null) {
                multimediaElement.setAttribute("type", multimedia.getType().toString());
            }
            if (multimedia.getMimeType() != null) {
                multimediaElement.setAttribute("mimeValue", multimedia.getMimeType());
            }
            if (multimedia.getHeight() > 0) {
                multimediaElement.setAttribute("height", "" + multimedia.getHeight());
            }
            if (multimedia.getWidth() > 0) {
                multimediaElement.setAttribute("width", "" + multimedia.getWidth());
            }
            mediaElement.appendChild(multimediaElement);
        }
        return mediaElement;
    }

    /**
	 * builds the <location> element
	 */
    private Element buildLocation(Location location, Element parent, Document doc) {
        Element locationElement = doc.createElement("location");
        for (Time time : location.getTimes()) {
            Element timeElement = doc.createElement("time");
            timeElement.setAttribute("time", time.getBilledTime().toString());
            timeElement.setAttribute("duration", time.getBilledDuration().toString());
            if (time.getActualTime() != null) {
                timeElement.setAttribute("actualTime", time.getActualTime().toString());
            }
            if (time.getActualDuration() != null) {
                timeElement.setAttribute("actualDuration", time.getActualDuration().toString());
            }
            locationElement.appendChild(timeElement);
        }
        for (RelativeTime time : location.getRelativeTimes()) {
            Element timeElement = doc.createElement("relativeTime");
            timeElement.setAttribute("time", time.getBilledTime().toString());
            timeElement.setAttribute("duration", time.getBilledDuration().toString());
            if (time.getActualTime() != null) {
                timeElement.setAttribute("actualTime", time.getActualTime().toString());
            }
            if (time.getActualDuration() != null) {
                timeElement.setAttribute("actualDuration", time.getActualDuration().toString());
            }
            locationElement.appendChild(timeElement);
        }
        for (Bearer bearer : location.getBearers()) {
            Element bearerElement = doc.createElement("bearer");
            bearerElement.setAttribute("id", bearer.getId().toString());
            locationElement.appendChild(bearerElement);
        }
        return locationElement;
    }

    /**
	 * builds the <scope> element
	 */
    private Element buildScope(Scope scope, Element parent, Document doc) {
        Element scopeElement = doc.createElement("scope");
        scopeElement.setAttribute("startTime", scope.getStartTime().toString());
        scopeElement.setAttribute("stopTime", scope.getStopTime().toString());
        for (ContentId id : scope.getServices()) {
            Element serviceElement = doc.createElement("serviceScope");
            serviceElement.setAttribute("id", id.toString());
            scopeElement.appendChild(serviceElement);
        }
        return scopeElement;
    }
}
