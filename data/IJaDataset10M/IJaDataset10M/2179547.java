package com.google.code.guidatv.android.rest;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.List;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.type.TypeReference;
import org.restlet.data.MediaType;
import org.restlet.engine.Engine;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import android.text.format.DateFormat;
import com.google.code.guidatv.model.Channel;
import com.google.code.guidatv.model.Schedule;

public class GuidaTvService {

    private static final String GUIDA_TV_URL = "http://guida-tv.appspot.com/";

    private ClientResource channelsResource;

    private ObjectMapper channelsMapper;

    public GuidaTvService() {
        Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());
        channelsResource = new ClientResource(GUIDA_TV_URL + "rest/channels");
        channelsMapper = new ObjectMapper();
        channelsMapper.setVisibilityChecker(new VisibilityChecker.Std(Visibility.DEFAULT, Visibility.DEFAULT, Visibility.DEFAULT, Visibility.DEFAULT, Visibility.ANY));
    }

    public List<Channel> getChannels() throws IOException {
        Representation representation = channelsResource.get(MediaType.APPLICATION_JSON);
        Reader reader;
        List<Channel> channels;
        reader = representation.getReader();
        try {
            channels = channelsMapper.readValue(reader, new TypeReference<List<Channel>>() {
            });
        } finally {
            reader.close();
        }
        return channels;
    }

    public Schedule getSchedule(String channelCode, Date date) throws IOException {
        String dateString = DateFormat.format("yyyyMMddz", date).toString();
        if (dateString.endsWith("Z")) {
            dateString = dateString.substring(0, 8) + "-0000";
        }
        ClientResource clientResource = new ClientResource(GUIDA_TV_URL + "rest/channels/" + channelCode + "/schedules/" + dateString);
        Representation representation = clientResource.get(MediaType.APPLICATION_JSON);
        Reader reader;
        Schedule schedule;
        reader = representation.getReader();
        try {
            schedule = channelsMapper.readValue(reader, new TypeReference<Schedule>() {
            });
        } finally {
            reader.close();
        }
        return schedule;
    }
}
