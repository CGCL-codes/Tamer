package au.gov.nla.aons.repository.fedora.domain;

import java.io.IOException;
import java.io.InputStream;

public interface FedoraDataStream {

    public String getMimeType();

    public abstract InputStream retrieveDataStream() throws IOException;
}
