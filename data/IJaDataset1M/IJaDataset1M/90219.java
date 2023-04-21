package org.opensih.gdq.Utils.Converters;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.opensih.gdq.Modelo.Paciente;

public class Xml2Rim {

    private static Xml2Rim instance = null;

    private Xml2Rim() {
    }

    public static Xml2Rim getInstance() {
        if (instance == null) {
            instance = new Xml2Rim();
        }
        return instance;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Paciente> XML2PatientBean(String xmlString) {
        List<Paciente> lista = new LinkedList<Paciente>();
        try {
            SAXBuilder saxBuilder = new SAXBuilder(false);
            Document doc = saxBuilder.build(new StringReader(xmlString));
            Element raiz = doc.getRootElement();
            String nsValue = raiz.getNamespaceURI();
            Namespace ns = Namespace.getNamespace(nsValue);
            Element cap = raiz.getChild("controlActProcess", ns);
            List sb = cap.getChildren("subject", ns);
            if (!sb.isEmpty()) {
                Iterator isb = sb.iterator();
                while (isb.hasNext()) {
                    Element e = (Element) isb.next();
                    Element re = e.getChild("registrationEvent", ns);
                    Element sb1 = re.getChild("subject1", ns);
                    Element pat = sb1.getChild("patient", ns);
                    Paciente patient = new Paciente();
                    List<Element> ids = pat.getChildren("id", ns);
                    List<String> aux = new LinkedList<String>();
                    if (ids.size() == 1) {
                        patient.setRoot_extension(ids.get(0).getAttributeValue("root") + "_" + ids.get(0).getAttributeValue("extension"));
                        aux.add(ids.get(0).getAttributeValue("extension"));
                    } else {
                        for (Element i : ids) {
                            aux.add(i.getAttributeValue("extension"));
                            if (i.getAttributeValue("root").equals("2.16.840.1.113883.2.14.2.1")) {
                                patient.setRoot_extension(i.getAttributeValue("root") + "_" + i.getAttributeValue("extension"));
                            }
                        }
                    }
                    patient.setIds(aux);
                    Element pp = pat.getChild("patientPerson", ns);
                    Element name = pp.getChild("name", ns);
                    if (name != null) {
                        Element given = name.getChild("given", ns);
                        Element family = name.getChild("family", ns);
                        if (given != null) patient.setNombre(given.getText());
                        if (family != null) patient.setApellido(family.getText());
                    }
                    Element add = pp.getChild("addr", ns);
                    if (add != null) {
                        Element street = add.getChild("streetAddressLine", ns);
                        Element city = add.getChild("city", ns);
                        Element state = add.getChild("state", ns);
                        Element country = add.getChild("country", ns);
                        if (street != null) patient.setCalle(street.getText());
                        if (city != null) patient.setCiudad(city.getText());
                        if (state != null) patient.setDepartamento(state.getText());
                        if (country != null) patient.setPais(country.getText());
                    }
                    Element sexo = pp.getChild("administrativeGenderCode", ns);
                    if (sexo != null) {
                        patient.setSexo(sexo.getAttributeValue("code"));
                    }
                    Element fn = pp.getChild("birthTime", ns);
                    if (fn != null) {
                        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyyMMdd");
                        String strFecha = fn.getAttributeValue("value");
                        Date fecha = null;
                        try {
                            fecha = formatoDelTexto.parse(strFecha);
                        } catch (Exception ex) {
                            System.out.println(e.toString());
                        }
                        patient.setFnac(fecha);
                    }
                    String telefonos = "";
                    List tels = pp.getChildren("telecom", ns);
                    if (tels != null) {
                        Iterator i = tels.iterator();
                        while (i.hasNext()) {
                            Element e1 = (Element) i.next();
                            String tel = e1.getAttributeValue("value");
                            if (tel.length() > 8) {
                                tel = tel.substring(7, tel.length() - 1);
                            }
                            telefonos += tel + "/";
                        }
                        if (telefonos.length() > 1) {
                            telefonos = telefonos.substring(0, telefonos.length() - 1);
                        }
                        patient.setTelefono(telefonos.toString());
                    }
                    lista.add(patient);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return lista;
    }
}
