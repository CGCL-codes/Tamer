package net.sf.xml2cb.bench;

import net.sf.xml2cb.core.Marshaller;
import net.sf.xml2cb.core.io.XmlWriter;
import net.sf.xml2cb.core.io.XmlWriterNull;

public class Flat2XmlWriterNullBench extends Flat2XmlBench {

    private XmlWriter xmlWriter = new XmlWriterNull();

    protected void doLoop(int loops, byte[] bytes, Marshaller marshaller) throws Exception {
        for (int i = 0; i < loops; i++) {
            marshaller.unmarshall(bytes, xmlWriter);
        }
    }
}
