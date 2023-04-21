package org.gromurph.util;

import java.io.IOException;
import org.gromurph.javascore.model.Regatta;

public interface Exporter {

    public abstract void setRegatta(Regatta aRegatta);

    public abstract void setFilename(String f) throws IOException;

    public abstract void export() throws IOException;
}
