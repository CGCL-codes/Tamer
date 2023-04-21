package org.nl.prueba.printer;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author usuario
 */
public class CntrlImprimeReport implements Controller {

    private static Logger logger = Logger.getLogger(CntrlImprimeReport.class);

    /** Creates a new instance of CntrlAjaxReporteAudiometria */
    public CntrlImprimeReport() {
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int in;
        try {
            OutputStream outStream = response.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(outStream);
            File file = new File("C:\\prueba.pdf");
            dataOut.writeUTF(file.getName());
            FileInputStream fileStrm = new FileInputStream(file);
            byte fileBytes[] = new byte[1024];
            BufferedInputStream bufferStrm = new BufferedInputStream(fileStrm);
            while ((in = bufferStrm.read(fileBytes, 0, fileBytes.length)) != -1) {
                outStream.write(fileBytes, 0, fileBytes.length);
            }
            System.out.println("Archivo enviado.");
            outStream.flush();
            fileStrm.close();
            bufferStrm.close();
            outStream.close();
            dataOut.close();
            outStream = null;
            bufferStrm = null;
            fileStrm = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }
}
