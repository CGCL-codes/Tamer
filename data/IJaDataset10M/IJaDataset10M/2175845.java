package org.mss.mozilla.model.mozilla;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

public class Main {

    public static void main(String... args) {
        JAXBTemplateBean bean = new JAXBTemplateBean();
        bean.setStartrow(1);
        bean.setFilter("filter");
        bean.setJob_schedule("0815");
        bean.setMaxrow(10);
        bean.setPassword("password");
        bean.setTabletype("vertical");
        bean.setTablexpath("/td/table");
        bean.setType("stammdaten");
        bean.setUsername("hans");
        URLModel mymodel = new URLModel();
        mymodel.setLinkName("welt");
        mymodel.setPosition(1);
        mymodel.setType("static");
        mymodel.setURL("www.welt.de");
        TableField myfields = new TableField();
        bean.setUrlmodel(mymodel);
        bean.setTablefields(myfields);
        JAXBContext context = null;
        Marshaller m;
        try {
            context = JAXBContext.newInstance(JAXBTemplateBean.class);
            m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(bean, System.out);
            Writer w = null;
            try {
                w = new FileWriter("c:\\mozilla-jaxb.xml");
                m.marshal(bean, w);
            } finally {
                try {
                    w.close();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            System.out.println(" Marshal ");
            e.printStackTrace();
        }
        try {
            Unmarshaller um = context.createUnmarshaller();
            JAXBTemplateBean umbean = (JAXBTemplateBean) um.unmarshal(new FileReader("c:\\mozilla-jaxb.xml"));
            System.out.println(umbean.getUrlmodel().getURL());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
