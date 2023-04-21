package com.leemba.leemlet.representation;

import com.leemba.leemlet.JsonBuilder;
import com.leemba.leemlet.Representation;
import com.leemba.leemlet.objects.LongMessage;
import com.leemba.leemlet.objects.StringMessage;

/**
 *
 * @author mrjohnson
 */
public class MessageRepresentation extends Representation {

    private Representation rep = null;

    /**
     * Will gson serialize obj
     *
     * @param obj
     */
    public MessageRepresentation(String message) {
        super.data = new StringMessage().setMessage(message);
    }

    public MessageRepresentation(Long id, String message) {
        super.data = new LongMessage().setMessage(message).setId(id);
    }

    public MessageRepresentation(String id, String message) {
        super.data = new StringMessage().setMessage(message).setId(id);
    }

    @Override
    public String toJson() {
        return JsonBuilder.builder().create().toJson(super.data);
    }

    @Override
    public String toText() {
        return (String) super.data;
    }
}
