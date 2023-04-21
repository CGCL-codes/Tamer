package net.sf.jeda.gedasymbols.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import net.sf.jeda.gedasymbols.api.parts.Part;
import net.sf.jeda.gedasymbols.api.parts.Pin;

/**
 *
 * @author eduardo-costa
 */
public class GEDASymbol {

    private Date version;

    private int fileFormatVersion;

    private Collection<Part> parts = new ArrayList<Part>();

    private Collection<Pin> pins = new ArrayList<Pin>();

    public int getFileFormatVersion() {
        return fileFormatVersion;
    }

    public void setFileFormatVersion(int fileFormatVersion) {
        this.fileFormatVersion = fileFormatVersion;
    }

    public Date getVersion() {
        return version;
    }

    public void setVersion(Date version) {
        this.version = version;
    }

    public Collection<Part> getParts() {
        return parts;
    }

    public Collection<Pin> getPins() {
        return pins;
    }
}
