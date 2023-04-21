package org.expasy.jpl.demos;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expasy.jpl.commons.collection.ExtraIterable.AbstractExtraIterator;
import org.expasy.jpl.io.ms.MSScan;
import org.expasy.jpl.io.ms.reader.MZXMLReader;

public class MzXMLReaderDemo {

    static Log logger = LogFactory.getLog(MzXMLReaderDemo.class);

    public static void main(String[] args) throws ParseException {
        MZXMLReader reader = MZXMLReader.newInstance();
        String filename = ClassLoader.getSystemResource("B06-8004_c.mzXML").getFile();
        System.out.print(Calendar.getInstance().getTime() + ",  " + filename + "\n");
        reader.parse(new File(filename));
        AbstractExtraIterator<MSScan> it = reader.iterator();
        List<MSScan> spectra = new ArrayList<MSScan>();
        while (it.hasNext()) {
            spectra.addAll(it.nextToList());
        }
        System.out.println(spectra.size() + " MS2 spectra parsed.");
        System.out.print(Calendar.getInstance().getTime() + "\n");
    }
}
