package bahn.model;

import mmf.api.IModel;
import mmf.model.ModelReference;
import mmf.model.ModelSupport;

public class MDecoder implements IModel {

    /** Zum Aufbau von (lazy) Referenzen */
    public static class Proxy extends ModelReference<MDecoder> {

        public Proxy() {
            super(MDecoder.class);
        }
    }

    private PKDecoder primaryKey;

    private String name;

    private ModelSupport modelSupport = new ModelSupport();

    public static final MetaDecoder metaModel = new MetaDecoder();

    public MDecoder(PKDecoder primaryKey) {
        this.primaryKey = primaryKey != null ? primaryKey : new PKDecoder();
    }

    public MetaDecoder getMetaModel() {
        return metaModel;
    }

    public ModelSupport getModelSupport() {
        return modelSupport;
    }

    @Override
    public String toString() {
        return metaModel.toPropertyList(this);
    }
}
