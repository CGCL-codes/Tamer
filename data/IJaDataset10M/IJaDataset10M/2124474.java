package flightstats;

import java.io.FileWriter;
import java.io.IOException;
import processing.core.PApplet;
import prohtml.Element;
import prohtml.HtmlList;
import prohtml.StandAloneElement;
import prohtml.Url;

public class LoadAiports extends PApplet {

    HtmlList site;

    FileWriter airportsFile = null;

    public void setup() {
        try {
            airportsFile = new FileWriter(sketchPath("data/airports_flightstats2.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseCountryList("http://www.flightstats.com/go/Airport/airportsOfTheWorld.do");
    }

    private void parseCountryList(final String i_link) {
        HtmlList site = new HtmlList(i_link);
        println();
        for (int i = 0; i < site.getNumberOfElements(); i++) {
            Element element = site.getElement(i);
            if (element.getElement().equals("a")) {
                StandAloneElement link = (StandAloneElement) element;
                if (link.getAttribute("href").contains("/go/Airport/airportsOfTheWorld.do?countryCode")) {
                    parseAirportList("http://www.flightstats.com" + link.getAttribute("href"));
                }
            }
        }
    }

    private String countryCode = "";

    private String stateCode = "";

    private boolean afterStartCountry = false;

    private boolean afterStartState = false;

    private boolean afterStartAirport = false;

    private String startCountry = "US";

    private String startState = "TX";

    private String startAirport = "MGI";

    private void parseAirportList(final String i_link) {
        Url url = new Url("", i_link);
        countryCode = url.getValue("countryCode");
        if (countryCode.equals(startCountry)) {
            afterStartCountry = true;
            println("aftercountry");
        }
        if (!afterStartCountry) {
            return;
        }
        if (url.hasParameter("stateCode")) {
            stateCode = url.getValue("stateCode");
        } else {
            if (countryCode.equals("US")) {
                parseCountryList(i_link);
                return;
            }
            stateCode = "  ";
        }
        if (stateCode.equals(startState)) {
            afterStartState = true;
            println("afterstartstate");
        }
        if (!afterStartState) {
            return;
        }
        println(countryCode);
        HtmlList site = new HtmlList(i_link);
        for (int i = 0; i < site.getNumberOfElements(); i++) {
            Element element = site.getElement(i);
            if (element.getElement().equals("a")) {
                StandAloneElement link = (StandAloneElement) element;
                if (link.getAttribute("href").contains("/go/Airport/airportDetails.do?airportCode")) {
                    parseAirport("http://www.flightstats.com" + link.getAttribute("href"));
                }
            }
        }
    }

    private String airportCode = "";

    private void parseAirport(final String i_link) {
        Url url = new Url("", i_link);
        airportCode = url.getValue("airportCode");
        if (airportCode.equals(startAirport)) {
            afterStartAirport = true;
            println("afterstart airport");
        }
        if (!afterStartAirport) {
            return;
        }
        HtmlList site = new HtmlList(i_link);
        StringBuffer line = new StringBuffer();
        for (int i = 0; i < site.getNumberOfElements(); i++) {
            Element element = site.getElement(i);
            try {
                if (element.getElement().equals("Airport:")) {
                    line.append(countryCode + "#" + stateCode + "#" + airportCode + "#");
                    line.append(site.getElement(i + 8).getElement().substring(3) + "#");
                }
                if (element.getElement().equals("City:")) {
                    String city = site.getElement(i + 5).getElement();
                    line.append(city.substring(0, city.length() - 2) + "#");
                }
                if (element.getElement().equals("Latitude:")) {
                    line.append(site.getElement(i + 5).getElement() + "#");
                }
                if (element.getElement().equals("Longitude:")) {
                    line.append(site.getElement(i + 5).getElement() + "#");
                }
                if (element.getElement().equals("Time Zone:")) {
                    line.append(site.getElement(i + 5).getElement() + "\n");
                    println(line.toString());
                    airportsFile.write(line.toString());
                    airportsFile.flush();
                }
            } catch (Exception e) {
                println("could not write:" + countryCode + "#" + stateCode + "#" + airportCode);
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { LoadAiports.class.getName() });
    }
}
