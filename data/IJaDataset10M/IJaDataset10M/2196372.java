package gui.actionservlet;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vdom.diffing.Hash;

/**
 * @author Mike
 * Classe che crea un tabella con i file presenti nella cache
 * I file vengono presentati nello stesso ordine di caricamento
 */
public class FileList extends Action {

    protected void Get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String content = "<form action=\"FileList\" method=\"post\"  enctype=\"multipart/form-data\">" + "<input type=\"submit\" value=\"Cancella tutti i Documenti presenti\" />" + "</form>";
        content += "<table border=\"0\" cellpadding=\"3\" cellspacing=\"2\" class=\"filelist\">" + "<thead>" + "<tr>" + "<th class=\"filelist\" align=\"center\">Nome Documento</th>" + "<th class=\"filelist\" align=\"center\">Descrizione</th>" + "<th class=\"filelist\" align=\"center\">Operazioni</th>" + "</tr>" + "</thead>" + "<tbody>";
        Vector<String> doc = stato.getFilePresenti();
        String style = "filelisttab1";
        for (int i = 0; i < doc.size(); i++) {
            if (i % 2 == 0) style = "filelisttab1"; else style = "filelisttab2";
            content += "<tr class=\"" + style + "\">";
            content += "<td align=\"center\" valign=\"middle\"><a href=\"\" onclick=\"javascript:window.open('MultiOpen?file=" + doc.get(i) + "&type=xml','" + Hash.md5(doc.get(i)) + "','width=800,height=600,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes');\">" + doc.get(i) + "</a></td>";
            content += "<td align=\"center\" valign=\"middle\">" + stato.getFileDescription(doc.get(i)) + "</td>";
            content += "<td align=\"center\" valign=\"middle\">";
            content += "<a href=\"FileList\" onclick=\"javascript:window.open('MultiOpen?file=" + doc.get(i) + "&type=msword','" + Hash.md5(doc.get(i)) + "','width=800,height=600,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes');\">";
            content += "<img src=\"widget/main/img/msword.png\" border=\"0\" width=\"30\" height=\"30\"></a>&nbsp;&nbsp;";
            content += "<a href=\"FileList\" onclick=\"window.open('MultiOpen?file=" + doc.get(i) + "&type=preview','" + Hash.md5(doc.get(i)) + "','width=800,height=600,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes');\">";
            content += "<img src=\"widget/main/img/preview.gif\" border=\"0\" width=\"30\" height=\"30\">";
            content += "</a>&nbsp;&nbsp;";
            content += "<a href=\"FileList\" onclick=\"window.open('MultiOpen?file=" + doc.get(i) + "&type=pdf','" + Hash.md5(doc.get(i)) + "','width=800,height=600,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes');\">";
            content += "<img src=\"widget/main/img/pdf.gif\" border=\"0\" width=\"30\" height=\"30\">";
            content += "</a>&nbsp;&nbsp;";
            content += "</td>";
            content += " </tr>";
        }
        content += "</tbody>" + "</table>";
        stato.setBody(content);
        stato.setHead("");
        stato.infoBox = "In questa tabella sono riportati i documenti caricati su " + request.getServerName() + ".<br>";
        stato.infoBox += "La lista dei documenti � mantenuta solo nella sessione corrente.";
        stato.menusel = 1;
        stato.onLoad = "";
    }

    protected void Post(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Vector<String> doc = stato.getFilePresenti();
        File f;
        for (int i = 0; i < doc.size(); i++) {
            f = new File(stato.ref.getCachePath(doc.get(i)));
            f.delete();
            System.out.println("Remove:" + stato.ref.getCachePath(doc.get(i)));
        }
        stato.clearFile();
        stato.addOperation("Eliminazine di tutti i documenti");
        Get(request, response);
    }
}
