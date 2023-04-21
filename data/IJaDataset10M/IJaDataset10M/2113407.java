package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.digester.Digester;
import org.exolab.castor.mapping.Mapping;
import org.skins.core.config.XmlBeanElement;
import org.skins.util.XmlUtil;
import org.xml.sax.InputSource;

public class TestCastor {

    /**
	 * @param args
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
        Mapping m = new Mapping();
        m.loadMapping(new InputSource(TestCastor.class.getResourceAsStream("/org/skins/core/config/etc/castor/config.cm.xml")));
        System.out.println(TestCastor.class.getResourceAsStream("/example/application.xml"));
        List obj = (List) XmlUtil.unmarshall(m, TestCastor.class.getResourceAsStream("/example/application.xml"), ArrayList.class);
        Iterator it = obj.iterator();
        while (it.hasNext()) {
            Object o = (Object) it.next();
            if (o instanceof XmlBeanElement) {
                XmlBeanElement ba = (XmlBeanElement) o;
                Collection interceptors = ba.getInterceptors();
                System.out.println(interceptors);
            }
        }
        System.out.println(obj);
    }
}
