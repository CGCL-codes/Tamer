package org.achup.elgenerador.xml.generator.basic;

import org.achup.elgenerador.generator.basic.IntegerGenerator;
import org.achup.elgenerador.xml.ElementHelper;
import org.achup.elgenerador.xml.ElementParser;
import org.achup.elgenerador.xml.ElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Marco Bassaletti
 */
public class IntegerGeneratorElement implements ElementParser<IntegerGenerator>, ElementWriter<IntegerGenerator> {

    @Override
    public IntegerGenerator parseXML(Element element) {
        if (element.getNodeName().compareTo("integer") != 0) {
            throw new IllegalArgumentException("Element name differs from integer.");
        }
        IntegerGenerator generator = new IntegerGenerator();
        try {
            int min = ElementHelper.getIntAttribute(element, "min");
            generator.setMin(min);
        } catch (Exception ex) {
        }
        try {
            int max = ElementHelper.getIntAttribute(element, "max");
            generator.setMax(max);
        } catch (Exception ex) {
        }
        try {
            boolean autoIncrement = ElementHelper.getBooleanAttribute(element, "autoincrement");
            generator.setIncremental(autoIncrement);
        } catch (Exception ex) {
        }
        try {
            long randomSeed = ElementHelper.getLongAttribute(element, "random-seed");
            generator.setSeed(randomSeed);
        } catch (Exception ex) {
        }
        return generator;
    }

    @Override
    public Element createXML(Document document, IntegerGenerator object) {
        Element element = document.createElement("integer");
        boolean autoincrement = object.isIncremental();
        if (autoincrement) {
            element.setAttribute("autoincrement", "true");
        }
        int min = object.getMin();
        element.setAttribute("min", String.valueOf(min));
        int max = object.getMax();
        element.setAttribute("max", String.valueOf(max));
        ElementHelper.writeAttribute(element, "random-seed", object.getSeed());
        return element;
    }
}
