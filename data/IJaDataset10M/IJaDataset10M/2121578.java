package org.matsim.mobsim.cppdeqsim;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.matsim.basic.v01.IdImpl;
import org.matsim.events.AgentArrivalEvent;
import org.matsim.events.AgentDepartureEvent;
import org.matsim.events.AgentWait2LinkEvent;
import org.matsim.events.BasicEventImpl;
import org.matsim.events.Events;
import org.matsim.events.LinkEnterEvent;
import org.matsim.events.LinkLeaveEvent;

public class EventsReaderDEQv1 {

    private final Events events;

    static final Logger log = Logger.getLogger(EventsReaderDEQv1.class);

    public EventsReaderDEQv1(final Events events) {
        this.events = events;
    }

    public void readFile(final String filename) {
        try {
            if (new File(filename).exists()) {
                readEventsSingleCPU(filename);
            } else {
                readEventsParallel(filename);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.events.printEventsCount();
    }

    private void readEventsSingleCPU(final String iterationEventsFile) throws FileNotFoundException, IOException {
        System.out.println("start reading deqsim events. " + (new Date()));
        DataInputStream in = null;
        try {
            in = new DataInputStream(new BufferedInputStream(new FileInputStream(iterationEventsFile)));
            boolean eventComplete = true;
            try {
                while (true) {
                    double time = in.readDouble();
                    eventComplete = false;
                    int agentId = in.readInt();
                    int linkId = in.readInt();
                    int eventType = in.readInt();
                    eventComplete = true;
                    this.events.processEvent(createEvent(time, agentId, linkId, eventType));
                }
            } catch (EOFException e) {
                if (!eventComplete) {
                    log.info("EOF while reading an event");
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
	 * Reads in multiple events files with the name <code>iterationEventsFileBase + "." + i</code>
	 * and processes the events such that all the events are still in chronological order.  For this,
	 * the events in each of the files must already be sorted by time. The method will look for files
	 * with the mentioned names beginning with <code>i = 0</code> and increases <code>i</code> until
	 * no file is found with the specified name.
	 *
	 * @param iterationEventsFileBase The base filename of the events files.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    private void readEventsParallel(final String iterationEventsFileBase) throws FileNotFoundException, IOException {
        System.out.println("start reading deqsim events. " + (new Date()));
        List<BinaryEventsReader> readerList = new ArrayList<BinaryEventsReader>();
        int counter = 0;
        File file = new File(iterationEventsFileBase + "." + counter);
        while (file.exists()) {
            BinaryEventsReader reader = new BinaryEventsReader(file);
            if (reader.hasNext()) {
                reader.next();
                readerList.add(reader);
            }
            counter++;
            file = new File(iterationEventsFileBase + "." + counter);
        }
        System.out.println("found " + counter + " events files.");
        int nofReaders = readerList.size();
        BinaryEventsReader[] readers = readerList.toArray(new BinaryEventsReader[nofReaders]);
        int idxEarliestEvent = -1;
        BasicEventImpl earliestEvent = null;
        while (nofReaders > 0) {
            idxEarliestEvent = 0;
            earliestEvent = readers[0].event;
            for (int i = 1; i < nofReaders; i++) {
                if (readers[i].event.getTime() < earliestEvent.getTime()) {
                    idxEarliestEvent = i;
                    earliestEvent = readers[i].event;
                }
            }
            this.events.processEvent(earliestEvent);
            if (readers[idxEarliestEvent].hasNext()) {
                readers[idxEarliestEvent].next();
            } else {
                readerList.remove(idxEarliestEvent);
                nofReaders = readerList.size();
                readers = readerList.toArray(new BinaryEventsReader[nofReaders]);
            }
        }
    }

    public static final BasicEventImpl createEvent(final double time, final int agentID, final int linkID, final int flag) {
        BasicEventImpl event = null;
        switch(flag) {
            case 2:
                event = new LinkLeaveEvent(time, new IdImpl(agentID), new IdImpl(linkID));
                break;
            case 5:
                event = new LinkEnterEvent(time, new IdImpl(agentID), new IdImpl(linkID));
                break;
            case 4:
                event = new AgentWait2LinkEvent(time, new IdImpl(agentID), new IdImpl(linkID));
                break;
            case 6:
                event = new AgentDepartureEvent(time, new IdImpl(agentID), new IdImpl(linkID));
                break;
            case 0:
                event = new AgentArrivalEvent(time, new IdImpl(agentID), new IdImpl(linkID));
                break;
        }
        return event;
    }

    private static class BinaryEventsReader {

        BasicEventImpl event = null;

        private BasicEventImpl next = null;

        private final DataInputStream in;

        private final String filename;

        public BinaryEventsReader(final File file) throws FileNotFoundException {
            this.filename = file.getName();
            this.in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            try {
                next();
            } catch (IOException e) {
                this.next = null;
                log.warn(e.getMessage());
            }
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public void next() throws IOException {
            this.event = this.next;
            boolean eventComplete = true;
            try {
                double time = this.in.readDouble();
                eventComplete = false;
                int agentId = this.in.readInt();
                int linkId = this.in.readInt();
                int eventType = this.in.readInt();
                this.next = createEvent(time, agentId, linkId, eventType);
            } catch (EOFException e) {
                this.next = null;
                if (!eventComplete) {
                    log.warn("EOF while reading an event from " + this.filename);
                }
                this.in.close();
            }
        }

        public BasicEventImpl getEvent() {
            return this.event;
        }
    }
}
