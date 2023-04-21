package com.flightspy.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.flightspy.constants.ParserConstants;
import com.flightspy.utility.StringParser;
import com.flightspy.parser.interfaces.FlightParser;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.Node;
import com.meterware.httpunit.WebResponse;
import com.flightspy.beans.Flight;

public class RyanairFlightParser implements FlightParser {

    public List parse(WebResponse response) throws IOException {
        ArrayList flights = new ArrayList();
        try {
            Parser parser = null;
            try {
                parser = Parser.createParser(response.getText(), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            NodeList list = parser.parse(null);
            NodeList nodeList = new NodeList();
            NodeFilter filterPriceFirst = new CssSelectorNodeFilter(ParserConstants.PRICE_FIRST);
            NodeFilter filterPriceAll = new CssSelectorNodeFilter(ParserConstants.PRICE_ALL);
            NodeFilter filterDateTimeFlight = new CssSelectorNodeFilter(ParserConstants.DATE_TIME_FLIGHTNUM_AIRPORT);
            NodeFilter filter = new OrFilter(new NodeFilter[] { filterPriceFirst, filterPriceAll, filterDateTimeFlight });
            nodeList = list.extractAllNodesThatMatch(filter, true);
            int accessFor = 0;
            int count = 0;
            Flight flight = null;
            for (int i = 0; i < nodeList.size(); i++) {
                Node n = (Node) nodeList.elementAt(i);
                String nodeValue = n.getText();
                if ((accessFor == 0 && nodeValue.equals(ParserConstants.PRICE_FIRST_NODE))) {
                    accessFor++;
                    flight = new Flight();
                    Node nSibling = n.getNextSibling();
                    flight.setPrice(StringParser.priceParsing(nSibling.getText()));
                } else if (nodeValue.equals(ParserConstants.PRICE_ALL_NODE)) {
                    flight = new Flight();
                    count = 0;
                    Node nSibling = n.getNextSibling();
                    flight.setPrice(StringParser.priceParsing(nSibling.getText()));
                } else if ((accessFor != 0 && nodeValue.equals(ParserConstants.DATE_TIME_FLIGHTNUM_AIRPORT_NODE))) {
                    Node nSibling = n.getNextSibling();
                    String nSiblingValue = nSibling.getText();
                    if (nSiblingValue.equals(ParserConstants.B)) {
                        Node nSiblingSecond = nSibling.getNextSibling();
                        String date = nSiblingSecond.getText();
                        Node nSiblingForth = nSiblingSecond.getNextSibling().getNextSibling();
                        flight.setDate(StringParser.dateAndTimeParsing(date + nSiblingForth.getText()));
                        Node nSiblingSixth = nSiblingForth.getNextSibling().getNextSibling();
                        flight.setFlight_number(StringParser.flightNumberParsing(nSiblingSixth.getText()));
                    } else {
                        if (count == 0) {
                            flight.setDeparture_time(StringParser.dateAndTimeParsing(nSibling.getText()));
                            count++;
                        } else if ((count == 1)) {
                            flight.setDeparture_airport(StringParser.airportParsing(nSibling.getText()));
                            count++;
                        } else if (count == 2) {
                            flight.setArrival_time(StringParser.dateAndTimeParsing(nSibling.getText()));
                            count++;
                        } else if (count == 3) {
                            flight.setArrival_airport(StringParser.airportParsing(nSibling.getText()));
                            count++;
                            flights.add(flight);
                        }
                    }
                }
            }
        } catch (ParserException pe) {
            System.out.println("parsing error occured");
            System.out.println(pe.getMessage());
        }
        return flights;
    }
}
