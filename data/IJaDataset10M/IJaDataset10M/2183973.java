package net.sf.afluentes.orders;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

class OrdersParser {

    void parse(OrdersHandler handler, InputSource input) throws ParserConfigurationException, SAXException, IOException {
        OrdersHandlerAdapter adapter;
        SAXParserFactory factory;
        SAXParser parser;
        adapter = new OrdersHandlerAdapter(handler);
        factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        parser = factory.newSAXParser();
        parser.parse(input, adapter);
    }
}
