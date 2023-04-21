package org.gomba.utils.xml;

import java.io.Reader;
import java.io.IOException;

/**
 * This Reader implementation removes invalid xml characters.
 * 
 * not in a smart way :-(
 * 
 * @author Daniele Galdi
 * @version $Id: XMLTextReader.java,v 1.1 2004/11/10 16:44:27 flaviotordini Exp $
 * @see http://www.w3.org/TR/REC-xml/#charsets
 */
public class XMLTextReader extends Reader {

    Reader reader;

    public XMLTextReader(Reader reader) {
        this.reader = reader;
    }

    public int read() throws IOException {
        return this.reader.read();
    }

    public int read(char[] cbuf) throws IOException {
        char[] tmp = new char[cbuf.length];
        int result = this.reader.read(tmp);
        int newLenght = 0;
        for (int i = 0; i < result; i++) {
            if (isXMLValid(tmp[i])) {
                cbuf[newLenght] = tmp[i];
                newLenght++;
            }
        }
        return (result < 0 ? result : newLenght);
    }

    public void close() throws IOException {
        this.reader.close();
    }

    public int read(char[] chars, int i, int i1) throws IOException {
        return read(chars, i, i1);
    }

    boolean isXMLValid(int i) {
        return !((i >= 0 && i <= 8) || i == 11 || i == 12 || (i >= 14 && i <= 31));
    }
}
