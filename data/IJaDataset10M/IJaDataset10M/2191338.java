package org.slasoi.infrastructure.monitoring.pubsub.messages;

import com.google.gson.Gson;
import org.slasoi.infrastructure.monitoring.pubsub.PubSubResponse;
import org.slasoi.infrastructure.monitoring.utils.JsonUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetSchedulerEventsResponse extends PubSubResponse {

    private List<Event> events;

    public GetSchedulerEventsResponse() {
        super.setResponseType("GetSchedulerEventsResponse");
        events = new ArrayList<Event>();
    }

    public static GetServiceEventsResponse fromJson(String jsonString) {
        Gson gson = JsonUtils.getInstance().getGson();
        return gson.fromJson(jsonString, GetServiceEventsResponse.class);
    }

    public String toJson() {
        Gson gson = JsonUtils.getInstance().getGson();
        return gson.toJson(this);
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void putEvent(Event event) {
        this.events.add(event);
    }

    public static class Event {

        private String source;

        private Date timestamp;

        private String description;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
