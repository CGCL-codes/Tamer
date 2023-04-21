package com.gcapmedia.dab.epg.binary;

import java.io.*;
import org.testng.annotations.Test;
import com.gcapmedia.dab.epg.Epg;
import com.gcapmedia.dab.epg.MarshallException;
import com.gcapmedia.dab.epg.xml.EpgXmlMarshaller;

/**
 * 
 */
public class FileReaderTest {

    @Test
    public void readFile() throws IOException, MarshallException {
        FileInputStream stream = new FileInputStream(new File("src/test/data/CE1564D6.DAT"));
        byte[] bytes = new byte[stream.available()];
        stream.read(bytes);
        System.out.println("read " + bytes.length + " bytes");
        System.out.println(BitBuilder.printByteArray(bytes));
        EpgBinaryMarshaller marshaller = new EpgBinaryMarshaller();
        Epg epg = marshaller.unmarshall(bytes);
        System.out.println(epg);
        EpgXmlMarshaller xml = new EpgXmlMarshaller();
        System.out.println(new String(xml.marshall(epg)));
    }
}
