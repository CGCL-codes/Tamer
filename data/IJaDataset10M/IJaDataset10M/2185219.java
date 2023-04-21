package org.opensplice.restful.service;

import java.util.HashMap;
import java.util.Map;
import DDS.ANY_STATUS;
import DDS.DATAREADER_QOS_USE_TOPIC_QOS;
import DDS.DataReader;
import DDS.Subscriber;
import DDS.Topic;

public class DDSSubscriberProxy {

    private Subscriber subscriber;

    private String name;

    private DDSParticipantProxy participant;

    private Map<String, DDSReaderProxy> readers;

    public DDSSubscriberProxy(DDSParticipantProxy _participant, String _name, Subscriber _subscriber) {
        participant = _participant;
        name = _name;
        subscriber = _subscriber;
        readers = new HashMap<String, DDSReaderProxy>();
    }

    public DDSReaderProxy lookupReader(String readerName, String topicName) {
        return readers.get(topicName + "," + readerName);
    }

    private void addReader(DDSReaderProxy reader, String readerName, String topicName) {
        readers.put(topicName + "," + readerName, reader);
    }

    public DDSReaderProxy lookupOrCreateReader(String readerName, String topicName, String typeName, Topic topic) {
        DDSReaderProxy result = lookupReader(topicName, readerName);
        if (result == null) {
            DataReader reader = subscriber.create_datareader(topic, DATAREADER_QOS_USE_TOPIC_QOS.value, null, ANY_STATUS.value);
            ErrorHandler.checkHandle(reader, "DDS.Subscriber.create_datareader");
            result = new DDSReaderProxy(this, readerName, reader, typeName);
            addReader(result, topicName, readerName);
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public DDSParticipantProxy getParticipant() {
        return participant;
    }
}
