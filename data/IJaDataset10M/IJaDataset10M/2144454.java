package org.matsim.core.network;

import java.io.IOException;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.api.internal.MatsimSomeWriter;
import org.matsim.core.network.NetworkChangeEvent.ChangeType;
import org.matsim.core.network.NetworkChangeEvent.ChangeValue;
import org.matsim.core.utils.io.MatsimXmlWriter;
import org.matsim.core.utils.misc.Time;

/**
 * @author illenberger
 *
 */
public class NetworkChangeEventsWriter extends MatsimXmlWriter implements MatsimSomeWriter {

    private static final Logger log = Logger.getLogger(NetworkChangeEventsWriter.class);

    public static final String TAB = "\t";

    public static final String WHITESPACE = " ";

    public static final String OPEN_TAG_1 = "<";

    public static final String OPEN_TAG_2 = "</";

    public static final String CLOSE_TAG_1 = ">";

    public static final String CLOSE_TAG_2 = "/>";

    public static final String QUOTE = "\"";

    public static final String EQUALS = "=";

    public static final String DTD_LOCATION = "http://www.matsim.org/files/dtd";

    public static final String W3_URL = "http://www.w3.org/2001/XMLSchema-instance";

    public static final String XSD_LOCATION = "http://www.matsim.org/files/dtd/networkChangeEvents.xsd";

    public void write(String file, Collection<NetworkChangeEvent> events) {
        try {
            openFile(file);
            super.writeXmlHead();
            this.writer.write(OPEN_TAG_1);
            this.writer.write(NetworkChangeEventsParser.NETWORK_CHANGE_EVENTS_TAG);
            this.writer.write(WHITESPACE);
            this.writer.write("xmlns");
            this.writer.write(EQUALS);
            this.writer.write(QUOTE);
            this.writer.write(DTD_LOCATION);
            this.writer.write(QUOTE);
            this.writer.write(WHITESPACE);
            this.writer.write("xmlns:xsi");
            this.writer.write(EQUALS);
            this.writer.write(QUOTE);
            this.writer.write(W3_URL);
            this.writer.write(QUOTE);
            this.writer.write(WHITESPACE);
            this.writer.write("xsi:schemaLocation");
            this.writer.write(EQUALS);
            this.writer.write(QUOTE);
            this.writer.write(DTD_LOCATION);
            this.writer.write(WHITESPACE);
            this.writer.write(XSD_LOCATION);
            this.writer.write(QUOTE);
            this.writer.write(CLOSE_TAG_1);
            this.writer.write(NL);
            this.writer.write(NL);
            for (NetworkChangeEvent event : events) {
                writeEvent(event);
                this.writer.write(NL);
                this.writer.write(NL);
            }
            this.writer.write(OPEN_TAG_2);
            this.writer.write(NetworkChangeEventsParser.NETWORK_CHANGE_EVENTS_TAG);
            this.writer.write(CLOSE_TAG_1);
            this.writer.write(NL);
            close();
        } catch (IOException e) {
            log.fatal("Error during writing network change events!", e);
        }
    }

    private void writeEvent(NetworkChangeEvent event) throws IOException {
        this.writer.write(TAB);
        this.writer.write(OPEN_TAG_1);
        this.writer.write(NetworkChangeEventsParser.NETWORK_CHANGE_EVENT_TAG);
        this.writer.write(WHITESPACE);
        this.writer.write(NetworkChangeEventsParser.START_TIME_TAG);
        this.writer.write(EQUALS);
        this.writer.write(QUOTE);
        this.writer.write(Time.writeTime(event.getStartTime()));
        this.writer.write(QUOTE);
        this.writer.write(CLOSE_TAG_1);
        this.writer.write(NL);
        for (Link link : event.getLinks()) {
            this.writer.write(TAB);
            this.writer.write(TAB);
            this.writer.write(OPEN_TAG_1);
            this.writer.write(NetworkChangeEventsParser.LINK_TAG);
            this.writer.write(WHITESPACE);
            this.writer.write(NetworkChangeEventsParser.REF_ID_TAG);
            this.writer.write(EQUALS);
            this.writer.write(QUOTE);
            this.writer.write(link.getId().toString());
            this.writer.write(QUOTE);
            this.writer.write(CLOSE_TAG_2);
            this.writer.write(NL);
        }
        if (event.getFlowCapacityChange() != null) {
            writeChangeValue(NetworkChangeEventsParser.FLOW_CAPACITY_TAG, event.getFlowCapacityChange());
        }
        if (event.getFreespeedChange() != null) {
            writeChangeValue(NetworkChangeEventsParser.FREESPEED_TAG, event.getFreespeedChange());
        }
        if (event.getLanesChange() != null) {
            writeChangeValue(NetworkChangeEventsParser.LANES_TAG, event.getLanesChange());
        }
        this.writer.write(TAB);
        this.writer.write(OPEN_TAG_2);
        this.writer.write(NetworkChangeEventsParser.NETWORK_CHANGE_EVENT_TAG);
        this.writer.write(CLOSE_TAG_1);
    }

    private void writeChangeValue(String attName, ChangeValue value) throws IOException {
        this.writer.write(TAB);
        this.writer.write(TAB);
        this.writer.write(OPEN_TAG_1);
        this.writer.write(attName);
        this.writer.write(WHITESPACE);
        this.writer.write(NetworkChangeEventsParser.CHANGE_TYPE_TAG);
        this.writer.write(EQUALS);
        this.writer.write(QUOTE);
        if (value.getType() == ChangeType.ABSOLUTE) {
            this.writer.write(NetworkChangeEventsParser.ABSOLUTE_VALUE);
        } else if (value.getType() == ChangeType.FACTOR) {
            this.writer.write(NetworkChangeEventsParser.FACTOR_VALUE);
        }
        this.writer.write(QUOTE);
        this.writer.write(WHITESPACE);
        this.writer.write(NetworkChangeEventsParser.VALUE_TAG);
        this.writer.write(EQUALS);
        this.writer.write(QUOTE);
        this.writer.write(String.valueOf(value.getValue()));
        this.writer.write(QUOTE);
        this.writer.write(CLOSE_TAG_2);
        this.writer.write(NL);
    }
}
