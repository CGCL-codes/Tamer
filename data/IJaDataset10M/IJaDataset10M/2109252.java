package org.albianj.persistence.object.impl;

import java.util.Map;
import org.albianj.persistence.object.IAlbianObjectHashMapping;
import org.albianj.persistence.object.IRoutingAttribute;
import org.albianj.persistence.object.IRoutingsAttribute;

public class RoutingsAttribute implements IRoutingsAttribute {

    private boolean writerHash = false;

    private boolean readerHash = false;

    private Map<String, IRoutingAttribute> writerRoutings = null;

    private Map<String, IRoutingAttribute> readerRoutings = null;

    private IAlbianObjectHashMapping hashMapping = null;

    private String type = null;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean getWriterHash() {
        return this.writerHash;
    }

    @Override
    public void setWriterHash(boolean writerHash) {
        this.writerHash = writerHash;
    }

    @Override
    public boolean getReaderHash() {
        return this.readerHash;
    }

    @Override
    public void setReaderHash(boolean readerHash) {
        this.readerHash = readerHash;
    }

    @Override
    public Map<String, IRoutingAttribute> getWriterRoutings() {
        return this.writerRoutings;
    }

    @Override
    public void setWriterRoutings(Map<String, IRoutingAttribute> writerRoutings) {
        this.writerRoutings = writerRoutings;
    }

    @Override
    public Map<String, IRoutingAttribute> getReaderRoutings() {
        return this.readerRoutings;
    }

    @Override
    public void setReaderRoutings(Map<String, IRoutingAttribute> readerRoutings) {
        this.readerRoutings = readerRoutings;
    }

    public IAlbianObjectHashMapping getHashMapping() {
        return this.hashMapping;
    }

    public void setHashMapping(IAlbianObjectHashMapping hashMapping) {
        this.hashMapping = hashMapping;
    }
}
