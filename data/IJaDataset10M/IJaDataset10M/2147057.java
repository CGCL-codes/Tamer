package pl.edu.pw.polygen.modeler.server.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.tools.ant.filters.StringInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JaxbTest {

    private JAXBContext context;

    private Marshaller marshaller;

    private Unmarshaller unmarshaller;

    private String marshalledData;

    @Before
    public void setUp() throws Exception {
        context = JAXBContext.newInstance(PolygenData.class);
        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        unmarshaller = context.createUnmarshaller();
    }

    private PolygenData getData() {
        PolygenData polygenData = new PolygenData();
        Vertex v1 = new Vertex();
        v1.setId(0);
        v1.setX(10D);
        v1.setY(20D);
        v1.setType("NORMAL_VERTEX");
        Vertex v2 = new Vertex();
        v2.setId(1);
        v2.setX(40D);
        v2.setY(50D);
        v2.setType("NORMAL_VERTEX");
        Edge edge = new Edge();
        edge.setBegin(0);
        edge.setEnd(1);
        edge.setType("SEGMENT");
        List<Edge> edges = new ArrayList<Edge>();
        edges.add(edge);
        v1.setEdges(edges);
        List<Vertex> vertexes = new ArrayList<Vertex>();
        vertexes.add(v1);
        vertexes.add(v2);
        polygenData.setVertexes(vertexes);
        return polygenData;
    }

    @Test
    public void marshalTest() {
        try {
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(getData(), stringWriter);
            System.out.println(stringWriter.toString());
            marshalledData = stringWriter.toString();
        } catch (Exception e) {
            System.err.println("cos nie tak");
            e.printStackTrace();
        }
        Assert.assertTrue(marshalledData != null && marshalledData.isEmpty() == false);
    }

    @Test
    public void unmarshalTest() {
        try {
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(getData(), stringWriter);
            marshalledData = stringWriter.toString();
            InputStream is = new ByteArrayInputStream(marshalledData.getBytes("UTF-8"));
            PolygenData data = (PolygenData) unmarshaller.unmarshal(is);
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
