package org.apache.nutch.parse;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.util.NutchConfiguration;
import org.apache.nutch.util.WritableTestUtils;
import org.apache.nutch.metadata.Metadata;
import junit.framework.TestCase;

/** Unit tests for ParseData. */
public class TestParseData extends TestCase {

    private Configuration conf = NutchConfiguration.create();

    public TestParseData(String name) {
        super(name);
    }

    public void testParseData() throws Exception {
        String title = "The Foo Page";
        Outlink[] outlinks = new Outlink[] { new Outlink("http://foo.com/", "Foo", conf), new Outlink("http://bar.com/", "Bar", conf) };
        Metadata metaData = new Metadata();
        metaData.add("Language", "en/us");
        metaData.add("Charset", "UTF-8");
        ParseData r = new ParseData(ParseStatus.STATUS_SUCCESS, title, outlinks, metaData);
        r.setConf(conf);
        WritableTestUtils.testWritable(r, conf);
    }

    public void testMaxOutlinks() throws Exception {
        Outlink[] outlinks = new Outlink[128];
        for (int i = 0; i < outlinks.length; i++) {
            outlinks[i] = new Outlink("http://outlink.com/" + i, "Outlink" + i, conf);
        }
        ParseData original = new ParseData(ParseStatus.STATUS_SUCCESS, "Max Outlinks Title", outlinks, new Metadata());
        Configuration conf = NutchConfiguration.create();
        conf.setInt("db.max.outlinks.per.page", 0);
        ParseData data = (ParseData) WritableTestUtils.writeRead(original, conf);
        assertEquals(0, data.getOutlinks().length);
        conf.setInt("db.max.outlinks.per.page", 100);
        data = (ParseData) WritableTestUtils.writeRead(original, conf);
        assertEquals(100, data.getOutlinks().length);
        conf.setInt("db.max.outlinks.per.page", 256);
        data = (ParseData) WritableTestUtils.writeRead(original, conf);
        assertEquals(outlinks.length, data.getOutlinks().length);
        conf.setInt("db.max.outlinks.per.page", -1);
        data = (ParseData) WritableTestUtils.writeRead(original, conf);
        assertEquals(outlinks.length, data.getOutlinks().length);
    }
}
