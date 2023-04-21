package org.apache.nutch.parse;

import java.io.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configurable;

/** The result of parsing a page's raw content.
 * @see Parser#getParse(Content)
 */
public class ParseImpl implements Parse, Writable, Configurable {

    private ParseText text;

    private ParseData data;

    private Configuration conf;

    public ParseImpl() {
    }

    public ParseImpl(Parse parse) {
        this(parse.getText(), parse.getData());
    }

    public ParseImpl(String text, ParseData data) {
        this(new ParseText(text), data);
    }

    public ParseImpl(ParseText text, ParseData data) {
        this.text = text;
        this.data = data;
    }

    public String getText() {
        return text.getText();
    }

    public ParseData getData() {
        return data;
    }

    public final void write(DataOutput out) throws IOException {
        text.write(out);
        data.write(out);
    }

    public void readFields(DataInput in) throws IOException {
        text = new ParseText();
        text.readFields(in);
        data = new ParseData();
        data.setConf(this.conf);
        data.readFields(in);
    }

    public static ParseImpl read(DataInput in, Configuration conf) throws IOException {
        ParseImpl parseImpl = new ParseImpl();
        parseImpl.setConf(conf);
        parseImpl.readFields(in);
        return parseImpl;
    }

    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    public Configuration getConf() {
        return this.conf;
    }
}
