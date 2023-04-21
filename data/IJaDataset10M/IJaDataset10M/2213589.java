package de.unkrig.commons.io;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * This {@link FilterWriter} scans the character stream for tags and inserts "artificial" line breaks as follows:
 * <dl>
 *   <dt>{@code <a><b}
 *   <dd>Wrap and indent (between '>' and '<')
 *   <dt>{@code </a><b}
 *   <dd>Wrap
 *   <dt>{@code <a/><b}
 *   <dd>Wrap
 *   <dt>{@code </a></b}
 *   <dd>Wrap and unindent
 *   <dt>{@code <a/></b}
 *   <dd>Wrap and unindent
 * </dl>
 */
public class XMLFormatterWriter extends FilterWriter {

    private int state = 0;

    private int indentation = 0;

    protected XMLFormatterWriter(Writer out) {
        super(out);
    }

    @Override
    public void write(int c) throws IOException {
        switch(this.state) {
            case 0:
                if (c == '<') this.state = 1;
                break;
            case 1:
                this.state = c == '/' ? 2 : 5;
                break;
            case 2:
                if (c == '>') this.state = 3;
                break;
            case 3:
                if (c == '<') {
                    this.state = 4;
                    return;
                }
                this.state = 0;
                return;
            case 4:
                this.out.write("\r\n");
                if (c == '/' && this.indentation > 0) this.indentation--;
                for (int i = 0; i < this.indentation; i++) this.out.write("  ");
                this.out.write('<');
                this.state = c == '/' ? 2 : 5;
                break;
            case 5:
                if (c == '/') {
                    this.state = 6;
                } else if (c == '>') {
                    this.state = 9;
                }
                break;
            case 6:
                this.state = c == '>' ? 7 : 5;
                break;
            case 7:
                if (c == '<') {
                    this.state = 8;
                    return;
                }
                this.state = 0;
                break;
            case 8:
                this.out.write("\r\n");
                if (c == '/' && this.indentation > 0) this.indentation--;
                for (int i = 0; i < this.indentation; i++) this.out.write("  ");
                this.out.write('<');
                this.state = c == '/' ? 2 : 5;
                break;
            case 9:
                if (c == '<') {
                    this.state = 10;
                    return;
                }
                this.state = 0;
                break;
            case 10:
                this.out.write("\r\n");
                if (c != '/') this.indentation++;
                for (int i = 0; i < this.indentation; i++) this.out.write("  ");
                this.out.write('<');
                this.state = c == '/' ? 2 : 5;
                break;
        }
        this.out.write(c);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) this.write(cbuf[i]);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) this.write(str.charAt(i));
    }
}
