package saiboten.actions;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import saiboten.ChannelAction;
import saiboten.Place;
import saiboten.Places;
import saiboten.SAXWeatherReport;

public class GetWeatherAction implements ChannelAction {

    Map<String, Place> places;

    public GetWeatherAction() {
        places = Places.getPlaces();
    }

    @Override
    public List<String> action(List<String> results, String channel, String sender, String message) {
        if (message.toLowerCase().startsWith("!weather")) {
            try {
                getWeatherReport(results, channel, places.get(message.split(" ")[1].toLowerCase()));
            } catch (Exception u) {
                results.add("Fant ikke sted. :(");
            }
        }
        return results;
    }

    private void getWeatherReport(List<String> results, String channel, Place place) {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(false);
            SAXParser saxParser = spf.newSAXParser();
            InputSource input = new InputSource("http://api.yr.no/weatherapi/locationforecast/1.7/?lat=" + place.lat + ";lon=" + place.lon);
            SAXWeatherReport handler = new SAXWeatherReport();
            saxParser.parse(input, handler);
            results.add(place.name + ": " + handler.getResult() + " V�rdata levert av yr.no");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
