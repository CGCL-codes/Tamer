package com.oneandone.sushi.metadata.xml;

import java.io.IOException;
import java.io.Writer;

public class WriterTree extends Tree {

    private final Writer dest;

    private int indent;

    private boolean strict;

    public WriterTree(Writer dest, boolean strict) {
        this.dest = dest;
        this.indent = 0;
        this.strict = strict;
    }

    @Override
    public Writer done() throws IOException {
        if (indent != 0) {
            throw new IllegalStateException("" + indent);
        }
        dest.flush();
        return dest;
    }

    @Override
    public void ref(String name, int id) throws IOException {
        indent();
        dest.write("<");
        dest.write(name);
        dest.write(" idref='");
        dest.write(Integer.toString(id));
        dest.write("'/>\n");
    }

    @Override
    public void begin(String name, int id, String type, boolean withEnd) throws IOException {
        indent();
        dest.write("<");
        dest.write(name);
        if (id != -1) {
            dest.write(" id='");
            dest.write(Integer.toString(id));
            dest.write('\'');
        }
        type(type);
        if (withEnd) {
            dest.write("/>\n");
        } else {
            indent++;
            dest.write(">\n");
        }
    }

    @Override
    public void end(String name) throws IOException {
        --indent;
        indent();
        dest.write("</");
        dest.write(name);
        dest.write(">\n");
    }

    @Override
    public void text(String name, String typeAttribute, String text) throws IOException {
        indent();
        dest.write('<');
        dest.write(name);
        type(typeAttribute);
        dest.write('>');
        dest.write(com.oneandone.sushi.xml.Serializer.escapeEntities(text, strict));
        dest.write("</");
        dest.write(name);
        dest.write(">\n");
    }

    private void type(String type) throws IOException {
        if (type != null) {
            dest.write(" type='");
            dest.write(type);
            dest.write('\'');
        }
    }

    private void indent() throws IOException {
        for (int i = 0; i < indent; i++) {
            dest.write("  ");
        }
    }
}
