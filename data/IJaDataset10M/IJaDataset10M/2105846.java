package globali;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.xml.sax.SAXException;

public class leggiXMLconfig {

    public static void main(String arg[]) {
    }

    public void caricaValori() {
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            Document xml;
            if (jcVariabili.FILE_XML.length() == 0) xml = builder.parse("configurazioni.xml"); else xml = builder.parse(jcVariabili.FILE_XML);
            NodeList ndlLibri = xml.getElementsByTagName("confi");
            int i;
            for (i = 0; i < ndlLibri.getLength(); i++) {
                Node ndLibro = ndlLibri.item(i);
                NamedNodeMap att = ndLibro.getAttributes();
                if (att.getNamedItem("nome").getNodeValue().equals("db")) {
                    jcPostgreSQL.DB = att.getNamedItem("valore").getNodeValue();
                } else if (att.getNamedItem("nome").getNodeValue().equals("db_port")) {
                    jcPostgreSQL.PORT = att.getNamedItem("valore").getNodeValue();
                } else if (att.getNamedItem("nome").getNodeValue().equals("db_ssl")) {
                    jcPostgreSQL.SSL = Boolean.parseBoolean(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("totale_decimali")) {
                    jcVariabili.TOTALI_DECIMALI = Integer.parseInt(att.getNamedItem("valore").getNodeValue());
                    jcVariabili.VISUALIZZAZIONE_CURRENCY = "###,##0.";
                    for (int c = 0; i < jcVariabili.TOTALI_DECIMALI; c++) {
                        jcVariabili.VISUALIZZAZIONE_CURRENCY += "0";
                    }
                } else if (att.getNamedItem("nome").getNodeValue().equals("autoresize")) {
                    jcVariabili.AUTORESIZE = Boolean.parseBoolean(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("file_logo")) {
                    jcVariabili.FILE_LOGO = att.getNamedItem("valore").getNodeValue();
                } else if (att.getNamedItem("nome").getNodeValue().equals("newvendita_docuid")) {
                    jcVariabili.NEW_VENDITA_DOCUID = Integer.parseInt(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("newvendita_statoevaso")) {
                    jcVariabili.NEW_VENDITA_STATO_EVASO = Integer.parseInt(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("newvendita_clienteid")) {
                    jcVariabili.NEW_VENDITA_CLIENTEID = Integer.parseInt(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("load_asterisk")) {
                    jcVariabili.LOAD_ASTERISK = Boolean.parseBoolean(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("asterisk_server")) {
                    jcVariabili.ASTERISK_SERVER = att.getNamedItem("valore").getNodeValue().toString();
                } else if (att.getNamedItem("nome").getNodeValue().equals("asterisk_context")) {
                    jcVariabili.ASTERISK_CONTEXT = att.getNamedItem("valore").getNodeValue().toString();
                } else if (att.getNamedItem("nome").getNodeValue().equals("asterisk_out")) {
                    jcVariabili.ASTERISK_OUT = att.getNamedItem("valore").getNodeValue().toString();
                } else if (att.getNamedItem("nome").getNodeValue().equals("host")) {
                    jcPostgreSQL.HOST = att.getNamedItem("valore").getNodeValue();
                } else if (att.getNamedItem("nome").getNodeValue().equals("visualizza_iva")) {
                    jcVariabili.VISUALIZZA_IVA = Boolean.parseBoolean(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("debug")) {
                    jcVariabili.DEBUG = Boolean.parseBoolean(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("stato_qnt_riaasegnate")) {
                    jcVariabili.STATO_QNT_RIASSEGNATA = Integer.parseInt(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("ftp_host")) {
                    jcVariabili.FTP_HOST = att.getNamedItem("valore").getNodeValue();
                } else if (att.getNamedItem("nome").getNodeValue().equals("ftp_user")) {
                    jcVariabili.FTP_USER = att.getNamedItem("valore").getNodeValue();
                } else if (att.getNamedItem("nome").getNodeValue().equals("ftp_link")) {
                    jcVariabili.FTP_LINK = att.getNamedItem("valore").getNodeValue();
                } else if (att.getNamedItem("nome").getNodeValue().equals("report_path")) {
                    jcVariabili.REPORT_PATH = att.getNamedItem("valore").getNodeValue();
                } else if (att.getNamedItem("nome").getNodeValue().equals("default_um")) {
                    jcVariabili.DEFAULT_UM = att.getNamedItem("valore").getNodeValue();
                } else if (att.getNamedItem("nome").getNodeValue().equals("interfaccia_tema")) {
                    jcVariabili.INTERFACCIA_TEMA = att.getNamedItem("valore").getNodeValue();
                } else if (att.getNamedItem("nome").getNodeValue().equals("servizio_tipodoc")) {
                    jcVariabili.SERVIZIO_TIPODOC = Integer.parseInt(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("servizio_stato")) {
                    jcVariabili.SERVIZIO_STATO = Integer.parseInt(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("email_smtp")) {
                    jcVariabili.EMAIL_SMTP = att.getNamedItem("valore").getNodeValue();
                } else if (att.getNamedItem("nome").getNodeValue().equals("email_smtp_ssl")) {
                    jcVariabili.EMAIL_SMTP_SSL = Boolean.parseBoolean(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("email_smtp_auth")) {
                    jcVariabili.EMAIL_SMTP_AUTH = Boolean.parseBoolean(att.getNamedItem("valore").getNodeValue());
                } else if (att.getNamedItem("nome").getNodeValue().equals("email_fatture_from")) {
                    jcVariabili.EMAIL_FATTURE_FROM = att.getNamedItem("valore").getNodeValue();
                } else if (att.getNamedItem("nome").getNodeValue().equals("email_invio_html")) {
                    jcVariabili.EMAIL_INVIO_HTML = Boolean.parseBoolean(att.getNamedItem("valore").getNodeValue());
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Impossibile trovare il file di configurazione !!", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (SAXException ex) {
        } catch (FactoryConfigurationError ex) {
        } catch (ParserConfigurationException ex) {
        }
    }
}
