package terica.file;

import terica.util.StringTokenV2;
import java.io.*;

/**

* <table border="0" width="600" cellpadding="5" cellspacing="0">
* <tr><td bgcolor="#CCCCCC"><font color="#006600">
* Dado un fichero HTML de entrada, filtra las etiquetas-html y el c�digo fuente embebido, generando un fichero TXT que s�lo contiene frases.
*<br>
* </font></tr></td>
* </table>

*
* @throws java.lang.exception

* @see <a href="http://www.luis.criado.org">�qui�n es Luis Criado?</a> 
* <br>Esta clase se utiliza en la herramienta
* @see <a href="http://sw2sws.sourceforge.net/">sw2sws</a>
* <br>basado en el 
* @see <a href="http://e-spacio.uned.es/fez/view.php?pid=tesisuned:IngInf-Lcriado">procedimiento semi-autom�tico para transformar la web en web sem�ntica</a> 
* <br>Licencia cedida con arreglo a:
* @see <a href="http://ec.europa.eu/idabc/eupl">EUPL V.1.1.1</a> 
*     
* @author Luis Criado Fern�ndez
* @since version 1.0 
* @version 1.1.2 
*/
public class html2txt {

    public html2txt(String caminoFichin, String caminoFichout) {
        try {
            FileInputStream segundaOnline = new FileInputStream(caminoFichin);
            BufferedInputStream segundaDato = new BufferedInputStream(segundaOnline);
            DataInputStream segundaLinea = new DataInputStream(segundaDato);
            FileWriter fw = new FileWriter(caminoFichout);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter salida = new PrintWriter(bw);
            String entrada = "";
            StringTokenV2 entrada2 = null, entrada3 = null;
            int inicio = 0, fin = 0;
            int vale = 0;
            String lineaFiltrada = "";
            while ((entrada = segundaLinea.readLine()) != null) {
                entrada2 = new StringTokenV2(entrada.trim(), "<");
                if (entrada2.isIncluidaSubcadena("<BODY")) {
                    inicio = 1;
                }
                if (entrada2.isIncluidaSubcadena("</BODY>") & inicio == 1) {
                    fin = 1;
                }
                if (entrada.trim().length() > 10) {
                    if (inicio == 1 & fin == 0) {
                        lineaFiltrada = entrada2.getFiltrar("<", ">").trim();
                        entrada3 = new StringTokenV2(lineaFiltrada.trim(), "<");
                        vale = 0;
                        if (entrada3.isIncluidaSubcadena("{") | entrada3.isIncluidaSubcadena("}") | entrada3.isIncluidaSubcadena("<") | entrada3.isIncluidaSubcadena(">") | entrada3.isIncluidaSubcadena("=") | entrada3.isIncluidaSubcadena("(") | entrada3.isIncluidaSubcadena(")") | entrada3.isIncluidaSubcadena("+") | entrada3.isIncluidaSubcadena("-") | entrada3.isIncluidaSubcadena("*")) {
                            vale = 1;
                        }
                        if (lineaFiltrada.length() > 15 & vale == 0) {
                            salida.write(lineaFiltrada + "\n");
                            salida.flush();
                        }
                    }
                }
            }
            segundaLinea.close();
            segundaDato.close();
            segundaOnline.close();
            salida.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
