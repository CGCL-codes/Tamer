package eu.fbk.hlt.common.output;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import eu.fbk.hlt.common.conffile.Configuration;

/**
 * @author Milen Kouylekov
 */
public class DefaultOutputStream extends CustomOutputStream {

    @Override
    public void print(Object str) {
        if (str instanceof Configuration) {
            System.out.print(toString((Configuration) str));
            return;
        }
        System.out.print(str.toString());
    }

    @Override
    public void print(String str) {
        System.out.print(str);
    }

    @Override
    public void println(Object str) {
        print(str);
        System.out.println();
    }

    @Override
    public void println(String str) {
        System.out.println(str);
    }

    public static String toString(Configuration s) {
        try {
            Marshaller marshaller = JAXBContext.newInstance("eu.fbk.hlt.common.conffile").createMarshaller();
            StringWriter fos = new StringWriter();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(new eu.fbk.hlt.common.conffile.ObjectFactory().createModule(s), fos);
            fos.close();
            return fos.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
