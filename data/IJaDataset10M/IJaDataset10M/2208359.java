package org.expasy.jpl.io.hadoop;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.junit.Test;

public class MapFileTest {

    private static final String[] DATA = { "One, two, buckle my shoe", "Three, four, shut the door", "Five, six, pick up sticks", "Seven, eight, lay them straight", "Nine, ten, a big fat hen" };

    @Test
    public void testWriteMapFile() throws IOException {
        String uri = "/tmp/numbers.map";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        IntWritable key = new IntWritable();
        Text value = new Text();
        MapFile.Writer writer = null;
        try {
            writer = new MapFile.Writer(conf, fs, uri, key.getClass(), value.getClass());
            for (int i = 0; i < 1024; i++) {
                key.set(i + 1);
                value.set(DATA[i % DATA.length]);
                writer.append(key, value);
            }
        } finally {
            IOUtils.closeStream(writer);
        }
    }

    public static byte[] serialize(Writable writable) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);
        writable.write(dataOut);
        dataOut.close();
        return out.toByteArray();
    }

    public static class DistSerialFormat {

        @SuppressWarnings("unused")
        private int version;

        @SuppressWarnings("unused")
        private String keyFormat;

        @SuppressWarnings("unused")
        private String valueFormat;
    }

    public interface SerialDist {

        int getVersion();

        int getClassWidth();

        int getFirstClassFrom();

        int getLastClassTo();
    }
}
