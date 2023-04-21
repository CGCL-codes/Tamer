package org.gruposp2p.aula.gwt.client.representation;

import java.util.List;
import org.gruposp2p.aula.gwt.client.event.AulaEventBus;
import org.gruposp2p.aula.gwt.client.event.StudentChangeEvent;
import org.gruposp2p.aula.gwt.client.model.Course;
import org.gruposp2p.aula.gwt.client.model.Student;
import org.gruposp2p.aula.gwt.client.util.LinkConstants;
import org.restlet.gwt.Callback;
import org.restlet.gwt.Client;
import org.restlet.gwt.data.MediaType;
import org.restlet.gwt.data.Protocol;
import org.restlet.gwt.data.Request;
import org.restlet.gwt.data.Response;
import org.restlet.gwt.resource.Representation;
import org.restlet.gwt.resource.StringRepresentation;
import org.restlet.gwt.resource.XmlRepresentation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;

public class StudentsRepresentation {

    private static StudentsRepresentation instance = new StudentsRepresentation();

    private XmlRepresentation xmlRepresentation;

    private StudentsRepresentation() {
    }

    /**
	 * Método que extrae lista de cursos a partir de un XML como el siguiente:
	 * 
	 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	 *	<aula>
     *		<cursos>
     *   		<curso nombre="Curso 20">
     *      		<descripcion>Descripción curso 20</descripcion>
     *    		</curso>
     *    		<curso nombre="Curso 99">
     *        		<descripcion>Descripción curso 99</descripcion>
     *    		</curso>
     *		</cursos>
	 *	</aula>
	 *
	 * @param xmlRepresentation recibida del servidor
	 * @return lista con los cursos incluidos en la representación recibida
	 */
    public Student getStudentFromXMlRepresentation(XmlRepresentation xmlRepresentation) {
        Student student = new Student();
        Document xmlDoc = xmlRepresentation.getDocument();
        GWT.log("xmlDoc.toString(): " + xmlDoc.toString(), null);
        final Element root = xmlDoc.getDocumentElement();
        XMLParser.removeWhitespace(xmlDoc);
        NodeList studentNodes = root.getElementsByTagName("student");
        NodeList addressNodes = root.getElementsByTagName("address");
        Element address = (Element) addressNodes.item(0);
        GWT.log("studentNodes.getLength(): " + studentNodes.getLength(), null);
        GWT.log("address.toString(): " + address.toString(), null);
        Element studentAddress = (Element) address.getElementsByTagName("student").item(0);
        GWT.log("studentAddress.toString(): " + studentAddress.toString(), null);
        for (int i = 0; i < studentNodes.getLength(); i++) {
            final Element courseElement = (Element) studentNodes.item(i);
            GWT.log("### courseElement.toString(): " + courseElement.toString(), null);
        }
        return student;
    }

    public void setXmlRepresentation(Document document) {
        GWT.log(document.toString(), null);
    }

    public XmlRepresentation getXmlRepresentation() {
        return xmlRepresentation;
    }

    /**
	 * Método que monta un XML como el siguiente a partir de la lista
	 * de cursos que se le pasa:
	 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	 *	<aula>
     *		<cursos>
     *   		<curso nombre="Curso 20">
     *      		<descripcion>Descripción curso 20</descripcion>
     *    		</curso>
     *    		<curso nombre="Curso 99">
     *        		<descripcion>Descripción curso 99</descripcion>
     *    		</curso>
     *		</cursos>
	 *	</aula>
	 * El XML se envía indicando que su formato es:
	 * application/vnd.gruposp2p.org.cursos_input+xml
	 */
    public Representation geRepresentation(List<Course> courses) {
        StringRepresentation result = null;
        Document xmlDoc = XMLParser.createDocument();
        Element root = xmlDoc.createElement("aula");
        Element coursesElement = xmlDoc.createElement("cursos");
        for (Course course : courses) {
            Element courseElement = xmlDoc.createElement("curso");
            Element courseElementDescription = xmlDoc.createElement("descripcion");
            Text descriptionTxt = xmlDoc.createTextNode(course.getDescription());
            courseElementDescription.appendChild(descriptionTxt);
            courseElement.appendChild(courseElementDescription);
            coursesElement.appendChild(courseElement);
        }
        root.appendChild(coursesElement);
        result = new StringRepresentation(root.toString());
        MediaType mediaType = new MediaType("application/vnd.gruposp2p.org.cursos_input+xml");
        result.setMediaType(mediaType);
        return result;
    }

    public static StudentsRepresentation getInstance() {
        return instance;
    }

    public void getStudents() {
        final Client client = new Client(Protocol.HTTP);
        client.get(LinkConstants.getStudentsLink(), new Callback() {

            @Override
            public void onEvent(Request request, Response response) {
                StudentChangeEvent studentChangeEvent = new StudentChangeEvent();
                Student student = getStudentFromXMlRepresentation(response.getEntityAsXml());
                studentChangeEvent.setStudent(student);
                AulaEventBus.getInstance().fireEvent(studentChangeEvent);
            }
        });
    }

    public void postHTTP(List<Course> courses) {
        final Client client = new Client(Protocol.HTTP);
        client.post(LinkConstants.getCoursesLink(), geRepresentation(courses), new Callback() {

            @Override
            public void onEvent(Request request, Response response) {
                GWT.log("response.getStatus(): " + response.getStatus(), null);
            }
        });
    }
}
