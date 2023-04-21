package ru.spb.leonidv.opensearch.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * 
 * @author leonidv
 */
public class Test {

    public static void main(String args[]) {
        JAXBContext context;
        try {
            ObjectFactory factory = new ObjectFactory();
            context = JAXBContext.newInstance("ru.spb.leonidv.opensearch.xml.opensearch");
            Marshaller marshaller = context.createMarshaller();
            SearchPlugin plugin = factory.createSearchPlugin();
            plugin.setShortName("Vkontakte.ru");
            plugin.setDescription("Description");
            UrlInfo url = factory.createUrlInfo();
            url.setTemplate("test template string");
            plugin.setURL(url);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(plugin, System.out);
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }
}
