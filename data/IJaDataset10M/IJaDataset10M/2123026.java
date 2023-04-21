package com.thoughtworks.blipit.restful;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.blipit.persistence.DataStoreHelper;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import javax.jdo.PersistenceManager;

public class ChannelResource extends ServerResource {

    private String channelId;

    @Override
    protected void doInit() throws ResourceException {
        this.getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        this.channelId = (String) getRequestAttributes().get("channel_id");
    }

    @Override
    protected Representation get(Variant variant) throws ResourceException {
        PersistenceManager persistenceManager = null;
        Channel channel = null;
        try {
            Key key = KeyFactory.createKey(Channel.class.getSimpleName(), Long.valueOf(channelId));
            persistenceManager = DataStoreHelper.getPersistenceManager();
            channel = persistenceManager.getObjectById(Channel.class, key);
        } finally {
            if (persistenceManager != null) persistenceManager.close();
        }
        if (channel == null) return new StringRepresentation("No channel found with ID: " + channelId);
        return new JsonRepresentation(new Gson().toJson(channel));
    }
}
