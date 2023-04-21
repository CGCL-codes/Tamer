package com.avometric.SHARD.reasoner;

import java.util.Collection;
import java.util.HashMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public abstract class Reasoner {

    protected String _tripleDir;

    protected String _reasonDir;

    protected String _system;

    protected String _tracker;

    protected String _replication;

    protected String _jar;

    protected String _mappers;

    protected String _reducers;

    protected String _rdfSchema;

    public Reasoner(String rdfSchema, String tripleDir, String reasonDir, String system, String tracker, String replication, String jar, String mappers, String reducers) {
        _rdfSchema = rdfSchema;
        _tripleDir = tripleDir;
        _reasonDir = reasonDir;
        _system = system;
        _tracker = tracker;
        _replication = replication;
        _jar = jar;
        _mappers = mappers;
        _reducers = reducers;
    }

    public void reason() throws Exception {
        Path source = new Path(_tripleDir);
        Path reasoned = new Path(_reasonDir);
        Configuration config = new Configuration();
        config.set("fs.default.name", _system);
        FileSystem hdfs = FileSystem.get(config);
        hdfs.delete(reasoned, true);
        generateReasonedData();
        hdfs.delete(source, true);
        hdfs.rename(reasoned, source);
    }

    protected abstract void generateReasonedData() throws Exception;
}
