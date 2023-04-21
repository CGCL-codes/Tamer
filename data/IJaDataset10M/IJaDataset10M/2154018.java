package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import org.nakedobjects.metamodel.encoding.DataInputExtended;
import org.nakedobjects.metamodel.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.encoding.Encodable;

public class GetPropertiesResponse implements Encodable, Serializable {

    private static final long serialVersionUID = 1L;

    private final Properties properties;

    public GetPropertiesResponse(Properties properties) {
        this.properties = properties;
        instantiated();
    }

    public GetPropertiesResponse(DataInputExtended input) throws IOException {
        this.properties = input.readSerializable(Properties.class);
        instantiated();
    }

    public void encode(DataOutputExtended output) throws IOException {
        output.writeSerializable(properties);
    }

    private void instantiated() {
    }

    public Properties getProperties() {
        return properties;
    }
}
