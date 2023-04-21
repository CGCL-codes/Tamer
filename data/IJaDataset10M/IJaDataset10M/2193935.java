package org.jtools.shovel.format.custom;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import org.jtools.feature.Named;
import org.jtools.shovel.api.Record;
import org.jtools.shovel.format.AbstractWriterFormat;
import org.jtools.util.CommonUtils;

public class CustomWriter extends AbstractWriterFormat {

    private final PrintWriter pw;

    private final CustomFormat format;

    private String[] attributes;

    public CustomWriter(Writer writer, CustomFormat format) {
        super(writer);
        this.pw = new PrintWriter(writer);
        this.format = (format == null ? new CustomFormat() : format).validate();
    }

    private final void print(String s) {
        String[] lines = s.split("\n", -1);
        for (String line : lines) pw.println(line);
    }

    public void execute(Record data) {
    }

    @Override
    public void start() throws IOException {
        Collection<String> attrCollection = CommonUtils.apply(Named.Getter.getInstance(), metaData());
        attributes = attrCollection.toArray(new String[attrCollection.size()]);
    }

    @Override
    public void close() throws IOException {
        pw.flush();
        pw.close();
        super.close();
    }

    @Override
    public void flush() throws IOException {
        pw.flush();
        super.flush();
    }

    @Override
    public String getName() {
        return "CSV";
    }
}
