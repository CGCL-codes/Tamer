package org.gridtrust.ppm.impl.policy.normalizer.jaxb;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gridtrust.ppm.impl.policy.bind.jaxb.TargetType;

public class JAXBUtil {

    private static final Log log = LogFactory.getLog(JAXBUtil.class);

    public static Object unmarshall(String packageName, String xmlContent, Class rootClass) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(packageName);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(new StreamSource(new StringReader(xmlContent)), rootClass);
        if (obj instanceof JAXBElement) {
            obj = ((JAXBElement) obj).getValue();
        }
        return obj;
    }

    public static String marshall(String packageName, Object object) throws Exception {
        String content = "";
        JAXBContext jc = JAXBContext.newInstance(packageName);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(object, out);
        content = new String(out.toByteArray());
        return content;
    }

    public static void marshall(String packageName, Object object, OutputStream out) throws Exception {
        try {
            JAXBContext jc = JAXBContext.newInstance(packageName);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(object, out);
        } catch (Exception e) {
            log.error("Marshalling error ", e);
        } finally {
            out.close();
        }
    }

    public static boolean isEmptyTarget(TargetType target) {
        boolean isEmpty = false;
        if (target == null) {
            isEmpty = true;
        } else {
            if (target.getSubjects() == null && target.getActions() == null && target.getResources() == null) {
                isEmpty = true;
            } else {
                if (target.getSubjects() != null && target.getActions() != null && target.getResources() != null) {
                    if ((target.getSubjects().getSubject() != null) && (target.getResources().getResource() != null) && (target.getActions().getAction() != null)) {
                        isEmpty = true;
                    } else {
                        isEmpty = false;
                    }
                }
            }
        }
        return isEmpty;
    }

    public static TargetType getEmptyTarget() {
        TargetType target = new TargetType();
        return target;
    }
}
