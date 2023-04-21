package convertemployeesfromxmltomysql;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.bpd.pojos.VwSsMaEmpleadosActivos;
import pk_util.UtilPojosModel;

/**
 * @author u19730
 */
public class XMLReaderByName {

    public static void main(String argv[]) throws ParserConfigurationException, MalformedURLException, IOException, SAXException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            URL url = new URL("http://co01sgd/sgdcent/cnt/vw_ss_ma_empleados_activos_to_xml.asp");
            InputStream inputStream = url.openStream();
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList employees = doc.getElementsByTagName("empleado");
            for (int emp = 0; emp < employees.getLength(); emp++) {
                Node fstNode = employees.item(emp);
                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element employee = (Element) fstNode;
                    Integer codigoEmpleado = Integer.parseInt((employee.getElementsByTagName("codigo_empleado").item(0).getChildNodes().item(0)).getNodeValue().toString().trim());
                    Integer mgrEmpId = Integer.parseInt((employee.getElementsByTagName("mgr_emp_id").item(0).getChildNodes().item(0)).getNodeValue().toString().trim());
                    VwSsMaEmpleadosActivos result = updateEmployee(codigoEmpleado, mgrEmpId);
                    if (result != null) {
                        System.out.println("RES: codigoEmpleado: " + result.getCodigoEmpleado() + " | mgrEmpId: " + result.getMgrEmpId());
                        System.out.println("");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static VwSsMaEmpleadosActivos updateEmployee(Integer codigoEmpleado, Integer mgrEmpId) {
        Session s = UtilPojosModel.getSessionFactory().openSession();
        Criteria c = s.createCriteria(VwSsMaEmpleadosActivos.class);
        c.add(Restrictions.eq("codigoEmpleado", codigoEmpleado));
        VwSsMaEmpleadosActivos data = (VwSsMaEmpleadosActivos) c.uniqueResult();
        if (data != null) {
            data.setMgrEmpId(mgrEmpId);
            s.beginTransaction();
            s.saveOrUpdate(data);
            s.getTransaction().commit();
        }
        s.close();
        return data;
    }
}
