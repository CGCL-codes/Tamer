package uk.ac.cam.caret.imscp.impl;

import uk.ac.cam.caret.imscp.api.Metadata;
import uk.ac.cam.caret.minibix.general.*;
import uk.ac.cam.caret.minibix.general.svo.api.*;
import java.util.*;

public class MetadataImpl implements Metadata {

    private String schema, schema_version;

    private List<BuildsSvo> metadata = new ArrayList<BuildsSvo>();

    private List<Object> extras = new ArrayList<Object>();

    public Object[] getExtras() {
        return extras.toArray(new Object[0]);
    }

    public BuildsSvo[] getMetadata() {
        return metadata.toArray(new BuildsSvo[0]);
    }

    public String getSchema() {
        return schema;
    }

    public String getSchemaVersion() {
        return schema_version;
    }

    public void setSchema(String in) {
        schema = in;
    }

    public void setSchemaVersion(String in) {
        schema_version = in;
    }

    public void addMetadata(BuildsSvo in) {
        metadata.add(in);
    }

    public void addExtrametadata(Object in) {
        extras.add(in);
    }

    public void cloneInto(Metadata dest) {
        dest.setSchema(schema);
        dest.setSchemaVersion(schema_version);
        for (BuildsSvo md : metadata) if (md instanceof Reproducable) dest.addMetadata((BuildsSvo) ((Reproducable) md).reproduce());
        for (Object x : extras) if (x instanceof Reproducable) dest.addExtrametadata((Object) ((Reproducable) x).reproduce());
    }

    public MetadataImpl reproduce() {
        MetadataImpl out = new MetadataImpl();
        cloneInto(out);
        return out;
    }
}
