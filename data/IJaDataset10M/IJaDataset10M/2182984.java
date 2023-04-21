package indexado;

import java.io.*;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/*******************************************
 * Implementation class Create_index_aux
 *******************************************/
public class Create_index_aux {

    private static Document doc = null;

    private static Hashtable<String, Integer> tablaH = new Hashtable<String, Integer>();

    private static boolean encUTF8 = true;

    private static String cod_centro = "";

    /***********************************************************************************************
	 * M�todo que es llamado desde el servlet Create_index. Se utliza para parsear los ficheros XML
	 * @param fich - Fichero XML a parsear
	 * @param con - Conexi�n con la BD
	 * @param TableEti - Tabla d�nde se almacenan las etiquetas junto con la informaci�n que contienen
	 * @param TableDataEtiq - Tabla d�nde se almacena la etiqueta junto al fichero XML d�nde aparece
	 ***********************************************************************************************/
    public static void parsearXML(String fich, Connection con, String TableEti, String TableDataEtiq) {
        String fichero;
        try {
            int indice = fich.indexOf("_C");
            cod_centro = fich.substring(indice, fich.indexOf("/", indice));
            fichero = LecturaFichero(fich);
            doc = string2DOM(fichero);
            if (doc != null) imprimirNodos(doc, "", con, TableEti, TableDataEtiq, fich); else {
                encUTF8 = false;
                parsearXML(fich, con, TableEti, TableDataEtiq);
                encUTF8 = true;
            }
            tablaH.clear();
        } catch (Exception error) {
            System.out.println(error.getMessage());
        }
    }

    /****************************************************************************************************
	 * M�todo que lee un fichero XML que se le pasa como par�metro y inserta su contenido en una variable
	 * de tipo String
	 * @param fich - String que contiene el nombre del fichero XML
	 * @return - Devuelve un String con el contenido del fichero XML
	 ****************************************************************************************************/
    private static String LecturaFichero(String fich) {
        String fich_completo = "";
        try {
            File archivo = new File(fich);
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String linea = "";
            if (!encUTF8) {
                while ((linea = br.readLine()) != null) {
                    if (linea.contains("encoding=\"UTF-8\"?>")) {
                        linea = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>";
                    }
                    if (!linea.contains("<!DOCTYPE")) fich_completo += linea;
                }
                fich_completo = fich_completo.replaceAll("á", "�");
                fich_completo = fich_completo.replaceAll("À", "�");
                fich_completo = fich_completo.replaceAll("è", "�");
                fich_completo = fich_completo.replaceAll("é", "�");
                fich_completo = fich_completo.replaceAll("í", "�");
                fich_completo = fich_completo.replaceAll("ì", "�");
                fich_completo = fich_completo.replaceAll("ó", "�");
                fich_completo = fich_completo.replaceAll("ò", "�");
                fich_completo = fich_completo.replaceAll("Ò", "�");
                fich_completo = fich_completo.replaceAll("Ó", "�");
                fich_completo = fich_completo.replaceAll("ú", "�");
                fich_completo = fich_completo.replaceAll("ñ", "�");
                fich_completo = fich_completo.replace("à", "�");
            } else {
                while ((linea = br.readLine()) != null) {
                    if (!linea.contains("<!DOCTYPE")) fich_completo += linea;
                }
            }
            fr.close();
            br.close();
            fich_completo = fich_completo.replaceAll("<br/>", " ");
            fich_completo = fich_completo.replaceAll("&quot;", "'");
            fich_completo = fich_completo.replaceAll("&apos;", "'");
            fich_completo = fich_completo.replaceAll("<b>", "");
            fich_completo = fich_completo.replaceAll("</b>", "");
            fich_completo = fich_completo.replaceAll("<ul>", "");
            fich_completo = fich_completo.replaceAll("</ul>", "");
            fich_completo = fich_completo.replaceAll("<li>", "");
            fich_completo = fich_completo.replaceAll("</li>", "");
        } catch (IOException e) {
            System.out.println("(Create_index_aux.java->Lectura Fichero)Error= " + e.getMessage());
        }
        return fich_completo;
    }

    /**********************************************************************************
	 * M�todo que convierte un fichero en formato String en un objeto de tipo Document
	 * @param s - Fichero XML en formato String
	 * @return - Objeto de tipo Document
	 **********************************************************************************/
    private static Document string2DOM(String s) {
        Document tmpX = null;
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (javax.xml.parsers.ParserConfigurationException error) {
            System.out.println("(Create_index_aux.java)Error= Creando factory String2DOM " + error.getMessage());
            return null;
        }
        try {
            tmpX = builder.parse(new ByteArrayInputStream(s.getBytes()));
        } catch (org.xml.sax.SAXException error) {
            System.out.println("(Create_index_aux.java)Error= parseo SAX String2DOM " + error.getMessage());
            return null;
        } catch (IOException error) {
            System.out.println("(Create_index_aux.java)Error= generando Bytes String2DOM " + error.getMessage());
            return null;
        }
        return tmpX;
    }

    /*******************************************************************************************************
	 * M�todo recursivo que procesa los nodos del objeto Document creado
	 * @param node - Nodo del objeto Document
	 * @param ruta - Ruta desde el nodo ra�z hasta una etiqueta hoja que contiene informaci�n
	 * @param con - Conexi�n a la BD
	 * @param TableEti - Tabla donde se almacenan las etiquetas junto a la informaci�n que contienen
	 * @param TableDataEtiq - Tabla donde se almacenan las etiquetas junto al fichero donde aparecen
	 * @param fich - ruta del fichero XML que se est� procesando 
	 *******************************************************************************************************/
    private static void imprimirNodos(Node node, String ruta, Connection con, String TableEti, String TableDataEtiq, String fich) {
        int type = node.getNodeType();
        if (node == null) {
            return;
        } else {
            switch(type) {
                case Node.DOCUMENT_NODE:
                    {
                        NodeList children = node.getChildNodes();
                        for (int iChild = 0; iChild < children.getLength(); iChild++) {
                            imprimirNodos(children.item(iChild), "", con, TableEti, TableDataEtiq, fich);
                        }
                        break;
                    }
                case Node.ELEMENT_NODE:
                    {
                        if (ruta == "") {
                            ruta = cod_centro + "/";
                            ruta += node.getNodeName();
                        } else ruta = ruta + "/" + node.getNodeName();
                        Element e = (Element) node;
                        if (e.hasAttribute("id")) {
                            ruta = ruta + "[id=" + e.getAttribute("id") + "]";
                        } else if (e.hasAttribute("identificador")) {
                            ruta = ruta + "[id=" + e.getAttribute("identificador") + "]";
                        }
                        NodeList children = node.getChildNodes();
                        if (children != null) {
                            int len = children.getLength();
                            for (int i = 0; i < len; i++) {
                                imprimirNodos(children.item(i), ruta, con, TableEti, TableDataEtiq, fich);
                            }
                        }
                        break;
                    }
                case Node.TEXT_NODE:
                    {
                        String cad;
                        cad = node.getNodeValue().replaceAll("\\t", "");
                        cad = cad.replaceAll("\\n", "");
                        cad = cad.replaceAll(" ", "");
                        if (cad.length() != 0) {
                            if (!tablaH.containsKey(ruta)) {
                                tablaH.put(ruta, new Integer(1));
                                ruta = ruta + "[" + tablaH.get(ruta) + "]";
                            } else {
                                Integer valor = (Integer) tablaH.get(ruta);
                                valor++;
                                tablaH.put(ruta, valor);
                                ruta = ruta + "[" + valor + "]";
                            }
                            try {
                                Statement stmt = con.createStatement();
                                String queryIns = "INSERT INTO " + TableEti + " VALUES(\"" + ruta + "\",\"" + node.getNodeValue() + "\");";
                                stmt.executeUpdate(queryIns);
                                queryIns = "INSERT INTO " + TableDataEtiq + " VALUES(\"" + fich + "\",\"" + ruta + "\");";
                                stmt.executeUpdate(queryIns);
                                stmt.close();
                            } catch (SQLException ex) {
                                System.out.println("(Create_index_aux.java)Error= " + ex.getMessage());
                            }
                        }
                        break;
                    }
            }
        }
    }
}
