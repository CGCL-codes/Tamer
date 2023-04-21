package store;

import java.io.*;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import primitivas.Figuras;
import primitivas.Punto;

public class LecturaEscritura {

    FileWriter fstream;

    BufferedWriter out;

    Vector<primitivas.Punto> puntos;

    private String tipo = "tipo";

    private String figu = "fig";

    public void escribe(String archivo, Vector<Figuras> figuras) throws IOException {
        fstream = new FileWriter(archivo);
        out = new BufferedWriter(fstream);
        out.write("<modelador>\n");
        if (figuras != null) {
            for (int i = 0; i < figuras.size(); i++) {
                out.write("<" + figu + " " + tipo + "=\"" + figuras.get(i).tipoFig + "\" >");
                puntos = figuras.get(i).puntos;
                for (int j = 0; j < puntos.size(); j++) {
                    out.write(puntos.get(j).getXML(j));
                }
                out.write("\n</" + figu + ">\n");
            }
        }
        out.write("\n</modelador>\n");
        out.close();
    }

    public void leer(String archivo, ide.ListaFiguras listaFiguras, Punto orig) {
        int tipoFigura;
        int pasos;
        boolean primero = true;
        double xt, yt, zt;
        xt = yt = zt = 0;
        double[] coors = new double[3];
        Vector<Punto> p = new Vector<Punto>();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(archivo));
            doc.getDocumentElement().normalize();
            NodeList listFiguras = doc.getElementsByTagName(figu);
            Element firstNameElement;
            NodeList firstNameList;
            Element firstFiguraElement;
            Node firstFiguraNode;
            for (int s = 0; s < listFiguras.getLength(); s++) {
                firstFiguraNode = listFiguras.item(s);
                tipoFigura = Integer.valueOf(firstFiguraNode.getAttributes().getNamedItem(tipo).getNodeValue());
                pasos = 0;
                p = new Vector<Punto>();
                if (firstFiguraNode.getNodeType() == Node.ELEMENT_NODE) {
                    firstFiguraElement = (Element) firstFiguraNode;
                    firstNameList = firstFiguraElement.getElementsByTagName("*");
                    for (int i = 0; i < firstNameList.getLength(); i++) {
                        firstNameElement = (Element) firstNameList.item(i);
                        NodeList textFNList = firstNameElement.getChildNodes();
                        coors[pasos++] = Double.valueOf(((Node) textFNList.item(0)).getNodeValue().trim());
                        if (pasos == 3) {
                            if (orig instanceof Punto) {
                                if (primero) {
                                    xt = orig.getX() - coors[0];
                                    yt = orig.getY() - coors[1];
                                    zt = orig.getZ() - coors[2];
                                    primero = false;
                                }
                            }
                            p.add(new Punto(coors[0] + xt, coors[1] + yt, coors[2] + zt));
                            pasos = 0;
                        }
                    }
                }
                listaFiguras.insertarFigura(tipoFigura, false, p);
            }
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LecturaEscritura le = new LecturaEscritura();
        try {
            le.escribe("obi.txt", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
