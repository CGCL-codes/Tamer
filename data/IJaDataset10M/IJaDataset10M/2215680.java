package net.walkingtools.main;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import net.walkingtools.WalkingtoolsInformation;
import net.walkingtools.gpsTypes.*;
import net.walkingtools.gpsTypes.hiperGps.*;
import net.walkingtools.server.gpx.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * Demonstrates how to to assemble and format WaypointContainers and GpxRoutes
 * from programatic data using the HiperWaypoint class (supporting WalkingToolsGpx
 * extensions to GPX.)
 * @author Brett Stalbaum
 * @version 0.1.1
 * @since 0.0.4
 */
public class HiperGpxFromScratchDriver {

    private static final String[] audio = { "eastcrk.wav", "deadb.wav", "elcap.wav", "goose.wav" };

    private static final String[] images = { "pondscum.png", "deadbird.png", "elcap.png", "goose.png" };

    private static final String[] video = { "not.mpg", "in.mpg", "samples.mpg", "yet.mpg" };

    private static final String[] alternateEvents = { "creekdance.js", "deadbird.js", "?sciptAsset=392nkj3i234", "?sciptAsset=goose.js" };

    /**
     * Demonstrates how to to assemble WaypointContainers and GpxRoutes
     * from programatic data and produce .gpx data. Compare this example to
     * GpxFromScratchDriver for the differences, which are not many!
     * @param args no used
     */
    public static void main(String[] args) {
        Vector<GpxWaypoint> wptVector = new Vector<GpxWaypoint>();
        final GpxFormatter formatter = new GpxFormatter(false);
        boolean visited = false;
        for (int i = 0; i < 4; i++) {
            HiperWaypoint temp = new HiperWaypoint(i / 1000d, i / 1000d, 0, "wpt" + i, i * 10 + 10);
            temp.setAgeOfDgpsData(i);
            temp.setDescription("my fake waypoint " + i);
            temp.setDgpsId(196 + i);
            temp.setGpsComment("my fake waypoint comment");
            temp.setGpxFixType(GpxFixType.FixType.NONE);
            temp.setSat(21 + i);
            temp.setHdop(22 + i);
            temp.setVdop(23 + i);
            temp.setPdop(24 + i);
            temp.setLink("info about this waypoint", "text/html", WalkingtoolsInformation.projectUrl);
            temp.setMagvar(i);
            temp.setSrc("Garmin Foretrex 101");
            temp.setType("type");
            temp.setSymbol("Flag");
            temp.setVisited(visited = !visited);
            HiperGpxExtensionType.AudioType audioType = temp.getAudioType();
            audioType.setFileName(audio[i]);
            audioType.setUri("http:// perhaps the location of a file to load");
            audioType.setDuration(40000 * i);
            audioType.setRepeatDuration(3000 * i);
            audioType.setStartDelay(5000 * i);
            audioType.setMimetype("audio/x-wav");
            audioType.setAudioVolume(2.0 * i);
            audioType.setAudioPan(2 * i);
            audioType.setStereo((i % 2 == 0) ? true : false);
            audioType.setLanguage("por");
            HiperGpxExtensionType.ImageType imageType = temp.getImageType();
            imageType.setFileName(images[i]);
            imageType.setUri("http://www.google.com/maps?q=&ll=" + i / 1000d + "," + i / 1000d);
            imageType.setDuration(40000 * i);
            imageType.setStartDelay(5000 * i);
            imageType.setMimetype("audio/x-wav");
            HiperGpxExtensionType.VideoType videoType = temp.getVideoType();
            videoType.setFileName(video[i]);
            videoType.setUri("http://perhaps a url providing further info, or ?");
            videoType.setDuration(40000 * i);
            videoType.setRepeatDuration(3000 * i);
            videoType.setStartDelay(5000 * i);
            videoType.setMimetype("video/mpeg");
            videoType.setLanguage("eng");
            HiperGpxExtensionType.AlternateEventType aletnateEventType = temp.getAlternateEventType();
            aletnateEventType.setFileName(alternateEvents[i]);
            aletnateEventType.setUri("http://perhaps a script file, trigger, soap call");
            aletnateEventType.setDuration(40000 * i);
            aletnateEventType.setRepeatDuration(3000 * i);
            aletnateEventType.setStartDelay(5000 * i);
            aletnateEventType.setMimetype("application/javascript");
            aletnateEventType.setCode("some executable code, instruction, etc.");
            GpxExtensionType type = new GpxExtensionType() {

                @Override
                public void formatExtension(Element extensions) {
                    Document doc = formatter.getDocument();
                    Element ele = doc.createElement("requiredNamespace:forThisExample");
                    ele.setAttribute("xmlns:requiredNamespace", "thisIsFakeSecondExtensionForExamplePurposesOnly");
                    Element subele = doc.createElement("test");
                    subele.appendChild(doc.createTextNode("test"));
                    ele.appendChild(subele);
                    extensions.appendChild(ele);
                }
            };
            wptVector.add(temp);
        }
        WaypointContainer wpts = WaypointContainer.makeWaypointContainer(wptVector);
        wpts.setGpsDataFormatter(formatter);
        System.out.println("The program constructed waypoint list:\n");
        System.out.println(wpts);
        GpxRoute route = GpxRoute.makeRoute(wptVector, "test route");
        route.setName("metadata name");
        route.setDescription("description in metadata");
        route.setAuthor("Brett Stalbaum");
        route.setLink("info about this route file", "text/html", WalkingtoolsInformation.projectUrl);
        route.setTime(System.currentTimeMillis());
        route.setKeywords("example, sample, test, demo");
        route.setRouteName("Line in the Atlantic");
        route.setRouteComment("A comment about the route");
        route.setRouteDescription("A route in the Atlantic");
        route.setRouteSrc("Source info about the data");
        route.setRouteLink("info about this route", "text/html", WalkingtoolsInformation.projectUrl);
        route.setRouteNumber(2);
        route.setRouteType("type string");
        route.setGpsDataFormatter(formatter);
        System.out.println("\nThe program constructed route:\n");
        System.out.println(route);
        try {
            String newlyProduced = route.toString();
            String newlyParsed = null;
            Vector<GpxExtensionParser> extParsers = new Vector<GpxExtensionParser>(2);
            extParsers.add(new HiperExtensionParser());
            GpxParser parser = new GpxParser(false, extParsers);
            parser.setGpxString(newlyProduced);
            GpsTypeContainer newRoute = parser.getGpsTypeContainer();
            newRoute.setGpsDataFormatter(formatter);
            newlyParsed = newRoute.toString();
            System.out.println("\nthe re-parsed route:\n");
            System.out.println(newlyParsed);
            System.out.print("The reparsed gpx is equal to the original programatically constructed gpx: ");
            System.out.println(newlyParsed.equals(newlyProduced));
            System.out.println();
        } catch (IOException ex) {
            Logger.getLogger(HiperGpxFromScratchDriver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(HiperGpxFromScratchDriver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(HiperGpxFromScratchDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
