package org.mibew.api.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.mibew.api.MibewThread;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *  @author inspirer
 */
public class UpdateHandler extends DefaultHandler {

    public static final int UPD_ERROR = 1;

    public static final int UPD_THREADS = 2;

    private int fResponse = 0;

    private String fMessage = "";

    private long fRevision;

    private long fTime;

    private List<MibewThread> fUpdated;

    private Stack<String> fPath = new Stack<String>();

    private MibewThread fCurrentThread;

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        try {
            if (fPath.size() == 0) {
                if (name.equals("error")) {
                    fResponse = UPD_ERROR;
                } else if (name.equals("threads")) {
                    fResponse = UPD_THREADS;
                    fTime = Long.parseLong(attributes.getValue("time"));
                    fRevision = Long.parseLong(attributes.getValue("revision"));
                } else {
                    throw new SAXException("unknown root element: " + name);
                }
            }
            if (fResponse == UPD_THREADS && fPath.size() == 1 && name.equals("thread")) {
                long id = Long.parseLong(attributes.getValue("id"));
                String stateid = attributes.getValue("stateid");
                fCurrentThread = new MibewThread(id, stateid);
                if (!stateid.equals("closed")) {
                    fCurrentThread.setStateText(attributes.getValue("state"));
                    fCurrentThread.setCanOpen(booleanAttribute(attributes.getValue("canopen")));
                    fCurrentThread.setCanView(booleanAttribute(attributes.getValue("canview")));
                    fCurrentThread.setCanBan(booleanAttribute(attributes.getValue("canban")));
                }
            }
        } catch (NumberFormatException ex) {
            throw new SAXException(ex.getMessage());
        }
        fPath.push(name);
    }

    private boolean booleanAttribute(String value) {
        if (value != null && value.equals("true")) {
            return true;
        }
        return false;
    }

    private long longValue(String value) throws SAXException {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new SAXException(ex);
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        fPath.pop();
        if (fResponse == UPD_THREADS && fPath.size() == 1 && name.equals("thread")) {
            if (fUpdated == null) {
                fUpdated = new ArrayList<MibewThread>();
            }
            fUpdated.add(fCurrentThread);
            fCurrentThread = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (fResponse == UPD_ERROR) {
            String current = fPath.peek();
            if (fPath.size() != 2 || !current.equals("descr")) {
                throw new SAXException("unexpected characters");
            }
            fMessage += new String(ch, start, length);
        } else if (fResponse == UPD_THREADS) {
            if (fCurrentThread == null || fPath.size() != 3) {
                throw new SAXException("unknown characters");
            }
            String subvar = fPath.peek();
            String value = new String(ch, start, length);
            if ("name".equals(subvar)) {
                fCurrentThread.setClientName(fCurrentThread.getClientName() + value);
            } else if ("addr".equals(subvar)) {
                fCurrentThread.setAddress(fCurrentThread.getAddress() + value);
            } else if ("message".equals(subvar)) {
                fCurrentThread.setFirstMessage(fCurrentThread.getFirstMessage() + value);
            } else if ("agent".equals(subvar)) {
                fCurrentThread.setAgent(fCurrentThread.getAgent() + value);
            } else if ("modified".equals(subvar)) {
                if (fCurrentThread.getWaitingTime() != 0) {
                    throw new SAXException("error: waiting time is already set");
                }
                fCurrentThread.setWaitingTime(longValue(value) - fTime + System.currentTimeMillis());
            }
        } else {
            throw new SAXException("unexpected characters: no root");
        }
    }

    public int getResponse() {
        return fResponse;
    }

    public String getMessage() {
        return fMessage;
    }

    public long getTime() {
        return fTime;
    }

    public long getRevision() {
        return fRevision;
    }

    public List<MibewThread> getThreads() {
        return fUpdated;
    }
}
