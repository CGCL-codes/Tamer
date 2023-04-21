package org.openstreetmap.xapi.interfaces;

import java.io.Reader;
import java.io.Writer;
import org.openstreetmap.xapi.RawDataProviderException;

public interface RawDataProvider {

    public Reader getDataReader() throws RawDataProviderException;

    public Writer getDataWriter() throws RawDataProviderException;
}
