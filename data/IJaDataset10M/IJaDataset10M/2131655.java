package edu.upc.lsi.kemlg.aws;

import edu.upc.lsi.kemlg.aws.input.domain.*;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author sergio
 */
public class AgentParser {

    public static AgentScenario getAgentScenarioFromXml(InputStream is) throws JAXBException {
        JAXBContext jc;
        Unmarshaller u;
        AgentScenario as;
        jc = JAXBContext.newInstance(AgentScenario.class);
        u = jc.createUnmarshaller();
        as = (AgentScenario) u.unmarshal(is);
        return as;
    }

    public static void getXmlFromContract(AgentList ct, OutputStream os) throws JAXBException {
        JAXBContext jc;
        Marshaller m;
        jc = JAXBContext.newInstance(AgentList.class);
        m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
        m.marshal(ct, os);
    }
}
