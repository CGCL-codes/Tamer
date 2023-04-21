package org.achup.generador.project.writer.generator.basic;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Locale;
import org.achup.generador.generator.basic.DateGenerator;
import org.achup.generador.project.writer.ElementWriter;
import org.achup.generador.project.writer.ElementWriterException;
import org.achup.generador.project.writer.WriterHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Marco Bassaletti Olivos.
 *
 */
public class DateGeneratorWriter implements ElementWriter<DateGenerator> {

    @Override
    public Element createXML(Document document, DateGenerator object) throws ElementWriterException {
        Element element = document.createElement("date");
        Date min = object.getMin();
        Date max = object.getMax();
        String minStr = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).format(min);
        String maxStr = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).format(max);
        element.setAttribute("min", minStr);
        element.setAttribute("max", maxStr);
        WriterHelper.writeAttribute(element, "random-seed", object.getSeed());
        return element;
    }
}
