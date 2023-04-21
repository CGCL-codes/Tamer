package org.nakedobjects.plugins.remoting.command.shared.requests;

import java.io.Serializable;
import org.nakedobjects.metamodel.commons.encoding.ByteDecoder;
import org.nakedobjects.metamodel.commons.encoding.ByteEncoder;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.remoting.command.shared.requests.Request;

public class Response implements Encodable, Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;

    private final Object object;

    public Response(final Request request) {
        this.id = request.getId();
        this.object = request.getResponse();
    }

    public Response(final ByteDecoder decoder) {
        id = decoder.getInt();
        object = decoder.getObject();
    }

    public Response(final Request request, final Exception e) {
        this.id = request.getId();
        object = e;
    }

    public void encode(final ByteEncoder encoder) {
        encoder.add(id);
        encoder.add(object);
    }

    public Object getObject() {
        return object;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("id", id);
        str.append("object", object);
        return str.toString();
    }
}
