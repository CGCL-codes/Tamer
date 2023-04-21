package ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import sql.EseguiQuery;
import sql.Utils;

public class GestioneDms {

    EseguiQuery ex;

    public GestioneDms(EseguiQuery ex) {
        super();
        this.ex = ex;
    }

    public String login() {
        String username = ex.leggiPrimoCampo("select * from opzioni", "username_dms");
        String password = ex.leggiPrimoCampo("select * from opzioni", "password_dms");
        try {
            ws.DmsService_Service service = new ws.DmsService_Service();
            ws.DmsService port = service.getDmsServiceImplPort();
            java.lang.String result = port.login(username, password);
            System.out.println("Result = " + result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }

    public boolean creaDocumento(String descrizione, String nomeFile) {
        String sid = login();
        try {
            Utils utils = new Utils();
            String sourceDate = utils.nowDateTimeMysql();
            ws.DmsService_Service service = new ws.DmsService_Service();
            ws.DmsService port = service.getDmsServiceImplPort();
            long folder = 29;
            if (descrizione.indexOf("Fattura_Vendita") > 0) {
                folder = Long.parseLong(ex.leggiPrimoCampo("select * from opzioni", "id_fatture_vendita_dms"));
            }
            if (descrizione.indexOf("Fattura_Acquisto") > 0) {
                folder = Long.parseLong(ex.leggiPrimoCampo("select * from opzioni", "id_fatture_acquisto_dms"));
            }
            if (descrizione.indexOf("DDT_Vendita") > 0) {
                folder = Long.parseLong(ex.leggiPrimoCampo("select * from opzioni", "id_ddt_vendita_dms"));
                ;
            }
            if (descrizione.indexOf("DDT_Acquisto") > 0) {
                folder = Long.parseLong(ex.leggiPrimoCampo("select * from opzioni", "id_ddt_acquisto_dms"));
                ;
            }
            if (descrizione.indexOf("Nota_Credito") > 0) {
                folder = Long.parseLong(ex.leggiPrimoCampo("select * from opzioni", "id_note_credito_dms"));
            }
            if (descrizione.indexOf("Nota_Debito") > 0) {
                folder = Long.parseLong(ex.leggiPrimoCampo("select * from opzioni", "id_note_debito_dms"));
            }
            if (descrizione.indexOf("Ordine_Acquisto") > 0) {
                folder = Long.parseLong(ex.leggiPrimoCampo("select * from opzioni", "id_ordini_acquisto_dms"));
            }
            if (descrizione.indexOf("Ordine_Vendita") > 0) {
                folder = Long.parseLong(ex.leggiPrimoCampo("select * from opzioni", "id_ordini_vendita_dms"));
            }
            java.lang.String docTitle = descrizione;
            java.lang.String source = "source";
            java.lang.String sourceAuthor = "admin";
            java.lang.String sourceType = "pdf";
            java.lang.String coverage = "";
            java.lang.String language = "it";
            java.lang.String tags = "";
            java.lang.String versionDesc = "";
            java.lang.String filename = nomeFile;
            File file = new File(nomeFile);
            String _content = "0123456789";
            byte[] content = this.getBytesFromFile(file);
            java.lang.String templateName = null;
            java.util.List<ws.Attribute> templateFields = null;
            java.lang.String sourceId = "";
            java.lang.String object = "";
            java.lang.String recipient = "";
            java.lang.String customId = null;
            String result = port.createDocument(sid, folder, descrizione, "", sourceDate, "admin", "prova", "prova", "it", "keywords", "versionDesc", descrizione, content, null, null, "sourceId", "object", "recipient", null);
            System.out.println("Result = " + result);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}
