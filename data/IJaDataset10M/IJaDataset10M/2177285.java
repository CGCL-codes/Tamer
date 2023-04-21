package net.disy.ogc.wps.v_1_0_0.model;

public interface LiteralTypeRegistry {

    public <T> LiteralType<T> getLiteralType(DataType<T> dataType);
}
