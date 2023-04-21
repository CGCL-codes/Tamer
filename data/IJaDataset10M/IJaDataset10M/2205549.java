package DnD;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import DnD.DnD_Enums.DnD_Clase;
import DnD.DnD_Enums.DnD_StatName;

public class DnD_XML {

    public static void salvarXML(DnD_Planilla p, String dir) {
        try {
            FileWriter writer = new FileWriter(new File(dir));
            writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            writer.write("<Personaje>\n");
            writer.write("<Nombre>" + p.getNombre() + "</Nombre>\n");
            writer.write("<Clase>" + p.getClase() + "</Clase>\n");
            writer.write("<Exp>" + p.getExp() + "</Exp>\n");
            for (DnD_StatName s : DnD_StatName.values()) writer.write("<" + s.toString() + ">" + p.getStat(s) + "</" + s.toString() + ">\n");
            writer.write("</Personaje>\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error en escritura de archivo para " + dir);
        }
    }

    public static DnD_Planilla cargarXML(String dir) {
        DnD_Planilla p = null;
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(dir));
            doc.getDocumentElement().normalize();
            Element e = (Element) doc.getFirstChild();
            p = new DnD_Planilla(geterMierda(e, "Nombre"), DnD_Clase.valueOf(geterMierda(e, "Clase")));
            for (DnD_StatName sn : DnD_StatName.values()) p.setStat(sn, Integer.parseInt(geterMierda(e, sn.toString())));
        } catch (Exception e) {
            System.err.println("Error en lectura de archivo para " + dir);
        }
        return p;
    }

    private static String geterMierda(Element e, String id) {
        return e.getElementsByTagName(id).item(0).getFirstChild().getNodeValue().toString();
    }
}
