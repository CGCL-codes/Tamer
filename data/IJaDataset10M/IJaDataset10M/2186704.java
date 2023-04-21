package com.opendicom.miniRIS.contextos;

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
import com.opendicom.miniRIS.entidades.Paciente;

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

    public static String encodeStr(String arg) {
        String salida = arg.toUpperCase().replaceAll("&#xe1;", "�").replaceAll("&#xc1;", "�").replaceAll("&#xe9;", "�").replaceAll("&#xc9;", "�").replaceAll("&#xed;", "�").replaceAll("&#xcd;", "�").replaceAll("&#xf3;", "�").replaceAll("&#xd3;", "�").replaceAll("&#xfa;", "�").replaceAll("&#xda;", "�").replaceAll("&#xe4;", "�").replaceAll("&#xc4;", "�").replaceAll("&#xeb;", "�").replaceAll("&#xcb;", "�").replaceAll("&#xef;", "�").replaceAll("&#xcf;", "�").replaceAll("&#xf6;", "�").replaceAll("&#xd6;", "�").replaceAll("&#xfc;", "�").replaceAll("&#xdc;", "�").replaceAll("&#xe0;", "�").replaceAll("&#xc0;", "�").replaceAll("&#xe8;", "�").replaceAll("&#xc8;", "�").replaceAll("&#xec;", "�").replaceAll("&#xcc;", "�").replaceAll("&#xf2;", "�").replaceAll("&#xd2;", "�").replaceAll("&#xf9;", "�").replaceAll("&#xd9;", "�").replaceAll("&#xf1;", "�").replaceAll("&#xd1;", "�").replaceAll("&#xbf;", "�").replaceAll("&#x3f;", "\\?").replaceAll("&#xa1;", "�").replaceAll("&#x21;", "!").replaceAll("&#xb4;", "�").replaceAll("&#x60;", "`").replaceAll("&#xe7;", "�").replaceAll("&#xc7;", "�");
        return salida;
    }

    @SuppressWarnings("unchecked")
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
                    Element id = pat.getChild("id", ns);
                    patient.setCi(id.getAttributeValue("extension"));
                    Element pp = pat.getChild("patientPerson", ns);
                    Element name = pp.getChild("name", ns);
                    if (name != null) {
                        Element given = name.getChild("given", ns);
                        Element family = name.getChild("family", ns);
                        if (given != null) {
                            patient.setNombre(encodeStr(given.getText()));
                        }
                        if (family != null) {
                            patient.setApellido(encodeStr(family.getText()));
                        }
                    }
                    Element add = pp.getChild("addr", ns);
                    if (add != null) {
                        Element street = add.getChild("streetAddressLine", ns);
                        Element city = add.getChild("city", ns);
                        Element state = add.getChild("state", ns);
                        Element country = add.getChild("country", ns);
                        if (street != null) {
                            patient.setDireccion(encodeStr(street.getText()));
                        }
                        if (city != null) {
                            patient.setCiudad(encodeStr(city.getText()));
                        }
                        if (state != null) {
                            patient.setDepartamento(encodeStr(state.getText()));
                        }
                        if (country != null) {
                            patient.setPais(encodeStr(country.getText()));
                        }
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
                            ex.printStackTrace();
                        }
                        patient.setFnac(fecha);
                    }
                    List<String> telefonos = new LinkedList<String>();
                    List tels = pp.getChildren("telecom", ns);
                    if (tels != null) {
                        Iterator i = tels.iterator();
                        while (i.hasNext()) {
                            Element e1 = (Element) i.next();
                            telefonos.add(e1.getAttributeValue("use") + ": " + e1.getAttributeValue("value"));
                        }
                        patient.setTelefono(telefonos.toString());
                    }
                    lista.add(patient);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
