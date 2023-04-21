package net.sf.lucis.core;

interface CheckpointWriter<T> {

    void setCheckpoint(T checkpoint) throws Exception;
}
