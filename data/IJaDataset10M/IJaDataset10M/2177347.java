package com.endigi.frame.base.web;

import java.io.IOException;
import java.io.StringReader;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.fop.svg.PDFTranscoder;

/**
 * Servlet implementation class for the batik Transcoder
 * 
 * @Thanks przemyslaw
 */
public class HighChartsServlet extends HttpServlet {

    private static final long serialVersionUID = 3920224595120519682L;

    public HighChartsServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String type = request.getParameter("type");
        String svg = request.getParameter("svg");
        ServletOutputStream out = response.getOutputStream();
        if (null != type && null != svg) {
            svg = svg.replaceAll(":rect", "rect");
            String ext = "";
            Transcoder t = null;
            if (type.equals("image/png")) {
                ext = "png";
                t = new PNGTranscoder();
            } else if (type.equals("image/jpeg")) {
                ext = "jpg";
                t = new JPEGTranscoder();
            } else if (type.equals("application/pdf")) {
                ext = "pdf";
                t = new PDFTranscoder();
            } else if (type.equals("image/svg+xml")) {
                ext = "svg";
            }
            response.addHeader("Content-Disposition", "attachment; filename=chart." + ext);
            response.addHeader("Content-Type", type);
            if (null != t) {
                TranscoderInput input = new TranscoderInput(new StringReader(svg));
                TranscoderOutput output = new TranscoderOutput(out);
                try {
                    t.transcode(input, output);
                } catch (TranscoderException e) {
                    out.print("Problem transcoding stream. See the web logs for more details.");
                    e.printStackTrace();
                }
            } else if (ext == "svg") {
                out.print(svg);
            } else {
                out.print("Invalid type: " + type);
            }
        } else {
            response.addHeader("Content-Type", "text/html");
            out.println("Usage:\n\tParameter [svg]: The DOM Element to be converted.\n\tParameter [type]: The destination MIME type for the elment to be transcoded.");
        }
        out.flush();
        out.close();
    }
}
