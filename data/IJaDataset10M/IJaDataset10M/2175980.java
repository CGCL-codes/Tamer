package org.travelfusion.xmlclient.ri.handler.plane;

import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.stream.XMLStreamConstants;
import org.travelfusion.xmlclient.handler.HandlesRequestsFor;
import org.travelfusion.xmlclient.impl.handler.AbstractXmlToolRequestStAXResponseHandler;
import org.travelfusion.xmlclient.ri.xobject.plane.XCheckRoutingRequest;
import org.travelfusion.xmlclient.ri.xobject.plane.XCheckRoutingResponse;
import org.travelfusion.xmlclient.ri.xobject.plane.XFlight;
import org.travelfusion.xmlclient.ri.xobject.plane.XRouterSummary;

/**
 * @author Jesse McLaughlin (nzjess@gmail.com)
 * @author Dogan Narinc (dogan.narinc@gmail.com)
 */
@HandlesRequestsFor(XCheckRoutingRequest.class)
public class CheckRoutingHandler extends AbstractXmlToolRequestStAXResponseHandler<XCheckRoutingRequest, XCheckRoutingResponse> {

    private final RouterSummarySubHandler routerSummaryHandler;

    private final FlightSubHandler flightHandler;

    public CheckRoutingHandler() {
        this.routerSummaryHandler = new RouterSummarySubHandler(this);
        this.flightHandler = new FlightSubHandler(this);
    }

    @Override
    protected void fillTemplate() throws Exception {
        template.gotoChild().gotoChild("RoutingId").addText(request.getRoutingId());
    }

    private static enum State {

        ROOT, ROUTERS, GROUPS, OUTWARD_FLIGHTS, RETURN_FLIGHTS, GROUP_PRICE, GROUP_TAX_ITEMS, SUMMARY, SUMMARY_AIRPORTS, SUMMARY_AIRPORTS_CITY, SUMMARY_STATIONS_LIST, SUMMARY_STATION, SUMMARY_STATION_CITY
    }

    public XCheckRoutingResponse handleResponse() throws Exception {
        XCheckRoutingResponse response = new XCheckRoutingResponse();
        response.setOutwardFlights(new ArrayList<XFlight>());
        response.setReturnFlights(new ArrayList<XFlight>());
        response.setRouterSummaries(new ArrayList<XRouterSummary>());
        response.setAirportNames(new HashMap<String, String>());
        response.setStationNames(new HashMap<String, String>());
        StringBuilder builder = new StringBuilder();
        String currentName = null;
        boolean build = false;
        XRouterSummary routerSummary = null;
        int groupIndexOutwardStart = 0;
        int groupIndexReturnStart = 0;
        String groupId = null;
        int groupPrice = -1;
        String groupCurrency = null;
        boolean groupTaxIncluded = false;
        String airportOrStationCode = null;
        String airportOrStationName = null;
        State state = State.ROOT;
        for (int event; ((event = responseReader.next()) != XMLStreamConstants.END_DOCUMENT); ) {
            switch(event) {
                case XMLStreamConstants.START_ELEMENT:
                    currentName = responseReader.getName().getLocalPart();
                    builder.setLength(0);
                    switch(state) {
                        case ROOT:
                            if ("RoutingId".equals(currentName)) {
                                build = true;
                            } else if ("RouterList".equals(currentName)) {
                                state = State.ROUTERS;
                            } else if ("Summary".equals(currentName)) {
                                state = State.SUMMARY;
                            }
                            break;
                        case ROUTERS:
                            if ("Router".equals(currentName)) {
                                routerSummary = routerSummaryHandler.handleResponse();
                                response.getRouterSummaries().add(routerSummary);
                                if (routerSummary.isComplete()) {
                                    response.setCompletedRouterCount(response.getCompletedRouterCount() + 1);
                                    state = State.GROUPS;
                                }
                            }
                            break;
                        case GROUPS:
                            if ("Id".equals(currentName)) {
                                build = true;
                            } else if ("OutwardList".equals(currentName)) {
                                state = State.OUTWARD_FLIGHTS;
                                groupIndexOutwardStart = response.getOutwardFlights().size();
                            } else if ("ReturnList".equals(currentName)) {
                                state = State.RETURN_FLIGHTS;
                                groupIndexReturnStart = response.getReturnFlights().size();
                            } else if ("Price".equals(currentName)) {
                                state = State.GROUP_PRICE;
                                groupPrice = -1;
                            }
                            break;
                        case OUTWARD_FLIGHTS:
                            if ("Outward".equals(currentName)) {
                                XFlight flight = flightHandler.handleResponse();
                                flight.setGroupId(groupId);
                                flight.setVendor(routerSummary.getVendor());
                                response.getOutwardFlights().add(flight);
                                routerSummary.setNumOutward(routerSummary.getNumOutward() + 1);
                            }
                            break;
                        case RETURN_FLIGHTS:
                            if ("Return".equals(currentName)) {
                                XFlight flight = flightHandler.handleResponse();
                                flight.setGroupId(groupId);
                                flight.setVendor(routerSummary.getVendor());
                                response.getReturnFlights().add(flight);
                                routerSummary.setNumReturn(routerSummary.getNumReturn() + 1);
                            }
                            break;
                        case GROUP_PRICE:
                            if ("Amount".equals(currentName)) {
                                groupPrice = 0;
                                groupCurrency = null;
                                groupTaxIncluded = false;
                                build = true;
                            } else if ("TaxItemList".equals(currentName)) {
                                state = State.GROUP_TAX_ITEMS;
                            }
                            break;
                        case SUMMARY:
                            if ("AirportList".equals(currentName)) {
                                state = State.SUMMARY_AIRPORTS;
                                build = true;
                            } else if ("StationList".equals(currentName)) {
                                state = State.SUMMARY_STATIONS_LIST;
                                build = true;
                            }
                            break;
                        case SUMMARY_AIRPORTS:
                            if ("City".equals(currentName)) {
                                state = State.SUMMARY_AIRPORTS_CITY;
                            }
                            break;
                        case SUMMARY_STATIONS_LIST:
                            if ("Station".equals(currentName)) {
                                state = State.SUMMARY_STATION;
                            }
                            break;
                        case SUMMARY_STATION:
                            if ("City".equals(currentName)) {
                                state = State.SUMMARY_STATION_CITY;
                            }
                            break;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (build) {
                        builder.append(responseReader.getTextCharacters(), responseReader.getTextStart(), responseReader.getTextLength());
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    currentName = responseReader.getName().getLocalPart();
                    switch(state) {
                        case ROOT:
                            if ("RoutingId".equals(currentName)) {
                                response.setRoutingId(builder.toString());
                                build = false;
                            }
                            break;
                        case ROUTERS:
                            if ("RouterList".equals(currentName)) {
                                state = State.ROOT;
                            }
                            break;
                        case GROUPS:
                            if ("Id".equals(currentName)) {
                                groupId = builder.toString();
                                build = false;
                            } else if ("Router".equals(currentName)) {
                                state = State.ROUTERS;
                            }
                            break;
                        case OUTWARD_FLIGHTS:
                            if ("OutwardList".equals(currentName)) {
                                state = State.GROUPS;
                            }
                            break;
                        case RETURN_FLIGHTS:
                            if ("ReturnList".equals(currentName)) {
                                state = State.GROUPS;
                            }
                            break;
                        case GROUP_PRICE:
                            if (groupPrice >= 0) {
                                if ("Amount".equals(currentName)) {
                                    groupPrice = (int) Math.round(Double.parseDouble(builder.toString()) * 100);
                                } else if ("Currency".equals(currentName)) {
                                    groupCurrency = builder.toString();
                                } else if ("PriceIncludesTax".equals(currentName)) {
                                    groupTaxIncluded = Boolean.parseBoolean(builder.toString());
                                }
                            }
                            if ("Price".equals(currentName)) {
                                if (groupPrice >= 0) {
                                    for (int i = groupIndexOutwardStart; i < response.getOutwardFlights().size(); i++) {
                                        XFlight flight = response.getOutwardFlights().get(i);
                                        flight.setGroupPrice(groupPrice);
                                        flight.setGroupCurrency(groupCurrency);
                                        flight.setGroupTaxIncluded(groupTaxIncluded);
                                    }
                                    for (int i = groupIndexReturnStart; i < response.getReturnFlights().size(); i++) {
                                        XFlight flight = response.getReturnFlights().get(i);
                                        flight.setGroupPrice(groupPrice);
                                        flight.setGroupCurrency(groupCurrency);
                                        flight.setGroupTaxIncluded(groupTaxIncluded);
                                    }
                                }
                                state = State.GROUPS;
                                build = false;
                            }
                            break;
                        case GROUP_TAX_ITEMS:
                            if ("TaxItemList".equals(currentName)) {
                                state = State.GROUP_PRICE;
                            }
                            break;
                        case SUMMARY:
                            if ("Summary".equals(currentName)) {
                                state = State.ROOT;
                            }
                            break;
                        case SUMMARY_AIRPORTS:
                            if ("Code".equals(currentName)) {
                                airportOrStationCode = builder.toString();
                            } else if ("Name".equals(currentName)) {
                                airportOrStationName = builder.toString();
                            } else if ("Airport".equals(currentName)) {
                                if (airportOrStationCode != null && airportOrStationName != null) {
                                    response.getAirportNames().put(airportOrStationCode, airportOrStationName);
                                }
                                airportOrStationCode = null;
                                airportOrStationName = null;
                            } else if ("AirportList".equals(currentName)) {
                                state = State.SUMMARY;
                                build = false;
                            }
                            break;
                        case SUMMARY_AIRPORTS_CITY:
                            if ("City".equals(currentName)) {
                                state = State.SUMMARY_AIRPORTS;
                            }
                            break;
                        case SUMMARY_STATIONS_LIST:
                            if ("StationList".equals(currentName)) {
                                state = State.SUMMARY;
                                build = false;
                            }
                            break;
                        case SUMMARY_STATION:
                            if ("Code".equals(currentName)) {
                                airportOrStationCode = builder.toString();
                            } else if ("Name".equals(currentName)) {
                                airportOrStationName = builder.toString();
                            } else if ("Station".equals(currentName)) {
                                if (airportOrStationCode != null && airportOrStationName != null) {
                                    response.getStationNames().put(airportOrStationCode, airportOrStationName);
                                }
                                airportOrStationCode = null;
                                airportOrStationName = null;
                                state = State.SUMMARY_STATIONS_LIST;
                            }
                            break;
                        case SUMMARY_STATION_CITY:
                            if ("City".equals(currentName)) {
                                state = State.SUMMARY_STATION;
                            }
                            break;
                    }
                    currentName = null;
            }
        }
        response.setOperatorNames(flightHandler.getOperatorNames());
        return response;
    }
}
