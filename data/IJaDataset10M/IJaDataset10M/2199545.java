package net.disy.ogc.wps.v_1_0_0.converter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class StringCalendarConverter extends StringConverter<XMLGregorianCalendar> {

    @Override
    public Class<XMLGregorianCalendar> getDestinationClass() {
        return XMLGregorianCalendar.class;
    }

    @Override
    public XMLGregorianCalendar convertToNotNull(String source) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(source);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertFromNotNull(XMLGregorianCalendar destination) {
        return destination.toXMLFormat();
    }
}
