package hu.sztaki.lpds.dcibridge.service;

import dci.extension.ExtensionType;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import org.ggf.schemas.bes._2006._08.bes_factory.ActivityDocumentType;
import org.ggf.schemas.bes._2006._08.bes_factory.BESFactoryService;
import org.ggf.schemas.bes._2006._08.bes_factory.CreateActivityResponseType;
import org.ggf.schemas.bes._2006._08.bes_factory.CreateActivityType;
import org.ggf.schemas.jsdl._2005._11.jsdl.JobDefinitionType;
import org.ggf.schemas.jsdl._2005._11.jsdl_posix.ArgumentType;
import org.ggf.schemas.jsdl._2005._11.jsdl_posix.FileNameType;
import org.ggf.schemas.jsdl._2005._11.jsdl_posix.GroupNameType;
import org.ggf.schemas.jsdl._2005._11.jsdl_posix.LimitsType;
import org.ggf.schemas.jsdl._2005._11.jsdl_posix.POSIXApplicationType;
import org.ggf.schemas.jsdl._2005._11.jsdl_posix.UserNameType;
import uri.mbschedulingdescriptionlanguage.SDLType;

/**
 *
 * @author lpds
 */
public class DCIBridgeClient {

    public CreateActivityResponseType call(Job pJob) throws Exception {
        BESFactoryService client = new BESFactoryService(pJob.getForwardURL(), new QName("http://schemas.ggf.org/bes/2006/08/bes-factory", "BESFactoryService"));
        CreateActivityType cat = new CreateActivityType();
        cat.setActivityDocument(new ActivityDocumentType());
        JobDefinitionType jsdl = readJSDLFromString(readJSDLFile(pJob));
        cat.getActivityDocument().setJobDefinition(jsdl);
        return client.getBESFactoryPort().createActivity(cat);
    }

    private Class[] classes = new Class[] { JobDefinitionType.class, UserNameType.class, GroupNameType.class, FileNameType.class, ArgumentType.class, LimitsType.class, POSIXApplicationType.class, ExtensionType.class, SDLType.class };

    private String readJSDLFile(Job pJob) throws FileNotFoundException, IOException {
        String res = "";
        String ln = "";
        String base = Base.getI().getJobDirectory(pJob.getId());
        File job = new File(base + "outputs/guse.jsdl");
        BufferedReader br = new BufferedReader(new FileReader(job));
        while ((ln = br.readLine()) != null) res = res.concat(ln + "\n");
        br.close();
        return res;
    }

    private String getJSDLXML(JobDefinitionType pValue) throws Exception {
        ByteArrayOutputStream res = new ByteArrayOutputStream();
        try {
            JAXBContext jc = JAXBContext.newInstance(classes);
            Marshaller msh = jc.createMarshaller();
            JAXBElement<JobDefinitionType> jbx = wrap("http://schemas.ggf.org/jsdl/2005/11/jsdl", "JobDefinition_Type", pValue);
            msh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            msh.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://schemas.ggf.org/jsdl/2005/11/jsdl");
            msh.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            msh.marshal(jbx, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(res.toByteArray());
    }

    private JobDefinitionType readJSDLFromString(String pValue) throws JAXBException, FileNotFoundException {
        JAXBContext jc = JAXBContext.newInstance(classes);
        Unmarshaller u = jc.createUnmarshaller();
        JAXBElement<JobDefinitionType> obj = u.unmarshal(new StreamSource(new StringReader(pValue)), JobDefinitionType.class);
        JobDefinitionType jsdl = (JobDefinitionType) obj.getValue();
        return jsdl;
    }

    private static <T> JAXBElement<T> wrap(String ns, String tag, T o) {
        QName qtag = new QName(ns, tag, "jsdl");
        Class<?> clazz = o.getClass();
        @SuppressWarnings("unchecked") JAXBElement<T> jbe = new JAXBElement(qtag, clazz, o);
        return jbe;
    }
}
