package br.gov.frameworkdemoiselle.cassandra.persistence;

public interface TypeMapping<T> {

    public abstract byte[] toBytes(Object value);

    public abstract T fromBytes(byte[] value);
}
